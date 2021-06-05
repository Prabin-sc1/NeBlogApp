package com.sc1stprabin.neblog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;
import com.facebook.FacebookSdk;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;

    private TextView login, signup;
    private Button submit;
    private TextView forgetPwd;
    private TextInputLayout edtConfirm;

    private TextInputEditText email, password, confirmPwd;

    private CallbackManager mCallbackManager;

    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private boolean isSignup = true;

    private ImageView googleImg;
    private ImageView facebookImg;


    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private static final String TAG = "FacebookAuthentication";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCallbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  constraintLayout = findViewById(R.id.clWelcomeId);

        login = findViewById(R.id.txtLoginId);
        signup = findViewById(R.id.txtSignupId);
        submit = findViewById(R.id.btnSubmit);
        forgetPwd = findViewById(R.id.forgerPwd);
        edtConfirm = findViewById(R.id.edtConfirm);

        facebookImg = findViewById(R.id.facebookId);
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());


        facebookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG,"onSuccess"+loginResult);
                        handleFacebookToken(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG,"onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG,"onError"+error);
                    }
                });
            }
        });


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    updateUI(firebaseUser);
                    //startActivity(new Intent(getApplicationContext(), BlogsActivity.class));
                }
                else {
                    updateUI(null);

                }
            }
        };

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken != null) {
                    mAuth.signOut();
                }
            }
        };





        googleImg = findViewById(R.id.googleImgViewId);


        mCallbackManager = CallbackManager.Factory.create();


        //signing with google process
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // setting login and signup
        email = findViewById(R.id.edtEmailId);
        password = findViewById(R.id.edtPasswordId);
        confirmPwd = findViewById(R.id.edtConfirmPwdId);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSignup = false;
                login.setElevation(4);
                login.setBackground(getResources().getDrawable(R.drawable.text_border_signup));
                login.setTextColor(getResources().getColor(R.color.grey));
                signup.setElevation(0);
                signup.setBackground(getResources().getDrawable(R.drawable.login_border));
                signup.setTextColor(getResources().getColor(R.color.bs));

                forgetPwd.setVisibility(View.VISIBLE);
                edtConfirm.setVisibility(View.GONE);
                submit.setText("Log in");


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSignup = true;
                login.setElevation(0);
                login.setBackground(getResources().getDrawable(R.drawable.login_border));
                login.setTextColor(getResources().getColor(R.color.bs));
                signup.setElevation(4);
                signup.setBackground(getResources().getDrawable(R.drawable.text_border_signup));
                signup.setTextColor(getResources().getColor(R.color.grey));

                forgetPwd.setVisibility(View.GONE);
                edtConfirm.setVisibility(View.VISIBLE);
                submit.setText("Sign up");

            }
        });

        googleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String emailVal = email.getText().toString();
                String pwdVal = password.getText().toString();
                String confirmVal = confirmPwd.getText().toString();

                if(isSignup){

                    if(!emailVal.isEmpty() && !pwdVal.isEmpty() && !confirmVal.isEmpty()){
                       if(pwdVal == confirmVal){
                            mAuth.createUserWithEmailAndPassword(emailVal,pwdVal).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        isSignup = false;
                                        login.setElevation(4);
                                        login.setBackground(getResources().getDrawable(R.drawable.text_border_signup));
                                        login.setTextColor(getResources().getColor(R.color.grey));
                                        signup.setElevation(0);
                                        signup.setBackground(getResources().getDrawable(R.drawable.login_border));
                                        signup.setTextColor(getResources().getColor(R.color.bs));

                                        forgetPwd.setVisibility(View.VISIBLE);
                                        edtConfirm.setVisibility(View.GONE);
                                        submit.setText("Log in");

                                        clear();

                                      //  Toast.makeText(MainActivity.this, "Successfully signed up", Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                       else{
                           Toast.makeText(MainActivity.this, "password doesn't match",Toast.LENGTH_SHORT).show();
                       }
                    }


                } else {

                    //why this because we are in same activity
                    if (!emailVal.isEmpty() && !pwdVal.isEmpty()) {

                       mAuth.signInWithEmailAndPassword(emailVal, pwdVal).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(MainActivity.this, BlogsActivity.class));
                                    finish();
                                   //Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else{
                        Toast.makeText(MainActivity.this,"Please fill your credentials",Toast.LENGTH_SHORT).show();
                    }

                }

            }
            public void clear(){
                email.setText("");
                password.setText("");
                confirmPwd.setText("");
            }
        });
//
//        constraintLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                constraintLayout.setVisibility(View.GONE);
//            }
//        });
    }

    private void handleFacebookToken(AccessToken accessToken) {
        Log.d(TAG,"handleFacebookToken"+accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"sigining with credential: successful");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    //startActivity(new Intent(MainActivity.this,BlogsActivity.class));
                    Toast.makeText(getApplicationContext(),"Signed in with fb",Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(TAG,"signing with credential: failure",task.getException());
                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            startActivity(new Intent(getApplicationContext(),BlogsActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    int RC_SIGN_IN = 11;
    private void signIn() {
      //  Intent signInIntentt = .getSignInIntent();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG,"please check here coz startActivityForResult() is deprecated ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //facebook
        mCallbackManager.onActivityResult(requestCode, resultCode, data);




        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this,BlogsActivity.class));
                            Toast.makeText(getApplicationContext(),"Signed in with google",Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    //facebook

}