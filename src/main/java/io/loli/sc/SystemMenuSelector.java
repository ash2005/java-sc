package io.loli.sc;

import io.loli.sc.ui.qt.QtSystemMenu;

public class SystemMenuSelector {
	private static SystemMenu menu;
	public static void main(String[] args){
		menu = new QtSystemMenu();
		menu.run();
	}
}
