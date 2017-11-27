package in.example.android.userandadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static in.example.android.userandadmin.Constants.TAG;

public class SignUpActivity extends AppCompatActivity
{
	
	SessionManager sessionManager;
	TextInputEditText username;
	Spinner role;
	Button signup;
	
	Users user;
	
	
	ProgressDialog dialog;
	
	String email_ent,username_ent,role_ent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		Log.v(Constants.TAG,"user in sign up");
		
		sessionManager = new SessionManager(SignUpActivity.this);
		
		username = findViewById(R.id.signup_username);
		role = (Spinner)findViewById(R.id.role_spinner);
		final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.roles,android.R.layout.simple_spinner_dropdown_item);
		role.setAdapter(adapter);
		signup = findViewById(R.id.signup_button);
		
		signup.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				dialog = new ProgressDialog(SignUpActivity.this);
				dialog.setMessage("Signing up");
				dialog.setTitle("Please Wait");
				dialog.setCancelable(false);
				
				email_ent = getIntent().getStringExtra("email");
				username_ent = username.getText().toString();
				role_ent = role.getSelectedItem().toString();
				
				user = new Users();
				user.setEmail(email_ent);
				user.setName(username_ent);
				user.setRole(role_ent);
				Log.v(TAG,"b4 ref");
				DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
				reference.child(String.valueOf(sessionManager.getUserCount()+1)).setValue(user)
						.addOnCompleteListener(new OnCompleteListener<Void>()
						{
							@Override
							public void onComplete(@NonNull Task<Void> task)
							{
								Log.v("signup", "child added");
								sessionManager.loginUser(email_ent,username_ent,role_ent,getIntent().getStringExtra("display_name"));
								startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
								finish();
								dialog.dismiss();
							}
						});
				Log.v(TAG,"inside listener");
			}
		});
		Log.v(TAG,"aftr listener");
		
	}
}
