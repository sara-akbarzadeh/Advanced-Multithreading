package sbu.cs.PrioritySimulator;
public class BlackThread extends ColorThread {

    private static final String MESSAGE = "hi blues, hi whites!";

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