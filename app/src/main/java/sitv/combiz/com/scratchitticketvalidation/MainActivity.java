package sitv.combiz.com.scratchitticketvalidation;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText txtUser;
    EditText txtPass;
    Button btnLogin;
    Boolean userLoggedIn = false;
    HashMap<String, String> credentials = new HashMap<String, String>();


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

    private void demoAddUsers() {
        credentialsAddUser("combiz","demo");
        credentialsAddUser("admin", "dasc");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        demoAddUsers();
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnLogin);
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
                    btnLogin.setBackgroundColor(getResources().getColor(R.color.button_inactive));
                } else {
                    btnLogin.setEnabled(true);
                    btnLogin.setBackgroundColor(getResources().getColor(R.color.button_active));
                }
            }
        });

    }
}
