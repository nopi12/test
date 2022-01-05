package com.bridgingcode.ProductOrdersAnalytics;

import com.bridgingcode.ProductOrdersAnalytics.model.ProductOrdersEvent;
import com.bridgingcode.ProductOrdersAnalytics.processor.binding.ProductOrdersBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableBinding(ProductOrdersBindings.class)
public class ProductOrdersAnalyticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductOrdersAnalyticsApplication.class, args);
	}

	@Component
	public static class OrderSender implements ApplicationRunner {

		private static final Logger LOGGER = LoggerFactory.getLogger(OrderSender.class);

		private final MessageChannel productOrdersOut;

		public OrderSender(ProductOrdersBindings bindings) {
			this.productOrdersOut = bindings.productOrdersOut();
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
				sendMessage("##################");

		}
		
		public void sendMessage(String text)
		{
			ProductOrdersEvent event = new ProductOrdersEvent();
			event.setMessage(text);

			Message<ProductOrdersEvent> message = MessageBuilder
					.withPayload(event)
					.setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString().getBytes())
					.build();

			try {
				this.productOrdersOut.send(message);
			} catch (Exception e) {
				LOGGER.error("An error occurred", e);
			}
		}
	}

}
