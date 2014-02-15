package jp.keitai2013.heallin.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jp.crudefox.chikara.util.CFOverScrolledScrollView;
import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.AppManager;
import jp.keitai2013.heallin.Const;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.chikara.manager.ProfileManager;
import jp.keitai2013.heallin.chikara.manager.ProfileManager.Profile;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;



/**
 * 		@author Chikara Funabashi
 * 		@first_date 2013/08/09
 *
 */

/**
 * 		プロフィール画面、編集できる
 * 		ちょっとかっこよくしたかったの。
 *
 */

public class ProfileFragment extends SherlockFragment {


	private final int REQCODE_GALLEY = 575;
	private final int REQCODE_CAMERA = 576;



	Context mContext;

	AppManager mApp;

	ProfileManager mProfileManager;

	LoginInfo mLoginInfo;


	private boolean mIsFirst = true;

	Handler mHandler = new Handler();


	CFOverScrolledScrollView mScrollView;
	View mScrollContent;

	View mRootView;


	ImageView mIconImageView;
	ImageView mBgImageView;
	EditText mNameTextView;
	EditText mBirthdayTextView;
	EditText mGenderTextView;
	EditText mIntroductTextView;
	EditText mGoalTextView;

	Bitmap mIconImageViewBitmap;


//	String mProfileName;
//	String mProfileBirthday;
//	String mProfileGender;
//	String mProfileIntroduct;
	Profile mWillSaveProfile;



	GetProfileTask mGetProfileTask;
	SetProfileTask mSetProfileTask;



	private ProgressDialog mProgressDlg;



	public ProfileFragment() {
		super();
		setHasOptionsMenu(true);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);



	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity();

		mApp = (AppManager) getActivity().getApplication();
		mProfileManager = mApp.getProfileManager();


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

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//super.onCreateView(inflater, container, savedInstanceState);

		setContentView(R.layout.fragment_profile);


		float sd = getResources().getDisplayMetrics().density;

		mScrollView = (CFOverScrolledScrollView) findViewById(R.id.scroll_view);
		mScrollView.setMaxOverScrolledY( (int)(150*sd), (int)(150*sd) );


		mScrollContent = findViewById(R.id.scroll_content);
		mBgImageView = (ImageView) findViewById(R.id.profile_bg);
		mIconImageView = (ImageView) findViewById(R.id.profile_icon);
		mNameTextView = (EditText) findViewById(R.id.profile_name);
		mGenderTextView = (EditText) findViewById(R.id.profile_gender);
		mBirthdayTextView = (EditText) findViewById(R.id.profile_birthday);
		mIntroductTextView = (EditText) findViewById(R.id.profile_introduction);
		mGoalTextView = (EditText) findViewById(R.id.profile_goal_message);


		mIconImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showChoosImageDlg();
			}
		});

//		ViewGroup.LayoutParams lp;
//		lp = mScrollContent.getLayoutParams();
//		lp.height = getResources().getDisplayMetrics().heightPixels;
//		mScrollContent.setLayoutParams(lp);

//		mNameTextView.setText(mProfileName);
//		mBirthdayTextView.setText(mProfileBirthday);
//		mGenderTextView.setText(mProfileGender);
//		mIntroductTextView.setText(mProfileIntroduct);

		updateProfiles();


//		mNameTextView.setOnEditorActionListener(new OnProfileEditorActionListener());
//		mBirthdayTextView.setOnEditorActionListener(new OnProfileEditorActionListener());
//		mGenderTextView.setOnEditorActionListener(new OnProfileEditorActionListener());
//		mIntroductTextView.setOnEditorActionListener(new OnProfileEditorActionListener());




		//postAttemptGetProfile(50);

		//AlertDialog.Builder ab = new AlertDialog.Builder(PictureActivity.this);

		return mRootView;

	}


//	private class OnProfileEditorActionListener implements TextView.OnEditorActionListener{
//		@Override
//		public boolean onEditorAction(TextView textView, int id,
//				KeyEvent keyEvent) {
//			if (id == R.id.profile_name) {
//				mProfileName = mNameTextView.getText().toString();
//				attemptSetProfiles();
//				return true;
//			}
//			else if (id == R.id.profile_gender) {
//				mProfileGender = mGenderTextView.toString().toString();
//				attemptSetProfiles();
//				return true;
//			}
//			else if (id == R.id.profile_birthday) {
//				mProfileBirthday = mBirthdayTextView.getText().toString();
//				attemptSetProfiles();
//				return true;
//			}
////			else if (id == R.id.profile_introduction) {
////				mProfileIntroduct = mIntroductTextView.getText().toString();
////				attemptSetProfiles();
////				return true;
////			}
//			else if(id == EditorInfo.IME_NULL){
//				attemptSetProfiles();
//				return true;
//			}
//			return false;
//		}
//	}


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


	@Override
	public void onStart() {
		super.onStart();

		mLoginInfo = mApp.getLoginInfo();

		if(mIsFirst){
			mIsFirst = false;

	        //プログレスダイアログを表示します
			if( attemptGetProfile() ){
				mProgressDlg.show();
			}
		}

	}


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.profile, menu);
//		return true;
//	}

	@Override
	public void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();

		if(mGetProfileTask!=null){
			mGetProfileTask.cancel(true);
			mGetProfileTask = null;
		}
		if(mSetProfileTask!=null){
			mSetProfileTask.cancel(true);
			mSetProfileTask = null;
		}
		mProgressDlg.dismiss();
	}

	@Override
	public void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDestroy();
	}


	private void showChoosImageDlg(){

		AlertDialog.Builder ab = new AlertDialog.Builder(mContext);

		String[] items = new String[]{
			"ファイルから",
			"カメラから"
		};

		ab.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which==0){
					Intent target = new Intent(Intent.ACTION_GET_CONTENT);
					//i_open.setDataAndType(data, "image/*");
					target.setType("image/*");
					try{
						Intent iii = Intent.createChooser(target, "ギャラリー選択");
						startActivityForResult(iii, REQCODE_GALLEY);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(which==1){
					Intent target = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					//target.setType("image/jpg");
					try{
						Intent iii = Intent.createChooser(target, "カメラ選択");
						startActivityForResult(iii, REQCODE_CAMERA);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		AlertDialog dlg = ab.create();
		dlg.show();

	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==REQCODE_GALLEY){
			if(resultCode == Activity.RESULT_OK){
				Bitmap bitmap = null;
				try {
					InputStream is = getActivity().getContentResolver().openInputStream(data.getData());
					bitmap = BitmapFactory.decodeStream(is);
					is.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mIconImageView.setImageBitmap(mIconImageViewBitmap = bitmap);
			}
		}

		else if(requestCode==REQCODE_CAMERA){
			if(resultCode == Activity.RESULT_OK){
				Bitmap bitmap = null;
				Uri uri = data.getData();
				if(uri!=null){
					try {
						InputStream is = getActivity().getContentResolver().openInputStream(uri);
						bitmap = BitmapFactory.decodeStream(is);
						is.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(bitmap==null){
					bitmap = (Bitmap) data.getExtras().get("data");
				}
				mIconImageView.setImageBitmap(mIconImageViewBitmap = bitmap);
			}
		}



	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		MenuItem mi;
		//SubMenu sm;
		int order = 1;

//		SubMenu sm_etc;

		mi = menu.add(Menu.NONE, Const.MENU_ID_SAVE_PROFILES, order++, "保存");
		//mi.setIcon(android.R.drawable.ic_menu_save);
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		mi = menu.add(Menu.NONE, Const.MENU_ID_UPDATE_PROFILES, order++, "更新");
		//mi.setIcon(android.R.drawable.ic_);
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if(id==Const.MENU_ID_UPDATE_PROFILES){
			attemptGetProfile();
			return true;
		}
		else if(id==Const.MENU_ID_SAVE_PROFILES){
			attemptSetProfiles();
			return true;
		}

		return false;
	}






	private void updateProfiles(){

		Profile profile = mProfileManager.getProfile();
		if(profile == null) return ;

		mNameTextView.setText(profile.name!=null?profile.name:"");

		if( profile.birthday==null || profile.birthday.equals("null") ){
			mBirthdayTextView.setText("");
		}else{
			mBirthdayTextView.setText(CFUtil.toDateString(profile.birthday));
		}

		mGenderTextView.setText(profile.gender!=null?profile.gender:"");

		mIntroductTextView.setText(profile.introduction!=null?profile.introduction:"");

		mGoalTextView.setText(profile.goal_message!=null?profile.goal_message:"");


		//mIconImageViewBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
		mIconImageView.setImageBitmap(mIconImageViewBitmap = profile.icon);
		//mIconImageView.setImageBitmap(mIconImageViewBitmap);

	}


//	private void postAttemptGetProfile(long delayed){
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				Activity activity = getActivity();
//				//if(activity==null) return ;
//				if(activity.isFinishing()) return ;
//				attemptGetProfile();
//			}
//		}, delayed);
//	}

	private boolean attemptGetProfile(){
		if(mGetProfileTask!=null){
			return false;
		}

		mGetProfileTask = new GetProfileTask();
		mGetProfileTask.execute((Void)null);

		return true;
	}

	private void attemptSetProfiles(){
		if(mSetProfileTask!=null){
			return ;
		}

		mWillSaveProfile = new Profile();
		mWillSaveProfile.name = mNameTextView.getText().toString();
		mWillSaveProfile.gender = mGenderTextView.getText().toString();

		String str_birthday = mBirthdayTextView.getText().toString();
		if(TextUtils.isEmpty(str_birthday) || str_birthday.equals("null")){
			mWillSaveProfile.birthday = null;
		}else{
			mWillSaveProfile.birthday = CFUtil.parseDate(str_birthday);
		}

		mWillSaveProfile.introduction = mIntroductTextView.getText().toString();
		mWillSaveProfile.goal_message = mGoalTextView.getText().toString();

		mWillSaveProfile.icon = mIconImageViewBitmap;


		mSetProfileTask = new SetProfileTask();
		mSetProfileTask.execute((Void)null);

	}


	/**
	 * doBack, Progress, postExecute
	 */
	private class GetProfileTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			postToast("更新中...");

			//boolean result = mMemberManager._update_mock(mLoginInfo);
			boolean result = mProfileManager.update(mLoginInfo);

			return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetProfileTask = null;
			//showProgress(false);

			mProgressDlg.dismiss();

			if(success){
				toast("プロフィール情報を更新しました。");
				updateProfiles();
			}else{
				toast("プロフィール情報を更新出来ませんでした\n(；´Д｀)");
			}

		}

		@Override
		protected void onCancelled() {
			mGetProfileTask = null;
			//showProgress(false);
		}
	}

	/**
	 * doBack, Progress, postExecute
	 */
	private class SetProfileTask extends AsyncTask<Void, Integer, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			postToast("更新中...");

			Profile pro = mApp.getProfileManager().getProfile();

			int result = 0;
			pro.name = mWillSaveProfile.name;
			pro.gender = mWillSaveProfile.gender;
			pro.birthday = mWillSaveProfile.birthday;
			pro.introduction = mWillSaveProfile.introduction;
			pro.icon = mWillSaveProfile.icon;
			pro.bg = mWillSaveProfile.bg;
			pro.goal_message = mWillSaveProfile.goal_message;

			result += mProfileManager.save(mLoginInfo) ? 1 : 0;

			return result;

		}

		@Override
		protected void onPostExecute(final Integer success) {
			mSetProfileTask = null;
			//showProgress(false);

			if(success>0){
				toast("プロフィール情報を更新しました。");
				//updateProfiles();
			}else{
				toast("プロフィール情報を更新出来ませんでした\n(；´Д｀)");
			}

		}



		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);


		}

		@Override
		protected void onCancelled() {
			mSetProfileTask = null;
			//showProgress(false);
		}
	}





//	@Override
//	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//		// TODO 自動生成されたメソッド・スタブ
//		super.onActivityResult(arg0, arg1, arg2);
//	}


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
