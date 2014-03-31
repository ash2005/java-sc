package io.loli.sc;

import io.loli.sc.ui.swing.SwingSystemMenu;

/**
 * 程序的启动类<br/>
 * User: choco(loli@linux.com) <br/>
 * Date: 2014年3月29日 <br/>
 * Time: 下午4:06:21 <br/>
 *
 * @author choco
 */
public class SystemMenuSelector {
    private static SystemMenu menu;

    public static void main(String[] args) {
        /**
         * 根据系统类型来决定使用哪个Menu实现类 <br/>
         * 现在全部使用Swing
         */
        String osname = System.getProperty("os.name").toLowerCase();
        if (osname.indexOf("linux") >= 0) {
            menu = new SwingSystemMenu();
        } else if (osname.indexOf("windows") >= 0) {
            menu = new SwingSystemMenu();
        } else if (osname.indexOf("mac") >= 0) {
            menu = new SwingSystemMenu();
        } else {
            menu = new SwingSystemMenu();
        }
        menu.run();
    }
}
