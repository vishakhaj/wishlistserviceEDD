package com.axon.query.service;

import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.axon.query.repository.AddItemToWishlistRepository;
import com.google.common.base.Strings;
import com.mongodb.WriteResult;

@Service
public class AddItemToWishlistService {

	@Autowired
	private AddItemToWishlistRepository addItemRepository;
	
	public Optional<WriteResult> addItemToWishlist(String wishlistId, String itemId) throws URISyntaxException {
		if (Strings.isNullOrEmpty(wishlistId)) {
			return Optional.empty();
		}

		return addItemRepository.addItemToWishlist(wishlistId, itemId);
	}
	
	public void deleteItemByUserIdAndWishlistId(String wishlistId, String itemId) throws URISyntaxException {
		if (Strings.isNullOrEmpty(itemId) || Strings.isNullOrEmpty(wishlistId)) {
		}
		addItemRepository.deleteItemFromWishlist(wishlistId, itemId);
	}
}
