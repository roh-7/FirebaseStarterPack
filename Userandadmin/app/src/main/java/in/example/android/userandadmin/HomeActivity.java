package in.example.android.userandadmin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity
{
	
	SessionManager sessionManager;
	
	RecyclerView recyclerView;
	private LinearLayoutManager linearLayoutManager;
	private DatabaseReference reference;
	private FirebaseRecyclerAdapter<Users, UsersViewHolder> adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		Log.v(Constants.TAG,"home");
		
		sessionManager = new SessionManager(HomeActivity.this);
		
		recyclerView = (RecyclerView) findViewById(R.id.users_recycler);
		linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
		recyclerView.setLayoutManager(linearLayoutManager);
		
		reference = FirebaseDatabase.getInstance().getReference();
		adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(Users.class,
				R.layout.user_item, UsersViewHolder.class, reference.child("users"))
		{
			@Override
			protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position)
			{
				viewHolder.email.setText(model.email);
				viewHolder.name.setText(model.name);
				viewHolder.role.setText(model.role);
			}
		};
		recyclerView.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		
		if (id == R.id.menu_logout)
		{
			FirebaseAuth.getInstance().signOut();
			sessionManager.logoutUser();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}