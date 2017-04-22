package com.lmntrx.languagetranslator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText input;
    TextView output;

    TextInputLayout inputLayout;


    public static final int VOLLEY_REQUEST_TIMEOUT = 10000;
    public static final int VOLLEY_REQUEST_RETRIES = 2;
    public static final float VOLLEY_REQUEST_BACKOFF_MULTIPLIER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        input = (EditText) findViewById(R.id.textInEnglishET);
        output = (TextView) findViewById(R.id.translatedTextView);
        inputLayout = (TextInputLayout) findViewById(R.id.textInEnglishInputLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textToTranslate = input.getText().toString();

                final Snackbar snackbar = Snackbar.make(view, "Translating \"" + textToTranslate + "\" to Tamil... Please wait...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null);
                snackbar.show();

                textToTranslate = URLEncoder.encode(textToTranslate);

                String url = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyDi7B2_f2GLZ9kNI0gY3CD8PJbJQtJEIdQ&source=en&target=ta&q=".concat(textToTranslate);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    snackbar.setText("Success");
                                    snackbar.setDuration(Snackbar.LENGTH_SHORT);
                                    String translatedText = response.getJSONObject("data").getJSONArray("translations").getJSONObject(0).getString("translatedText");
                                    output.setText(translatedText);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    snackbar.setText("Something Bad Happened...");
                                    snackbar.setDuration(Snackbar.LENGTH_SHORT);
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                snackbar.setText("Please check your connection...");
                                snackbar.setDuration(Snackbar.LENGTH_SHORT);
                            }
                        }
                );

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        VOLLEY_REQUEST_TIMEOUT,
                        VOLLEY_REQUEST_RETRIES,
                        VOLLEY_REQUEST_BACKOFF_MULTIPLIER));
                Volley.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
