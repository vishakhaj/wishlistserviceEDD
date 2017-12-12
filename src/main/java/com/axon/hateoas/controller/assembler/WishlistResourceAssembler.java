package com.axon.hateoas.controller.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.axon.WishlistApi;
import com.axon.common.Content;
import com.axon.common.WishlistViewBean;
import com.axon.hateoas.resource.WishlistResource;
import com.axon.query.controller.ItemController;
import com.axon.query.controller.WishlistController;
import com.axon.query.entity.Item;
import com.axon.query.entity.Wishlist;

@Component
public class WishlistResourceAssembler extends ResourceAssemblerSupport<WishlistViewBean, WishlistResource> {

	@Autowired
	private HttpSession httpSession;

	public WishlistResourceAssembler() {
		super(WishlistController.class, WishlistResource.class);
	}

	@Override
	public WishlistResource toResource(WishlistViewBean wishlist) {

		WishlistResource wishlistResource = new WishlistResource(wishlist);

		httpSession.setAttribute("wishlistIdSession", wishlist.getWishlistId());
		try {
			// view wishlist by ID
			wishlistResource.add(linkTo(methodOn(WishlistController.class).getWishlistByUserIdAndWishlistId(
					wishlistResource.getWishlist().getClient(), wishlistResource.getWishlist().getLocale(),
					wishlistResource.getWishlist().getUserId(), wishlistResource.getWishlist().getWishlistId()))
							.withSelfRel());
			// update wishlist
			wishlistResource
					.add(linkTo(methodOn(WishlistApi.class).updateWishlist(wishlistResource.getWishlist().getClient(),
							wishlistResource.getWishlist().getLocale(), wishlistResource.getWishlist().getUserId(),
							wishlistResource.getWishlist().getWishlistId(), new Wishlist()))
									.withRel("update_this_wishlist"));
			// delete this wishlist
			wishlistResource
					.add(linkTo(methodOn(WishlistApi.class).removeWishlist(wishlistResource.getWishlist().getClient(),
							wishlistResource.getWishlist().getLocale(), wishlistResource.getWishlist().getUserId(),
							wishlistResource.getWishlist().getWishlistId())).withRel("delete_this_wishlist"));
			// view items
			wishlistResource.add(linkTo(methodOn(ItemController.class).getAllItems()).withRel("view_items"));

			// delete an item from wishlist
			for (Item item : wishlistResource.getWishlist().getItems()) {
				wishlistResource.add(linkTo(
						methodOn(WishlistApi.class).deleteItemFromWishlist(wishlistResource.getWishlist().getClient(),
								wishlistResource.getWishlist().getLocale(), wishlistResource.getWishlist().getUserId(),
								wishlistResource.getWishlist().getWishlistId(), item.getItemId()))
										.withRel("delete_this_item"));
			}

			// delete all items from wishlist
			wishlistResource.add(linkTo(methodOn(WishlistApi.class).deleteAllItemsFromWishlist(
					wishlistResource.getWishlist().getClient(), wishlistResource.getWishlist().getLocale(),
					wishlistResource.getWishlist().getUserId(), wishlistResource.getWishlist().getWishlistId()))
							.withRel("delete-all-items"));

			// view all wishlists
			wishlistResource.add(linkTo(methodOn(WishlistController.class).getAllWishlistsByUserId(
					wishlistResource.getWishlist().getClient(), wishlistResource.getWishlist().getLocale(),
					wishlistResource.getWishlist().getUserId())).withRel("view_all_wishlists"));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return wishlistResource;
	}

	public List<WishlistResource> toResources(List<WishlistViewBean> wishlists)
			throws IOException, URISyntaxException {

		List<WishlistResource> wishlistResources = wishlists.stream().map(WishlistResource::new)
				.collect(Collectors.toList());

		for (WishlistResource wishlistResource : wishlistResources) {
			httpSession.setAttribute("userIdSession", wishlistResource.getWishlist().getUserId());
			httpSession.setAttribute("clientSession", wishlistResource.getWishlist().getClient());
			httpSession.setAttribute("localeSession", wishlistResource.getWishlist().getLocale());

			// view a wishlist
			wishlistResource.add(linkTo(methodOn(WishlistController.class).getWishlistByUserIdAndWishlistId(
					wishlistResource.getWishlist().getClient(), wishlistResource.getWishlist().getLocale(),
					wishlistResource.getWishlist().getUserId(), wishlistResource.getWishlist().getWishlistId()))
							.withSelfRel());
			// create a wishlist
			wishlistResource
					.add(linkTo(methodOn(WishlistApi.class).createWishlist(wishlistResource.getWishlist().getClient(),
							wishlistResource.getWishlist().getLocale(), wishlistResource.getWishlist().getUserId(),
							new Wishlist())).withRel("add_a_new_wishlist"));
			// delete all wishlists
			wishlistResource.add(linkTo(
					methodOn(WishlistApi.class).deleteAllWishlistsByUserId(wishlistResource.getWishlist().getClient(),
							wishlistResource.getWishlist().getLocale(), wishlistResource.getWishlist().getUserId()))
									.withRel("delete_all_wishlists"));

//			// view all wishlists by source
//			wishlistResource.add(linkTo(methodOn(WishlistController.class).getAllWishlistsByUserIdAndSource(
//					wishlistResource.getWishlist().getClient(), wishlistResource.getWishlist().getLocale(),
//					wishlistResource.getWishlist().getUserId(), content.getSource()))
//							.withRel("view_all_wishlists_by_source"));

		}
		return wishlistResources;
	}

	// return url after creating and deleting wishlist
	public WishlistResource returnUriForAddWishlist() throws IOException, URISyntaxException {

		WishlistResource wishlistResource = new WishlistResource();

		// return get all wishlists of a user
		wishlistResource.add(linkTo(methodOn(WishlistController.class).getAllWishlistsByUserId(
				httpSession.getAttribute("clientSession").toString(),
				httpSession.getAttribute("localeSession").toString(),
				httpSession.getAttribute("userIdSession").toString())).withRel("view_all_wishlists"));
		return wishlistResource;
	}
	
	// return url after creating and deleting wishlist
		public WishlistResource returnUriForDeleteWishlist() throws IOException, URISyntaxException {

			WishlistResource wishlistResource = new WishlistResource();

			// return get all wishlists of a user
			wishlistResource.add(linkTo(methodOn(WishlistController.class).getAllWishlistsByUserId(
					httpSession.getAttribute("clientSession").toString(),
					httpSession.getAttribute("localeSession").toString(),
					httpSession.getAttribute("userIdSession").toString())).withRel("view_all_wishlists"));
			return wishlistResource;
		}

	// return url after updating an existing wishlist
	public WishlistResource returnUriForUpdateWishlist(boolean isUpdated, WishlistViewBean viewBean)
			throws IOException, URISyntaxException {

		WishlistResource wishlistResource = new WishlistResource();

		if (isUpdated) {
			// return get all wishlists of a user
			wishlistResource.add(linkTo(methodOn(WishlistController.class).getAllWishlistsByUserId(viewBean.getClient(),
					viewBean.getLocale(), viewBean.getUserId())).withRel("view_all_wishlists"));

			// return get a specific wishlist of a user
			wishlistResource.add(
					linkTo(methodOn(WishlistController.class).getWishlistByUserIdAndWishlistId(viewBean.getClient(),
							viewBean.getLocale(), viewBean.getUserId(), viewBean.getWishlistId()))
									.withRel("view_this_wishlist"));
			System.out.println("wishlist id session: " + httpSession.getAttribute("wishlistIdSession").toString());
			return wishlistResource;
		}

		return null;
	}

	// return url after items are added or deleted from a wishlist of a user
	public WishlistResource returnUriForItemAddedOrDeletedFromWishlist(String wishlistId, String userId)
			throws IOException, URISyntaxException {

		WishlistResource wishlistResource = new WishlistResource();

		// get all wishlists of a user
		wishlistResource.add(linkTo(methodOn(WishlistController.class).getAllWishlistsByUserId(
				httpSession.getAttribute("clientSession").toString(),
				httpSession.getAttribute("localeSession").toString(), userId))
						.withRel("view_all_wishlists"));

		// get a specific wishlist of a user
		wishlistResource.add(linkTo(methodOn(WishlistController.class).getWishlistByUserIdAndWishlistId(
				httpSession.getAttribute("clientSession").toString(),
				httpSession.getAttribute("localeSession").toString(), userId, wishlistId))
						.withRel("view_this_wishlist"));

		return wishlistResource;
	}

}
