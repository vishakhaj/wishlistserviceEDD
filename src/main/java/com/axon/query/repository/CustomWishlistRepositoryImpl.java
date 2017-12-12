package com.axon.query.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axon.common.Privacy;
import com.axon.common.SortOrder;
import com.axon.common.SortWishlistByDate;
import com.axon.query.entity.Wishlist;
import com.axon.utils.Client;
import com.axon.utils.Clients;
import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Repository
public class CustomWishlistRepositoryImpl implements CustomWishlistRepository {

	@Autowired
	private JpaRepository<Wishlist, String> repository;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomWishlistRepositoryImpl.class);

	private Cache<Client, Map<Locale, List<Wishlist>>> cacheByClientAndLocale;

	private Cache<String, List<Wishlist>> cacheByUserId;

	public CustomWishlistRepositoryImpl() {
		cacheByClientAndLocale = CacheBuilder.newBuilder().maximumSize(1000).build();
		cacheByUserId = CacheBuilder.newBuilder().maximumSize(1000).build();
	}

	/**
	 * Caches all wish-lists
	 */
	public void cacheAllWishlists() {
		try {
			List<Wishlist> wishlists = repository.findAll(sortByUserDate());
			System.out.println("All wishlists: " + wishlists);

			cacheWishlistsByClientAndLocale(wishlists);
			cacheWishlistsByUserId(wishlists);

		} catch (RuntimeException e) {
			logger.warn(e.toString(), e);
		}
	}

	private Map<Client, Map<Locale, List<Wishlist>>> cacheWishlistsByClientAndLocale(List<Wishlist> wishlists) {
		Map<Client, Map<Locale, List<Wishlist>>> mapByClient = new HashMap<>();

		for (Wishlist wishlist : wishlists) {
			if (wishlist != null) {
				Client client = Clients.clientByShortName(wishlist.getClient());

				Map<Locale, List<Wishlist>> mapByLocale = mapByClient.get(client);
				if (mapByLocale == null) {
					mapByClient.put(client, mapByLocale = new HashMap<>());
				}
				Locale locale = Locale.forLanguageTag(wishlist.getLocale());
				List<Wishlist> listOfWishlists = mapByLocale.get(locale);
				if (listOfWishlists == null) {
					mapByLocale.put(locale, listOfWishlists = new ArrayList<>());
				}
				listOfWishlists.add(wishlist);
				
			}
		}
		cacheByClientAndLocale.invalidateAll();
		cacheByClientAndLocale.putAll(mapByClient);
		return mapByClient;
	}

	private Map<String, List<Wishlist>> cacheWishlistsByUserId(List<Wishlist> wishlists) {
		Map<String, List<Wishlist>> mapByUserId = new HashMap<>();
		for (Wishlist wishlist : wishlists) {
			if (wishlist != null) {
				System.out.println("user id: " + wishlist.getUserId());
				List<Wishlist> listOfWishlists = mapByUserId.get(wishlist.getUserId());
				if (listOfWishlists == null) {
					mapByUserId.put(wishlist.getUserId(), listOfWishlists = new ArrayList<>());
				}
				listOfWishlists.add(wishlist);
				System.out.println("list of wishlists: " + listOfWishlists);

			}
		}
		cacheByUserId.invalidateAll();
		cacheByUserId.putAll(mapByUserId);
		return mapByUserId;
	}

	/**
	 * Returns all wish-lists of a specific user by USER ID.
	 */
	public Optional<List<Wishlist>> findAllWishlistsByUserId(String userId) {
		List<Wishlist> wishlists = cacheByUserId.getIfPresent(userId);
		if(wishlists==null ||wishlists.isEmpty()){
			return Optional.empty();
		}
			return Optional.of(wishlists);
	}

	@Override
	/**
	 * Returns a wish-list by a specified wish-list ID
	 */
	public Optional<Wishlist> findWishlistByUserIdAndWishlistId(String wishlistId) {
		for (Entry<String, List<Wishlist>> mapByUserId : cacheByUserId.asMap().entrySet()) {
			List<Wishlist> wishlists = mapByUserId.getValue();
			if (wishlists != null) {
				for (Wishlist wishlist : wishlists) {
					if (Objects.equal(wishlist.getWishlistId(), wishlistId)) {
						return Optional.of(wishlist);
					}
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns all wish-lists where privacy=PUBLIC
	 */
	public Optional<List<Wishlist>> findAllPublicWishlists(String privacy) {
		List<Wishlist> resultList = new ArrayList<>();
		
		for (Entry<String, List<Wishlist>> mapByUserId : cacheByUserId.asMap().entrySet()) {
			List<Wishlist> wishlists = mapByUserId.getValue();
			if (wishlists.isEmpty() || wishlists == null) {
				return Optional.empty();
			}
			if (wishlists != null) {
				for (Wishlist wishlist : wishlists) {
					if(Objects.equal(privacy, Privacy.PUBLIC.toString().toLowerCase()) && Objects.equal(wishlist.getPrivacy().toString().toLowerCase(), privacy)){
						resultList.add(wishlist);
					}
					
				}
			}
		}
		return Optional.of(resultList);
	}

	@Override
	/**
	 * Returns all wish-lists of a specific USER and a specific SOURCE.
	 */
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndSource(String userId, String source) {
		List<Wishlist> resultList = new ArrayList<>();
		List<Wishlist> wishlists = cacheByUserId.getIfPresent(userId);
		if (wishlists != null) {
			for (Wishlist wishlist : wishlists) {
				if (wishlist.getSource().toString().toLowerCase().equals(source)) {
					resultList.add(wishlist);
				}
			}
			return Optional.of(resultList);
		}
		return Optional.empty();
	}

	@Override
	/**
	 * Returns all wish-lists of a specific USER and specific PRIVACY.
	 */
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndPrivacy(String userId, String privacy) {
		List<Wishlist> resultList = new ArrayList<>();
		List<Wishlist> wishlists = cacheByUserId.getIfPresent(userId);
		if (wishlists != null) {
			for (Wishlist wishlist : wishlists) {
				if (wishlist.getPrivacy().toString().toLowerCase().equals(privacy)) {
					resultList.add(wishlist);
				}
			}
			return Optional.of(resultList);
		}
		return Optional.empty();
	}

	@Override
	/**
	 * Returns all wish-lists of a specific USER and specified TYPE
	 */
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndType(String userId, String type) {
		List<Wishlist> resultList = new ArrayList<>();

		List<Wishlist> wishlists = cacheByUserId.getIfPresent(userId);
		if (wishlists != null) {
			for (Wishlist wishlist : wishlists) {
				if (wishlist.getType().toString().toLowerCase().equals(type)) {
					resultList.add(wishlist);
				}
			}
			return Optional.of(resultList);
		}
		return Optional.empty();
	}

	@Override
	/**
	 * Returns all wish-list of a specific USER and SOURCE, PRIVACY, TYPE
	 */
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndSourceAndPrivacyAndType(String userId, String source,
			String privacy, String type) {
		List<Wishlist> resultList = new ArrayList<>();

		List<Wishlist> wishlists = cacheByUserId.getIfPresent(userId);
		if (wishlists != null) {
			for (Wishlist wishlist : wishlists) {
				if (wishlist.getSource().toString().toLowerCase().equals(source)) {
					if (wishlist.getPrivacy().toString().toLowerCase().equals(privacy)) {
						if (wishlist.getType().toString().toLowerCase().equals(type)) {
							resultList.add(wishlist);
						}
					}
				}
			}
			return Optional.of(resultList);
		}
		return Optional.empty();
	}

	@Override
	/**
	 * Returns all wish-list of a specific USER and SOURCE, PRIVACY.
	 */
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndSourceAndPrivacy(String userId, String source,
			String privacy) {
		List<Wishlist> resultList = new ArrayList<>();

		List<Wishlist> wishlists = cacheByUserId.getIfPresent(userId);
		if (wishlists != null) {
			for (Wishlist wishlist : wishlists) {
				if (wishlist.getSource().toString().toLowerCase().equals(source)) {
					if (wishlist.getPrivacy().toString().toLowerCase().equals(privacy)) {
						resultList.add(wishlist);
					}
				}
			}
			return Optional.of(resultList);
		}
		return Optional.empty();
	}

	@Override
	/**
	 * Returns all wish-list of a specific USER and SOURCE, TYPE
	 */
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndSourceAndType(String userId, String source,
			String type) {
		List<Wishlist> resultList = new ArrayList<>();

		List<Wishlist> wishlists = cacheByUserId.getIfPresent(userId);
		if (wishlists != null) {
			for (Wishlist wishlist : wishlists) {
				if (wishlist.getSource().toString().toLowerCase().equals(source)) {
					if (wishlist.getType().toString().toLowerCase().equals(type)) {
						resultList.add(wishlist);
					}
				}
			}
			return Optional.of(resultList);
		}
		return Optional.empty();
	}

	@Override
	/**
	 * Returns all wish-list of a specific USER and PRIVACY, TYPE
	 */
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndPrivacyAndType(String userId, String privacy,
			String type) {
		List<Wishlist> resultList = new ArrayList<>();
		List<Wishlist> wishlists = cacheByUserId.getIfPresent(userId);
		if (wishlists != null) {
			for (Wishlist wishlist : wishlists) {
				if (wishlist.getPrivacy().toString().toLowerCase().equals(privacy)) {
					if (wishlist.getType().toString().toLowerCase().equals(type)) {
						resultList.add(wishlist);
					}
				}
			}
			return Optional.of(resultList);
		}
		return Optional.empty();
	}

	private Sort sortByUserDate() {
		return new Sort(Sort.Direction.DESC, "createdAt");
	}

	@Override
	public Optional<List<Wishlist>> findAllWishlists() {
		Map<Locale, List<Wishlist>> client = cacheByClientAndLocale.getIfPresent(Clients.clientByShortName("MediaDE"));
		System.out.println(client);
		for(Entry<Client, Map<Locale, List<Wishlist>>>  clientEntry : cacheByClientAndLocale.asMap().entrySet()){
			System.out.println("Goes inside this..");
			Map<Locale, List<Wishlist>> localeMap = clientEntry.getValue();
			if(localeMap!=null){
				for(Entry<Locale, List<Wishlist>> localeEntry : localeMap.entrySet()){
					List<Wishlist> wishlists = localeEntry.getValue();
					return Optional.of(wishlists);
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<List<Wishlist>> findAllWishlistsByUserIdAndSortOrder(String userId, String sortOrder) {
		List<Wishlist> wishlists = cacheByUserId.getIfPresent(userId);

		if (wishlists != null) {
			System.out.println("sort order " + SortOrder.DESC.toString());
			System.out.println("parameter sort order: " + sortOrder);
			if (Objects.equal(SortOrder.DESC.toString().toLowerCase(), sortOrder)) {
				Collections.sort(wishlists, new SortWishlistByDate().reversed());
				System.out.println("wishlists" + wishlists);
				return Optional.of(wishlists);
			} else if (Objects.equal(SortOrder.ASC.toString().toLowerCase(), sortOrder)) {
				Collections.sort(wishlists, new SortWishlistByDate());
				return Optional.of(wishlists);
			}
		}

		return Optional.empty();
	}

	@Override
	public int countWishlists(String privacy) {
		cacheAllWishlists();
		Optional<List<Wishlist>> wishlists = findAllWishlists();
		int count =  0;
		System.out.println("wishlists: "  + wishlists);
		
		if(!wishlists.isPresent()){
			return 0;
		}
		
		for(Wishlist wishlist : wishlists.get()){
			if(wishlist.getPrivacy().toString().equals(privacy)){
				count += 1;
				
			}else if(wishlist.getPrivacy().toString().equals(privacy)){
				count += 1;
			}
		}
		return count;
	}

}
