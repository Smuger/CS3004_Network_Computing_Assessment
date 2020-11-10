package com.kwietniewski;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ThreadLocalRandom;

public class Server extends Thread{
    private int ServerPort;
    private String ServerName;

    public Server(int ServerPort, String ServerName) {
        this.ServerPort = ServerPort;
        this.ServerName = ServerName;
    }
    /**
     * TODO: 1. carArrived() <- (client requesting arrival) [Client] "Arriving"
     * TODO: 2. carQueue() -> (position in the queue update) [Client] "Cars before you: 1"
     * TODO: 3. carEnter() -> (permission granted) [Client] "Entering"
     * TODO: 4. carLeave() <- (client requesting to leave) [Client] "Leaving car park"
     * TODO: 5. carLeave() -> (car left) [Client] "Goodbye"
     */
    public void run(){
        boolean mainProgramLoop = true;
        ServerSocket ActionServerSocket;
        double carParkSpaceLeftFree = 5;
        SharedActionState ourSharedActionStateObject = new SharedActionState(carParkSpaceLeftFree);

        try {
            ActionServerSocket = new ServerSocket(ServerPort);
            System.out.println("SERVER []: " + "-SERVER STARTED-");

            while (mainProgramLoop){
                new ActionServerThread(ActionServerSocket.accept(),ThreadLocalRandom.current().nextInt(), ourSharedActionStateObject).start();
                System.out.println("SERVER []: " + "-CLIENT CONNECTED-");
            }
            ActionServerSocket.close();
        } catch (IOException e) {
            System.err.println("SERVER []: " + "-ERROR-");
            System.err.println("SERVER []: " + "-SERVER FAILED TO START-");
            System.err.println("SERVER []: " + "ERROR START: " + e + " :ERROR END");
            System.exit(-1);
        }

    }
}
