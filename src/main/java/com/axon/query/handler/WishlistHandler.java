package com.axon.query.handler;

import java.util.ArrayList;
/*
 * Handles and stores events to the database
 */
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.axon.query.entity.Item;
import com.axon.query.entity.Wishlist;
import com.axon.query.repository.WishlistQueryRepository;
import com.axon.write.events.WishlistCreatedEvent;
import com.axon.write.events.WishlistDeletedEvent;
import com.axon.write.events.WishlistUpdatedEvent;

@Component
public class WishlistHandler {

	private final WishlistQueryRepository repository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public WishlistHandler(WishlistQueryRepository repository) {
		System.out.println("Instantiate Wishlist Handler...");
		this.repository = repository;
	}

	@EventHandler
	public void onWishlistCreate(WishlistCreatedEvent event) {
		System.out.println("Adding data in Wishlist database...");
		Wishlist wishlist = repository.save(new Wishlist(event.getWishlistId(), event.getName(), event.getDescription(), event.getClient(),
				event.getLocale(), event.getCreatedAt(), event.getSource(), event.getType(), event.getPrivacy(),
				event.getUserId(), event.getModifiedOn(), new ArrayList<Item>()));
		System.out.println("added wishlist: " + wishlist);
		System.out.println("Data added in database...");
	}

	@EventHandler
	public void onWishlistUpdate(WishlistUpdatedEvent event) {
		System.out.println("Adding data in Wishlist database...");
		Wishlist wishlist = repository.findOne(event.getWishlistId().toString());
		wishlist.setName(event.getName());
		wishlist.setDescription(event.getDescription());
		wishlist.setSource(event.getSource());
		wishlist.setType(event.getType());
		wishlist.setPrivacy(event.getPrivacy());
		wishlist.setModifiedOn(event.getModifiedOn());
		repository.save(wishlist);
		System.out.println("Data added in database...");

	}

	@EventHandler
	public void onWishlistDelete(WishlistDeletedEvent event) {
		Wishlist wishlist = entityManager.find(Wishlist.class, event.getWishlistId().toString());
		if (Objects.equals(event.getWishlistId().toString(), wishlist.getWishlistId())) {
			entityManager.remove(wishlist);
		}
		
		System.out.println("Data removed in database...");
	}

}