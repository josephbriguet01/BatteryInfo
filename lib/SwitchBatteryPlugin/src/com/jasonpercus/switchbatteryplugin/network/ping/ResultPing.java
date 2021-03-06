/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, May 2021
 */
package com.jasonpercus.switchbatteryplugin.network.ping;



/**
 * Cette classe représente une réponse d'un ping
 * @author JasonPercus
 * @version 1.0
 */
public abstract class ResultPing implements java.io.Serializable {

    
    
//ATTRIBUT
    /**
     * Correspond à l'adresse IP qui a répondu au ping
     */
    final String from;
    
    
    
//CONSTRUCTOR
    /**
     * Crée une réponse de ping
     * @param from Correspond à l'adresse IP qui a répondu au ping
     */
    protected ResultPing(String from) {
        this.from = from;
    }

    
    
//METHODES PUBLICS
    /**
     * Renvoie l'adresse IP qui a répondu au ping
     * @return Retourne l'adresse IP qui a répondu au ping
     */
    public String getFrom() {
        return from;
    }
    
    /**
     * Renvoie la réponse du ping sous la forme d'une chaîne de caractères
     * @return Retourne la réponse du ping sous la forme d'une chaîne de caractères
     */
    @Override
    public abstract String toString();
    
    
    
}