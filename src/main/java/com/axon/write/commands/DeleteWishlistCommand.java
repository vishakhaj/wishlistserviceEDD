package com.axon.write.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.axon.query.entity.Item;
import com.axon.write.aggregates.WishlistId;

public class DeleteWishlistCommand {

	@TargetAggregateIdentifier
	private String wishlistId;

	private String userId;

	private String wId;
	
	public DeleteWishlistCommand(String wishlistId, String userId) {
		System.out.println("Instatiated DeleteWishlistCommand...");
		this.wishlistId = wishlistId;
		this.userId = userId;
	}

//	public DeleteWishlistCommand(String wishlistId, String userId) {
//		System.out.println("Instatiated DeleteWishlistCommand...");
//		this.wId = wishlistId;
//		this.userId = userId;
//	}

	public String getWishlistId() {
		return wishlistId;
	}

	public String getUserId() {
		return userId;
	}

	public String getwId() {
		return wId;
	}

	@Override
	public String toString() {
		return "DeleteWishlistCommand [wishlistId=" + wishlistId + ", userId=" + userId + "]";
	}

}
