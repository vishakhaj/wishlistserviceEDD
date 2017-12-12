package com.axon.query.repository;

import java.util.List;
import java.util.Optional;

import com.axon.query.entity.Wishlist;

public interface CustomWishlistRepository {

	public void cacheAllWishlists();

	public Optional<List<Wishlist>> findAllWishlistsByUserId(String userId);

	public Optional<Wishlist> findWishlistByUserIdAndWishlistId(String wishlistId);
	
	public Optional<List<Wishlist>> findAllPublicWishlists(String privacy);
	
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndSource(String userId, String source);
	
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndPrivacy(String userId, String privacy);
	
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndType(String userId, String type);
	
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndSourceAndPrivacyAndType(String userId, String source, String privacy, String type);

	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndSourceAndPrivacy(String userId, String source, String privacy);
	
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndSourceAndType(String userId, String source, String type);
	
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndPrivacyAndType(String userId, String privacy, String type);
	
	public Optional<List<Wishlist>> findAllWishlists();
	
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndSortOrder(String userId, String sortOrder);
	
	public int countWishlists(String privacy);
	
}
