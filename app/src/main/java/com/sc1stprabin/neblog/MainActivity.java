package com.sc1stprabin.neblog;



import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;

    private TextView login, signup;
    private Button submit;
    private TextView forgetPwd;
    private TextInputLayout edtConfirm;

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
                if(isSignup){
                      Toast.makeText(getApplicationContext(),"Signing Up!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Log in!",Toast.LENGTH_SHORT).show();
                }

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