package com.sc1stprabin.neblog;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;

    private TextView login, signup;
    private Button submit;
    private TextView forgetPwd;
    private TextInputLayout edtConfirm;

    private TextInputEditText email, password, confirmPwd;

    private boolean isSignup = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.clWelcomeId);

        login = findViewById(R.id.txtLoginId);
        signup = findViewById(R.id.txtSignupId);
        submit = findViewById(R.id.btnSubmit);
        forgetPwd = findViewById(R.id.forgerPwd);
        edtConfirm = findViewById(R.id.edtConfirm);

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

        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String emailVal = email.getText().toString();
                String pwdVal = password.getText().toString();
                String confirmVal = confirmPwd.getText().toString();

                if(isSignup){

                    if(!emailVal.isEmpty() && !pwdVal.isEmpty() && !confirmVal.isEmpty()){
                       if(pwdVal != confirmVal){
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailVal,pwdVal).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
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
                      //Toast.makeText(getApplicationContext(),"Signing Up!",Toast.LENGTH_SHORT).show();

                } else {
                    //Toast.makeText(getApplicationContext(),"Log in!",Toast.LENGTH_SHORT).show();
//                     emailVal = email.getText().toString();
//                     pwdVal = password.getText().toString();
                    //why this because we are in same activity
                    if (!emailVal.isEmpty() && !pwdVal.isEmpty()) {

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailVal, pwdVal).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
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
            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.GONE);
            }
        });
    }
}