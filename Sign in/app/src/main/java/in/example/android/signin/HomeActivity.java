package in.example.android.signin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.signin.R;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


// This activity shows the details of the user, retrieved from google at the time of login
// The details are stored in the session using shared preferences (check SessionManager.java)


public class HomeActivity extends AppCompatActivity
{
	private static final String TAG = HomeActivity.class.getSimpleName();
	
	// defining views to be used
	TextView display_name, display_email;
	
	// using CircleImageView to show the profile image
	CircleImageView profile;
	
	// defining parameters to be used. This can be avoided by calling the methods of SessionManager directly inside setText()
	String name, email, profile_url;
	
	// instance of SessionManager
	SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		
		// initialising the views
		display_email = findViewById(R.id.display_email);
		display_name = findViewById(R.id.display_name);
		
		profile = findViewById(R.id.profile_img);
		
		// initialising session
		sessionManager = new SessionManager(HomeActivity.this);
		
		// storing values in strings
		name = sessionManager.getUserName();
		email = sessionManager.getUserEmail();
		
		profile_url = sessionManager.getProfileUrl();
		
		// so that profile pic can be seen from the log itself
		Log.v(TAG,profile_url);
		
		display_name.setText(name);
		display_email.setText(email);
		
		// using Glide, a fast and efficient image loading framework
		// https://github.com/bumptech/glide
		// requires the INTERNET permission to be defined in the manifest
		Glide.with(this).load(profile_url).into(profile);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// inflating custom menu in option menu
		// home_menu.xml is defined in app/res/menu/home_menu.xml (Android view)
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		
		if (id == R.id.options_logout)
		{
			// when user clicks the logout option from the menu
			FirebaseAuth.getInstance().signOut();
			sessionManager.logoutUser();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
