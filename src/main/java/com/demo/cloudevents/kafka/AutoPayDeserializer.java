package com.demo.cloudevents.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.demo.cloudevents.entity.AutoPayEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AutoPayDeserializer implements Deserializer{

	@Override
	public void configure(Map configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		AutoPayEvent ap = null;
		try {
			ap = mapper.readValue(data, AutoPayEvent.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ap;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
}
