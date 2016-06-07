package com.github.quadflask.fleamarketseller.store;

public class ModelValidationException extends RuntimeException {
	public ModelValidationException(String detailMessage) {
		super(detailMessage);
	}
}
