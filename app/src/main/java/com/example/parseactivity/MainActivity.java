package com.example.parseactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements  View.OnClickListener , View.OnKeyListener{

Boolean signUpModeActive = true;
Button signUpbutton;
TextView loginTextView;
EditText usernameEditText;
ImageView logo;
    EditText passwordEditText;

    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if( i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            signUpClicked(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.logintextView){
            if(signUpModeActive){
                signUpModeActive = false;
                signUpbutton.setText("Login");
                loginTextView.setText("or, SignUp");
            }else{
                signUpModeActive = true;
                signUpbutton.setText("Sign Up !");
                loginTextView.setText("or, Login");

            }
        }else if(view.getId() == R.id.backgorundLayout || view.getId() == R.id.iconImageView){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
        }
    }

    public void signUpClicked(View view){

if(usernameEditText.getText().toString().matches("") && passwordEditText.getText().toString().matches("")){
    Toast.makeText(this , "A username and password are required!",Toast.LENGTH_SHORT).show();
}else{
      if(signUpModeActive) {
          ParseUser user = new ParseUser();
          user.setUsername(usernameEditText.getText().toString());
          user.setPassword(passwordEditText.getText().toString());
          user.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {
                  if (e == null) {
                      Log.i("Signup", "Successful");
                      showUserList();
                  } else {
                      Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                  }
              }
          });
      }else{
          //Login
          ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
              @Override
              public void done(ParseUser user, ParseException e) {
                  if(user != null){
                      Log.i("Log in" , "Success");
                      showUserList();
                  }else{
                      Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                  }
              }
          });
      }

}
loginTextView.setOnClickListener(this);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

 }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Instagram Clone");
        usernameEditText = findViewById(R.id.usernameText);
        passwordEditText = findViewById(R.id.passwordText);
        loginTextView = findViewById(R.id.logintextView);
        signUpbutton  = findViewById(R.id.signupButton);
      passwordEditText.setOnKeyListener(this);
      logo = findViewById(R.id.iconImageView);
      logo.setOnClickListener(this);
        ConstraintLayout layout = findViewById(R.id.backgorundLayout);
        layout.setOnClickListener(this);
        if(ParseUser.getCurrentUser() != null){
            showUserList();
        }
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
}
}