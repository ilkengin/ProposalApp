package com.ilkengin.proposalapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.alfredayibonte.questionnaireviewlib.QuestionnaireView;
import com.alfredayibonte.questionnaireviewlib.adapters.RadioListAdapter;
import com.alfredayibonte.questionnaireviewlib.models.Answer;
import com.alfredayibonte.questionnaireviewlib.utils.AnswerType;
import com.ilkengin.proposalapp.utils.Constants;
import com.ilkengin.proposalapp.listeners.IQuestionnaireAnswerListener;
import com.ilkengin.proposalapp.utils.JsonOperations;
import com.ilkengin.proposalapp.utils.Math;
import com.ilkengin.proposalapp.utils.Utils;
import com.ilkengin.proposalapp.items.Questionnaire;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class QuestionnaireViewAdapter implements RadioListAdapter.OnRadioItemClickListener, TextView.OnEditorActionListener {

    private QuestionnaireView questionnaireView;
    private IQuestionnaireAnswerListener questionnaireAnswerListener;
    private Questionnaire currentQuestionnaire = null;

    private List<Questionnaire> questionnaires;

    private SharedPreferences sharedPref;

    public QuestionnaireViewAdapter(Activity activity, QuestionnaireView view,String fileName) {
        this.questionnaireView = view;
        sharedPref = activity.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        setup(activity, fileName);
    }

    public QuestionnaireViewAdapter(Activity activity, QuestionnaireView view,List<Questionnaire> questionnaires) {
        this.questionnaireView = view;
        sharedPref = activity.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
        //setup(activity, fileName);
        this.questionnaires = questionnaires;
    }

    private void setup(Context context,String fileName) {
        int id = context.getResources().getIdentifier(fileName,"raw",context.getPackageName());
        String quesionsString = JsonOperations.getJSONStringFromAssets(id, context);

        try {
            Moshi moshi = new Moshi.Builder().build();
            Type type = Types.newParameterizedType(List.class, Questionnaire.class);
            JsonAdapter<List<Questionnaire>> adapter = moshi.adapter(type);

            questionnaires = adapter.fromJson(quesionsString);
        } catch (IOException e) {
            Log.d(getClass().toString(), e.getLocalizedMessage());
            questionnaires = null;
        }
    }

    public void setQuestionnaireAnswerListener(IQuestionnaireAnswerListener _listener) {
        this.questionnaireAnswerListener = _listener;
    }

    private void setQuestionnarie(final Questionnaire questionnarie) {
        questionnaireView.setQuestion(questionnarie.question);
        if ("radio".equals(questionnarie.type)) {
            questionnaireView.setViewType(AnswerType.RADIO);
            CharSequence[] answers = questionnarie.answers.toArray(new CharSequence[questionnarie.answers.size()]);
            questionnaireView.setAnswers(answers);
            questionnaireView.addRadioItemListener(this);
        } else if ("edittext".equals(questionnarie.type)) {
            questionnaireView.setViewType(AnswerType.EDITTEXT);
            questionnaireView.addOnEditorActionListener(this);
        }
        this.currentQuestionnaire = questionnarie;
    }

    public int getCurrentQuestionIndex() {
        int lastQuestion = sharedPref.getInt(Constants.CURRENT_SECURITY_QUESTIONNAIRE_KEY,0);
        return Math.min(lastQuestion,questionnaires.size()-1);
    }

    public void nextQuestion() {
        int lastQuestion = sharedPref.getInt(Constants.CURRENT_SECURITY_QUESTIONNAIRE_KEY,0);
        int currentQuestionnaire = Math.min(lastQuestion,questionnaires.size()-1);
        setQuestionnarie(questionnaires.get(currentQuestionnaire));
    }

    @Override
    public void onRadioItemClick(List<Answer> list) {
        if (questionnaireAnswerListener != null) {
            for (Answer answer : list) {
                if (answer.isChecked()) {
                    if ("".equals(currentQuestionnaire.correctAnswer) || answer.getAnswer().toLowerCase().equals(currentQuestionnaire.correctAnswer.toLowerCase())) {
                        int currentQuestionnaire = sharedPref.getInt(Constants.CURRENT_SECURITY_QUESTIONNAIRE_KEY,0);
                        sharedPref.edit().putInt(Constants.CURRENT_SECURITY_QUESTIONNAIRE_KEY,++currentQuestionnaire).apply();
                        if (currentQuestionnaire >= questionnaires.size()) {
                            questionnaireAnswerListener.onLastQuestionnarieCorrectAnswered();
                        } else {
                            questionnaireAnswerListener.onQuestionnarieAnswered(answer.getAnswer(), true);
                        }
                    } else {
                        questionnaireAnswerListener.onQuestionnarieAnswered(answer.getAnswer(), false);
                    }
                    return;
                }
            }
            questionnaireAnswerListener.onQuestionnarieAnswered("", false);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER || i == KeyEvent.KEYCODE_ENDCALL) {
            String answer = questionnaireView.getTextFromEditText();
            if ("".equals(currentQuestionnaire.correctAnswer) || answer.toLowerCase().equals(currentQuestionnaire.correctAnswer.toLowerCase())) {
                int currentQuestionnaire = sharedPref.getInt(Constants.CURRENT_SECURITY_QUESTIONNAIRE_KEY,0);
                sharedPref.edit().putInt(Constants.CURRENT_SECURITY_QUESTIONNAIRE_KEY,++currentQuestionnaire).apply();
                if (currentQuestionnaire >= questionnaires.size()) {
                    questionnaireAnswerListener.onLastQuestionnarieCorrectAnswered();
                } else {
                    questionnaireAnswerListener.onQuestionnarieAnswered(answer, true);
                }
            } else {
                questionnaireAnswerListener.onQuestionnarieAnswered(answer, false);
            }
        }
        return false;
    }
}
