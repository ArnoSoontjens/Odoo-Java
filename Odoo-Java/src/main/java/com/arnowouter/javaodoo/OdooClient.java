/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo;

import de.timroes.axmlrpc.XMLRPCClient;
import java.net.URL;

/**
 *
 * @author Arno
 */
public class OdooClient extends XMLRPCClient {

    public OdooClient(URL url, String userAgent, int flags) {
        super(url, userAgent, flags);
    }

    public OdooClient(URL url, int flags) {
        super(url, flags);
    }

    public OdooClient(URL url, String userAgent) {
        super(url, userAgent);
    }

    public OdooClient(URL url) {
        super(url);
    }
}
