package net.dolpen.gae.libs.servlet;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Post/Getパラメータを扱うヘルパーです。
 *
 * @author yamada
 */
public class RequestParam {
    public static final long MAX_FILE_SIZE = 50 * 1024 * 1024L;
    private Map<String, String[]> map;
    HttpServletRequest request;

    @SuppressWarnings("unchecked")
    public RequestParam(HttpServletRequest req) {
        map = (Map<String, String[]>) req.getParameterMap();
        request = req;
    }

    /**
     * キーからファイルストリームを取得します。
     *
     * @param key 　キー
     * @return <code>FileItemStream</code> または <code>null</code>
     */
    public FileItemStream getFile(String key) {
        ServletFileUpload fileUpload = new ServletFileUpload();
        fileUpload.setSizeMax(MAX_FILE_SIZE);
        try {
            request.setCharacterEncoding("utf-8");
            FileItemIterator itemIterator = fileUpload.getItemIterator(request);
            while (itemIterator.hasNext()) {
                FileItemStream itemStream = itemIterator.next();
                if (key.equals(itemStream.getFieldName())) {
                    return itemStream;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * キーから単一の文字列を取得します。
     *
     * @param key 　キー
     * @return <code>String</code> または <code>null</code>
     */
    public String getString(String key) {
        if (!map.containsKey(key))
            return null;
        return map.get(key)[0];
    }

    /**
     * キーから単一の文字列を取得します。
     *
     * @param key 　キー
     * @return <code>String</code>
     */
    public String getStringExcludeNull(String key) {
        if (!map.containsKey(key))
            return "";
        return map.get(key)[0];
    }

    /**
     * キーから文字列のリストを取得します。
     *
     * @param key 　キー
     * @return <code>List&lt;String&gt;</code>
     */
    public List<String> getStringList(String key) {
        if (!map.containsKey(key)) {
            return Lists.newArrayList();
        }
        return Arrays.asList(map.get(key));
    }

    /**
     * キーから単一の数値を取得します。 変換できない、または存在しなければ<code>null</code>
     *
     * @param key 　キー
     * @return <code>Integer</code> または <code>null</code>
     */
    public Integer getInteger(String key) {
        try {
            return Integer.valueOf(getString(key));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * キーから数値のリストを取得します。
     *
     * @param key 　キー
     * @return <code>List&lt;Integer&gt;</code>
     */
    public List<Integer> getIntegerList(String key) {
        List<String> strList = getStringList(key);
        List<Integer> intList = Lists.newArrayList();
        try {
            for (String str : strList) {
                intList.add(Integer.valueOf(str));
            }
        } catch (Exception e) {
            return Lists.newArrayList();
        }
        return intList;
    }

    /**
     * キーから単一の数値を取得します。 変換できない、または存在しなければ<code>null</code>
     *
     * @param key 　キー
     * @return <code>Long</code> または <code>null</code>
     */
    public Long getLong(String key) {
        try {
            return Long.valueOf(getString(key));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * キーから数値のリストを取得します。
     *
     * @param key 　キー
     * @return <code>List&lt;Long&gt;</code>
     */
    public List<Long> getLongList(String key) {
        List<String> strList = getStringList(key);
        List<Long> longList = Lists.newArrayList();
        try {
            for (String str : strList) {
                longList.add(Long.valueOf(str));
            }
        } catch (Exception e) {
            return Lists.newArrayList();
        }
        return longList;
    }
}