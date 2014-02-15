package jp.keitai2013.heallin.fragment;


import jp.crudefox.chikara.util.BitmapLruCache;
import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.AppManager;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.chikara.manager.ProfileManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;


/**
 * 		@author Chikara Funabashi
 * 		@date 2013/08/09
 *
 */

/**
 *
 *
 */

public class GoalSettingsFragment extends SherlockFragment {



	private AppManager mApp;

//com.handmark.pulltorefresh.library.PullToRefreshListView

	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();

	boolean mIsFirst = true;

	private LayoutInflater mLayoutInflater;



	private EditText mEdit_CurValue;
	private EditText mEdit_Unit;
	private EditText mEdit_Term;
	private EditText mEdit_GoalValue;

	private View mBtn_Ok;


	private GetGoalTask mGetGoalTask;
	private SetGoalSettingTask mSetGoalTask;

	//private LoginInfo mLoginInfo;



	private RequestQueue mQueue;
    private ImageLoader mImageLoader;


    ProgressDialog mProgressDlg;



	public GoalSettingsFragment() {
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

		setContentView(R.layout.fragment_goal);

		mEdit_CurValue = (EditText) findViewById(R.id.edit_value);
		mEdit_Unit = (EditText) findViewById(R.id.edit_unit);
		mEdit_Term = (EditText) findViewById(R.id.edit_term);
		mEdit_GoalValue = (EditText) findViewById(R.id.edit_goal);
		mBtn_Ok =  findViewById(R.id.btn_ok);


		//float sd = getResources().getDisplayMetrics().density;

		mBtn_Ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showOkDialog();
			}
		});

		//updateListView();

		return mRootView;

	}


	private void showOkDialog(){


		AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());

		ab.setPositiveButton(android.R.string.ok, new  DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				attemptSetGoal();
			}
		});
		ab.setNegativeButton(android.R.string.cancel, new  DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});


		ab.setTitle("確認");
		ab.setMessage("目標を設定してよろしいですか？");

		ab.create().show();
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		mApp = (AppManager) getActivity().getApplication();

		mLayoutInflater = getLayoutInflater();


		mQueue = CFUtil.newRequestQueue(getActivity(), 32*1024*1024);
        mImageLoader = new ImageLoader(mQueue, new BitmapLruCache());


		mProgressDlg = new ProgressDialog(getActivity());
        //progressDialog.setTitle("タイトル");
        // プログレスダイアログのメッセージを設定します
        mProgressDlg.setMessage("ちょっとだけお待ちください。");
        // プログレスダイアログの確定（false）／不確定（true）を設定します
        mProgressDlg.setIndeterminate(true);
        // プログレスダイアログのスタイルを水平スタイルに設定します
        mProgressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // プログレスダイアログのキャンセルが可能かどうかを設定します
        mProgressDlg.setCancelable(false);
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


//	private void postAttemptGetBorad(long delayed){
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				Activity activity = getActivity();
//				if(activity==null) return ;
//				if(activity.isFinishing()) return ;
//				attemptGetBorad();
//			}
//		}, delayed);
//	}

	private boolean attemptGetGoal(){
		if(mGetGoalTask!=null){
			return false;
		}

		mGetGoalTask = new GetGoalTask();
		mGetGoalTask.execute((Void)null);

		return true;
	}

	private void attemptSetGoal(){
		if(mSetGoalTask!=null){
			return ;
		}


		ProfileManager pm = mApp.getProfileManager();
		ProfileManager.Profile p = pm.getProfile();

		p.goal = Double.parseDouble(mEdit_GoalValue.getText().toString());
		p.goal_term = Double.parseDouble(mEdit_Term.getText().toString());
		p.goal_unit = mEdit_Unit.getText().toString();

		String nowv = mEdit_CurValue.getText().toString();

		mSetGoalTask = new SetGoalSettingTask();
		mSetGoalTask.execute(""+nowv);

	}



	private void updateViewState(){

		ProfileManager pm = mApp.getProfileManager();
		ProfileManager.Profile p = pm.getProfile();

		mEdit_Unit.setText( ""+p.goal_unit );
		mEdit_Term.setText( ""+p.goal_term );
		mEdit_GoalValue.setText( ""+p.goal );

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

//	private static final int MENU_ID_UPDATE = 200;



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);

		MenuItem mi;
		int order = 30;

//		mi = menu.add(Menu.NONE, Const.MENU_ID_UPDATE_MEMBER,order++,"更新");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		super.onCreateOptionsMenu(menu, inflater);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);

//		int id = item.getItemId();

//		if(id==Const.MENU_ID_UPDATE_MEMBER){
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

		if(mIsFirst){
			mIsFirst = false;

			if( attemptGetGoal() ){
				mProgressDlg.show();
			}
		}

		mQueue.start();
	}

	@Override
	public void onStop() {
		super.onStop();

		mProgressDlg.dismiss();

		mQueue.stop();
		cancelTuusin();
	}


	private void cancelTuusin(){
		if(mGetGoalTask!=null){
			mGetGoalTask.cancel(true);
		}
		if(mSetGoalTask!=null){
			mSetGoalTask.cancel(true);
		}
	}


	/**
	 * doBack, Progress, postExecute
	 */
	private class SetGoalSettingTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO: attempt authentication against a network service.

			postToast("更新中...");

			//boolean result = mMemberManager._update_mock(mLoginInfo);
			LoginInfo li = mApp.getLoginInfo();
			ProfileManager pm = mApp.getProfileManager();
			boolean result = pm.writeGoalSettings(li, Double.parseDouble(params[0]));

			return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			//showProgress(false);

			if(success){
				toast("更新しました。");
				finish();
			}else{
				toast("更新出来ませんでした\n(；´Д｀)");
			}

			mSetGoalTask = null;

		}

		@Override
		protected void onCancelled() {
			mSetGoalTask = null;
			//showProgress(false);
		}
	}


	/**
	 * doBack, Progress, postExecute
	 */
	private class GetGoalTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			postToast("更新中...");

			//boolean result = mMemberManager._update_mock(mLoginInfo);
			LoginInfo li = mApp.getLoginInfo();
			ProfileManager pm = mApp.getProfileManager();
			//boolean result = mClothesManager.update(li);

			boolean result = pm.readGoalSettings(li);

			return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			//showProgress(false);

			if(success){
				toast("更新しました。");
				updateViewState();
			}else{
				toast("更新出来ませんでした\n(；´Д｀)");
			}

			mProgressDlg.dismiss();

			mGetGoalTask = null;

		}

		@Override
		protected void onCancelled() {
			mGetGoalTask = null;
			//showProgress(false);
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
