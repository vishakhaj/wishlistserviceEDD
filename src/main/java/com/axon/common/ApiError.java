package com.axon.common;

import org.springframework.http.HttpStatus;

import com.axon.hateoas.resource.WishlistResource;


public class ApiError {

	private HttpStatus status;
	private String message;
	private WishlistResource resource;

	public ApiError(HttpStatus status, String message, WishlistResource resource) {
		this.status = status;
		this.message = message;
		this.resource = resource;
	}

	public ApiError(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public WishlistResource getResource() {
		return resource;
	}

}
