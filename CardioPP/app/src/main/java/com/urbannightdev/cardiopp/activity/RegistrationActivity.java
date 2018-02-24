package com.urbannightdev.cardiopp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urbannightdev.cardiopp.R;
import com.urbannightdev.cardiopp.helper.SQLiteHandler;
import com.urbannightdev.cardiopp.helper.UserLoggedManager;
import com.urbannightdev.cardiopp.model.UserInfoTable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    @BindView(R.id.tiet_nameInput) EditText inputNama;
    @BindView(R.id.tiet_usernameInput) EditText inputUsername;
    @BindView(R.id.tiet_emailInput) EditText inputEmail;
    @BindView(R.id.tiet_passwordInput) EditText inputPassword;
    @BindView(R.id.tiet_noHpInput) EditText inputHandphone;

    @BindView(R.id.btnCreateAccount) Button btnRegister;

    private String sNama;
    private String sUsername;
    private String sEmail;
    private String sPassword;
    private String sHandphone;
    private String sPoint;
    private String sSaldo;

    private SQLiteHandler db;

    private FirebaseAuth mAuth;
    DatabaseReference databaseUser;

    private UserLoggedManager userLoggedManager;

    private ActionBar aksibar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.redkuat));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(myToolbar);

        ButterKnife.bind(this);


        aksibar = RegistrationActivity.this.getSupportActionBar();
        assert aksibar != null;
        aksibar.setDisplayHomeAsUpEnabled(true);

        generateDataForm();

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        initializeListener();

        databaseUser = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        userLoggedManager = new UserLoggedManager(getApplicationContext());

//        dialog = new SpotsDialog(RegistrationActivity.this, "Loading");
//        dialog.setCancelable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                RegistrationActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void generateDataForm() {
        sNama = inputNama.getText().toString().trim();
        sUsername = inputUsername.getText().toString().trim();
        sEmail = inputEmail.getText().toString().trim();
        sPassword = inputPassword.getText().toString().trim();
        sHandphone = inputHandphone.getText().toString().trim();
        sPoint = "0";
        sSaldo = "15000";
    }

    private void initializeListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateDataForm();
                if (!sNama.isEmpty() && !sUsername.isEmpty()
                        && !sEmail.isEmpty() && !sPassword.isEmpty()
                        && !sHandphone.isEmpty()) {
                    if (isEmailValid(sEmail)) {
                        registerUser();
                        finish();
                    } else {
                        showToast("email tidak valid");
                    }
                } else {
                    showToast("jangan kosong");
                }
            }
        });
    }

    private void registerUser() {
//        encryptPassword();
        createAuthenticationFirebase();
        createDatabaseFirebase();
        saveUserToSqliteDb();
        showToast("sukses");
    }

    private void saveUserToSqliteDb() {
        db.addUser(sUsername,sNama,sEmail,sHandphone,sPoint, sSaldo);
    }

    private void createDatabaseFirebase() {
        String id = databaseUser.push().getKey();
        UserInfoTable user = new UserInfoTable(sNama, sUsername, sEmail, sPassword, sHandphone, sPoint, sSaldo);
        String childEmail = removeNonAlphabetCharacterFromString(sEmail);
        try {
            databaseUser.child(childEmail).setValue(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String removeNonAlphabetCharacterFromString(String word) {
        word = word.replace(".","");
        word = word.replace("@","");
        return word;
    }

    private void createAuthenticationFirebase() {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void encryptPassword() {
        Log.d(TAG,"Encrypting Password");
        String mypass = md5(sPassword);
        String mypass1 = md5(mypass);
        sPassword = mypass1;
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * method is used for checking valid email id format.
     * source : https://stackoverflow.com/questions/6119722/how-to-check-edittexts-text-is-email-address-or-not
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * method ini untuk menampilkan toast agar pemanggilan lebih sederhana
     * @param s pesan
     */
    private void showToast(String s) {
        Toast.makeText(RegistrationActivity.this,s,Toast.LENGTH_LONG).show();
    }
}
