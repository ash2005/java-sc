package io.loli.sc.core;

public class HotKeyRegister {
    private static final String LIBRARY_NAME = "liblisten";

    static {
        if (System.getProperty("os.name").indexOf("Windows") >= 0
                || System.getProperty("os.name").indexOf("windows") >= 0) {
            System.load(LIBRARY_NAME + ".dll");
        } else {
            System.load("/home/choco/download/home/soft/Workspaces/eclipse/sc/target/classes/liblisten.so");
        }
    }

    private static native int bindAndListen(int i);

    public static void main(String[] args) {
        System.out.println(bindAndListen(5));
    }
}
