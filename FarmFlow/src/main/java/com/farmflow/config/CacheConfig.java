package com.farmflow.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {


    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "addressAllCache", "addressByIdCache", "addressByUserCache",

                 "farmerCache",
                "productCacheAll","productCacheAvailable", "productCacheById", "productCacheByCategory","productCacheByFarmer",

                "reviewCacheById","reviewCacheAll","reviewCacheByProduct","reviewCacheByUser",

                "imageCache",
                "userCache","userCacheById", "userCacheAll",

                "cartCache","categoryCache", "cartCache", "orderCache", "fileCache"
        ); // add all your cache names here
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(3, TimeUnit.HOURS));
        return cacheManager;
    }
}
