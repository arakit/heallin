package jp.keitai2013.heallin.fragment;


import jp.crudefox.chikara.util.BitmapLruCache;
import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.AppManager;
import jp.keitai2013.heallin.ClothesActivity;
import jp.keitai2013.heallin.ClothesShopActivity;
import jp.keitai2013.heallin.HomeActivity;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.chikara.manager.LoginManager;
import jp.keitai2013.heallin.chikara.manager.ProfileManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;


/**
 * 		@author Chikara Funabashi
 * 		@date 2013/08/10
 *
 */

/*
 *
 *
 */
public class HeallinRoomFragment extends SherlockFragment {






	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();


	private LayoutInflater mLayoutInflater;
	//private DateFormat mDateFormat;

	private GetCommentTask mGetCommentTask;

	private LoginInfo mLoginInfo;

	private LoginManager mLoginManager;
	private AppManager mApp;


	private HeallinView mHeallinView;

	private ViewGroup mHukidasiContainer;
	private TextView mCommentTextView;

	private ProfileManager mProfileManager;


	private View mClothesBtn;
	private View mShopBtn;

	private View mSyokuziBtn;
	private View mTaizyuuBtn;

	private RequestQueue mQueue;
    private ImageLoader mImageLoader;


	//private DeleteContributeTask mDelTask;



	public HeallinRoomFragment() {
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


		setContentView(R.layout.fragment_heallin_room);

		//mListView  = (CFOverScrolledListView) findViewById(R.id.member_frends_listView);


		mCommentTextView = (TextView) findViewById(R.id.heallin_room_comment);
		mHukidasiContainer = (ViewGroup) findViewById(R.id.hukidasi_frame);
		mClothesBtn =  findViewById(R.id.room_clothes_btn);
		mShopBtn =  findViewById(R.id.room_shop_btn);
		mSyokuziBtn =  findViewById(R.id.btn_syokuzi);
		mTaizyuuBtn =  findViewById(R.id.btn_taizyuu);


		ViewGroup heallin_container = (ViewGroup) findViewById(R.id.heallin_room_container);


		//int wc = ViewGroup.LayoutParams.WRAP_CONTENT;
		int mp = ViewGroup.LayoutParams.MATCH_PARENT;

		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mp, mp);
		lp.gravity = Gravity.CENTER;
		mHeallinView = new HeallinView(mContext, null);
		mHeallinView.setLayoutParams(lp);


		heallin_container.addView(mHeallinView);

		mHukidasiContainer.setVisibility(View.GONE);
		comment(null);




		mClothesBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(getActivity(), ClothesActivity.class);
				startActivity(in);
			}
		});
		mShopBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(getActivity(), ClothesShopActivity.class);
				startActivity(in);
			}
		});

		mSyokuziBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!(getActivity() instanceof HomeActivity)) return ;
				HomeActivity home = (HomeActivity) getActivity();
				home.selectTab(HomeActivity.TAB_TIMELIN);
				home.showSubmitScreen();
			}
		});
		mTaizyuuBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!(getActivity() instanceof HomeActivity)) return ;
				HomeActivity home = (HomeActivity) getActivity();
				home.selectTab(HomeActivity.TAB_GRAPH);
			}
		});



		Bundle bundle = getArguments();

		//Intent intent = getIntent();
		//mLoginInfo = (LoginInfo) bundle.getSerializable(Const.AK_LOGIN_INFO);

		mLoginInfo = mApp.getLoginInfo();

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


		mApp = (AppManager) getActivity().getApplication();

		mLoginManager = mApp.getLoginManager();
		mProfileManager = mApp.getProfileManager();


		mQueue = CFUtil.newRequestQueue(getActivity(), 32*1024*1024);
        mImageLoader = new ImageLoader(mQueue, new BitmapLruCache());


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

	private void attemptGetComment(){
		if(mGetCommentTask!=null){
			return ;
		}

		mGetCommentTask = new GetCommentTask();
		mGetCommentTask.execute((Void)null);

	}



	//private Animation mCommentStartAnim;
	private Animation mCommentEndAnim;

	private void comment(String str){
		if(str!=null){
			if(mCommentEndAnim!=null){
				mCommentEndAnim.cancel();
				mCommentEndAnim = null;
			}
			mHukidasiContainer.setVisibility(View.VISIBLE);
			mCommentTextView.setText(str);
			Animation ani = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_bottom);
			mHukidasiContainer.startAnimation(ani);
		}else{
			boolean visivle = mHukidasiContainer.getVisibility() == View.VISIBLE;
			//mHukidasiContainer.setVisibility(View.GONE);
			if(visivle){
				Animation ani = mCommentEndAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_to_bottom);
				ani.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					@Override
					public void onAnimationEnd(Animation animation) {
						mCommentEndAnim = null;
						mHukidasiContainer.setVisibility(View.GONE);
					}
				});
				mHukidasiContainer.startAnimation(ani);
			}
		}
	}





	private class HeallinView extends View{

		public static final int FRAME_TIME = 50;
		public static final float FRAME_SEC_F = FRAME_TIME / 1000.0f;

		Handler mmHandler = new Handler();

		Bitmap mmLoadingChara;
		Bitmap mmChara;

		float mDesnsity;

		boolean mmIsStarted;
		boolean mmIsInitialized = false;

		PointF mmHealPt = new PointF();
		int mmWalk = 0;
		int mmMuki;

		long mmMoonWalk = 0;

		public HeallinView(Context context, AttributeSet attrs) {
			super(context, attrs);

			Resources res = getResources();
			mDesnsity = res.getDisplayMetrics().density;

			mmLoadingChara = ((BitmapDrawable) res.getDrawable(R.drawable.chara_hellin_0)).getBitmap();
			//mmLoadingChara = ((BitmapDrawable) res.getDrawable(android.R.drawable.ic_dialog_alert)).getBitmap();



		}

		public void initHeallin(){
			int width = getWidth();
			int height = getHeight();

			mmHealPt.set(0,height/2);
			mmWalk = 0;
			mmMuki = 1;
			mmMoonWalk =0;

		}

		public void startHeallin(){
			if(mmIsStarted) return ;
			mmIsStarted = true;

			String url = mProfileManager.getProfile().chara_url;
			if(url!=null){
				ImageContainer ic1 = mImageLoader.get(url, new ImageListener() {
		            @Override
		            public void onErrorResponse(VolleyError error) {

		            }
		            @Override
		            public void onResponse(ImageContainer response, boolean isImmediate) {
		                if (response.getBitmap() != null) {
		                	Bitmap bmp = response.getBitmap();
		                	mmChara = bmp;
		                } else {

		                }
		            }
		        }, 280, 280);
			}


//			if(mmChara == null ||  mmLoadingChara == mmChara || mProfileManager.getProfile().chara_bmp!=mmChara){
//				mmChara = mProfileManager.getProfile().chara_bmp;
//			}
			if(mmChara == null){
				mmChara = mmLoadingChara;
			}

			mmHandler.postDelayed(mmDrawRunnable, FRAME_TIME);
		}

		public void stopHeallin(){
			if(!mmIsStarted) return ;
			mmIsStarted = false;

			mmHandler.removeCallbacks(mmDrawRunnable);
		}

		private void tickHeallin(){
			if(!mmIsInitialized) return ;

			int width = getWidth();





			if(mmMoonWalk<=0){
				//ムーンじゃない

				mmWalk = ( mmWalk + (int)(FRAME_SEC_F*1000/0.25f) ) % 1000;

				mmHealPt.x += (int)(( 500*FRAME_SEC_F/10 )*mDesnsity) * mmMuki;

				if(mmHealPt.x>width){
					mmMuki = -1;
				}else if(mmHealPt.x<0){
					mmMuki = +1;
				}

				if(mmMuki<0 && mmHealPt.x<width/2 ){
					if( Math.random()  > 1.0f - ( 0.50 * FRAME_SEC_F) ){
						//mmMoonWalk = 1000;
						mmMuki = 1;
						mmMoonWalk = 1000*2;
						comment("わぁぁぁ。");
					}
				}
			}else{
				//ムーン
				mmWalk = 0;//( mmWalk + (int)(FRAME_SEC_F*1000/1) ) % 1000;
				mmHealPt.x += (int)(( 1000*FRAME_SEC_F/10 )*mDesnsity) * mmMuki;
				mmMoonWalk -= FRAME_TIME;

				if(mmMoonWalk<=0){
					mmMoonWalk = 0;
					mmWalk = 0;
					mmMuki = -1;
					comment(null);
				}
			}


		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			if(!mmIsInitialized){
				mmIsInitialized = true;
				initHeallin();
			}

			int width = getWidth();
			int height = getHeight();
			int cx = width/2;
			int cy = height/2;
			float sd = mDesnsity;

			Bitmap ch = mmChara;
			Rect s_rc = new Rect(0,0,ch.getWidth(),ch.getHeight());
			Rect d_rc = new Rect(0,0,(int)(200*sd),(int)(200*sd));

			long time = SystemClock.uptimeMillis();

			float bb;
			if(mmWalk>=750){
				bb = -1.0f + (mmWalk-750)/250.0f;

			}else if(mmWalk>=500){
				bb = - (mmWalk-500)/250.0f;

			}else if(mmWalk>=250){
				bb = 1.0f - (mmWalk-250)/250.0f;

			}else{
				bb = (mmWalk-0)/250.0f;

			}


			//float aa = mm;//( (time%3000)/3000.0f ) ;

			canvas.save();
			canvas.translate(mmHealPt.x-s_rc.width()/2, mmHealPt.y-s_rc.height()/2);
			//canvas.translate( cx-s_rc.centerX(), cy-s_rc.centerY() );
			canvas.rotate((bb)*10, s_rc.centerX(), s_rc.centerY());

			canvas.drawBitmap(mmChara, s_rc, d_rc, null);

			canvas.restore();

		}


		Runnable mmDrawRunnable = new Runnable() {

			@Override
			public void run() {
				if(!mmIsStarted) return ;
				HeallinView.this.tickHeallin();
				HeallinView.this.invalidate();
				mmHandler.postDelayed(mmDrawRunnable, FRAME_TIME);
			}
		};

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			int action = event.getActionMasked();

			if(action == MotionEvent.ACTION_DOWN){
				//mCommentTextView.setText("ダイエットするリン！");
				attemptGetComment();
			}

			return super.onTouchEvent(event);
		}



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
		cancelTuusin();
		if(mHeallinView!=null){
			mHeallinView.stopHeallin();
		}
	}



	@Override
	public void onPause() {
		super.onPause();
		if(mHeallinView!=null){
			mHeallinView.stopHeallin();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mHeallinView!=null){
			mHeallinView.startHeallin();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		mQueue.start();
	}

	@Override
	public void onStop() {
		super.onStop();

		mQueue.stop();
		cancelTuusin();
	}


	private void cancelTuusin(){
		if(mGetCommentTask!=null){
			mGetCommentTask.cancel(true);
		}
	}



	/**
	 * doBack, Progress, postExecute
	 */
	private class GetCommentTask extends AsyncTask<Void, Void, Boolean> {

		String mmComment;

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			postToast("...");

			//boolean result = true;//mMemberManager._update_mock(mLoginInfo);

			String hc = mLoginManager.HeallinComment(mLoginInfo);

			mmComment = hc;

			return mmComment!=null;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetCommentTask = null;
			//showProgress(false);

			if(success){
				comment(mmComment);
				//toast("メンバー情報を更新しました。");
				//updateListView();
			}else{
				toast("失敗");
				//toast("メンバー情報を更新出来ませんでした\n(；´Д｀)");
			}

		}

		@Override
		protected void onCancelled() {
			mGetCommentTask = null;
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
