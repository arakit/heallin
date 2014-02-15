package jp.keitai2013.heallin.chikara.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.FileManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

public class PictureManager {


	public interface OnUpdateImageListner{
		public void onUpdateImage(PictureManager pm,String url);
	}


	private final Handler mHandler;

	private final HashMap<String, Bitmap> mBitmaps = new HashMap<String, Bitmap>();

	private final Queue<Runnable> mTaskQueue = new ConcurrentLinkedQueue<Runnable>();
	private final List<Runnable> mExecuteTaskList = Collections.synchronizedList(new ArrayList<Runnable>());

	private final HashSet<OnUpdateImageListner> mOnUpdateImageListners = new HashSet<OnUpdateImageListner>();

	public PictureManager(){
		mHandler = new Handler();
	}

	public void addOnUpdateImageListener(OnUpdateImageListner lis){
		mOnUpdateImageListners.add(lis);
	}
	public void removeOnUpdateImageListener(OnUpdateImageListner lis){
		mOnUpdateImageListners.remove(lis);
	}

	public void putBitmap(String url, Bitmap bmp){
		CFUtil.Log("putBitmap "+url+" , "+bmp);
		mBitmaps.put(url, bmp);
	}

	public Bitmap getBitmap(String url){
		CFUtil.Log("getBitmap "+url);
		return mBitmaps.get(url);
	}

	public boolean containsBitmapKey(String url){
		return mBitmaps.containsKey(url);
	}



	public static Bitmap downloadImageToBmp(String url){
		return downloadImageToBmp(url, false);
	}

	public static Bitmap downloadImageToBmp(String url, boolean force_update){
		String name = Uri.encode(url);
		File dir = FileManager.getImageDataDir();
		CFUtil.Log("dir="+dir+" ");
		if(dir==null) return null;
		Bitmap bmp;
		File file = new File(dir, name);
		final int con_time = 1000;
		final int so_time = 3000;
		final int max_image_size = 380;
		if(!file.exists() || force_update){
			boolean suc = CFUtil.getDataToFile(url, con_time, so_time, file);
			if(!suc) return null;
			bmp = CFUtil.createBitmapByMax(file, max_image_size, max_image_size);
		}else{
			bmp = CFUtil.createBitmapByMax(file, max_image_size, max_image_size);
			if(bmp==null){
				boolean suc = CFUtil.getDataToFile(url, con_time, so_time, file);
				if(!suc) return null;
				bmp = CFUtil.createBitmapByMax(file, max_image_size, max_image_size);
			}
		}
		return bmp;
	}

//	public void requestDownloadBitmapForDefault(String url){
//		if(url==null) return ;
//		this.requestDownloadBtmap(url, new GetImageTaskListener() {
//			@Override
//			public Bitmap onGetTask(PictureManager pm, String url) {
//				CFUtil.Log("onGetTask "+url);
//				Bitmap bmp = CFUtil.getImage(url, 500, 500);
//				return bmp;
//			}
//
//			@Override
//			public void onUpdateImage(PictureManager pm, String url) {
//
//			}
//		});
//	}

	public void requestDownloadBtmap(final String url,final GetImageTaskListener lis){

//		if(PictureManager.this.containsBitmapKey(url)){
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
//					lis.onUpdateImage(PictureManager.this, url);
//				}
//			});
//			return ;
//		}

		final GetImageTaskInfo info = new GetImageTaskInfo();
		final GetImageTask task = new GetImageTask();

		final Runnable run = new Runnable() {
			@Override
			public void run() {
				task.execute(info);
			}
		};

		info.thisRun = run;
		info.listener = lis;
		info.url = url;

//		if(!PictureManager.this.containsBitmapKey(url)){
//			PictureManager.this.putBitmap(url, null);
//		}

		offerQueueTask(run);

	}


	private void offerQueueTask(Runnable run){

		mTaskQueue.offer(run);
		nextQueueTask();
	}


	private void nextQueueTask(){

		if(mExecuteTaskList.size()>2) return ;

		int num = 1;
		for(int i=0;i<num;i++){
			Runnable run =  mTaskQueue.poll();
			if(run!=null){
				mExecuteTaskList.add(run);
				run.run();
			}
		}
	}

	private void endQueueTask(Runnable run){

		mExecuteTaskList.remove(run);
		nextQueueTask();

	}

	private void onUpdateImage(String url){
		for(OnUpdateImageListner lis : mOnUpdateImageListners){
			lis.onUpdateImage(this, url);
		}
	}

//	private final Runnable mTaskRooper = new Runnable() {
//		@Override
//		public void run() {
//
//		}
//	};


//	private class GetImageThread extends Thread{
//
//
//
//	}




	private class GetImageTaskInfo{
		String url;
		GetImageTaskListener listener;
		Runnable thisRun;
	}

	public interface GetImageTaskListener{
		public Bitmap onGetTask(PictureManager pm, String url);
		public void onUpdateImage(PictureManager pm, String url);
	}

	/**
	 * doBack, Progress, postExecute
	 */
	private class GetImageTask extends AsyncTask<GetImageTaskInfo, Void, Boolean> {

		GetImageTaskInfo mmInfo;
		Bitmap mmBmp;

		@Override
		protected Boolean doInBackground(GetImageTaskInfo... params) {

			mmInfo = params[0];

			mmBmp  = mmInfo.listener.onGetTask(PictureManager.this, mmInfo.url);

			return true;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
//			mGetBoradTask = null;
			//showProgress(false);

//			if(success){
//				toast("投稿を取得しました。\nヽ(=´▽`=)ﾉ");
//				updateListView();
//			}else{
//				toast("投稿を取得出来ませんでした。\n(´・ω・`)");
//			}
//
//			mPullToRefreshListView.onRefreshComplete();

			putBitmap(mmInfo.url, mmBmp);

			endQueueTask(mmInfo.thisRun);

//			mHandler.post(new Runnable() {
//
//				@Override
//				public void run() {
//
//				}
//			});

			mmInfo.listener.onUpdateImage(PictureManager.this, mmInfo.url);

			onUpdateImage(mmInfo.url);
		}

		@Override
		protected void onCancelled() {
//			mGetBoradTask = null;
			//showProgress(false);
		}
	}

}
