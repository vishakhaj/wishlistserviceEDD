package com.axon.apiurlfactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.axon.consulconfig.ConsulConfigItem;
import com.axon.consulconfig.ConsulRepository;

@Component
public class ApiUrlFactoryImpl implements ApiUrlFactory {

	private static final String CONSUL_ITEM_PATH = "eventdriven/wishlistservice/urls/items";

	@Autowired
	private ConsulRepository consulRepository;

	@Override
	public URI createItemApiUrl() throws URISyntaxException {
		Optional<List<ConsulConfigItem>> consulItemList = consulRepository
				.requestConsulValuesRecursively(CONSUL_ITEM_PATH);

		for (ConsulConfigItem item : consulItemList.get()) {
			String decodedConsulValue = consulRepository.decodedConsulValue(item.getKey(), item.getValue());
			return new URI(decodedConsulValue);
		}
		return null;
	}

}
