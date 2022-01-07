/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, January 2021
 */
package com.jasonpercus.battery;



/**
 * Cette classe représente la classe principale d'un plugin
 * @author JasonPercus
 * @version 1.0
 */
public abstract class OnOff implements Comparable<OnOff> {
    
    
    
//ATTRIBUTS
    /**
     * Correspond au chemin absolu du dossier contenant les données de l'application
     */
    private String pathDatasApp;
    
    /**
     * Correspond au nom de l'application
     */
    private String nameApp;
    
    /**
     * Correspond à la version de l'application
     */
    private String versionApp;
    
    /**
     * Correspond à l'icone de l'application
     */
    private java.awt.Image iconApp;
    
    /**
     * Correspond à l'icone du systray de l'application
     */
    private java.awt.Image iconTrayApp;
    
    /**
     * Correspond à la popup du systray de l'application
     */
    private java.awt.PopupMenu popupTrayApp;

    
    
//CONSTRUCTOR
    /**
     * Crée un objet {@link OnOff} par défaut
     */
    public OnOff() {
    }
    
    
    
//METHODES PUBLICS
    /**
     * Lorsque l'objet est en cours de création
     */
    public void onCreate(){
        
    }
    
    /**
     * Modifie le nom de l'application
     * @param nameApp Correspond au nom de l'application
     */
    final void setNameApp(String nameApp){
        this.nameApp = nameApp;
    }
    
    /**
     * Modifie la version de l'application
     * @param versionApp Correspond à la nouvelle version de l'application
     */
    final void setVersionApp(String versionApp){
        this.versionApp = versionApp;
    }
    
    /**
     * Modifie le chemin absolu du dossier contenant les données de l'application
     * @param pathDatasApp Correspond au nouveau chemin absolu du dossier contenant les données de l'application
     */
    final void setPathDatasApp(String pathDatasApp) {
        this.pathDatasApp = pathDatasApp;
    }

    /**
     * Renvoie l'icône de l'application
     * @param iconApp Correspond à la nouvelle icône de l'application
     */
    final void setIconApp(java.awt.Image iconApp) {
        this.iconApp = iconApp;
    }

    /**
     * Modifie l'icône du systray de l'application
     * @param iconTrayApp Correspond à la nouvelle icône du systray de l'application
     */
    final void setIconTrayApp(java.awt.Image iconTrayApp) {
        this.iconTrayApp = iconTrayApp;
    }

    /**
     * Modifie la popup du systray de l'application
     * @param popupTrayApp Correspond à la nouvelle popup du systray de l'application
     */
    final void setPopupTrayApp(java.awt.PopupMenu popupTrayApp) {
        this.popupTrayApp = popupTrayApp;
    }
    
    /**
     * Renvoie le chemin absolu du dossier contenant les données de l'application
     * @return Retourne le chemin absolu du dossier contenant les données de l'application
     */
    public final String getPathDatasApp() {
        return this.pathDatasApp;
    }
    
    /**
     * Renvoie le dossier contenant tous les fichiers de données de l'application
     * @return Retourne le dossier contenant tous les fichiers de données de l'application
     */
    public final java.io.File getDirectoryDatasApp() {
        return new java.io.File(this.pathDatasApp);
    }
    
    /**
     * Renvoie un fichier de données de l'application
     * @param nameFile Correspond au nom du fichier
     * @return Retourne un fichier de données de l'application
     */
    public final java.io.File getFileDatasApp(String nameFile){
        return new java.io.File(this.pathDatasApp + java.io.File.separator + nameFile);
    }
    
    /**
     * Renvoie le nom de l'application
     * @return Retourne le nom de l'application
     */
    public final String getNameApp(){
        return this.nameApp;
    }
    
    /**
     * Renvoie la version de l'application
     * @return Retourne la version de l'application
     */
    public final String getVersionApp(){
        return this.versionApp;
    }

    /**
     * Renvoie l'icône de l'application
     * @return Retourne l'icône de l'application
     */
    public final java.awt.Image getIconApp() {
        return iconApp;
    }

    /**
     * Renvoie l'icône du systray de l'application
     * @return Retourne l'icône de l'application
     */
    public final java.awt.Image getIconTrayApp() {
        return iconTrayApp;
    }

    /**
     * Renvoie la popup du systray de l'application
     * @return Retourne la popup du systray de l'application
     */
    public final java.awt.PopupMenu getPopupTrayApp() {
        return popupTrayApp;
    }
    
    /**
     * Renvoie la priorité d'exécution de ce plugin (bas = prioritaire, haut = secondaire)
     * @return Retourne la priorité d'exécution de ce plugin
     */
    public abstract int priority();
    
    /**
     * Active une action
     * @return Retourne true si l'action a été consumé, sinon false
     */
    public abstract boolean on();
    
    /**
     * Active l'action inverse
     * @return Retourne true si l'action a été consumé, sinon false
     */
    public abstract boolean off();
    
    /**
     * Stoppe l'action en cours
     * @return Retourne true si elle a bien été stoppé, sinon false
     */
    public abstract boolean stop();
    
    /**
     * Lorsqu'il y a un changement d'état de la batterie
     * @param power Détermine si la prise de secteur est branchée ou pas
     * @param percent Correspond au pourcentage de la batterie
     * @param lifeTime Correspond au nombre de secondes restantes avant que la batterie soit déchargée
     * @param fullLifeTime Correspond au nombre de secondes restantes avant que la batterie soit déchargée lorsque la batterie est pleine
     */
    public void onChange(boolean power, int percent, long lifeTime, long fullLifeTime){
        
    }

    /**
     * Compare deux OnOff
     * @param o Correspond au second OnOff à comparer au courant
     * @return Retourne le résultat de la comparaison
     */
    @Override
    public final int compareTo(OnOff o){
        if(o == null) return 1;
        if(priority() < o.priority()) return -1;
        else if(priority() > o.priority()) return 1;
        else return 0;
    }
    
    
    
}