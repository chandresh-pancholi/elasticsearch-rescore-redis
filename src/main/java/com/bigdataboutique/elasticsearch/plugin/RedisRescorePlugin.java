package com.bigdataboutique.elasticsearch.plugin;

import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import static java.util.Collections.singletonList;
import java.nio.file.Path;
import java.util.Set;

public class RedisRescorePlugin extends Plugin implements SearchPlugin {

    private final Config config;

    public RedisRescorePlugin(final Settings settings, final Path configPath) {
        this.config = new Config(settings);

        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        String[] redisClusterNodes = config.getRedisUrl().split(",");
        for (int i = 0; i < redisClusterNodes.length; i++) {
            String[] hostPortList = redisClusterNodes[i].split(":");
            jedisClusterNodes.add(new HostAndPort(hostPortList[0], Integer.valueOf(hostPortList[1])));
        }

        JedisCluster jc = new JedisCluster(jedisClusterNodes);
        RedisRescoreBuilder.setJedis(jc);
    }

    /**
     * @return the plugin's custom settings
     */
    @Override
    public List<Setting<?>> getSettings() {
        return Arrays.asList(Config.REDIS_URL);
    }

    @Override
    public Settings additionalSettings() {
        final Settings.Builder builder = Settings.builder();

        // Exposes REDIS_URL as a node setting
        builder.put(Config.REDIS_URL.getKey(), config.getRedisUrl());

        return builder.build();
    }

    @Override
    public List<RescorerSpec<?>> getRescorers() {
        return singletonList(
                new RescorerSpec<>(RedisRescoreBuilder.NAME, RedisRescoreBuilder::new, RedisRescoreBuilder::fromXContent));
    }
}