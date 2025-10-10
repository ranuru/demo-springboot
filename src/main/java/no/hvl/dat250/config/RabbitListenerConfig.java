package no.hvl.dat250.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.hvl.dat250.messaging.VoteEventListener;

@Configuration
public class RabbitListenerConfig {

    @Bean
    public SimpleMessageListenerContainer dynamicListenerContainer(
            ConnectionFactory connectionFactory,
            VoteEventListener voteEventListener,
            Jackson2JsonMessageConverter jsonMessageConverter) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        // Create a listener adapter with JSON conversion
        MessageListenerAdapter adapter = new MessageListenerAdapter(voteEventListener, "handleVoteEvent");
        adapter.setMessageConverter(jsonMessageConverter); // this fixes VoteEvent deserialization

        container.setMessageListener(adapter);

        // Start empty; queues will be added dynamically
        container.start();

        return container;
    }
}
