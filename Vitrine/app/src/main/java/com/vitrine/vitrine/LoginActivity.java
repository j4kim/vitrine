package com.vitrine.vitrine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;

    private final Context loginContext= this;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //If user persistence exist, pass it to tabActivity and don't show login
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.contains("userJson"))
        {
            String userJson = settings.getString("userJson", null);
            try {
                User user = new User(userJson);
                Intent intent = new Intent(this, TabActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Attempts to sign in the account specified by the login form.
     */
    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            RequestQueue queue = Volley.newRequestQueue(this);

            String url = getString(R.string.login_url) + "?username=" + username + "&password=" + password;

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                dialog.dismiss();
                                User user = new User(response);

                                // User storage for no-login
                                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("userJson", user.toJson());

                                // Commit the edits!
                                editor.apply();

                                Intent intent = new Intent(loginContext, TabActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);

                            } catch (JSONException e) {
                                dialog.dismiss();
                                mUsernameView.setError(getString(R.string.error_incorrect_credentials));
                                mUsernameView.requestFocus();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(loginContext, "Connection failed", Toast.LENGTH_LONG).show();
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            dialog = ProgressDialog.show(this, "",
                    "Loading. Please wait...", true);
        }
    }
}

