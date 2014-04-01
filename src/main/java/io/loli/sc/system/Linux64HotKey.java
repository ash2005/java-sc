package io.loli.sc.system;

import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jxgrabkey.HotkeyConflictException;
import jxgrabkey.JXGrabKey;

public class Linux64HotKey {
    {
        // Load JXGrabKey lib
        System.load(ClassLoader.getSystemResource("libJXGrabKey.so").getFile());
    }
    private static final int MY_HOTKEY_INDEX = 1;
    // 加上瞬时关键字以支持并发访问
    private transient boolean canStop = false;

    /**
     * 判断是否能注册此快捷键
     * 
     * @param mask
     *            控制键
     * @param key
     *            数值键
     * @return 是否能注册此快捷键
     */
    public static boolean canRegister(int mask, int key) {
        boolean canRegist = false;
        try {
            JXGrabKey.getInstance().registerAwtHotkey(MY_HOTKEY_INDEX, mask,
                    key);
            canRegist = true;
        } catch (HotkeyConflictException e) {
        } finally {
            JXGrabKey.getInstance().unregisterHotKey(MY_HOTKEY_INDEX);
            JXGrabKey.getInstance().cleanUp();
        }
        return canRegist;
    }

    private ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 监听任务，当快捷键被触发时应该执行onHotKey方法
     * 
     * @author choco
     *
     */
    class CustomHotkeyListener implements jxgrabkey.HotkeyListener {
        private HotKeyTask task;

        CustomHotkeyListener(HotKeyTask task) {
            super();
            this.task = task;
        }

        /**
         * 当快捷键被触发时执行此方法，任务在新线程中启动
         */
        @Override
        public void onHotkey(int arg0) {
            executor.execute(new TaskRunnable(task));
        }

    }

    /**
     * 注册快捷键
     * 
     * @param mask
     *            控制键
     * @param key
     *            数值键
     * @param task
     *            需要执行的task
     */
    public void register(int mask, int key, final HotKeyTask task) {
        try {
            JXGrabKey.getInstance().registerAwtHotkey(MY_HOTKEY_INDEX, mask,
                    key);

            // Add HotkeyListener
            JXGrabKey.getInstance().addHotkeyListener(
                    new CustomHotkeyListener(task));
            executor.execute(new ListenRunnable());
        } catch (HotkeyConflictException e) {
            JXGrabKey.getInstance().unregisterHotKey(MY_HOTKEY_INDEX);
            JXGrabKey.getInstance().cleanUp();
        }
    }

    /**
     * 后台等待线程
     * 
     * @author choco
     *
     */
    class ListenRunnable implements Runnable {

        @Override
        public void run() {
            listen();
        }

    }

    /**
     * “等待线程”所需要执行的方法<br/>
     * 当canStop标记为true时，循环结束，“等待线程”结束
     */
    private void listen() {
        while (!canStop) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 监听程序停止，并取消快捷键注册
     */
    public void stop() {
        canStop = true;
        JXGrabKey.getInstance().unregisterHotKey(MY_HOTKEY_INDEX);
        JXGrabKey.getInstance().cleanUp();
        executor.shutdown();
    }

    /**
     * 运行任务的Runnable，在新线程中执行task.run()方法
     * 
     * @author choco
     *
     */
    class TaskRunnable implements Runnable {
        private HotKeyTask task;

        TaskRunnable(HotKeyTask task) {
            super();
            this.task = task;
        }

        @Override
        public void run() {
            task.run();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Linux64HotKey key = new Linux64HotKey();

        key.register(KeyEvent.CTRL_MASK | KeyEvent.ALT_MASK, KeyEvent.VK_1,
                new HotKeyTask() {

                    @Override
                    public void run() {
                        System.out.println("test");
                    }

                });
        Thread.sleep(10000);
        key.stop();
    }
}
