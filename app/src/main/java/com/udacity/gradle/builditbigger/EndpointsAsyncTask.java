package com.udacity.gradle.builditbigger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.test.mock.MockContext;

import com.firstsputnik.androidjokelibrary.JokeActivity;
import com.firstsputnik.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by ibalashov on 3/2/2016.
 */
public class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {

    private ProgressDialog pg;
    private static MyApi myApiService = null;
    private Context context;

    public EndpointsAsyncTask(Context context) {
        super();
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        if (!(context instanceof MockContext)) {
            pg = new ProgressDialog(context);
            this.pg.setMessage(context.getString(R.string.joke_loading));
            this.pg.show();
        }
    }

    @Override
    protected String doInBackground(Context... params) {
        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }




        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {

        if (!(context instanceof MockContext)) {
            if (pg.isShowing()) {
                pg.dismiss();
            }
            Intent intent = new Intent(context, JokeActivity.class);
            intent.putExtra("Joke", result);
            context.startActivity(intent);
        }
    }
}