package jp.keitai2013.heallin;


import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.chikara.manager.ProfileManager;
import jp.keitai2013.heallin.fragment.GraphFragment;
import jp.keitai2013.heallin.fragment.HeallinRoomFragment;
import jp.keitai2013.heallin.fragment.MemberFragment;
import jp.keitai2013.heallin.fragment.SubmitDlgFragment;
import jp.keitai2013.heallin.fragment.TimeLineFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


/**
 * 		@auth Chikara Funabashi
 * 		@date 2013/08/08
 *
 */

/**
 * 		ホーム画面です。
 * 		タブとしてフラグメントでタイムラインやヘルリンなどを包括
 *		みんなはフラグメントとしてコンテンツを作成してね！
 **/


public class HomeActivity extends SherlockFragmentActivity  {


	public static int REQCODE_CONTRIBUTE_SUBMIT = 1003;

	public static final int TAB_TIMELIN = 0;
	public static final int TAB_ROOM = 1;
	public static final int TAB_FRENDS = 2;
	public static final int TAB_GRAPH = 3;


	Handler mHandler = new Handler();

	LoginInfo mLoginInfo;


	AppManager mAppManager;


	TimeLineFragment mTimeLineFragment;
	HeallinRoomFragment mHeallinRoomFragment;
	MemberFragment mMemberFragment;
	GraphFragment mGraphFragment;
	ProfileManager mProfileManager;

	GetDisplayHeallinTask mGetDisplayHeallinTask;

	private boolean mIsFirst = true;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setTheme(R.style.Theme_Sherlock_Light);
		//setTheme(android.R.style.Theme_Holo_Light);

		setContentView(R.layout.activity_home3);

		// Set up the action bar.
//		final ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mAppManager = (AppManager) getApplication();


		//Bundle b = getIntent().getExtras();

		//LoginInfo li = mLoginInfo = (LoginInfo) b.getSerializable(Const.AK_LOGIN_INFO);
		LoginInfo li = mLoginInfo = mAppManager.getLoginInfo();

		if(li==null){
			toast("ログインしていません。");
			finish();
			return ;
		}

		Bundle args;


		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTimeLineFragment = new TimeLineFragment();
        mHeallinRoomFragment = new HeallinRoomFragment();
        mMemberFragment = new MemberFragment();
        mGraphFragment = new GraphFragment();
        mProfileManager = mAppManager.getProfileManager();


		Tab tab;

		tab = actionBar.newTab();
		tab.setText("タイム\nライン");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		tab.setTabListener(new HomeTabListener(
                this, "home_tab1", mTimeLineFragment, android.R.id.content, args));
		actionBar.addTab(tab);

		tab = actionBar.newTab();
		tab.setText("ヘルリン\nの部屋");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		//args.putString(DummySectionFragment.ARG_SECTION_MSG, "へるりんがいるはず！");
		tab.setTabListener(new HomeTabListener(
                this, "home_tab2", mHeallinRoomFragment, android.R.id.content, args));
		actionBar.addTab(tab);

		tab = actionBar.newTab();
		tab.setText("メンバ");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		//args.putString(DummySectionFragment.ARG_SECTION_MSG, "ゆかいな仲間たち");
		tab.setTabListener(new HomeTabListener(
                this, "home_tab3", mMemberFragment, android.R.id.content, args));
		actionBar.addTab(tab);

		tab = actionBar.newTab();
		tab.setText("グラフ");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		args.putString(DummySectionFragment.ARG_SECTION_MSG, "りくのグラフが入るのかなー！");
		tab.setTabListener(new HomeTabListener(
                this, "home_tab4", mGraphFragment, android.R.id.content, args));
		actionBar.addTab(tab);


		selectTab(TAB_TIMELIN);
	}

	public void refreshTimeLine(){
		mTimeLineFragment.refreshTimeLine();
	}


	public void selectTab(int index){
		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
//		Tab tab = null;
//
//		switch (index) {
//		case TAB_TIMELIN: tab = mTimeLineFragment; break;
//		}

		actionBar.selectTab(actionBar.getTabAt(index));
	}



	/**
     * タブの選択時や非選択時の操作を定義する。
     *
     * @param <T>
     */
    public class HomeTabListener implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        //private final Class<T> mClass;
        private final Bundle mArgs;
        private final int mContainerId;

        private boolean mIsTabFirst = true;

        /**
         * コンストラクタ。タブが生成されるときに使用される。
         *
         * @param activity タブのホストのActivity。フラグメントをインスタンス化する時に使用する。
         * @param tag      フラグメントを識別するのに使用するタグ
         * @param clz      フラグメントのクラス。フラグメントをインスタンス化するのに使用する。
         */
        public HomeTabListener(Activity activity, String tag, Fragment f,int container_id, Bundle args) {
            this.mActivity = activity;
            this.mTag = tag;
            //this.mClass = clz;
            this.mFragment = f;
            this.mArgs = args;
            this.mContainerId = container_id;


            mFragment.setArguments(mArgs);
        }

        //--------以下はそれぞれのタブのコールバック --------------

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//            // フラグメントが初期化されているかをチェックする
//            if (mFragment == null) {
//                // 初期化されていない場合はインスタンス化し、Activityに追加する
//                //mFragment = Fragment.instantiate(mActivity, mClass.getName());
//                if(mArgs!=null) mFragment.setArguments(mArgs);
//                ft.add(mContainerId, mFragment, mTag);
//            } else {
//                // すでにある場合は、表示するために設定する
//                //ft.attach(mFragment);
//            	ft.add(mContainerId, mFragment, mTag);
//            }
        	int position = tab.getPosition();
            switch (position) {
                case 0:
                	if( mIsTabFirst ){
                		CFUtil.Log("add");
                		mIsTabFirst = false;
                		ft.add(android.R.id.content, mTimeLineFragment);
                	}else{
                		CFUtil.Log("attach");
                		ft.attach(mTimeLineFragment);
                		//ft.a(android.R.id.content,mTimeLineFragment);
                	}
                    break;
                case 1:
                    //ft.add(android.R.id.content, mHeallinRoomFragment);
                	if(mIsTabFirst){
                		mIsTabFirst = false;
                		ft.add(android.R.id.content, mHeallinRoomFragment);
                	}else{
                		ft.attach(mHeallinRoomFragment);
                		//ft.add(android.R.id.content, mHeallinRoomFragment);
                	}
                    break;
                case 2:
//                	mMemberFragment = new MemberFragment();
//                	mMemberFragment.setArguments(mArgs);
//                    ft.add(android.R.id.content, mMemberFragment);
                	if(mIsTabFirst){
                		mIsTabFirst = false;
                		ft.add(android.R.id.content, mMemberFragment);
                	}else{
                		ft.attach(mMemberFragment);
                		//ft.add(android.R.id.content, mMemberFragment);
                	}
                    break;
                case 3:
                	mGraphFragment = new GraphFragment();
                	mGraphFragment.setArguments(mArgs);
                    ft.add(android.R.id.content, mGraphFragment);
                    break;
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//            if (mFragment != null) {
//                // タブの選択が解除されたときに、表示を解除する。
//                //ft.detach(mFragment);
//                ft.remove(mFragment);
//            }
        	int position = tab.getPosition();
            switch (position) {
        		case 0:
        			ft.detach(mTimeLineFragment);
            		break;
        		case 1:
        			ft.detach(mHeallinRoomFragment);
        			break;
            	case 2:
            		ft.detach(mMemberFragment);
                	break;
                case 3:
                    ft.remove(mGraphFragment);
                    break;
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // すでに選択されているタブを再び選択したときの処理。
            // 通常は使用しない。
        }
    }

//	/**
//     * タブの選択時や非選択時の操作を定義する。
//     *
//     * @param <T>
//     */
//    public class TabListener<T extends Fragment> implements ActionBar.TabListener {
//        private Fragment mFragment;
//        private final Activity mActivity;
//        private final String mTag;
//        private final Class<T> mClass;
//        private final Bundle mArgs;
//        private final int mContainerId;
//
//        /**
//         * コンストラクタ。タブが生成されるときに使用される。
//         *
//         * @param activity タブのホストのActivity。フラグメントをインスタンス化する時に使用する。
//         * @param tag      フラグメントを識別するのに使用するタグ
//         * @param clz      フラグメントのクラス。フラグメントをインスタンス化するのに使用する。
//         */
//        public TabListener(Activity activity, String tag, Class<T> clz,int container_id, Bundle args) {
//            this.mActivity = activity;
//            this.mTag = tag;
//            this.mClass = clz;
//            this.mArgs = args;
//            this.mContainerId = container_id;
//        }
//
//        //--------以下はそれぞれのタブのコールバック --------------
//
//        @Override
//        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//            // フラグメントが初期化されているかをチェックする
//            if (mFragment == null) {
//                // 初期化されていない場合はインスタンス化し、Activityに追加する
//                mFragment = Fragment.instantiate(mActivity, mClass.getName());
//                if(mArgs!=null) mFragment.setArguments(mArgs);
//                ft.add(mContainerId, mFragment, mTag);
//            } else {
//                // すでにある場合は、表示するために設定する
//                //ft.attach(mFragment);
//            	ft.add(mContainerId, mFragment, mTag);
//            }
//        }
//
//        @Override
//        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//            if (mFragment != null) {
//                // タブの選択が解除されたときに、表示を解除する。
//                //ft.detach(mFragment);
//                ft.remove(mFragment);
//            }
//        }
//
//        @Override
//        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//            // すでに選択されているタブを再び選択したときの処理。
//            // 通常は使用しない。
//        }
//    }



    private void attemptHeallinChara(){
    	if(mGetDisplayHeallinTask != null) return ;

    	mGetDisplayHeallinTask = new GetDisplayHeallinTask();
    	mGetDisplayHeallinTask.execute();

    }



    private void cancelGetHeallinCharaTask(){

    	if(mGetDisplayHeallinTask == null) return ;

    	mGetDisplayHeallinTask.cancel(true);

    }


    private void canceTuusin(){
    	cancelGetHeallinCharaTask();
    }


	@Override
	protected void onPause() {
		super.onPause();


	}



	@Override
	protected void onStop() {
		super.onStop();

		canceTuusin();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}



	@Override
	protected void onStart() {
		super.onStart();

		if(mIsFirst){
			mIsFirst = false;

			attemptHeallinChara();
		}

	}


	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		return super.onCreateOptionsMenu(menu);

		MenuItem mi;
		//SubMenu sm;
		int order = 1;

//		SubMenu sm_etc;

		mi = menu.add(Menu.NONE, Const.MENU_ID_CONTROBUTE,order++,"投稿");
		mi.setIcon(android.R.drawable.ic_menu_upload);
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

//		sm_etc = menu.addSubMenu(1100,Menu.NONE,order++,"…");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


//		mi = sm_etc.add(Menu.NONE,MENU_ID_PROFIELE, order++,"プロフィール");
//		mi = sm_etc.add(Menu.NONE,MENU_ID_ABOUT, order++,"About");


		mi = menu.add(Menu.NONE, Const.MENU_ID_PROFILE, order++,"プロフィール");
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		mi = menu.add(Menu.NONE, Const.MENU_ID_LOGOUT,order++,"ログアウト");
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		mi = menu.add(Menu.NONE, Const.MENU_ID_SETTINGS,order++,"設定");
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


		mi = menu.add(Menu.NONE, Const.MENU_ID_ABOUT,order++,"About");
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);

		int id = item.getItemId();

		if(id==Const.MENU_ID_CONTROBUTE){
			showSubmitScreen();
			return true;
		}
		else if(id==Const.MENU_ID_PROFILE){
			Intent intent  = new Intent(HomeActivity.this, ProfileActivity.class);
			startActivity(intent);
			return true;
		}
		else if(id==Const.MENU_ID_ABOUT){

			return true;
		}
		else if(id==Const.MENU_ID_LOGOUT){
			finish();
			return true;
		}
		else if(id==Const.MENU_ID_SETTINGS){
			Intent intent  = new Intent(HomeActivity.this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}

		return false;
	}





	public void showSubmitScreen(){

//		Intent intent  = new Intent(this, ContributionActivity.class);
//		intent.putExtra(Const.AK_LOGIN_INFO, mLoginInfo);
//		startActivityForResult(intent, REQCODE_CONTRIBUTE_SUBMIT);

//		toast("はるひの写真投稿画面がくるよ！");

		mHandler.post(new Runnable() {

			@Override
			public void run() {
				FragmentManager fg = getSupportFragmentManager();
				SubmitDlgFragment dlgf = new SubmitDlgFragment();

				dlgf.show(fg, "submit");
			}
		});




	}



	/**
	 * doBack, Progress, postExecute
	 */
	private class GetDisplayHeallinTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			postToast("更新中...");

			//boolean result = mMemberManager._update_mock(mLoginInfo);
			boolean result = mProfileManager.updateHeallinChara(mLoginInfo);

			return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetDisplayHeallinTask = null;
			//showProgress(false);

			if(success){
				toast("ヘルリンキャラを更新しました。");
				//updateProfiles();
			}else{
				toast("ヘルリンキャラを更新出来ませんでした\n(；´Д｀)");
			}

		}

		@Override
		protected void onCancelled() {
			mGetDisplayHeallinTask = null;
			//showProgress(false);
		}
	}




	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		public static final String ARG_SECTION_MSG = "section_msg";

		private String mmMsg;

		public DummySectionFragment() {

		}
//		public DummySectionFragment(String title) {
//			mmTitle = title;
//		}
		public void setMsg(String msg){
			mmMsg = msg;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home_dummy, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			//dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			if(mmMsg==null) dummyTextView.setText(getArguments().getString(ARG_SECTION_MSG));
			else dummyTextView.setText(mmMsg);

			//dummyTextView.setText(mmMsg);
			return rootView;
		}
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==REQCODE_CONTRIBUTE_SUBMIT){
			if(resultCode==Activity.RESULT_OK){

//				int id = data.getIntExtra(Const.AK_SUBMIT_CONTRIBUTE_ID, -1);
//				LoginInfo lf = (LoginInfo) data.getSerializableExtra(Const.AK_LOGIN_INFO);
//				if(lf!=null){
//					mLoginInfo = lf;
//				}

	//			LoginInfo lf = (LoginInfo) data.getSerializableExtra(Const.AK_LOGIN_INFO);
	//
	//			Intent intent = new Intent(StartActivity.this, BoardActivity.class);
	//			intent.putExtra(Const.AK_LOGIN_INFO, lf);
	//
	//			mMode = S_MODE_NONE;
	//			startActivity(intent);

				//attemptGetBorad();
//				postAttemptGetBorad(100);

			}
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
