package com.library;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String question;
    private String answer;
    private List<String> options;

    public Question() {
        options = new ArrayList<>();
    }

    public Question(String question, String answer, List<String> options) {
        this.question = question;
        this.answer = answer;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getOptions() {
        return options;
    }
}
