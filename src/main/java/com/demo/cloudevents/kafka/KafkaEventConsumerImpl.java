package com.demo.cloudevents.kafka;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.cloudevents.entity.AutoPayEvent;
import com.demo.cloudevents.entity.DemoEvent;

import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventBuilder;
import io.cloudevents.Extension;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

@Service
public class KafkaEventConsumerImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventConsumerImpl.class);
	@Autowired
	private KafkaEventConsumer consumer;
	private static PrintWriter writer;
	private PublishSubject<AutoPayEvent> subject;
	private int messages = 0;
	/***
	 * Method to initiate subscription to the kafka topic.
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public void initConn() throws FileNotFoundException, UnsupportedEncodingException{
		LOGGER.info("Initializing connection");
		subject = PublishSubject.create();
		Consumer<String, AutoPayEvent> cons = consumer.getConsumer();
		/*writer = new PrintWriter("kafka_data.txt", "UTF-8");*/
		cons.subscribe(Collections.singletonList(IKafkaConstants.TOPIC_NAME));
		while(true) {
			//Poll the consumer every 0.3 seconds for the data.
			ConsumerRecords<String, AutoPayEvent> consumerRecords = cons.poll(Duration.ofMillis(2000));
			LOGGER.info("I polled for {}", consumerRecords.count());
			if(consumerRecords.count() == 0) {
				messages++;
				 //if messages couldn't be found more than
				 //300 times, then the loop breaks.
				if(messages > 100) {
					break;
				} else {
					continue;
				}
			}
			consumerRecords.forEach(item -> {
				//Writes the data to the file.
				subject.onNext(item.value());
				/*writer.println(item.value());
				writer.flush();
				LOGGER.info("Written in File. {}", item.value());*/
			});
			//Commit to acknowledge that the record is received.
			cons.commitSync();
		}
		//Unsubscribe to the topic
		cons.unsubscribe();
		subject.onComplete();
		//Closes the file once subscription is terminated.
		writer.close();
		//Close the connection.
		cons.close();
		LOGGER.info("Closing Connection");
	}
	
	/***
	 * 
	 * @return data from the kafka consumer converted to Cloud events.
	 */
	public List<CloudEvent<AutoPayEvent>> getData(){
		//pollData();
		//Get consumer from the properties.
		Consumer<String, AutoPayEvent> cons = consumer.getConsumer();
		//Subscribe to the topic.
		cons.subscribe(Collections.singletonList(IKafkaConstants.TOPIC_NAME));
		//Poll for records from the last commit point.
		ConsumerRecords<String, AutoPayEvent> consumerRecords = cons.poll(Duration.ofMillis(509));
		//Set up the cloudevent *headers.
		String eventId = UUID.randomUUID().toString();
		URI src = URI.create("/autopay");
		String eventType = "AutoPayEvent";
		URI schemaURL = URI.create("/schema");
		List<CloudEvent<AutoPayEvent>> events = new ArrayList<>();
		//Create a cloudevent for each data item.
		consumerRecords.forEach(item -> {
			//Building a complex object
			//Creating the cloudevent.
			CloudEvent<AutoPayEvent> event = new CloudEventBuilder<AutoPayEvent>()
					.type(eventType)
					.source(src)
					.id(eventId)
					.time(ZonedDateTime.now())
					.data(item.value())
					.contentType("application/json")
					.schemaURL(schemaURL)
					.build();
			events.add(event);
		});
		//Commit the to acknowledge the received records.
		cons.commitSync();
		//Close the consumer.
		cons.close();
		//cons.seekToEnd(Collections.singletonList(currPartition));
		LOGGER.info("Returning CloudEvents");
		return events;
	}
	
	public Observable<CloudEvent> getStream(){
		return subject.map(item -> {
			String eventId = UUID.randomUUID().toString();
			CloudEvent<AutoPayEvent> event = new CloudEventBuilder<AutoPayEvent>()
					.type("AutoPayEvent")
					.source(URI.create("/autopay"))
					.id(eventId)
					.time(ZonedDateTime.now())
					.data(item)
					.contentType("application/json")
					.schemaURL(URI.create("/schema"))
					.build();
			return event;
		});
	}
}