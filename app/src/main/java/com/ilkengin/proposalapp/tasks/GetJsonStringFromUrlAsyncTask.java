package com.ilkengin.proposalapp.tasks;

import android.os.AsyncTask;

import com.ilkengin.proposalapp.listeners.IHTTPRequestListener;
import com.ilkengin.proposalapp.utils.JsonOperations;

import java.io.IOException;


public class GetJsonStringFromUrlAsyncTask extends AsyncTask<String,Void,String> {

    private IHTTPRequestListener httpRequestListener;

    public GetJsonStringFromUrlAsyncTask(IHTTPRequestListener requestListener) {
        this.httpRequestListener = requestListener;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return JsonOperations.getJSONStringFromURL(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null) {
            this.httpRequestListener.onRequestDone(result);
        }
    }
}
