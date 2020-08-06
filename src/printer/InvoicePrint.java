package printer;

import billingsoftware.utils.Data;
import java.util.List;
import javax.print.PrintService;
import model.InvoiceModel;
import model.Product;
import model.SettingsModel;

public class InvoicePrint extends PUtils{
    private InvoiceModel im;
    private String ADDRESS;
    
    public InvoicePrint(String printerName,InvoiceModel im) {
        super(printerName);
        
        this.im = im;
        SettingsModel settings = new Data().getSettings();
        ADDRESS = settings.getAddress();
    }
    
    public void print(){
        printCenter();
        printTextMedium();
        printString("INVOICE");
        printLineFeed();
        
        printTextNormal();
        printCenter();
        printString(ADDRESS);
        printLineFeed();
        printLineFeed();
        
        printLeft();
        printString("Invoice# "+im.getId());
        printLineFeed();
        
        printString("Customer: "+im.getCustomerName());
        printLineFeed();
        
        printString("Date: "+im.getStringDate());
        printLineFeed();
        printSeperator();
        printLineFeed();
        
        printLeft();
        printString("Description              Rate  Qty  Amount");
        printLineFeed();
        printSeperator();
        printLineFeed();
        
        List<Product>list = im.getProductList();
        double total = 0;
        for(Product p : list){
            printLeft();
            printString(p.getName());
            printLineFeed();
            
            printRight();
            printString("Qty:"+p.getQty()+"|"+"Amount:"+p.getSellingPrice());
            printLineFeed();
            total += p.getQty()*p.getSellingPrice();
        }
        
        printSeperator();
        printLineFeed();
        printTextMedium();
        printCenter();
        printString("Total:"+total);
        printLineFeed();
        printTextNormal();
        printSeperator();
        printLineFeed();
        
        printTextLarge();
        printCenter();
        printString("Thank you! Visit Again");
        printLineFeed();
        printLineFeed();
        printLineFeed();
        printLineFeed();
        
        
    }

}
