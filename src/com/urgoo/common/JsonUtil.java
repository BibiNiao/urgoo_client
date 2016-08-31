package com.urgoo.common;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by bb on 2016/8/16.
 */
public class JsonUtil {
    /**
     * 删除特定元素
     *
     * @param jsonArray
     * @param index
     * @return
     */
    public static JSONArray remove(JSONArray jsonArray, int index) {
        JSONArray mJsonArray = new JSONArray();

        if (index < 0) return mJsonArray;
        if (index > jsonArray.length()) return mJsonArray;
        for (int i = 0; i < index; i++) {
            try {
                mJsonArray.put(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = index + 1; i < jsonArray.length(); i++) {
            try {
                mJsonArray.put(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mJsonArray;
    }

}
