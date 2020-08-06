/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billingsoftware.utils;

import billingsoftware.Settings;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import model.SettingsModel;
import org.json.JSONObject;

/**
 *
 * @author Youtube
 */
public class Data {
    private String PRINTER_TYPE = "PRINTER_TYPE";
    private String PRINTER_NAME = "PRINTER_NAME";
    private String ADDRESS = "ADDRESS";
    
    public Data(){
        if(!createDirIfNot()){
            saveJson("");
        }
    }
    
    public boolean saveSettings(SettingsModel settings){
        saveJson(new Gson().toJson(settings,SettingsModel.class));
        return true;
    }
    
    public SettingsModel getSettings(){
        String data = getJson();
        if(data.isEmpty()) return null;
        
        return new Gson().fromJson(data, SettingsModel.class);
    }
    
    
    
    private void saveJson(String json){
        try{
            String savePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "drive";
            File saveLocation = new File(savePath);
            if(!saveLocation.exists()){
                saveLocation.mkdir();
            }
            
            File myFile = new File(savePath, "data.txt");
            PrintWriter textFileWriter = new PrintWriter(new FileWriter(myFile));

            textFileWriter.write(json);
            textFileWriter.close();
        }catch(Exception e){System.out.println(e.toString());}
    }
    
    private String getJson(){
        try{
            String savePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "drive/data.txt";
            File file = new File(savePath); 
  
            BufferedReader br = new BufferedReader(new FileReader(file)); 

            String st;
            String data = "";
            while ((st = br.readLine()) != null) 
              data += st;
            
            System.out.println(data);
            return data;
        }catch(Exception e){
            System.out.println(e.toString());
            return "";
        }
    }
    
    private boolean createDirIfNot() {
        String savePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "drive";
        File theDir = new File(savePath);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            return false;
        }
        else return true;
    }
}
