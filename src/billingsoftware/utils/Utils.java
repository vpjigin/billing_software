/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billingsoftware.utils;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.naming.Context;
import javax.swing.JOptionPane;

/**
 *
 * @author jigin
 */
public class Utils {
    
    public static void showMessage(Component context,int i){
        if(i>0)
            JOptionPane.showMessageDialog(context, "Saved");
        else
            JOptionPane.showMessageDialog(context, "Failed!");
    }
    
    public static String getNowDateTime(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        Calendar calobj = Calendar.getInstance();
        return df.format(calobj.getTime());
    }
    
    
}
