package jp.keitai2013.heallin;

import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.fragment.TimeLineFragment;
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

public class HomeActivity_B0 extends SherlockFragmentActivity  {

	/*		Auth: Chikara Funabashi
	 * 		Date: 2013/08/08
	 *
	 */


//	SectionsPagerAdapter mSectionsPagerAdapter;

	//ViewPager mViewPager;

	Handler mHandler = new Handler();

	//FragmentTabHost mTabHost;
	//TabHost mTabHost;
//	CFFragmentTabHost2 mTabHost;
//	TabWidget mTabs;


//	TimeLineFragment mTimeLineFragment;
//	DummySectionFragment mHeallinFragment;
//	DummySectionFragment mGraphFragment;
//	DummySectionFragment mMemberFragment;
//
//	Fragment[] mFragments = new Fragment[4];


	//ActionBar mActionBar;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);

		setContentView(R.layout.activity_home3);

		// Set up the action bar.
//		final ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


		//mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		Bundle b = getIntent().getExtras();

		LoginInfo li = (LoginInfo) b.getSerializable(Const.AK_LOGIN_INFO);

		if(li==null){
			toast("ログインしていません。");
			finish();
			return ;
		}



		//mViewPager = (ViewPager) findViewById(R.id.pager);
		//mTabHost = (CFFragmentTabHost2) findViewById(android.R.id.tabhost);
		//mTabs = (TabWidget) findViewById(android.R.id.tabs);

		//mTabHost.setup(HomeActivity.this, getSupportFragmentManager(), android.R.id.content);

		//mTabHost.setup();

//		mTabHost.setup();
//		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//			@Override
//			public void onTabChanged(String tabId) {
//				//mTabHost.findViewWithTag(tabId);
//			}
//		});

		//mTabHost.setup(this, getSupportFragmentManager(), android.R.id.content);


		//mViewPager.setAdapter(mSectionsPagerAdapter);


//		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//			@Override
//			public void onPageSelected(int position) {
//				//actionBar.setSelectedNavigationItem(position);
//			}
//		});

//		// For each of the sections in the app, add a tab to the action bar.
//		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
//			// Create a tab with text corresponding to the page title defined by
//			// the adapter. Also specify this Activity object, which implements
//			// the TabListener interface, as the callback (listener) for when
//			// this tab is selected.
//			actionBar.addTab(
//					actionBar.newTab()
//							.setText(mSectionsPagerAdapter.getPageTitle(i))
//							.setTabListener(this));
//		}

		//mActionBar = getSupportActionBar();


//		mTimeLineFragment = new TimeLineFragment();
//
//		mHeallinFragment = new DummySectionFragment();
//		mHeallinFragment.setMsg("ヘルリンの部屋だよー");
//
//		mMemberFragment = new DummySectionFragment();
//		mMemberFragment.setMsg("友達を表示する予定！");
//
//		mGraphFragment = new DummySectionFragment();
//		mGraphFragment.setMsg("りくのグラフがここに入るよ！");
//
//		mFragments[0] = mTimeLineFragment;
//		mFragments[1] = mHeallinFragment;
//		mFragments[2] = mMemberFragment;
//		mFragments[3] = mGraphFragment;


//		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
//
//			String title = "" + mSectionsPagerAdapter.getPageTitle(i);
//
//			TabSpec spec = mTabHost.newTabSpec("tab"+(i+1));
//			spec.setIndicator(title);
//
//			Bundle bundle = getIntent().getExtras();
//			//bundle.putSerializable(Const.AK_LOGIN_INFO, m);
//
//			mTabHost.addTab(spec, Fragment.class, bundle);
//
//		}

//		TabSpec spec;
		Bundle args;


		ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


		Tab tab;

		tab = actionBar.newTab();
		tab.setText("タイム\nライン");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		tab.setTabListener(new TabListener<TimeLineFragment>(
                this, "tag1", TimeLineFragment.class, android.R.id.content, args));
		actionBar.addTab(tab);

		tab = actionBar.newTab();
		tab.setText("ヘルリン\nの部屋");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		tab.setTabListener(new TabListener<DummySectionFragment>(
                this, "tag2", DummySectionFragment.class, android.R.id.content, args));
		actionBar.addTab(tab);

		tab = actionBar.newTab();
		tab.setText("メンバ");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		tab.setTabListener(new TabListener<DummySectionFragment>(
                this, "tag3", DummySectionFragment.class, android.R.id.content, args));
		actionBar.addTab(tab);

		tab = actionBar.newTab();
		tab.setText("グラフ");
		args = new Bundle();
		args.putSerializable(Const.AK_LOGIN_INFO, li);
		tab.setTabListener(new TabListener<DummySectionFragment>(
                this, "tag4", DummySectionFragment.class, android.R.id.content, args));
		actionBar.addTab(tab);



        //        actionBar.addTab(actionBar.newTab()
//                   .setText("ページ２")
//                   .setTabListener(new TabListener<Tab2Fragment>(
//                              this, "tag2", Tab2Fragment.class)));



//		//タイムライン
//		spec = mTabHost.newTabSpec("tab0");
//		spec.setIndicator("タイムライン");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		mTabHost.addTab(spec, TimeLineFragment.class, args);
//
//		//ヘルリン
//		spec = mTabHost.newTabSpec("tab1");
//		spec.setIndicator("ヘルリン");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		args.putString(DummySectionFragment.ARG_SECTION_MSG, "ヘルリンがいるはず！");
//		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, 1);
//		mTabHost.addTab(spec, DummySectionFragment.class, args);
//
//		//メンバー
//		spec = mTabHost.newTabSpec("tab2");
//		spec.setIndicator("メンバ");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		args.putString(DummySectionFragment.ARG_SECTION_MSG, "愉快な友達いっぱい");
//		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, 2);
//		mTabHost.addTab(spec, DummySectionFragment.class, args);

		//グラフ
//		spec = mTabHost.newTabSpec("tab3");
//		spec.setIndicator("グラフ");
//		args = new Bundle();
//		args.putSerializable(Const.AK_LOGIN_INFO, li);
//		args.putString(DummySectionFragment.ARG_SECTION_MSG, "りくのグラフがここに入る！");
//		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, 3);
//		mTabHost.addTab(spec, DummySectionFragment.class, args);
//
//		mTabHost.setCurrentTab(0);


		//bundle.putSerializable(Const.AK_LOGIN_INFO, m);

		//mTabHost.addTab(spec, Fragment.class, bundle);




	}


//	final ActionBar.TabListener mTabListener = new ActionBar.TabListener() {
//
//		@Override
//		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//			// TODO 自動生成されたメソッド・スタブ
//
//		}
//
//		@Override
//		public void onTabSelected(Tab tab, FragmentTransaction ft) {
//			// TODO 自動生成されたメソッド・スタブ
//
//		}
//
//		@Override
//		public void onTabReselected(Tab tab, FragmentTransaction ft) {
//			// TODO 自動生成されたメソッド・スタブ
//
//		}
//	};


	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {

		private Fragment mFragment;
	    private final SherlockFragmentActivity mActivity;
	    private final String mTag;
	    private final Class<T> mClass;
	    private int mContainerId;
	    private Bundle mArgs;

		    /**
		     * コンストラクタ
		     * @param activity
		     * @param tag
		     * @param clz
		     */
		    public TabListener(SherlockFragmentActivity activity, String tag, Class<T> clz, int container_id,Bundle args) {
		        mActivity = activity;
		        mTag = tag;
		        mClass = clz;
		        mContainerId = container_id;
		        //FragmentManagerからFragmentを探す。  2012/12/11 追記
		        mFragment = mActivity.getSupportFragmentManager().findFragmentByTag(mTag);
		        mArgs = args;
		        if(mFragment!=null && mArgs!=null) mFragment.setArguments(mArgs);
		    }

		    /**
		     * @brief 　タブが選択されたときの処理
		     */
		    public void onTabSelected(Tab tab, FragmentTransaction ft) {
		        //ftはnullではないが使用するとNullPointExceptionで落ちる
		        if (mFragment == null) {
		            mFragment = Fragment.instantiate(mActivity, mClass.getName());
		            if(mFragment!=null && mArgs!=null) mFragment.setArguments(mArgs);
		            FragmentManager fm = mActivity.getSupportFragmentManager();
		            fm.beginTransaction().add(mContainerId, mFragment, mTag).commit();
		        } else {
		            //detachされていないときだけattachするよう変更   2012/12/11　変更
		            //FragmentManager fm = mActivity.getSupportFragmentManager();
		            //fm.beginTransaction().attach(mFragment).commit();
		            if (mFragment.isDetached()) {
		                FragmentManager fm = mActivity.getSupportFragmentManager();
		                fm.beginTransaction().attach(mFragment).commit();
		             }
		        }
		    }
		    /**
		     * @brief 　タブの選択が解除されたときの処理
		     */
		    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		        //ftはnullではないが使用するとNullPointExceptionで落ちる
		        if (mFragment != null) {
		            FragmentManager fm = mActivity.getSupportFragmentManager();
		            fm.beginTransaction().detach(mFragment).commit();
		       }
		    }
		    /**
		     * @brief　タブが2度目以降に選択されたときの処理
		     */
		    public void onTabReselected(Tab tab, FragmentTransaction ft) {
		    }
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.home, menu);
//		return true;
//	}

//	@Override
//	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//		// When the given tab is selected, switch to the corresponding page in
//		// the ViewPager.
//		mViewPager.setCurrentItem(tab.getPosition());
//	}
//
//	@Override
//	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//	}
//
//	@Override
//	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//	}

//	/**
//	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//	 * one of the sections/tabs/pages.
//	 */
//	public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//		public SectionsPagerAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public android.support.v4.app.Fragment getItem(int position) {
//			// getItem is called to instantiate the fragment for the given page.
//			// Return a DummySectionFragment (defined as a static inner class
//			// below) with the page number as its lone argument.
//
//
//			return mFragments[position];
//
////			Fragment fragment = new DummySectionFragment();
////			Bundle args = new Bundle();
////			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
////			//args.putString(DummySectionFragment.ARG_SECTION_MSG, ""+getPageTitle(position));
////			fragment.setArguments(args);
////			return fragment;
//		}
//
//		@Override
//		public int getCount() {
//			// Show 4 total pages.
//			return 4;
//		}
//
//		@Override
//		public CharSequence getPageTitle(int position) {
//			Locale l = Locale.getDefault();
//			switch (position) {
//			case 0:
//				return "ホーム";
//			case 1:
//				return "ヘルリンの部屋";
//			case 2:
//				return "メンバ";
//			case 3:
//				return "グラフ";
////			case 2:
////				return getString(R.string.title_section3).toUpperCase(l);
//			}
//			return null;
//		}
//	}

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
