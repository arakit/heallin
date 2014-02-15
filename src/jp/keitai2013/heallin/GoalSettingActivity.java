package jp.keitai2013.heallin;

import jp.crudefox.chikara.util.TabListener;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.fragment.GoalSettingsFragment;
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



public class GoalSettingActivity extends SherlockFragmentActivity  {


	public static int REQCODE_CONTRIBUTE_SUBMIT = 1003;


	Handler mHandler = new Handler();

	LoginInfo mLoginInfo;


	AppManager mAppManager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);

		setContentView(R.layout.activity_goal_setting);

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
		tab.setText("目標設定");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		tab.setTabListener(new TabListener<GoalSettingsFragment>(
                this, "tag1", GoalSettingsFragment.class, R.id.content, args));
		actionBar.addTab(tab);








	}






	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		return super.onCreateOptionsMenu(menu);


		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);




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
