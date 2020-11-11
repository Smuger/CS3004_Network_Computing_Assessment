package com.kwietniewski;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class SharedActionState{

    private SharedActionState mySharedObj;
    private int myThreadName;
    private double mySharedVariable;
    private Queue<Integer> carQueue = new LinkedList<>();
    // TODO: Change this to 5
    final private int CARPARKSPACELIMIT = 2;
    private ArrayList<Integer> carCurrentlyParked = new ArrayList<>();
    private boolean accessing=false; // true a thread has a lock, false otherwise
    private int threadsWaiting=0; // number of waiting writers
    HashMap<Integer, PrintWriter> myThreadNameBySocketOut = new HashMap<Integer, PrintWriter>();


// Constructor

    SharedActionState(double SharedVariable) {
        mySharedVariable = SharedVariable;
    }

    public synchronized HashMap getAllOutSockets(){
        return myThreadNameBySocketOut;
    }

//Attempt to aquire a lock

    public synchronized void acquireLock() throws InterruptedException{
        Thread me = Thread.currentThread(); // get a ref to the current thread
        //System.out.println(me.getName()+" is attempting to acquire a lock!");
        ++threadsWaiting;
        while (accessing) {  // while someone else is accessing or threadsWaiting > 0
            //System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
            //wait for the lock to be released - see releaseLock() below
            wait();
        }
        // nobody has got a lock so get one
        --threadsWaiting;
        accessing = true;
        //System.out.println(me.getName()+" got a lock!");
    }

    // Releases a lock to when a thread is finished

    public synchronized void releaseLock() {
        //release the lock and tell everyone
        accessing = false;
        notifyAll();
        Thread me = Thread.currentThread(); // get a ref to the current thread
        //System.out.println(me.getName()+" released a lock!");
    }


    /* The processInput method */

    public synchronized String processInput(int myThreadName, String theInput,PrintWriter out) {
        if (!myThreadNameBySocketOut.containsKey(myThreadName)){
            myThreadNameBySocketOut.put(myThreadName, out);
        }

        System.out.println("SERVER []: " + myThreadName + " : "+ theInput);

        String theOutput = null;
        // Check what the client said

        if (theInput.equals("arrived")){
            if (carCurrentlyParked.size()==CARPARKSPACELIMIT){
                carQueue.add(myThreadName);
                System.out.println("SERVER []: " + myThreadName + " was put in a queue.");
                theOutput = "queued";
            }
            else {
                carCurrentlyParked.add(myThreadName);
                System.out.println("SERVER []: " + myThreadName + " just parked.");
                theOutput = "parked";
            }
        }
        else if (theInput.equals("leave")){
            theOutput = "leave";
            if (carCurrentlyParked.contains(myThreadName)){
                carCurrentlyParked.remove(carCurrentlyParked.lastIndexOf(myThreadName));
            }
            else if (carQueue.contains(myThreadName)) {
                carQueue.remove(myThreadName);
            }
            if (carQueue.size()>0){
                int carFromQueue = carQueue.poll();
                PrintWriter outQueue = myThreadNameBySocketOut.get(carFromQueue);
                outQueue.println("parked");
                carCurrentlyParked.add(carFromQueue);
                System.out.println("SERVER []: " + myThreadName + " moved from queue to park.");
            }
            else {
                theOutput = "carParkEmpty";
            }
        }
        else {
            System.out.println("SERVER []: " + myThreadName + " SERVER CONFUSED. Server did not know what to do with your message. Please try again.");
            theOutput = "confused";
        }

        System.out.println("SERVER []: QUEUE: " + carQueue.size());
        System.out.println("SERVER []: PARKED: " + carCurrentlyParked.size());

        //Return the output message to the ActionServer
        System.out.println(theOutput);

        return theOutput;
    }
}