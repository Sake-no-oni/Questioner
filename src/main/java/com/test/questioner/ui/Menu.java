package com.test.questioner.ui;

import com.test.questioner.entities.QuestionsManager;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * The user interface
 *
 * @author ebrius
 */
public class Menu {
    
    QuestionsManager qManager;
    Scanner scanner = new Scanner(System.in);
    
    public Menu(QuestionsManager qManager) {
        this.qManager = qManager;
    }
    
    /**
     * Shows the main menu
     */
    public void show() throws IOException {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
        System.out.println("\t<< Questioner v1.0 >>\n---------------------------------------\n");
        System.out.println("Please choose an action:\n 1. Create a question\n 2. Ask a question\n");
        String userInput = scanner.nextLine();
        if (userInput.equals("1")) {
            questionDialogue();
        } else if (userInput.equals("2"))  {
            answerDialogue();
        } else {
            System.out.println("Please input either 1 or 2");
        }
        System.out.println("Press ENTER to continue");
        scanner.nextLine();
    }
    
    /**
     * The menu for creating the new questions
     */
    private void questionDialogue() {
        System.out.print("\033[H\033[2J");  
        System.out.println("Please add a question according to the pattern:\n[Question]? \"[Answer 1]\" \"[Answer 2]\" [...] \"[Answer n]\"\n");
        String question = scanner.nextLine();
        String result = qManager.newQuestion(question);
        if (result.equals("ok")) {
            System.out.println("\nThe question is added successfully");
        } else {
            System.out.println("An error has occured: " + result);
        }
    }
    
    /**
     * The menu for answering the questions
     */
    private void answerDialogue() {
        System.out.print("\033[H\033[2J");  
        System.out.println("Please input a question");
        String question = scanner.nextLine();
        List<String> answers = qManager.getAnswers(question);
        for (String item: answers) {
            System.out.println("• "  + item);
        }
    }
}
