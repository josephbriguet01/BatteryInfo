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
 * Cette classe rassemble les propriétés de controle
 * @author JasonPercus
 * @version 1.0
 */
public class Properties {
    
    
    
//ATTRIBUTS
    /**
     * Correspond au seuil minimum à partir duquel la batterie devrait être en charge
     */
    public int percentMin;
    
    /**
     * Correspond au seuil maximum à partir duquel la batterie ne devrait plus être en charge
     */
    public int percentMax;
    
    /**
     * Correspond au dernier état de la batterie avant le dernier démarrage de l'ordinateur
     */
    public boolean charge;
    
    
    
}