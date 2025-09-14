package com.farmflow.controller;

import com.farmflow.endpoint.CacheEndpoint;
import com.farmflow.service.CacheManagerService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class CacheController implements CacheEndpoint {

    private final CacheManagerService cacheService;

    public ResponseEntity<?> clearCache(String cacheName) {
        cacheService.clearCache(cacheName);
        return CommonUtil.createBuildResponseMessage("Cache " + cacheName + " cleared successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> clearAllCaches() {
        cacheService.clearAllCaches();
        return CommonUtil.createBuildResponseMessage("All caches cleared successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> isCacheEmpty(String cacheName) {
        boolean isEmpty = cacheService.isCacheEmpty(cacheName);
        return CommonUtil.createBuildResponse("Cache " + cacheName + " is " + (isEmpty ? "empty" : "not empty"), HttpStatus.OK);
    }

    public ResponseEntity<?> getCache(String cacheName) {
        Object cacheContent = cacheService.getCache(cacheName);
        if (cacheContent != null) {
            return CommonUtil.createBuildResponse(cacheContent, HttpStatus.OK);
        }
        return CommonUtil.createBuildResponseMessage("Cache " + cacheName + " not found or empty", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getAllCaches() {
        Set<String> cacheNames = cacheService.getAllCacheNames();
        return CommonUtil.createBuildResponse(cacheNames, HttpStatus.OK);
    }
}