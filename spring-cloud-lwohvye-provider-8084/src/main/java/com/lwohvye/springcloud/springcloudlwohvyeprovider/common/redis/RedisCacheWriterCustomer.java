/*
 * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.redis;

import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * {@link RedisCacheWriter} implementation capable of reading/writing binary data from/to Redis in {@literal standalone}
 * and {@literal cluster} environments. Works upon a given {@link RedisConnectionFactory} to obtain the actual
 * {@link RedisConnection}. <br />
 * {@link DefaultRedisCacheWriter} can be used in
 * {@link RedisCacheWriter#lockingRedisCacheWriter(RedisConnectionFactory) locking} or
 * {@link RedisCacheWriter#nonLockingRedisCacheWriter(RedisConnectionFactory) non-locking} mode. While
 * {@literal non-locking} aims for maximum performance it may result in overlapping, non atomic, command execution for
 * operations spanning multiple Redis interactions like {@code putIfAbsent}. The {@literal locking} counterpart prevents
 * command overlap by setting an explicit lock key and checking against presence of this key which leads to additional
 * requests and potential command wait times.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author Hongyan Wang
 * @description 在DefaultRedisCacheWriter的基础上，重写了put和get方法，实现来过期时间的设置和过期时间的访问刷新，
 * 其他功能宜通过重写相关方法来实现
 * 可以通过重写其他的方法，来实现对应的功能
 * @className RedisCacheWriterCustomer
 * @date 2019/10/15 12:21
 * @since 2.0
 */
// TODO 未考虑线程安全问题，后续需进行优化
public class RedisCacheWriterCustomer implements RedisCacheWriter {


    private final RedisConnectionFactory connectionFactory;
    private final Duration sleepTime;


    /**
     * @param connectionFactory must not be {@literal null}.
     */
    public RedisCacheWriterCustomer(RedisConnectionFactory connectionFactory) {
        this(connectionFactory, Duration.ZERO);
    }

    /**
     * @param connectionFactory must not be {@literal null}.
     * @param sleepTime         sleep time between lock request attempts. Must not be {@literal null}. Use {@link Duration#ZERO}
     *                          to disable locking.
     */
    public RedisCacheWriterCustomer(RedisConnectionFactory connectionFactory, Duration sleepTime) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        Assert.notNull(sleepTime, "SleepTime must not be null!");

        this.connectionFactory = connectionFactory;
        this.sleepTime = sleepTime;
    }

    /**
     * @return void
     * @description 重写了put方法，用于设置缓存的过期时间
     * @params [name, key, value, ttl]
     * @author Hongyan Wang
     * @date 2019/10/16 9:08
     */
    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#put(java.lang.String, byte[], byte[], java.time.Duration)
     */
    @Override
    public void put(@NotNull String name, @NotNull byte[] key, @NotNull byte[] value, @Nullable Duration ttl) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        execute(name, connection -> {
            //当设置了过期时间，则修改取出
            //@Cacheable(value="user-key#key_expire=1200",key = "#id",condition = "#id != 2")
            //name 对应 value
            //key 对应 value :: key

            //判断name里面是否设置了过期时间，如果设置了则对key进行缓存，并设置过期时间
            var index = name.lastIndexOf(RedisKeys.REDIS_EXPIRE_TIME_KEY);
            if (index > 0) {
                //取出对应的时间 1200 index + 1是还有一个=号
                var expireString = name.substring(index + 1 + RedisKeys.REDIS_EXPIRE_TIME_KEY.length());
                var expireTime = Long.parseLong(expireString);
                connection.set(key, value, Expiration.from(expireTime, TimeUnit.SECONDS), SetOption.upsert());
            } else if (shouldExpireWithin(ttl)) {
                connection.set(key, value, Expiration.from(ttl.toMillis(), TimeUnit.MILLISECONDS), SetOption.upsert());
            } else {
                connection.set(key, value);
            }

            return "OK";
        });
    }

    /**
     * @return byte[]
     * @description 重写了get方法，用于实现访问刷新过期时间的功能
     * @params [name, key]
     * @author Hongyan Wang
     * @date 2019/10/16 9:10
     */
    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#get(java.lang.String, byte[])
     */
    @Override
    public byte[] get(@NotNull String name, @NotNull byte[] key) {
        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        return execute(name, (connection) -> {
            /*
            在获取数据之前，先重新设置其过期时间，
            有两种方案，一种是重新设定其为固定值，另一种为在现有过期时间上加上固定值，还可以设置在指定的时间失效expireAt()
            这里采用首次十分钟，第二次加半小时，第三次加两小时，两小时以上不做更改
             */
            var index = name.lastIndexOf(RedisKeys.REDIS_EXPIRE_TIME_KEY);
            if (index > 0) {
                //取出对应的时间 1200 index + 1是还有一个=号
                var expireString = name.substring(index + 1 + RedisKeys.REDIS_EXPIRE_TIME_KEY.length());
                var expireTime = Long.parseLong(expireString);
                connection.expire(key, expireTime);
            } else {
                var needExpire = true;
//                获取缓存剩余过期时间
                var existTime = connection.pTtl(key, TimeUnit.SECONDS);
//                拿到过期时间，且key存在，方设置过期时间
                if (existTime != null && existTime > 0) {
//                  剩余时间在十分钟之内，将其加上半小时，作为过期时间
                    if (existTime < 10 * 60) {
                        existTime += 30 * 60;
//                  剩余时间一小时之内，将其加上两小时，作为过期时间
                    } else if (existTime < 60 * 60) {
                        existTime += 2 * 60 * 60;
                    } else {
//                  剩余时间两小时以上，不更新过期时间
                        needExpire = false;
                    }
//                  设置新的过期时间，单位秒
                    if (needExpire)
                        connection.expire(key, existTime);
                }
            }
            return connection.get(key);
        });
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#putIfAbsent(java.lang.String, byte[], byte[], java.time.Duration)
     */
    @Override
    public byte[] putIfAbsent(@NotNull String name, @NotNull byte[] key, @NotNull byte[] value, @Nullable Duration ttl) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        return execute(name, connection -> {

            if (isLockingCacheWriter()) {
                doLock(name, connection);
            }

            try {
                if (connection.setNX(key, value)) {

                    if (shouldExpireWithin(ttl)) {
                        connection.pExpire(key, ttl.toMillis());
                    }
                    return null;
                }

                return connection.get(key);
            } finally {

                if (isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#remove(java.lang.String, byte[])
     */
    @Override
    public void remove(@NotNull String name, @NotNull byte[] key) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(key, "Key must not be null!");

        execute(name, connection -> connection.del(key));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.cache.RedisCacheWriter#clean(java.lang.String, byte[])
     */
    @Override
    public void clean(@NotNull String name, @NotNull byte[] pattern) {

        Assert.notNull(name, "Name must not be null!");
        Assert.notNull(pattern, "Pattern must not be null!");

        execute(name, connection -> {

            var wasLocked = false;

            try {

                if (isLockingCacheWriter()) {
                    doLock(name, connection);
                    wasLocked = true;
                }

                var keys = Optional.ofNullable(connection.keys(pattern)).orElse(Collections.emptySet())
                        .toArray(new byte[0][]);

                if (keys.length > 0) {
                    connection.del(keys);
                }
            } finally {

                if (wasLocked && isLockingCacheWriter()) {
                    doUnlock(name, connection);
                }
            }

            return "OK";
        });
    }

    /**
     * Explicitly set a write lock on a cache.
     *
     * @param name the name of the cache to lock.
     */
    void lock(String name) {
        execute(name, connection -> doLock(name, connection));
    }

    /**
     * Explicitly remove a write lock from a cache.
     *
     * @param name the name of the cache to unlock.
     */
    void unlock(String name) {
        executeLockFree(connection -> doUnlock(name, connection));
    }

    private Boolean doLock(String name, @NotNull RedisConnection connection) {
        return connection.setNX(createCacheLockKey(name), new byte[0]);
    }

    private Long doUnlock(String name, @NotNull RedisConnection connection) {
        return connection.del(createCacheLockKey(name));
    }

    boolean doCheckLock(String name, @NotNull RedisConnection connection) {
        return connection.exists(createCacheLockKey(name));
    }

    /**
     * @return {@literal true} if {@link RedisCacheWriter} uses locks.
     */
    private boolean isLockingCacheWriter() {
        return !sleepTime.isZero() && !sleepTime.isNegative();
    }

    private <T> T execute(String name, @NotNull Function<RedisConnection, T> callback) {

        RedisConnection connection = connectionFactory.getConnection();
        try {

            checkAndPotentiallyWaitUntilUnlocked(name, connection);
            return callback.apply(connection);
        } finally {
            connection.close();
        }
    }

    private void executeLockFree(@NotNull Consumer<RedisConnection> callback) {

        RedisConnection connection = connectionFactory.getConnection();

        try {
            callback.accept(connection);
        } finally {
            connection.close();
        }
    }

    private void checkAndPotentiallyWaitUntilUnlocked(String name, RedisConnection connection) {

        if (!isLockingCacheWriter()) {
            return;
        }

        try {

            while (doCheckLock(name, connection)) {
                Thread.sleep(sleepTime.toMillis());
            }
        } catch (InterruptedException ex) {

            // Re-interrupt current thread, to allow other participants to react.
            Thread.currentThread().interrupt();

            throw new PessimisticLockingFailureException(String.format("Interrupted while waiting to unlock cache %s", name),
                    ex);
        }
    }

//    @Contract("null -> false")
    private static boolean shouldExpireWithin(@Nullable Duration ttl) {
        return ttl != null && !ttl.isZero() && !ttl.isNegative();
    }

    @NotNull
//    @Contract(pure = true)
    private static byte[] createCacheLockKey(String name) {
        return (name + "~lock").getBytes(StandardCharsets.UTF_8);
    }
}

