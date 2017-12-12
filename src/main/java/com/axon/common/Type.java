package com.axon.common;

public enum Type {

	DEFAULT("default");

	private String type;

	private Type(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
