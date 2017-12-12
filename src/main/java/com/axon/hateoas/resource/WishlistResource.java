package com.axon.hateoas.resource;

import org.springframework.hateoas.ResourceSupport;

import com.axon.common.Content;
import com.axon.common.WishlistViewBean;

public class WishlistResource extends ResourceSupport {

	private WishlistViewBean wishlist;

	private boolean itemAddedToWishlist;

	private Content content;

	public WishlistResource() {
	}

	public WishlistResource(WishlistViewBean wishlist) {
		this.wishlist = wishlist;
	}

	public WishlistViewBean getWishlist() {
		return wishlist;
	}

	public void setWishlist(WishlistViewBean wishlist) {
		this.wishlist = wishlist;
	}

	public boolean isItemAddedToWishlist() {
		return itemAddedToWishlist;
	}

	public void setItemAddedToWishlist(boolean itemAddedToWishlist) {
		this.itemAddedToWishlist = itemAddedToWishlist;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
	

}
