package com.axon.query.repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axon.apiurlfactory.ApiUrlFactory;
import com.axon.query.entity.Item;
import com.axon.resttemplateconfig.RestTemplateConfig;
import com.axon.utils.Client;
import com.axon.utils.Clients;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Repository
@RestController
public class ItemRepositoryImpl implements ItemRepository{

	@Autowired
	private ApiUrlFactory apiUrlFactory;
	
	@Autowired
	private RestTemplateConfig restTemplateConfig;
	
	private Cache<Client, Map<Locale, List<Item>>> cache;
	
	public ItemRepositoryImpl(){
		cache = CacheBuilder.newBuilder().build();
	}
	
	@Override
	public void cacheAllItems() throws URISyntaxException {
		Map<Client, Map<Locale, List<Item>>> items = getAllItems();
		cache.invalidateAll();
		cache.putAll(items);		
	}

	@RequestMapping("rest/items")
	private Map<Client, Map<Locale, List<Item>>> getAllItems() throws URISyntaxException {
		Map<Client, Map<Locale, List<Item>>> mapByClient = new HashMap<>();
		for(Item item : items()){
			Client client = Clients.clientByShortName(item.getClient());
			Map<Locale, List<Item>> resultByLocale = mapByClient.get(client);
			if(resultByLocale==null){
				mapByClient.put(client, resultByLocale = new HashMap<>());
			}
			Locale locale = Locale.forLanguageTag(item.getLocale());
			List<Item> itemList = resultByLocale.get(locale);
			if(itemList==null){
				resultByLocale.put(locale, itemList = new ArrayList<>());
			}
			itemList.add(item);
			
		}
	return mapByClient;
	}

	private List<Item> items() throws URISyntaxException {
		URI items = apiUrlFactory.createItemApiUrl();
		List<Item> itemList = Arrays.asList(restTemplateConfig.restTemplate().getForObject(items, Item[].class));
		
		return itemList;
	}

	@Override
	public Item findItemByItemId(String itemId) {
		for(Entry<Client, Map<Locale, List<Item>>>  clientEntry : cache.asMap().entrySet()){
			Map<Locale, List<Item>> localeMap = clientEntry.getValue();
			if(localeMap!=null){
				for(Entry<Locale, List<Item>> localeEntry : localeMap.entrySet()){
					List<Item> items = localeEntry.getValue();
					for(Item item : items){
						if(Objects.equals(item.getItemId(), itemId)){
							return item;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public List<Item> findAllItems() {
		for(Entry<Client, Map<Locale, List<Item>>>  clientEntry : cache.asMap().entrySet()){
			Map<Locale, List<Item>> localeMap = clientEntry.getValue();
			if(localeMap!=null){
				for(Entry<Locale, List<Item>> localeEntry : localeMap.entrySet()){
					List<Item> items = localeEntry.getValue();
					return items;
				}
			}
		}
		return null;
	}

}
