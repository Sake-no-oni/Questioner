package com.test.questioner.entities;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Operates the questions' file storage
 *
 * @author ebrius
 */
public class QuestionStorageHandler {
    
    Gson gson = new Gson();    
    String currentDir = System.getProperty("user.dir");
    
    File storageFile;
    
    public QuestionStorageHandler (String name) {
         storageFile = new File(name);
    }
    
    /**
     * Acquires the questions from the backup (if any) as json and forms the Question class instances
     * 
     * @return a map with question's texts as keys and Question instances as values
     */
    public Map<String, Question> readFromFile() throws FileNotFoundException, IOException {
        Map<String, Question> storage = new HashMap<String, Question>();        
        boolean fileIsOk = false;
        if (storageFile.isFile()) {
            try {
                // Reading the Storage File as Json
                JsonObject storageAsJson = gson.fromJson(new FileReader(storageFile), JsonObject.class);                
                // Converting Entries to Questions                
                for (Map.Entry<String, JsonElement> entry: storageAsJson.entrySet()) {
                    storage.put(entry.getKey(), gson.fromJson(entry.getValue().toString(), Question.class));
                }                
                fileIsOk = true;
            } catch (IllegalArgumentException ex) {
                storageFile.delete();
                Logger.getLogger(QuestionStorageHandler.class.getName()).log(Level.WARNING, "Error while reading questions.json. The file has been deleted: ", ex);
            }
        }        
        if (!fileIsOk) {
            writeToFile(storage);
        }        
        return storage;
    }
    
    /**
     * Writes the questions' storage into the json file
     * 
     * @param map the whole questions' storage
     */
    private void writeToFile (Map<String, Question> map) throws IOException {        
        FileWriter writer = new FileWriter(storageFile);
        gson.toJson(map, writer);
        writer.flush();
        writer.close();    
    }
    
    /**
     * The public wrapper for the writeToFile() for calling it in a separate thread.
     * In case of large 'questions.json' file
     * 
     * @param map the whole questions' storage
     */
    public void doWriteToFile (Map<String, Question> map) {        
        new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     writeToFile(map);
                 } catch (IOException ex) {
                     Logger.getLogger(QuestionStorageHandler.class.getName()).log(Level.SEVERE, "Error while writing to questions.json: ", ex);
                 }
             }
        }).start();
    } 
}
