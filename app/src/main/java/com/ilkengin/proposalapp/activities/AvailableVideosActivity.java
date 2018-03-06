package com.ilkengin.proposalapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ilkengin.proposalapp.R;
import com.ilkengin.proposalapp.adapters.AvailableVideosListAdapter;
import com.ilkengin.proposalapp.items.AvailableVideosListItem;
import com.ilkengin.proposalapp.utils.AvailableVideosUtils;
import com.ilkengin.proposalapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AvailableVideosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView availableVideosListView;
    private ProgressBar progressBar;

    private AvailableVideosListAdapter availableVideosListAdapter;
    private List<AvailableVideosListItem> availableVideos = new ArrayList<>();

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_videos);
        availableVideosListView = (ListView) findViewById(R.id.available_videos_list_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        availableVideosListView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);

        availableVideosListAdapter = new AvailableVideosListAdapter(this,availableVideos);
        availableVideosListView.setAdapter(availableVideosListAdapter);

        availableVideosListView.setOnItemClickListener(this);

        new MyAsynctask().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AvailableVideosListItem item = (AvailableVideosListItem) availableVideosListAdapter.getItem(i);
        Bundle args = new Bundle();
        args.putString(Constants.ARG_FILE_NAME,item.getFileName());
        Intent intent = new Intent(getApplicationContext(),VideoPlayerActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    private class MyAsynctask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            availableVideos = AvailableVideosUtils.getAvailableVideos(sharedPref);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            availableVideosListAdapter.setAvailableVideos(availableVideos);
            availableVideosListAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            availableVideosListView.setVisibility(View.VISIBLE);
        }
    }
}
