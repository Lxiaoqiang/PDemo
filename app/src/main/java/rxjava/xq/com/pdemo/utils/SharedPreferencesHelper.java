package rxjava.xq.com.pdemo.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Set;

/**
 * 
 * @author
 * 
 */
public class SharedPreferencesHelper {

	public static final String USER_ID = "userId";
	public static final String USER_BIND_STATUS = "bind_status";
	public static final String USER_BIND_MOB = "bind_mob";

	public static final String SHAREDPERFRENCE ="xdemo_shareperfrence";
	/**
	 * 判断是否存储过该用户
	 * 
	 * */
	public static boolean existsUser(final Context ctx) {
		boolean exists = false;
		final SharedPreferences settings = ctx.getSharedPreferences(
				SHAREDPERFRENCE, Context.MODE_PRIVATE);
		if (settings != null) {
			exists = settings.contains(USER_ID);
		}
		return exists;
	}

	/**
	 * 存储userId
	 * 
	 * */
	public static void putUser(final Context ctx, String userId) {
		ctx.getSharedPreferences(SHAREDPERFRENCE, Context.MODE_PRIVATE)
				.edit().putString(USER_ID, userId).commit();
	}

	/**
	 * 通过key存储value
	 * 
	 * */
	public static void putValueByKey(final Context ctx, String key, String value) {
		ctx.getSharedPreferences(SHAREDPERFRENCE, Context.MODE_PRIVATE)
				.edit().putString(key, value).commit();
	}

	/**
	 * 通过key获取value
	 * 
	 * */
	public static String getValueByKey(final Context ctx, String key,String defaultStr) {
		return ctx.getSharedPreferences(SHAREDPERFRENCE,
				Context.MODE_PRIVATE).getString(key, defaultStr);
	}
	/**
	 * 通过key存储SetString数组
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void putSetByKey(final Context ctx, String key, Set<String> value){
		ctx.getSharedPreferences(SHAREDPERFRENCE, Context.MODE_PRIVATE)
				.edit().putStringSet(key, value).commit();
	}
	/**
	 * 通过key获取SetString数组
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static Set<String> getSetByKey(final Context ctx, String key){
		return ctx.getSharedPreferences(SHAREDPERFRENCE, Context.MODE_PRIVATE).getStringSet(key,null);
	}
	/**
	 * 通过key获取boolean
	 * 
	 * @return
	 */
	public static boolean getBooleanByKey(final Context ctx, String key) {
		return ctx.getSharedPreferences(SHAREDPERFRENCE,
				Context.MODE_PRIVATE).getBoolean(key, false);
	}

	/**
	 * 通过key移除value
	 * 
	 * */
	public static void removeValueByKey(final Context ctx, String key) {
		final SharedPreferences sp = ctx.getSharedPreferences(
				SHAREDPERFRENCE, Context.MODE_PRIVATE);
		if (sp != null) {
			sp.edit().remove(key).commit();
		}
	}

	/**
	 * 判断该key值是否存在
	 * 
	 * */
	public static boolean existsByKey(final Context ctx, String key) {
		boolean exists = false;
		final SharedPreferences settings = ctx.getSharedPreferences(
				SHAREDPERFRENCE, Context.MODE_PRIVATE);
		if (settings != null) {
			exists = settings.contains(key);
		}
		return exists;
	}

}
