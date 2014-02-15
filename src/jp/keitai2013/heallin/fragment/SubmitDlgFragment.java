package jp.keitai2013.heallin.fragment;

import java.io.FileNotFoundException;
import java.io.InputStream;

import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.AppManager;
import jp.keitai2013.heallin.HomeActivity;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.chikara.manager.LoginManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class SubmitDlgFragment extends SherlockDialogFragment{




	private final static int REQCODE_CAMERA = 2000;
	private final static int REQCODE_GALLEY = 2001;

	private AppManager mAppManager;
	private LoginManager mLoginManager;

	private ImageView mImageView;
	private EditText mEditText;

	private RadioGroup mRadioGroup;
	private final RadioButton[] mRadioButtons = new RadioButton[3];

	private Uri mDataUri;
	private Bitmap mBitmap;

	//private AlertDialog mDlg;

	private PostTask mPostTask;

	private Handler mHandler = new Handler();;

	private ProgressDialog mProgressDlg;



	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCancel(dialog);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);

		mAppManager = (AppManager) getActivity().getApplication();
		mLoginManager = mAppManager.getLoginManager();


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
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private View makeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//super.onCreateView(inflater, container, savedInstanceState);

		//AlertDialog.Builder ab = new AlertDialog.Builder(getSherlockActivity().getSupportActionBar().getThemedContext());
		//AlertDialog.Builder ab = new AlertDialog.Builder(getSherlockActivity());
		//LayoutInflater inflater = getLayoutInflater(savedInstanceState);
		//LayoutInflater inflater = (LayoutInflater) getSherlockActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cv = inflater.inflate(R.layout.fragment_submit, null);
		View cancel = cv.findViewById(R.id.cancel);
		View submit = cv.findViewById(R.id.submit);
		EditText body = (EditText) cv.findViewById(R.id.body);
		ImageView pircture = (ImageView) cv.findViewById(R.id.picture);

		mRadioGroup = (RadioGroup) cv.findViewById(R.id.radio_group);
		mRadioButtons[0] = (RadioButton) cv.findViewById(R.id.radio0);
		mRadioButtons[1] = (RadioButton) cv.findViewById(R.id.radio1);
		mRadioButtons[2] = (RadioButton) cv.findViewById(R.id.radio2);

		mImageView = pircture;
		mEditText = body;

		//ab.setView(cv);

		//final AlertDialog dlg = mDlg = ab.create();

		//dlg.setContentView(cv);

		pircture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showChoosImageDlg();
			}
		});

		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if( !attemptPostPosts() ){
					toast("投稿できません。");
				}
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().cancel();
			}
		});

		return cv;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//Dialog dlg = super.onCreateDialog(savedInstanceState);

		AlertDialog.Builder ab = new AlertDialog.Builder(getSherlockActivity());
		//ab.setView(v);

		LayoutInflater inflater = (LayoutInflater) getSherlockActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//LayoutInflater inflater = getLayoutInflater(savedInstanceState);
		View view = makeView(inflater, null, savedInstanceState);
		view.setBackgroundColor(Color.WHITE);
		ab.setView(view);

		AlertDialog dlg = ab.create();

		//dlg.setContentView(view);

//		dlg.setView(onCreateView(getLayoutInflater(savedInstanceState), null, savedInstanceState));

//		dlg.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//		//フルスクリーンでダイアログを表示します。
//		dlg.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//		WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

		return dlg;
	}




	private class PostTaskData{
		public LoginInfo lf;
		public String title;
		public String summary;
		public InputStream in;
		public String name;
		public int eat_time;
		public Dialog dlg;
	}


	private boolean attemptPostPosts(){

		if(mPostTask!=null) return false;

		LoginInfo lf = mAppManager.getLoginInfo();
		if(lf==null) return false;

		if(mBitmap==null && mDataUri==null) return false;

		int rid = mRadioGroup.getCheckedRadioButtonId();
		if(rid==View.NO_ID) return false;

		PostTaskData data = new PostTaskData();
		data.lf = lf;
		data.title = mEditText.getText().toString();
		data.summary = mEditText.getText().toString();
		data.dlg = getDialog();

		switch(rid){
		case R.id.radio0: data.eat_time = 1; break;
		case R.id.radio1: data.eat_time = 2; break;
		case R.id.radio2: data.eat_time = 3; break;
		default: return false;
		}

		if(mDataUri!=null){
			try {
				data.in = getActivity().getContentResolver().openInputStream(mDataUri);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				toast("画像データが読み込めません。");
				return false;
			}
			data.name = "a.png";//mDataUri.toString();
		}else{
//			Outp
//			mBitmap.compress(format, quality, stream);
//
//			data.in = mBitmap.geti;
//			data.name = "image.png";
			return false;
		}


		mProgressDlg.show();

		mPostTask = new PostTask();
		mPostTask.execute(data);

		return true;
	}



	private void showChoosImageDlg(){

		AlertDialog.Builder ab = new AlertDialog.Builder(getSherlockActivity());

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
					bitmap = CFUtil.createBitmapByMax(getActivity(), mDataUri = data.getData(), 512, 512);
//					InputStream is = getActivity().getContentResolver().openInputStream(mDataUri = data.getData());
//					bitmap = BitmapFactory.decodeStream(is);
//					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				mImageView.setImageBitmap(mBitmap = bitmap);
			}
		}

		else if(requestCode==REQCODE_CAMERA){
			if(resultCode == Activity.RESULT_OK){
				Bitmap bitmap = null;
				Uri uri = mDataUri = data.getData();
				if(uri!=null){
					try {
						bitmap = CFUtil.createBitmapByMax(getActivity(), mDataUri = data.getData(), 512, 512);
//						InputStream is = getActivity().getContentResolver().openInputStream(uri);
//						bitmap = BitmapFactory.decodeStream(is);
//						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(bitmap==null){
					bitmap = (Bitmap) data.getExtras().get("data");
				}
				mImageView.setImageBitmap(mBitmap = bitmap);
			}
		}



	}


	/**
	 * doBack, Progress, postExecute
	 */
	private class PostTask extends AsyncTask<PostTaskData, Void, Boolean> {

		PostTaskData mData;

		@Override
		protected Boolean doInBackground(PostTaskData... params) {
			// TODO: attempt authentication against a network service.

			postToast("投稿中...");

			PostTaskData d = mData = params[0];

			long post_id = mLoginManager.submitPost(
					d.lf, d.title, d.summary, d.in, d.name, d.eat_time
					);

			return post_id!=-1;

		}

		@Override
		protected void onPostExecute(final Boolean success) {

			//showProgress(false);

			mProgressDlg.dismiss();

			if(success){
				toast("投稿しました。");
				mData.dlg.dismiss();

				if( getActivity() instanceof HomeActivity){
					HomeActivity ha = (HomeActivity) getActivity();
					ha.selectTab(HomeActivity.TAB_TIMELIN);
					ha.refreshTimeLine();
				}

			}else{
				toast("投稿できませんでした。");
			}

			mPostTask = null;

		}

		@Override
		protected void onCancelled() {
			mPostTask = null;
			//showProgress(false);
		}
	}








	private Toast mToast;
	private void toast(String str){
		if(mToast==null){
			mToast = Toast.makeText(getActivity().getApplicationContext(), str, Toast.LENGTH_SHORT);
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
