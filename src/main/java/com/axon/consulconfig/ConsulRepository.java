package com.axon.consulconfig;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import com.axon.resttemplateconfig.RestTemplateConfig;


@Component
public class ConsulRepository {

	private static final Logger logger = LoggerFactory.getLogger(ConsulRepository.class);

	private static final String CONSUL_ADDRESS = "http://" + System.getenv("CONSUL") + ":8500";
	//private static final String CONSUL_ADDRESS = "http://" + "192.168.1.2:8500";
	
	private static final String KEY_VALUE_PATH = "v1/kv";

	private static final String URL_SEPARATOR = "/";

	private static final String RECURSE_PARAMETER = "?recurse";

	private static final String LINE_SEPARATOR = "\\r?\\n";

	@Autowired
	private RestTemplateConfig restTemplateConfig;

	public Optional<ConsulConfigItem> requestConsulValue(String path) {
		String consulAddress = CONSUL_ADDRESS + URL_SEPARATOR + KEY_VALUE_PATH + URL_SEPARATOR + path;
		try {
			ConsulConfigItem consulItem = restTemplateConfig.restTemplate().getForObject(consulAddress,
					ConsulConfigItem.class);
			if (consulItem != null) {
				return Optional.of(consulItem);
			}
		} catch (ResourceAccessException ex) {
			logger.trace("Error occured when requesting Consul value: ", ex);
			logger.error("I/O error occured during accessing Consul with URL '" + consulAddress + "'.\n",
					ex.getMessage());
		} catch (RestClientException ex) {
			logger.trace("Error occured when requesting Consul value: ", ex);
			logger.error("An error occured during retreiving of key/values out of Consul.", ex.getMessage());
		}
		return Optional.empty();
	}

	public Optional<List<ConsulConfigItem>> requestConsulValuesRecursively(String path) {
		String consulAddress = CONSUL_ADDRESS + URL_SEPARATOR + KEY_VALUE_PATH + URL_SEPARATOR + path
				+ RECURSE_PARAMETER;
		try {
			ConsulConfigItem[] consulItemList = restTemplateConfig.restTemplate().getForObject(consulAddress,
					ConsulConfigItem[].class);
			if (consulItemList != null) {
				return Optional.of(Arrays.asList(consulItemList));
			}
		} catch (ResourceAccessException ex) {
			logger.trace("Error occured when requesting Consul values: ", ex);
			logger.error("I/O error occured during accessing Consul with URL '" + consulAddress + "'.\n",
					ex.getMessage());
		} catch (RestClientException ex) {
			logger.trace("Error occured when requesting Consul values: ", ex);
			logger.error("A RestClientException occured during retreiving of key/values out of Consul.",
					ex.getMessage());
		}
		return Optional.empty();
	}

	public String decodedConsulValue(String key, String encodedValue) {

		if (encodedValue == null) {
			return null;
		}

		try {
			byte[] decodedValue = Base64.getDecoder().decode(encodedValue);
			String decodedStringValue = new String(decodedValue, StandardCharsets.UTF_8);
			return decodedStringValue.replaceAll(LINE_SEPARATOR, "");
		} catch (IllegalArgumentException iae) {
			logger.trace("Error occured when decoding Consul value: ", iae);
			logger.error("Could not Base64-decode consul value for key: " + key, iae.getMessage());
		}
		return null;

	}
}
