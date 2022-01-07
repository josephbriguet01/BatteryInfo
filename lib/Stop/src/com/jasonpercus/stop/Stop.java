/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, January 2021
 */
package com.jasonpercus.stop;



/**
 * Cette classe permet de demander de stopper l'analyse de la batterie
 * @author JasonPercus
 * @version 1.0
 */
public class Stop {

    
    
//MAIN
    /**
     * Lance le programme
     * @param args Correspond aux éventuels arguments
     */
    public static void main(String[] args) {
        java.io.File dir = new java.io.File(System.getProperty("user.home") + java.io.File.separator + "AppData" + java.io.File.separator + "Local" + java.io.File.separator + "BatteryInfo");
        if(!dir.exists())
            dir.mkdir();
        
        java.io.File file = new java.io.File(System.getProperty("user.home") + java.io.File.separator + "AppData" + java.io.File.separator + "Local" + java.io.File.separator + "BatteryInfo" + java.io.File.separator + "stop");
        try {
            if(!file.exists())
                file.createNewFile();
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(Stop.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    
    
}