package com.shandakova.documents;

import lombok.extern.slf4j.Slf4j;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class DocumentServiceApp {
    public static void main(String[] args) throws Exception {
        ApplicationContext cx = new AnnotationConfigApplicationContext("com.shandakova.documents");
        Server server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);
        WebAppContext root = new WebAppContext("rest/src/main/webapp", "/");
        server.setHandlers(new Handler[]{root});
        server.start();
    }
}
