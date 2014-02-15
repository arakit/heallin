package jp.keitai2013.heallin.fragment;

import java.text.DateFormat;

import jp.crudefox.chikara.util.CFCardUIAdapter;
import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.AppManager;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.chikara.manager.GraphManager;
import jp.keitai2013.heallin.chikara.manager.GraphManager.GItem;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

public class LogFragment extends SherlockFragment {



	private AppManager mApp;

	private LayoutInflater mLayoutInflater;

	//com.handmark.pulltorefresh.library.PullToRefreshListView

	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();

	//private CFOverScrolledListView mListView;
	private ListView mListView;
	private PullToRefreshListView mPullToRefreshListView;


	private LogListViewAdapter mLogListViewAdapter;


	private DateFormat mDateFormat;

	

	/*
	MyContactDBHelper helper = null;
	SQLiteDatabase dbRead = null;

	//どこでも使える変数
	public static int REQCODE_CONTRIBUTE_SUBMIT = 1003;

	private ListView listView;

	private LoginManager mLoginManager;
	private BoardManager mBoardManager;

	private LayoutInflater mLayoutInflater;
	private DateFormat mDateFormat;

	//private GetBoardTask mGetBoardTask;

	private LoginInfo mLoginInfo;

	//private DeleteContributeTask mDelTask;

	private BaseAdapter baseAdapter;*/


	//private Json json;

	public View onCreateView(LayoutInflater inflater,
							ViewGroup container,
							Bundle saInstanceState){
		//View v = inflater.inflate(R.layout.fragment_log, container, false);

		setContentView(R.layout.fragment_log);






		//mListView  = (CFOverScrolledListView) findViewById(R.id.member_frends_listView);
		mPullToRefreshListView  = (PullToRefreshListView) findViewById(R.id.listView);
		ListView actualListView = mListView = mPullToRefreshListView.getRefreshableView();

//		Bundle bundle = getArguments();

		//Intent intent = getIntent();
		//mLoginInfo = (LoginInfo) bundle.getSerializable(Const.AK_LOGIN_INFO);
		//mLoginInfo = mApp.getLoginInfo();

//		if(CFUtil.isOk_SDK(9)){
//			mListView.setOverscrollHeader(
//					getResources().getDrawable(R.drawable.update_over_scrolled));
//		}


//		mListView.setAdapter(mCAdapter);

//		if(mLoginInfo!=null){
//			//CFUtil.Log("length = "+mTLManager.getItemLength());
//			if(mMemberManager.getItemLength()==0){
//				postAttemptGetBorad(250);
//			}
//		}else{
//			toast("ログインに失敗しています。");
//			finish();
//		}

//		mListView.setOnOverScrolledListener(mOverScrolledListener);

		mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//				MItem mitem = mMemberManager.getItemByIndex(position-1);
//				if(mitem==null) return ;
//				showFrendsDialog(mitem);

			}
		});


		//mListView.setOverscrollHeaderFooter(R.drawable.update_over_scrolled, 0);
//		mListView.setOverscrollHeaderEx(getResources().getDrawable(R.drawable.update_over_scrolled));
//		mListView.setOverscrollFooterEx(null);
//		mListView.setOnPullToRefreshListener(mPullToRefreshListener);


		mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

		// Set a listener to be invoked when the list should be refreshed.
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						refreshView.onRefreshComplete();
					}
				}, 10);


				//getActivity().

				// Do work to refresh the list here.
				//new GetDataTask().execute();
				//attemptGetMember();
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
//		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
//		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
//		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pyo1);
		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.cat5);
		mPullToRefreshListView.setOnPullEventListener(soundListener);

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		//actualListView.setAdapter(mCAdapter);

		mPullToRefreshListView.setAdapter(mLogListViewAdapter);
//
//
//		updateListView();





		return mRootView;
	}




	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		mLayoutInflater = getLayoutInflater();
		//mDateFormat = DateFormat.getInstance();

		mApp = (AppManager) getActivity().getApplication();


		mLogListViewAdapter = new LogListViewAdapter(mContext);

		mDateFormat = DateFormat.getInstance();


	}






	public void updateLog(){


		updateList();

	}

	private void updateList(){

		GraphManager gm = mApp.getGrapthManager();

		LogListViewAdapter ada = mLogListViewAdapter;
		ada.clearItems();

		int num = gm.getItemLength();
		for(int i=0;i<num;i++){
			GItem item = gm.getItemByIndex(i);
			ada.addItem(item, 0);
		}

		ada.notifyDataSetChanged();

		CFUtil.Log("logFragment.updateList"+num);
	}




	private class LogListViewAdapter extends CFCardUIAdapter<GItem>{


		public LogListViewAdapter(Context context) {
			super(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View line = convertView;
			if(line==null){
				line =  mLayoutInflater.inflate(R.layout.log_list_item, null);
			}

			GItem mitem = getItem(position);

			//ListData data = mListData.get(position);

			TextView text1 = (TextView) line.findViewById(R.id.text1);
			TextView text2 = (TextView) line.findViewById(R.id.text2);
			ImageView icon = (ImageView) line.findViewById(R.id.icon);
			
			String datetext = mDateFormat.format(mitem.time);

			text1.setText(datetext);
			text2.setText(""+mitem.value);

			//icon.setImageBitmap(mitem.icon);

			if(isCardMotion(position)){
				doCardMotion(line, R.anim.card_ui_motion_from_right);
				setCardMotion(position, false);
			}

			return line;
		}
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






		//mLayoutInflater = getLayoutInflater();
		//mDateFormat = DateFormat.getInstance();
		//mLoginManager = new LoginManager(getApplicationContext());
		/*mBoardManager = new BoardManager(getActivity().getApplicationContext());

		//Intent intent = getIntent();
		//mLoginInfo = (LoginInfo)intent.getSerializableExtra(Const.AK_LOGIN_INFO);

		baseAdapter = new BoardAdapter();

		log("0000100");
		//String data = getRawText(this, R.raw.coment);
		listView = (ListView)v.findViewById(R.id.listView1);
		log("000020");
		listView.setAdapter(baseAdapter);

		/*if(mLoginInfo != null){
			attemptGetBoard();
		}else{
			toast("ログインに失敗しました");
			//画面を閉じる
			finish();
		}*/
		//("0000300");







	/*@Override
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
		if(mGetBoardTask != null){
			mGetBoardTask .cancel(true);
		}
	}


	 private class GetBoardTask extends AsyncTask<Void, Void, Boolean> {
		 @Override
		 //結果を受け取っている間に動けるようにするのです。
			protected Boolean doInBackground(Void... params) {

			 boolean result = mBoardManager.getByRange(mLoginInfo, 0, Long.MAX_VALUE);

			 return result;
			}
		 @Override
		 //
		 protected void onPostExecute(final Boolean sucess){
			 mGetBoardTask = null;

			 if(sucess){
				 toast("投稿を取得しました");
				 updateListView();
			 }else{
				 toast("投稿を取得できませんでした");
			 }
		 }

		 protected void onCancelled() {
			 mGetBoardTask = null;
		 }
	 }*/



	/*private void attemptGetBoard(){
		if(mGetBoardTask != null){
			return ;
		}
		mGetBoardTask = new GetBoardTask();
		mGetBoardTask.execute((Void)null);
	}

	 //リストビューの更新を促す
	private void updateListView() {
		// TODO 自動生成されたメソッド・スタブ
		baseAdapter.notifyDataSetChanged();
	}

	@Override
	public void onBackPressed(){
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mGetBoardTask != null){
			mGetBoardTask.cancel(true);
		}
	}



	public static String getRawText(Context context,int id){
		  StringBuilder sb = new StringBuilder();
		  BufferedReader r = new  BufferedReader( new InputStreamReader( context.getResources().openRawResource(id) ));

		  try {
			  String line;
			  while((line =r.readLine())!=null){
				  sb.append(line);
				  }
			  } catch (IOException e) {
				  e.printStackTrace();
				  return null;
				  }
		  return sb.toString();
	}


	///baseAdaper///////////////////////////////////////////////////////////////////////////////////////

	private class BoardAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO 自動生成されたメソッド・スタブ
			return mBoardManager.getItemLength();
		}

		@Override
		public Object getItem(int position) {
			// TODO 自動生成されたメソッド・スタブ
			return mBoardManager.getItemByIndex(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO 自動生成されたメソッド・スタブ
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView textView1;
			TextView textView2;

			View v = convertView;

			if(v == null) {
				//LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = mLayoutInflater.inflate(R.layout.log_list, null);
			}
			log("0000400");

			BItem udata = mBoardManager.getItemByIndex(position);
			if(udata != null){

				textView1 = (TextView)v.findViewById(R.id.date);
				textView2 = (TextView)v.findViewById(R.id.weight);
				log("0000500");
			// 表示形式を設定
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");

				textView1.setText(sdf.format(udata.time));
				textView2.setText(udata.body);
				log("0000600");


			}
			return v;
		}
	}


	private class DeleteContributeTask extends AsyncTask<Integer, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Integer... params) {
			// TODO 自動生成されたメソッド・スタブ
			boolean result = mBoardManager.deleteContoribute(mLoginManager, mLoginInfo, params[0]);

			return result;
		}

		@Override
		protected void onPostExecute(final Boolean result){
			mDelTask = null;

			if(result){
				toast("削除しました");
				baseAdapter.notifyDataSetChanged();
			}
		}

		@Override
		protected void onCancelled(){
			mDelTask = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == REQCODE_CONTRIBUTE_SUBMIT){
			if(resultCode == RESULT_OK){

				int id = data.getIntExtra(Const.AK_SUBMIT_CONTRIBUTE_ID,-1);
				LoginInfo lf = (LoginInfo)data.getSerializableExtra(Const.AK_LOGIN_INFO);
				if(lf != null){
					mLoginInfo = lf;
				}

				attemptGetBoard();
			}
		}
	}


	private Toast mToast;
	private void toast(String str) {
		// TODO 自動生成されたメソッド・スタブ
		if(mToast == null ){
			mToast = Toast.makeText(getActivity().getApplicationContext(), str, Toast.LENGTH_SHORT);
		}else{
			mToast.setText(str);
		}
		mToast.show();
	}


	public static void log(String str){
		android.util.Log.d("test", str);
	}

	*/







