package in.example.android.database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
{
	private static final String TAG = HomeActivity.class.getSimpleName();
	
	SessionManager sessionManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		sessionManager = new SessionManager(HomeActivity.this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_home,menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		
		if(id == R.id.menu_logout)
		{
			FirebaseAuth.getInstance().signOut();
			sessionManager.logoutUser();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
