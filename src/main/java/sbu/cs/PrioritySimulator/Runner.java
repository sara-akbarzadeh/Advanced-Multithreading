package sbu.cs.PrioritySimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Runner {

    public static List<Message> messages = new ArrayList<>();
    private static CountDownLatch blackLatch, blueLatch;

    /**
     * Simulate a priority system between Black, white, and blue threads.
     * Given a number of each thread type, your program must first execute every Black thread, then
     * execute every Blue thread, and finally execute every white thread. (Black > Blue > White)
     * Complete each thread type class by adding the specified code in the respective run() methods.
     *
     * The order of execution only matters when comparing two different thread types. This means two
     * black threads are not prioritized over each other (can swap their execution order) but between a
     * Black thread and a Blue thread, the Black thread must fully execute before the Blue thread starts.
     *
     * You are NOT ALLOWED to use the Thread.Sleep() method to solve this problem, as it does not
     * guarantee the desired thread execution order.
     * You can utilize other classes and methods available in the java.util.concurrent package. It is
     * recommended that you use the "CountDownLatch" class to solve the problem.
     *
     * You are allowed to change the existing code in any of the given classes or add your own new code
     * to any of them.
     * Your code must pass all of the test cases provided in the test folder.
     *
     * @param blackCount    number of black threads
     * @param blueCount     number of blue threads
     * @param whiteCount    number of white threads
     */
    public void run(int blackCount, int blueCount, int whiteCount) throws InterruptedException {
        blackLatch = new CountDownLatch(blackCount);
        blueLatch = new CountDownLatch(blueCount);

        List<ColorThread> colorThreads = new ArrayList<>();

        // Create and start Black threads
        for (int i = 0; i < blackCount; i++) {
            BlackThread blackThread = new BlackThread();
            colorThreads.add(blackThread);
            blackThread.start();
        }

        // Wait for all Black threads to finish
        blackLatch.await();

        // Create and start Blue threads
        for (int i = 0; i < blueCount; i++) {
            BlueThread blueThread = new BlueThread();
            colorThreads.add(blueThread);
            blueThread.start();
        }

        // Wait for all Blue threads to finish
        blueLatch.await();

        // Create and start White threads
        for (int i = 0; i < whiteCount; i++) {
            WhiteThread whiteThread = new WhiteThread();
            colorThreads.add(whiteThread);
            whiteThread.start();
        }

        // Wait for all threads to finish
        for (ColorThread thread : colorThreads) {
            thread.join();
        }
    }

    synchronized public static void addToList(Message message) {
        messages.add(message);
        if (message.getCallerThreadColor().equals("BlackThread")) {
            blackLatch.countDown();
        } else if (message.getCallerThreadColor().equals("BlueThread")) {
            blueLatch.countDown();
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public static void main(String[] args) {
        Runner runner = new Runner();
        try {
            runner.run(2, 3, 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(runner.getMessages());
    }
}
