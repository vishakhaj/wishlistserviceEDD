package com.axon.apiurlfactory;

import java.net.URI;
import java.net.URISyntaxException;

public interface ApiUrlFactory {

	public URI createItemApiUrl() throws URISyntaxException;
}
