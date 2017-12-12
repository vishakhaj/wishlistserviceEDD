package com.axon.write.commands;

import java.util.Date;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import com.axon.common.Privacy;
import com.axon.common.Source;
import com.axon.common.Type;

public class UpdateWishlistCommand {

	@TargetAggregateIdentifier
	private String wishlistId;

	private String name;

	private String description;

	private Source source;

	private Type type;

	private Privacy privacy;

	private String userId;

	private Date createdAt;

	private Date modifiedOn;

	public UpdateWishlistCommand() {
	}

	public UpdateWishlistCommand(String wishlistId, String name, String description,
			Source source, Type type, Privacy privacy, String userId, Date modifiedOn) {
		System.out.println("UpdateWishlistCommand Instatiated...");
		this.wishlistId = wishlistId;
		this.name = name;
		this.description = description;
		this.source = source;
		this.type = type;
		this.privacy = privacy;
		this.userId = userId;
		this.modifiedOn = modifiedOn;
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
		return "UpdateWishlistCommand [wishlistId=" + wishlistId + ", name=" + name + ", description=" + description
				+ ", source=" + source + ", type=" + type + ", privacy=" + privacy + ", userId=" + userId
				+ ", createdAt=" + createdAt + ", modifiedOn=" + modifiedOn + "]";
	}

}
