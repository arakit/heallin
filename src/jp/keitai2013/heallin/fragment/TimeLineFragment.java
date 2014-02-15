package jp.keitai2013.heallin.fragment;


import java.text.DateFormat;

import jp.crudefox.chikara.util.AdapterBridge;
import jp.crudefox.chikara.util.BitmapLruCache;
import jp.crudefox.chikara.util.CFCardUIAdapter;
import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.AppManager;
import jp.keitai2013.heallin.Const;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.chikara.manager.CFConst;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.chikara.manager.LoginManager;
import jp.keitai2013.heallin.chikara.manager.LoginManager.SetGoodInfo;
import jp.keitai2013.heallin.chikara.manager.PictureManager;
import jp.keitai2013.heallin.chikara.manager.PictureManager.OnUpdateImageListner;
import jp.keitai2013.heallin.chikara.manager.TimeLineManager;
import jp.keitai2013.heallin.chikara.manager.TimeLineManager.OnUpdateTimelineImageListner;
import jp.keitai2013.heallin.chikara.manager.TimeLineManager.TItem;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

/**
 * 		@auth Chikara Funabashi
 * 		@date 2013/08/08
 *
 */

/**
 * 		タイムライン画面
 * 		ホーム画面内にタブとして配置される
 *
 */

public class TimeLineFragment extends SherlockFragment {






	private Context mContext;
	private View mRootView;

	private AppManager mAppManager;
	private PictureManager mPictureManager;

	private boolean mIsFirst = true;

	private Handler mHandler = new Handler();

	//private CFOverScrolledListView mListView;
	private ListView mListView;
	private PullToRefreshListView mPullToRefreshListView;

	private LoginManager mLoginManager;
	private TimeLineManager mTLManager;

	private LayoutInflater mLayoutInflater;
	private DateFormat mDateFormat;


	private GetTimeLineTask mGetBoradTask;
	private SetGoodTask mSetGoodTask;
	private DeletePostTask mDeletePostTask;

	private LoginInfo mLoginInfo;



	private DeleteContributeTask mDelTask;

	private CFCardUIAdapter<TItem> mCAdapter;


	private RequestQueue mQueue;
    private ImageLoader mImageLoader;



	public TimeLineFragment() {
		super();
		setHasOptionsMenu(true);
	}




	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}


	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//super.onCreateView(inflater, container, savedInstanceState);

		CFUtil.Log("onCreateView "+this);


		setContentView(R.layout.fragment_timeline);

		//mListView  = (CFOverScrolledListView) findViewById(R.id.timeline_listView);
		mPullToRefreshListView  = (PullToRefreshListView) findViewById(R.id.timeline_listView);
		CFUtil.Log(""+mPullToRefreshListView);
		ListView actualListView = mListView = mPullToRefreshListView.getRefreshableView();


		Bundle bundle = getArguments();

		//Intent intent = getIntent();
		mLoginInfo = (LoginInfo) bundle.getSerializable(Const.AK_LOGIN_INFO);

//		if(CFUtil.isOk_SDK(9)){
//			mListView.setOverscrollHeader(
//					getResources().getDrawable(R.drawable.update_over_scrolled));
//		}

		mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);


		mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				TItem item = mCAdapter.getItem(position-1);
				if(item==null) return ;
				showPostMenuDlg(item);

			}
		});

		// Set a listener to be invoked when the list should be refreshed.
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				//new GetDataTask().execute();
				attemptGetBorad();
			}
		});

		// Add an end-of-list listener
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//Toast.makeText(PullToRefreshListActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
			}
		});



		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);

		/**
		 * Add Sound Event Listener
		 */
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(getActivity());
//		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pyo1);
//		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
//		soundListener.addSoundEvent(State.REFRESHING, R.raw.cat5);
		mPullToRefreshListView.setOnPullEventListener(soundListener);

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		mPullToRefreshListView.setAdapter(mCAdapter);


		//mListView.setAdapter(mCAdapter);

		if(mLoginInfo!=null){
			//CFUtil.Log("length = "+mTLManager.getItemLength());
//			if(mTLManager.getItemLength()==0){
//				postAttemptGetBorad(100);
//			}
		}else{
			toast("ログインに失敗しています。");
			finish();
		}

		updateListView();

		//mListView.setOnOverScrolledListener(mOverScrolledListener);

		return mRootView;

	}





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();


		mLayoutInflater = getLayoutInflater();
		mDateFormat = DateFormat.getInstance();

		mCAdapter = new TimeLineListAdapter(mContext);

		mAppManager = (AppManager) getActivity().getApplication();

		mLoginManager = mAppManager.getLoginManager();

//		if(mLoginManager==null){
//			mLoginManager = new LoginManager(getApplicationContext());
//		}
		mPictureManager = mAppManager.getPictureManager();
		mTLManager = new TimeLineManager(getApplicationContext(), mPictureManager);


		mQueue = CFUtil.newRequestQueue(getActivity(), 32*1024*1024);
        mImageLoader = new ImageLoader(mQueue, new BitmapLruCache());


	}




	private View findViewById(int id){
		return mRootView.findViewById(id);
	}
	private LayoutInflater getLayoutInflater(){
		return getActivity().getLayoutInflater();
	}
	private void setContentView(int id){
		mRootView = getLayoutInflater().inflate(id, null);
	}
	private Context getApplicationContext(){
		return mContext.getApplicationContext();
	}

	private void finish(){
		Activity act = getActivity();
		if(act!=null){
			act.finish();
		}else{

		}
	}


	private void postAttemptGetBorad(long delayed){
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Activity activity = getActivity();
				if(activity==null) return ;
				if(activity.isFinishing()) return ;
				attemptGetBorad();
			}
		}, delayed);
	}

	private void attemptGetBorad(){
		if(mGetBoradTask!=null){
			return ;
		}

		mGetBoradTask = new GetTimeLineTask();
		mGetBoradTask.execute((Void)null);

	}

	private void attemptSetGoods(long post_id, String goods_val){
		if(mSetGoodTask!=null){
			return ;
		}

		mSetGoodTask = new SetGoodTask();
		mSetGoodTask.execute(post_id, goods_val);

	}

	private void attemptDeletePost(long post_id){
		if(mDeletePostTask!=null){
			return ;
		}

		mDeletePostTask = new DeletePostTask();
		mDeletePostTask.execute(post_id);
	}

	private void updateListView(){

		mCAdapter.clearItems();

		TItem[] arr = mTLManager.getItems();
		for(int i=0;i<arr.length;i++){
			TItem item = arr[i];
			mCAdapter.addItem(item, 0);
		}

		mCAdapter.notifyDataSetChanged();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.board, menu);
//		return true;
//	}



//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//
//		//Intent intent;
//
//		int id = item.getItemId();
//		switch(id){
//		case R.id.action_write:
//
//			//showContiributeDlg();
//			showContiributeActivity();
//
//			return true;
//		case R.id.action_update:
//
//			attemptGetBorad();
//
//			return true;
//		}
//
//		return false;
//	}

	public void refreshTimeLine(){

		if(mPullToRefreshListView==null) return;

		if(!mPullToRefreshListView.isRefreshing()){
			mPullToRefreshListView.setRefreshing();
		}

	}

	private void showPostMenuDlg(final TItem item){

		AlertDialog.Builder ab = new AlertDialog.Builder(getSherlockActivity());

		final AdapterBridge<Integer> bri = new AdapterBridge<Integer>();

		bri.addItem("削除", 100);

		ab.setItems(bri.getItemTextArray(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				int id = bri.getItem(which);
				if(id==100){
					showDeletePostDlg(item.id);
				}

			}
		});

		AlertDialog dlg = ab.create();

		dlg.show();
	}


	private void showDeletePostDlg(final long post_id){

		AlertDialog.Builder ab = new AlertDialog.Builder(getSherlockActivity());

		ab.setTitle("確認");
		ab.setMessage("削除してよろしいですか。");

		ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				attemptDeletePost(post_id);
			}
		});
		ab.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		AlertDialog dlg = ab.create();

		dlg.show();
	}



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);

		MenuItem mi;
		int order = 20;

		mi = menu.add(Menu.NONE, Const.MENU_ID_UPDATE_TIMELINE,order++,"更新");
		mi.setIcon(android.R.drawable.ic_popup_sync);
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		super.onCreateOptionsMenu(menu, inflater);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);

		int id = item.getItemId();

		if(id== Const.MENU_ID_UPDATE_TIMELINE){
			attemptGetBorad();
			return true;
		}

		return false;
	}




//	@Override
//	public void onBackPressed() {
//		// TODO 自動生成されたメソッド・スタブ
//		super.onBackPressed();
//	}




	@Override
	public void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
		cancelTuusin();
	}



	@Override
	public void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStart();

		mQueue.start();

		if(mIsFirst){
			mIsFirst = false;

			mPullToRefreshListView.postDelayed(new Runnable() {
				@Override
				public void run() {
					mPullToRefreshListView.setRefreshing(true);
				}
			}, 750);
		}

		mPictureManager.addOnUpdateImageListener(mOnUpdateImageListner);
	}

	@Override
	public void onStop() {
		super.onStop();

		mQueue.stop();

		cancelTuusin();
		mPictureManager.removeOnUpdateImageListener(mOnUpdateImageListner);

		mPullToRefreshListView.onRefreshComplete();

	}

	private final OnUpdateImageListner mOnUpdateImageListner = new OnUpdateImageListner() {
		@Override
		public void onUpdateImage(PictureManager pm, String url) {
			//mCAdapter.notifyDataSetChanged();
		}
	};
	private final OnUpdateTimelineImageListner mOnUpdateTimelineImageListner = new OnUpdateTimelineImageListner() {
		@Override
		public void onUpdate(TItem item, String url) {
			CFUtil.Log("OnUpdateTimelineImageListner onUpdate "+url);
			mCAdapter.notifyDataSetChanged();
		}
	};


	private void cancelTuusin(){
		if(mGetBoradTask!=null){
			mGetBoradTask.cancel(true);
		}
		if(mSetGoodTask!=null){
			mSetGoodTask.cancel(true);
		}
		if(mDeletePostTask!=null){
			mDeletePostTask.cancel(true);
		}
	}



	/**
	 * doBack, Progress, postExecute
	 */
	private class GetTimeLineTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			postToast("更新中...");

			TimeLineManager tm = mTLManager;
			LoginInfo l = mLoginInfo;
			OnUpdateTimelineImageListner lis = mOnUpdateTimelineImageListner;

			if( !tm.update_mirai(l, 0, lis, true) ){
				return false;
			}

			tm.sortByDate(false);

			mHandler.post(new Runnable() {
				@Override
				public void run() {
					updateListView();
				}
			});


			try { Thread.sleep(1000*3); } catch (InterruptedException e) { }

			while(true){
				int num1 = tm.size();
				if( !tm.update_mirai(l, 1, lis, false) ){
					return false;
				}
				int num2 = tm.size();
				if(num1 == num2) break;
			}

			tm.sortByDate(false);

			return true;


			//boolean result = mTLManager.update(mLoginInfo, mOnUpdateTimelineImageListner);

			//return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetBoradTask = null;
			//showProgress(false);

			if(success){
				toast("投稿を取得しました。\nヽ(=´▽`=)ﾉ");
				updateListView();
			}else{
				toast("投稿を取得出来ませんでした。\n(´・ω・`)");
			}

			mPullToRefreshListView.onRefreshComplete();

		}

		@Override
		protected void onCancelled() {
			mGetBoradTask = null;
			//showProgress(false);
		}
	}

	/**
	 * doBack, Progress, postExecute
	 */
	private class SetGoodTask extends AsyncTask<Object, Void, Boolean> {

		String mGoodVal;
		SetGoodInfo mInfo;

		@Override
		protected Boolean doInBackground(Object... params) {
			// TODO: attempt authentication against a network service.

			Long post_id = (Long)params[0];
			String good_val = mGoodVal = (String)params[1];

			postToast(good_val.toUpperCase()+"...");

			mInfo = mLoginManager.setGood(mLoginInfo, post_id, good_val);

			return mInfo!=null;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mSetGoodTask = null;
			//showProgress(false);

			if(success){
				toast(mGoodVal.toUpperCase()+"!");
				//updateListView();
				for(int i=0;i<mCAdapter.getCount();i++){
					TItem item = mCAdapter.getItem(i);
					if(item.id == mInfo.post_id){
						if(CFConst.IS_MIRAI){
							if(mGoodVal.equals("good")) item.good_num++;
							if(mGoodVal.equals("bad"))	item.bad_num++;
							item.enable_bad = item.enable_good = false;
						}else{
							item.good_num = mInfo.good_num;
							item.bad_num = mInfo.bad_num;
						}
					}
				}
				mCAdapter.notifyDataSetChanged();
			}else{
				toast("failed.");
			}

		}

		@Override
		protected void onCancelled() {
			mSetGoodTask = null;
			//showProgress(false);
		}
	}



	/**
	 * doBack, Progress, postExecute
	 */
	private class DeletePostTask extends AsyncTask<Object, Void, Boolean> {

		long mmPostId;

		@Override
		protected Boolean doInBackground(Object... params) {
			// TODO: attempt authentication against a network service.

			mmPostId = (Long)params[0];

			boolean suc = mLoginManager.deletePost(mLoginInfo, mmPostId);

			return suc;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			//showProgress(false);

			if(success){
				toast("削除しました。");
				//updateListView();
				for(int i=0;i<mCAdapter.getCount();i++){
					TItem item = mCAdapter.getItem(i);
					if(item.id == mmPostId){
						mCAdapter.removeItemByItem(item);
						break;
					}
				}
				mCAdapter.notifyDataSetChanged();
			}else{
				toast("削除できませんでした。");
			}

			mDeletePostTask = null;
		}

		@Override
		protected void onCancelled() {
			mDeletePostTask = null;
			//showProgress(false);
		}
	}



	private class TimeLineListAdapter extends CFCardUIAdapter<TItem>{

		final int [] mmColors = new int[]{
				Color.parseColor("#80acffac"),
				Color.parseColor("#80ffacac"),
				Color.parseColor("#80acacff"),
				Color.parseColor("#80acacac"),
		};

		public TimeLineListAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View line = convertView;
			if(line==null){
				line =  mLayoutInflater.inflate(R.layout.timeline_list_item, null);
			}

			final TItem bitem = mTLManager.getItemByIndex(position);

			//ListData data = mListData.get(position);

			//#80acffac

			View content = line.findViewById(R.id.content);
			TextView text_user = (TextView) line.findViewById(R.id.timeline_list_text_user);
			TextView text_date = (TextView) line.findViewById(R.id.timeline_list_text_date);
			TextView text_summary = (TextView) line.findViewById(R.id.timeline_list_summary);
			TextView text_good_num = (TextView) line.findViewById(R.id.timeline_list_good_num);
			TextView text_bad_num = (TextView) line.findViewById(R.id.timeline_list_bad_num);
			final ImageView icon_icon = (ImageView) line.findViewById(R.id.timeline_list_icon);
			final ImageView icon_picture = (ImageView) line.findViewById(R.id.timeline_list_picture);
			Button btn_good = (Button) line.findViewById(R.id.timeline_list_good);
			Button btn_bad = (Button) line.findViewById(R.id.timeline_list_bad);
			final ProgressBar loading_progress = (ProgressBar) line.findViewById(R.id.loading_progress);

			View content_text = line.findViewById(R.id.timeline_list_content_text);
			View content_picture = line.findViewById(R.id.timeline_list_content_picture);



			String str_titme = mDateFormat.format(bitem.time);

			text_user.setText(bitem.user_name+"/"+bitem.user_id);
			text_date.setText(str_titme);

			text_summary.setText(bitem.summary!=null?bitem.summary:"");

			text_good_num.setText(""+bitem.good_num);
			text_bad_num.setText(""+bitem.bad_num);

//			icon_icon.setImageBitmap(bitem.user_icon);

			ImageContainer ic1 = mImageLoader.get(bitem.user_icon_url, new ImageListener() {

	            @Override
	            public void onErrorResponse(VolleyError error) {
	            	icon_icon.setImageResource(android.R.drawable.ic_dialog_alert);
	            }

	            @Override
	            public void onResponse(ImageContainer response, boolean isImmediate) {
	                if (response.getBitmap() != null) {
	                	Bitmap bmp = response.getBitmap();
	                	bitem.user_icon = bmp;
	                    icon_icon.setImageBitmap(bmp);
	                } else {
	                    icon_icon.setImageResource(android.R.drawable.ic_menu_gallery);
	                }
	            }
	        }, 128, 1280);

			if(ic1.getBitmap()!=null){
            	Bitmap bmp = ic1.getBitmap();
                icon_icon.setImageBitmap(bmp);
			}

//			if(bitem.user_icon!=null){
//				icon_icon.setImageBitmap(bitem.user_icon);
//			}else{
//				icon_icon.setImageResource(android.R.drawable.ic_menu_gallery);
//			}

			int concol_ran = position % mmColors.length;
			content.setBackgroundColor(mmColors[concol_ran]);


//			if(mLoginInfo.isMyId(bitem.user_id)){
//				btn_del.setVisibility(View.VISIBLE);
//				btn_del.setOnClickListener(new OnDelBtnClickListener(bitem.id));
//			}else{
//				btn_del.setVisibility(View.INVISIBLE);
//				btn_del.setOnClickListener(null);
//			}

			final long post_id = bitem.id;
			btn_good.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//toast("good");
					attemptSetGoods(post_id, "good");
				}
			});
			btn_bad.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//toast("bad");
					attemptSetGoods(post_id, "bad");
				}
			});

			btn_good.setEnabled(bitem.enable_good);
			btn_bad.setEnabled(bitem.enable_bad);


			content_text.setVisibility(View.GONE);
			content_picture.setVisibility(View.VISIBLE);


//			ImageListener picture_listener = ImageLoader.getImageListener(icon_picture,
//					android.R.drawable.ic_menu_gallery /* 表示待ち時の画像 */,
//				    android.R.drawable.ic_dialog_alert /* エラー時の画像 */);

			loading_progress.setVisibility(View.VISIBLE);

			ImageContainer ic2 = mImageLoader.get(bitem.image_url, new ImageListener() {

	            @Override
	            public void onErrorResponse(VolleyError error) {
	                icon_picture.setImageResource(android.R.drawable.ic_dialog_alert);
	    			loading_progress.setVisibility(View.GONE);
	            }

	            @Override
	            public void onResponse(ImageContainer response, boolean isImmediate) {
	                if (response.getBitmap() != null) {
	                	Bitmap bmp = response.getBitmap();
	                	bitem.image = bmp;
	    				bmp.setDensity(50);
	                    icon_picture.setImageBitmap(bmp);
		    			loading_progress.setVisibility(View.GONE);
	                } else {
	                    icon_picture.setImageResource(android.R.drawable.ic_menu_gallery);
		    			if(!isImmediate) loading_progress.setVisibility(View.GONE);
	                }
	            }
	        }, 320, 320);

			if(ic2.getBitmap()!=null){
            	Bitmap bmp = ic2.getBitmap();
				bmp.setDensity(50);
                icon_picture.setImageBitmap(bmp);
    			loading_progress.setVisibility(View.GONE);
			}

//			bitem.image = mPictureManager.getBitmap(bitem.image_url);
//			if(bitem.image!=null){
//				loading_progress.setVisibility(View.GONE);
//				bitem.image.setDensity(50);
//				icon_picture.setImageBitmap(bitem.image);
//			}else{
//				icon_picture.setImageResource(android.R.drawable.ic_menu_gallery);
//			}

			if( isCardMotion(position) ){
				doCardMotion(line, R.anim.card_ui_motion_from_right);
				setCardMotion(position, false);

//				if(bitem.image==null && !mPictureManager.containsBitmapKey(bitem.image_url)){
//					loading_progress.setVisibility(View.VISIBLE);
//					mPictureManager.requestDownloadBtmap(bitem.image_url, new GetImageTaskListener() {
//						@Override
//						public void onUpdateImage(PictureManager pm, final String url) {
//							mHandler.post(new Runnable() {
//								@Override
//								public void run() {
//									CFUtil.Log("update iamge url "+url);
//									notifyDataSetChanged();
//								}
//							});
//						}
//						@Override
//						public Bitmap onGetTask(PictureManager pm, String url) {
//							Bitmap bmp = PictureManager.downloadImageToBmp(url);
//							//Bitmap bmp = CFUtil.getImage(url, 200, 4000);
//							CFUtil.Log("update iamge bmpl "+bmp);
//							return bmp;
//						}
//					});
//				}
			}


			return line;
		}

	}


//	private final BaseAdapter mAdapter = new BaseAdapter() {
//
//		int mLastAnimationPosition = -1;
//		int mFirstAnimationPosition = Integer.MAX_VALUE;
//
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View line = convertView;
//			if(line==null){
//				line =  mLayoutInflater.inflate(R.layout.timeline_list_item, null);
//			}
//
//			BItem bitem = mTLManager.getItemByIndex(position);
//
//			//ListData data = mListData.get(position);
//
//			TextView text_user = (TextView) line.findViewById(R.id.timeline_list_text_user);
//			TextView text_date = (TextView) line.findViewById(R.id.timeline_list_text_date);
//			ImageView icon_icon = (ImageView) line.findViewById(R.id.timeline_list_icon);
//			ImageView icon_picture = (ImageView) line.findViewById(R.id.timeline_list_picture);
//			Button btn_good = (Button) line.findViewById(R.id.timeline_list_good);
//			Button btn_bad = (Button) line.findViewById(R.id.timeline_list_bad);
//
//			View content_text = line.findViewById(R.id.timeline_list_content_text);
//			View content_picture = line.findViewById(R.id.timeline_list_content_picture);
//
//
//			String str_titme = mDateFormat.format(new Date(bitem.time));
//
//			text_user.setText(bitem.user_name+"/"+bitem.user_id);
//			text_date.setText(str_titme);
//
//			icon_icon.setImageBitmap(bitem.user_icon);
//			icon_picture.setImageBitmap(bitem.picture);
//
//
//			if (mLastAnimationPosition < position) {
//		        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.card_ui_motion_from_right);
//		        line.startAnimation(animation);
//		        mLastAnimationPosition = position;
//		    }
//			else if (mFirstAnimationPosition > position) {
//		        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.card_ui_motion_from_right);
//		        line.startAnimation(animation);
//		        mFirstAnimationPosition = position;
//		    }
//
////			if(mLoginInfo.isMyId(bitem.user_id)){
////				btn_del.setVisibility(View.VISIBLE);
////				btn_del.setOnClickListener(new OnDelBtnClickListener(bitem.id));
////			}else{
////				btn_del.setVisibility(View.INVISIBLE);
////				btn_del.setOnClickListener(null);
////			}
//
//			btn_good.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					toast("good");
//				}
//			});
//			btn_bad.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					toast("bad");
//				}
//			});
//
//
//			content_text.setVisibility(View.GONE);
//			content_picture.setVisibility(View.VISIBLE);
//
//
//			return line;
//		}
//
//		public long getItemId(int position) {
//			return position;
//		}
//
//		public Object getItem(int position) {
//			return mTLManager.getItemByIndex(position);
//		}
//
//		public int getCount() {
//			return mTLManager.getItemLength();
//		}
//
//	};






	private class OnDelBtnClickListener implements View.OnClickListener{

		int mmId;

		public OnDelBtnClickListener(int id) {
			mmId = id;
		}

		@Override
		public void onClick(View v) {

			if( mDelTask!=null ) return ;

			final TItem bitem = mTLManager.getItemById(mmId);

			AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
			ab.setTitle("確認");
			ab.setMessage("削除しますか。");

			ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDelTask = new DeleteContributeTask();
					mDelTask.execute(bitem.id);
				}
			});
			ab.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});

			ab.create().show();

		}
	}





	public class DeleteContributeTask extends AsyncTask<Long, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Long... params) {


			//Toast.makeText(LoginActivity.this, "バックグラウンド処理中です！", Toast.LENGTH_SHORT).show();

			//通信処理

			//LoginInfo lf = mLoginManager.login(mId, mPassword);
			//boolean result = mTLManager.deleteContoribute(mLoginManager , mLoginInfo, params[0]);
			boolean result =false;
			return result;
		}

		@Override
		protected void onPostExecute(final Boolean result) {
			mDelTask = null;
//			mAuthTask = null;
//			showProgress(false);


//			if (lf!=null) {
//
//				Intent data = new Intent();
//				data.putExtra(Const.AK_LOGIN_INFO, lf);
//				setResult(RESULT_OK, data);
//				finish();
//			} else {
//				mPasswordView
//						.setError(getString(R.string.error_incorrect_password));
//				mPasswordView.requestFocus();
//			}

			if(result){
				toast("削除しました。");
				//mBoradManager.removeItemById(id)
				updateListView();
			}
		}

		@Override
		protected void onCancelled() {
			mDelTask = null;
			//mAuthTask = null;
			//showProgress(false);
		}

	}


//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		if(requestCode==REQCODE_CONTRIBUTE_SUBMIT){
//			if(resultCode==RESULT_OK){
//
//				int id = data.getIntExtra(Const.AK_SUBMIT_CONTRIBUTE_ID, -1);
//				LoginInfo lf = (LoginInfo) data.getSerializableExtra(Const.AK_LOGIN_INFO);
//				if(lf!=null){
//					mLoginInfo = lf;
//				}
//
////				LoginInfo lf = (LoginInfo) data.getSerializableExtra(Const.AK_LOGIN_INFO);
////
////				Intent intent = new Intent(StartActivity.this, BoardActivity.class);
////				intent.putExtra(Const.AK_LOGIN_INFO, lf);
////
////				mMode = S_MODE_NONE;
////				startActivity(intent);
//
//				//attemptGetBorad();
//				postAttemptGetBorad(100);
//
//			}
//		}
//
//	}





	private boolean mIsOverScrooling = false;





//	private final CFOverScrolledListView.OnOverScrolledListener mOverScrolledListener = new CFOverScrolledListView.OnOverScrolledListener() {
//		@Override
//		public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
//
//			//CFUtil.Log(String.format("%d %d %s %s", scrollX, scrollY, clampedX, clampedY));
//
//			int thored = mListView.getMaxOverScrolledTopY()*50/100;
//			if(scrollY<-thored && !mIsOverScrooling && clampedY){
//				mIsOverScrooling = true;
//				attemptGetBorad();
//			}else if(scrollY>=0){
//				mIsOverScrooling = false;
//			}
//
//		}
//	};




	private Toast mToast;
	private void toast(String str){
		if(mToast==null){
			mToast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
		}else{
			mToast.setText(str);
		}
		mToast.show();
	}
	private void postToast(final String str){
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				toast(str);
			}
		});
	}




}
