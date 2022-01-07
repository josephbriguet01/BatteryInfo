/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, January 2021
 */
package com.jasonpercus.off;



/**
 * Cette classe permet d'activer la prise Shelly Plug S
 * @author JasonPercus
 * @version 1.0
 */
public class Off {

    
    
//ATTRIBUT
    /**
     * Correspond à l'adresse IP de la prise domotique
     */
    private static String ip;
    
    
    
//MAIN
    /**
     * Démarre le programme
     * @param args Correspond aux éventuels arguments
     */
    public static void main(String[] args) {
        java.io.File file = new java.io.File(System.getProperty("user.home") + java.io.File.separator + "AppData" + java.io.File.separator + "Local" + java.io.File.separator + "BatteryInfo" + java.io.File.separator + "ip_plug.txt");
        try {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
                ip = br.readLine();
                if(ip != null && ip.isEmpty()) ip = null;
            }
        } catch (java.io.FileNotFoundException ex) {
            try {
                file.createNewFile();
            } catch (java.io.IOException e) {}
        } catch (java.io.IOException ex) {}
        
        command("off");
    }
    
    
    
//METHODE PRIVATE
    /**
     * Envoie une command à destination de la prise Shelly Plug S
     * @param command Correspond à la commande à envoyer
     * @return Retourne true, si elle a bien été envoyé et traité, sinon false
     */
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private static boolean command(String command){
        if(ip == null || ip.isEmpty()) return false;
        try {
            java.net.URL obj = new java.net.URL("http://" + ip + "/relay/0?turn=" + command);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
                StringBuilder response;
                try (java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
    
    
    
}