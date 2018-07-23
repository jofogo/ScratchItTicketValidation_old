package sitv.combiz.com.scratchitticketvalidation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ValidationActivity extends AppCompatActivity {
    ImageButton btnAdd;
    ImageButton btnUpload;
    ImageButton btnRemove;
    EditText txtTicketCount;
    EditText txtTicketCode;
    ImageButton btnMenu;
    ImageButton btnAbout;
    ImageButton btnSettings;
    ImageButton btnLogout;
    Boolean menuHidden = true;


    ArrayList<String> ticketCodes = new ArrayList<String>();
    private final int ticketCodeLength = 10;

    public void menuPressed(View view) {
        if (menuHidden) {
            menuShow();
            menuHidden=false;
        } else {
            menuHide();
            menuHidden=true;
        }

    }

    private void menuShow() {
        int translationYBy = 100;
        int duration = 100;
        btnAbout.setVisibility(View.VISIBLE);
        btnAbout.animate().translationYBy(translationYBy).setDuration(duration);
        btnSettings.setVisibility(View.VISIBLE);
        btnSettings.animate().translationYBy(translationYBy).setDuration(duration+50);
        btnLogout.animate().translationYBy(translationYBy).setDuration(duration+100);
        btnLogout.setVisibility(View.VISIBLE);

    }

    private void menuHide(){
        int translationYBy = -100;
        btnAbout.setVisibility(View.INVISIBLE);
        btnAbout.setTranslationY(translationYBy);
        btnSettings.setVisibility(View.INVISIBLE);
        btnSettings.setTranslationY(translationYBy);
        btnLogout.setVisibility(View.INVISIBLE);
        btnLogout.setTranslationY(translationYBy);
    }



    public void ticketAdd(View view) {
        String ticketCode = txtTicketCode.getText().toString();
        if (ticketCode.equals("")) {
            Toast.makeText(this, "Ticket code is empty!", Toast.LENGTH_SHORT).show();
        } else if (ticketCode.length() < ticketCodeLength) {
            Toast.makeText(this, "Ticket code should be at least " +ticketCodeLength+ " characters!", Toast.LENGTH_SHORT).show();
        } else {
            if (ticketCodes.contains(ticketCode)) {
                Toast.makeText(this, "Ticket code was already used!", Toast.LENGTH_SHORT).show();
            } else {
                ticketCodes.add(ticketCode);
                Toast.makeText(this, "Ticket code: " + ticketCode + " was added.", Toast.LENGTH_SHORT).show();
                txtTicketCount.setText("" + ticketCodes.size());
                txtTicketCode.setText("");
            }
        }
    }

    public void ticketRemoveAll(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Remove all tickets")
                .setMessage("Do you want to delete all scanned tickets?")
                .setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ticketCodes.clear();
                                txtTicketCount.setText("0");

                            }
                        }

                )
                .setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }
                ).show();


    }

    public void ticketUpload(View view) {

    }

    public void userLogout(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ValidationActivity.super.getBaseContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                )
                .setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }
                ).show();





    }


    @Override
    public void onBackPressed() {
        userLogout(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnUpload = (ImageButton) findViewById(R.id.btnUpload);
        btnRemove = (ImageButton) findViewById(R.id.btnRemove);
        txtTicketCount = (EditText) findViewById(R.id.txtTicketCount);
        txtTicketCode = (EditText) findViewById(R.id.txtTicketCode);

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnAbout = (ImageButton) findViewById(R.id.btnAbout);
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);

        menuHide();


        btnAdd.setEnabled(false);
        btnUpload.setEnabled(false);
        btnRemove.setEnabled(false);

        txtTicketCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtTicketCode.getText().toString().equals("")) {
                    btnAdd.setEnabled(false);
                    btnAdd.setImageResource(R.drawable.ico_disabled_add);

                } else {
                    btnAdd.setEnabled(true);
                    btnAdd.setImageResource(R.drawable.ico_enabled_add);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtTicketCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (txtTicketCount.getText().toString().equals("0")) {
                    btnUpload.setEnabled(false);
                    btnUpload.setImageResource(R.drawable.ico_disabled_upload);
                    btnRemove.setEnabled(false);
                    btnRemove.setImageResource(R.drawable.ico_disabled_remove);

                } else {
                    btnUpload.setEnabled(true);
                    btnUpload.setImageResource(R.drawable.ico_enabled_upload);
                    btnRemove.setEnabled(true);
                    btnRemove.setImageResource(R.drawable.ico_enabled_remove);

                }
            }
        });

    }
}
