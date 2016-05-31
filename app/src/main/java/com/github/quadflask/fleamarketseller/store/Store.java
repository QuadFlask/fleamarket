package com.github.quadflask.fleamarketseller.store;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.quadflask.fleamarketseller.FleamarketApplication;
import com.github.quadflask.fleamarketseller.actions.Action;
import com.github.quadflask.fleamarketseller.dispatcher.Dispatcher;
import com.github.quadflask.fleamarketseller.model.Category;
import com.github.quadflask.fleamarketseller.model.Market;
import com.github.quadflask.fleamarketseller.model.Product;
import com.github.quadflask.fleamarketseller.model.Vendor;
import com.github.quadflask.fleamarketseller.view.UiUpdateEvent;
import com.google.common.base.Strings;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import lombok.val;
import rx.Observer;

public class Store implements Observer {

	private Dispatcher dispatcher;

	public Store(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void onNext(final Object action) {
		if (action instanceof Action.CreateCategory) {
			val _action = (Action.CreateCategory) action;
			val category = _action.category;

			if (!Strings.isNullOrEmpty(category.getParentName())
					&& findParentCategoryByName(category.getParentName()) == null) {
				insertWith(realm -> realm.copyToRealm(Category.builder()
						.date(new Date())
						.parentName(null)
						.name(category.getParentName())
						.build()));
			}

			category.setParent(findParentCategoryByName(category.getParentName()));

			insertWith(realm -> {
				if (category.getDate() == null)
					category.setDate(new Date());
				realm.copyToRealm(category);
			});

			emitStoreChange();
			emitUiUpdate(new UiUpdateEvent.CategoryAdded(category));
		} else if (action instanceof Action.CreateProduct) {
			val _action = (Action.CreateProduct) action;
			val product = _action.product;

			product.setCategory(findCategoryByName(product.getCategoryName()));

			insertWith(realm -> {
				if (product.getDate() == null)
					product.setDate(new Date());
				realm.copyToRealm(product);
			});

			emitStoreChange();
			emitUiUpdate(new UiUpdateEvent.ProductAdded(product));
		} else if (action instanceof Action.CreateTransaction) {
			val _action = (Action.CreateTransaction) action;
			val transaction = _action.transaction;

			transaction.setMarket(findMarketByName(transaction.getMarketName()));
			transaction.setVendor(findVendorByName(transaction.getVendorName()));

			insertWith(realm -> {
				if (transaction.getDate() == null)
					transaction.setDate(new Date());
				realm.copyToRealm(transaction);
			});

			emitStoreChange();
			emitUiUpdate(new UiUpdateEvent.TransactionAdded(transaction));
		} else if (action instanceof Action.EditProduct) {
			val _action = (Action.EditProduct) action;
			val editedProduct = _action.product;
			Product product = findProductByName(_action.oldName);

			if (product != null) {
				product.setName(editedProduct.getName());
				product.setPrice(editedProduct.getPrice());
				product.setText(editedProduct.getText());
				product.setCategory(findCategoryByName(editedProduct.getCategoryName()));
			}
		}
	}

	private Product findProductByName(String name) {
		if (Strings.isNullOrEmpty(name)) return null;
		return FleamarketApplication.realm()
				.where(Product.class)
				.equalTo("name", name)
				.findFirst();
	}

	private Vendor findVendorByName(String vendorName) {
		if (Strings.isNullOrEmpty(vendorName)) return null;
		return FleamarketApplication.realm()
				.where(Vendor.class)
				.equalTo("name", vendorName)
				.findFirst();
	}

	private Market findMarketByName(String marketName) {
		if (Strings.isNullOrEmpty(marketName)) return null;
		return FleamarketApplication.realm()
				.where(Market.class)
				.equalTo("name", marketName)
				.findFirst();
	}

	private Category findParentCategoryByName(String parentName) {
		if (Strings.isNullOrEmpty(parentName)) return null;
		return FleamarketApplication.realm()
				.where(Category.class)
				.isNull("parent")
				.equalTo("name", parentName)
				.findFirst();
	}

	private Category findCategoryByName(String name) {
		if (Strings.isNullOrEmpty(name)) return null;
		return FleamarketApplication.realm()
				.where(Category.class)
				.equalTo("name", name)
				.findFirst();
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

	public List<String> loadParentCategoryNames() {
		val parents = FleamarketApplication.realm()
				.where(Category.class)
				.isNull("parent")
				.findAll();

		return toCategoryNames(parents);
	}

	public List<String> loadCategoryNames() {
		val parents = FleamarketApplication.realm()
				.where(Category.class)
				.isNotNull("parent")
				.findAll();

		return toCategoryNames(parents);
	}

	public RealmResults<Product> loadProducts() {
		return FleamarketApplication.realm()
				.where(Product.class)
				.findAll();
	}

	public RealmResults<Market> loadMarkets() {
		return FleamarketApplication.realm()
				.where(Market.class)
				.findAll();
	}

	public RealmResults<Vendor> loadVendors() {
		return FleamarketApplication.realm()
				.where(Vendor.class)
				.findAll();
	}

	private List<String> toCategoryNames(RealmResults<Category> parents) {
		return Stream
				.of(parents)
				.map(Category::getName)
				.collect(Collectors.toList());
	}

	public static class StoreChangeEvent {
	}

	private interface RealmInsert {
		void insert(Realm realm);
	}

	private void insertWith(RealmInsert realmInsert) {
		Realm realm = FleamarketApplication.realm();
		try {
			realm.beginTransaction();
			realmInsert.insert(realm);
			realm.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			realm.cancelTransaction();
		}
	}
}
