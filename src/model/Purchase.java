/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Youtube
 */
public class Purchase {
    private int id,compID;
    private String date,compName;
    private List<PurchaseItems>purchaseList;

    public Purchase() {
        purchaseList = new ArrayList<>();
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompID() {
        return compID;
    }

    public void setCompID(int compID) {
        this.compID = compID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<PurchaseItems> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<PurchaseItems> purchaseList) {
        this.purchaseList = purchaseList;
    }
    
}
