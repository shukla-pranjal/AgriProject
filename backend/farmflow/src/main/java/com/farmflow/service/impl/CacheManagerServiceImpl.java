package com.farmflow.service.impl;

import com.farmflow.service.CacheManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CacheManagerServiceImpl implements CacheManagerService{
    @Autowired
    private CacheManager cacheManager;

    @Override
    public void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    @Override
    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(this::clearCache);
    }

    @Override
    public boolean isCacheEmpty(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null && cache.getNativeCache() instanceof org.springframework.cache.concurrent.ConcurrentMapCache) {
            return ((org.springframework.cache.concurrent.ConcurrentMapCache) cache.getNativeCache()).getNativeCache().isEmpty();
        }
        return true; // Return true if cache is null or not a ConcurrentMapCache
    }

    @Override
    public Object getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null && cache.getNativeCache() instanceof org.springframework.cache.concurrent.ConcurrentMapCache) {
            return ((org.springframework.cache.concurrent.ConcurrentMapCache) cache.getNativeCache()).getNativeCache();
        }
        return null; // Return null if cache is null or not a ConcurrentMapCache
    }

    @Override
    public Set<String> getAllCacheNames() {
        return (Set<String>) cacheManager.getCacheNames();
    }
}
