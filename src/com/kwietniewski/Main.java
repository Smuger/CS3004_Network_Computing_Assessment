package com.kwietniewski;

import java.io.IOException;

/**
 * Track of the number of available spaces as cars leave and enter a car park
 * The car park has two entrances and two exits.
 *
 * Car park has 5 spaces
 *
 * • Two clients [entrances]
 * • Two clients [exits]
 * • One server
 */
public class Main {

    //TODO: 1. Allowed to enter if there is space.
    //TODO: 2. Queue at the entrance until there is space.
    //TODO: 3. Car leaving the car reduce occupied space count.
    public static void main(String[] args) {
        new Server(4545,"ActionServer").start();
        new Client().start();
        new Client().start();
        new Client().start();
        new Client().start();
        new Client().start();
    }
}
