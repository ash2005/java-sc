package io.loli.sc;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

public class Config {
    private static final String CONFIG_DIR = ".SC-JAVA";
    private static final String CONFIG_FILE = "config.properties";
    private static String staticSavePath;

    /**
     * 打开图片保存文件夹
     */
    private void openFolder() {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        try {
            desktop.open(new File(staticSavePath));
        } catch (IOException e) {
        }
    }

    private static void init() {
        File path = FileSystemView.getFileSystemView().getHomeDirectory();
        File propDir = new File(path.getAbsolutePath() + File.separator
                + CONFIG_DIR);
        if (!propDir.exists()) {
            propDir.mkdir();
        }
        File propFile = new File(propDir.getAbsolutePath() + File.separator
                + CONFIG_FILE);
        // 如果prop文件不存在则创建
        if (!propFile.exists()) {
            Properties properties = new Properties();
            properties.setProperty("savePath", propDir.getAbsolutePath());
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(propFile);
                properties.store(output, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 读取prop文件
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(propFile));
            props.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        staticSavePath = props.getProperty("savePath");

    }

    // 初始化
    static {
        init();
    }

    public Config() {
        this.setSavePath(staticSavePath);
    }

    private String savePath;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

}
