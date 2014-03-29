package io.loli.sc.ui.qt;

import io.loli.sc.SystemMenu;
import io.loli.sc.ui.swing.SCLauncher;

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QSystemTrayIcon;

/**
 * QT托盘菜单类<br/>
 * User: choco(loli@linux.com) <br/>
 * Date: 2014年3月29日 <br/>
 * Time: 下午4:03:56 <br/>
 * 
 * @author choco
 */
public class QtSystemMenu implements SystemMenu {
    private QMenu trayIconMenu;
    private QSystemTrayIcon trayIcon;

    @Override
    public void run() {
        QApplication.initialize(new String[0]);
        setupSystemTray();
        QApplication.exec();
    }

    // 托盘菜单的一系列事件
    protected void fullScreenAction() {
        SCLauncher.launch("full");
    }

    protected void selectScreenAction() {
        SCLauncher.launch("select");
    }

    protected void settingAction() {
        Ui_Settings settings = new Ui_Settings();
        settings.setupUi(new QDialog());
    }

    protected void quitAction() {
        System.exit(0);
    }

    // QT的托盘菜单并添加事件
    private void setupSystemTray() {
        if (QSystemTrayIcon.isSystemTrayAvailable()) {
            trayIconMenu = new QMenu();
            QAction fullAction = new QAction("全屏截图", null);
            fullAction.triggered.connect(this, "fullScreenAction()");
            trayIconMenu.addAction(fullAction);

            QAction selectAction = new QAction("选择截图", null);
            selectAction.triggered.connect(this, "selectScreenAction()");
            trayIconMenu.addAction(selectAction);

            QAction settingsAction = new QAction("设置", null);
            settingsAction.triggered.connect(this, "settingAction()");
            trayIconMenu.addAction(settingsAction);

            QAction quitAction = new QAction("退出", null);
            quitAction.triggered.connect(this, "quitAction()");
            trayIconMenu.addAction(quitAction);

            trayIconMenu.addSeparator();
            trayIcon = new QSystemTrayIcon();
            trayIcon.setToolTip("System trayIcon example");
            trayIcon.setContextMenu(trayIconMenu);
            trayIcon.setIcon(new QIcon(new QPixmap(ICON_PATH)));
            trayIcon.show();
        }
    }

    private static final String ICON_PATH = "classpath:icon.png";
}
