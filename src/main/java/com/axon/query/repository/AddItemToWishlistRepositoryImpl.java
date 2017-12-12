package com.axon.query.repository;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.axon.query.entity.Item;
import com.axon.query.entity.Wishlist;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

@Repository
public class AddItemToWishlistRepositoryImpl implements AddItemToWishlistRepository {

	@Autowired
	private CustomWishlistRepository wishlistRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Optional<WriteResult> addItemToWishlist(String wishlistId, String itemId) throws URISyntaxException {
		Optional<Wishlist> wishlist = wishlistRepository.findWishlistByUserIdAndWishlistId(wishlistId);
		itemRepository.cacheAllItems();
		Item item = itemRepository.findItemByItemId(itemId);
		if (wishlist.get().getWishlistId().equals(wishlistId)) {
			Query query = new Query(Criteria.where("_id").is(wishlist.get().getWishlistId()));
			Update update = new Update();
			update.addToSet("items", item);
			WriteResult updateFirst = mongoTemplate.updateFirst(query, update, Wishlist.class);
			return Optional.of(updateFirst);

		}
		return Optional.empty();
	}

	@Override
	public void deleteItemFromWishlist(String wishlistId, String itemId) throws URISyntaxException {
		Optional<Wishlist> wishlist = wishlistRepository.findWishlistByUserIdAndWishlistId(wishlistId);
		itemRepository.cacheAllItems();
		Item item = itemRepository.findItemByItemId(itemId);
		if (wishlist.get().getWishlistId().equals(wishlistId) && item.getItemId().equals(itemId)) {

			String[] itemArray = { itemId };
			Query findQuery = Query.query(Criteria.where("items.itemId").in(Arrays.asList(itemArray)));
			DBObject pullUpdate = BasicDBObjectBuilder.start()
					.add("itemId", BasicDBObjectBuilder.start().add("$in", itemArray).get()).get();

			Update update = new Update().pull("items", pullUpdate);
			mongoTemplate.updateMulti(findQuery, update, Wishlist.class);
		}
	}

}
