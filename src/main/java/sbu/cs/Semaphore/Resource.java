package sbu.cs.Semaphore;

import java.util.concurrent.Semaphore;

public class Resource {
    public static void accessResource() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}