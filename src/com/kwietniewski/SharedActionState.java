package com.kwietniewski;

import java.net.*;
import java.io.*;

public class SharedActionState{

    private SharedActionState mySharedObj;
    private int myThreadName;
    private double mySharedVariable;
    private int carQueue = 0;
    final private int carParkSpaceLimit = 5;
    private int carCurrentlyParked = 0;
    private boolean accessing=false; // true a thread has a lock, false otherwise
    private int threadsWaiting=0; // number of waiting writers

// Constructor

    SharedActionState(double SharedVariable) {
        mySharedVariable = SharedVariable;
    }

//Attempt to aquire a lock

    public synchronized void acquireLock() throws InterruptedException{
        Thread me = Thread.currentThread(); // get a ref to the current thread
        System.out.println(me.getName()+" is attempting to acquire a lock!");
        ++threadsWaiting;
        while (accessing) {  // while someone else is accessing or threadsWaiting > 0
            System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
            //wait for the lock to be released - see releaseLock() below
            wait();
        }
        // nobody has got a lock so get one
        --threadsWaiting;
        accessing = true;
        System.out.println(me.getName()+" got a lock!");
    }

    // Releases a lock to when a thread is finished

    public synchronized void releaseLock() {
        //release the lock and tell everyone
        accessing = false;
        notifyAll();
        Thread me = Thread.currentThread(); // get a ref to the current thread
        System.out.println(me.getName()+" released a lock!");
    }


    /* The processInput method */

    public synchronized String processInput(int myThreadName, String theInput) {
        System.out.println("SERVER []: " + myThreadName + " : "+ theInput);
        String theOutput = null;
        // Check what the client said

        if (theInput.equals("")){
            if (carCurrentlyParked==5){
                carQueue++;
            }
            else {
                carCurrentlyParked++;
                System.out.println("SERVER []: " + myThreadName + " just parked.");
            }
        }

        //Return the output message to the ActionServer
        System.out.println(theOutput);
        return theOutput;
    }
}