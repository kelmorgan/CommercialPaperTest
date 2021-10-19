package com.cp.api.execute;

import com.cp.utils.SocketService;



public class Api {
    public static String executeCall(String serviceName, String requestXml){
        return new SocketService().executeIntegrationCall(serviceName,requestXml);
    }
}
