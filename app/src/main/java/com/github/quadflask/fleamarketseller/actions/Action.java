package com.github.quadflask.fleamarketseller.actions;

import com.github.quadflask.fleamarketseller.model.Category;
import com.github.quadflask.fleamarketseller.model.Market;
import com.github.quadflask.fleamarketseller.model.Product;
import com.github.quadflask.fleamarketseller.model.Transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

@Builder
@Getter
@Setter
public class Action {
	protected String type;
	protected Object data;

	public static class CreateCategory {
		public final Category category;

		public CreateCategory(Category category) {
			this.category = category;
		}
	}

	public static class CreateProduct {
		public final Product product;

		public CreateProduct(Product product) {
			this.product = product;
		}
	}

	public static class CreateTransaction {
		public final Transaction transaction;

		public CreateTransaction(Transaction transaction) {
			this.transaction = transaction;
		}
	}

	public static class CreateMarket {
		public final Market market;

		public CreateMarket(Market market) {
			this.market = market;
		}
	}

	public static class EditProduct {
		public final Product product;

		public EditProduct(Product product) {
			this.product = product;
		}
	}

	public static class EditCategory {
		public final Category category;

		public EditCategory(Category category) {
			this.category = category;
		}
	}

	public static class EditMarket {
		public final Market market;

		public EditMarket(Market market) {
			this.market = market;
		}
	}

	public static class DeleteCategory {
		public final Long categoryId;

		public DeleteCategory(Long categoryId) {
			this.categoryId = categoryId;
		}
	}

	public static class DeleteProduct {
		public final Long productId;

		public DeleteProduct(Long productId) {
			this.productId = productId;
		}
	}
}
