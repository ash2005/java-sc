package io.loli.sc;

import io.loli.sc.config.Config;
import io.loli.sc.system.HotKeyRegister;
import io.loli.sc.system.HotKeyTask;
import io.loli.sc.system.Linux64HotKeyRegister;
import io.loli.sc.ui.SystemMenu;
import io.loli.sc.ui.swing.SCLauncher;
import io.loli.sc.ui.swing.SwingSystemMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private static Config config = new Config();
    private static List<HotKeyRegister> registerList = new ArrayList<HotKeyRegister>();
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static HotKeyRegister register;

    static {
        String name = System.getProperty("os.name");
        if (name.toLowerCase().indexOf("linux") != -1) {
            String ver = System.getProperty("os.arch");
            if (ver.indexOf("64") != -1) {
                register = new Linux64HotKeyRegister(new HotKeyTask() {
                    public void run(int index) {
                        if (index == 0)
                            SCLauncher.launch("select");
                        if (index == 1)
                            SCLauncher.launch("full");
                    }
                });
            } else {
                // TODO x86
            }
        }
    }

    public static void main(String[] args) {
        showMenu();
        startup();
    }

    public static class OptionRunnable extends Thread {
        public OptionRunnable() {
            super();
        }

        private void regist(final String option) {
            int[] keys = config.getHotKeys(option);
            registerList.add(register);
            register.register(option.equals("select") ? 0 : 1, keys[0], keys[1]);
        }

        @Override
        public void run() {
            regist("select");
            regist("full");
            register.startListen();
        }
    }

    public static void showMenu() {
        // 托盘菜单线程
        executor.execute(new Thread() {
            @Override
            public void run() {
                menu = new SwingSystemMenu();
                menu.run();
            }
        });

    }

    public static void startup() {
        // 后台监听注册快捷键线程
        if (executor.isShutdown()) {
            executor = Executors.newCachedThreadPool();
        }
        executor.execute(new OptionRunnable());

    }

    public static void shutdown() {
        executor.shutdown();
    }

    public static void restart() {
        shutdown();
        startup();
    }

}
