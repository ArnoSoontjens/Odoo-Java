/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.exceptions;

/**
 *
 * @author Arno
 */
public class OdooConnectorException extends Exception{

    public OdooConnectorException() {
    }

    public OdooConnectorException(String message) {
        super(message);
    }

    public OdooConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
