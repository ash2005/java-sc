package io.loli.sc.system;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

;
public class WindowsHotKeyRegister implements HotKeyRegister {
	private HotKeyTask task;
	private transient boolean canStop = false;

	public WindowsHotKeyRegister(HotKeyTask task) {
		this.task = task;
	}

	@SuppressWarnings("finally")
	@Override
	public boolean canRegister(int index, int mask, int key) {
		boolean canRegist = false;
		try {
			getHotkey().registerSwingHotKey(index, mask, key);
			getHotkey().addHotKeyListener(new HotkeyListener() {
				@Override
				public void onHotKey(int identifier) {
				}
			});
			canRegist = false;
		} catch (Exception e) {
			canRegist = false;
		} finally {
			this.stop();
			return canRegist;
		}
	}

	private JIntellitype getHotkey() {
		return JIntellitype.getInstance();
	}

	@Override
	public void register(int index, int mask, int key) {
		canStop = false;
		try {
			getHotkey().registerSwingHotKey(index, mask, key);
		} catch (Exception e) {
			e.printStackTrace();
			this.stop();
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

	// 执行线程用的线程池
	private ExecutorService executor = Executors.newCachedThreadPool();

	/**
	 * 监听程序停止，并取消快捷键注册
	 */
	public void stop() {
		canStop = true;
		getHotkey().cleanUp();
		executor.shutdown();
	}

	/**
	 * 监听任务，当快捷键被触发时应该执行onHotKey方法
	 * 
	 * @author choco
	 * 
	 */
	class CustomHotkeyListener implements
			com.melloware.jintellitype.HotkeyListener {
		private HotKeyTask task;

		CustomHotkeyListener(HotKeyTask task) {
			super();
			this.task = task;
		}

		/**
		 * 当快捷键被触发时执行此方法，任务在新线程中启动
		 */
		@Override
		public void onHotKey(int index) {
			executor.execute(new TaskRunnable(index, task));
		}

	}

	/**
	 * 开始监听按键
	 */
	@Override
	public void startListen() {
		getHotkey().addHotKeyListener(new CustomHotkeyListener(task));
		listen();
	}
}