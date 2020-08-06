/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billingsoftware.db;

import billingsoftware.utils.BillModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.List;
import javax.swing.JFrame;
import model.Product;
import model.Purchase;
import model.PurchaseItems;
import model.Return;

/**
 *
 * @author jigin
 */
public class Db {
    static Db db;
    static Connection conn;
    private Db(){
        
    }
    
    public static Db getObj(){
        if(db == null){
            initConnection();
            return new Db();
        }
        return db;
    }
    
    private static void initConnection(){
        try{
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            //conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billing?useSSL=false","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/billing?useSSL=false&allowPublicKeyRetrieval=true","root","root");
            System.out.println("connected");
        }catch(ClassNotFoundException | SQLException e)  {
            System.out.println("disconnected");
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    //Category ------------------------------------------------------------
    public int insertIntoCategory(String name){
        String Q = "insert into category (name) value('"+name+"')";
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            p.execute();
            return 1;
        }catch(Exception e){
            return 0;
        }
    }
    //update category
    public int updateCategory(String name,String id){
        String Q = "update category set name = '"+name+"' where id = '"+id+"'";
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            p.execute();
            return 1;
        }catch(Exception e){
            return 0;
        }
    }
    
    //getting all datas from database
    public List<String[]>getAllCategory(){
        String Q = "select id,name from category";
        List<String[]>list = new ArrayList<>();
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r= p.executeQuery();
            
            if(r != null){
                while(r.next()){
                    list.add(new String[]{r.getString(1),r.getString(2)});
                }
            }
            return list;
        }catch(Exception e){
            System.out.println(e.toString());
            return null;
        }
        
    }
    
    //---------------------------------------------------------------
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //products ------------------------------------------------------
    public int insertProducts(String name,int catId,double price,double purchasePrice){
        String Q = "insert into products(name,categoryId,price,stock,purchasePrice)"
                + "values('"+name+"','"+catId+"','"+price+"',0,"+purchasePrice+")";
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            p.execute();
            return 1;
        }catch(Exception e){
            System.out.println(e.toString());
            return 0;
        }
    }
    public int updateProducts(String name,int catId,double price,String id,double purchasePrice){
        String Q = "update products set name = '"+name+"',categoryId = '"+catId+"',"
                + "price = '"+price+"',purchasePrice='"+purchasePrice+"' where id = '"+id+"'";
        
        System.out.println(Q);
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            p.execute();
            return 1;
        }catch(Exception e){
            return 0;
        }
    }
    
    //get all products
    public List<String[]>getAllProducts(){
        String Q = "select p.name,c.name,p.price,p.id,p.purchasePrice from products as "
                + "p inner join category as c on p.categoryId = c.id order by p.name asc";
        
        List<String[]>l = new ArrayList<>();
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            
            if(r != null){
                while(r.next()){
                    l.add(new String[]{r.getString(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5)});
                }
            }
            return l;
        }catch(Exception e){
            return null;
        }
    }
    
    public List<Product> getAllProductsModel(){
        String Q = "select p.name,c.name,p.price,p.id,p.purchasePrice from products as "
                + "p inner join category as c on p.categoryId = c.id order by p.name asc";
      
        List<Product>proList = new ArrayList<>();
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            
            if(r != null){
                while(r.next()){
                    Product pm = new Product();
                    pm.setName(r.getString(0));
                    pm.setCategoryName(r.getString(1));
                    pm.setSellingPrice(r.getDouble(2));
                    pm.setId(r.getInt(3));
                    pm.setPurchasePrice(r.getDouble(4));
                    proList.add(pm);
                }
            }
            return proList;
        }catch(Exception e){
            return proList;
        }
    }
    
    public List<String[]>getAllProductsByCatId(String id){
        String Q = "select p.name,c.name,p.price,p.id from products as p inner join category as "
                + "c on p.categoryId = c.id where c.id = '"+id+"' order by c.name desc";
        
        System.out.println(Q);
        
        List<String[]>l = new ArrayList<>();
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            
            if(r != null){
                while(r.next()){
                    l.add(new String[]{r.getString(1),r.getString(2),r.getString(3),r.getString(4)});
                }
            }
            return l;
        }catch(Exception e){
            return null;
        }
    }
    public String[] getProById(String id){
        String Q = " select name,categoryId,price,stock,purchasePrice from products where id = '"+id+"'";
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                r.next();
                return new String[]{r.getString(1),r.getString(2),r.getString(3),
                    r.getString(4),r.getString(5)};
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }
    
    //--------------------------------------------------------------------
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //billing -----------------------------------------------------------
    public int insertBill(List<String[]>list,String date,String custId){
        
        //insert bill into database
        String Q = "insert into bills(date,custId)values('"+date+"','"+custId+"')";
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            p.execute(Q);
        }catch(Exception e){
            System.out.println("Exception");
            System.out.println(e.toString());
            return 0;
        }
        
        //getting the last inserted bill id
        int billId = 0;
        String Q1 = "select id from bills order by id desc limit 1";
        try{
            PreparedStatement p1 = conn.prepareStatement(Q1);
            ResultSet r1 = p1.executeQuery();
            if(r1 != null){
                r1.next();
                billId = r1.getInt(1);
            }
        }catch(Exception e){
            System.out.println("Exception");
            System.out.println(e.toString());
            return 0;
        }
        
        //inserting the products into database
        int s = list.size();
        try{
            for(int i=0;i<s;i++){
                String d[] = list.get(i);
                String Q2 = "insert into billedItem (name,price,billId,qty,purchasePrice)"
                        + "values('"+d[0]+"','"+d[2]+"','"+billId+"','"+d[1]+"',"+d[5]+")";
                PreparedStatement p = conn.prepareStatement(Q2);
                p.execute();
                
                //decreasing the stock from products table
                double qty = Double.parseDouble(d[1]);
                int proId = Integer.parseInt(d[4]);
                
                String STOCK_QUERY = "update products set stock = stock-"+qty+" where id = '"+proId+"'";
                PreparedStatement sp = conn.prepareStatement(STOCK_QUERY);
                sp.execute();
                
            }
        }catch(Exception e){
            System.out.println("Exception6666");
            System.out.println(e.toString());
            return 0;
        }
        
        return billId;
    }
    
    //total bills
    public int totalBills(){
        String Q = "select count(id) from bills";
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                r.next();
                return r.getInt(1);
            }
        }catch(Exception e){
            return 0;
        }
        return 0;
    }
    
    //view all bills 
    public List<String[]>viewAllBills(String from,String to){
        String Q = "select b.id,DATE_FORMAT(b.date, '%M %d %Y'),c.name,c.mobile from bills as b inner join customer as c on b.custId = "
        + "c.id where b.date between '"+from+"' and '"+to+"' order by b.date desc";
        
        List<String[]>billList = new ArrayList<>();
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while (r.next()) {                    
                    billList.add(new String[]{r.getString(1),r.getString(2),r.getString(3),r.getString(4)});
                }
            }
        }catch(Exception e){
            return null;
        }
        return billList;
    }
    
    public BillModel getSingleBill(String invId){
        //bill
        String Q = "select b.date,c.name from bills as b inner join customer as c on b.custId"
                + " = c.id where b.id = '"+invId+"'";
        BillModel b = new BillModel();
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                r.next();
                String []d = new String[]{r.getString(1),r.getString(2)};
                b.setData(d);
            }
        }catch(Exception e){
            return null;
        }
        
        
        //products
        String Q1 = "select name,price,qty from billedItem where billId = '"+invId+"'";
        
        try{
            PreparedStatement p = conn.prepareStatement(Q1);
            ResultSet r = p.executeQuery();
            List<String[]>proList = new ArrayList<>();
            if(r != null){
                while(r.next()){
                    proList.add(new String[]{r.getString(1),r.getString(2),r.getString(3)});
                }
                b.setProList(proList);
            }
        }catch(Exception e){
            return null;
        }
        return b;
    }
    
    //getting all invoice
    public List<String>getAllInv(){
        String Q = "select id from bills";
        List<String>invList = new ArrayList<>();
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    invList.add(r.getString(1));
                }
            }
            
            return invList;
        }catch(Exception e){
            return null;
        }
    }
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    //RETURN -----------------------------------------------------------
    public int insertReturn(List<String[]>list,String date,String invId){
        
        //insert return into database
        String Q = "insert into returnvoc(date,invId)values('"+date+"','"+invId+"')";
        System.out.println(Q);
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            p.execute(Q);
        }catch(Exception e){
            System.out.println("Exception");
            System.out.println(e.toString());
            return 0;
        }
        
        //getting the last return id
        int returnId = 0;
        String Q1 = "select id from returnvoc order by id desc limit 1";
        System.out.println(Q1);
        try{
            PreparedStatement p1 = conn.prepareStatement(Q1);
            ResultSet r1 = p1.executeQuery();
            if(r1 != null){
                r1.next();
                returnId = r1.getInt(1);
            }
        }catch(Exception e){
            System.out.println("Exception");
            System.out.println(e.toString());
            return 0;
        }
        
        //inserting the products into database
        int s = list.size();
        try{
            for(int i=0;i<s;i++){
                String d[] = list.get(i);
                //String Q2 = "insert into billedItem (name,price,billId,qty)values
                //('"+d[0]+"','"+d[2]+"','"+billId+"','"+d[1]+"')";
                String Q2 = "insert into returnitem(name,price,returnId,qty,purchasePrice)values"
                        + "('"+d[0]+"','"+d[2]+"','"+returnId+"','"+d[1]+"',"+d[5]+")";
                System.out.println(Q2);
                PreparedStatement p = conn.prepareStatement(Q2);
                p.execute();
                
                //increase the stock from products table
                double qty = Double.parseDouble(d[1]);
                int proId = Integer.parseInt(d[4]);
                
                String STOCK_QUERY = "update products set stock = stock+"+qty+" where id = '"+proId+"'";
                System.out.println(STOCK_QUERY);
                PreparedStatement sp = conn.prepareStatement(STOCK_QUERY);
                sp.execute();
                
            }
        }catch(Exception e){
            System.out.println("Exception");
            System.out.println(e.toString());
            return 0;
        }
        
        return returnId;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //customer -----------------------------------------------------------
    public int insertNewCustomer(String[]d){
        String Q = "insert into customer(name,mobile,landline,notes)values('"+d[0]+"','"+d[1]+"','"+d[2]+"','"+d[3]+"')";
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            p.execute();
        }catch(Exception e){
            return 0;
        }
        return 1;
    }
    
    //update customer
    public int udpateCustomer(String[]d){
        String Q = "update customer set name = '"+d[0]+"',mobile='"+d[1]+"',landline='"+d[2]+"',"
                + "notes='"+d[3]+"' where id = '"+d[4]+"'";
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            p.execute();
        }catch(Exception e){
            return 0;
        }
        return 1;
    }
    
    //getting all custoemrs
    public List<String[]>getCustomers(){
        String Q = "select id,name from customer";
        List<String[]>custList = new ArrayList<>();
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r= p.executeQuery();
            if(r != null){
                while (r.next()) {                    
                    custList.add(new String[]{r.getString(1),r.getString(2)});
                }
            }
        }catch(Exception e){
            return null;
        }
        return custList;
    }
    
    //getting all custoemrs
    public List<String[]>getCustomersDt(){
        String Q = "select id,name,mobile,landline,notes from customer";
        List<String[]>custList = new ArrayList<>();
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r= p.executeQuery();
            if(r != null){
                while (r.next()) {                    
                    custList.add(new String[]{r.getString(1),r.getString(2),
                                r.getString(3),r.getString(4),r.getString(5)});
                }
            }
        }catch(Exception e){
            return null;
        }
        return custList;
    }

    public Return getReturnByID(int retID) {
        
        List<Product>proList = new ArrayList<>();
        try{
            String Q = "select id,name,price,qty from returnitem where returnId = "+retID;
            
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while (r.next()){
                    Product product = new Product();
                    product.setId(r.getInt(1));
                    product.setName(r.getString(2));
                    product.setSellingPrice(r.getDouble(3));
                    product.setQty(r.getDouble(4));

                    proList.add(product);
                }
            }
        }
        catch(Exception e){System.out.println(e.toString());}
        
        
        Return returnModel = new Return();
        try{
            String Q = "select id,date,invId from returnvoc where id = "+retID;
            System.out.println(Q);
            
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    returnModel.setId(r.getInt(1));
                    returnModel.setDate(r.getString(2));
                    returnModel.setInvID(r.getInt(3));
                }
            }
        }catch(Exception e){}
        
        returnModel.setProList(proList);
        
        return returnModel;
    }

    public List<Return> getAllReturn() {
        List<Return> rmList = new ArrayList<>();
        try{
            String Q = "select id,date,invId from returnvoc order by id desc";
            
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    Return rm = new Return();
                    rm.setId(r.getInt(1));
                    rm.setDate(r.getString(2));
                    rm.setInvID(r.getInt(3));
                    
                    rmList.add(rm);
                }
            }
            else System.out.println("else");
        }catch(Exception e){System.out.println(e.toString());}
        
        return rmList;
    }
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //purchase -------------------------------------------
    
    public int lastInsertedPurchaseID(JFrame obj){
        try{
            String Q = "select id from purchase order by id desc limit 1";
            System.out.println(Q);
            
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    return r.getInt(1);
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(obj, e.toString());
            return 0;
        }
        return 0;
    }
    
    public int increaseStock(int proID,double qty){
        
        try{
            String STOCK_QUERY = "update products set stock = stock+"+qty+" where id = '"+proID+"'";
            PreparedStatement sp = conn.prepareStatement(STOCK_QUERY);
            sp.execute();
        }catch(Exception e){
            return 0;
        }
        return 1;
    }
    
    public int insertPurchase(JFrame obj,Purchase pModel){
        
        //INSERTING INTO PURCHASE HEADER
        try{
            String Q = "insert into purchase(date,compID)values('"+pModel.getDate()+"',"+pModel.getCompID()+")";
            System.out.println(Q);
            
            PreparedStatement p = conn.prepareStatement(Q);
            p.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(obj, e.getMessage());
            return 0;
        }
        
        int lastPurchaseID = lastInsertedPurchaseID(obj);//GETTING LAST INSERTED ROW.
        
        //INSERT INTO PURCHASE ITEMS
        try{
            
            for(PurchaseItems im:pModel.getPurchaseList()){
                
                String Q = "insert into purchase_items(purID,name,price,proID,qty)"
                        + "values("+lastPurchaseID+",'"+im.getProName()+"',"+im.getPrice()+","+
                        im.getProID()+","+im.getQty()+")";
                System.out.println(Q);
                
                PreparedStatement p = conn.prepareStatement(Q);
                p.execute();
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(obj, e.toString());
            return 0;
        }
        
        //UPDATING THE STOCK
        try{
            for(PurchaseItems im : pModel.getPurchaseList()){
                int proID = im.getProID();
                double qty = im.getQty();
                increaseStock(proID, qty);
            }
        }catch(Exception e){
            
        }
        
        return 1;
    }
    
    //getting all purchases
    public List<Purchase>getAllPurchase(){
        String Q = "select p.id,p.date,c.name from purchase as p inner join "
                + "customer as c on p.compID=c.id order by p.id desc";
        List<Purchase>list = new ArrayList<>();
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    Purchase purchase = new Purchase();
                    purchase.setId(r.getInt(1));
                    purchase.setDate(r.getString(2));
                    purchase.setCompName(r.getString(3));
                    
                    list.add(purchase);
                }
            }   
        }catch(Exception e){
            
        }
        return list;
    }
    
    public Purchase getPurchaseByID(int id){
        Purchase purchase = new Purchase();
        
        try{
            String Q = "select p.id,p.date,p.compID,c.name from purchase as p "
                    + "inner join customer as c on p.compID=c.id where p.id="+id;
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    purchase.setId(r.getInt(1));
                    purchase.setDate(r.getString(2));
                    purchase.setCompID(r.getInt(3));
                    purchase.setCompName(r.getString(4));
                }
            }
        }
        catch(Exception e){}
        
        try{
            String Q = "select id,purID,name,price,proID,qty from purchase_items where purID = "+id;
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    PurchaseItems items = new PurchaseItems();
                    items.setId(r.getInt(1));
                    items.setPurID(r.getInt(2));
                    items.setProName(r.getString(3));
                    items.setPrice(r.getDouble(4));
                    items.setProID(r.getInt(5));
                    items.setQty(r.getDouble(6));
                
                    purchase.getPurchaseList().add(items);
                }
            }
        }catch(Exception e){}
        
        return purchase;
    }

    public double getTotalInvoice(String from, String to) {
        String Q = "select sum(bi.price*bi.qty) from billeditem as bi inner join "
                + "bills as b on bi.billId = b.id where (DATE(b.date) between "
                + "'"+from+"' and '"+to+"')";
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    return r.getDouble(1);
                }
            }
        }catch (Exception e){}
    
        return 0;
    }
    
    public double getTotalInvoiceProfit(String from, String to) {
        String Q = "select sum((bi.price-bi.purchasePrice)*bi.qty) from billeditem as bi inner join "
                + "bills as b on bi.billId = b.id where (DATE(b.date) between "
                + "'"+from+"' and '"+to+"')";
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    return r.getDouble(1);
                }
            }
        }catch (Exception e){}
    
        return 0;
    }
    
    public double getTotalInvReturn(String from,String to){
        String Q = "select sum(bi.price*bi.qty) from returnitem as bi inner join "
                + "returnvoc as b on bi.returnId = b.id where (DATE(b.date) "
                + "between '"+from+"' and '"+to+"')";
    
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    return r.getDouble(1);
                }
            }
        }catch(Exception e){}
        return 0;
    }
    
    public double getTotalInvReturnLoss(String from,String to){
        String Q = "select sum((bi.price-bi.purchasePrice)*bi.qty) from returnitem as bi inner join "
                + "returnvoc as b on bi.returnId = b.id where (DATE(b.date) "
                + "between '"+from+"' and '"+to+"')";
    
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    return r.getDouble(1);
                }
            }
        }catch(Exception e){}
        return 0;
    }
    
    public double getTotalPurchase(String from,String to){
        String Q = "select sum(bi.price*bi.qty) from purchase_items as bi inner join purchase as "
                + "b on bi.purID = b.id where (DATE(b.date) between '"+from+"' and '"+to+"')";
        
        try{
            PreparedStatement p = conn.prepareStatement(Q);
            ResultSet r = p.executeQuery();
            if(r != null){
                while(r.next()){
                    return r.getDouble(1);
                }
            }
        }catch(Exception e){}
        return 0;
    }
    
}
