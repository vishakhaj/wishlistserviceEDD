package com.axon.query.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.axon.common.WishlistViewBean;
import com.axon.query.UImapper.WishlistUIMapper;
import com.axon.query.entity.Wishlist;
import com.axon.query.repository.CustomWishlistRepository;
import com.google.common.base.Strings;

@Service
@RestController
public class WishlistService {

	private static final WishlistViewBean NOT_FOUND_WISHLIST = new WishlistViewBean(true);

	private static final List<WishlistViewBean> NOT_FOUND_LISTOFWISHLISTS = new ArrayList<>();

	@Autowired
	private CustomWishlistRepository wishlistRepository;

	@Autowired
	private WishlistUIMapper wishlistUIMapper;
	
	public void getAllWishlistsFromCache() {
		wishlistRepository.cacheAllWishlists();
	}

	public List<WishlistViewBean> getAllWishlistsByUserId(String userId) {
		if (Strings.isNullOrEmpty(userId)) {
			return NOT_FOUND_LISTOFWISHLISTS;
		}
		Optional<List<Wishlist>> wishlistsByUserId = wishlistRepository.findAllWishlistsByUserId(userId);

		if (wishlistsByUserId.isPresent()) {
			return wishlistUIMapper.createUIViewBeanList(wishlistsByUserId.get());
		}
		return NOT_FOUND_LISTOFWISHLISTS;
	}

	public WishlistViewBean getWishlistByUserIdAndWishlistId(String userId, String wishlistId) {
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(wishlistId)) {
			return NOT_FOUND_WISHLIST;
		}
		Optional<Wishlist> wishlist = wishlistRepository.findWishlistByUserIdAndWishlistId(wishlistId);

		if (wishlist.isPresent()) {
			if (wishlist.get().getUserId().equals(userId)) {
				return wishlistUIMapper.createUIViewBean(wishlist.get());
			} else if (!wishlist.get().getUserId().equals(userId)
					&& wishlist.get().getPrivacy().toString().equals("PUBLIC")) {
				return wishlistUIMapper.createUIViewBean(wishlist.get());
			}
		}
		return NOT_FOUND_WISHLIST;
	}

	public List<WishlistViewBean> getAllWishlistsByUserIdAndSource(String userId, String source) {
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(source)) {
			return NOT_FOUND_LISTOFWISHLISTS;
		}

		Optional<List<Wishlist>> wishlists = wishlistRepository.findAllWishlistsByUserIdAndSource(userId, source);
		if (wishlists.isPresent()) {
			return wishlistUIMapper.createUIViewBeanList(wishlists.get());
		}
		return NOT_FOUND_LISTOFWISHLISTS;
	}

	public List<WishlistViewBean> getAllWishlistsByUserIdAndPrivacy(String userId, String privacy) {
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(privacy)) {
			return NOT_FOUND_LISTOFWISHLISTS;
		}
		Optional<List<Wishlist>> wishlists = wishlistRepository.findAllWishlistsByUserIdAndPrivacy(userId, privacy);
		if (wishlists.isPresent()) {
			return wishlistUIMapper.createUIViewBeanList(wishlists.get());
		}
		return NOT_FOUND_LISTOFWISHLISTS;
	}

	public List<WishlistViewBean> getAllWishlistsByUserIdAndType(String userId, String type) {
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(type)) {
			return NOT_FOUND_LISTOFWISHLISTS;
		}
		Optional<List<Wishlist>> wishlists = wishlistRepository.findAllWishlistsByUserIdAndType(userId, type);
		if (wishlists.isPresent()) {
			return wishlistUIMapper.createUIViewBeanList(wishlists.get());
		}
		return NOT_FOUND_LISTOFWISHLISTS;
	}

	public List<WishlistViewBean> getAllWishlistsByUserIdAndSourceAndPrivacyAndType(String userId, String source,
			String privacy, String type) {
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(privacy)
				|| Strings.isNullOrEmpty(type)) {
			return NOT_FOUND_LISTOFWISHLISTS;
		}
		Optional<List<Wishlist>> wishlists = wishlistRepository
				.findAllWishlistsByUserIdAndSourceAndPrivacyAndType(userId, source, privacy, type);
		if (wishlists.isPresent()) {
			return wishlistUIMapper.createUIViewBeanList(wishlists.get());
		}
		return NOT_FOUND_LISTOFWISHLISTS;
	}

	public List<WishlistViewBean> getAllWishlistsByUserIdAndSourceAndPrivacy(String userId, String source,
			String privacy) {
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(privacy)) {
			return NOT_FOUND_LISTOFWISHLISTS;
		}
		Optional<List<Wishlist>> wishlists = wishlistRepository.findAllWishlistsByUserIdAndSourceAndPrivacy(userId,
				source, privacy);
		if (wishlists.isPresent()) {
			return wishlistUIMapper.createUIViewBeanList(wishlists.get());
		}
		return NOT_FOUND_LISTOFWISHLISTS;
	}

	public List<WishlistViewBean> getAllWishlistsByUserIdAndSourceAndType(String userId, String source, String type) {
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(type)) {
			return NOT_FOUND_LISTOFWISHLISTS;
		}
		Optional<List<Wishlist>> wishlists = wishlistRepository.findAllWishlistsByUserIdAndSourceAndType(userId, source,
				type);
		if (wishlists.isPresent()) {
			return wishlistUIMapper.createUIViewBeanList(wishlists.get());
		}
		return NOT_FOUND_LISTOFWISHLISTS;
	}

	public List<WishlistViewBean> getAllWishlistsByUserIdAndPrivacyAndType(String userId, String privacy, String type) {
		if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(privacy) || Strings.isNullOrEmpty(type)) {
			return NOT_FOUND_LISTOFWISHLISTS;
		}
		Optional<List<Wishlist>> wishlists = wishlistRepository.findAllWishlistsByUserIdAndPrivacyAndType(userId,
				privacy, type);
		if (wishlists.isPresent()) {
			return wishlistUIMapper.createUIViewBeanList(wishlists.get());
		}
		return NOT_FOUND_LISTOFWISHLISTS;
	}

	public List<WishlistViewBean> getAllPublicWishlists(String privacy) {
		if(Strings.isNullOrEmpty(privacy)){
			return NOT_FOUND_LISTOFWISHLISTS;
		}
		Optional<List<Wishlist>> wishlists = wishlistRepository.findAllPublicWishlists(privacy);
		if (wishlists.isPresent()) {
			return wishlistUIMapper.createUIViewBeanList(wishlists.get());
		}
		return NOT_FOUND_LISTOFWISHLISTS;
	}
	
	public Optional<List<Wishlist>> getAllWishlists(){
		Optional<List<Wishlist>> wishlists = wishlistRepository.findAllWishlists();
		if(wishlists.isPresent()){
			return wishlists;
		}
		return Optional.empty();
	}
	
	public List<WishlistViewBean> getAllWishlistsByUserIdAndSortOrder(String userId, String sortOrder){
		if(Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(sortOrder)){
			return NOT_FOUND_LISTOFWISHLISTS;
		}
		Optional<List<Wishlist>> wishlists = wishlistRepository.findAllWishlistsByUserIdAndSortOrder(userId, sortOrder);
		if(wishlists.isPresent()){
			return wishlistUIMapper.createUIViewBeanList(wishlists.get());
		}
		return NOT_FOUND_LISTOFWISHLISTS;
	}
	
}
