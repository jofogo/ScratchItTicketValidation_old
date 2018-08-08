package sitv.combiz.com.scratchitticketvalidation;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ValidationActivity extends AppCompatActivity {
    ImageButton btnAdd;
    ImageButton btnUpload;
    ImageButton btnTorch;
    EditText txtTicketCount;
    EditText txtTicketCode;
    ImageButton btnMenu;
    ImageButton btnAbout;
    ImageButton btnSettings;
    ImageButton btnLogout;
    Boolean menuHidden = true;
    Boolean torchToggled = false;
    Button btnTicketCount;

    private DecoratedBarcodeView tViewScanner;
    private String lastTicketCode="";
    private DefaultDecoderFactory defaultDecoderFactory;



    ArrayList<String> ticketCodes = new ArrayList<String>();
    private final int ticketCodeLength = 22;

    private void playBeep(int status) {
        MediaPlayer mediaPlayer = new MediaPlayer();

        if (status == 0) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep_ok);

        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep_error);

        }

        try {
            mediaPlayer.start();
        } catch (Exception e) {
            mediaPlayer.release();
        }

    }



    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText().equals(null) || result.getText().equals(lastTicketCode)) {
                return;
            }
            lastTicketCode = result.getText();
            Log.i("SCANRESULT", "" + lastTicketCode);
            addTicket(lastTicketCode);
            //tViewScanner.setStatusText(lastTicketCode);
            //txtTicketCode.setText(lastTicketCode);

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

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

    //Shows the top menu
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

    //Hides the top menu
    private void menuHide(){
        int translationYBy = -100;
        btnAbout.setVisibility(View.INVISIBLE);
        btnAbout.setTranslationY(translationYBy);
        btnSettings.setVisibility(View.INVISIBLE);
        btnSettings.setTranslationY(translationYBy);
        btnLogout.setVisibility(View.INVISIBLE);
        btnLogout.setTranslationY(translationYBy);
    }

    //Formats the text for the Ticket Count button
    private void setBtnTicketCountText(int count) {
        String btnText = "";
        try {
            btnText = String.format("%03d", count);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            btnText = String.format("%03d", 0);
        }
        btnTicketCount.setText(btnText);
    }

    //Add button pressed (manually add a ticketcode):
    //1. Check if null text is being added
    //2. call addTicket if Ticket Code text field is not null
    public void ticketAdd(View view) {
        String ticketCode = txtTicketCode.getText().toString();
        if (ticketCode.equals("")) {
            Toast.makeText(this, "Ticket code is empty!", Toast.LENGTH_SHORT).show();
        } else {
            addTicket(ticketCode);
        }
    }


    //Add a ticket:
    //1. Allows to manually add a Ticket Code if the Ticket Code text-field is populated;
    // - Ticket code should not be a duplicate
    //2. Automatically adds a ticket code if added using the scanner
    private void addTicket(String ticketCode) {
        if (ticketCode.length() != ticketCodeLength) {
            playBeep(1);
            Toast.makeText(this, "Ticket code should be " +ticketCodeLength+ " digits!", Toast.LENGTH_SHORT).show();
        } else if (ticketCodes.contains(ticketCode)) {
            playBeep(1);
            Toast.makeText(this, "Ticket code was already used!", Toast.LENGTH_SHORT).show();
        } else {
            ticketCodes.add(ticketCode);
            playBeep(0);
            try {
                Thread.sleep(75);

            } catch (InterruptedException ie) {

            }
            playBeep(0);
            Toast.makeText(this, "Ticket " + ticketCode + " was added.", Toast.LENGTH_SHORT).show();
            setBtnTicketCountText(ticketCodes.size());
            //txtTicketCount.setText("" + ticketCodes.size());
            txtTicketCode.setText("");
        }

    }

    //Torch handler
    public void toggleTorch (View view) {

        if (torchToggled) {
            torchOff();
        } else {
            torchOn();
        }
    }

    //Turn off the phone's torch
    private void torchOff() {
            tViewScanner.setTorchOff();
            torchToggled = false;
            btnTorch.setImageResource(R.drawable.ico_disabled_torch);

    }

    //Turn on the phone's torch
    private void torchOn() {
            tViewScanner.setTorchOn();
            torchToggled = true;
            btnTorch.setImageResource(R.drawable.ico_enabled_torch);

    }

    //Remove all saved Ticket Codes (clears the array)
    public void ticketRemoveAll(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Remove all tickets")
                .setMessage("Do you want to delete all saved tickets?")
                .setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ticketCodes.clear();
                                setBtnTicketCountText(0);
                                //btnTicketCount.setText("0");
                                //txtTicketCount.setText("0");
                                lastTicketCode = "";
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


    public void ticketList(View view) {
        final CharSequence[] arrayTickets = ticketCodes.toArray(new CharSequence[ticketCodes.size()]);

        final ArrayList<Integer> listSelected = new ArrayList<Integer>();
        new AlertDialog.Builder(this)
                .setTitle("Remove saved tickets")
                .setMultiChoiceItems(arrayTickets, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b) {
                                    listSelected.add(i);
                                } else if (listSelected.contains(i)) {
                                    listSelected.remove(Integer.valueOf(i));
                                }
                            }
                        })
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for ( int ctr = 0; ctr < listSelected.size(); ctr ++) {
                            ticketCodes.remove(arrayTickets[listSelected.get(ctr)]);
                        }
                        setBtnTicketCountText(ticketCodes.size());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
        listSelected.clear();

    }


    public void ticketUpload(View view) {

    }

    //Logout sequence - Moves the Activity back to the login (MainActivity)
    private void logout() {
        Intent intent = new Intent(ValidationActivity.super.getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //User logout:
    // 1. Logs out automatically if Ticket Count = 0
    // 2. Logs out after pop-up confirmation if Ticket Count > 0
    public void userLogout(View view) {
        if (btnTicketCount.getText().toString().equals("0")) {
       // if (txtTicketCount.getText().toString().equals("0")) {
            logout();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    logout();
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

    }

    //Initiate logout sequence when hardware Back button is pressed
    @Override
    public void onBackPressed() {
        userLogout(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tViewScanner.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tViewScanner.pause();
    }

    public void pause (View view) {
        tViewScanner.pause();
    }

    public void resume (View view) {
        tViewScanner.resume();
    }

    public void triggerScan(View view) {
        tViewScanner.decodeSingle(callback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        //ValidationActivity main objects
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnUpload = (ImageButton) findViewById(R.id.btnUpload);
        btnTorch = (ImageButton) findViewById(R.id.btnTorch);
        txtTicketCount = (EditText) findViewById(R.id.txtTicketCount);
        txtTicketCode = (EditText) findViewById(R.id.txtTicketCode);
        btnTicketCount = (Button) findViewById(R.id.btnTicketCount);
        //Top menu buttons
        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnAbout = (ImageButton) findViewById(R.id.btnAbout);
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);


        //Barcode scanner
        tViewScanner = (DecoratedBarcodeView) findViewById(R.id.tViewScanner);
        Collection<BarcodeFormat> codeFormats = Arrays.asList(BarcodeFormat.PDF_417, BarcodeFormat.QR_CODE);
        //tViewScanner.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(codeFormats));

        tViewScanner.decodeContinuous(callback);

        //Hide the top menu initially
        menuHide();

        //Initialize ValidationActivity button states
        btnAdd.setEnabled(false);
        btnUpload.setEnabled(false);

        //Listen for Ticket Code field changes
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

        //Listen for Ticket Count field changes
        btnTicketCount.addTextChangedListener(new TextWatcher() {


        //txtTicketCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (btnTicketCount.getText().toString().equals("000")) {
            //    if (txtTicketCount.getText().toString().equals("0")) {
                    btnUpload.setEnabled(false);
                    btnUpload.setImageResource(R.drawable.ico_disabled_upload);
                    btnTicketCount.setEnabled(false);

                } else {
                    btnUpload.setEnabled(true);
                    btnUpload.setImageResource(R.drawable.ico_enabled_upload);

                    btnTicketCount.setEnabled(true);

                }
            }
        });

    }
}
