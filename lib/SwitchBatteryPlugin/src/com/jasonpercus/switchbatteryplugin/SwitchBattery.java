/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, January 2021
 */
package com.jasonpercus.switchbatteryplugin;



import com.jasonpercus.battery.OnOff;
import com.jasonpercus.switchbatteryplugin.network.ping.FailedPing;
import com.jasonpercus.switchbatteryplugin.network.ping.Ping;
import com.jasonpercus.switchbatteryplugin.network.ping.ResultPing;
import com.jasonpercus.switchbatteryplugin.network.ping.SuccessPing;



/**
 * Cette classe permet de contrôler une prise domotique Shelly Plug S
 * @author JasonPercus
 * @version 1.0
 */
public class SwitchBattery extends OnOff {

    
    
//ATTRIBUT
    /**
     * Correspond à l'adresse IP de la prise domotique
     */
    String ip;
    
    /**
     * Détermine si l'équipement à été trouvé sur le réseau
     */
    boolean found;
    
    
    
//CONSTRUCTOR
    /**
     * Crée un objet SwitchBattery
     */
    public SwitchBattery() {
        
    }

    
    
//METHODES PUBLICS
    /**
     * Lorsque l'objet est en cours de création
     */
    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void onCreate(){
        loadFileIp();
        java.awt.MenuItem on  = new java.awt.MenuItem("On (Shelly Plug S)");
        java.awt.MenuItem off = new java.awt.MenuItem("Off (Shelly Plug S)");
        java.awt.MenuItem change = new java.awt.MenuItem("Set IP (Shelly Plug S)");
        on.addActionListener((java.awt.event.ActionEvent e) -> {
            on();
        });
        off.addActionListener((java.awt.event.ActionEvent e) -> {
            off();
        });
        change.addActionListener((java.awt.event.ActionEvent e) -> {
            loadFileIp();
            java.awt.EventQueue.invokeLater(() -> {
                new SetIP(getIconApp(), this).setVisible(true);
            });
        });
        getPopupTrayApp().insertSeparator(0);
        getPopupTrayApp().insert(change, 0);
        getPopupTrayApp().insert(off, 0);
        getPopupTrayApp().insert(on, 0);
//        waitForPing();
        new Thread(() -> {
            while(true){
                ResultPing resultPing = null;
                try {
                    resultPing = Ping.execute(ip);
                } catch (Exception ex) {}
                this.found = resultPing != null && resultPing instanceof SuccessPing;
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
    
    /**
     * Renvoie la priorité d'exécution de ce plugin (bas = prioritaire, haut = secondaire)
     * @return Retourne la priorité d'exécution de ce plugin
     */
    @Override
    public int priority() {
        return 0;
    }
    
    /**
     * Active la prise Shelly Plug S
     * @return Retourne true si elle a bien été activé, sinon false
     */
    @Override
    public boolean on() {
        return command("on");
    }

    /**
     * Désactive la prise Shelly Plug S
     * @return Retourne true si elle a bien été désactivé, sinon false
     */
    @Override
    public boolean off() {
        return command("off");
    }

    /**
     * Stoppe l'action en cours
     * @return Retourne true si elle a bien été stoppé, sinon false
     */
    @Override
    public boolean stop() {
        return false;
    }
    
    
    
//METHODES PRIVATES
    /**
     * Envoie une command à destination de la prise Shelly Plug S
     * @param command Correspond à la commande à envoyer
     * @return Retourne true, si elle a bien été envoyé et traité, sinon false
     */
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private boolean command(String command){
        if(ip == null || ip.isEmpty()) return false;
        try {
            java.net.URL obj = new java.net.URL("http://" + this.ip + "/relay/0?turn=" + command);
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
    
    /**
     * Charge l'ip contenu dans un fichier de données
     */
    private void loadFileIp(){
        java.io.File file = getFileDatasApp("ip_plug.txt");
        try{
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
                ip = br.readLine();
                if(ip != null && ip.isEmpty()) ip = null;
            }
        } catch (java.io.FileNotFoundException ex) {
            try {
                file.createNewFile();
            } catch (java.io.IOException e) {}
        } catch (java.io.IOException ex) {}
    }
    
    /**
     * Enregistre l'ip
     */
    void saveFileIp(){
        java.io.File file = getFileDatasApp("ip_plug.txt");
        try {
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(file))) {
                bw.write((this.ip == null) ? "" : this.ip);
                bw.flush();
            }
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(SwitchBattery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Attend que l'ip récupérée puisse être pingé
     */
    @SuppressWarnings("SleepWhileInLoop")
    private void waitForPing(){
        ResultPing resultPing = null;
        while(resultPing == null || resultPing instanceof FailedPing){
            try {
                resultPing = Ping.execute(ip);
            } catch (Exception ex) {}
            if(resultPing != null && resultPing instanceof SuccessPing){
                this.found = true;
                break;
            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    
    
}