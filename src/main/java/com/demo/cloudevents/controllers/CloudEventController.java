package com.demo.cloudevents.controllers;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.cloudevents.entity.AutoPayEvent;
import com.demo.cloudevents.kafka.KafkaEventConsumerImpl;

import io.cloudevents.CloudEvent;
import io.reactivex.Observable;

@RestController
public class CloudEventController {
	
	@Autowired
	private KafkaEventConsumerImpl impl;
	
	/***
	 * Endpoint to retrieve Kafka topic values as CloudEvents
	 * @return List of Kafka topic values as CloudEvents
	 */
	@GetMapping(path="/events", produces="application/cloudevents+json")
	public List<CloudEvent<AutoPayEvent>> getEvents(){
		return impl.getData();
	}
	
	/***
	 * Endpoint to subscribe to Kafka Topic.
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	@GetMapping(path="/conn")
	public String connInit() throws FileNotFoundException, UnsupportedEncodingException {
		impl.initConn();
		return "Subs started";
	}
	
	@GetMapping(path="/stream", produces="text/event-stream")
	public Observable<CloudEvent> stream(){
		return impl.getStream();
	}
}