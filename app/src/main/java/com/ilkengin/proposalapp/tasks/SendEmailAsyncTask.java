package com.ilkengin.proposalapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.ilkengin.proposalapp.utils.GMailSender;

public class SendEmailAsyncTask  extends AsyncTask<String,Void,Void> {

    private static final String TAG = SendEmailAsyncTask.class.getSimpleName();


    private String email;
    private String password;

    public SendEmailAsyncTask(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            GMailSender sender = new GMailSender(this.email, this.password);
            sender.sendMail(params[0], params[1], this.email, params[2]);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
