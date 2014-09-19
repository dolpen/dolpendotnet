package net.dolpen.gae.libs.cache;

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import com.google.appengine.labs.repackaged.com.google.common.base.*;
import net.sf.jsr107cache.*;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by yamada on 2014/09/19.
 */
public class CacheEx<T> {

    private Cache cache;

    public final String key;


    private Optional<T> value = Optional.absent();

    private boolean checked;

    /**
     * @param name   cache name
     * @param params parameters
     */
    @SuppressWarnings("unchecked")
    public CacheEx(String name, int dulation, Object... params) {
        Map props = new HashMap();
        props.put(GCacheFactory.EXPIRATION_DELTA, dulation);
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(props);
        } catch (CacheException e) {
            throw new RuntimeException("cache open failed", e);
        }
        this.key = makeKey(name, params);
        checked = false;
    }


    /**
     * 名前とパラメータからキーを生成します.
     *
     * @param name   cache
     * @param params parameters
     * @return key
     */
    public static String makeKey(String name, Object... params) {
        final StringBuilder sb = new StringBuilder();
        for (Object obj : params) {
            if (obj instanceof Iterable) {
                Joiner.on('_').useForNull("N").appendTo(sb, (Iterable<?>) obj);
            } else {
                sb.append(Optional.fromNullable(obj).or("N"));
            }
            sb.append(':');
        }
        sb.append(name);
        return sb.toString();
    }


    /**
     * @return キャッシュヒットした場合 <code>true</code>
     * キャッシュが存在してもvalueがnullなら<code>false</code>を返す.
     */
    @SuppressWarnings("unchecked")
    public boolean hit() {
        if (!checked) {
            checked = true;
            value = Optional.fromNullable((T) cache.get(key));
        }
        return value.isPresent();
    }

    /**
     * @return キャッシュヒットした場合、キャッシュされた値. ヒットしなかった場合 <code>null</code>.
     */
    public T get() {
        return hit() ? value.get() : null;
    }

    /**
     * @return キャッシュヒットした場合、キャッシュされた値. ヒットしなかった場合 valueLoaderから得た値をキャッシュして返す.
     */
    public T getOrElse(Callable<? extends T> valueLoader) {
        if (hit()) {
            return value.get();
        }
        try {
            T o = valueLoader.call();
            cache.put(key, o);
            return o;
        } catch (Exception e) {
            throw new RuntimeException("cache value load failed", e);
        }
    }

    /**
     * 指定された値をキャッシュします.
     *
     * @param o 任意の値
     * @return o
     */
    public T set(T o) {
        cache.remove(key);
        cache.put(key, o);
        value = Optional.fromNullable(o);
        return o;
    }

    /**
     * エントリが存在しない場合のみ、指定された値をキャッシュに追加します.
     *
     * @param o 任意の値
     */
    public void add(T o) {
        if (hit()) return;
        cache.put(key, o);
        checked = false;
    }

    /**
     * 既にエントリが存在する場合のみ値を入れ替えます.
     *
     * @param o 任意の値
     */
    public void replace(T o) {
        if (!hit()) return;
        cache.remove(key);
        cache.put(key, o);
        checked = false;
    }

    /**
     * キャッシュエントリを削除します.
     */
    public void delete() {
        if (!hit()) return;
        cache.remove(key);
        value = Optional.absent();
    }

}
