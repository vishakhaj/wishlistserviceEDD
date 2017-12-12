package com.axon.write.commands;

import java.util.Date;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class AddItemToWishlistCommand {

	@TargetAggregateIdentifier
	private String wishlistId;

	private String itemId;

	private String itemName;

	private String brandName;

	private Double rating;

	private String client;

	private String locale;
	
	private Date itemAddedOn;

	public AddItemToWishlistCommand(String wishlistId, String itemId, String itemName, String brandName, Double rating,
			String client, String locale, Date itemAddedOn) {
		System.out.println(this.getClass().toString() + "AddWishlistItemCommand instantiated");
		this.wishlistId = wishlistId;
		this.itemId = itemId;
		this.itemName = itemName;
		this.brandName = brandName;
		this.rating = rating;
		this.client = client;
		this.locale = locale;
		this.itemAddedOn = itemAddedOn;
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
	
	public Date getItemAddedOn() {
		return itemAddedOn;
	}

	@Override
	public String toString() {
		return "AddWishlistItemCommand [wishlistId=" + wishlistId + ", itemId=" + itemId + ", itemName=" + itemName
				+ ", brandName=" + brandName + ", rating=" + rating + ", client=" + client + ", locale=" + locale + "]";
	}

}
