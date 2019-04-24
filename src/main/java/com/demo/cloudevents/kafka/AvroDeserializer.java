package com.demo.cloudevents.kafka;

import java.io.IOException;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Deserializer;

import com.demo.cloudevents.avro.User;

public class AvroDeserializer<T extends SpecificRecord> implements Deserializer<T> {
	
	protected final Class<T> targetType = (Class<T>) User.class;
	
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T deserialize(String topic, byte[] data) {
		T result = null;
			try {
				if(data != null) {
				DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(targetType.newInstance().getSchema());
				Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
				result = (T) datumReader.read(null, decoder);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
