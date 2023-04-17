package edu.school21.servertanks.servertanks.app;

import edu.school21.servertanks.servertanks.gui.ViewGuiClient;
import edu.school21.servertanks.servertanks.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Program {
    public static void main(String[] args) {
        int port = ViewGuiClient.createConfiguration();

        ApplicationContext context = createContext();
        Server server = context.getBean(Server.class);
        try {
            server.start(port);
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    private static ApplicationContext createContext() {
        return new AnnotationConfigApplicationContext("edu.school21.servertanks.servertanks");
    }
}
