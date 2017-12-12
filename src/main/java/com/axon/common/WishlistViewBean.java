package com.axon.common;

import java.time.LocalDateTime;
import java.util.List;

import com.axon.query.entity.Item;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class WishlistViewBean {

	private String wishlistId;

	private String name;

	private String description;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createdAt;

	private String client;

	private String locale;

	private Source source;

	private Type type;

	private Privacy privacy;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime modifiedOn;

	private String userId;

	private boolean emptyWishlist;

	private List<Item> items;
	
	public WishlistViewBean() {
		this(false);
	}

	public WishlistViewBean(String wishlistId, String name, String description, LocalDateTime createdAt, String client,
			String locale, Source source, Type type, Privacy privacy, LocalDateTime modifiedOn, String userId, List<Item> items) {
		this.wishlistId = wishlistId;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.client = client;
		this.locale = locale;
		this.source = source;
		this.type = type;
		this.privacy = privacy;
		this.modifiedOn = modifiedOn;
		this.userId = userId;
		this.items = items;
	}

	public WishlistViewBean(boolean emptyWishlist) {
		this.emptyWishlist = emptyWishlist;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setPrivacy(Privacy privacy) {
		this.privacy = privacy;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	// public void setEmptyWishlist(boolean emptyWishlist) {
	// this.emptyWishlist = emptyWishlist;
	// }

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getWishlistId() {
		return wishlistId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setWishlistId(String wishlistId) {
		this.wishlistId = wishlistId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getClient() {
		return client;
	}

	public Source getSource() {
		return source;
	}

	public Type getType() {
		return type;
	}

	public Privacy getPrivacy() {
		return privacy;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public String getUserId() {
		return userId;
	}

	public boolean isEmptyWishlist() {
		return emptyWishlist;
	}

	public List<Item> getItems() {
		return items;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Override
	public String toString() {
		return "WishlistViewBean [wishlistId=" + wishlistId + ", name=" + name + ", description=" + description + "]";
	}

}
