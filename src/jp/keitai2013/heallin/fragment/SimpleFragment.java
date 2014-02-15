package jp.keitai2013.heallin.fragment;


import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.Const;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.R.layout;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class SimpleFragment extends SherlockFragment {

	/*		Auth: Chikara Funabashi
	 * 		Date: 2013/08/10
	 *
	 */

	/*
	 *
	 *
	 */




	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();


	private LayoutInflater mLayoutInflater;
	//private DateFormat mDateFormat;

	private GetMemberTask mGetMemberTask;

	private LoginInfo mLoginInfo;


	//private DeleteContributeTask mDelTask;



	public SimpleFragment() {
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


		setContentView(R.layout.fragment_member_frends);

		//mListView  = (CFOverScrolledListView) findViewById(R.id.member_frends_listView);


		Bundle bundle = getArguments();

		//Intent intent = getIntent();
		mLoginInfo = (LoginInfo) bundle.getSerializable(Const.AK_LOGIN_INFO);

//		if(CFUtil.isOk_SDK(9)){
//			mListView.setOverscrollHeader(
//					getResources().getDrawable(R.drawable.update_over_scrolled));
//		}


		//mListView.setAdapter(mAdapter);

		if(mLoginInfo!=null){
			//CFUtil.Log("length = "+mTLManager.getItemLength());
//			if(mMemberManager.getItemLength()==0){
//				postAttemptGetBorad(250);
//			}
		}else{
			toast("ログインに失敗しています。");
			finish();
		}

//		mListView.setOnOverScrolledListener(mOverScrolledListener);
//
//		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//				MItem mitem = mMemberManager.getItemByIndex(position);
//				if(mitem==null) return ;
//				showFrendsDialog(mitem);
//
//			}
//		});

		return mRootView;

	}





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		mLayoutInflater = getLayoutInflater();
		//mDateFormat = DateFormat.getInstance();


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
		if(mGetMemberTask!=null){
			return ;
		}

		mGetMemberTask = new GetMemberTask();
		mGetMemberTask.execute((Void)null);

	}






	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);

		MenuItem mi;
		int order = 1;

//		mi = menu.add(Menu.NONE,MENU_ID_UPDATE,order++,"更新");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		super.onCreateOptionsMenu(menu, inflater);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);

//		int id = item.getItemId();

//		if(id==MENU_ID_UPDATE){
//			attemptGetBorad();
//			return true;
//		}

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
		if(mGetMemberTask!=null){
			mGetMemberTask.cancel(true);
		}
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
	}

	@Override
	public void onStop() {
		super.onStop();
		if(mGetMemberTask!=null){
			mGetMemberTask.cancel(true);
		}
	}




	/**
	 * doBack, Progress, postExecute
	 */
	private class GetMemberTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			postToast("更新中...");

			boolean result = true;//mMemberManager._update_mock(mLoginInfo);

			return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetMemberTask = null;
			//showProgress(false);

//			if(success){
//				toast("メンバー情報を更新しました。");
//				updateListView();
//			}else{
//				toast("メンバー情報を更新出来ませんでした\n(；´Д｀)");
//			}

		}

		@Override
		protected void onCancelled() {
			mGetMemberTask = null;
			//showProgress(false);
		}
	}




//	private final BaseAdapter mAdapter = new BaseAdapter() {
//
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View line = convertView;
//			if(line==null){
//				line =  mLayoutInflater.inflate(R.layout.member_frends_list_item, null);
//			}
//
//			MItem mitem = mMemberManager.getItemByIndex(position);
//
//			//ListData data = mListData.get(position);
//
//			TextView text_name = (TextView) line.findViewById(R.id.member_frends_list_name);
//			TextView text_id = (TextView) line.findViewById(R.id.member_frends_list_id);
//			ImageView icon_icon = (ImageView) line.findViewById(R.id.member_frends_list_icon);
//
//			text_name.setText(mitem.name);
//			text_id.setText(mitem.id);
//
//			icon_icon.setImageBitmap(mitem.icon);
//
//			return line;
//		}
//
//		public long getItemId(int position) {
//			return position;
//		}
//
//		public Object getItem(int position) {
//			return mMemberManager.getItemByIndex(position);
//		}
//
//		public int getCount() {
//			return mMemberManager.getItemLength();
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
