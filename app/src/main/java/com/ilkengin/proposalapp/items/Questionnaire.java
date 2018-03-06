package com.ilkengin.proposalapp.items;

import java.util.List;

/**
 * Created by ilkengin on 26.08.2017.
 */

public class Questionnaire {
    public String question;
    public String type;
    public List<String> answers;
    public String correctAnswer;
    public String videoName;

    public Questionnaire() {

    }

    public Questionnaire(String question, String type, List<String> answers, String correctAnswer, String videoName) {
        this.question = question;
        this.type = type;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.videoName = videoName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}
