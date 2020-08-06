/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Youtube
 */
public class SettingsModel {
    private int printerType;
    private String printerName;
    private String address;

    public int getPrinterType() {
        return printerType;
    }

    public void setPrinterType(int printerType) {
        this.printerType = printerType;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
}
