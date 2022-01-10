/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, January 2021
 */
package com.jasonpercus.battery;



import com.jasonpercus.json.JSON;
import com.jasonpercus.util.LoaderPlugin;
import com.jasonpercus.util.WinBatteryInfo;



/**
 * Cette classe permet de controler l'état de la batterie au cours du temps
 * @author JasonPercus
 * @version 1.0
 */
@SuppressWarnings("SleepWhileInLoop")
public class BatteryInfo {
    
    
    
//CONSTANTS
    /**
     * Correspond au chemin absolu du dossier contenant les données de l'application
     */
    private final static String PATH_DATAS_APP = System.getProperty("user.home") + java.io.File.separator + "AppData" + java.io.File.separator + "Local" + java.io.File.separator + "BatteryInfo";
    
    /**
     * Correspond au fichier qui permet l'arrêt du processus
     */
    private final static java.io.File STOP_FILE = new java.io.File(PATH_DATAS_APP + java.io.File.separator + "stop");
    
    /**
     * Correspond au nom de l'application
     */
    private final static String NAME_APP = BatteryInfo.class.getSimpleName();
    
    /**
     * Correspond au préfix de la version de l'application
     */
    private final static String PREFIX_VERSION = "1";
    
    /**
     * Correspond au suffix de la version de l'application
     */
    private final static int SUFFIX_VERSION = 0;
    
    /**
     * Correspond à la version de l'application
     */
    private final static String VERSION_APP = String.format("%s.%d", PREFIX_VERSION, SUFFIX_VERSION);
    
    /**
     * Correspond à l'icône de l'application
     */
    private static java.awt.Image ICON_APP;
    
    /**
     * Correspond à l'icône du systray de l'application
     */
    private static java.awt.Image ICON_TRAY_APP;
    
    /**
     * Correspond à la popup du systray
     */
    private final static java.awt.PopupMenu POPUP = new java.awt.PopupMenu();
    
    
    
//INIT
    static{
        try {
            ICON_APP        = javax.imageio.ImageIO.read(BatteryInfo.class.getResource("/com/jasonpercus/battery/battery.png"));
            ICON_TRAY_APP   = javax.imageio.ImageIO.read(BatteryInfo.class.getResource("/com/jasonpercus/battery/tray.png"));
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(BatteryInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    
    
//ATTRIBUTS
    /**
     * Correspond aux propriétés du projet
     */
    static Properties properties;
    
    /**
     * Correspond à l'icône dans le systray
     */
    private static java.awt.TrayIcon trayIcon;
    
    /**
     * Correspond à l'état de la batterie avant le dernier arrêt de l'ordinateur
     */
    private static Boolean hasCharging;
    
    /**
     * Correspond à la liste des objets créés pour chaque plugin chargé
     */
    private static java.util.List<OnOff> plugins;
    
    /**
     * Détermine si la prise de secteur est branchée ou pas
     */
    private static Boolean power;
    
    /**
     * Correspond au pourcentage de la batterie
     */
    private static Integer percent;
    
    /**
     * Correspond au nombre de secondes restantes avant que la batterie soit déchargée
     */
    private static Long lifeTime;
    
    /**
     * Correspond au nombre de secondes restantes avant que la batterie soit déchargée lorsque la batterie est pleine
     */
    private static Long fullLifeTime;
    
    /**
     * Détermine si le programme d'analyse est en pause
     */
    private static boolean paused;
    
    
    
//MAIN
    /**
     * Lance le programme
     * @param args Correspond aux éventuels arguments
     */
    public static void main(String[] args){
        //<editor-fold defaultstate="collapsed" desc=" Look and feel">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BatteryInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        createSysTray();
        
        System.out.println(String.format("Launching %s (v%s)...", NAME_APP, VERSION_APP));
        
        java.io.File datasFolder = new java.io.File(PATH_DATAS_APP);
        if(!datasFolder.exists())
            datasFolder.mkdir();
        
        loadProperties();
        loadPlugin();
        
        new Thread(() -> {
            while(true){
                checkToStop();
                checkToPause();
                try {
                    WinBatteryInfo infos = get();
                    properties.charge = infos.getStatus() == WinBatteryInfo.ACLineStatus.ONLINE;
                    saveProperties();
                    if(mustCharge(infos)){
                        on();
                    }else if(mustDisCharge(infos)){
                        off();
                    }
                    for(int i=0;i<5;i++){
                        checkToStop();
                        checkToPause();
                        Thread.sleep(500);
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
    
    
    
//METHODES PUBLICS
    /**
     * La prise secteur devrait être branchée
     */
    private static void on(){
        for(OnOff plugin : plugins){
            if(plugin.on())
                break;
        }
        while (get().getStatus() == WinBatteryInfo.ACLineStatus.OFFLINE) {
            checkToStop();
            checkToPause();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        checkToStop();
        checkToPause();
        properties.charge = true;
        saveProperties();
        for (OnOff plugin : plugins) {
            if (plugin.stop()) {
                break;
            }
        }
    }
    
    /**
     * La prise secteur devrait être débranchée
     */
    private static void off(){
        for(OnOff plugin : plugins){
            if(plugin.off())
                break;
        }
        while (get().getStatus() == WinBatteryInfo.ACLineStatus.ONLINE) {
            checkToStop();
            checkToPause();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        checkToStop();
        checkToPause();
        properties.charge = false;
        saveProperties();
        for (OnOff plugin : plugins) {
            if (plugin.stop()) {
                break;
            }
        }
    }
    
    /**
     * Détermine si la prise secteur devrait être branchée ou pas
     * @param infos Correspond aux infos actuelles de la batterie
     * @return Retourne true, si le secteur devrait être branché à la batterie
     */
    private static boolean mustCharge(WinBatteryInfo infos){
        if(0 <= infos.getPercent() && infos.getPercent() <= properties.percentMin){
            return infos.getStatus() == WinBatteryInfo.ACLineStatus.OFFLINE;
        }else{
            if(hasCharging != null && hasCharging){
                hasCharging = null;
                return infos.getStatus() == WinBatteryInfo.ACLineStatus.OFFLINE;
            }else{
                return false;
            }
        }
    }
    
    /**
     * Détermine si la prise secteur devrait être débranchée ou pas
     * @param infos Correspond aux infos actuelles de la batterie
     * @return Retourne true, si le secteur devrait être débranché de la batterie
     */
    private static boolean mustDisCharge(WinBatteryInfo infos){
        if(properties.percentMax <= infos.getPercent() && infos.getPercent() <= 100){
            return infos.getStatus() == WinBatteryInfo.ACLineStatus.ONLINE;
        }else{
            if(hasCharging != null && !hasCharging){
                hasCharging = null;
                return infos.getStatus() == WinBatteryInfo.ACLineStatus.ONLINE;
            }else{
                return false;
            }
        }
    }
    
    /**
     * Renvoie le status de la batterie. Si le status à changé, notifie les plugins des nouvelles valeurs
     * @return Retourne le status de la batterie
     */
    private static WinBatteryInfo get() {
        WinBatteryInfo infos = WinBatteryInfo.get();
        boolean dc = infos.getStatus() == WinBatteryInfo.ACLineStatus.ONLINE;
        boolean pass = false;
        if(power == null || power != dc)
            pass = true;
        if(percent == null || percent != infos.getPercent())
            pass = true;
        if(lifeTime == null || lifeTime != infos.getLifeTime())
            pass = true;
        if(fullLifeTime == null || fullLifeTime != infos.getFullLifeTime())
            pass = true;
        if(pass){
            power        = dc;
            percent      = infos.getPercent();
            lifeTime     = infos.getLifeTime();
            fullLifeTime = infos.getFullLifeTime();
            trayIcon.setToolTip(NAME_APP + " " + percent + " %" + ((power) ? " (Charging)" : ""));
            for (OnOff plugin : plugins)
                plugin.onChange(power, percent, lifeTime, fullLifeTime);
        }
        return infos;
    }
    
    /**
     * Crée le systray de l'application
     */
    private static void createSysTray(){
        if (java.awt.SystemTray.isSupported()) {
            // get the SystemTray instance
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            
            // create menu item for the default action
            java.awt.MenuItem quit = new java.awt.MenuItem("Quit " + NAME_APP);
            
            quit.addActionListener((java.awt.event.ActionEvent e) -> {
                if(STOP_FILE.exists())
                    STOP_FILE.delete();
                System.exit(0);
            });
            java.awt.MenuItem pause_resume = new java.awt.MenuItem("Pause");
            pause_resume.addActionListener((java.awt.event.ActionEvent e) -> {
                if(paused){
                    resume();
                    pause_resume.setLabel("Pause");
                }else{
                    pause();
                    pause_resume.setLabel("Resume");
                }
            });
            java.awt.MenuItem settings = new java.awt.MenuItem("Settings");
            settings.addActionListener((java.awt.event.ActionEvent e) -> {
                java.awt.EventQueue.invokeLater(() -> {
                    new Settings(ICON_APP).setVisible(true);
                });
            });
            
            POPUP.add(pause_resume);
            POPUP.add(settings);
            POPUP.addSeparator();
            POPUP.add(quit);
            trayIcon = new java.awt.TrayIcon(ICON_TRAY_APP, NAME_APP, POPUP);
            try {
                tray.add(trayIcon);
            } catch (java.awt.AWTException e) {
                System.err.println(e);
            }
        }
    }
    
    /**
     * Clone un BufferedImage
     * @param toClone Correspond à l'image à cloner
     * @return Retourne une copy de l'image source
     */
    private static java.awt.Image clone(java.awt.image.BufferedImage toClone){
        java.awt.image.BufferedImage copyOfImage = new java.awt.image.BufferedImage(toClone.getWidth(null), toClone.getHeight(null), java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics g = copyOfImage.createGraphics();
        g.drawImage(toClone, 0, 0, null);
        g.dispose();
        return copyOfImage;
    }
    
    /**
     * Vérifie s'il faut arrêter le processus d'analyse d'état de la batterie
     */
    private static void checkToStop(){
        if(STOP_FILE.exists()){
            STOP_FILE.delete();
            System.exit(0);
        }
    }
    
    /**
     * Vérifie s'il faut mettre le processus d'analyse d'état de la batterie en pause
     */
    private static void checkToPause(){
        try {
            while (paused) {
                Thread.sleep(500);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Met en pause le process d'analyse d'état de la batterie
     */
    private static void pause(){
        paused = true;
    }
    
    /**
     * Resume le process d'analyse d'état de la batterie
     */
    private static void resume(){
        paused = false;
    }
    
    /**
     * Charge la liste des objets de chaque plugin chargé
     */
    private static void loadPlugin(){
        java.io.File pluginFolder = new java.io.File("plugins");
        plugins = new java.util.ArrayList<>();
        if(!pluginFolder.exists())
            pluginFolder.mkdir();
        LoaderPlugin loader = new LoaderPlugin(pluginFolder, "jar");
        for(int i=0;i<loader.size();i++){
            loader.setPosition(i);
            for(Object o : loader.getObject(OnOff.class)){
                OnOff onOff = (OnOff) o;
                onOff.setPathDatasApp(PATH_DATAS_APP);
                onOff.setNameApp(NAME_APP);
                onOff.setVersionApp(VERSION_APP);
                onOff.setIconApp(clone((java.awt.image.BufferedImage) ICON_APP));
                onOff.setIconTrayApp(clone((java.awt.image.BufferedImage) ICON_TRAY_APP));
                onOff.setPopupTrayApp(POPUP);
                onOff.onCreate();
                plugins.add(onOff);
            }
        }
        java.util.Collections.sort(plugins);
    }
    
    /**
     * Charge les propriétés du projet
     */
    private static void loadProperties(){
        try {
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(new java.io.File(PATH_DATAS_APP + java.io.File.separator + "battery.json")))) {
                String chain = "";
                String line;
                while((line = br.readLine()) != null){
                    if(!chain.isEmpty()) chain += "\n";
                    chain += line;
                }
                properties = (Properties) JSON.deserialize(Properties.class, chain).getObj();
                hasCharging = properties.charge;
            }
        } catch (java.io.FileNotFoundException ex) {
            properties = new Properties();
            properties.charge = false;
            properties.percentMax = 99;
            properties.percentMin = 10;
            saveProperties();
            hasCharging = false;
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(BatteryInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Enregistre les propriétés du projet
     */
    static void saveProperties(){
        try {
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(new java.io.File(PATH_DATAS_APP + java.io.File.separator + "battery.json")))) {
                bw.write(JSON.serialize(properties));
                bw.flush();
            }
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(BatteryInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    
    
}