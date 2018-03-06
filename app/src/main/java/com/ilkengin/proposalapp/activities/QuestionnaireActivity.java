package com.ilkengin.proposalapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.alfredayibonte.questionnaireviewlib.QuestionnaireView;
import com.ilkengin.proposalapp.R;
import com.ilkengin.proposalapp.adapters.QuestionnaireViewAdapter;
import com.ilkengin.proposalapp.items.AvailableVideosListItem;
import com.ilkengin.proposalapp.items.Questionnaire;
import com.ilkengin.proposalapp.listeners.IHTTPRequestListener;
import com.ilkengin.proposalapp.listeners.IQuestionnaireAnswerListener;
import com.ilkengin.proposalapp.tasks.GetJsonStringFromUrlAsyncTask;
import com.ilkengin.proposalapp.utils.AvailableVideosUtils;
import com.ilkengin.proposalapp.utils.Constants;
import com.ilkengin.proposalapp.utils.JsonOperations;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireActivity extends AppCompatActivity implements IQuestionnaireAnswerListener, EasyVideoCallback, IHTTPRequestListener {

    private static final String TAG = QuestionnaireActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private QuestionnaireView questionnaireView;
    private EasyVideoPlayer videoPlayer;

    private List<Questionnaire> questionnaireList = new ArrayList<>();
    private QuestionnaireViewAdapter questionnaireViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        Log.d(TAG,"onCreate");

        progressBar = (ProgressBar) findViewById(R.id.activity_questionnaire_progress_bar);
        videoPlayer = (EasyVideoPlayer) findViewById(R.id.activity_questionnaire_video_player);
        questionnaireView = (QuestionnaireView) findViewById(R.id.activity_questionnaire_questionnaire_view);

        progressBar.setVisibility(View.VISIBLE);
        videoPlayer.setVisibility(View.GONE);
        questionnaireView.setVisibility(View.GONE);

        Bundle args = getIntent().getExtras();
        String fileName = args.getString(Constants.ARG_FILE_NAME,"");
        String url = args.getString(Constants.ARG_URL,"");

        videoPlayer.setCallback(this);

        if(fileName.isEmpty()) {
            new GetJsonStringFromUrlAsyncTask(this).execute(url);
        } else {
            questionnaireList = JsonOperations.getItemsFromFile(this,fileName,Questionnaire.class);
            questionnaireViewAdapter = new QuestionnaireViewAdapter(this, questionnaireView,questionnaireList);
            init();
        }
    }

    private void init() {
        questionnaireViewAdapter.setQuestionnaireAnswerListener(this);

        questionnaireViewAdapter.nextQuestion();
        String fileName = questionnaireList.get(questionnaireViewAdapter.getCurrentQuestionIndex()).getVideoName();
        if(fileName == null || fileName.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    videoPlayer.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    questionnaireView.setVisibility(View.VISIBLE);
                }
            }, 1000);
        } else {
            String filePath = "android.resource://" + getPackageName() + "/" + getResources().getIdentifier(fileName,"raw",getPackageName());
            videoPlayer.setSource(Uri.parse(filePath));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    videoPlayer.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    questionnaireView.setVisibility(View.GONE);
                }
            }, 1000);
        }

    }

    @Override
    public void onQuestionnarieAnswered(String answer, boolean isCorrect) {
        questionnaireView.setVisibility(View.GONE);
        videoPlayer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        if (isCorrect) {
            Alerter.create(this)
                    .setTitle("Tebrikler")
                    .setText("\"" + answer + "\" cevabınız doğru!")
                    .setBackgroundColorRes(R.color.colorPrimary)
                    .setDuration(1000)
                    .setOnHideListener(new OnHideAlertListener() {
                        @Override
                        public void onHide() {
                            questionnaireViewAdapter.nextQuestion();
                            String fileName = questionnaireList.get(questionnaireViewAdapter.getCurrentQuestionIndex()).getVideoName();
                            if(fileName == null || fileName.isEmpty()) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        videoPlayer.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.GONE);
                                        questionnaireView.setVisibility(View.VISIBLE);
                                    }
                                }, 1000);
                            } else {
                                String filePath = "android.resource://" + getPackageName() + "/" + getResources().getIdentifier(fileName,"raw",getPackageName());
                                videoPlayer.setSource(Uri.parse(filePath));

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        videoPlayer.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        questionnaireView.setVisibility(View.GONE);
                                    }
                                }, 1000);
                            }

                            questionnaireView.setVisibility(View.GONE);
                            videoPlayer.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    })
                    .show();
        } else {
            Alerter.create(this)
                    .setTitle("Maalesef")
                    .setText("\"" + answer + "\" cevabınız yanlış!")
                    .setBackgroundColorRes(R.color.colorPrimary)
                    .setOnHideListener(new OnHideAlertListener() {
                        @Override
                        public void onHide() {
                            questionnaireView.setVisibility(View.VISIBLE);
                            videoPlayer.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    })
                    .setDuration(1000)
                    .show();
        }
    }

    @Override
    public void onLastQuestionnarieCorrectAnswered() {
        questionnaireView.setVisibility(View.GONE);
        videoPlayer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Alerter.create(this)
                .setTitle("Tebrikler")
                .setText("Bütün soruları doğru cevapladınız!")
                .setBackgroundColorRes(R.color.colorPrimary)
                .setDuration(1000)
                .setOnHideListener(new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        //startActivity(new Intent(QuestionnaireActivity.this,StoryActivity.class));
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        super.onBackPressed();
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        Questionnaire questionnaire = questionnaireList.get(questionnaireViewAdapter.getCurrentQuestionIndex());
        AvailableVideosUtils.addToAvailableVideos(getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE),new AvailableVideosListItem(questionnaire.getVideoName(),"Question " + (questionnaireViewAdapter.getCurrentQuestionIndex() + 1) ));
        questionnaireView.setVisibility(View.VISIBLE);
        videoPlayer.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onRequestDone(String jsonString) {
        questionnaireList = JsonOperations.getItemsFromString(jsonString, Questionnaire.class);
        questionnaireViewAdapter = new QuestionnaireViewAdapter(this, questionnaireView,questionnaireList);
        init();
    }
}
