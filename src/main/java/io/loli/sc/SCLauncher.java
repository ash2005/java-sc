package io.loli.sc;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class SCLauncher {
    public static void launch(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                case "option":
                    new ConfigFrame(new Config());
                    break;
                case "select":
                    try {
                        // 如果不延迟会连菜单一起捕捉到
                        Thread.sleep(200);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    DragFrame df = new DragFrame();
                    String s = df.getResult();
                    ScreenCaptor.copyToClipboard(s);
                    new MP3Player().play(ClassLoader
                            .getSystemResourceAsStream("message.mp3"));
                    while (ifStrinClipboard(s)) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "full":
                    try {
                        // 如果不延迟会连菜单一起捕捉到
                        Thread.sleep(200);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    ScreenCaptor sc = ScreenCaptor.newInstance();
                    String result = sc.getLink();
                    ScreenCaptor.copyToClipboard(result);
                    new MP3Player().play(ClassLoader
                            .getSystemResourceAsStream("message.mp3"));
                    while (ifStrinClipboard(result)) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }

            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args[0]);
    }

    public static boolean ifStrinClipboard(String str) {
        return getClipboard().equals(str);
    }

    public static String getClipboard() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tra = cb.getContents(null);
        String result = null;
        try {
            result = (String) tra.getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
