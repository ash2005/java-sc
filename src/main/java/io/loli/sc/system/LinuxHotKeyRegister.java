package io.loli.sc.system;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jxgrabkey.HotkeyConflictException;
import jxgrabkey.HotkeyListener;
import jxgrabkey.JXGrabKey;

public class LinuxHotKeyRegister implements HotKeyRegister {
	private static String LINUX64_LIB_NAME = "libJXGrabKey.so";
	private static String LINUX32_LIB_NAME = "libJXGrabKey32.so";
	static {
		// Load JXGrabKey lib
		String libName = null;
		String ver = System.getProperty("os.arch");
		if (ver.indexOf("64") != -1) {
			libName = LINUX64_LIB_NAME;
		} else {
			libName = LINUX32_LIB_NAME;
		}
		System.load(ClassLoader.getSystemResource(libName).getFile());
	}
	// 加上瞬时关键字以支持并发访问
	private transient boolean canStop = false;

	/**
	 * 构造方法
	 * 
	 * @param task
	 *            在监听到按键时需要执行的方法
	 */
	public LinuxHotKeyRegister(HotKeyTask task) {
		this.task = task;
	}

	private JXGrabKey getHotkey() {
		return JXGrabKey.getInstance();
	}

	/**
	 * 判断是否能注册此快捷键
	 * 
	 * @param mask
	 *            控制键
	 * @param key
	 *            数值键
	 * @return 是否能注册此快捷键
	 */
	@SuppressWarnings("finally")
	public synchronized boolean canRegister(int index, int mask, int key) {
		boolean canRegist = false;
		try {
			getHotkey().registerAwtHotkey(index, mask, key);
			getHotkey().addHotkeyListener(new HotkeyListener() {
				@Override
				public void onHotkey(int arg0) {
				}
			});
			canRegist = true;

		} catch (HotkeyConflictException e) {
			canRegist = false;
		} finally {
			this.stop();
			return canRegist;
		}

	}

	// 执行线程用的线程池
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
		public void onHotkey(int index) {
			executor.execute(new TaskRunnable(index, task));
		}

	}

	private HotKeyTask task;

	public HotKeyRegister setTask(HotKeyTask task) {
		this.task = task;
		return this;
	}

	/**
	 * 开始监听按键
	 */
	@Override
	public void startListen() {
		getHotkey().addHotkeyListener(new CustomHotkeyListener(task));
		listen();
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
	public void register(int index, int mask, int key) {
		canStop = false;
		try {
			getHotkey().registerAwtHotkey(index, mask, key);
		} catch (HotkeyConflictException e) {
			e.printStackTrace();
			this.stop();
		}
	}

	/**
	 * 后台等待线程
	 * 
	 * @author choco
	 * 
	 */
	class ListenRunnable extends Thread {

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
		getHotkey().cleanUp();
		executor.shutdown();
	}

}
