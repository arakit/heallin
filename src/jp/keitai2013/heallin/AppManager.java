package jp.keitai2013.heallin;

import jp.keitai2013.heallin.chikara.manager.GraphManager;
import jp.keitai2013.heallin.chikara.manager.LoginInfo;
import jp.keitai2013.heallin.chikara.manager.LoginManager;
import jp.keitai2013.heallin.chikara.manager.MemberManager;
import jp.keitai2013.heallin.chikara.manager.PictureManager;
import jp.keitai2013.heallin.chikara.manager.ProfileManager;
import android.app.Application;


/**
*
* @author Chikara Funabashi.
*
*/

public class AppManager extends Application{




	private LoginInfo mLoginInfo;

	private LoginManager mLoginManger;


	private MemberManager mMemberManager;

	private ProfileManager mProfileManager;

	private GraphManager mGraphManager;

	private PictureManager mPictureManager;


	@Override
	public void onCreate() {
		super.onCreate();

		mLoginManger = new LoginManager(this);
		mMemberManager = new MemberManager(this);
		mProfileManager = new ProfileManager(this);
		mGraphManager = new GraphManager(this);
		mPictureManager = new PictureManager();

	}

	@Override
	public void onTerminate() {
		// TODO 自動生成されたメソッド・スタブ
		super.onTerminate();
	}

	public LoginInfo getLoginInfo(){
		return mLoginInfo;
	}

	public LoginManager getLoginManager(){
		return mLoginManger;
	}

	public MemberManager getMemberManager(){
		return mMemberManager;
	}

	public ProfileManager getProfileManager(){
		return mProfileManager;
	}

	public GraphManager getGrapthManager(){
		return mGraphManager;
	}
	
	public PictureManager getPictureManager(){
		return mPictureManager;
	}




	public void setLoginInfo(LoginInfo info){
		mLoginInfo = info;
	}



	public boolean isLoggedIn(){
		if(mLoginInfo==null) return false;
		return mLoginInfo.isLoggedIn();
	}



	public void clearLogin(){
		setLoginInfo(null);

		mMemberManager.clear();
		mProfileManager.clear();

	}





}
