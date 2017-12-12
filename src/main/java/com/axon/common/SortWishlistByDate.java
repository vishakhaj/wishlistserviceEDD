package com.axon.common;

import java.util.Comparator;

import com.axon.query.entity.Wishlist;


public class SortWishlistByDate implements Comparator<Wishlist> {

	@Override
	public int compare(Wishlist o1, Wishlist o2) {
		return o1.getCreatedAt().compareTo(o2.getCreatedAt());
	}

}
