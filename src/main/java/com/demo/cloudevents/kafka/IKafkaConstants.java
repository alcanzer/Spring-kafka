package com.demo.cloudevents.kafka;
/***
 * Constants for the Kafka Properties.
 * @author Joseph
 *
 */
public interface IKafkaConstants {
	
	public static String KAFKA_BROKER = "localhost:9092";
	public static String CLIENT_NAME = "client";
	public static String TOPIC_NAME = "demoo";
	public static String GROUP_ID = "group_1";
	public static String OFFSET_EARLIEST = "earliest";
	public static String OFFSET_LATEST = "latest";

}
