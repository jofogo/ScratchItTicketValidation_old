package sitv.combiz.com.scratchitticketvalidation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText txtUser;
    EditText txtPass;
    Button btnLogin;
    Boolean userLoggedIn = false;

    HashMap<String, String> credentials = new HashMap<String, String>();
    private View mLayout;

    AudioManager audioManager;
    int maxVolume;

    private final static int MY_PERMISSIONS_REQUEST_NECESSARY= 1;


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
        mLayout = findViewById(R.id.mainLayout);
        checkAndRequestPermissions();
        demoAddUsers();
        txtUser = (EditText) findViewById(R.id.txtUser);
        txtPass = (EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);



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

    //Menu launcher
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_loggedout, menu);
        return true;
    }

    //Menu handler
    @SuppressLint("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    TextView myMsg = new TextView(this);
                    myMsg.setText("V " + getPackageManager().getPackageInfo(getPackageName(),0).versionName  + "\n" +
                            "IMEI: " + telephonyManager.getDeviceId() + "\n"
                            + getResources().getString(R.string.dev_name));
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setTitle(getResources().getString(R.string.app_name))
                            .setView(myMsg)
                            .show();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.menu_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }


    private boolean checkAndRequestPermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionPHONESTATE = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionPHONESTATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_NECESSARY);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_NECESSARY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission Granted Successfully. Write working code here.
                } else {

                    quitApp();
                }
                break;
        }
    }

    private void quitApp() {

        finishAffinity();
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
