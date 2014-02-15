package jp.keitai2013.heallin;

import jp.crudefox.chikara.util.TabListener;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.fragment.ClothesShopFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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



public class ClothesShopActivity extends SherlockFragmentActivity  {


	public static int REQCODE_CONTRIBUTE_SUBMIT = 1003;


	Handler mHandler = new Handler();

	LoginInfo mLoginInfo;


	AppManager mAppManager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);

		setContentView(R.layout.activity_clothes);

		// Set up the action bar.
//		final ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mAppManager = (AppManager) getApplication();


//		Bundle b = getIntent().getExtras();

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


		Tab tab;

		tab = actionBar.newTab();
		tab.setText("服");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		tab.setTabListener(new TabListener<ClothesShopFragment>(
                this, "tag1", ClothesShopFragment.class, android.R.id.content, args));
		actionBar.addTab(tab);



//		tab = actionBar.newTab();
//		tab.setText("帽子");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		tab.setTabListener(new TabListener<ClothesFragment>(
//                this, "tag2", ClothesFragment.class, android.R.id.content, args));
//		actionBar.addTab(tab);
//
//
//
//
//		tab = actionBar.newTab();
//		tab.setText("装飾品");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		tab.setTabListener(new TabListener<ClothesFragment>(
//                this, "tag3", ClothesFragment.class, android.R.id.content, args));
//		actionBar.addTab(tab);





	}






	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		return super.onCreateOptionsMenu(menu);

		MenuItem mi;
		//SubMenu sm;
		int order = 1;

//		SubMenu sm_etc;

//		mi = menu.add(Menu.NONE, Const.MENU_ID_CONTROBUTE,order++,"投稿");
//		mi.setIcon(android.R.drawable.ic_menu_upload);
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

//		sm_etc = menu.addSubMenu(1100,Menu.NONE,order++,"…");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


//		mi = sm_etc.add(Menu.NONE,MENU_ID_PROFIELE, order++,"プロフィール");
//		mi = sm_etc.add(Menu.NONE,MENU_ID_ABOUT, order++,"About");


//		mi = menu.add(Menu.NONE, Const.MENU_ID_PROFILE, order++,"プロフィール");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//
//		mi = menu.add(Menu.NONE, Const.MENU_ID_LOGOUT,order++,"ログアウト");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//
//		mi = menu.add(Menu.NONE, Const.MENU_ID_SETTINGS,order++,"設定");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//
//
//		mi = menu.add(Menu.NONE, Const.MENU_ID_ABOUT,order++,"About");
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);

		int id = item.getItemId();



		return false;
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
