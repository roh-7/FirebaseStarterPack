package in.example.android.signin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static in.example.android.signin.Constants.IS_LOGIN;
import static in.example.android.signin.Constants.PROFILE_URL;
import static in.example.android.signin.Constants.USER_EMAIL;
import static in.example.android.signin.Constants.USER_NAME;

/**
 * Created by rohitramaswamy on 16/11/17.
 */

public class SessionManager
{
	// creating a tag to be used in all log messages in this class.
	// doing this for each class is a good practice
	private final static String TAG = SessionManager.class.getSimpleName();
	
	// name of the shared preference file
	private static final String PREF_NAME = "FirebaseTry";
	private static final int PRIVATE_MODE = 0;
	
	
	Context context;
	
	SharedPreferences sharedPreferences;
	
	SharedPreferences.Editor editor;
	
	public SessionManager(Context context)
	{
		this.context = context;
		sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = sharedPreferences.edit();
	}
	
	// function to check whether the user is connected to the internet or not
	// requires the ACCESS_NETWORK_STATE permission to be defined in the manifest
	public boolean checkNet()
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
	
	//	method to login user
	public void loginUser(String user_name, String user_email, String profile_url)
	{
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(USER_EMAIL, user_email);
		editor.putString(USER_NAME, user_name);
		editor.putString(PROFILE_URL, profile_url);
		editor.apply();
	}
	
	// method to logout user
	public void logoutUser()
	{
		// Clearing the SharedPreferences
		editor.clear();
		editor.commit();
		editor.apply();
		
		// After logout redirect user to Main Activity
		Intent i = new Intent(context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Staring LoginActivity Activity
		context.startActivity(i);
		
	}
	
	// To check if the user is logged in or not
	// This is useful when you are not using firebase(sign in with google). In this example this is already taken care of
	// This flag is checked at the time of splash to direct user to login activity or the home activity
	public boolean isLogin()
	{
		return sharedPreferences.getBoolean(IS_LOGIN, false);
	}
	
	
	// getter methods for SharedPreference values
	public String getUserName()
	{
		return sharedPreferences.getString(USER_NAME, "John Doe");
	}
	
	public String getUserEmail()
	{
		return sharedPreferences.getString(USER_EMAIL, "john.doe@example.com");
	}
	
	public String getProfileUrl()
	{
		return sharedPreferences.getString(PROFILE_URL, "https://cdn1.iconfinder.com/data/icons/business-charts/512/customer-512.png");
	}
	
}
