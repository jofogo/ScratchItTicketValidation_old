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
    ArrayList<String> ticketCodes = new ArrayList<String>();
    private final int ticketCodeLength = 10;

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

    private void userLogout() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                userLogout();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpload = findViewById(R.id.btnUpload);
        btnRemove = findViewById(R.id.btnRemove);
        txtTicketCount = findViewById(R.id.txtTicketCount);
        txtTicketCode = findViewById(R.id.txtTicketCode);
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
                    btnAdd.setBackgroundColor(getResources().getColor(R.color.button_inactive));
                } else {
                    btnAdd.setEnabled(true);
                    btnAdd.setBackgroundColor(getResources().getColor(R.color.button_active));
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
                    btnUpload.setBackgroundColor(getResources().getColor(R.color.button_inactive));
                    btnRemove.setEnabled(false);
                    btnRemove.setBackgroundColor(getResources().getColor(R.color.button_inactive));
                } else {
                    btnUpload.setEnabled(true);
                    btnUpload.setBackgroundColor(getResources().getColor(R.color.button_upload));
                    btnRemove.setEnabled(true);
                    btnRemove.setBackgroundColor(getResources().getColor(R.color.button_remove));
                }
            }
        });

    }
}
