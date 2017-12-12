package com.axon.write.commands;

import java.util.Date;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class DeleteItemFromWishlistCommand {

	@TargetAggregateIdentifier
	private String wishlistId;

	private String itemId;

	private String itemName;

	private String brandName;

	private Double rating;

	private String client;

	private String locale;

	private Date itemDeletedOn;

	public DeleteItemFromWishlistCommand(String wishlistId, String itemId, String itemName, String brandName,
			Double rating, String client, String locale, Date itemDeletedOn) {
		System.out.println("DeleteItemFromWishlistCommand instatiated...");
		this.wishlistId = wishlistId;
		this.itemId = itemId;
		this.itemName = itemName;
		this.brandName = brandName;
		this.rating = rating;
		this.client = client;
		this.locale = locale;
		this.itemDeletedOn = itemDeletedOn;
	}

	public String getWishlistId() {
		return wishlistId;
	}

	public String getItemId() {
		return itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public String getBrandName() {
		return brandName;
	}

	public Double getRating() {
		return rating;
	}

	public String getClient() {
		return client;
	}

	public String getLocale() {
		return locale;
	}

	public Date getItemDeletedOn() {
		return itemDeletedOn;
	}

}
