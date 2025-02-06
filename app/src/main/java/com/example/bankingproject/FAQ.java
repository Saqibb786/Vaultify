package com.example.bankingproject;

public class FAQ {
    private String question;
    private String answer;

    public FAQ(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // Getters
    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
