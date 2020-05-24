package epamers.surwave.configuration.cache;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class CacheConfiguration {

  private final CacheExpiryMap cacheExpiryMap;

  @Value("${spring.redis.host}")
  private String redisAddress;

  @Value("${spring.redis.port}")
  private Long redisPort;

  @Bean
  @Profile("redis-cache")
  public RedissonClient redisson() {
    Config config = new Config();
    config.useSingleServer().setAddress(String.format("redis://%s:%d", redisAddress, redisPort));

    return Redisson.create(config);
  }

  @Bean
  @Profile("redis-cache")
  public CacheManager cacheManager(RedissonClient redissonClient) {
    Map<String, CacheConfig> config = new HashMap<>();

    Map<String, Long> cacheTTLMap = cacheExpiryMap.build();
    for (Entry<String, Long> entry : cacheTTLMap.entrySet()) {

      String cacheName = entry.getKey();
      Long cacheTTL = entry.getValue();

      config.put(cacheName, new CacheConfig(cacheTTL, cacheTTL));
    }

    return new RedissonSpringCacheManager(redissonClient, config);
  }

  @Bean
  @Profile("no-cache")
  public CacheManager noCacheManager() {
    return new NoOpCacheManager();
  }


  @Bean
  public KeyGenerator commonKeyGenerator() {
    return (o, method, objects) -> generateKey(o, method, objects).toString();
  }

  private StringBuilder generateKey(Object o, Method method, Object... objects) {
    StringBuilder key = new StringBuilder(CacheConstants.NAMESPACE);
    key.append(CacheConstants.KEY_DELIMITER).append(o.getClass().getSimpleName());
    key.append(CacheConstants.KEY_DELIMITER).append(method.getName());
    for (Object obj : objects) {
      if (obj != null) {
        key.append(CacheConstants.KEY_DELIMITER).append(obj.toString());
      }
    }

    return key;
  }
}
