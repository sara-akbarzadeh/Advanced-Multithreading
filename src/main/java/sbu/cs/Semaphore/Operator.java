package sbu.cs.Semaphore;

import java.util.concurrent.Semaphore;

public class Operator extends Thread {

    private static Semaphore semaphore = new Semaphore(2);

    public Operator(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                semaphore.acquire(); // acquire the permit
                System.out.println(getName() + " entered the critical section at " + System.currentTimeMillis());
                Resource.accessResource(); // critical section
                System.out.println(getName() + " left the critical section at " + System.currentTimeMillis());
                semaphore.release(); // release the permit
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}