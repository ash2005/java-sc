package io.loli.sc;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class SCLauncher {

    public static void main(String[] args) {
        switch (args[0]) {
        case "option":
            new ConfigFrame(new Config());
            break;
        case "select":
            DragFrame df = new DragFrame();
            String s = df.getResult();
            ScreenCaptor.copyToClipboard(s);
            while (ifStrinClipboard(s)) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            break;
        case "full":
            ScreenCaptor sc = ScreenCaptor.newInstance();
            String result = sc.getLink();
            ScreenCaptor.copyToClipboard(result);
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
