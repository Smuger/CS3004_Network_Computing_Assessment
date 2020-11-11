package com.kwietniewski;

import java.net.*;
import java.io.*;


public class ActionServerThread extends Thread {


    private Socket actionSocket;
    private SharedActionState mySharedActionStateObject;
    private int myActionServerThreadName;
    private double mySharedVariable;

    public ActionServerThread(Socket actionSocket, int ActionServerThreadName, SharedActionState SharedObject) {
        this.actionSocket = actionSocket;
        mySharedActionStateObject = SharedObject;
        myActionServerThreadName = ActionServerThreadName;
    }

    public void run() {
        try {
            System.out.println("SERVER []: SERVER ASSIGNED FOLLOWING ID TO THE NEW CLIENT: " + myActionServerThreadName);
            PrintWriter out = new PrintWriter(actionSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(actionSocket.getInputStream()));
            String inputLine, outputLine;

            while ((inputLine = in.readLine()) != null) {
                // Get a lock first
                try {
                    System.out.println("SERVER []: INPUT FROM: " + myActionServerThreadName + ": " + inputLine);
                    mySharedActionStateObject.acquireLock();
                    outputLine = mySharedActionStateObject.processInput(myActionServerThreadName, inputLine, out);
                    out.println(outputLine);
                    mySharedActionStateObject.releaseLock();
                }
                catch(InterruptedException e) {
                    //System.err.println("Failed to get lock when reading:"+e);
                }
            }

            out.close();
            in.close();
            actionSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
