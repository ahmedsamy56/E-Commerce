package Core.Interfaces.Services;

public interface ICacheService {
    <T> T get(String key, Class<T> clazz);
    <T> void set(String key, T value, long expirationMinutes);
    void delete(String key);
    void deleteByPrefix(String prefix);
    boolean isAllowed(String key, int limit, int seconds);
}
