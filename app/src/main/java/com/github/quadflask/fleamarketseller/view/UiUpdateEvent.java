package com.github.quadflask.fleamarketseller.view;

import com.github.quadflask.fleamarketseller.model.Category;
import com.github.quadflask.fleamarketseller.model.Product;

public class UiUpdateEvent {

	public static class CategoryAdded extends UiUpdateEvent {
		final Category addedCategory;

		public CategoryAdded(Category addedCategory) {
			this.addedCategory = addedCategory;
		}
	}

	public static class ProductAdded extends UiUpdateEvent {
		final Product addedProduct;

		public ProductAdded(Product addedProduct) {
			this.addedProduct = addedProduct;
		}
	}
}
