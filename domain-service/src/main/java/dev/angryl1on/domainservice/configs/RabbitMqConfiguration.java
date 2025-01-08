package dev.angryl1on.domainservice.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up RabbitMQ in the application.
 *
 * <p>This configuration defines the queue, exchange, routing key, and
 * necessary beans for RabbitMQ integration, including a JSON message
 * converter and a pre-configured {@link RabbitTemplate}.</p>
 *
 * <p>Usage of this class assumes that RabbitMQ is properly configured
 * and running in the environment where the application is deployed.</p>
 *
 * @author AngryL1on
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class RabbitMqConfiguration {

    /**
     * The name of the RabbitMQ queue for transactions.
     */
    public static final String TRANSACTION_QUEUE = "transaction.queue";

    /**
     * The name of the RabbitMQ exchange for transactions.
     */
    public static final String TRANSACTION_EXCHANGE = "transaction.exchange";

    /**
     * The routing key for binding the queue to the exchange.
     */
    public static final String TRANSACTION_ROUTING_KEY = "transaction.key";

    /**
     * Defines a durable RabbitMQ queue.
     *
     * @return A new {@link Queue} object.
     */
    @Bean
    public Queue queue() {
        return new Queue(TRANSACTION_QUEUE, true);
    }

    /**
     * Defines a RabbitMQ direct exchange.
     *
     * @return A new {@link DirectExchange} object.
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(TRANSACTION_EXCHANGE);
    }

    /**
     * Binds the defined queue to the exchange using the specified routing key.
     *
     * @param queue The queue to bind.
     * @param exchange The exchange to bind the queue to.
     * @return A new {@link Binding} object.
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(TRANSACTION_ROUTING_KEY);
    }

    /**
     * Configures a message converter for RabbitMQ to use JSON serialization and deserialization.
     *
     * @return A {@link Jackson2JsonMessageConverter} for converting messages to and from JSON.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configures a {@link RabbitTemplate} with a JSON message converter.
     *
     * @param connectionFactory The connection factory for RabbitMQ.
     * @param messageConverter  The message converter for converting messages to and from JSON.
     * @return A configured {@link RabbitTemplate}.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
