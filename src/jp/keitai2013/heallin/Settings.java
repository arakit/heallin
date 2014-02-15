
package jp.keitai2013.heallin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Settings {


	public static final String KEY_USER = "user";
	public static final String KEY_PASSWORD = "password";


	public static void setUserAndPassword(Context context, String user, String pass){
		final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		final String key_u = KEY_USER;
		final String key_p = KEY_PASSWORD;
		final Editor edit = sp.edit();
		edit.putString(key_u, user);
		edit.putString(key_p, pass);
		edit.commit();
	}



	public static String getUser(Context context){
		final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		final String key = KEY_USER;
		return sp.getString(key, null);
	}
	public static String getPassword(Context context){
		final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		final String key = KEY_PASSWORD;
		return sp.getString(key, null);
	}




}
