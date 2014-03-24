package io.loli.sc;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;

public class SystemMenu {

    public static void main(String[] args) {
        new SystemMenu().run();
    }

    private SystemTray tray;// 系统托盘对象
    private TrayIcon trayIcon;// 托盘所代表的图标
    
    
    public void run() {
        Image image = readIcon();
        // 判断该系统是否支持"系统托盘"
        if (SystemTray.isSupported()) {
            tray = SystemTray.getSystemTray();
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String cmd = e.getActionCommand();// 获得托盘的事件或点击图标弹出菜单的菜单项
                    switch (cmd) {
                    case "全屏截图":
                        SCLauncher.launch("full");
                        break;
                    case "选择截图":
                        SCLauncher.launch("select");
                        break;
                    case "设置":
                        SCLauncher.launch("option");
                        break;
                    case "退出":
                        System.exit(0);
                    }
                }
            };
            // 创建弹出菜单
            PopupMenu popup = new PopupMenu();
            // 以下将各个菜单项加入到弹出菜单中
            MenuItem full = new MenuItem("全屏截图");
            full.addActionListener(listener);
            popup.add(full);
            MenuItem select = new MenuItem("选择截图");
            select.addActionListener(listener);
            popup.add(select);
            MenuItem settings = new MenuItem("设置");
            settings.addActionListener(listener);
            popup.add(settings);
            MenuItem quit = new MenuItem("退出");
            quit.addActionListener(listener);
            popup.add(quit);

            trayIcon = new TrayIcon(image, "sc-java", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(listener);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        } else {
        }

    }

    private BufferedImage readIcon() {
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new MemoryCacheImageInputStream(ClassLoader
                    .getSystemResourceAsStream("icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bimg;
    }
}