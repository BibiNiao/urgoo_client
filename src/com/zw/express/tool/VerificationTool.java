package com.zw.express.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/5/18.
 */
public class VerificationTool {

    public static boolean verificationStr(String _str){
        String str = _str.replaceAll(" ", "");
        //String str = "/";
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return  m.find();
    }
}
