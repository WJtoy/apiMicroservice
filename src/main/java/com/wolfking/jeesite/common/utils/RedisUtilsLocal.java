package com.wolfking.jeesite.common.utils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.kkl.kklplus.starter.redis.config.GsonRedisSerializer;
import com.kkl.kklplus.starter.redis.utils.RedisUtils;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：RedisUtils
 * <p>
 * 描述：Redis工具类
 * <p>
 * 创建人：Ryan Lu
 * <p>
 * 创建时间：
 * <p>
 * Copyright @ 2017
 */
@Component
@Configurable
@Slf4j
public class RedisUtilsLocal {



    private static Gson gson = new Gson();
//    @Value("${spring.redis.database}")
//    private int database;

//    @SuppressWarnings("rawtypes")
//    @Autowired
//    public RedisTemplate redisTemplate;

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

//    @Resource(name = "gsonRedisSerializer")

//    @Autowired
    private RedisUtils redisUtils;

    public GsonRedisSerializer gsonRedisSerializer;

    private RedisTemplate redisTemplate;
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
        this.redisTemplate = redisUtils.redisTemplate;
        this.gsonRedisSerializer = redisUtils.gsonRedisSerializer;
        this.stringRedisTemplate = redisUtils.stringRedisTemplate;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean set(final String key, Object value, long expireSeconds) {
        return set(RedisConstant.RedisDBType.REDIS_CONSTANTS_DB, key, value, expireSeconds);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param dbType
     * @param expireSeconds 过期时间（单位:秒）
     * @return
     */
    public Boolean set(final RedisConstant.RedisDBType dbType, final String key, Object value, long expireSeconds) {
        if (value == null) {
            return true;
        }
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.select(dbType.ordinal());
                try {
                    connection.set(bkey, bvalue);
                    if (expireSeconds > 0) {
                        connection.expire(key.getBytes("utf-8"), expireSeconds);
                    }
                    return 1L;
                } catch (UnsupportedEncodingException e) {
                    log.error("[RedisUtils.set]", e);
                    return -1L;
                }
            }
        }).equals(1L);
    }

    /**
     * 写入缓存,并设置过期时间（原子性(atomic)操作）
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
     * 如果 key 已经存在， SETEX 命令将覆写旧值。
     *
     * @param key
     * @param value
     * @param dbType
     * @param expireSeconds 过期时间（单位:秒）<=0,默认设置为30天
     * @return
     */
    public Boolean setEX(final RedisConstant.RedisDBType dbType, final String key, Object value, long expireSeconds) {
        if (StringUtils.isBlank(key) || value == null) {
            return true;
        }
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        Long result = (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.select(dbType.ordinal());
                try {
                    connection.setEx(bkey, expireSeconds <= 0 ? 30 * 24 * 3600 : expireSeconds, bvalue);
                    return 1L;
                } catch (Exception e) {
                    log.error("[RedisUtils.setEX]", e);
                    return -1L;
                }
            }
        });
        return result.equals(1L);
    }

    /**
     * 写入缓存,是『SET if Not eXists』(如果不存在，则 SET)的简写。
     * key不存在，写入，返回true
     * key存在，不覆盖，返回false
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean setNX(final String key, Object value, long expireSeconds) {
        return setNX(RedisConstant.RedisDBType.REDIS_CONSTANTS_DB, key, value, expireSeconds);
    }

    /**
     * 写入缓存,是『SET if Not eXists』(如果不存在，则 SET)的简写。
     * key不存在，写入，返回true
     * key存在，不覆盖，返回false
     *
     * @param key
     * @param value
     * @param dbType
     * @param expireSeconds 过期时间（单位:秒）
     * @return
     */
    public Boolean setNX(final RedisConstant.RedisDBType dbType, final String key, Object value, long expireSeconds) {
        if (StringUtils.isBlank(key) || value == null) {
            return false;
        }
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.select(dbType.ordinal());
                Boolean lock = false;
                try {
                    lock = connection.setNX(bkey, bvalue);

                    if (lock && expireSeconds > 0) {
                        connection.expire(bkey, expireSeconds);
                    }
                    return lock;
                } catch (Exception e) {
                    log.error("[RedisUtils.setNX]", e);
                    //setnx成功，但设置过期时间异常
                    if (lock && bkey != null) {
                        connection.del(bkey);
                    }
                    return false;
                }
            }
        });
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     * @Sample: Dict dict = (Dict)get("key",Dict.class)
     */
    public Object get(final String key, Class clazz) {
        return get(RedisConstant.RedisDBType.REDIS_CONSTANTS_DB, key, clazz);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     * @Sample: Dict dict = (Dict)get("key",Dict.class)
     */
    public Object get(final RedisConstant.RedisDBType dbType, final String key, Class clazz) {
        if (StringUtils.isBlank(key)) return null;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                //System.out.println(connection.toString());
                try {
                    connection.select(dbType.ordinal());
                    return connection.get(bkey);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                return new byte[0];
            }
        });
        if (bytes == null || bytes.length == 0) return null;
        return gson.fromJson(new String(bytes), clazz);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     * @Sample: Dict dict = (Dict)get("key",Dict.class)
     */
    public Object getString(final RedisConstant.RedisDBType dbType, final String key, Class clazz) {
        if (StringUtils.isBlank(key)) return null;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.get(bkey);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                return new byte[0];
            }
        });
        if (bytes == null || bytes.length == 0) return null;
        return new String(bytes);
    }

    /**
     * 在Redis键中设置指定的字符串值，并返回其旧值
     *
     * @param key
     * @return
     * @Sample:
     */
    public Object getSet(final RedisConstant.RedisDBType dbType, final String key, Object newValue) {
        if (StringUtils.isBlank(key)) return null;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(newValue);
        byte[] bytes = (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.getSet(bkey, bvalue);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                return new byte[0];
            }
        });
        if (bytes == null || bytes.length == 0) return null;
        if (newValue instanceof String) {
            return new String(bytes);
        }
        return gson.fromJson(new String(bytes), newValue.getClass());
    }

    /**
     * 读取缓存,value存储对象为List<T>的json格式（使用google.gson序列化）
     *
     * @param key
     * @param type 反序列化后的数据类型
     * @param <T>
     * @return
     * @Sample List<Dict> rlist = redisUtils.getList(1,"dicts",Dict[].class);
     */
    public <T> List<T> getList(final String key, Class<T[]> type) {
        return getList(RedisConstant.RedisDBType.REDIS_CONSTANTS_DB, key, type);
    }

    /**
     * 读取缓存,value存储对象为List<T>的json格式（使用google.gson序列化）
     *
     * @param dbType
     * @param key
     * @param type   反序列化后的数据类型
     * @param <T>
     * @return
     * @Sample List<Dict> rlist = redisUtils.getList(1,"dicts",Dict[].class);
     */
    public <T> List<T> getList(final RedisConstant.RedisDBType dbType, final String key, Class<T[]> type) {
        if (StringUtils.isBlank(key)) return Lists.newArrayList();
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.get(bkey);
                } catch (Exception e) {
                    log.error("[RedisUtils.getList] key:{} ,type:", key, type.getName(), e);
                }
                return Lists.newArrayList();
            }
        });
        if (bytes == null || bytes.length == 0) return null;
        T[] arr = gson.fromJson(StringUtils.toString(bytes), type);
        return new ArrayList<>(Arrays.asList(arr));
    }


    /**
     * 删除对应的value
     *
     * @param key
     */
    public Boolean remove(final String key) {
        return remove(RedisConstant.RedisDBType.REDIS_CONSTANTS_DB, key);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public Boolean remove(final RedisConstant.RedisDBType dbType, final String key) {
        if (StringUtils.isBlank(key)) return false;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {

                long cnt = 0;
                try {
                    connection.select(dbType.ordinal());
                    cnt = connection.del(bkey);
                } catch (Exception e) {
                    log.error("[RedisUtils.remove]", e);
                }
                return cnt > 0;
            }
        });
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public Boolean remove(final String... keys) {
        return remove(RedisConstant.RedisDBType.REDIS_CONSTANTS_DB, keys);
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public Boolean remove(final RedisConstant.RedisDBType dbType, final String... keys) {
        if (keys == null || keys.length == 0) return false;
        int vsize = keys.length;
        final byte[][] bkeys = new byte[vsize][];
        for (int i = 0; i < vsize; i++) {
            bkeys[i] = keys[i].getBytes(StandardCharsets.UTF_8);
        }
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                long cnt = 0;
                try {
                    connection.select(dbType.ordinal());
                    cnt = connection.del(bkeys);
                } catch (Exception e) {
                    log.error("[RedisUtils.remove]", e);
                }
                return cnt > 0;
            }
        });
    }

    /**
     * 设置key过期时间
     *
     * @param key
     * @param seconds 单位:秒
     * @return
     */
    public Boolean expire(final RedisConstant.RedisDBType dbType, final String key, final long seconds) {
        if (StringUtils.isBlank(key) || seconds <= 0) return false;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.select(dbType.ordinal());
                return connection.expire(bkey, seconds);
            }
        });
    }

    /**
     * 设置key在某时间过期
     *
     * @param dbType    database
     * @param key
     * @param timeStamp 所在时间(TIME_IN_UNIX_TIMESTAMP)
     * @return
     */
    public Boolean expireAt(final RedisConstant.RedisDBType dbType, final String key, final long timeStamp) {
        if (StringUtils.isBlank(key) || timeStamp <= 0) return false;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.select(dbType.ordinal());
                return connection.expireAt(bkey, timeStamp);
            }
        });
    }

    /**
     * 判断缓存中是否有对应的key
     *
     * @param key
     * @return
     */
    public Boolean exists(final String key) {
        return exists(RedisConstant.RedisDBType.REDIS_CONSTANTS_DB, key);
    }

    /**
     * 判断缓存中是否有对应的key
     *
     * @param dbType
     * @param key
     * @return
     */
    public Boolean exists(final RedisConstant.RedisDBType dbType, final String key) {
        if (StringUtils.isBlank(key)) return false;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.select(dbType.ordinal());
                return connection.exists(bkey);
            }
        });
    }

    /**
     * 插入时取得某张表的自增id值
     *
     * @param key 示例:db.{tableName}.id
     * @return 大于0时表示正常 -1 表示key必须填写
     */
    public long incr(final String key) {
        return incr(RedisConstant.RedisDBType.REDIS_CONSTANTS_DB, key);
    }

    /**
     * 插入时取得某张表的自增id值
     *
     * @param dbType 数据库下标
     * @param key    示例:db.{tableName}.id
     * @return 大于0时表示正常 -1 表示key必须填写
     */
    @SuppressWarnings("unchecked")
    public long incr(final RedisConstant.RedisDBType dbType, final String key) {
        if (StringUtils.isBlank(key)) {
            return -1l;
        }
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.select(dbType.ordinal());
                long id = 0;
                try {
                    id = connection.incr(bkey);
                } catch (Exception e) {
                    log.error("[RedisUtils.incr]", e);
                }
                return id;
            }
        });
    }


    /**
     * 判断哈希中是否有指定的字段
     *
     * @param key
     * @param field
     * @return
     */
    @SuppressWarnings("unchecked")
    public Boolean hexist(final RedisConstant.RedisDBType dbType, final String key, final String field) {
        if (StringUtils.isBlank(key)) return false;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bfield = field.getBytes(StandardCharsets.UTF_8);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.hExists(bkey, bfield);
                } catch (Exception e) {
                    log.error("[RedisUtils.hexist]", e);
                    return false;
                }
            }
        });
    }

    /**
     * 哈希 添加
     *
     * @param dbType
     * @param key:哈希主Key
     * @param field:属性key
     * @param value:属性值
     * @param expireSeconds
     */
    public Boolean hmSet(final RedisConstant.RedisDBType dbType, final String key, String field, Object value, long expireSeconds) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field) || value == null) return false;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bfield = field.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    Boolean result = connection.hSet(bkey, bfield, bvalue);
                    if (expireSeconds > 0) {
                        connection.expire(bkey, expireSeconds);
                    }
                    return result;
                } catch (Exception e) {
                    log.error("[RedisUtils.hmSet]", e);
                }
                return false;
            }
        });
    }

    /**
     * 哈希 添加多个
     *
     * @param key
     * @param map hashMap
     */
    public Boolean hmSetAll(final RedisConstant.RedisDBType dbType, String key, Map<String, Object> map, long expireSeconds) {
        if (StringUtils.isBlank(key) || map == null || map.size() == 0) return false;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final Map<byte[], byte[]> bmap = new HashMap<>();
        map.forEach(
                (k, v) -> {
                    byte[] field = k.getBytes(StandardCharsets.UTF_8);
                    byte[] value = gsonRedisSerializer.serialize(v);
                    bmap.put(field, value);
                }
        );
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    connection.hMSet(bkey, bmap);
                    if (expireSeconds > 0) {
                        connection.expire(bkey, expireSeconds);
                    }
                    return true;
                } catch (Exception e) {
                    log.error("[RedisUtils.hmSetAll]", e);
                }
                return false;
            }
        });
    }

    /**
     * 获得哈希指定field的值
     *
     * @param key
     * @param field
     * @return
     */
    public <T> T hGet(RedisConstant.RedisDBType dbType, String key, String field, Class<T> clazz) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) return null;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bfield = field.getBytes(StandardCharsets.UTF_8);

        Object result = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.hGet(bkey, bfield);
                } catch (Exception e) {
                    log.error("[RedisUtils.hGet]", e);
                }
                return null;
            }
        });
        if (result == null) return null;
        byte[] bytes = (byte[]) result;
        if (bytes.length == 0) return null;
        return (T) gsonRedisSerializer.deserialize(bytes, clazz);
    }


    /**
     * 获得哈希指定一些的所有值
     *
     * @param key
     * @param fields
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<byte[]> hGet(final RedisConstant.RedisDBType dbType, final String key, final String[] fields) {
        if (StringUtils.isBlank(key) || fields == null || fields.length == 0) return Lists.newArrayList();
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        int vsize = fields.length;
        final byte[][] bfields = new byte[vsize][];
        for (int i = 0; i < vsize; i++) {
            bfields[i] = fields[i].getBytes(StandardCharsets.UTF_8);
        }
        return (List<byte[]>) redisTemplate
                .execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection)
                            throws DataAccessException {
                        try {
                            connection.select(dbType.ordinal());
                            return connection.hMGet(bkey, bfields);
                        } catch (Exception e) {
                            log.error("[RedisUtils.hGet]", e);
                        }
                        return Lists.newArrayList();
                    }
                });
    }


    @SuppressWarnings("unchecked")
    public Map<String, byte[]> hGetAll(final RedisConstant.RedisDBType dbType, final String key) {
        if (StringUtils.isBlank(key)) return null;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        Map<byte[], byte[]> maps = (Map<byte[], byte[]>) redisTemplate
                .execute(new RedisCallback<Object>() {
                    @Override
                    public Map<byte[], byte[]> doInRedis(
                            RedisConnection connection)
                            throws DataAccessException {
                        try {
                            connection.select(dbType.ordinal());
                            return connection.hGetAll(bkey);
                        } catch (Exception e) {
                            log.error("[RedisUtils.hGetAll]", e);
                        }
                        return null;
                    }
                });
        if (maps == null || maps.size() == 0) return new HashMap<>();
        //将key转成String
        return maps.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> StringUtils.toString(e.getKey()),
                        e -> e.getValue()
                ));
    }

    @SuppressWarnings("unchecked")
    public Map<byte[], byte[]> hGetAllByte(final RedisConstant.RedisDBType dbType, final String key) {
        if (StringUtils.isBlank(key)) return null;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        Map<byte[], byte[]> maps = (Map<byte[], byte[]>) redisTemplate
                .execute(new RedisCallback<Object>() {
                    @Override
                    public Map<byte[], byte[]> doInRedis(
                            RedisConnection connection)
                            throws DataAccessException {
                        try {
                            connection.select(dbType.ordinal());
                            return connection.hGetAll(bkey);
                        } catch (Exception e) {
                            log.error("[RedisUtils.hGetAll]", e);
                        }
                        return null;
                    }
                });
        if (maps == null || maps.size() == 0) return new HashMap<>();
        return maps;
    }


    public <T> Map<String, T> hGetAllObj(final RedisConstant.RedisDBType dbType, String key, Class<T> clazz) {
        Map<String, T> result = Maps.newHashMap();
        Map<byte[], byte[]> map = hGetAllByte(dbType, key);
        if (!map.isEmpty()) {
            result = (Map) map.entrySet().stream().collect(Collectors.toMap((i) -> {
                return StringUtils.toString((byte[]) i.getKey());
            }, (i) -> {
                return gson.fromJson(StringUtils.toString(i.getValue()), clazz);
            }));
        }

        return (Map) result;
    }

    /**
     * 哈希 字段自增
     * 如field不存在，自动添加,且值为0+delta
     *
     * @param dbType db索引
     * @param key    键值
     * @param field  字段
     * @param delta  步长
     * @return
     */
    @SuppressWarnings("unchecked")
    public long hIncrBy(final RedisConstant.RedisDBType dbType, final String key, final String field, final long delta) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) return 0;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bfield = field.getBytes(StandardCharsets.UTF_8);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                long id = 0;
                try {
                    connection.select(dbType.ordinal());
                    id = connection.hIncrBy(bkey, bfield, delta);
                } catch (Exception e) {
                    log.error("[RedisUtils.hIncrBy]", e);
                }
                return id;
            }
        });
    }


    /**
     * 哈希删除某个field
     *
     * @param key
     * @param field
     */
    public Long hdel(final RedisConstant.RedisDBType dbType, final String key, final String field) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bfield = field.getBytes(StandardCharsets.UTF_8);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.hDel(bkey, bfield);
                } catch (Exception e) {
                    log.error("[RedisUtils.hdel]", e);
                }
                return 0l;
            }
        });
    }


    /**
     * 哈希删除多个field
     *
     * @param key
     * @param fields
     */
    public Long hdel(final RedisConstant.RedisDBType dbType, final String key, final String[] fields) {
        if (StringUtils.isBlank(key) || fields == null || fields.length == 0) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        int vsize = fields.length;
        final byte[][] bfields = new byte[vsize][];
        for (int i = 0; i < vsize; i++) {
            bfields[i] = fields[i].getBytes(StandardCharsets.UTF_8);
        }
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.hDel(bkey, bfields);
                } catch (Exception e) {
                    log.error("[RedisUtils.hdel]", e);
                }
                return 0l;
            }
        });
    }


    /**
     * 将值value插入到列表key的表头
     * 返回值大于0成功
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Long lPush(final RedisConstant.RedisDBType dbType, final String key, final Object value) {
        if (StringUtils.isBlank(key) || value == null) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                long cnt = 0;
                try {
                    connection.select(dbType.ordinal());
                    return connection.lPush(bkey, bvalue);
                } catch (Exception e) {
                    log.error("[RedisUtils.lPush]", e);
                }
                return cnt;
            }
        });
    }

    /**
     * 列表添加多个对象
     *
     * @param dbType
     * @param key
     * @param values
     */
    public Long lPushAll(final RedisConstant.RedisDBType dbType, String key, List<?> values) {
        if (StringUtils.isBlank(key) || values == null || values.size() == 0) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[][] bvalues = Lists.transform(values, serialObjectToByte).stream().toArray(size -> new byte[size][]);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.lPush(bkey, bvalues);
                } catch (Exception e) {
                    log.error("[RedisUtils.lPushAll]", e);
                }
                return 0l;
            }
        });
    }

    /**
     * 将值value插入到列表key的表尾
     * 返回值大于0成功
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Long rPush(final RedisConstant.RedisDBType dbType, final String key, final Object value) {
        if (StringUtils.isBlank(key) || value == null) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                long cnt = 0;
                try {
                    connection.select(dbType.ordinal());
                    return connection.rPush(bkey, bvalue);
                } catch (Exception e) {
                    log.error("[RedisUtils.rPush]", e);
                }
                return cnt;
            }
        });
    }

    /**
     * 列表添加多个对象
     *
     * @param dbType
     * @param key
     * @param values
     */
    public Long rPushAll(final RedisConstant.RedisDBType dbType, String key, List<?> values, long expireSeconds) {
        if (StringUtils.isBlank(key) || values == null || values.size() == 0) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[][] bvalues = Lists.transform(values, serialObjectToByte).stream().toArray(size -> new byte[size][]);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                long count = 0;
                try {
                    connection.select(dbType.ordinal());
                    count = connection.rPush(bkey, bvalues);
                    if (expireSeconds > 0) {
                        connection.expire(bkey, expireSeconds);
                    }
                    return count;
                } catch (Exception e) {
                    log.error("[RedisUtils.rPushAll]", e);
                }
                return 0l;
            }
        });
    }

    /**
     * 向SET中添加一个成员，为一个Key添加一个值。如果这个值已经在这个Key中，则返回FALSE
     *
     * @param key
     * @param start
     * @param end
     * @param type  返回对象类
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> lRange(final RedisConstant.RedisDBType dbType, final String key, final int start, final int end, Class<T> type) {
        if (StringUtils.isBlank(key)) return Lists.newArrayList();
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        List<byte[]> list = (List<byte[]>) redisTemplate
                .execute(new RedisCallback<Object>() {
                    @Override
                    public List<byte[]> doInRedis(RedisConnection connection)
                            throws DataAccessException {
                        try {
                            connection.select(dbType.ordinal());
                            return connection.lRange(bkey, start, end);
                        } catch (Exception e) {
                            log.error("[RedisUtils.lRange]", e);
                        }
                        return null;
                    }
                });
        if (list != null && list.size() > 0) {
            return Lists.transform(list, new Function<byte[], T>() {
                @Override
                public T apply(byte[] bytes) {
                    return (T) gsonRedisSerializer.deserialize(bytes, type);
                }
            });
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 保留列表指定范围内的元素
     *
     * @param dbType
     * @param key
     * @param begin
     * @param end
     * @return
     */
    @SuppressWarnings("unchecked")
    public Boolean lTrim(final RedisConstant.RedisDBType dbType, final String key, final int begin, final int end) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    connection.lTrim(bkey, begin, end);
                    return true;
                } catch (Exception e) {
                    log.error("[RedisUtils.lTrim]", e);
                }
                return false;
            }
        });
    }


    /**
     * 列表左侧删除一个元素
     *
     * @param dbType
     * @param key
     * @return Dict d = (Dict)redisUtils.lPop(15,"test:list:theme",Dict.class);
     */
    @SuppressWarnings("unchecked")
    public Object lPop(final RedisConstant.RedisDBType dbType, final String key, Class type) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.lPop(bkey);
                } catch (Exception e) {
                    log.error("[RedisUtils.lPop]", e);
                }
                return null;
            }
        });
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return gsonRedisSerializer.deserialize(bytes, type);
        } catch (Exception e) {
            log.error("[RedisUtils.lPop]", e);
        }
        return null;
    }


    /**
     * 通过索引号获得list对应的值
     *
     * @param key
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object lIndex(final RedisConstant.RedisDBType dbType, final String key, final long index, Class clazz) {
        if (StringUtils.isBlank(key) || index < 0) return null;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] result = null;
                try {
                    connection.select(dbType.ordinal());
                    return connection.lIndex(bkey, index);
                } catch (Exception e) {
                    log.error("[RedisUtils.lIndex]", e);
                }
                return null;
            }
        });
        if (bytes == null || bytes.length == 0) return null;
        try {
            return gsonRedisSerializer.deserialize(bytes, clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 列表中值数量
     *
     * @param key
     * @return
     */
    public long lLen(final RedisConstant.RedisDBType dbType, final String key) {
        if (StringUtils.isBlank(key)) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.lLen(bkey);
                } catch (Exception e) {
                    log.error("[RedisUtils.lLen]", e);
                }
                return 0l;
            }
        });
    }


    /**
     * 向SET中添加一个成员，为一个Key添加一个值。如果这个值已经在这个Key中，则返回FALSE
     *
     * @param key
     * @param value
     */
    public long sAdd(final RedisConstant.RedisDBType dbType, String key, Object value, final long expireSeconds) {
        if (StringUtils.isBlank(key) || value == null) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                long cnt = 0;
                try {
                    connection.select(dbType.ordinal());
                    cnt = connection.sAdd(bkey, bvalue);
                    if (expireSeconds > 0) {
                        connection.expire(bkey, expireSeconds);
                    }
                } catch (Exception e) {
                    log.error("[RedisUtils.sAdd]", e);
                }
                return cnt;
            }
        });
    }


    /**
     * 集合添加多个元素
     *
     * @param key
     * @param values 传入参数必须为List<Object>,不能是List<String>,
     *               否则会当初Object，调用上面的sAdd方法
     */
    public long sAdd(final RedisConstant.RedisDBType dbType, String key, List<Object> values, final long expireSeconds) {
        if (StringUtils.isBlank(key) || values == null || values.size() == 0) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[][] bvalues = Lists.transform(values, serialObjectToByte).stream().toArray(size -> new byte[size][]);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                long cnt = 0;
                try {
                    connection.select(dbType.ordinal());
                    cnt = connection.sAdd(bkey, bvalues);
                    if (expireSeconds > 0) {
                        connection.expire(bkey, expireSeconds);
                    }
                } catch (Exception e) {
                    log.error("[RedisUtils.sAdd]", e);
                }
                return cnt;
            }
        });
    }

    /**
     * 移除并返回集合中的一个随机元素,当集合不存在或是空集时，返回 nil
     *
     * @param key
     * @param clazz 返回成员类型
     */
    public Object sPop(final RedisConstant.RedisDBType dbType, String key, Class clazz) {
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = key.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                long cnt = 0;
                try {
                    connection.select(dbType.ordinal());
                    return connection.sPop(bkey);
                } catch (Exception e) {
                    log.error("[RedisUtils.sPop]", e);
                }
                return null;
            }
        });
        if (bytes == null || bytes.length == 0) return null;
        return gson.fromJson(new String(bytes), clazz);
    }


    /**
     * 将成员从源集合移出放入目标集合
     * 如果源集合不存在或不包哈指定成员，不进行任何操作，返回0
     * 否则该成员从源集合上删除，并添加到目标集合，如果目标集合中成员已存在，则只在源集合进行删除
     *
     * @param srckey 源集合
     * @param dstkey 目标集合
     * @param member 源集合中的成员
     * @return 状态码，1成功，0失败
     */
    public Boolean sMove(final RedisConstant.RedisDBType dbType, final String srckey, final String dstkey, Object member) {
        if (StringUtils.isBlank(srckey) || StringUtils.isBlank(dstkey) || member == null) return false;
        final byte[] sbkey = srckey.getBytes(StandardCharsets.UTF_8);
        final byte[] dbkey = dstkey.getBytes(StandardCharsets.UTF_8);
        final byte[] bmember = gsonRedisSerializer.serialize(member);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.sMove(sbkey, dbkey, bmember);
                } catch (Exception e) {
                    log.error("[RedisUtils.sMove]", e);
                    return false;
                }
            }
        });
    }


    /**
     * 取得SET所有成员
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<byte[]> sMembers(final RedisConstant.RedisDBType dbType, final String key) {
        if (StringUtils.isBlank(key)) return Sets.newHashSet();
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Set<byte[]>) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.sMembers(bkey);
                } catch (Exception e) {
                    log.error("[RedisUtils.sMembers]", e);
                }
                return Sets.newHashSet();
            }
        });
    }


    /**
     * 取得SET所有成员
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set sMembers(final RedisConstant.RedisDBType dbType, final String key, Class clazz) {
        if (StringUtils.isBlank(key)) return Sets.newHashSet();
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        Set<byte[]> sets = (Set<byte[]>) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.sMembers(bkey);

                } catch (Exception e) {
                    log.error("[RedisUtils.sMembers]", e);
                }
                return Sets.newHashSet();
            }
        });
        if (sets == null || sets.size() == 0) {
            return Sets.newHashSet();
        }
        return sets.stream()
                .collect(
                        () -> new HashSet<Object>(),
                        (set, item) -> set.add(gsonRedisSerializer.deserialize(item, clazz)),
                        (set, subSet) -> set.addAll(subSet)
                );
    }


    /**
     * 交集，并返回结果
     *
     * @param dbType
     * @param keys
     * @return
     */
    public Set<byte[]> sInter(final RedisConstant.RedisDBType dbType, String... keys) {
        if (keys.length == 0) return Sets.newHashSet();
        int vsize = keys.length;
        final byte[][] bkeys = new byte[vsize][];
        for (int i = 0; i < vsize; i++) {
            bkeys[i] = keys[i].getBytes(StandardCharsets.UTF_8);
        }
        return (Set) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.sInter(bkeys);
                } catch (Exception e) {
                    log.error("[RedisUtils.sInter]", e);
                }
                return Sets.newHashSet();
            }
        });
    }


    /**
     * 交集，产生的结果保存在新的key中（newkey）
     *
     * @param dbType
     * @param newkey
     * @param keys
     * @return
     */
    public Long sInterStore(final RedisConstant.RedisDBType dbType, String newkey, String... keys) {
        if (StringUtils.isBlank(newkey)) return 0l;
        if (keys.length == 0) return 0l;
        final byte[] newbkey = newkey.getBytes(StandardCharsets.UTF_8);
        int vsize = keys.length;
        final byte[][] bkeys = new byte[vsize][];
        for (int i = 0; i < vsize; i++) {
            bkeys[i] = keys[i].getBytes(StandardCharsets.UTF_8);
        }

        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.sInterStore(newbkey, bkeys);
                } catch (Exception e) {
                    log.error("[RedisUtils.sInterStore]", e);
                }
                return 0l;
            }
        });
    }


    /**
     * 并集，产生的结果保存在新的key中（newkey）
     *
     * @param dbType
     * @param keys
     * @return
     */
    public Set<byte[]> sUnion(final RedisConstant.RedisDBType dbType, String... keys) {
        if (keys.length == 0) return Sets.newHashSet();
        int vsize = keys.length;
        final byte[][] bkeys = new byte[vsize][];
        for (int i = 0; i < vsize; i++) {
            bkeys[i] = keys[i].getBytes(StandardCharsets.UTF_8);
        }
        return (Set) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.sUnion(bkeys);
                } catch (Exception e) {
                    log.error("[RedisUtils.sUnion]", e);
                }
                return Sets.newHashSet();
            }
        });
    }


    /**
     * 差集，返回从第一组和所有的给定集合之间的差异的成员
     *
     * @param keys
     * @return 差异的成员集合
     */
    public Set<byte[]> sDiff(final RedisConstant.RedisDBType dbType, String... keys) {
        if (keys.length == 0) return Sets.newHashSet();
        int vsize = keys.length;
        final byte[][] bkeys = new byte[vsize][];
        for (int i = 0; i < vsize; i++) {
            bkeys[i] = keys[i].getBytes(StandardCharsets.UTF_8);
        }
        return (Set) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.sDiff(bkeys);
                } catch (Exception e) {
                    log.error("[RedisUtils.sDiff]", e);
                }
                return Sets.newHashSet();
            }
        });
    }


    /**
     * 差集，将从第一组和所有的给定集合之间的差异的成员保存到新的Key中（newkey）
     *
     * @param keys
     * @return 差异的成员集合
     */
    public Long sDiffStore(final RedisConstant.RedisDBType dbType, String newkey, String... keys) {
        if (StringUtils.isBlank(newkey)) return 0l;
        if (keys.length == 0) return 0l;
        final byte[] newbkey = newkey.getBytes(StandardCharsets.UTF_8);
        int vsize = keys.length;
        final byte[][] bkeys = new byte[vsize][];
        for (int i = 0; i < vsize; i++) {
            bkeys[i] = keys[i].getBytes(StandardCharsets.UTF_8);
        }

        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.sDiffStore(newbkey, bkeys);
                } catch (Exception e) {
                    log.error("[RedisUtils.sDiffStore]", e);
                }
                return 0l;
            }
        });
    }


    /**
     * 并集，产生的结果保存在新的key中（newkey）
     *
     * @param dbType
     * @param newkey
     * @param keys
     * @return
     */
    public Long sUnionStore(final RedisConstant.RedisDBType dbType, String newkey, String... keys) {
        if (StringUtils.isBlank(newkey)) return 0l;
        if (keys.length == 0) return 0l;
        final byte[] newbkey = newkey.getBytes(StandardCharsets.UTF_8);
        int vsize = keys.length;
        final byte[][] bkeys = new byte[vsize][];
        for (int i = 0; i < vsize; i++) {
            bkeys[i] = keys[i].getBytes(StandardCharsets.UTF_8);
        }

        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.sUnionStore(newbkey, bkeys);
                } catch (Exception e) {
                    log.error("[RedisUtils.sUnionStore]", e);
                }
                return 0l;
            }
        });
    }


    /**
     * 向有序集合中添加一个成员，为一个Key添加一个值。如果这个值已经在这个Key中，则返回FALSE
     *
     * @param key
     * @param value
     */
    public Boolean zAdd(final RedisConstant.RedisDBType dbType, String key, Object value, double scoure, final long expireSeconds) {
        if (StringUtils.isBlank(key) || value == null) return false;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                Boolean result = true;
                try {
                    connection.select(dbType.ordinal());
                    result = connection.zAdd(bkey, scoure, bvalue);
                    if (expireSeconds > 0) {
                        connection.expire(bkey, expireSeconds);
                    }
                    return result;
                } catch (Exception e) {
                    log.error("[RedisUtils.zAdd]", e);
                }
                return false;
            }
        });
    }


    /**
     * 向有序集合中添加一个成员，为一个Key添加一个值。
     * 如果这个值已经在这个Key中，则返回FALSE,并替换
     *
     * @param key
     * @param value
     */
    public Boolean zSetEX(final RedisConstant.RedisDBType dbType, String key, Object value, double score, final long expireSeconds) {
        if (StringUtils.isBlank(key) || value == null) return false;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    connection.zRemRangeByScore(bkey, score, score);
                    Boolean result = connection.zAdd(bkey, score, bvalue);
                    if (expireSeconds > 0) {
                        connection.expire(bkey, expireSeconds);
                    }
                    return result;
                } catch (Exception e) {
                    log.error("[RedisUtils.zSetEX]", e);
                }
                return false;
            }
        });
    }


    /**
     * 向有序集合中添加一个成员，为一个Key添加一个值。如果这个值已经在这个Key中，则返回FALSE
     */
    public Boolean zAdd(final RedisConstant.RedisDBType dbType, String key, Set<RedisZSetCommands.Tuple> values, final long expireSeconds) {
        if (StringUtils.isBlank(key) || values == null || values.size() == 0) return false;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    Long rst = connection.zAdd(bkey, values);
                    if (expireSeconds > 0) {
                        connection.expire(bkey, expireSeconds);
                    }
                    return rst > 0;
                } catch (Exception e) {
                    log.error("[RedisUtils.zAdd]", e);
                }
                return false;
            }
        });
    }


    /**
     * 获取给定值在集合中的权重
     * 异常或给定值不存在，返回-1
     *
     * @param key
     * @param value
     * @return double 权重
     */
    public double zScore(final RedisConstant.RedisDBType dbType, String key, Object value) {
        if (StringUtils.isBlank(key) || value == null) return 0;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return (double) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Double doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zScore(bkey, bvalue);
                } catch (Exception e) {
                    log.error("[RedisUtils.zScore]", e);
                    return -1d;
                }
            }
        });
    }

    /**
     * 从集合中删除成员
     *
     * @param key
     * @param value
     * @return 0：失败
     */
    public Long zRem(final RedisConstant.RedisDBType dbType, String key, Object value) {
        if (StringUtils.isBlank(key) || value == null) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return (long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zRem(bkey, bvalue);
                } catch (Exception e) {
                    log.error("[RedisUtils.zRem]", e);
                    return 0L;
                }
            }
        });
    }


    /**
     * 删除给定位置区间的元素
     *
     * @param key
     * @param start 开始区间，从0开始(包含)
     * @param end   结束区间,-1为最后一个元素(包含)
     * @return 删除的数量
     */
    public Long zRemRange(final RedisConstant.RedisDBType dbType, String key, long start, long end) {
        if (StringUtils.isBlank(key)) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zRemRange(bkey, start, end);
                } catch (Exception e) {
                    log.error("[RedisUtils.zRemRange]", e);
                    return 0L;
                }
            }
        });
    }


    /**
     * 删除给定权重区间的元素
     *
     * @param key
     * @param min 下限权重(包含)
     * @param max 上限权重(包含)
     * @return 删除的数量
     */
    public Long zRemRangeByScore(final RedisConstant.RedisDBType dbType, String key, double min, double max) {
        if (StringUtils.isBlank(key)) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zRemRangeByScore(bkey, min, max);
                } catch (Exception e) {
                    log.error("[RedisUtils.zRemRangeByScore]", e);
                    return 0L;
                }
            }
        });
    }


    /**
     * 权重增加给定值，如果给定的member已存在
     *
     * @param key
     * @param scoure 要增的权重
     * @param value  要插入的值
     * @return 增后的权重
     */
    public double zIncrBy(final RedisConstant.RedisDBType dbType, String key, double scoure, Object value) {
        if (StringUtils.isBlank(key) || value == null) return 0d;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        final byte[] bvalue = gsonRedisSerializer.serialize(value);
        return (double) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Double doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zIncrBy(bkey, scoure, bvalue);
                } catch (Exception e) {
                    log.error("[RedisUtils.zIncrBy]", e);
                    return 0d;
                }
            }
        });
    }


    /**
     * 获取集合中元素的数量
     *
     * @param key
     * @return 如果返回0则集合不存在
     */
    public Long zCard(final RedisConstant.RedisDBType dbType, String key) {
        if (StringUtils.isBlank(key)) return 0l;
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zCard(bkey);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return 0L;
                }
            }
        });
    }


    /**
     * 获取指定权重区间内集合的数量
     *
     * @param key
     * @param min 最小排序位置
     * @param max 最大排序位置
     */
    public Long zCount(final RedisConstant.RedisDBType dbType, String key, double min, double max) {
        if (StringUtils.isBlank(key)) return 0L;
        byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zCount(bkey, min, max);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return 0L;
                }
            }
        });
    }


    /**
     * 获得set的长度
     *
     * @param key
     * @return
     */
    public Integer zLength(final RedisConstant.RedisDBType dbType, String key) {
        if (StringUtils.isBlank(key)) return 0;
        byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Integer) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Integer doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zRange(bkey, 0, -1).size();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return 0;
                }
            }
        });
    }


    /**
     * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
     * (按score从大到小排序)
     *
     * @param key
     * @param start 开始位置(包含)
     * @param end   结束位置(包含)
     * @return Set<byte [ ]>
     */
    public Set<byte[]> zRange(final RedisConstant.RedisDBType dbType, String key, long start, long end) {
        if (StringUtils.isBlank(key)) return Sets.newHashSet();
        byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        return (Set<byte[]>) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zRange(bkey, start, end);
                } catch (Exception e) {
                    log.error("[RedisUtils.zRange]", e);
                    return Sets.newHashSet();
                }
            }
        });
    }


    /**
     * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
     * (按score从小到大排序)
     *
     * @param key
     * @param start 开始位置(包含)
     * @param end   结束位置(包含)
     * @return List
     * List<Order> list = zRange(0,"wip:orders",0,-1,Order.class);
     */
    public List zRange(final RedisConstant.RedisDBType dbType, String key, long start, long end, Class clazz) {
        if (StringUtils.isBlank(key)) return Lists.newArrayList();
        byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        Set<byte[]> sets = (Set) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zRange(bkey, start, end);
                } catch (Exception e) {
                    log.error("[RedisUtils.zRange]", e);
                    return Sets.newHashSet();
                }
            }
        });
        if (sets == null || sets.size() == 0) {
            return Lists.newArrayList();
        }

        return sets.stream().map(t -> gsonRedisSerializer.deserialize(t, clazz))
                .collect(Collectors.toList());
    }


    /**
     * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
     * (按score从大到小排序)
     *
     * @param key
     * @param start 开始位置(包含)
     * @param end   结束位置(包含)
     * @return List
     * Set<Order> set = zRevRange(0,"wip:orders",0,-1,Order.class);
     */
    public List zRevRange(final RedisConstant.RedisDBType dbType, String key, long start, long end, Class clazz) {
        if (StringUtils.isBlank(key)) return Lists.newArrayList();
        byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        Set<byte[]> sets = (Set) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zRevRange(bkey, start, end);
                } catch (Exception e) {
                    log.error("[RedisUtils.zRevRange]", e);
                    return Sets.newHashSet();
                }
            }
        });
        if (sets == null || sets.size() == 0) {
            return Lists.newArrayList();
        }
        return sets.stream().map(t -> gsonRedisSerializer.deserialize(t, clazz))
                .collect(Collectors.toList());
    }


    /**
     * 返回指定权重区间的元素集合
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return User user  = redisUtils.rangeByScore("wip:orders",1,10,User.class);
     */
    public Object zRangeOneByScore(final RedisConstant.RedisDBType dbType, String key, double scoure, double scoure1, Class clazz) {
        if (StringUtils.isBlank(key)) Lists.newArrayList();
        final byte[] bkey = key.getBytes(StandardCharsets.UTF_8);
        Set<RedisZSetCommands.Tuple> sets = (Set<RedisZSetCommands.Tuple>) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zRangeByScoreWithScores(bkey, scoure, scoure1);
                } catch (Exception e) {
                    log.error("[RedisUtils.zRangeByScore]", e);
                    return Sets.newHashSet();
                }
            }
        });

        if (sets == null || sets.size() == 0) {
            return null;
        }
        return sets.stream().filter(t -> {
            return Objects.equals(t.getScore(), scoure);
        })
                .map(t -> gsonRedisSerializer.deserialize(t.getValue(), clazz))
                .findFirst()
                .orElse(null);
    }

    /**
     * 并集，产生的结果保存在新的key中（newkey,覆盖原集合）
     *
     * @param dbType
     * @param newkey
     * @param keys
     * @return
     */
    public Long zUnionStore(final RedisConstant.RedisDBType dbType, String newkey, String... keys) {
        if (StringUtils.isBlank(newkey)) return 0l;
        if (keys.length == 0) return 0l;
        final byte[] bnewkey = newkey.getBytes(StandardCharsets.UTF_8);
        int vsize = keys.length;
        final byte[][] bkeys = new byte[vsize][];
        for (int i = 0; i < vsize; i++) {
            bkeys[i] = keys[i].getBytes(StandardCharsets.UTF_8);
        }

        return (Long) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.select(dbType.ordinal());
                    return connection.zUnionStore(bnewkey, bkeys);
                } catch (Exception e) {
                    log.error("[RedisUtils.zUnionStore]", e);
                    return 0L;
                }
            }
        });
    }

    public Function<Object, byte[]> serialObjectToByte = new Function<Object, byte[]>() {
        public byte[] apply(Object input) {
            return gsonRedisSerializer.serialize(input);
        }
    };


    //region lua脚本调用

    /**
     * 执行Lua脚本
     *
     * @param fileClasspath
     * @param resultType
     * @param keys
     * @param values
     * @param <T>
     * @return
     */
    public <T> T runLuaScript(String fileClasspath, Class<T> resultType, List<String> keys, Object... values) {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(fileClasspath)));
        redisScript.setResultType(resultType);
        return (T) stringRedisTemplate.execute(redisScript, keys, values);
    }

    /**
     * 执行Lua脚本
     *
     * @param scriptText
     * @param resultType
     * @param keys
     * @param values
     * @param <T>
     * @return
     */
    public <T> T runLuaScriptText(String scriptText, Class<T> resultType, List<String> keys, Object... values) {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(scriptText);
        redisScript.setResultType(resultType);
        return (T) stringRedisTemplate.execute(redisScript, keys, values);
    }

    //endregion

    //region 锁相关的方法

    /**
     * 获取锁
     *
     * @param lockKey
     * @param requestId
     * @param expireSeconds
     * @return
     */
    public Boolean getLock(final String lockKey, String requestId, long expireSeconds) {
        return getLock(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, requestId, expireSeconds);
    }

    /**
     * 获取锁
     *
     * @param dbType
     * @param lockKey
     * @param requestId
     * @param expireSeconds
     * @return
     */
    public Boolean getLock(final RedisConstant.RedisDBType dbType, final String lockKey, String requestId, long expireSeconds) {
        if (StringUtils.isEmpty(lockKey) || StringUtils.isEmpty(requestId)) {
            return false;
        }
        String scriptText = "redis.call(\"select\", ARGV[1]); " +
                "if (redis.call(\"setnx\", KEYS[1], ARGV[2]) == 1) then " +
                "redis.call('expire', KEYS[1], ARGV[3]); " +
                "return 1; " +
                "else " +
                "return 0; " +
                "end ";
        Long result = runLuaScriptText(scriptText, Long.class, Lists.newArrayList(lockKey), dbType.ordinal() + "", requestId, expireSeconds + "");
        return result != null && result == 1;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     * @param requestId
     * @return
     */
    public Boolean releaseLock(final String lockKey, final String requestId) {
        return releaseLock(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, requestId);
    }

    /**
     * 释放锁
     *
     * @param dbType
     * @param lockKey
     * @param requestId
     * @return
     */
    public Boolean releaseLock(final RedisConstant.RedisDBType dbType, final String lockKey, final String requestId) {
        if (lockKey == null || lockKey.isEmpty() || requestId == null || requestId.isEmpty()) {
            return false;
        }

        String scriptText = "redis.call(\"select\", ARGV[1]); " +
                "if (redis.call(\"get\", KEYS[1]) == ARGV[2]) then " +
                "return redis.call(\"del\", KEYS[1]); " +
                "else " +
                "return 0 " +
                "end ";

        Long result = runLuaScriptText(scriptText, Long.class, Lists.newArrayList(lockKey), dbType.ordinal() + "", requestId);
        return result == 1;
    }

    /**
     * 释放锁 延时释放锁
     *
     * @param lockKey
     * @param requestId
     * @param delaySeconds
     * @return
     */
    public Boolean releaseLockDelay(final String lockKey, final String requestId, long delaySeconds) {
        return releaseLockDelay(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, requestId, delaySeconds);
    }

    /**
     * 释放锁 延时释放锁
     *
     * @param dbType
     * @param lockKey
     * @param requestId
     * @param delaySeconds
     * @return
     */
    public Boolean releaseLockDelay(final RedisConstant.RedisDBType dbType, final String lockKey, final String requestId, long delaySeconds) {
        if (lockKey == null || lockKey.isEmpty() || requestId == null || requestId.isEmpty()) {
            return false;
        }

        String scriptText = "redis.call(\"select\", ARGV[1]); " +
                "if (redis.call(\"get\", KEYS[1]) == ARGV[2]) then " +
                "return redis.call('expire', KEYS[1], ARGV[3]); " +
                "else " +
                "return 0; " +
                "end ";
        Long result = runLuaScriptText(scriptText, Long.class, Lists.newArrayList(lockKey), dbType.ordinal() + "", requestId, delaySeconds + "");
        return result != null && result == 1;
    }

    //endregion


}
