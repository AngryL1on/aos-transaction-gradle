package dev.angryl1on.gateway.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * Configuration class for setting up Redis as the caching layer in the application.
 *
 * <p>This configuration uses Lettuce as the Redis client and provides beans for
 * establishing a Redis connection factory and configuring a {@link RedisCacheManager}
 * with custom cache settings.</p>
 *
 * <p>The caching configuration supports specific cache regions like `transactions`
 * and `transactionsList`, with a default Time-to-Live (TTL) of 10 minutes.</p>
 *
 * <p>Values stored in the cache are serialized using {@link GenericJackson2JsonRedisSerializer}.</p>
 *
 * @author AngryL1on
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class RedisConfiguration {

    /**
     * Redis server hostname, injected from application properties.
     */
    @Value("${spring.data.redis.host}")
    private String redisHost;

    /**
     * Redis server port, injected from application properties.
     */
    @Value("${spring.data.redis.port}")
    private int redisPort;

    /**
     * Creates a {@link LettuceConnectionFactory} to establish a connection to the Redis server.
     *
     * @return A configured {@link LettuceConnectionFactory}.
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(configuration);
    }

    /**
     * Configures a {@link RedisCacheManager} for managing application caches.
     *
     * <p>The cache manager sets up default and named cache configurations:
     * <ul>
     *   <li>`transactions` - Cache configuration with a TTL of 10 minutes.</li>
     *   <li>`transactionsList` - Cache configuration with a TTL of 10 minutes.</li>
     * </ul>
     * The configuration disables caching of null values and applies
     * JSON serialization for cached values.</p>
     *
     * @return A configured {@link RedisCacheManager}.
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfig)
                .withCacheConfiguration("transactions", myDefaultCacheConfig(Duration.ofMinutes(10)))
                .withCacheConfiguration("transactionsList", myDefaultCacheConfig(Duration.ofMinutes(10)))
                .build();
    }

    /**
     * Creates a default cache configuration with the specified Time-to-Live (TTL).
     *
     * <p>The configuration includes:
     * <ul>
     *   <li>Setting the TTL for cached entries.</li>
     *   <li>Serialization of values using {@link GenericJackson2JsonRedisSerializer}.</li>
     * </ul>
     * </p>
     *
     * @param duration The TTL for cache entries.
     * @return A {@link RedisCacheConfiguration} instance with custom settings.
     */
    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
