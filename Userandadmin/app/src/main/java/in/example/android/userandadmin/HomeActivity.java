package in.example.android.userandadmin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity
{
	
	SessionManager sessionManager;
	
	RecyclerView recyclerView;
	TextView username, email, role, displayname;
	private LinearLayoutManager linearLayoutManager;
	private DatabaseReference reference;
	private FirebaseRecyclerAdapter<Users, UsersViewHolder> adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		Log.v(Constants.TAG, "home");
		role = findViewById(R.id.home_role);
		email = findViewById(R.id.home_email);
		username = findViewById(R.id.home_username);
		displayname = findViewById(R.id.home_display_name);
		sessionManager = new SessionManager(HomeActivity.this);
		role.setText(sessionManager.getRole());
		email.setText(sessionManager.getEmail());
		username.setText(sessionManager.getUserName());
		displayname.setText(sessionManager.getDisplayName());
		recyclerView = (RecyclerView) findViewById(R.id.users_recycler);
		
		recyclerView.setVisibility(View.GONE);
		reference = FirebaseDatabase.getInstance().getReference("/users");
		Query query = reference.orderByChild("email").equalTo(sessionManager.getEmail());
		query.addListenerForSingleValueEvent(new ValueEventListener()
		{
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				Log.v(Constants.TAG, "i");
				if (dataSnapshot.toString().contains("admin"))
				{
					if (dataSnapshot.getValue() != null)
					{
						Log.v(Constants.TAG, "in");
						
						recyclerView.setVisibility(View.VISIBLE);
						linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
						recyclerView.setLayoutManager(linearLayoutManager);
						
						adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(Users.class,
								R.layout.user_item, UsersViewHolder.class, reference)
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
				}
				else
				{
					recyclerView.setVisibility(View.GONE);
				}
				
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError)
			{
			
			}
		});
		
		
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