package com.test.questioner;

import com.test.questioner.entities.QuestionsManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author ebrius
 */
public class QuestionerTests {
    
    QuestionsManager qManager;
    Properties props;
    
    public QuestionerTests () throws IOException {
        
        // Loading App Properties        
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream resourceStream = loader.getResourceAsStream("unittest.properties");
        props = new Properties();
        props.load(resourceStream);
        
        // Creating Resources Directory
        String resourcesDirPath = System.getProperty("user.dir") + "/resources";
        File resourcesDir = new File(resourcesDirPath);
        if (!resourcesDir.exists()) {
            resourcesDir.mkdir();
        }
        props.setProperty("resPath", resourcesDirPath);
        
        // Instantiating the Questions Manager
        qManager = new QuestionsManager(props);
    }
    
    @Test
    public void createAndReturnQuestionTest() throws IOException {        
        // Create question
        String testQuestion = "What does this program do? \"Saves the given questions\" \"Answers the saved questions\"";
        qManager.newQuestion(testQuestion);
        
        // Ask question
        List<String> answers = qManager.getAnswers("What does this program do?");
        System.out.println("The answers to the test question: " + answers);
    }        
    
    @Test
    public void tooLongQuestionTest() {
        String fakeQuewstion = StringUtils.repeat("*", Integer.parseInt(props.getProperty("app.qst_size_restr")) + 1);
        String fullFakeQuestion = fakeQuewstion + "?\"**\"";
        String res = qManager.newQuestion(fullFakeQuestion);
        Assertions.assertTrue(res.equals("The question is too long, the restriction is " + props.getProperty("app.qst_size_restr") + " characters"));
        System.out.println("IllegalArgumentException thrown on the oversized question");
    }
    
    @Test
    public void tooLongAnswerTest() {
        String fakeAnswer = StringUtils.repeat("*", Integer.parseInt(props.getProperty("app.ans_size_restr")) + 1);
        String fullFakeQuestion = "**?\"" + fakeAnswer + "\"";
        String res = qManager.newQuestion(fullFakeQuestion);
        Assertions.assertTrue(res.equals("The answer " + fakeAnswer + " is too long, the restriction is " + props.getProperty("app.ans_size_restr") + " characters"));
        System.out.println("IllegalArgumentException thrown on the oversized answer");        
    }
    
    @Test
    public void unknownQuestionTest() {
        List<String> answers = qManager.getAnswers("Some unknown question?");
        Assertions.assertTrue(answers.get(0).equals("the answer to life, universe and everything is 42"));
        System.out.println("The answer for an unknown question is as expected: " + answers.get(0));
    }
}
