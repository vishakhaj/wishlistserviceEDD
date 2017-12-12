package com.axon.query.repository;

import java.net.URISyntaxException;
import java.util.Optional;

import com.mongodb.WriteResult;

public interface AddItemToWishlistRepository {

	public Optional<WriteResult> addItemToWishlist(String wishlistId, String itemId) throws URISyntaxException;

	public void deleteItemFromWishlist(String wishlistId, String itemId) throws URISyntaxException;

}
