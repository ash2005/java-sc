package io.loli.sc.core;

import io.loli.sc.api.API;
import io.loli.sc.api.DropboxAPI;
import io.loli.sc.api.GDriveAPI;
import io.loli.sc.api.ImageCloudAPI;
import io.loli.sc.api.ImgurAPI;
import io.loli.sc.config.Config;
import io.loli.util.FileNameGenerator;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenCaptor {
	private Config config;
	private volatile String link;
	private String apiStr;

	// 选择上传时不必保存
	private ScreenCaptor(boolean upload) {
		this.config = new Config();
	}

	private ScreenCaptor(String apiStr) {
		this.config = new Config();
		this.apiStr = apiStr;
		File scf = this.screenShotSave();
		link = this.uploadFile(scf);
	}

	private ScreenCaptor() {
		this.config = new Config();
		this.apiStr = config.getDefaultUpload();
		File scf = this.screenShotSave();
		link = this.uploadFile(scf);
	}

	public static ScreenCaptor newInstance(String apiStr) {
		return new ScreenCaptor(apiStr);
	}

	public static ScreenCaptor newInstance() {
		return new ScreenCaptor();
	}

	public static ScreenCaptor newInstance(boolean upload) {
		return new ScreenCaptor(false);
	}

	private API getAPI() {
		API api = null;
		if (apiStr.equals("imgur")) {
			api = new ImgurAPI(config);
		} else if (apiStr.equals("dropbox")) {
			api = new DropboxAPI(config);
		} else if (apiStr.equals("gdrive")) {
			api = new GDriveAPI(config);
		} else if (apiStr.equals("imgCloud")) {
			api = new ImageCloudAPI(config);
		}
		return api;
	}

	public static void copyToClipboard(String content) {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable t = new StringSelection(content);
		cb.setContents(t, null);
	}

	/**
	 * 截图
	 * 
	 * @param savePath
	 *            保存图片的路径
	 * @return 图片文件
	 */
	public File screenShotSave() {

		BufferedImage screenCapture = screenShot();

		File imgFile = saveImageToFile(screenCapture);
		return imgFile;
	}

	private File saveImageToFile(BufferedImage img) {
		File f = new File(config.getSavePath() + File.separator
				+ FileNameGenerator.generate(config.getFileNameFormat())
				+ ".png");
		try {
			// png格式比jpg格式清晰很多
			ImageIO.write(img, "png", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	private String uploadFile(File file) {
		return this.getAPI().upload(file);
	}

	public String uploadImage(String apiStr, BufferedImage image) {
		this.apiStr = apiStr;
		return uploadFile(saveImageToFile(image));
	}

	/**
	 * 判断系统，并调用相应系统下的方法
	 * 
	 * @return
	 */
	public static BufferedImage screenShot() {
		String system = System.getProperty("os.name").toLowerCase();
		if (system.indexOf("windows") >= 0) {
			return winScreenShot();
		} else if (system.indexOf("linux") >= 0) {
			return linuxScreenShot();
		} else if (system.indexOf("mac") >= 0) {
			return macScreenShot();
		} else {
			return winScreenShot();
		}
	}

	/**
	 * linux下的截图
	 * 
	 * @return 返回截图的BufferedImage对象
	 */
	private static BufferedImage linuxScreenShot() {
		File file = new File(System.getProperty("java.io.tmpdir"),
				FileNameGenerator.generate() + ".png");
		try {
			// 需要加waitFor()等待执行结束
			Runtime.getRuntime()
					.exec(new String[] { "/bin/sh", "-c",
							"import -window root " + file.getCanonicalPath() })
					.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		BufferedImage screenCapture = null;
		try {
			screenCapture = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return screenCapture;
	}

	/**
	 * windows下的截图
	 * 
	 * @return 返回截图的BufferedImage对象
	 */
	private static BufferedImage winScreenShot() {
		Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit()
				.getScreenSize());
		BufferedImage screenCapture = null;
		try {
			screenCapture = new Robot().createScreenCapture(screen);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		return screenCapture;
	}

	/**
	 * mac下的截图
	 * 
	 * @return 返回截图的BufferedImage对象
	 */
	private static BufferedImage macScreenShot() {
		return winScreenShot();
	}

	public String getLink() {
		while (link == null || link.equals("")) {
		}
		return link;
	}

	public static void main(String[] args) {
		ScreenCaptor sc = ScreenCaptor.newInstance();
		ScreenCaptor.copyToClipboard(sc.getLink());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}
