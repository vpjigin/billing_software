/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billingsoftware.utils;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import javax.swing.JTable;

/**
 *
 * @author jigin
 */
public class PrintBill implements Printable{
    int status = 0;
    int startIndex = 0;
    int endIndex = 0; // array of page break line positions.
    
    Font font;
    JTable j;
    String total;
    String date;
    int invId;
    String type;
    
    ArrayList<String[]>l = new ArrayList<>();
    public PrintBill(JTable j,String total,String date,int invId,String type){
        //this.a = a;
        this.j = j;
        this.total = total;
        this.date = date;
        this.invId = invId;
        this.type = type;
        
        int s = j.getRowCount();
        for(int i=0; i<s; i++){
            String da[] = new String[]{j.getValueAt(i, 0).toString(),j.getValueAt(i, 1).toString(),j.getValueAt(i, 2).toString()
                    ,j.getValueAt(i, 3).toString(),j.getValueAt(i, 4).toString()};
            l.add(da);
        }
        
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                 job.print();
            } catch (PrinterException ex) {
             /* The job did not successfully complete */
            }
        }
        
    }
    
    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        double pgHgt = pf.getPaper().getHeight();
        double pgLngth = pf.getPaper().getWidth();
        
        if(l.isEmpty()){
            return NO_SUCH_PAGE;
        }
        
        if(endIndex == l.size() && status == 2){
            //System.out.println("NO SUCH PAGE "+status);
            //status ++;
            return NO_SUCH_PAGE;
        }
        
        status++;
        
        font = new Font("Serif", Font.PLAIN, 10);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        
        int lineHeight = metrics.getHeight();
        
        int tK = lineHeight+20;
        
        setBorder(g, pf, 5);//setting border to the page
        
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         * Since we are drawing text we
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        
        //Setting the header need for psnd due amount
        //setBorder(g, pf, 5);//setting border to the page
        
        //setting address
        String firmName = "MRUTHASANJEEVANI";
        g.drawString(firmName, getCenter(g, pf, firmName), tK);
        tK+=18;
        String socName = "Eco-Aqua shop,Kuruvantheri road,valayam";
        g.drawString(socName, getCenter(g, pf, socName), tK);
        tK+=18;
        String ph = "Ph. 9495727991,9497077078,9846568483";
        g.drawString(ph, getCenter(g, pf, ph), tK);
        tK+=20;
        
        //heading
        font = new Font("Serif", Font.PLAIN, 20);//setting font size as 20
        g.setFont(font);
        
        String heading = type;
        g.drawString(heading, getCenter(g, pf, heading), tK);
        
        
        
        font = new Font("Serif", Font.PLAIN, 10);//setting font size as 10
        g.setFont(font);
        
        g.drawString("Inv.No:"+invId, 15 , tK);
        g.drawString("Date:"+date, 450 , tK);
        //tK += 20;
        //next line
        //int ratioValue[] = getRatio(pf, new int[]{1,1},20);
        //g.drawString("PSN : "+a[0], ratioValue[0], tK);
        //g.drawString("SALA : "+a[1], ratioValue[1], tK);
        
        tK += 20;
        int taW = 15;//Table width
        
        int ra[] = getRatio(pf, new int[]{1,6,1,2,2}, taW);
        g.drawString("Sl", ra[0], tK);
        g.drawString("NAME", ra[1], tK);
        g.drawString("QTY", ra[2], tK);
        g.drawString("UNIT", ra[3], tK);
        g.drawString("TOTAL", ra[4], tK);
        
        
        System.out.println("Line height:"+lineHeight);
        
        double remainingHeight = pgHgt-tK;
        int count = (int) remainingHeight/20;//20->the tK size for each row.
        
        int k = startIndex;
        int eCo = k+count;//end count
        if(l.size() >= eCo){
            endIndex = k+count;
        }
        else{
            endIndex = l.size();
        }
        System.out.println("page index::::::"+pageIndex);
        
        for(int i = startIndex;i<endIndex;i++){
            System.out.println(i);
            tK += 20;
            String[] am = l.get(i);
            g.drawString(String.valueOf(i+1), ra[0], tK);
            g.drawString(am[1], ra[1], tK);
            g.drawString(am[2], ra[2], tK);
            g.drawString(am[3], ra[3], tK);
            g.drawString(am[4], ra[4], tK);
            //g.drawString(am[5], ra[5], tK);
        }
        
        if(l.size() == endIndex){
            font = new Font("Serif", Font.BOLD, 15);
            g.setFont(font);
            g.drawString("Total : ₹"+total, ra[4]-75, tK+20);
        }
        else if(status == 2){
            status = 0;
            startIndex = endIndex;
        }
        
        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
    
    
    
    public int getCenter(Graphics g,PageFormat pf,String text){
        FontMetrics metrics = g.getFontMetrics(font);
        
        int headingSize = metrics.stringWidth(text);
        int hHalf = headingSize/2;
        int paprSize = (int)pf.getPaper().getWidth();
        int paprHalf = paprSize/2;
        
        return paprHalf-hHalf;
    }
    
    public int[] getRatio(PageFormat pf,int[] ratio,int x){
        //x is used for spacing set in the x-direction.
        int[]retRatio = new int[ratio.length];
        if(Integer.signum(x) == -1)
            x=0;
        
        //finding the total ratio
        int totRatio=0;
        for(int i = 0;i<ratio.length;i++){
            totRatio = totRatio+ratio[i];
        }
        
        double pageSize = pf.getPaper().getWidth()-(x*2);
        int totRatioDiv = (int)pageSize/totRatio;
        
        int va[] = new int[ratio.length];
        for(int i=0;i<ratio.length;i++){
            va[i]=totRatioDiv*ratio[i];
        }
        
        for(int i=0;i<ratio.length;i++){
            
            if(i==0){
                retRatio[i] = x;
            }
            else{
                retRatio[i] = retRatio[i-1]+va[i-1];
            }
        }
        
        return retRatio;
    }
    
    public boolean setBorder(Graphics g,PageFormat pf,int borderWeight){
        try {
            int x1 = 5;
            int y1 = 5;
            int x2 = (int)pf.getPaper().getWidth()-5;
            int y2 = (int)pf.getPaper().getHeight()-5;

            g.drawLine(x1, y1, x2, y1);
            g.drawLine(x2, y1, x2, y2);
            g.drawLine(x1, y1, x1, y2);
            g.drawLine(x2, y2, x1, y2);
            return true;
        } catch (Exception e) {
            return false;
        }
        
    }
}
