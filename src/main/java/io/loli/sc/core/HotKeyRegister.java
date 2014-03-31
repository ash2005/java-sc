package io.loli.sc.core;

public class HotKeyRegister {
    // 定义动态链接库的名称，这个看个人喜好可随便起名
    private static final String LIBRARY_NAME = "liblisten";

    static {
        // 根据操作系统加载相应的动态连接库
        if (System.getProperty("os.name").indexOf("Windows") >= 0
                || System.getProperty("os.name").indexOf("windows") >= 0) {
            System.load(LIBRARY_NAME + ".dll");
        } else {
            // Linux中动态连接库的命名规范：lib库名.so
            System.load("/home/choco/download/home/soft/Workspaces/eclipse/sc/target/classes/liblisten.so");
        }
    }

    private static native int bindAndListen(int i); // 用native的是jni方法，在C语言中实现

    public static void main(String[] args) {
        System.out.println(bindAndListen(5));
    }
}
