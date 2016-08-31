package com.urgoo.domain;

import org.json.JSONException;
import org.json.JSONObject;

import com.urgoo.common.ZWConfig;

public class User {

	public static int TYPE_AUTO = 0;	//系统自动生成用户
	public static int TYPE_NORMAL = 1;	//已注册用户
	
	public static int STATUS_NO = 0;	//无
	public static int STATUS_HAS = 1;	//有但未验证
	public static int STATUS_VALIDATOR = 2;		//验证码验证
	public static int STATUS_AUTHEN = 3;  //实名认证
	
	public String userId = "";
	public String userface = "";
	public String nickname = "";
	public String password = "";
	public String phone = "";
	public int phoneStatus = 0;
	public String email = "";
	public int emailStatus = 0;
	public String sex = "";
	public String birthday = "";
	public long modifyTime = 0;
	public int type = 0;

	//public String realname;  //真实姓名
	//public String idcard;  //身份证号码

	public User(){
	}

	public User(String str){
		if(str != null && str.trim().length() > 0){
			String[] strs = str.split(ZWConfig.SPLITOR_STR);
			if(strs.length == 12){
				this.userId = strs[0].trim();
				this.userface = strs[1].trim();
				this.nickname = strs[2].trim();
				this.password = strs[3].trim();
				this.phone = strs[4].trim();
				this.phoneStatus = Integer.valueOf(strs[5].trim());
				this.email = strs[6].trim();
				this.emailStatus = Integer.valueOf(strs[7].trim());
				this.sex = strs[8].trim();
				this.birthday = strs[9].trim();
				this.modifyTime = Long.valueOf(strs[10].trim());
				this.type = Integer.valueOf(strs[11].trim());
			}
		}
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(userId != null ? userId : "").append(ZWConfig.SPLITOR_STR);
		sb.append(userface != null ? userface : "").append(ZWConfig.SPLITOR_STR);
		sb.append(nickname != null ? nickname : "").append(ZWConfig.SPLITOR_STR);
		sb.append(password != null ? password : "").append(ZWConfig.SPLITOR_STR);
		sb.append(phone != null ? phone : "").append(ZWConfig.SPLITOR_STR);
		sb.append(phoneStatus).append(ZWConfig.SPLITOR_STR);
		sb.append(email != null ? email : "").append(ZWConfig.SPLITOR_STR);
		sb.append(emailStatus).append(ZWConfig.SPLITOR_STR);
		sb.append(sex != null ? sex : "").append(ZWConfig.SPLITOR_STR);
		sb.append(birthday != null ? birthday : "").append(ZWConfig.SPLITOR_STR);
		sb.append(modifyTime).append(ZWConfig.SPLITOR_STR);
		sb.append(type);
		return sb.toString();
	}
	
	public String autoUserId(){
		return ZWConfig.APPCODE + System.currentTimeMillis() + (10000 + (int) (Math.random() * 10000));
	}
	
	public String autoPassword(){
		return ZWConfig.APPCODE + System.currentTimeMillis() + (10000 + (int) (Math.random() * 10000));
	}
	
	public User(JSONObject js){
		if(js != null){
			try {
				this.userId = js.has("userId")?js.getString("userId"):"";
				this.userface = js.has("userface")?js.getString("userface"):"";
				this.nickname = js.has("nickname")?js.getString("nickname"):"";
				this.password = js.has("password")?js.getString("password"):"";
				this.phone = js.has("phone")?js.getString("phone"):"";
				this.phoneStatus = js.has("phoneStatus")?js.getInt("phoneStatus"):0;
				this.email = js.has("email")?js.getString("email"):"";
				this.emailStatus = js.has("emailStatus")?js.getInt("emailStatus"):0;
				this.sex = js.has("sex")?js.getString("sex"):"";
				this.birthday = js.has("birthday")?js.getString("birthday"):"";
				this.modifyTime = js.has("modifyTime")?js.getLong("modifyTime"):0;
				this.type = js.has("type")?js.getInt("type"):0;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
