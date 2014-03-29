package io.loli.sc.ui.qt;

import io.loli.sc.SystemMenu;

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QSystemTrayIcon;

public class QtSystemMenu implements SystemMenu {
	private QMenu trayIconMenu;
	private QSystemTrayIcon trayIcon;

	@Override
	public void run() {

		QApplication.initialize(new String[0]);
		setupSystemTray();
		QApplication.exec();
	}

	private void setupSystemTray() {
		if (QSystemTrayIcon.isSystemTrayAvailable()) {
			trayIconMenu = new QMenu();
			QAction toggleVisibilityAction = new QAction("Show/Hide", null);
			trayIconMenu.addAction(toggleVisibilityAction);

			QAction restoreAction = new QAction("Restore", null);
			trayIconMenu.addAction(restoreAction);

			QAction minimizeAction = new QAction("Minimize", null);
			trayIconMenu.addAction(minimizeAction);

			QAction maximizeAction = new QAction("Maximize", null);
			trayIconMenu.addAction(maximizeAction);

			trayIconMenu.addSeparator();

			QAction quitAction = new QAction("&Quit", null);
			trayIconMenu.addAction(quitAction);
			trayIcon = new QSystemTrayIcon();
			trayIcon.setToolTip("System trayIcon example");
			trayIcon.setContextMenu(trayIconMenu);
			trayIcon.setIcon(new QIcon(new QPixmap(ICON_PATH)));
			trayIcon.show();
		}
	}

	private static final String ICON_PATH = "classpath:icon.png";
}
