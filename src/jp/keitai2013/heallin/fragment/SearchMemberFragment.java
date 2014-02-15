package jp.keitai2013.heallin.fragment;


import java.util.Locale;

import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.Const;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.chikara.manager.LoginManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

public class SearchMemberFragment extends SherlockFragment {

	/*		Auth: Chikara Funabashi
	 * 		Date: 2013/08/08
	 *
	 */

	/*		メンバー
	 * 		ホーム画面内にタブとして配置される
	 *		フラグメントの中にフラグメントでややこしいけど頑張るよｗ
	 */




	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();

	//private CFOverScrolledListView mListView;

	private LoginManager mLoginManager;
	//private TimeLineManager mTLManager;

//	private LayoutInflater mLayoutInflater;
//	private DateFormat mDateFormat;


	private GetMemberTask mGetMemberTask;

	private LoginInfo mLoginInfo;


	//private DeleteContributeTask mDelTask;




	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;


	MemerOfFrendsFragment mMemerOfFrendsFragment;
	MemerOfFrendsFragment mMemerOfFrendsFragment2;


	private RequestQueue mQueue;
    private ImageLoader mImageLoader;


	public SearchMemberFragment() {
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

//		mBasicProfileFragment = new BasicProfileFragment();
//		mIntroductionProfileFragment = new IntroductionProfileFragment();



		//mListView  = (CFOverScrolledListView) findViewById(R.id.timeline_listView);


//		Bundle bundle = getArguments();
//
//		//Intent intent = getIntent();
//		mLoginInfo = (LoginInfo) bundle.getSerializable(Const.AK_LOGIN_INFO);

//		if(CFUtil.isOk_SDK(9)){
//			mListView.setOverscrollHeader(
//					getResources().getDrawable(R.drawable.update_over_scrolled));
//		}


//		mListView.setAdapter(mAdapter);

//		if(mLoginInfo!=null){
//			//CFUtil.Log("length = "+mTLManager.getItemLength());
//			if(mMemberManager.getItemLength()==0){
//				postAttemptGetBorad(250);
//			}
//		}else{
//			toast("ログインに失敗しています。");
//			finish();
//		}

		//mListView.setOnOverScrolledListener(mOverScrolledListener);






		return mRootView;

	}





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		//mLayoutInflater = getLayoutInflater();
		//mDateFormat = DateFormat.getInstance();

		if(mLoginManager==null){
			mLoginManager = new LoginManager(getApplicationContext());
		}
//		if(mTLManager==null){
//			mTLManager = new TimeLineManager(getApplicationContext());
//		}

		Bundle bundle = getArguments();

		//Intent intent = getIntent();
		mLoginInfo = (LoginInfo) bundle.getSerializable(Const.AK_LOGIN_INFO);

		Bundle args;


		mMemerOfFrendsFragment = new MemerOfFrendsFragment();
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, mLoginInfo);
		mMemerOfFrendsFragment.setArguments(args);


		mMemerOfFrendsFragment2 = new MemerOfFrendsFragment();
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, mLoginInfo);
		mMemerOfFrendsFragment2.setArguments(args);

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


	private void postAttemptGetMember(long delayed){
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Activity activity = getActivity();
				if(activity==null) return ;
				if(activity.isFinishing()) return ;
				attemptGetMember();
			}
		}, delayed);
	}

	private void attemptGetMember(){
		if(mGetMemberTask!=null){
			return ;
		}

		mGetMemberTask = new GetMemberTask();
		mGetMemberTask.execute((Void)null);

	}

	private void updateListView(){


		mAdapter.notifyDataSetChanged();

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
		int order = 1;

		mi = menu.add(Menu.NONE,Const.MENU_ID_SEARCH_MEMBER, order++,"検索");
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		mi = menu.add(Menu.NONE,Const.MENU_ID_UPDATE_MEMBER,order++,"メンバ更新");
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

			boolean result = true;//mTLManager._update_mock(mLoginInfo);

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




	private final BaseAdapter mAdapter = new BaseAdapter() {

		public View getView(int position, View convertView, ViewGroup parent) {
			View line = convertView;
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
			return line;

		}

		public long getItemId(int position) {
			return position;
		}

		public Object getItem(int position) {
			return null;
			//return mTLManager.getItemByIndex(position);
		}

		public int getCount() {
			return 0;
			//return mTLManager.getItemLength();
		}
	};






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
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "友達";
			case 1:
				return "グループ";
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




}
