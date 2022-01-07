/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jasonpercus.switchbatteryplugin.network;



/**
 * Cette classe sert d'exception lorsqu'une adresse n'a pas le bon format.
 * @author Briguet
 * @version 1.0
 */
public class InvalidAddressException extends RuntimeException {
    
    
    
    // <editor-fold defaultstate="collapsed" desc="SERIAL_VERSION_UID">
    /**
     * Correspond au numéro de série qui identifie le type de dé/sérialization utilisé pour l'objet
     */
    private static final long serialVersionUID = 1L;
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Crée une exception
     * @param string Correspond au message de l'exception
     */
    public InvalidAddressException(String string) {
        super(string);
    }
    // </editor-fold>
    
    
    
}