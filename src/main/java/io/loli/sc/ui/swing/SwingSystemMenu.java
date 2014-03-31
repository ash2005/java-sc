package io.loli.sc.ui.swing;

import io.loli.sc.SystemMenu;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import tray.SystemTrayAdapter;
import tray.SystemTrayProvider;

public class SwingSystemMenu implements SystemMenu {

    public static void main(String[] args) {
        new SwingSystemMenu().run();
    }

    private PopupMenu generateMenu() {
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
        return popup;
    }

    private ActionListener listener;

    @Override
    public void run() {
        // 判断该系统是否支持"系统托盘"
        if (SystemTray.isSupported()) {
            listener = new ActionListener() {
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

            SystemTrayAdapter trayAdapter = new SystemTrayProvider()
                    .getSystemTray();
            URL imageUrl = ClassLoader.getSystemResource("icon.png");
            String tooltip = "ImageCloud";
            trayAdapter.createAndAddTrayIcon(imageUrl, tooltip,
                    this.generateMenu());
        } else {
        }

    }

}