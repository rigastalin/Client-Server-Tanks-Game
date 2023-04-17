package edu.school21.servertanks.servertanks.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.school21.servertanks.servertanks.service.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Server {
    private PointsService pointsService;
    private List<Client> clients = new ArrayList<>();
    int num = 0;

    @Autowired
    public Server(PointsService pointsService) {
        this.pointsService = pointsService;
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is run");
            while (num != 2) {
                Socket socket = serverSocket.accept();
                addNewClient(socket);
            }
            clients.stream()
                    .map(Thread::new)
                    .forEach(Thread::start);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addNewClient(Socket socket) {
        Client client = new Client(socket, ++num);
        clients.add(client);
        System.out.println("New client connected! Number of clients: " + num);
        pointsService.createClient(num);
    }

    private void removeClient(Client client) {
        clients.remove(client);
        num--;
    }

    private class Client extends Thread {
        private PrintWriter writer;
        private Scanner reader;
        private Socket socket;
        private int num;


        Client(Socket socket, int num) {
            try {
                this.socket = socket;
                this.num = num;
                reader = new Scanner(socket.getInputStream());
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        @Override
        public void run() {
            writer.println("start");
            System.out.println("Client " + num + " start");

            while (true) {
                try {
                    if (reader.hasNextLine()) {
                        String input = reader.nextLine();

                        if (input.equals("outshoot")) {
                            pointsService.addShot(num);
                        } else if(input.equals("hit")) {
                            pointsService.addHit(num);
                        } else if (input.equals("gameOver")) {
                            clients.stream()
                                    .filter(client -> this.num != client.num)
                                    .forEach(client -> client.writer.println(input));

                            String statistics = pointsService.getStatistics(num);
                            writer.println(statistics);
                            break;
                        }
                        clients.stream()
                                .filter(client -> this.num != client.num)
                                .forEach(client -> client.writer.println(input));
                    } else {
                        clients.stream()
                                .filter(client -> this.num != client.num)
                                .forEach(client -> client.writer.println("enemyLeftGame"));

                        exitClient();
                        return;
                    }
                } catch (Exception e) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }

        private void exitClient() {
            try {
                removeClient(this);
                reader.close();
                writer.flush();
                writer.close();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}