package com.demo.cloudevents.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class AvroSerializer<T extends SpecificRecord> implements Serializer<T>{

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] serialize(String topic, T data) {
		try {
			byte[] result = null;
			if(data != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(stream, null);
				DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(data.getSchema());
				datumWriter.write((GenericRecord) data, encoder);
				encoder.flush();
				stream.close();
				result = stream.toByteArray();
			}
			return result;
		} catch(IOException e) {
			throw new SerializationException("Cannot serialize" + data);
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
