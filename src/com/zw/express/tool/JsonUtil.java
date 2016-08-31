package com.zw.express.tool;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

public class JsonUtil {


    // ***************华丽的分割线**********************************************************

    public static HashSet<String> getHashSet(JSONArray ja) throws JSONException {
        HashSet<String> hs = null;
        if (ja != null && ja.length() > 0) {
            hs = new HashSet<String>(ja.length());
            for (int i = 0; i < ja.length(); i++) {
                hs.add(ja.getString(i));
            }
        }
        return hs;
    }

    public static String getString(JSONObject j, String key) {
        String value = "";
        try {
            if (j.has(key) && !j.isNull(key)) {
                value = j.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static int getInt(JSONObject j, String key) {
        int value = 0;
        try {
            if (j.has(key) && !j.isNull(key)) {
                value = j.getInt(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static long getLong(JSONObject j, String key) {
        long value = 0;
        try {
            if (j.has(key) && !j.isNull(key)) {
                value = j.getInt(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void success(Context context, String jsonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString).getJSONObject("header");
            Toast.makeText(context.getApplicationContext(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
