package in.example.android.database;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity
{
	private static final int RC_SIGN_IN = 9001;
	private final String TAG = LoginActivity.class.getSimpleName();
	SignInButton button;
	GoogleSignInClient googleSignInClient;
	ProgressDialog dialog;
	
	// Declaration of FirebaseAuth
	private FirebaseAuth firebaseAuth;
	private SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// configuring google sign in
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		
		// initialise FirebaseAuth
		firebaseAuth = FirebaseAuth.getInstance();
		
		dialog = new ProgressDialog(LoginActivity.this);
		dialog.setMessage("Signing in");
		dialog.setTitle("Please Wait");
		dialog.setCancelable(false);
		
		googleSignInClient = GoogleSignIn.getClient(this, gso);
		
		sessionManager = new SessionManager(LoginActivity.this);
		
		button = findViewById(R.id.login_button);
		
		button.setSize(SignInButton.SIZE_WIDE);
		
		button.setOnClickListener(new View.OnClickListener()
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
					Snackbar.make(findViewById(R.id.root), "No Internet", Snackbar.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void signIn()
	{
		Intent signInIntent = googleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		dialog.show();
		Log.v(TAG, String.valueOf(resultCode));
		
		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN)
		{
			Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
			try
			{
				// Google sign in was successfull.
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
	
	@Override
	protected void onStart()
	{
		super.onStart();
		// To check whether user is already signed in or not
		// Otherwise implemented using IS_LOGIN key stored in shared preferences and checked in Splash (one of the uses of splash).
		FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
		if (mFirebaseUser != null)
		{
			startActivity(new Intent(this, HomeActivity.class));
			finish();
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
					sessionManager.loginUser(account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString());
					startActivity(new Intent(LoginActivity.this, HomeActivity.class));
					dialog.dismiss();
				}
				else
				{
					dialog.dismiss();
					Snackbar.make(findViewById(R.id.root), "Something went wrong", Snackbar.LENGTH_SHORT).show();
				}
			}
		});
	}
}