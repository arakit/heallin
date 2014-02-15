package jp.keitai2013.heallin.fragment;

import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.AppManager;
import jp.keitai2013.heallin.Const;
import jp.keitai2013.heallin.GoalSettingActivity;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.chikara.manager.GraphManager;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


/**
 *
 * @author riku ito, chikara funabashi
 *
 **/

/**
 *		11月11日、グラフ部分受け取りました。
 *		結合作業に入ります。by chikara.
 *
 **/

public class GraphFragment extends SherlockFragment {





	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();


	private LayoutInflater mLayoutInflater;

	private GraphManager mGraphManager;


	private LoginInfo mLoginInfo;
	private AppManager mApp;


	private boolean mIsFirst = true;

	private GetGraphTask mGetGraphTask;
	private PostRecordTask mPostRecordTask;


	private ViewGroup mGraphViewFragmentContainer;
	private ViewGroup mLogFragmentContainer;

	private View mLogBtn;


	private GraphViewFragment mGraphViewFragment;
	private LogFragment mLogFragment;



	//private GraphManager mGraphManager;


//	private XYMultipleSeriesDataset dataset;
//	private GraphicalView graphicalView;



	public GraphFragment() {
		super();
		setHasOptionsMenu(true);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//super.onCreateView(inflater, container, savedInstanceState);

		CFUtil.Log("onCreateView "+this);


		setContentView(R.layout.fragment_graph);

		//mListView  = (CFOverScrolledListView) findViewById(R.id.member_frends_listView);


		mGraphViewFragmentContainer = (ViewGroup) findViewById(R.id.container_fragment_graph);
		mLogFragmentContainer = (ViewGroup) findViewById(R.id.container_fragment_log);
		mLogBtn = findViewById(R.id.btn_log);


		mLogBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showInputDlg();
			}
		});
//
//		mLogFragment = (LogFragment) fm.findFragmentById( R.id.fragment_log );
//		mGraphViewFragment = (GraphViewFragment) fm.findFragmentById( R.id.fra );


		//Bundle bundle = getArguments();

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



		return mRootView;

	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity();

		mLayoutInflater = getLayoutInflater();
		//mDateFormat = DateFormat.getInstance();

		mApp = (AppManager)getActivity().getApplication();

		mGraphManager = mApp.getGrapthManager();

		mGraphViewFragment = new GraphViewFragment();
		mLogFragment = new LogFragment();

 	}

	public void attemptGetGrapgh(){
		if(mGetGraphTask!=null){
			return ;
		}

		mGetGraphTask = new GetGraphTask();
		mGetGraphTask.execute((Void)null);

	}
	public void attemptPostRecord(double val){
		if(mPostRecordTask!=null){
			return ;
		}

		mPostRecordTask = new PostRecordTask();
		mPostRecordTask.execute(Double.valueOf(val));

	}




		/*String[] titles = new String[] {"Blue", "Green"};
		ArrayList<double[]> x = new ArrayList<double[]>();
		for(int i = 0; i < titles.length; i++){
			x.add(new double[] {1, 2, 3, 4,5});

		}
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[] { 1, 2, 3, 4, 60});
        values.add(new double[] { 18, 17, 16, 15, -14 });
        int[] colors = new int[] { Color.BLUE, Color.GREEN };
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        setChartSettings(renderer, "Average temperature", "Horizontal axis",
                "Vertical axis", 0.5, 12.5, -10, 40, Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Align.RIGHT);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setZoomButtonsVisible(true);
        renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
        renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });

        dataset = buildDataset(titles, x, values);

        graphicalView = ChartFactory.getLineChartView(
                getApplicationContext(), dataset, renderer);

        setContentView(graphicalView);
	}




    private XYMultipleSeriesDataset buildDataset(String[] titles,
            List<double[]> xValues, List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
            List<double[]> xValues, List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }

    private XYMultipleSeriesRenderer buildRenderer(int[] colors,
            PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    private void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
            PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[] { 20, 30, 15, 20 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }

    private void setChartSettings(XYMultipleSeriesRenderer renderer,
            String title, String xTitle, String yTitle, double xMin,
            double xMax, double yMin, double yMax, int axesColor,
            int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }*/


    private void showInputDlg(){

		  LayoutInflater inflater //XMLで作った画面を呼び出してインスタンスに
		    = LayoutInflater.from(getActivity());
		  View view = inflater.inflate(R.layout.dialog_input_graph, null);
		  final EditText y = (EditText)view.findViewById(R.id.value);

		  new AlertDialog.Builder(getActivity())
		  .setTitle("現在の値を入力")
		  //.setIcon(R.drawable.icon)
		  .setView(view)
		  .setPositiveButton(
		    "更新",
		    new DialogInterface.OnClickListener() {

		    	@Override
		    	public void onClick(DialogInterface dialog, int which) {

		    		String str = y.getText().toString();
		    		double yv;
		    		if(str!=null && str.length()!=0){
		    			yv = Double.parseDouble(str);
		    		}else{
		    			yv = 0;
		    		}



//				    //double y = 0;
//				    try {
//				    	//エディットテキストの文字をdouble型に変換してリストに入れる
//				    	mYValue.add(Double.parseDouble(mY.getText().toString()));
//
//				    	} catch (NumberFormatException e) {
//				    		mY.requestFocus();
//				    		return;
//				    }
//				    mXValue.add(++mI);
//
//				    updateGrapgh();

				    //入力された値を元にグラフを再描画すればいいはず
		    		attemptPostRecord(yv);

		    	}
		  })
		  .show();
    }


	private void updateListView(){




		if(mGraphViewFragment!=null){
			mGraphViewFragment.updateGraph();
		}

		if(mLogFragment!=null){
			mLogFragment.updateLog();
		}

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


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);

		MenuItem mi;
		int order = 20;

		mi = menu.add(Menu.NONE,Const.MENU_ID_GRAPH_GOAL_SETTING,order++,"目標設定");
		//mi.setIcon(android.R.drawable.ic_popup_sync);
		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);


		super.onCreateOptionsMenu(menu, inflater);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);

		int id = item.getItemId();

		if(id==Const.MENU_ID_GRAPH_GOAL_SETTING){
			Intent intent = new Intent(getActivity(), GoalSettingActivity.class);
			startActivity(intent);
			return true;
		}

		return false;
	}



	/**
	 * doBack, Progress, postExecute
	 */
	private class GetGraphTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			postToast("グラフ更新中...");


			//MemberManager mm = mApp.getMemberManager();
			LoginInfo li = mApp.getLoginInfo();

			GraphManager gm = mGraphManager;

			boolean result = gm.update(li);

			return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetGraphTask = null;
			//showProgress(false);

			if(success){
				toast("グラフデータを更新しました。");
				updateListView();
			}else{
				toast("グラフデータを更新できませんでした。");
			}

		}

		@Override
		protected void onCancelled() {
			mGetGraphTask = null;
			//showProgress(false);
		}
	}


	/**
	 * doBack, Progress, postExecute
	 */
	private class PostRecordTask extends AsyncTask<Double, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Double... params) {
			// TODO: attempt authentication against a network service.

			postToast("記録中...");


			//MemberManager mm = mApp.getMemberManager();
			LoginInfo li = mApp.getLoginInfo();

			GraphManager gm = mGraphManager;

			boolean result = gm.postRecord(li, params[0]);

			return result;

		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetGraphTask = null;
			//showProgress(false);

			if(success){
				toast("記録しました。");
				//updateListView();
				attemptGetGrapgh();
			}else{
				toast("記録できませんでした。");
			}

		}

		@Override
		protected void onCancelled() {
			mGetGraphTask = null;
			//showProgress(false);
		}
	}















	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();

		FragmentManager fm = getChildFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
	    ft.replace(R.id.container_fragment_graph, mGraphViewFragment);
	    ft.replace(R.id.container_fragment_log, mLogFragment);
	    ft.commit();

		if(mIsFirst){
			mIsFirst =false;
			attemptGetGrapgh();
		}



	}

	@Override
	public void onStop() {
		super.onStop();
		cancelTuusin();
	}




	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

//		FragmentManager fm = getChildFragmentManager();
//		FragmentTransaction ft = fm.beginTransaction();
//	    ft.replace(R.id.container_fragment_graph, mGraphViewFragment);
//	    ft.replace(R.id.container_fragment_log, mLogFragment);
//	    ft.commit();

	}

	@Override
	public void onDetach() {
		super.onDetach();

//		FragmentManager fm = getChildFragmentManager();
//		FragmentTransaction ft = fm.beginTransaction();
//	    ft.remove(mGraphViewFragment);
//	    ft.remove(mLogFragment);
//	    ft.commit();
	}

	private void cancelTuusin(){
		cancelGetGrapghTask();
	}
	private void cancelGetGrapghTask(){
		if(mGetGraphTask==null) return ;
		mGetGraphTask.cancel(true);
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
