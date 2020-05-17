package com.test.questioner.entities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the main operations with the questuions
 *
 * @author ebrius
 */
public class QuestionsManager {
    
    private Map<String, Question> questionsStorage;
    private Properties props;    
    QuestionStorageHandler storage;
    
    public QuestionsManager (Properties props) throws FileNotFoundException, IOException {
        this.props = props;
        storage = new QuestionStorageHandler(props.getProperty("resPath") + "/questions.json");
        questionsStorage = storage.readFromFile();
    }
    
    /**
     * Creates a new question
     * 
     * @param newQuestion the new question string from the user. Must correspond the pattern [Question]? "[Answer 1]" "[Answer 2]" [...] "[Answer n]
     * @return execution status or the error description
     */
    public String newQuestion (String newQuestion) {
        String result = new String();        
        try {
            Question question = new Question(newQuestion, Integer.parseInt(props.getProperty("app.qst_size_restr")), Integer.parseInt(props.getProperty("app.ans_size_restr")));            
            questionsStorage.put(question.getQuestion(), question);
            storage.doWriteToFile(questionsStorage);
            result = "ok";
        } catch (Exception ex) {
            result = ex.getMessage();
            Logger.getLogger(QuestionStorageHandler.class.getName()).log(Level.WARNING, "Error while creating the new question: ", ex);
        }        
        return result;
    }
    
    /**
     * Retrieves the answers for the given question
     * 
     * @param question from the user
     * @return answers in a list
     */
    public List<String> getAnswers(String question) {
        List<String> answers = new ArrayList<String>();        
        if (questionsStorage.containsKey(question)) {
            answers = questionsStorage.get(question).getAnswers();
        } else {
            answers.add(props.getProperty("app.default_answer"));
        }        
        return answers;
    }
    
}
