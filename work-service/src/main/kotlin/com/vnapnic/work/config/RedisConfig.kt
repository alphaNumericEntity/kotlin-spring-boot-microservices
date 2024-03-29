package com.vnapnic.work.config

import com.vnapnic.common.config.BaseRedisConfig
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class RedisConfig : BaseRedisConfig() 