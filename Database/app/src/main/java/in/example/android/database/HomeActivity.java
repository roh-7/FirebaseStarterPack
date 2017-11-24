package in.example.android.database;

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
	private static final String TAG = HomeActivity.class.getSimpleName();
	
	SessionManager sessionManager;
	
	RecyclerView recyclerView;
	private LinearLayoutManager linearLayoutManager;
	private DatabaseReference reference;
	private FirebaseRecyclerAdapter<Updates, UpdatesViewHolder> adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		sessionManager = new SessionManager(HomeActivity.this);
		
		recyclerView = (RecyclerView) findViewById(R.id.updates_recycler);
		linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
		recyclerView.setLayoutManager(linearLayoutManager);
		
		reference = FirebaseDatabase.getInstance().getReference();
		adapter = new FirebaseRecyclerAdapter<Updates, UpdatesViewHolder>(Updates.class,
				R.layout.updates_item, UpdatesViewHolder.class, reference.child("updates"))
		{
			@Override
			protected void populateViewHolder(UpdatesViewHolder viewHolder, Updates model, int position)
			{
				viewHolder.update.setText(model.title);
				Log.v(TAG, "update: " + model.title);
				Log.v(TAG, "getUpdate: " + model.getTitle());
				Log.v(TAG, "count: " + model.count);
				Log.v(TAG, "getCount: " + model.getCount());
				viewHolder.count.setText(model.count);
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