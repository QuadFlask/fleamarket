package com.github.quadflask.fleamarketseller.view;

import com.github.quadflask.fleamarketseller.model.Category;
import com.github.quadflask.fleamarketseller.model.Product;
import com.github.quadflask.fleamarketseller.model.Transaction;

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

	public static class TransactionAdded extends UiUpdateEvent {
		final Transaction transaction;

		public TransactionAdded(Transaction transaction) {
			this.transaction = transaction;
		}
	}

	public static class ProductUpdated extends UiUpdateEvent {
		final Product product;

		public ProductUpdated(Product product) {
			this.product = product;
		}
	}

	public static class CategoryUpdated extends UiUpdateEvent {
		final Category category;

		public CategoryUpdated(Category category) {
			this.category = category;
		}
	}
}
