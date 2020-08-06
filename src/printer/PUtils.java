package printer;

import java.util.HashMap;
import java.util.Iterator;

public class PUtils {
    private PrinterService printerService;
    private String printerName;
    
    public PUtils(String printerName){
        this.printerName = printerName;
        printerService = new PrinterService();
    }
    /**
     * bytes which is useful for the formatting of the texts*/
    public static byte[] central = {0x1B, 0x61, 1};
    public static byte[] left = {0x1B, 0x61, 0};
    public static byte[] right = {0x1B, 0x61, 2};
    public static byte[] lineFeed = {0x0A};
    public static byte[] escape = {0x1B};
    public static byte[] textSizeNormal = {0x1D,0x21,0x00};
    public static byte[] textSizeMedium = {0x1D,0x21,0x01};
    public static byte[] textSizeLarge = {0x1D,0x21,0x02};
    public static byte[] initialze = {0x1B,0x40};
    public static byte[] seperatorLine = "------------------------------------------".getBytes();

    public void printLineFeed(){
        printerService.printBytes(printerName, lineFeed);
    }
    
    public void printCenter(){
        printerService.printBytes(printerName, central);
    }
    
    public void printLeft(){
        printerService.printBytes(printerName, left);
    }
    
    public void printRight(){
        printerService.printBytes(printerName, right);
    }
    
    public void printSeperator(){
        printerService.printBytes(printerName, seperatorLine);
    }
    
    public void printTextNormal(){
        printerService.printBytes(printerName, textSizeNormal);
    }
    
    public void printTextMedium(){
        printerService.printBytes(printerName, textSizeMedium);
    }
    
    public void printTextLarge(){
        printerService.printBytes(printerName, textSizeLarge);
    }
    
    public void printExcape(){
        printerService.printBytes(printerName, escape);
    }
    
    public void printInitialize(){
        printerService.printBytes(printerName, initialze);
    }
    
    public void printString(String text){
        printerService.printString(printerName, text);
    }
    
    
    
    

}
