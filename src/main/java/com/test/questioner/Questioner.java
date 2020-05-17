package com.test.questioner;

import com.test.questioner.entities.QuestionsManager;
import com.test.questioner.ui.Menu;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;

/**
 * Questioner v1.0
 *
 * @author ebrius
 */
public class Questioner {

    public static void main(String[] args) throws IOException {
        
        // Loading App Properties        
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream resourceStream = loader.getResourceAsStream("app.properties");
        Properties props = new Properties();
        props.load(resourceStream);
        
        // Creating Resources Directory
        String resourcesDirPath = System.getProperty("user.dir") + "/resources";
        File resourcesDir = new File(resourcesDirPath);
        if (!resourcesDir.exists()) {
            resourcesDir.mkdir();
        }
        props.setProperty("resPath", resourcesDirPath);
        
        // Initizlizing the Logger
        LogManager.getLogManager().readConfiguration(loader.getResourceAsStream("logging.properties"));
        
        // Instantiating the Questions Manager
        QuestionsManager qManager = new QuestionsManager(props);
        
        // Instantiating the User Interface
        Menu menu = new Menu(qManager);
        
        while (true) {            
            menu.show();
        }
    }    
}
