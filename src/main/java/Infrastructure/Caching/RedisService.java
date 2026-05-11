package Infrastructure.Caching;

import Core.Interfaces.Services.ICacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

public class RedisService implements ICacheService {
    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;
    private static RedisService instance;

    public static synchronized RedisService getInstance() {
        if (instance == null) {
            instance = new RedisService();
        }
        return instance;
    }

    private RedisService() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        String host = System.getenv("REDIS_HOST");
        if (host == null || host.isBlank()) {
            host = "localhost";
        }

        String portStr = System.getenv("REDIS_PORT");
        int port = 6379;
        if (portStr != null && !portStr.isBlank()) {
            port = Integer.parseInt(portStr);
        }

        System.out.println(" Connecting to Redis at " + host + ":" + port);
        this.jedisPool = new JedisPool(poolConfig, host, port);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(key);
            if (json != null) {
                System.out.println("Data retrieved from Cache for key: " + key);
                return objectMapper.readValue(json, clazz);
            }
        } catch (Exception e) {
            System.out.println("Error getting from cache: " + e.getMessage());
        }
        System.out.println("Data retrieved from Database for key: " + key);
        return null;
    }

    @Override
    public <T> void set(String key, T value, long expirationMinutes) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = objectMapper.writeValueAsString(value);
            jedis.setex(key, expirationMinutes * 60, json);
            System.out.println("Data stored in Cache for key: " + key);
        } catch (Exception e) {
            System.out.println("Error setting to cache: " + e.getMessage());
        }
    }

    @Override
    public void delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
            System.out.println("Cache invalidated for key: " + key);
        } catch (Exception e) {
            System.out.println("Error deleting from cache: " + e.getMessage());
        }
    }

    @Override
    public void deleteByPrefix(String prefix) {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys(prefix + "*");
            if (keys != null && !keys.isEmpty()) {
                jedis.del(keys.toArray(new String[0]));
                System.out.println("Cache invalidated for prefix: " + prefix);
            }
        } catch (Exception e) {
            System.out.println("Error deleting by prefix: " + e.getMessage());
        }
    }
    @Override
    public boolean isAllowed(String key, int limit, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            long count = jedis.incr(key);
            if (count == 1) {
                jedis.expire(key, seconds);
            }
            return count <= limit;
        } catch (Exception e) {
            System.out.println("Error in rate limiting: " + e.getMessage());
            return true; // Default to allow if Redis is down
        }
    }
}
