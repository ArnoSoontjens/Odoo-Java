/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo;

/**
 *
 * @author Arno
 */
public interface OdooConnector {
    public int write();
    public void read();
    public void search();
    public void searchAndRead();
}
