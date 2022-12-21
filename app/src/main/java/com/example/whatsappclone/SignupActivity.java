package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout rootLayout;
    EditText edtSignupEmail, edtSignupUsername, edtSignupPassword;
    Button btnSignup;
    TextView txtLoginActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        rootLayout = findViewById(R.id.rootLayout);
        edtSignupEmail = findViewById(R.id.edtSignupEmail);
        edtSignupUsername = findViewById(R.id.edtSignupUsername);
        edtSignupPassword = findViewById(R.id.edtSignupPassword);
        btnSignup = findViewById(R.id.btnSignup);
        txtLoginActivity = findViewById(R.id.txtLoginActivity);

        edtSignupPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                // if the key that is pressed is the enter key
                if(keyCode == keyEvent.KEYCODE_ENTER &&
                        // if there is an action down on this key
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    // btn sign up is a view
                    onClick(btnSignup);
                }
                return false;
            }
        });

        txtLoginActivity.setOnClickListener(SignupActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rootLayout:
                hideKeyboard(rootLayout);
                break;

            case R.id.btnSignup:
                if(edtSignupEmail.getText().toString().equals("") ||
                   edtSignupUsername.getText().toString().equals("") ||
                   edtSignupPassword.getText().toString().equals("") ){

                    FancyToast.makeText(SignupActivity.this,
                            "Email, Password are required!",
                            FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                            false).show();
                }else{
                    ParseUser parseUser = new ParseUser();
                    parseUser.setEmail(edtSignupEmail.getText().toString());
                    parseUser.setUsername(edtSignupUsername.getText().toString());
                    parseUser.setPassword(edtSignupPassword.getText().toString());

                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing...");
                    progressDialog.show();
                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){

                                FancyToast.makeText(SignupActivity.this,
                                        parseUser.getUsername() + " is signed up",
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                                        false).show();
                                edtSignupEmail.setText("");
                                edtSignupUsername.setText("");
                                edtSignupPassword.setText("");
                            }else{
                                FancyToast.makeText(SignupActivity.this,
                                        "there was an error " + e.getMessage(),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                                        false).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.txtLoginActivity:
                onBackPressed();
                break;
        }
    }
    public void hideKeyboard(View view){

        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

    }
}