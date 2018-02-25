package com.urbannightdev.cardiopp.activity;

import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urbannightdev.cardiopp.R;
import com.urbannightdev.cardiopp.helper.AmbilToken;
import com.urbannightdev.cardiopp.helper.SQLiteHandler;
import com.urbannightdev.cardiopp.helper.UserLoggedManager;
import com.urbannightdev.cardiopp.model.UserInfoTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    @BindView(R.id.tiet_nameInput) EditText inputNama;
    @BindView(R.id.tiet_usernameInput) EditText inputUsername;
    @BindView(R.id.tiet_emailInput) EditText inputEmail;
    @BindView(R.id.tiet_passwordInput) EditText inputPassword;
    @BindView(R.id.tiet_noHpInput) EditText inputHandphone;
    @BindView(R.id.etToken) EditText edittextToken;
    @BindView(R.id.btnSubmitOTP) Button btnSubmitOTP;

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

    String tokenmu;

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

        btnSubmitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tokenmu = edittextToken.getText().toString().trim();
                Location location = null;
                new verifyOTP().execute(location);
            }
        });
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

    public class verifyOTP extends AsyncTask<Location, Integer, Void> {

        @Override
        protected Void doInBackground(Location... locations) {

            String tokensaya = "";

            OkHttpClient clientToken = new OkHttpClient();

            MediaType mediaTypeToken = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody bodyToken = RequestBody.create(mediaTypeToken, "grant_type=client_credentials");
            Request requestToken = new Request.Builder()
                    .url("https://api.mainapi.net/token")
                    .post(bodyToken)
                    .addHeader("Authorization", "Basic RWQwTDZCOVJPcHdTU242NnZWblVzZGo5MGFRYTpfVEx2TmpMRVBORVQwVjRyeWR4VHlFQm5ZNXNh") // get token
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "0803bf9d-0182-42a6-8a9b-c26113190a91")
                    .build();

            try {
                Response response = clientToken.newCall(requestToken).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                tokensaya = jsonObject.getString("access_token");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }



            String status = "";


            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n  \"otpstr\": \"" + tokenmu + "\",\r\n  \"digit\": 4,\r\n  \"maxAttempt\": 3,\r\n  \"expireIn\": 1000\r\n}"); // otpstr adalah angka khsusus, digit adalah digit anga khusus, maxAttempt sama kayak sebelumnya, expireIn sama kayak sebelumnya
            Request request = new Request.Builder()
                    .url("https://api.mainapi.net/smsotp/1.0.1/otp/keyBebasRahasia/verifications") // ganti "keyBebasRahasia" tapi harus sama dengan yang awal
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + tokensaya) // perbarui token
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "11dfb748-59ce-4195-9c83-228a1a77be5f")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                JSONObject object = new JSONObject(response.body().string());
                status = object.getString("status");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            showToast("sukses");
            RegistrationActivity.this.finish();

            return null;
        }
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
                        Location location = null;
                        new SMSOTP().execute(location);
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

    public class SMSOTP extends AsyncTask<Location, Integer, Void> {

        @Override
        protected Void doInBackground(Location... locations) {

            String tokensaya = "";

            OkHttpClient clientToken = new OkHttpClient();

            MediaType mediaTypeToken = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody bodyToken = RequestBody.create(mediaTypeToken, "grant_type=client_credentials");
            Request requestToken = new Request.Builder()
                    .url("https://api.mainapi.net/token")
                    .post(bodyToken)
                    .addHeader("Authorization", "Basic RWQwTDZCOVJPcHdTU242NnZWblVzZGo5MGFRYTpfVEx2TmpMRVBORVQwVjRyeWR4VHlFQm5ZNXNh") // get token
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "0803bf9d-0182-42a6-8a9b-c26113190a91")
                    .build();

            try {
                Response response = clientToken.newCall(requestToken).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                tokensaya = jsonObject.getString("access_token");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }



            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n  \"maxAttempt\": 3,\r\n  \"phoneNum\": \"+6285871547208\",\r\n  \"expireIn\": 1000,\r\n  \"digit\": 4\r\n}"); // max attempt diisi, phoneNum isi nomor tujuan, expireIn diisi, digit antara 4-10.
            Request request = new Request.Builder()
                    .url("https://api.mainapi.net/smsotp/1.0.1/otp/keyBebasRahasia") // ganti "keyBebasRahasia" tapi harus sama dengan yang akhir
                    .put(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Bearer " + tokensaya) // perbarui token
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "649af415-100a-42dc-ba50-61c5745a5c3e")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
