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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout rootLayout;
    EditText edtLoginEmail, edtLoginPassword;
    Button btnLogin;
    TextView txtSignUpActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rootLayout = findViewById(R.id.rootLayout);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignUpActivity = findViewById(R.id.txtSignupActivity);

        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            // if the key that is pressed is the enter key
            if(keyCode == keyEvent.KEYCODE_ENTER &&
                    // if there is an action down on this key
                    keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                // btn sign up is a view
                onClick(btnLogin);
            }
            return false;
        }
    });

        txtSignUpActivity.setOnClickListener(LoginActivity.this);


      /*  if(ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();

        }*/
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.txtSignupActivity:
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                break;

            case R.id.btnLogin:

                try {
                    if(edtLoginEmail.getText().toString().equals("") ||
                            edtLoginPassword.getText().toString().equals("")){

                        FancyToast.makeText(LoginActivity.this,
                                "Email, Password are required!",
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                                false).show();
                    }else{

                        ParseUser appUser = new ParseUser();
                        ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Logging...");
                        progressDialog.show();
                        appUser.logInInBackground(edtLoginEmail.getText().toString(),
                                edtLoginPassword.getText().toString(), new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        if(user != null && e == null){
                                            FancyToast.makeText(LoginActivity.this,
                                                    user.getUsername() + " is logged in",
                                                    FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                                                    false).show();
                                            edtLoginEmail.setText("");
                                            edtLoginPassword.setText("");
                                            transitionToWhatsAppUsersActivity();

                                        }else{

                                            FancyToast.makeText(LoginActivity.this,
                                                    "there was an error " + e.getMessage(),
                                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                                                    false).show();
                                        }
                                        progressDialog.dismiss();
                                    }
                                });

                    }
                }catch (Exception e){

                }

                break;

            case R.id.rootLayout:
                hideKeyboard(rootLayout);
                break;
        }
    }
    public void hideKeyboard(View view){

        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

    }

    private void transitionToWhatsAppUsersActivity(){
        Intent intent = new Intent(LoginActivity.this, WhatsAppUsersActivity.class);
        startActivity(intent);

    }
}