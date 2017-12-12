package com.axon.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.axon.query.entity.Wishlist;

//@Repository
@RepositoryRestResource(collectionResourceRel = "wishlist", path = "wishlist/api")
public interface WishlistQueryRepository extends JpaRepository<Wishlist,String> {
	
}