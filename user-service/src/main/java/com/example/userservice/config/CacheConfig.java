package com.example.userservice.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Configuration class for setting up Redis caching in the user service.
 * This class configures the Redis connection factory and cache manager
 * with default cache settings including TTL and null value handling.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Redis host name, defaulting to localhost if not specified in application
     * properties.
     */
    @Value("${spring.data.redis.host:localhost}")
    private String REDIS_HOST;

    /**
     * Redis port number, defaulting to 6379 if not specified in application
     * properties.
     */
    @Value("${spring.data.redis.port:6379}")
    private int REDIS_PORT;

    /**
     * Creates a Redis connection factory bean for connecting to Redis.
     * Uses Lettuce as the connection client with standalone configuration.
     * 
     * @return RedisConnectionFactory instance
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(REDIS_HOST);
        configuration.setPort(REDIS_PORT);
        return new LettuceConnectionFactory(configuration);
    }

    /**
     * Creates a Redis cache manager bean with default cache configuration.
     * All caches will have a TTL of 10 minutes and null values will not be cached.
     * 
     * @param redisConnectionFactory the Redis connection factory to use
     * @return CacheManager instance configured for Redis
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Set a TTL of 10 minutes for all caches
                .disableCachingNullValues(); // avoid caching null values

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfig)
                .build();
    }
}
