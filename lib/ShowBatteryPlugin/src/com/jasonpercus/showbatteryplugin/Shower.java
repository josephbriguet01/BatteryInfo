/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, January 2021
 */
package com.jasonpercus.showbatteryplugin;



import com.jasonpercus.battery.OnOff;



/**
 * Cette classe permet de contrôler les fenêtres qui affichent le status de la batterie
 * @author JasonPercus
 * @version 1.0
 */
public class Shower extends OnOff {

    
    
//ATTRIBUT
    /**
     * Correspond à la fenêtre qui affiche le status de la batterie
     */
    private Screen screen;
    
    
    
//METHODES PUBLICS
    /**
     * Renvoie la priorité d'exécution de ce plugin (bas = prioritaire, haut = secondaire)
     * @return Retourne la priorité d'exécution de ce plugin
     */
    @Override
    public int priority() {
        return 10;
    }

    /**
     * Demande à l'utilisateur de brancher le secteur
     * @return Retourne toujours false, car l'action n'est pas consumé
     */
    @Override
    public boolean on() {
        if(screen != null)
            screen.dispose();
        
        java.awt.EventQueue.invokeLater(() -> {
            if(screen != null)
                screen.dispose();
            screen = new Screen(false, getNameApp(), getVersionApp(), getIconApp());
            screen.setVisible(true);
        });
        
        return false;
    }

    /**
     * Demande à l'utilisateur de débrancher le secteur
     * @return Retourne toujours false, car l'action n'est pas consumé
     */
    @Override
    public boolean off() {
        if(screen != null)
            screen.dispose();
        
        java.awt.EventQueue.invokeLater(() -> {
            if(screen != null)
                screen.dispose();
            screen = new Screen(true, getNameApp(), getVersionApp(), getIconApp());
            screen.setVisible(true);
        });
        
        return false;
    }

    /**
     * L'utilisateur vient de réagir à la fenêtre. La prise secteur vient d'être dé/branché. Alors la fenêtre peut se fermer
     * @return Retourne true, car l'action vient d'être consumé
     */
    @Override
    public boolean stop() {
        if(screen != null)
            screen.dispose();
        return false;
    }
    
    
    
}