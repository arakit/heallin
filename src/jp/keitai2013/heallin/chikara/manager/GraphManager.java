package jp.keitai2013.heallin.chikara.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import jp.crudefox.chikara.util.CFUtil;
import jp.crudefox.chikara.util.CFUtil.HttpResponseData;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * 		@author Chikara Funabashi
 * 		@date 2013/08/09
 *
 **/

public class GraphManager {





	//static boolean LOCAL_DEBUG = true;


//	public static int LOGIN_RESULT_OK = 0;
//	public static int LOGIN_RESULT_FAILED = -1;


//	private static final String FREDNS_OK = "OK";



	private static String FRENDS_URL = CFConst.SERVER + CFConst.FREDNS;
	private static String SEARCH_FRENDS_URL = CFConst.SERVER + CFConst.SEARCH_FRENDS;

	private Context mContext;

	//private ArrayList<ConItem>mList = new ArrayList<ConItem>();

	private ArrayList<GItem> mList = new ArrayList<GItem>();


	public static class GItem implements Serializable{
		public String id;
		public double value;
		public double goalvalue;
		public Date time;
		//public String body;
	}




	public GraphManager(Context context){
		mContext = context;
	}


	//http://hellin.dip.jp/m/MemberList.php

	public synchronized boolean update_mirai(LoginInfo l){

		if(!l.isLoggedIn()) return false;

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();


		ArrayList<Cookie> cookie = new ArrayList<Cookie>();
		cookie.add(new BasicClientCookie("PHPSESSID", l.sid.sid));

		for(int i=0;i<cookie.size();i++){
			BasicClientCookie c = (BasicClientCookie) cookie.get(i);
			c.setDomain("hellin.dip.jp");
			c.setPath("/");
		}


		HttpResponseData rd;

		rd = CFUtil.postData( "http://hellin.dip.jp/m/DisplayValue.php", params, cookie);

		if(rd == null || rd.data == null) return false;

		log(rd.data);

		mList.clear();

		try {
			JSONObject json = new JSONObject(rd.data);

//			if(result.equalsIgnoreCase(LoginManager.BOARD_ERR_OVER_TIME_TOKEN )){
//				return false;
//			}

//			Resources res = mContext.getResources();
//			Bitmap[] bmps = new Bitmap[2];
//			bmps[0] = ((BitmapDrawable) res.getDrawable(R.drawable.ic_launcher)).getBitmap();
//			bmps[1] = ((BitmapDrawable) res.getDrawable(R.drawable.ic_launcher)).getBitmap();

			JSONArray jdata = json.getJSONArray("record");

			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			int  num = jdata.length();
			for(int i=0;i<num;i++){
				JSONObject jc = jdata.getJSONObject(i);

				GItem item = new GItem();
				item.id = ""+i;//jc.getString("friend");

				item.value = jc.getDouble("value");
				item.goalvalue = jc.getDouble("goalvalue");
				item.time = CFUtil.parseDateTme( jc.getString("record_time") );

				if(item.value<0.0 || item.value>200.0) continue;

				mList.add(item);
			}
			
			sortByDate(true);

			CFUtil.Log("num="+mList.size());

			return true;
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			CFUtil.Log("member ", e);
			return false;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			CFUtil.Log("member ", e);
			return false;
		}


	}



	public synchronized boolean update(LoginInfo l){

		if(CFConst.IS_MIRAI){
			return update_mirai(l);
		}

		if(!l.isLoggedIn()) return false;

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("sid", l.sid.sid));
//		params.add(new BasicNameValuePair("range_start", ""+start_time));
//		params.add(new BasicNameValuePair("range_end", ""+end_time));

		log("member 001");

		String data = null;

		//JSONObject json;
//		if(CFUtil.LOCAL_DEBUG){
//			//json = CUtil.getRawTextJson(mContext, R.raw.board_resp_sample);
//		}else{
//
//			//json = CUtil.postDataReturnJson(BOARD_URL, params);
//		}

		data = CFUtil.postData( FRENDS_URL, params);

		if(data==null) return false;

		log(data);

		mList.clear();

		log("frends 002");

		try {

			log("frends 003");

			JSONObject json = new JSONObject(data);

			String result = json.getString("result");

			log("frends 004");

			if(!result.equalsIgnoreCase("OK") ){
				return false;
			}

//			if(result.equalsIgnoreCase(LoginManager.BOARD_ERR_OVER_TIME_TOKEN )){
//				return false;
//			}

//			Resources res = mContext.getResources();
//			Bitmap[] bmps = new Bitmap[2];
//			bmps[0] = ((BitmapDrawable) res.getDrawable(R.drawable.ic_launcher)).getBitmap();
//			bmps[1] = ((BitmapDrawable) res.getDrawable(R.drawable.ic_launcher)).getBitmap();

			JSONArray jdata = json.getJSONArray("data_list");

			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			int  num = jdata.length();
			for(int i=0;i<num;i++){
				JSONObject jc = jdata.getJSONObject(i);

//				MItem item = new MItem();
//				item.id = jc.getString("user_id");
//
//				if(!jc.isNull("user_name"))
//					item.name = jc.getString("user_name");
//				else item.name = null;
//
//				item.icon = CFUtil.getImage(CFConst.SERVER+CFConst.PROFILE_IMEGES_FROM_USER_ID_DIR+item.id, 500, 500);
//
//				mList.add(item);
			}
			
			sortByDate(true);

			CFUtil.Log("num="+mList.size());

			return true;
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			CFUtil.Log("member ", e);
			return false;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			CFUtil.Log("member ", e);
			return false;
		}


	}



	
	public synchronized boolean postRecord(LoginInfo l, double value){

		if(!l.isLoggedIn()) return false;

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("value", ""+value));

		
		ArrayList<Cookie> cookie = new ArrayList<Cookie>();
		cookie.add(new BasicClientCookie("PHPSESSID", l.sid.sid));

		for(int i=0;i<cookie.size();i++){
			BasicClientCookie c = (BasicClientCookie) cookie.get(i);
			c.setDomain("hellin.dip.jp");
			c.setPath("/");
		}


		HttpResponseData rd;

		rd = CFUtil.postData( "http://hellin.dip.jp/m/UpdateGraph.php", params, cookie);

		if(rd == null || rd.data == null) return false;

		log(rd.data);

		if( !rd.data.equalsIgnoreCase("OK") ) return false;


		

		return true;
	}
	
	
	
	
	
	
	
	
	
	

	
	
	
	
//	public synchronized boolean deleteContoribute(LoginManager lf,LoginInfo li, int id){
//
//		if( lf.deleteContribute(li, id) ){
//			removeItemById(id);
//			return true;
//		}
//
//		return false;
//	}



	public synchronized GItem removeItemById(String id){
		GItem mitem = getItemById(id);
		if(mitem==null) return null;
		if( mList.remove(mitem) ) return mitem;
		return null;
	}


	public synchronized void clear(){

		mList.clear();

	}




	public synchronized GItem getItemById(String id){
		for(int i=0;i<mList.size();i++){
			GItem item = mList.get(i);
			if( item.id.equals(id) ) return item;
		}
		return null;
	}

	public synchronized GItem getItemByIndex(int index){
		if(index>=mList.size() || index<0) return null;
		return mList.get(index);
	}
	public synchronized int getItemLength(){
		return mList.size();
	}






	private synchronized void sortByDate(final boolean asc){

		Comparator<GItem> comp;

		if(asc){
			comp = new Comparator<GItem>() {
				@Override
				public int compare(GItem lhs, GItem rhs) {
//					if(lhs.name<rhs.time) return -1;
//					if(lhs.time>rhs.time) return +1;
//					return 0;
					return lhs.time.compareTo(rhs.time);
				}
			};
		}else{
			comp = new Comparator<GItem>() {
				@Override
				public int compare(GItem lhs, GItem rhs) {
//					if(lhs.time<rhs.time) return +1;
//					if(lhs.time>rhs.time) return -1;
//					return 0;
					return -lhs.time.compareTo(rhs.time);
				}
			};
		}


		Collections.sort(mList, comp);


	}




	public static void log(String str){
		android.util.Log.d("test", str);
	}




}
