package com.github.quadflask.fleamarketseller.actions;

import com.github.quadflask.fleamarketseller.dispatcher.Dispatcher;
import com.github.quadflask.fleamarketseller.model.Category;
import com.github.quadflask.fleamarketseller.model.Product;

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

}
