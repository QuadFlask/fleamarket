package com.github.quadflask.fleamarketseller.actions;

import com.github.quadflask.fleamarketseller.dispatcher.Dispatcher;
import com.github.quadflask.fleamarketseller.model.Category;
import com.github.quadflask.fleamarketseller.model.Market;
import com.github.quadflask.fleamarketseller.model.Product;
import com.github.quadflask.fleamarketseller.model.Transaction;
import com.github.quadflask.fleamarketseller.model.Vendor;

public class ActionCreator {
	Dispatcher dispatcher;

	public ActionCreator(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void newCategory(Category category) {
		dispatcher.dispatch(new Action.CreateCategory(category));
	}

	public void newProduct(Product product) {
		dispatcher.dispatch(new Action.CreateProduct(product));
	}

	public void newMarket(Market market) {
		dispatcher.dispatch(new Action.CreateMarket(market));
	}

	public void newTransaction(Transaction transaction) {
		dispatcher.dispatch(new Action.CreateTransaction(transaction));
	}

	public void editProduct(Product product) {
		dispatcher.dispatch(new Action.EditProduct(product));
	}

	public void editCategory(Category category) {
		dispatcher.dispatch(new Action.EditCategory(category));
	}

	public void editMarket(Market market) {
		dispatcher.dispatch(new Action.EditMarket(market));
	}

	public void deleteCategory(Long categoryId) {
		dispatcher.dispatch(new Action.DeleteCategory(categoryId));
	}

	public void deleteProduct(Long productId) {
		dispatcher.dispatch(new Action.DeleteProduct(productId));
	}

	public void newVendor(Vendor vendor) {
		dispatcher.dispatch(new Action.CreateVendor(vendor));
	}

	public void editVendor(Vendor vendor) {
		dispatcher.dispatch(new Action.EditVendor(vendor));
	}
}
