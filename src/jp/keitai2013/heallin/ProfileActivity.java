package jp.keitai2013.heallin;

import jp.crudefox.chikara.util.TabListener;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.fragment.ProfileFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;


/**
 * 		@author Chikara Funabashi
 * 		@first_date 2013/08/09
 *
 */

/**
 * 		プロフィール画面、編集できる
 *
 */



public class ProfileActivity extends SherlockFragmentActivity {



	private AppManager mApp;

	private Handler mHandler = new Handler();

	//private LoginInfo mLoginInfo;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);

		setContentView(R.layout.activity_profile2);

		mApp = (AppManager) getApplication();

		// Set up the action bar.
//		final ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		LoginInfo li = mApp.getLoginInfo();


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
		tab.setText("プロフィール");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		tab.setTabListener(new TabListener<ProfileFragment>(
                this, "tag_profile", ProfileFragment.class, android.R.id.content, args));
		actionBar.addTab(tab);


	}

	@Override
	protected void onStart() {
		super.onStart();


//		Intent intent = getIntent();
//		Bundle bundle = intent.getExtras();
//
//		if(bundle!=null){
//
//			mLoginInfo = (LoginInfo) bundle.getSerializable(Const.AK_LOGIN_INFO);
//
//		}

		//setProfileState(mLoginInfo);

//		mLoginInfo = mApp.getLoginInfo();


	}



//	private void setProfileState(LoginInfo info){
//
//		if(info==null) return ;
//
//		if(mNameEditText!=null){
//			mNameEditText.setText(""+info.getName());
//		}
//
//
//	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.profile, menu);
//		return true;
//	}

	@Override
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
	}







	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(arg0, arg1, arg2);
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
