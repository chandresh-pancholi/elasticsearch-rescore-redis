
package com.bigdataboutique.elasticsearch.plugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Setting.Property;
import org.elasticsearch.common.settings.Settings;

/**
 * {@link Config} contains the settings values and their static declarations.
 */
public class Config {
    protected static final Logger log = LogManager.getLogger(RedisRescoreBuilder.class);

    static final Setting<String> REDIS_URL = new Setting<String>("redisRescore.redisUrl", "", v -> {
        try {
            return v;
        } catch (Exception e) {
            throw new IllegalArgumentException("Setting is not a valid URI");
        }
    }, Property.NodeScope, Property.Dynamic);

    private final String redisUrl;

    public Config(final Settings settings) {
        this.redisUrl = REDIS_URL.get(settings);
    }

    public String getRedisUrl() {
        log.info("redisUrl ==> {} ", redisUrl);
        return redisUrl;
    }
}
