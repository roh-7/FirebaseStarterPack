package in.example.android.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.android.signin.R;
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
	private FirebaseAuth firebaseAuth;
	private SessionManager sessionManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		
		firebaseAuth = FirebaseAuth.getInstance();
		
		dialog = new ProgressDialog(LoginActivity.this);
		dialog.setMessage("Signing in");
		dialog.setTitle("Please Wait");
		dialog.setCancelable(false);
		
		googleSignInClient = GoogleSignIn.getClient(this, gso);
		
		sessionManager = new SessionManager(LoginActivity.this);
		
		button = (SignInButton) findViewById(R.id.login_button);
		
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
		Log.v(TAG, "Sign in hogaya");
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		dialog.show();
		Log.v(TAG, String.valueOf(resultCode));
		Log.v(TAG, "in result");
		if (requestCode == RC_SIGN_IN)
		{
			Log.v(TAG, "request code matches");
			Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
			try
			{
				GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
				firebaseAuthWithGoogle(googleSignInAccount);
			}
			catch (ApiException e)
			{
				dialog.dismiss();
				Log.v(TAG, "error: " + e.toString());
//				e.printStackTrace();
			}
		}
		else
		{
			Log.v(TAG, "req code match nahi hua" + String.valueOf(RC_SIGN_IN));
			dialog.dismiss();
		}
		Log.v(TAG, "result over");
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
	
	private void firebaseAuthWithGoogle(final GoogleSignInAccount account)
	{
		Log.v(TAG, "firebaseAuthWithGoogle: " + account.getId());
		AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
		firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
		{
			@Override
			public void onComplete(@NonNull Task<AuthResult> task)
			{
				if (task.isSuccessful())
				{
					// signed in
					Log.v(TAG, "Signed In");
					FirebaseUser user = firebaseAuth.getCurrentUser();
					sessionManager.loginUser(account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString());
					startActivity(new Intent(LoginActivity.this, HomeActivity.class));
					dialog.dismiss();
				}
				else
				{
					Log.v(TAG, "Not Signed In");
					dialog.dismiss();
					Snackbar.make(findViewById(R.id.root), "Something went wrong", Snackbar.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	
}

