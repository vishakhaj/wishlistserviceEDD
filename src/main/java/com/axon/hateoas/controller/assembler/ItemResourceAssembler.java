package com.axon.hateoas.controller.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.axon.WishlistApi;
import com.axon.hateoas.resource.ItemResource;
import com.axon.query.controller.ItemController;
import com.axon.query.entity.Item;

@Component
public class ItemResourceAssembler extends ResourceAssemblerSupport<Item, ItemResource>{

	@Autowired
	private HttpSession httpSession;
	
	public ItemResourceAssembler() {
		super(ItemController.class, ItemResource.class);
	}

	@Override
	public ItemResource toResource(Item item) {
		ItemResource itemResource = new ItemResource(item);
		
		try {
			//view item by ID
			itemResource.add(linkTo(methodOn(ItemController.class).getItemById(itemResource.getItem().getItemId())).
					withSelfRel());
			
			//add item to wishlist by ID
			itemResource.add(linkTo(methodOn(WishlistApi.class).addItemToWishlist(
					itemResource.getItem().getClient(), itemResource.getItem().getLocale(),
					httpSession.getAttribute("userIdSession").toString(), httpSession.getAttribute("wishlistIdSession").toString(),
					itemResource.getItem().getItemId())).
					withRel("add_item_to_this_wishlist"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return itemResource;
	}
	
	public List<ItemResource> toResources(List<Item> items) throws URISyntaxException{
		List<ItemResource> itemResources = items.stream().map(ItemResource::new)
				.collect(Collectors.toList());
		for(ItemResource itemResource : itemResources){
			//view item by ID
			itemResource.add(linkTo(methodOn(ItemController.class).getItemById(itemResource.getItem().getItemId())).
					withSelfRel());
		}
		return itemResources;
	}
	
}
