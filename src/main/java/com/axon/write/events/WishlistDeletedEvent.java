package com.axon.write.events;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.axon.write.aggregates.WishlistId;

public class WishlistDeletedEvent {

	@TargetAggregateIdentifier
	private String wishlistId;

	private String userId;

	public WishlistDeletedEvent(String wishlistId, String userId) {
		System.out.println("Instatiated WishlistDeletedEvent...");
		this.wishlistId = wishlistId;
		this.userId = userId;
	}

	public String getWishlistId() {
		return wishlistId;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return "WishlistDeletedEvent [wishlistId=" + wishlistId + ", userId=" + userId + "]";
	}

}
