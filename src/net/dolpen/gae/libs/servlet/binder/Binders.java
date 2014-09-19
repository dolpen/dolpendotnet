package net.dolpen.gae.libs.servlet.binder;

import com.google.appengine.labs.repackaged.com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by yamada on 2014/09/19.
 */
public class Binders {
    public static class IntegerBinder implements TypeBinder<Integer> {
        @Override
        public Integer bind(String[] src) {
            try {
                return Integer.valueOf(src[0]);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class LongBinder implements TypeBinder<Long> {
        @Override
        public Long bind(String[] src) {
            try {
                return Long.valueOf(src[0]);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class StringBinder implements TypeBinder<String> {
        @Override
        public String bind(String[] src) {
            return src == null || src.length == 0 ? null : src[0];
        }
    }

    public static Map<Class, TypeBinder> map = ImmutableMap.<Class, TypeBinder>builder()
            .put(String.class, new StringBinder())
            .put(Integer.class, new IntegerBinder())
            .put(Long.class, new LongBinder())
            .build();

}
