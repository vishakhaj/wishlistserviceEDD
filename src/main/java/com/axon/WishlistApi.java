package com.axon;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.axon.common.ApiError;
import com.axon.common.WishlistViewBean;
import com.axon.hateoas.controller.assembler.WishlistResourceAssembler;
import com.axon.hateoas.resource.WishlistResource;
import com.axon.query.entity.Item;
import com.axon.query.entity.Wishlist;
import com.axon.query.service.ItemService;
import com.axon.query.service.WishlistService;
import com.axon.write.aggregates.WishlistId;
import com.axon.write.commands.AddItemToWishlistCommand;
import com.axon.write.commands.CreateWishlistCommand;
import com.axon.write.commands.DeleteItemFromWishlistCommand;
import com.axon.write.commands.DeleteWishlistCommand;
import com.axon.write.commands.UpdateWishlistCommand;

@RestController
@RequestMapping(value = "/api")
public class WishlistApi {

	private CommandGateway commandGateway;

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private WishlistResourceAssembler wishlistResourceAssembler;

	public WishlistApi(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@RequestMapping(value = "{client}/{locale}/users/user/{userId}/wishlists", method = RequestMethod.POST)
	public ResponseEntity<WishlistResource> createWishlist(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId,
			@RequestBody Wishlist wishlist) throws IOException, URISyntaxException {
		httpSession.setAttribute("userIdSession", userId);
		httpSession.setAttribute("clientSession", client);
		httpSession.setAttribute("localeSession", locale);
		System.out.println("Sending CreateWishlistCommand via CommandGateway...");
		commandGateway.send(new CreateWishlistCommand(new WishlistId(), wishlist.getName(), wishlist.getDescription(),
				client, locale, new Date(), wishlist.getSource(), wishlist.getType(), wishlist.getPrivacy(), userId,
				new Date()));
		System.out.println("After sending CreateWishlistCommand...");

		WishlistResource resource = wishlistResourceAssembler.returnUriForAddWishlist();
		return new ResponseEntity<>(resource, HttpStatus.CREATED);
	}

	@RequestMapping(value = "{client}/{locale}/users/user/{userId}/wishlists/wishlist/{wishlistId}", method = RequestMethod.PUT)
	public ResponseEntity<WishlistResource> updateWishlist(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId,
			@PathVariable("wishlistId") String wishlistId, @RequestBody Wishlist wishlist)
			throws IOException, URISyntaxException {

		WishlistViewBean w = wishlistService.getWishlistByUserIdAndWishlistId(userId, wishlistId.toString());

		if (Objects.equals(w.getUserId(), userId) && Objects.equals(w.getWishlistId(), wishlistId.toString())) {

			commandGateway.send(new UpdateWishlistCommand(wishlistId, wishlist.getName(), wishlist.getDescription(),
					wishlist.getSource(), wishlist.getType(), wishlist.getPrivacy(), userId, new Date()));
			System.out.println("After sending UpdateWishlistCommand...");

			WishlistResource wishlistResource = new WishlistResource();
			wishlistResource.setItemAddedToWishlist(true);
			WishlistResource resource = wishlistResourceAssembler
					.returnUriForUpdateWishlist(wishlistResource.isItemAddedToWishlist(), w);

			return new ResponseEntity<WishlistResource>(resource, HttpStatus.OK);
		}

		return new ResponseEntity<WishlistResource>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "{client}/{locale}/users/user/{userId}/wishlists/wishlist/{wishlistId}", method = RequestMethod.DELETE)
	public ResponseEntity<WishlistResource> removeWishlist(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId,
			@PathVariable("wishlistId") String wishlistId) throws IOException, URISyntaxException {

		WishlistViewBean w = wishlistService.getWishlistByUserIdAndWishlistId(userId, wishlistId);

		if (Objects.equals(w.getUserId(), userId) && Objects.equals(w.getWishlistId(), wishlistId.toString())) {
			System.out.println("Sending DeleteWishlistCommand via CommandGateway...");
			commandGateway.send(new DeleteWishlistCommand(wishlistId.toString(), userId));
			System.out.println("DELETED...");

			WishlistResource resource = wishlistResourceAssembler.returnUriForDeleteWishlist();

			return new ResponseEntity<>(resource, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/{client}/{locale}/users/user/{userId}/wishlists/wishlist/{wishlistId}/items/item/{itemId}", method = RequestMethod.PUT)
	public ResponseEntity<?> addItemToWishlist(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId,
			@PathVariable("wishlistId") String wishlistId, @PathVariable("itemId") String itemId)
			throws URISyntaxException, IOException {

		itemService.getCachedItems();
		List<Item> items = itemService.getAllItems();
		Item item = itemService.getItemByItemId(itemId);
		wishlistService.getAllWishlistsFromCache();
		WishlistViewBean wishlist = wishlistService.getWishlistByUserIdAndWishlistId(userId, wishlistId);

		if (wishlist.getItems().contains(item)) {
			WishlistResource resource = wishlistResourceAssembler.returnUriForAddWishlist();
			ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Item already exists", resource);
			return new ResponseEntity<>(apiError, apiError.getStatus());

			// return new ResponseEntity<>(resource, HttpStatus.CONFLICT);
		}

		WishlistResource resource = wishlistResourceAssembler
				.returnUriForItemAddedOrDeletedFromWishlist(wishlistId, userId);

		
		for (Item i : items) {
			if (i.getItemId().equals(itemId)) {
				commandGateway.send(new AddItemToWishlistCommand(wishlistId, itemId, item.getItemName(),
						item.getBrandName(), item.getRating(), item.getClient(), item.getClient(), new Date()));

				
				return new ResponseEntity<WishlistResource>(resource, HttpStatus.CREATED);
			}
		}
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Item not found", resource);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@RequestMapping(value = "/{client}/{locale}/users/user/{userId}/wishlists/wishlist/{wishlistId}/items/item/{itemId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteItemFromWishlist(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId,
			@PathVariable("wishlistId") String wishlistId, @PathVariable("itemId") String itemId)
			throws URISyntaxException, IOException {

		itemService.getCachedItems();
		Item item = itemService.getItemByItemId(itemId);
		WishlistViewBean wishlist = wishlistService.getWishlistByUserIdAndWishlistId(userId, wishlistId);
		WishlistResource resource = wishlistResourceAssembler.returnUriForItemAddedOrDeletedFromWishlist(wishlistId,
				userId);

		if (wishlist.getItems().contains(item)) {
			commandGateway.send(new DeleteItemFromWishlistCommand(wishlistId, itemId, item.getItemName(),
					item.getBrandName(), item.getRating(), item.getClient(), item.getLocale(), new Date()));

			return new ResponseEntity<>(resource, HttpStatus.OK);

		}

		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Item does not exist", resource);
		return new ResponseEntity<>(apiError, apiError.getStatus());

	}

	@RequestMapping(value = "/{client}/{locale}/users/user/{userId}/wishlists/wishlist/{wishlistId}/items", method = RequestMethod.DELETE)
	public ResponseEntity<WishlistResource> deleteAllItemsFromWishlist(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId,
			@PathVariable("wishlistId") String wishlistId) throws URISyntaxException, IOException {

		itemService.getCachedItems();
		List<Item> items = itemService.getAllItems();

		for (Item item : items) {
			commandGateway.send(new DeleteItemFromWishlistCommand(wishlistId, item.getItemId(), item.getItemName(),
					item.getBrandName(), item.getRating(), item.getClient(), item.getLocale(), new Date()));
		}
		WishlistResource resource = wishlistResourceAssembler.returnUriForItemAddedOrDeletedFromWishlist(wishlistId,
				userId);
		return new ResponseEntity<WishlistResource>(resource, HttpStatus.OK);
	}

	@RequestMapping(value = "/{client}/{locale}/users/user/{userId}/wishlists", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAllWishlistsByUserId(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId) {

		Optional<List<Wishlist>> wishlists = wishlistService.getAllWishlists();

		for (Wishlist wishlist : wishlists.get()) {
			commandGateway.send(new DeleteWishlistCommand(wishlist.getWishlistId(), userId));
		}

		return new ResponseEntity<>("You have no wishlists", HttpStatus.OK);
	}

}
