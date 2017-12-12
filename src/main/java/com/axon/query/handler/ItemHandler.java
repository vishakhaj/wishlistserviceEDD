package com.axon.query.handler;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.axon.query.entity.Item;
import com.axon.query.entity.Wishlist;
import com.axon.query.repository.ItemRepository;
import com.axon.query.repository.WishlistQueryRepository;
import com.axon.write.events.ItemAddedToWishlistEvent;
import com.axon.write.events.ItemDeletedFromWishlistEvent;

@Component
public class ItemHandler {

	@Autowired
	private ItemRepository itemRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private WishlistQueryRepository repository;
	
	//private List<Item> itemList = new ArrayList<>();

	public ItemHandler(WishlistQueryRepository repository) {
		System.out.println(this.getClass().toString() + "### instanciate ItemHandler");
		this.repository = repository;
	}

	@EventHandler
	public void onItemAdd(ItemAddedToWishlistEvent event) throws URISyntaxException {
		System.out.println(this.getClass().toString() + "### handle WishlistItemAddedToWishlistEvent");
		Wishlist wishlist = repository.findOne(event.getWishlistId().toString());
		
		Item item = new Item(event.getItemId(), event.getItemName(), event.getBrandName(), event.getRating(),
				event.getClient(), event.getLocale(), new Date());
		//itemList.add(item);
		if (wishlist.getItems().isEmpty()) {
			wishlist.addWishlistItem(item);
			repository.save(wishlist);
		}else{
			wishlist.addWishlistItem(item);
			repository.save(wishlist);
		}
	}

	@EventHandler
	public void onItemDelete(ItemDeletedFromWishlistEvent event) throws URISyntaxException {
		Item emItem = entityManager.find(Item.class, event.getItemId());
		System.out.println(this.getClass().toString() + "### handle ItemDeletedFromWishlistEvent");
		Wishlist wishlist = repository.findOne(event.getWishlistId().toString());
		itemRepository.cacheAllItems();
		List<Item> items = itemRepository.findAllItems();
		if(items.isEmpty()){
			System.out.println("no items present");
		}else{
			Item item = itemRepository.findItemByItemId(event.getItemId());
			System.out.println("item: " + item);
			wishlist.deleteWishlistItem(item);
			entityManager.remove(emItem);
			repository.save(wishlist);
		}
		
	}

}
