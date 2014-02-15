package jp.keitai2013.heallin.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.crudefox.chikara.util.CFUtil;
import jp.keitai2013.heallin.AppManager;
import jp.keitai2013.heallin.R;
import jp.keitai2013.heallin.chikara.manager.GraphManager;
import jp.keitai2013.heallin.chikara.manager.GraphManager.GItem;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class GraphViewFragment extends SherlockFragment{



	private Context mContext;
	private View mRootView;

	private Handler mHandler = new Handler();


	private LayoutInflater mLayoutInflater;



	private LoginInfo mLoginInfo;
	private AppManager mApp;


	private boolean mIsFirst = true;


//	private XYMultipleSeriesDataset dataset;
//	private GraphicalView graphicalView;
//	private LinearLayout layout;


	private EditText mY;
	private Button mRec;
	private TextView mTextView;
	//private XYMultipleSeriesDataset dataset;
	private GraphicalView mGraph;
	private List<Integer> mXValue;
    private List<Double> mYValue;
    private XYSeries mSeries;
    private XYSeries mSeriesTarget;
    private XYMultipleSeriesRenderer mRenderer;
    XYMultipleSeriesDataset mDataset;
    //private int mI;

    private Date mMinDate;
    private Date mMaxDate;
    private Date mNowDate;

    private double mTarget  = 0.0;
    private double mTargetX  = 0.0;

    private double mCurrent  = 0.0;
    private double mCurrentX  = 0.0;





    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity();
		mLayoutInflater = getActivity().getLayoutInflater();

		mApp = (AppManager) getActivity().getApplication();




	}




	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState){

    		setContentView(R.layout.fragment_graphview);


			//getActivity().setContentView(R.layout.fragment_graphview);
	        mRec = (Button)findViewById(R.id.record);
	        mTextView = (TextView)findViewById(R.id.text);

	        Intent intent = getActivity().getIntent();
	        //PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND };

	        double current = intent.getDoubleExtra("current", 70.0);
	        mTarget  = intent.getDoubleExtra("target",  50.0);
	        int attain  = intent.getIntExtra("attain", 10);
	        String unit    = intent.getStringExtra("unit");

	        if(unit==null) unit = "kg";

	        //mI=1;

	        mXValue = new ArrayList<Integer>();
	        mYValue = new ArrayList<Double>();
	        //mSeries = makeParabola(mXValue, mYValue);//new XYSeries("現在値");
	        mSeries = new XYSeries("現在値");
	        mSeriesTarget = new XYSeries("目標値");


//	        mXValue.add(mI);
//	        mYValue.add(current);

//	        remnant(mYValue, mTarget);



	        //XYSeries series2  = makeLine(attain,current,mTarget);
	        //XYSeries series2  = new XYSeries("目標値");

	        mDataset = makeDataset(mSeries, mSeriesTarget);
	        //dataset.addSeries(mSeriesTarget);

	        //makeLine(0, 0, 1, 100);

	        mRenderer = makeRenderer(unit,attain);

	        mGraph = makeGraph(getActivity().getApplication(), mDataset, mRenderer);

	        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
	        layout.addView(mGraph);



	        //記録ボタンを押すことでダイアログを表示
	        mRec.setOnClickListener(new View.OnClickListener() {
	    		@Override
	    		public void onClick(View v) {
	    			//showInputDlg();
	    		}
	    	});


		return mRootView;
}





    public void updateGraph(){
    	updateGrapghData();
    	updateGrapghView();
    }

    public void updateGrapghData(){

    	if(mApp==null) return ;
    	GraphManager gm = mApp.getGrapthManager();

    	mMinDate = null;
    	mMaxDate = null;

    	mXValue.clear();
    	mYValue.clear();

    	mNowDate = new Date();

    	int num = gm.getItemLength();
    	for(int i=0;i<num;i++){
    		GItem item = gm.getItemByIndex(i);
    		mXValue.add( calcSumDate(item.time) - calcSumDate(mNowDate) );
    		mYValue.add(item.value);

    		if(mMinDate==null || item.time.getTime()<mMinDate.getTime()){
    			mMinDate = item.time;
    		}
    		if(mMaxDate==null || item.time.getTime()>mMaxDate.getTime()){
    			mMaxDate = item.time;
    		}
    	}

    	if(gm.getItemLength()>0){
    		GItem item = gm.getItemByIndex(0);
    		mCurrent = item.value;
    		mCurrentX = calcSumDate(item.time) - calcSumDate(mNowDate);


    		GItem item2 = gm.getItemByIndex(gm.getItemLength()-1);
    		mTarget = item2.goalvalue;
    		mTargetX = calcSumDate(item2.time) - calcSumDate(mNowDate) + 1;

    		CFUtil.Log("time1 "+item.time);
    		CFUtil.Log("time2 "+item2.time);

    		CFUtil.Log("current_x "+mCurrentX);
    		CFUtil.Log("target_x "+mTargetX);
    	}


    }

    private int calcSumDate(Date date){
    	return (int) ((date.getTime() - new Date(2000,0,1).getTime()) / (24*60*60*1000));
    }

    public void updateGrapghView(){

    	int min = mMinDate!=null ? calcSumDate(mMinDate) - calcSumDate(mNowDate) : 0;
    	int max = mMaxDate!=null ? calcSumDate(mMaxDate) - calcSumDate(mNowDate) : 0;

    	mSeries.clear();
    	mSeriesTarget.clear();

        mRenderer.setRange(new double[]{
    			Math.min(-90, min-30)-10,
    			Math.max(+60, max+30)+10,
        		0,100});
    	mRenderer.setPanLimits(new double[] {
    			Math.min(-90, min-30),
    			Math.max(+60, max+30),
    			0, 100 }); //グラフの表示領域を設定(x軸の範囲、y軸の範囲)

    	mRenderer.setZoomLimits(new double[] {
    			Math.min(-60, min-30),
    			Math.max(+30, max+30),
    			0, 100 }); //グラフの表示領域を設定(x軸の範囲、y軸の範囲)

	    makeParabola(mXValue, mYValue);
	    makeLine(mCurrentX, mCurrent, mTargetX, mTarget);
	    //makeLine(0, 0, 10, 50);

	    //makeDataset(mSeries);
	    //remnant(mYValue,mTarget);

	    mGraph.repaint();

    }



   XYSeries makeParabola(List<Integer> xValue, List<Double> yValue) {

	   //XYSeries series = new XYSeries("TEST");

	   //mSeries = new XYSeries("現在値");
        for(int i = 0; i<xValue.size();i++){
        	double x = xValue.get(i);
        	double y = yValue.get(i);
        	mSeries.add(x,y);

        }

        return mSeries;
    }

   //目標値までの目標のグラフを描画
    XYSeries makeLine(double current_x,double current, double target_x,double target){
    	XYSeries series = mSeriesTarget;//new XYSeries("目標値");
    	int attain = (int)( target_x - current_x);
    	double x[] = new double[attain];
    	double y[] = new double[attain];
    	double ave = (target - current)/attain; //一日にすべき減量を計算

    	for(int i = 0; i < y.length; i++){
    		x[i] = i + current_x;
    		y[i] = current + ave*i;
    	}
    	for(int i = 0; i<y.length; i++){
    		series.add(x[i], y[i]);
    		CFUtil.Log(i+" "+x[i]+" "+y[i]);
    	}
    	return series;
    }

    //描画のためのデータをセット
    XYMultipleSeriesDataset makeDataset(XYSeries series, XYSeries series_target) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);
        dataset.addSeries(series_target);
        return dataset;
    }

    //描画のデザインの設定
    XYMultipleSeriesRenderer makeRenderer(String munit, int mAttain ) {

    	float sd = getResources().getDisplayMetrics().density;

        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(); //グラフ描画で共通のインスタンスを生成
        renderer.setPanEnabled(true, true);  //グラフ表示位置ををスライドで移動できるかの有無（x軸、y軸）
        renderer.setXTitle("日付"); //ラベルの名前を決める
        renderer.setYTitle(munit);
        renderer.setXLabels(mAttain); //x軸のグリッドの間隔を指定、値が大きいほうが細かくなる
        renderer.setYLabels(mAttain); //y軸 ,,,
        renderer.setAxisTitleTextSize(12*sd); //ラベルのテキストサイズ
        renderer.setLabelsTextSize(12*sd);    //メモリのテキストサイズ
        renderer.setLabelsColor(Color.argb(255, 64, 64, 64));
        renderer.setShowGrid(true);        //グリッドの有無
        renderer.setGridColor(Color.argb(255, 128, 128, 128)); //グリッドカラーを設定
        renderer.setXLabelsAlign(Align.RIGHT);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setBarWidth(100*sd);
        renderer.setZoomButtonsVisible(false);  //グラフズームボタンの有無
        renderer.setPointSize(5f);
        renderer.setBackgroundColor(Color.argb(255, 255,255,255));
        renderer.setMarginsColor(Color.argb(255, 245,245,245));
        renderer.setMargins(new int[]{(int)(20*sd),(int)(30*sd),(int)(10*sd),(int)(15*sd)});
        renderer.setPanLimits(new double[] { -100, +100, 0, 100 }); //グラフの表示領域を設定(x軸の範囲、y軸の範囲)
        renderer.setZoomLimits(new double[] { -100, +100, 0, 100 });
        //renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
        renderer.setRange(new double[]{-100,+100,0,100});

        XYSeriesRenderer r = new XYSeriesRenderer(); //グラフごとのデザインを決めるインスタンス生成
        r.setLineWidth(4);     //グラフの太さを変更する
        r.setColor(Color.RED); //グラフの色を変更するために引数で色を渡す
        r.setPointStyle(PointStyle.DIAMOND); //ポイントマーカーの形を指定
        r.setFillPoints(true); //ポイントマーカーを塗りつぶしの有無

        XYSeriesRenderer r2 = new XYSeriesRenderer();

        renderer.addSeriesRenderer(r);
        renderer.addSeriesRenderer(r2);

        return renderer;
    }

    //最終的にグラフにするためのインスタンスを作る
    GraphicalView makeGraph(Context context, XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {

        GraphicalView graph = ChartFactory.getLineChartView(context, dataset, renderer);

        return graph;
    }


    //目標値までの残りの数値を表示
//    void remnant(List<Double> yValue, double mtarget ){
//
//    	if( yValue.size() > 0 ){
//	    	double num = yValue.get(yValue.size()-1);
//	    	mTextView.setTextColor(Color.WHITE);
//	    	mTextView.setText("目標まで後"+(num-mtarget)+"だりん");
//    	}else{
//	    	mTextView.setTextColor(Color.WHITE);
//	    	mTextView.setText("目標まで後"+"---"+"だりん");
//    	}
//    }












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
}









