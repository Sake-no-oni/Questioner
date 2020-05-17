package com.test.questioner.entities;

import java.util.Arrays;
import java.util.List;

/**
 * The question entity
 *
 * @author ebrius
 */
public class Question {
    
    private String question;
    private List<String> answers;
    
    /**
     * 
     * @param rawString the user input to be parsed
     * @param qstSizeRestr question size restriction
     * @param ansSizeRestr answer size restriction
     */
    public Question(String rawString, Integer qstSizeRestr, Integer ansSizeRestr) {
        
        String question;
        List<String> answers;
        
        // Acquiring the question
        if (rawString.indexOf("?") >= 0) {
            Integer qstEnd = rawString.indexOf("?") + 1;
            question = rawString.substring(0, qstEnd);
            
            // Checking the question's length            
            if (question.length() > qstSizeRestr) {
                throw new IllegalArgumentException("The question is too long, the restriction is " + qstSizeRestr.toString() + " characters");
            }
            
            // Acquiring the answers
            if (question.indexOf("\"") == -1) {
                if (rawString.substring(qstEnd, rawString.length()).length() > 0) {
                    String answersAsStr = rawString.substring(qstEnd, rawString.length());
                    String prepAnswersAsStr = answersAsStr.replaceAll("^\\s*\"", "").replaceAll("\"\\s*$", "");
                    answers = Arrays.asList(prepAnswersAsStr.split("\"\\s*\""));
                    for (String answer: answers) {
                        answer = answer.replaceAll("\"", "");
                    }
                    
                    // Checking the answers' length
                    for (String ans: answers) {
                        if (ans.length() > qstSizeRestr) {
                            throw new IllegalArgumentException("The answer " + ans + " is too long, the restriction is " + ansSizeRestr.toString() + " characters");
                        }
                    }
                    
                    this.question = question;
                    this.answers = answers;
                }
            } else {
                throw new IllegalArgumentException("The input contains no question or it's not on its place");
            }
        } else {
            throw new IllegalArgumentException("The input contains no question");
        }        
    }
    
    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
