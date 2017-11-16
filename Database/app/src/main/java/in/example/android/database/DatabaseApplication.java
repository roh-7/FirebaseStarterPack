package in.example.android.database;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by rohitramaswamy on 17/11/17.
 */

public class DatabaseApplication extends Application
{
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		FirebaseApp.initializeApp(this);
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
	}
}
