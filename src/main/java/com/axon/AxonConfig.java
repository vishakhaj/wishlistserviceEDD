package com.axon;

import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.mongo.eventsourcing.eventstore.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;

/*
 * Specifies explicit configuration that Axon will include.
 * Mongo database connection and database names.
 * Event handling to handle events in the specified package.
 */
@Configuration
public class AxonConfig {
	
	@Bean
	public MongoEventStorageEngine eventStorageEngine() {
		return new MongoEventStorageEngine(mongoTemplate());
	}

	private org.axonframework.mongo.eventsourcing.eventstore.MongoTemplate mongoTemplate() {
		return new DefaultMongoTemplate(mongo(), "wishlist", "events", "snapshotEvents");
	}

	private MongoClient mongo() {
		return new MongoClient("localhost", 27017);
	}

	@Autowired
	public void configure(EventHandlingConfiguration configuration) {
		configuration.registerTrackingProcessor("com.axon.query.handler");
	}
	
}