package jp.keitai2013.heallin.chikara.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.crudefox.chikara.util.CFUtil;
import jp.crudefox.chikara.util.CFUtil.HttpResponseData;
import jp.keitai2013.heallin.R;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;


/**
 * 		@author Chikara Funabashi
 * 		@date 2013/08/09
 *
 */

public class ClothesManager {



	//static boolean LOCAL_DEBUG = true;


//	public static int LOGIN_RESULT_OK = 0;
//	public static int LOGIN_RESULT_FAILED = -1;


//	private static final String FREDNS_OK = "OK";



//	private static String FRENDS_URL = CFConst.SERVER + CFConst.FREDNS;
//	private static String SEARCH_FRENDS_URL = CFConst.SERVER + CFConst.SEARCH_FRENDS;

	private Context mContext;

	//private ArrayList<ConItem>mList = new ArrayList<ConItem>();

	private ArrayList<ClItem> mList = new ArrayList<ClItem>();


	public static class ClItem implements Serializable{
		public long id;
		public String image_url;
		public Bitmap image;
		//public String body;
	}




	public ClothesManager(Context context){
		mContext = context;
	}


	public boolean update(LoginInfo l){

		if(!l.isLoggedIn()) return false;


		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		//params.add(new BasicNameValuePair("sid", l.sid.sid));
		//params.add(new BasicNameValuePair("reload", ""+reload));


		ArrayList<Cookie> cookie = new ArrayList<Cookie>();
		cookie.add(new BasicClientCookie("PHPSESSID", l.sid.sid));

		for(int i=0;i<cookie.size();i++){
			BasicClientCookie c = (BasicClientCookie) cookie.get(i);
			c.setDomain("hellin.dip.jp");
			c.setPath("/");
		}


		HttpResponseData rd;

		//rd = CFUtil.postData( "http://hellin.dip.jp/m/Timeline.php", params, cookie);
		rd = CFUtil.postData( "http://hellin.dip.jp/m/DisplayOwnContent.php", params, cookie);


		if(rd==null || rd.data==null) return false;
		if(rd.data.equalsIgnoreCase("NG")) return false;

		log(rd.data);

		synchronized (ClothesManager.this) {

			mList.clear();

			try {

				JSONObject json = new JSONObject(rd.data);

				JSONArray jarr = json.getJSONArray("ownhellin");

				int  num = jarr.length();
				for(int i=0;i<num;i++){
					JSONObject jc = jarr.getJSONObject(i);

					ClItem item = new ClItem();
					item.id = jc.getLong("id");
					item.image_url = jc.getString("hellin");

					//item.icon = CFUtil.getImage(CFConst.SERVER+CFConst.PROFILE_IMEGES_FROM_USER_ID_DIR+item.id, 500, 500);

					mList.add(item);
				}

				CFUtil.Log("num="+mList.size());

				return true;
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				CFUtil.Log("clothes ", e);
				return false;
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				CFUtil.Log("clothes ", e);
				return false;
			}

		}


	}



	public boolean updateShop(LoginInfo l){

		if(!l.isLoggedIn()) return false;


		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		//params.add(new BasicNameValuePair("sid", l.sid.sid));
		//params.add(new BasicNameValuePair("reload", ""+reload));


		ArrayList<Cookie> cookie = new ArrayList<Cookie>();
		cookie.add(new BasicClientCookie("PHPSESSID", l.sid.sid));

		for(int i=0;i<cookie.size();i++){
			BasicClientCookie c = (BasicClientCookie) cookie.get(i);
			c.setDomain("hellin.dip.jp");
			c.setPath("/");
		}


		HttpResponseData rd;

		//rd = CFUtil.postData( "http://hellin.dip.jp/m/Timeline.php", params, cookie);
		rd = CFUtil.postData( "http://hellin.dip.jp/m/DisplayShopCostume.php", params, cookie);


		if(rd==null || rd.data==null) return false;
		if(rd.data.equalsIgnoreCase("NG")) return false;

		log(rd.data);

		synchronized (ClothesManager.this) {

			mList.clear();

			try {

				JSONObject json = new JSONObject(rd.data);

				JSONArray jarr = json.getJSONArray("shoplist");

				int  num = jarr.length();
				for(int i=0;i<num;i++){
					JSONObject jc = jarr.getJSONObject(i);

					ClItem item = new ClItem();
					item.id = jc.getLong("shopcostumeid");
					item.image_url = jc.getString("shopurl");

					//item.icon = CFUtil.getImage(CFConst.SERVER+CFConst.PROFILE_IMEGES_FROM_USER_ID_DIR+item.id, 500, 500);

					mList.add(item);
				}

				CFUtil.Log("num="+mList.size());

				return true;
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				CFUtil.Log("shop ", e);
				return false;
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				CFUtil.Log("shop ", e);
				return false;
			}

		}


	}





	public synchronized ClItem removeItemById(long id){
		ClItem mitem = getItemById(id);
		if(mitem==null) return null;
		if( mList.remove(mitem) ) return mitem;
		return null;
	}


	public synchronized void clear(){

		mList.clear();

	}




	public synchronized ClItem getItemById(long id){
		for(int i=0;i<mList.size();i++){
			ClItem item = mList.get(i);
			if( item.id == id ) return item;
		}
		return null;
	}

	public synchronized ClItem getItemByIndex(int index){
		if(index>=mList.size() || index<0) return null;
		return mList.get(index);
	}
	public synchronized int getItemLength(){
		return mList.size();
	}





	public boolean _update_mock(LoginInfo l){


		try {
			// Simulate network access.
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//if(outResult!=null) outResult[0] = LOGIN_ERR_CONNECT;
			return false;
		}

		Resources res = mContext.getResources();
		Bitmap[] bmps = new Bitmap[2];
		bmps[0] = ((BitmapDrawable) res.getDrawable(R.drawable.cloth_sample1)).getBitmap();
		bmps[1] = ((BitmapDrawable) res.getDrawable(R.drawable.cloth_sample1)).getBitmap();

		//Bitmap cloth_sample = ((BitmapDrawable) res.getDrawable(R.drawable.cloth_sample1)).getBitmap();


		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		ArrayList<ClItem> list  = new ArrayList<ClItem>();

		int num = ((int)(Math.random()*10)) + 10;

		try{
			for(int i=0;i<num;i++){
				int rand = ((int)(Math.random()*100)) ;

				ClItem item = new ClItem();
				item.id = i;
				//item.name = "ダイエットな服";
				item.image = bmps[rand%2];

				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		synchronized (ClothesManager.this) {
			mList.clear();
			mList = list;
			sortByDate(false);
		}

		return true;
	}


	private synchronized void sortByDate(final boolean asc){

		Comparator<ClItem> comp;

		if(asc){
			comp = new Comparator<ClItem>() {
				@Override
				public int compare(ClItem lhs, ClItem rhs) {
					if(lhs.id<rhs.id) return -1;
					if(lhs.id>rhs.id) return +1;
					return 0;
				}
			};
		}else{
			comp = new Comparator<ClItem>() {
				@Override
				public int compare(ClItem lhs, ClItem rhs) {
					if(lhs.id<rhs.id) return +1;
					if(lhs.id>rhs.id) return -1;
					return 0;
				}
			};
		}


		Collections.sort(mList, comp);


	}




	public static void log(String str){
		android.util.Log.d("test", str);
	}




}
