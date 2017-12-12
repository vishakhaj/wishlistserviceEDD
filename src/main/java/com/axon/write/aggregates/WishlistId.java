package com.axon.write.aggregates;

import org.axonframework.common.IdentifierFactory;

public class WishlistId {

	private String identifier;

	public WishlistId() {
		this.identifier = IdentifierFactory.getInstance().generateIdentifier();
	}
	
	public WishlistId(String identifier) {
		this.identifier = identifier;
		if (identifier == null) {
			throw new IllegalArgumentException("Identifier may not be null");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		WishlistId wishlistId = (WishlistId) o;

		return identifier.equals(wishlistId.identifier);

	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}

	@Override
	public String toString() {
		return identifier.toString();
	}

	public String getIdentifier() {
		return identifier;
	}

}
