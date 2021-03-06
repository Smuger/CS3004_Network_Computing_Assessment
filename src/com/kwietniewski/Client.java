package com.kwietniewski;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

public class Client extends Thread {
    /**
     * TODO: 1. carArrived() -> (ask server for permission to enter) [Server] "Arriving"
     * TODO: 2. carQueue() <- (position in the queue update) [Server] "Cars before you: 1"
     * TODO: 3. carEnter() <- (permission granted) [Server] "Entering"
     * TODO: 4. carLeave() -> (ask server for permission to leave) [Server] "Leaving car park"
     * TODO: 5. carLeave() <- (server confirmed, car left) [Server] "Goodbye"
     */
    private String currentState = "";
    private int uniqueID;

    public Client(int uniqueID){
        this.uniqueID = uniqueID;
    }

    @Override
    public void run() {
        Socket carClientSocket;
        PrintWriter out = null;
        BufferedReader in = null;
        int serverPort = 4545;
        String serverIP = "localhost";
        //String uniqueID = UUID.randomUUID().toString();

        try {
            carClientSocket = new Socket(serverIP, serverPort);
            out = new PrintWriter(carClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(carClientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("CLIENT [" + uniqueID + "]: " + "-ERROR-");
            System.err.println("CLIENT [" + uniqueID + "]: " + "-INVALID SERVER URL-");
            System.err.println("CLIENT [" + uniqueID + "]: " + "CLIENT: " + uniqueID);
            System.err.println("CLIENT [" + uniqueID + "]: " + "ERROR START: " + e + " :ERROR END");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("CLIENT [" + uniqueID + "]: " + "-ERROR-");
            System.err.println("CLIENT [" + uniqueID + "]: " + "-UNABLE TO CONNECT TO THE SERVER-");
            System.err.println("CLIENT [" + uniqueID + "]: " + "CLIENT: " + uniqueID);
            System.err.println("CLIENT [" + uniqueID + "]: " + "ERROR START: " + e + " :ERROR END");
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        //System.out.println("CLIENT [" + uniqueID + "]: " + "CONNECTION ESTABLISHED");
        try {
            // Park car
            Thread.sleep((long) (Math.random() * 200));
            out.println("arrived");
            currentState = "approaching";
            System.out.println("CLIENT ["+ uniqueID +"] [" + currentState + "] : " + "Hello server. I would like to park my car.");

            fromServer = in.readLine();

            if (fromServer.equals("queued")){
                currentState = "queued";
                System.out.println("CLIENT ["+ uniqueID +"] [" + currentState + "] : " + "CAR PARK CURRENTLY FULL. PLACED IN THE QUEUE.");
            }
            if (fromServer.equals("parked")){
                currentState = "parked";
                System.out.println("CLIENT ["+ uniqueID +"] [" + currentState + "] : " + "YOUR CAR WAS PARKED SUCCESSFULLY.");
            }

            if (currentState.equals("queued")){
                currentState = in.readLine();
                if (currentState.equals("parked")) {
                    System.out.println("CLIENT ["+ uniqueID +"] [" + currentState + "] : " + "YOUR CAR WAS PARKED SUCCESSFULLY.");
                }
            }

            // Leave car park
            Thread.sleep((long) (Math.random() * 2000));
            out.println("leave");
            System.out.println("CLIENT ["+ uniqueID +"] [" + currentState + "] : " + "Hello server. I would like to leave.");

            fromServer = in.readLine();
            if (fromServer.equals("leave")){
                currentState = "leave";
                System.out.println("CLIENT ["+ uniqueID +"] [" + currentState + "] : " + "CAR LEFT CAR PARK. GOODBYE");
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        //while (true) {
        //    try {
        //        fromUser = stdIn.readLine();
        //        if (fromUser != null) {
        //            System.out.println("CLIENT [" + uniqueID + "]: " + fromUser + " to ActionServer");
        //            out.println(fromUser);
        //        }
        //        fromServer = in.readLine();
        //        System.out.println("SERVER []: " + fromServer);
        //    }
        //    catch (Exception e){
        //
        //    }
        //}


    }
}
