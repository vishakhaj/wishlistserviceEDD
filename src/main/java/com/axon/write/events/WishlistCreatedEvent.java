package com.axon.write.events;

import java.util.Date;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.axon.common.Privacy;
import com.axon.common.Source;
import com.axon.common.Type;
import com.axon.write.aggregates.WishlistId;

public class WishlistCreatedEvent {

	@TargetAggregateIdentifier
	private WishlistId wishlistId;

	private String name;

	private String description;

	private String client;

	private String locale;

	private Date createdAt;

	private Source source;

	private Type type;

	private Privacy privacy;

	private String userId;

	private Date modifiedOn;

	public WishlistCreatedEvent(WishlistId wishlistId, String name, String description, String client, String locale,
			Date createdAt, Source source, Type type, Privacy privacy, String userId, Date modifiedOn) {
		System.out.println(this.getClass().getName() + ": instantiated WishlistCreatedEvent");
		this.wishlistId = wishlistId;
		this.name = name;
		this.description = description;
		this.client = client;
		this.locale = locale;
		this.createdAt = createdAt;
		this.source = source;
		this.type = type;
		this.privacy = privacy;
		this.userId = userId;
		this.modifiedOn = modifiedOn;
	}

	public WishlistId getWishlistId() {
		return wishlistId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getClient() {
		return client;
	}

	public String getLocale() {
		return locale;
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

	public String getUserId() {
		return userId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	@Override
	public String toString() {
		return "WishlistCreatedEvent [wishlistId=" + wishlistId + ", name=" + name + ", description=" + description
				+ ", client=" + client + ", locale=" + locale + ", createdAt=" + createdAt + ", source=" + source
				+ ", type=" + type + ", privacy=" + privacy + ", userId=" + userId + ", modifiedOn=" + modifiedOn + "]";
	}

}