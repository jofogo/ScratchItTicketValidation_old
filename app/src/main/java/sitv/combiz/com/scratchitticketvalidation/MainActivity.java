package sitv.combiz.com.scratchitticketvalidation;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText txtUser;
    EditText txtPass;
    Button btnLogin;
    ImageButton btnMenu;
    ImageButton btnAbout;
    ImageButton btnSettings;
    ImageButton btnLogout;
    Boolean userLoggedIn = false;
    Boolean menuHidden = true;
    HashMap<String, String> credentials = new HashMap<String, String>();

    //Menu pressed handler
    public void menuPressed(View view) {
        if (menuHidden) {
            menuShow();
            menuHidden=false;
        } else {
            menuHide();
            menuHidden=true;
        }

    }

    //Show top menu
    private void menuShow() {
        int translationYBy = 100;
        int duration = 100;
        btnAbout.setVisibility(View.VISIBLE);
        btnAbout.animate().translationYBy(translationYBy).setDuration(duration);
        btnSettings.setVisibility(View.VISIBLE);
        btnSettings.animate().translationYBy(translationYBy).setDuration(duration+50);
        btnLogout.animate().translationYBy(translationYBy).setDuration(duration+100);
        //btnLogout.setVisibility(View.VISIBLE);

    }

    //Hide top menu
    private void menuHide(){
        int translationYBy = -100;
        btnAbout.setVisibility(View.INVISIBLE);
        btnAbout.setTranslationY(translationYBy);
        btnSettings.setVisibility(View.INVISIBLE);
        btnSettings.setTranslationY(translationYBy);
        btnLogout.setVisibility(View.INVISIBLE);
        btnLogout.setTranslationY(translationYBy);
    }

    //Add a unique user to the allowed user list
    private void credentialsAddUser (String user, String pass) {
        String userAddStatus = "";
        Log.i("Add User", "Attempting to add " + user + "/" + pass);
        if (user.equals("")) {
            userAddStatus += " username";
        }
        if (pass.equals("")) {
            userAddStatus += " password";
        }
        if (userAddStatus.equals("")) {
            if (credentialsCheckUsername(user)) {
                userAddStatus = "User already exists";
            } else {
                credentials.put(user.toLowerCase(), pass.toLowerCase());
                Log.i("User added", user);
            }

        }
    }

    //Check if the specified username exists
    private Boolean credentialsCheckUsername (String user) {
        Boolean userExists = false;
        user = user.toLowerCase();
        if (user.equals("")) {
            Log.i("Check User", "User is empty!");
        } else {
            if (credentials.containsKey(user)) {
                userExists = true;
                Log.i("Check User", "User found!" );
            }

        }
        return userExists;
    }

    //Check the username/password input
    private Boolean credentialsCheckPassword (String user, String pass) {
        String checkCredentials="";
        Boolean status = false;
        Log.i("Check if exists", user + "/" + pass);
        if (user.equals("") || pass.equals("")) {
            checkCredentials = "No username/password supplied!";
        } else {
            if (credentialsCheckUsername(user)) {
                if (credentials.get(user.toLowerCase()).equals(pass)) {
                    checkCredentials = "Logging in...";
                    status = true;
                } else {
                    checkCredentials = "Incorrect password!";
                }
            } else {
                checkCredentials = "Invalid username/password";
            }
        }
        Toast.makeText(this, checkCredentials, Toast.LENGTH_SHORT).show();
        return status;
    }

    //Validate login form inputs
    public void checkForm(View view) {
        String username = txtUser.getText().toString();
        String password = txtPass.getText().toString();
        Log.i("credentials supplied", username + "/" + password);
        if(credentialsCheckPassword(username, password)) {
            userLoggedIn = true;
            Intent activityValidation = new Intent(this, ValidationActivity.class);
            startActivity(activityValidation);
        } else {
            txtPass.setText("");
            txtUser.requestFocus();
            txtUser.selectAll();
        };

    }

    //Fill in demo credential list
    private void demoAddUsers() {
        credentialsAddUser("combiz","demo");
        credentialsAddUser("admin", "dasc");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        demoAddUsers();
        txtUser = (EditText) findViewById(R.id.txtUser);
        txtPass = (EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnAbout = (ImageButton) findViewById(R.id.btnAbout);
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);

        menuHide();

        txtUser.requestFocus();
        txtPass.setEnabled(false);
        btnLogin.setEnabled(false);

        txtUser.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               }

               @Override
               public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               }

               @Override
               public void afterTextChanged(Editable editable) {
                   if (txtUser.getText().toString().equals("")) {
                       txtPass.setEnabled(false);
                   } else {
                       txtPass.setEnabled(true);
                   }
               }
           }

        );

        txtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (txtPass.getText().toString().equals("")) {
                    btnLogin.setEnabled(false);
                    btnLogin.setTextColor(getResources().getColor(R.color.text_gray));
                    btnLogin.setBackgroundColor(getResources().getColor(R.color.button_inactive));
                } else {
                    btnLogin.setEnabled(true);
                    btnLogin.setTextColor(getResources().getColor(R.color.text_white));
                    btnLogin.setBackgroundColor(getResources().getColor(R.color.button_active));
                }
            }
        });

    }



    // Auto-updater functionality - TBD
    private  class checkForUpdates extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection httpURLConnection = null;
            try {

            } catch (Exception e) {
                return null;
            }
            return null;
        }
    }

    private void downloadUpdate() {

    }

}
