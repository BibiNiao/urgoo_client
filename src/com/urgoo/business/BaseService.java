package com.urgoo.business;

import android.util.Log;

import com.urgoo.domain.NetHeaderInfoEntity;
import com.zw.express.tool.GsonTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/15.
 */
public class BaseService {


    public  static NetHeaderInfoEntity getNetHeadInfo(JSONObject obj){

        NetHeaderInfoEntity entity =null;

        Log.d("mytestheadinfo", "xxx>" + obj.toString());

        try{
            obj= new JSONObject(obj.get("header").toString());
            entity=GsonTools.getTargetClass(obj.toString(), NetHeaderInfoEntity.class);
        }catch (JSONException e){
            e.printStackTrace();

        }
        return  entity;


    }

}
