package com.github.quadflask.fleamarketseller.actions;

import com.github.quadflask.fleamarketseller.model.Category;
import com.github.quadflask.fleamarketseller.model.Product;

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
}
