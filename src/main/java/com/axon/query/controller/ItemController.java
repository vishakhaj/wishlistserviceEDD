package com.axon.query.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.axon.hateoas.controller.assembler.ItemResourceAssembler;
import com.axon.hateoas.resource.ItemResource;
import com.axon.query.entity.Item;
import com.axon.query.service.ItemService;


@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ItemResourceAssembler itemResourceAssembler;

	@RequestMapping(value = "/items", method = RequestMethod.GET)
	public ResponseEntity<List<ItemResource>> getAllItems() throws URISyntaxException {
		itemService.getCachedItems();
		List<Item> items = itemService.getAllItems();
		
		List<ItemResource> itemResources = itemResourceAssembler.toResources(items);
		return new ResponseEntity<List<ItemResource>>(itemResources, HttpStatus.OK);
	}

	@RequestMapping(value = "/items/item/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<ItemResource> getItemById(@PathVariable("itemId") String itemId)
			throws URISyntaxException {
		itemService.getCachedItems();
		Item item = itemService.getItemByItemId(itemId);
		
		ItemResource itemResource = itemResourceAssembler.toResource(item);
		return new ResponseEntity<ItemResource>(itemResource, HttpStatus.OK);
	}
}
