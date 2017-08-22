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
public class OdooQueryException extends Exception {

    public OdooQueryException() {
    }

    public OdooQueryException(String message) {
        super(message);
    }

    public OdooQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
