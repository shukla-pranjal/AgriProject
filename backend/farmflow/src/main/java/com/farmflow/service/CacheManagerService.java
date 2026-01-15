package com.farmflow.service;

public interface CacheManagerService {
    void clearCache(String cacheName);
    void clearAllCaches();
    boolean isCacheEmpty(String cacheName);
    Object getCache(String cacheName); // New method to get cache contents
    java.util.Set<String> getAllCacheNames(); // New method to get all cache names
}