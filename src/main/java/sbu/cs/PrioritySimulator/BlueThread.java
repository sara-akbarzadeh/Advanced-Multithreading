package sbu.cs.PrioritySimulator;
public class BlueThread extends ColorThread {

    private static final String MESSAGE = "hi finished blacks, hi whites!";

    @Override
    public void run() {
        printMessage();
    }

    void printMessage() {
        super.printMessage(new Message(this.getClass().getName(), getMessage()));
    }

    @Override
    String getMessage() {
        return MESSAGE;
    }
}