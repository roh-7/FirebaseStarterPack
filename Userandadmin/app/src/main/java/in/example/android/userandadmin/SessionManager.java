package in.example.android.userandadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static in.example.android.userandadmin.Constants.EMAIL;
import static in.example.android.userandadmin.Constants.IS_LOGIN;
import static in.example.android.userandadmin.Constants.ROLE;
import static in.example.android.userandadmin.Constants.USERS;
import static in.example.android.userandadmin.Constants.USER_NAME;

/**
 * Created by rohitramaswamy on 26/11/17.
 */

public class SessionManager
{
	
	private static final String TAG = SessionManager.class.getSimpleName();
	
	private static final String PREF_NAME = "UserAndAdmin";
	private final int PRIVATE_MODE = 0;
	
	Context context;
	
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	
	public SessionManager(Context context)
	{
		this.context = context;
		sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = sharedPreferences.edit();
	}
	
	public boolean checkNet()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnectedOrConnecting();
	}
	
	public void loginUser(String email, String user_name,String role)
	{
		editor.putString(EMAIL, email);
		editor.putString(USER_NAME, user_name);
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(ROLE,role);
		editor.apply();
	}
	
	public void logoutUser()
	{
		editor.clear();
		editor.commit();
		editor.apply();
		
		Intent intent = new Intent(context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public boolean isLogin()
	{
		return sharedPreferences.getBoolean(IS_LOGIN, false);
	}
	
	public String getEmail()
	{
		return sharedPreferences.getString(EMAIL, "email.email@gmail.com");
	}
	
	public String getUserName()
	{
		return sharedPreferences.getString(USER_NAME, "User Name");
	}
	
	public void setUserCount(long i)
	{
		editor.putLong(USERS,i);
		editor.apply();
	}
	
	public long getUserCount()
	{
		return sharedPreferences.getLong(USERS,1);
	}
	
	public String getRole()
	{
		return sharedPreferences.getString(ROLE,"user");
	}
}
