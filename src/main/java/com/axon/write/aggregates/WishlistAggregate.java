package com.axon.write.aggregates;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.axon.common.Privacy;
import com.axon.common.Source;
import com.axon.common.Type;
import com.axon.query.entity.Item;
import com.axon.write.commands.AddItemToWishlistCommand;
import com.axon.write.commands.CreateWishlistCommand;
import com.axon.write.commands.DeleteItemFromWishlistCommand;
import com.axon.write.commands.DeleteWishlistCommand;
import com.axon.write.commands.UpdateWishlistCommand;
import com.axon.write.events.ItemAddedToWishlistEvent;
import com.axon.write.events.ItemDeletedFromWishlistEvent;
import com.axon.write.events.WishlistCreatedEvent;
import com.axon.write.events.WishlistDeletedEvent;
import com.axon.write.events.WishlistUpdatedEvent;

/*
 * Entity responsible for maintaining state.
 * Handles commands and passed it to the EventSourcingHandler.
 */
@Aggregate
public class WishlistAggregate {
	
	@AggregateIdentifier
	private String wishlistId;

	private String name;

	private String description;

	private String client;

	private String locale;

	private Source source;

	private Type type;

	private Privacy privacy;

	private String userId;

	private Date createdAt;

	private Date modifiedOn;

	//private Map<WishlistId, Map<String, Item>> wishlistItems;
	
	private String wId;
	
	private List<Item> items;

	WishlistAggregate() {
	}

	@CommandHandler
	public WishlistAggregate(CreateWishlistCommand command) {

		System.out.println("Before applying WishlistCreatedEvent in Wishlist constructor...");
		apply(new WishlistCreatedEvent(command.getWishlistId(), command.getName(), command.getDescription(),
				command.getClient(), command.getLocale(), command.getCreatedAt(), command.getSource(),
				command.getType(), command.getPrivacy(), command.getUserId(), command.getModifiedOn()));
		System.out.println("After WishlistCreatedEvent in Wishlist constructor...");
	}

	@CommandHandler
	public void handleRemoveWishlist(DeleteWishlistCommand command) {
		apply(new WishlistDeletedEvent(command.getWishlistId(), command.getUserId()));
	}

	@CommandHandler
	public void handleUpdateWishlist(UpdateWishlistCommand command) {
		apply(new WishlistUpdatedEvent(command.getWishlistId(), command.getName(), command.getDescription(),
				command.getSource(), command.getType(), command.getPrivacy(), command.getUserId(),
				command.getModifiedOn()));
	}

	@CommandHandler
	public void handleAddItemToWishlistCommand(AddItemToWishlistCommand command) {
		apply(new ItemAddedToWishlistEvent(command.getWishlistId(), command.getItemId(), command.getItemName(),
				command.getBrandName(), command.getRating(), command.getClient(), command.getLocale(), command.getItemAddedOn()));
	}
	
	@CommandHandler
	public void handleDeleteItemFromWishlistCommand(DeleteItemFromWishlistCommand command){
		apply(new ItemDeletedFromWishlistEvent(command.getWishlistId(), command.getItemId(), command.getItemName(), 
				command.getBrandName(), command.getRating(), command.getClient(), command.getLocale(), command.getItemDeletedOn()));
	}

	@EventSourcingHandler
	public void applyWishlistCreatedEvent(WishlistCreatedEvent event) {
		System.out.println("Handle WishlistCreatedEvent...");
		this.wishlistId = event.getWishlistId().toString();
		this.name = event.getName();
		this.description = event.getDescription();
		this.client = event.getClient();
		this.locale = event.getLocale();
		this.createdAt = event.getCreatedAt();
		this.source = event.getSource();
		this.type = event.getType();
		this.privacy = event.getPrivacy();
		this.userId = event.getUserId();
		this.modifiedOn = event.getModifiedOn();
	}

	@EventSourcingHandler
	public void applyWishlistUpdatedEvent(WishlistUpdatedEvent event) {
		System.out.println("Handle WishlistUpdatedEvent...");
		this.wishlistId = event.getWishlistId();
		this.name = event.getName();
		this.description = event.getDescription();
		this.source = event.getSource();
		this.type = event.getType();
		this.privacy = event.getPrivacy();
		this.userId = event.getUserId();
		this.modifiedOn = event.getModifiedOn();
	}

	@EventSourcingHandler
	public void applyWislistRemovedEvent(WishlistDeletedEvent event) {
		System.out.println("Handle WishlistDeletedEvent...");
		this.wId = event.getWishlistId();
		this.userId = event.getUserId();
	}

	@EventSourcingHandler
	public void applyItemAddedToWishlistEvent(ItemAddedToWishlistEvent event) {
		List<Item> itemList = new ArrayList<>();
		itemList.add(new Item(event.getItemId(), event.getItemName(), event.getBrandName(), event.getRating(), event.getLocale(), event.getClient(), event.getItemAddedOn()));
		System.out.println("Handle WishlistItemAddedToWishlistEvent...");
		this.wishlistId = event.getWishlistId();
		this.items = itemList;

//		Map<String, Item> mapByItemId = wishlistItems.get(event.getWishlistId());
//		if (mapByItemId == null) {
//			wishlistItems.put(event.getWishlistId(), mapByItemId = new HashMap<>());
//		}
//
//		Item item = mapByItemId.get(event.getItemId());
//		if (item == null) {
//			mapByItemId.put(event.getItemId(), new Item(event.getItemId(), event.getItemName(), event.getBrandName(),
//					event.getRating(), event.getClient(), event.getLocale()));
//		}

	}
	
	@EventSourcingHandler
	public void applyItemDeletedFromWishlistEvent(ItemDeletedFromWishlistEvent event){
		List<Item> itemList=  new ArrayList<>();
		System.out.println("event.getItemName(): " + event.getItemName());
		itemList.add(new Item(event.getItemId(), event.getItemName(), event.getBrandName(), event.getRating(),
				event.getClient(), event.getLocale(), event.getItemDeletedOn()));
		System.out.println("Handle ItemDeletedFromWishlistEvent...");
		this.wishlistId = event.getWishlistId();
		this.items = itemList;
		
	}

	@Override
	public String toString() {
		return "WishlistAggregate [wishlistId=" + wishlistId + ", name=" + name + ", description=" + description
				+ ", client=" + client + ", locale=" + locale + ", source=" + source + ", type=" + type + ", privacy="
				+ privacy + ", userId=" + userId + ", createdAt=" + createdAt + ", modifiedOn=" + modifiedOn
				+ ", items=" + items + "]";
	}

}