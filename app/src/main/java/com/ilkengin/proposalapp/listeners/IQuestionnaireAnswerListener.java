package com.ilkengin.proposalapp.listeners;

/**
 * Created by ilkengin on 26.08.2017.
 */

public interface IQuestionnaireAnswerListener {
    public void onQuestionnarieAnswered(String answer, boolean isCorrect);
    public void onLastQuestionnarieCorrectAnswered();
}
