package com.axon.query.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.axon.common.ApiError;
import com.axon.common.Content;
import com.axon.common.Privacy;
import com.axon.common.WishlistViewBean;
import com.axon.hateoas.controller.assembler.WishlistResourceAssembler;
import com.axon.hateoas.resource.WishlistResource;
import com.axon.query.service.WishlistService;

@RestController
@RequestMapping(value = "/api")
public class WishlistController {

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private WishlistResourceAssembler wishlistResourceAssembler;

	/**
	 * @return All Wish-lists of a specific user ID.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "{client}/{locale}/users/user/{userId}/wishlists", method = RequestMethod.GET)
	// @ControllerHateoasInclude(method="toResources",
	// resource=WishlistResource.class)
	public ResponseEntity<?> getAllWishlistsByUserId(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId) throws IOException, URISyntaxException {

		wishlistService.getAllWishlistsFromCache();
		List<WishlistViewBean> wishlists = wishlistService.getAllWishlistsByUserId(userId);

		if (wishlists == null || wishlists.isEmpty()) {
			return new ResponseEntity<>("You have no wishlists", HttpStatus.NOT_FOUND);
		}

		//Content content = new Content(source, privacy, type);
		List<WishlistResource> wishlistResources = wishlistResourceAssembler.toResources(wishlists);
		return new ResponseEntity<List<WishlistResource>>(wishlistResources, HttpStatus.OK);
	}

	/**
	 * @param wishlistId
	 *            - Takes the unique wish-list ID
	 * @return Wish-list of a specific wish-list ID
	 * @throws IOException
	 */
	@RequestMapping(value = "{client}/{locale}/users/user/{userId}/wishlists/wishlist/{wishlistId}", method = RequestMethod.GET)
	public ResponseEntity<?> getWishlistByUserIdAndWishlistId(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId,
			@PathVariable("wishlistId") String wishlistId) throws IOException {

		wishlistService.getAllWishlistsFromCache();
		WishlistViewBean wishlist = wishlistService.getWishlistByUserIdAndWishlistId(userId, wishlistId);

		if (wishlist.getWishlistId() == null) {
			
			ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "You cannot access this wishlist.");
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}

		WishlistResource wishlistResource = wishlistResourceAssembler.toResource(wishlist);

		return new ResponseEntity<WishlistResource>(wishlistResource, HttpStatus.OK);
	}

	/**
	 * @return all wish-lists that are PUBLIC
	 * @throws IOException
	 */
	@RequestMapping(value = "/wishlists", method = RequestMethod.GET)
	public List<WishlistViewBean> getAllPublicWishlists(@RequestParam("privacy") String privacy,
			HttpServletResponse response) throws IOException {

		if (!privacy.equals(Privacy.PUBLIC.toString().toLowerCase())) {
			throw new IllegalArgumentException(
					"Required value for the parameter 'privacy' is not present or not correct.");
		}
		wishlistService.getAllWishlistsFromCache();
		List<WishlistViewBean> wishlists = wishlistService.getAllPublicWishlists(privacy);
		if (wishlists == null || wishlists.isEmpty()) {
			response.sendError(HttpStatus.NOT_FOUND.value(), "Public wishlists not found");
		}
		response.setStatus(HttpStatus.OK.value());
		return wishlists;
	}

	/**
	 * @param userId
	 * @param source
	 * @return all wish-lists by SOURCE of a specific user ID
	 * @throws IOException
	 */
	@RequestMapping(value = "/{client}/{locale}/users/user/{userId}/wishlists", params = "source", method = RequestMethod.GET)
	public List<WishlistViewBean> getAllWishlistsByUserIdAndSource(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId,
			@RequestParam(value = "source") String source) throws IOException {
		wishlistService.getAllWishlistsFromCache();
		List<WishlistViewBean> listOfWishlists = wishlistService.getAllWishlistsByUserIdAndSource(userId, source);
		if (listOfWishlists == null || listOfWishlists.isEmpty()) {
			// response.sendError(HttpStatus.BAD_REQUEST.value(), "Required
			// value for the parameter is empty or not correct.");
		}

		// response.setStatus(HttpStatus.OK.value());
		return listOfWishlists;
	}

	/**
	 * @param userId
	 * @param privacy
	 * @return all wish-lists by PRIVACY of a specific user ID.
	 * @throws IOException
	 */
	@RequestMapping(value = "/{client}/{locale}/users/user/{userId}/wishlists", params = "privacy", method = RequestMethod.GET)
	public List<WishlistViewBean> getAllWishlistsByUserIdAndPrivacy(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId,
			@RequestParam("privacy") String privacy) throws IOException {
		wishlistService.getAllWishlistsFromCache();
		List<WishlistViewBean> listOfWishlists = wishlistService.getAllWishlistsByUserIdAndPrivacy(userId, privacy);
		if (listOfWishlists == null || listOfWishlists.isEmpty()) {
			//response.sendError(HttpStatus.BAD_REQUEST.value(),
				//	"Required value for the parameter is empty or not correct.");
		}
		//response.setStatus(HttpStatus.OK.value());
		return listOfWishlists;
	}

	/**
	 * @param userId
	 * @param type
	 * @return all wish-lists by TYPE of a specific user ID.
	 * @throws IOException
	 */
	@RequestMapping(value = "/{client}/{locale}/users/user/{userId}/wishlists", params = "type", method = RequestMethod.GET)
	public List<WishlistViewBean> getAllWishlistsByUserIdAndType(@PathVariable("client") String client,
			@PathVariable("locale") String locale, @PathVariable("userId") String userId,
			@RequestParam("type") String type) throws IOException {
		wishlistService.getAllWishlistsFromCache();
		List<WishlistViewBean> listOfWishlists = wishlistService.getAllWishlistsByUserIdAndType(userId, type);
		if (listOfWishlists == null || listOfWishlists.isEmpty()) {
//			response.sendError(HttpStatus.BAD_REQUEST.value(),
//					"Required value for the parameter is empty or not correct.");
		}
		//response.setStatus(HttpStatus.OK.value());
		return listOfWishlists;
	}

	/**
	 * @param userId
	 * @param source
	 * @param privacy
	 * @param type
	 * @return all wish-lists by SOURCE, TYPE and PRIVACY of a specific user ID.
	 * @throws IOException
	 */
	@RequestMapping(value = "/users/user/{userId}/wishlists", params = { "source", "type",
			"privacy" }, method = RequestMethod.GET)
	public List<WishlistViewBean> getAllWishlistsByUserIdAndSourceAndPrivacyAndType(
			@PathVariable("userId") String userId, @RequestParam("source") String source,
			@RequestParam("privacy") String privacy, @RequestParam("type") String type, HttpServletResponse response)
			throws IOException {
		wishlistService.getAllWishlistsFromCache();
		List<WishlistViewBean> listOfWishlists = wishlistService
				.getAllWishlistsByUserIdAndSourceAndPrivacyAndType(userId, source, privacy, type);
		if (listOfWishlists == null || listOfWishlists.isEmpty()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),
					"Required value for the parameter is empty or not correct.");
		}
		response.setStatus(HttpStatus.OK.value());
		return listOfWishlists;
	}

	/**
	 * @param userId
	 * @param source
	 * @param privacy
	 * @return all wish-lists by SOURCE and PRIVACY of a specific user ID.
	 * @throws IOException
	 */
	@RequestMapping(value = "/users/user/{userId}/wishlists", params = { "source",
			"privacy" }, method = RequestMethod.GET)
	public List<WishlistViewBean> getAllWishlistsByUserIdAndSourceAndPrivacy(@PathVariable("userId") String userId,
			@RequestParam("source") String source, @RequestParam("privacy") String privacy,
			HttpServletResponse response) throws IOException {
		wishlistService.getAllWishlistsFromCache();
		List<WishlistViewBean> listOfWishlists = wishlistService.getAllWishlistsByUserIdAndSourceAndPrivacy(userId,
				source, privacy);
		if (listOfWishlists == null || listOfWishlists.isEmpty()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),
					"Required value for the parameter is empty or not correct.");
		}
		response.setStatus(HttpStatus.OK.value());
		return listOfWishlists;
	}

	/**
	 * @param userId
	 * @param source
	 * @param type
	 * @return all wish-lists by SOURCE AND TYPE of a specific user ID
	 * @throws IOException
	 */
	@RequestMapping(value = "/users/user/{userId}/wishlists", params = { "source", "type" }, method = RequestMethod.GET)
	public List<WishlistViewBean> getAllWishlistsByUserIdAndSourceAndType(@PathVariable("userId") String userId,
			@RequestParam("source") String source, @RequestParam("type") String type, HttpServletResponse response)
			throws IOException {
		wishlistService.getAllWishlistsFromCache();
		List<WishlistViewBean> listOfWishlists = wishlistService.getAllWishlistsByUserIdAndSourceAndType(userId, source,
				type);
		if (listOfWishlists == null || listOfWishlists.isEmpty()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),
					"Required value for the parameter is empty or not correct.");
		}
		response.setStatus(HttpStatus.OK.value());
		return listOfWishlists;
	}

	/**
	 * @param userId
	 * @param privacy
	 * @param type
	 * @return all wish-lists by PRIVACY and TYPE of a specific user ID.
	 * @throws IOException
	 */
	@RequestMapping(value = "/users/user/{userId}/wishlists", params = { "privacy",
			"type" }, method = RequestMethod.GET)
	public List<WishlistViewBean> getAllWishlistsByUserIdAndPrivacyAndType(@PathVariable("userId") String userId,
			@RequestParam("privacy") String privacy, @RequestParam("type") String type, HttpServletResponse response)
			throws IOException {
		wishlistService.getAllWishlistsFromCache();
		List<WishlistViewBean> listOfWishlists = wishlistService.getAllWishlistsByUserIdAndPrivacyAndType(userId,
				privacy, type);
		if (listOfWishlists == null || listOfWishlists.isEmpty()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),
					"Required value for the parameter is empty or not correct.");
		}
		response.setStatus(HttpStatus.OK.value());
		return listOfWishlists;
	}

	@RequestMapping(value = "/users/user/{userId}/wishlists", params = "sortOrder", method = RequestMethod.GET)
	public List<WishlistViewBean> getAllWishlistsbyUserIdAndSortOrder(@PathVariable("userId") String userId,
			@RequestParam("sortOrder") String sortOrder, HttpServletResponse response) throws IOException {

		List<WishlistViewBean> listOfWishlists = wishlistService.getAllWishlistsByUserIdAndSortOrder(userId, sortOrder);
		if (listOfWishlists == null || listOfWishlists.isEmpty()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(),
					"Required value for the parameter is empty or not correct.");
		}
		response.setStatus(HttpStatus.OK.value());
		return listOfWishlists;
	}

}
