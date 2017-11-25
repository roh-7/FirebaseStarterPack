package in.example.android.userandadmin;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by rohitramaswamy on 26/11/17.
 */

public class UserAndAdminApplication extends Application
{
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		FirebaseApp.initializeApp(this);
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
	}
}
