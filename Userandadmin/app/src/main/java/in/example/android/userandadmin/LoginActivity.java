package in.example.android.userandadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity
{
	private static final int RC_SIGN_IN = 9001;
	SignInButton signInButton;
	GoogleSignInClient googleSignInClient;
	ProgressDialog dialog;
	
	private FirebaseAuth firebaseAuth;
	private DatabaseReference reference;
	private SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		sessionManager = new SessionManager(LoginActivity.this);
		
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		
		firebaseAuth = FirebaseAuth.getInstance();
		reference = FirebaseDatabase.getInstance().getReference().child("users");
		
		dialog = new ProgressDialog(LoginActivity.this);
		dialog.setMessage("Signing in");
		dialog.setTitle("Please Wait");
		dialog.setCancelable(false);
		
		googleSignInClient = GoogleSignIn.getClient(this, gso);
		signInButton = findViewById(R.id.login_button);
		signInButton.setSize(SignInButton.SIZE_WIDE);
		signInButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (sessionManager.checkNet())
				{
					signIn();
				}
				else
				{
					Snackbar.make(findViewById(R.id.root), "Check network connectivity", Snackbar.LENGTH_SHORT);
				}
			}
		});
		revokeAccess();
	}
	
	public void signIn()
	{
		Intent intent = googleSignInClient.getSignInIntent();
		startActivityForResult(intent, RC_SIGN_IN);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
		if (mFirebaseUser != null)
		{
			startActivity(new Intent(this, HomeActivity.class));
			finish();
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		dialog.show();
		
		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN)
		{
			Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
			try
			{
				// Google sign in was successful.
				GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
				// Now authenticating using firebase
				firebaseAuthWithGoogle(googleSignInAccount);
			}
			catch (ApiException e)
			{
				// google sign in failed
				dialog.dismiss();
				e.printStackTrace();
			}
		}
		else
		
		{
			dialog.dismiss();
		}
	}
	
	
	private void firebaseAuthWithGoogle(final GoogleSignInAccount account)
	{
		AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
		firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
		{
			@Override
			public void onComplete(@NonNull Task<AuthResult> task)
			{
				if (task.isSuccessful())
				{
					// signed in successfully. Starting custom session using SessionManager and building intent to Home.
					// *User says i'm in in hacker style*
					FirebaseUser user = firebaseAuth.getCurrentUser();
					final String email_chosen = account.getEmail();
					final String display_name = account.getDisplayName();
					
					reference.addValueEventListener(new ValueEventListener()
					{
						@Override
						public void onDataChange(DataSnapshot dataSnapshot)
						{
							for (DataSnapshot snapshot : dataSnapshot.getChildren())
							{
								Long count = dataSnapshot.getChildrenCount();
								sessionManager.setUserCount(count);
								Users users = snapshot.getValue(Users.class);
								if (email_chosen.equalsIgnoreCase(users.email))
								{
									// user already exists
									sessionManager.loginUser(account.getEmail(), users.getName(), users.getRole(),display_name);
									startActivity(new Intent(LoginActivity.this, HomeActivity.class));
									dialog.dismiss();
									finish();
								}
							}
							// user email has been verified but doesn't have an account
							if (!sessionManager.isLogin())
							{
								startActivity(new Intent(LoginActivity.this, SignUpActivity.class).putExtra("email", account.getEmail()).putExtra("display_name",account.getDisplayName()));
								dialog.dismiss();
							}
						}
						
						@Override
						public void onCancelled(DatabaseError databaseError)
						{
						
						}
					});
					
				}
				else
				{
					dialog.dismiss();
					Snackbar.make(findViewById(R.id.root), "Something went wrong", Snackbar.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void revokeAccess()
	{
		googleSignInClient.revokeAccess()
				.addOnCompleteListener(this, new OnCompleteListener<Void>()
				{
					@Override
					public void onComplete(@NonNull Task<Void> task)
					{
						new SessionManager(LoginActivity.this).accessRevoked();
					}
				});
	}
}
