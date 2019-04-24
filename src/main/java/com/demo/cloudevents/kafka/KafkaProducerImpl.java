package com.demo.cloudevents.kafka;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.cloudevents.avro.Account;
import com.demo.cloudevents.avro.Autopay;
import com.demo.cloudevents.avro.Customer;
import com.demo.cloudevents.avro.Payment;
import com.demo.cloudevents.avro.PaymentSchedule;
import com.demo.cloudevents.avro.User;

@Service
public class KafkaProducerImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerImpl.class);
	@Autowired
	private KafkaEventProducer kafkaEventProducer;
	
	public void pushMessage(String message) throws IOException {
		try {
			Producer prod = kafkaEventProducer.getProducer();
			User user = User.newBuilder()
					.setCustomer(Customer.newBuilder()
											.setCcId("99983452345")
											.setCustomerId("AX883172")
											.build())
					.setAccount(Account.newBuilder()
										.setAccountNumber("23412431")
										.setPayment(Payment.newBuilder()
															.setPaymentAmount(1000.00)
															.setPaymentId("AX123664")
															.build())
										.build())
					.setPaymentSchedule(PaymentSchedule.newBuilder()
														.setPaymentFrequency("3 days")
														.setPaymentScheduleType("Rolling")
														.setAutoPay(Autopay.newBuilder()
																			.setAutoPayDate("4th Jan 2019")
																			.setAutoPayStartDate("1st Jan 2019")
																			.build())
														.build())
					.build();
			LOGGER.info(user.toString());
			ProducerRecord<String, User> record = new ProducerRecord("demoo", "3", user);
			prod.send(record);
			prod.close();
			prod.flush();
			DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
			DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
			dataFileWriter.create(user.getSchema(), new File("users.avro"));
			dataFileWriter.append(user);
			dataFileWriter.append(user);
			dataFileWriter.close();
		} finally {
			
		}
	}
}
