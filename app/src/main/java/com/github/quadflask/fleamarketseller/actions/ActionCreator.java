package com.github.quadflask.fleamarketseller.actions;

import com.github.quadflask.fleamarketseller.dispatcher.Dispatcher;
import com.github.quadflask.fleamarketseller.model.Category;
import com.github.quadflask.fleamarketseller.model.Market;
import com.github.quadflask.fleamarketseller.model.Product;
import com.github.quadflask.fleamarketseller.model.Transaction;

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

	public void editProduct(String oldName, Product product) {
		dispatcher.dispatch(new Action.EditProduct(oldName, product));
	}

	public void editCategory(String oldName, Category category) {
		dispatcher.dispatch(new Action.EditCategory(oldName, category));
	}

	public void editMarket(String oldName, Market market) {
		dispatcher.dispatch(new Action.EditMarket(oldName, market));
	}
}
