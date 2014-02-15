package jp.keitai2013.heallin.fragment;


import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.AppManager;
import jp.keitai2013.heallin.Const;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.SearchActivity;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.chikara.manager.LoginManager;
import jp.keitai2013.heallin.chikara.manager.MemberManager;
import jp.keitai2013.heallin.chikara.manager.MemberManager.MItem;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;


/**
 * 		@auth Chikara Funabashi
 * 		@first_date 2013/08/08
 *
 */

/**
 * 		メンバー
 * 		ホーム画面内にタブとして配置される
 *		フラグメントの中にフラグメントでややこしいけど頑張るよｗ
 */

public class MemberFragment extends SherlockFragment {




	AppManager mApp;

	boolean mIsFirstStart = true;


	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();

	//private CFOverScrolledListView mListView;

	//private LoginManager mLoginManager;
	//private TimeLineManager mTLManager;

//	private LayoutInflater mLayoutInflater;
//	private DateFormat mDateFormat;


	private GetMemberTask mGetMemberTask;

	//private LoginInfo mLoginInfo;


	//private DeleteContributeTask mDelTask;




	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;


	MemerOfFrendsFragment mMemerOfFrendsFragment;
	MemerOfFrendsFragment mMemerOfFrendsFragment2;
	//SearchFrendsResultFragment mSearchFrendsResultFragment;



	public MemberFragment() {
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


		setContentView(R.layout.fragment_member);



		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);


		return mRootView;

	}





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		mApp = (AppManager) getActivity().getApplication();

		//Bundle bundle = getArguments();

		//Intent intent = getIntent();
		//mLoginInfo = (LoginInfo) bundle.getSerializable(Const.AK_LOGIN_INFO);
		LoginInfo li = mApp.getLoginInfo();

		Bundle args;


		mMemerOfFrendsFragment = new MemerOfFrendsFragment();
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		mMemerOfFrendsFragment.setArguments(args);


		mMemerOfFrendsFragment2 = new MemerOfFrendsFragment();
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		mMemerOfFrendsFragment2.setArguments(args);

//		mSearchFrendsResultFragment = new SearchFrendsResultFragment();
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		mSearchFrendsResultFragment.setArguments(args);



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


//	private void postAttemptGetMember(long delayed){
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				Activity activity = getActivity();
//				if(activity==null) return ;
//				if(activity.isFinishing()) return ;
//				attemptGetMember();
//			}
//		}, delayed);
//	}

	private void attemptGetMember(){
		if(mGetMemberTask!=null){
			return ;
		}

		mGetMemberTask = new GetMemberTask();
		mGetMemberTask.execute((Void)null);

	}


	private void updateListView(){

		mMemerOfFrendsFragment.updateListView();
		mMemerOfFrendsFragment2.updateListView();

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





	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);

		MenuItem mi;
		int order = 20;





//		SearchView sv;
//        SearchManager sm = (SearchManager)mContext.getSystemService(Context.SEARCH_SERVICE);
//        ComponentName c = getComponentName();
//        SearchableInfo si = sm.getSearchableInfo(c);
//        MenuItem mi = menu.findItem(R.id.action_Search);
//
//		/*
//		 * getActionView()について
//		 * menuのxmlにおいて、android:actionViewClassで設定したactionViewを取得する。
//		 * もし、android:actionViewClassが存在しない場合、getActionView()の戻値はnullである。
//		 */
//        sv = (SearchView)mi.getActionView();
//        sv.setSearchableInfo(si);
//
//        /*
//         * setIconifiedByDefault()について
//         * falseは、文字列入力領域が表示される。
//         * trueは、文字列入力領域は表示されずに、虫メガネアイコンだけが表示される。
//         */
//        sv.setIconifiedByDefault(false);

		SearchView sv = new SearchView(getSherlockActivity().getSupportActionBar().getThemedContext());
		//sv.setIconifiedByDefault(true);
		sv.setQueryHint("友達を検索");
		sv.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				if(TextUtils.isEmpty(query)) return false;
				Intent intent = new Intent(getSherlockActivity() ,SearchActivity.class);
				intent.putExtra(Const.AK_SEARCH_Q, query);
				startActivity(intent);
				return true;
			}
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});


		mi = menu.add(Menu.NONE,Const.MENU_ID_SEARCH_MEMBER, order++,"検索");
		//mi = menu.add("検索");
		mi.setIcon(android.R.drawable.ic_menu_search);
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		mi.setActionView(sv);


		mi = menu.add(Menu.NONE,Const.MENU_ID_UPDATE_MEMBER,order++,"メンバ更新");
		mi.setIcon(android.R.drawable.ic_popup_sync);
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);


		super.onCreateOptionsMenu(menu, inflater);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);

		int id = item.getItemId();

		if(id==Const.MENU_ID_UPDATE_MEMBER){
			attemptGetMember();
			return true;
		}
		else if(id==Const.MENU_ID_SEARCH_MEMBER){
			//Intent intent = new Intent(mContext, );

			//return true;
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
		super.onStart();

		if(mIsFirstStart){
			mIsFirstStart = false;
			//attemptGetMember();
		}
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

			MemberManager mm = mApp.getMemberManager();
			LoginInfo li = mApp.getLoginInfo();

			boolean result = mm.update(li);

			return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetMemberTask = null;
			//showProgress(false);

			if(success){
				toast("メンバーを更新しました。");
				updateListView();
			}else{
				toast("メンバーを更新できませんでした。");
			}

		}

		@Override
		protected void onCancelled() {
			mGetMemberTask = null;
			//showProgress(false);
		}
	}







//	private class OnDelBtnClickListener implements View.OnClickListener{
//
//		int mmId;
//
//		public OnDelBtnClickListener(int id) {
//			mmId = id;
//		}
//
//		@Override
//		public void onClick(View v) {
//
//			if( mDelTask!=null ) return ;
//
//			final BItem bitem = mTLManager.getItemById(mmId);
//
//			AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//			ab.setTitle("確認");
//			ab.setMessage("削除しますか。");
//
//			ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					mDelTask = new DeleteContributeTask();
//					mDelTask.execute(bitem.id);
//				}
//			});
//			ab.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//
//				}
//			});
//
//			ab.create().show();
//
//		}
//	}








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

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

//			DummySectionFragment fragment = new DummySectionFragment();
//
//			Bundle args = new Bundle();
//			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//			fragment.setArguments(args);
//
//			return fragment;

			if(position==0){
				return mMemerOfFrendsFragment;
			}
			if(position==1){
				return mMemerOfFrendsFragment2;
			}
//			if(position==2){
//				return mSearchFrendsResultFragment;
//			}

			return null;
//			Fragment fragment = new DummySectionFragment();
//			Bundle args = new Bundle();
//			//args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//			fragment.setArguments(args);
//			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			//Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "友達";
			case 1:
				return "グループ";
			case 2:
				return "検索";
			}
			return null;
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

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_profile_dummy, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}




//	private boolean mIsOverScrooling = false;
//
//
//	private final CFOverScrolledListView.OnOverScrolledListener mOverScrolledListener = new CFOverScrolledListView.OnOverScrolledListener() {
//		@Override
//		public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
//
//			//CFUtil.Log(String.format("%d %d %s %s", scrollX, scrollY, clampedX, clampedY));
//
//			int thored = mListView.getMaxOverScrolledY()*50/100;
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







	
	

	/**
	 * doBack, Progress, postExecute
	 */
	private static class FollowTask extends AsyncTask<Object, Void, Boolean> {
		
		public FollowTask() {
			
		}
		
		AppManager mmApp;
		String mmFriendId;
		boolean mmFollow;
		
		@Override
		protected Boolean doInBackground(Object... params) {
			// TODO: attempt authentication against a network service.

			mmApp = (AppManager)params[0];
			mmFriendId = (String)params[1];
			mmFollow = (Boolean)params[2];

			MemberManager mm = mmApp.getMemberManager();
			LoginManager lm = mmApp.getLoginManager();
			LoginInfo li = mmApp.getLoginInfo();

			boolean result = lm.followFrend(li, mmFriendId, mmFollow);

			return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			//mGetMemberTask = null;
			//showProgress(false);

			if(success){
				//toast("フォローを更新しました。");
				//updateListView();
				CFUtil.Log("succed follow update.");
				if(mmFollow){
					Toast.makeText(mmApp.getApplicationContext(), "友達になりました！\n一緒に理想の体型を目指そう！", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mmApp.getApplicationContext(), "友達解除しました！\nさよなら！", Toast.LENGTH_SHORT).show();
				}
			}else{
				//toast("メンバーを更新できませんでした。");
				CFUtil.Log("failed follow update.");
			}

		}

		@Override
		protected void onCancelled() {
			//mGetMemberTask = null;
			//showProgress(false);
		}
	}


	public static void showFrendsDialog(Context context,final AppManager am,LayoutInflater inf,final MItem mitem){
		if(mitem==null) return ;
		
		//MemberManager memman = am.getMemberManager();

		final AlertDialog[] dlg = new AlertDialog[1];

		AlertDialog.Builder ab = new AlertDialog.Builder(context);

		//ab.setTitle(""+mitem.name);
		//ab.setMessage("id="+mitem.id+" / name="+mitem.name+"");

		View view = inf.inflate(R.layout.frend_simple_dialog, null);

		ImageView icon_icon = (ImageView) view.findViewById(R.id.frend_simple_dlg_icon);
		TextView text_id = (TextView) view.findViewById(R.id.frend_simple_dlg_id);
		TextView text_name = (TextView) view.findViewById(R.id.frend_simple_dlg_name);
		ToggleButton btn_is_frend = (ToggleButton) view.findViewById(R.id.frend_simple_dlg_is_frend);

		icon_icon.setImageBitmap(mitem.icon);
		text_id.setText(""+mitem.id);
		text_name.setText(""+mitem.name);
		btn_is_frend.setChecked(mitem.is_follow);
		

		btn_is_frend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean is_checked = ((ToggleButton)v).isChecked();
				CFUtil.Log("is_checked = "+is_checked);
				
				FollowTask task = new FollowTask();
				task.execute(am, mitem.id, is_checked);

//				dlg[0].dismiss();

//				mMemberManager.removeItemById(mitem.id);
//				toast("サトシ"+"は \n"+
//						mitem.name+"を外に逃がしてあげた！\n"+
//						"バイバイ！"+mitem.name+"！");
//				updateListView();
			}
		});

		ab.setView(view);

		ab.setPositiveButton(android.R.string.ok, null);

		dlg[0] = ab.create();
		dlg[0].show();
	}


}
