/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arnowouter.javaodoo.util;

import java.util.HashMap;


public class VersionInfo {
    private int protocol_version;
    private String server_serie;
    private String server_version;
    private String server_version_info;

    public VersionInfo(Object versionObject) {
        server_serie = new String();
        server_version = new String();
        server_version_info = new String();
        processVersionObject(versionObject);
    }
    
    private void processVersionObject(Object versionObject){
        HashMap<String, Object> versionInfoHM = (HashMap<String,Object>) versionObject;
        protocol_version = (int) versionInfoHM.get("protocol_version");
        server_serie = (String) versionInfoHM.get("server_serie");
        server_version = (String) versionInfoHM.get("server_version");
        processServerVersionInfo((Object[])versionInfoHM.get("server_version_info"));
    }

    private void processServerVersionInfo(Object[] serverInfo){
        for(Object info : serverInfo){
            server_version_info += info + " ";
        }
        server_version_info = server_version_info.trim().replaceAll(" ", ".");
    }
    
    @Override
    public String toString() {
        return "OdooVersionInfo{" + "protocol_version=" + protocol_version + ", server_serie=" + server_serie + ", server_version=" + server_version + ", server_version_info=" + server_version_info + '}';
    }
    
    
}
