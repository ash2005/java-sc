package io.loli.sc;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        new DragFrame(new Config());
        System.out.println("Frame closed");
        Thread.sleep(30000);
    }
}