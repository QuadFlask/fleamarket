package com.github.quadflask.fleamarketseller.store;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.quadflask.fleamarketseller.actions.Action;
import com.github.quadflask.fleamarketseller.dispatcher.Dispatcher;
import com.github.quadflask.fleamarketseller.model.Category;
import com.github.quadflask.fleamarketseller.model.Market;
import com.github.quadflask.fleamarketseller.model.Product;
import com.github.quadflask.fleamarketseller.model.Transaction;
import com.github.quadflask.fleamarketseller.model.Vendor;
import com.github.quadflask.fleamarketseller.view.UiUpdateEvent;
import com.google.common.base.Strings;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;
import lombok.val;
import rx.Observable;
import rx.Observer;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.realm;

public class Store implements Observer {

	private Dispatcher dispatcher;

	public Store(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	Realm.Transaction.OnError onInsertCommonError = error -> {
		// TODO
		// emitUiUpdate(new UiUpdateEvent.CategoryAddFail(category));
	};

	@Override
	public void onNext(final Object action) {
		if (action instanceof Action.CreateCategory) {
			val _action = (Action.CreateCategory) action;
			val category = _action.category;
			val parentName = category.getParentName();

			if (!Strings.isNullOrEmpty(parentName)
					&& findParentCategoryByName(parentName) == null) {

				realm().executeTransaction(
						realm -> realm.copyToRealm(Category.builder()
								.id(nextKey(Category.class))
								.name(parentName)
								.date(new Date())
								.color(ColorFactory.nextColor())
								.parentName(null)
								.build()));
			}
			category.setId(nextKey(Category.class));
			category.setDate(new Date());
			category.setColor(ColorFactory.nextColor());
			category.setParent(findParentCategoryByName(parentName));

			realm().executeTransaction(realm -> {
				Category persist = realm.copyToRealm(category);

				emitStoreChange();
				emitUiUpdate(new UiUpdateEvent.CategoryAdded(persist));
			});

		} else if (action instanceof Action.CreateProduct) {
			val _action = (Action.CreateProduct) action;
			val product = _action.product;

			product.setId(nextKey(Product.class));
			product.setDate(new Date());

			realm().executeTransaction(realm -> {
				product.setCategory(findCategoryByName(realm, product.getCategoryName()));

				Product persist = realm.copyToRealm(product);

				emitStoreChange();
				emitUiUpdate(new UiUpdateEvent.ProductAdded(persist));
			});

		} else if (action instanceof Action.CreateTransaction) {
			val _action = (Action.CreateTransaction) action;
			val transaction = _action.transaction;

			transaction.setProduct(findProductByName(transaction.getProductName()));
			transaction.setMarket(findMarketByName(transaction.getMarketName()));
			transaction.setVendor(findVendorByName(transaction.getVendorName()));
			transaction.setId(nextKey(Transaction.class));
			transaction.setDate(new Date());

			realm().executeTransaction(realm -> {
				Transaction persist = realm.copyToRealm(transaction);

				emitStoreChange();
				emitUiUpdate(new UiUpdateEvent.TransactionAdded(persist));
			});

		} else if (action instanceof Action.CreateMarket) {
			val _action = (Action.CreateMarket) action;
			val market = _action.market;

			realm().executeTransaction(realm -> {
				market.setDate(new Date());
				market.setId(nextKey(Market.class));
				market.setColor(ColorFactory.nextColor());
				realm.copyToRealm(market);

				emitStoreChange();
				emitUiUpdate(new UiUpdateEvent.MarketAdded(market));
			});

		} else if (action instanceof Action.EditProduct) {
			val _action = (Action.EditProduct) action;
			val editedProduct = _action.product;
			Product product = findProductByName(_action.targetProductName);

			if (product != null) {
				realm().executeTransaction(realm -> {
					product.setName(editedProduct.getName());
					product.setPrice(editedProduct.getPrice());
					product.setText(editedProduct.getText());
					product.setCategory(findCategoryByName(editedProduct.getCategoryName()));

					emitStoreChange();
					emitUiUpdate(new UiUpdateEvent.ProductUpdated(product));
				});
			}
		} else if (action instanceof Action.EditCategory) {
			val _action = (Action.EditCategory) action;
			val editedCategory = _action.category;
			Category category = findCategoryById(editedCategory.getId());

			if (category != null) {
				realm().executeTransaction(realm -> {
					category.setName(editedCategory.getName());
					category.setParent(findCategoryByName(editedCategory.getParentName()));

					emitStoreChange();
					emitUiUpdate(new UiUpdateEvent.CategoryUpdated(category));
				});
			}
		} else if (action instanceof Action.EditMarket) {
			val _action = (Action.EditMarket) action;
			val editedMarket = _action.market;
			val market = findMarketByName(_action.targetMarketName);

			if (market != null) {
				realm().executeTransaction(realm -> {
					market.setName(editedMarket.getName());
					market.setLocation(editedMarket.getLocation());

					emitStoreChange();
					emitUiUpdate(new UiUpdateEvent.MarketUpdated(market));
				});
			}
		} else if (action instanceof Action.DeleteCategory) {
			val _action = (Action.DeleteCategory) action;
			realm().executeTransactionAsync(realm -> {
				// TODO if delete parent category, should delete cascade
				Category category = findCategoryById(realm, _action.categoryId);
				if (category != null)
					category.deleteFromRealm();
			}, () -> {
				emitStoreChange();
				emitUiUpdate(new UiUpdateEvent.CategoryDeleted());
			});
		} else if (action instanceof Action.DeleteProduct) {
			val _action = (Action.DeleteProduct) action;
			realm().executeTransactionAsync(realm -> {
				Product product = findProductByName(realm, _action.productName);
				if (product != null)
					product.deleteFromRealm();
			}, () -> {
				emitStoreChange();
				emitUiUpdate(new UiUpdateEvent.ProductDeleted());
			});
		}
	}

	private long nextKey(Class<? extends RealmModel> clazz) {
		return PrimaryKeyFactory.getInstance().nextKey(clazz);
	}

	private void emitUiUpdate(UiUpdateEvent uiUpdateEvent) {
		dispatcher.emitUiUpdate(uiUpdateEvent);
	}

	private void emitStoreChange() {
		dispatcher.emitChange(new StoreChangeEvent());
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onCompleted() {
	}

	public Product findProductByName(String name) {
		return findProductByName(realm(), name);
	}

	private Product findProductByName(Realm realm, String name) {
		if (Strings.isNullOrEmpty(name)) return null;
		return realm()
				.where(Product.class)
				.equalTo("name", name)
				.findFirst();
	}

	private Vendor findVendorByName(String vendorName) {
		if (Strings.isNullOrEmpty(vendorName)) return null;
		return realm()
				.where(Vendor.class)
				.equalTo("name", vendorName)
				.findFirst();
	}

	private Market findMarketByName(String marketName) {
		if (Strings.isNullOrEmpty(marketName)) return null;
		return realm()
				.where(Market.class)
				.equalTo("name", marketName)
				.findFirst();
	}

	private Category findParentCategoryByName(Realm realm, String parentName) {
		if (Strings.isNullOrEmpty(parentName)) return null;
		return realm
				.where(Category.class)
				.isNull("parent")
				.equalTo("name", parentName)
				.findFirst();
	}

	private Category findCategoryByName(Realm realm, String categoryName) {
		if (Strings.isNullOrEmpty(categoryName)) return null;
		return realm
				.where(Category.class)
				.equalTo("name", categoryName)
				.findFirst();
	}

	private Category findParentCategoryByName(String parentName) {
		return findParentCategoryByName(realm(), parentName);
	}

	public Category findCategoryById(long id) {
		return findCategoryById(realm(), id);
	}

	public Category findCategoryById(Realm realm, long id) {
		if (id < 0) return null;
		return realm
				.where(Category.class)
				.equalTo("id", id)
				.findFirst();
	}

	public Category findCategoryByName(String name) {
		if (Strings.isNullOrEmpty(name)) return null;
		return realm()
				.where(Category.class)
				.equalTo("name", name)
				.findFirst();
	}

	public RealmResults<Category> loadAllCategories() {
		return realm()
				.where(Category.class)
				.findAll();
	}

	public List<String> loadParentCategoryNames() {
		val parents = realm()
				.where(Category.class)
				.isNull("parent")
				.findAll();

		return toCategoryNames(parents);
	}

	public List<String> loadCategoryNames() {
		val parents = realm()
				.where(Category.class)
				.isNotNull("parent")
				.findAll();

		return toCategoryNames(parents);
	}

	public RealmResults<Product> loadProducts() {
		return realm()
				.where(Product.class)
				.findAll();
	}

	public RealmResults<Market> loadMarkets() {
		return realm()
				.where(Market.class)
				.findAll();
	}

	public RealmResults<Vendor> loadVendors() {
		return realm()
				.where(Vendor.class)
				.findAll();
	}

	public RealmResults<Transaction> loadTransactions() {
		return realm()
				.where(Transaction.class)
				.findAll();
	}

	public Observable<RealmResults<Transaction>> loadTransactionsByIncome(boolean isIncome) {
		return realm()
				.where(Transaction.class)
				.equalTo("isIncome", isIncome)
				.findAll()
				.asObservable();
	}

	private List<String> toCategoryNames(RealmResults<Category> parents) {
		return Stream
				.of(parents)
				.map(Category::getName)
				.collect(Collectors.toList());
	}

	public void checkValid(Category category) {
		if (category == null) throw new ModelValidationException("카테고리가 없습니다");
		if (Strings.isNullOrEmpty(category.getName()))
			throw new ModelValidationException("이름이 없습니다");
		Category categoryByName = findCategoryByName(category.getName());
		boolean isEdit = category.getId() == null;
		if (isEdit && categoryByName != null) {
			if (!categoryByName.getId().equals(category.getId()))
				throw new ModelValidationException("수정할 카테고리와 일치하지 않습니다");
		} else if (categoryByName == null)
			throw new ModelValidationException("수정할 원본 카테고리가 없습니다");
		else return;
		throw new ModelValidationException("알 수 없는 에러");
	}

	public static class StoreChangeEvent {
	}
}
