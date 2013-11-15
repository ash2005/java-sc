package io.loli.sc;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

public class Config {
    private static final String CONFIG_DIR = ".SC-JAVA";
    private static final String CONFIG_FILE = "config.properties";
    private File path;
    private File propDir;
    private File propFile;
    private Properties properties;
    private ImgurConfig imgurConfig;
    private DropboxConfig dropboxConfig;
    private GDriveConfig gdriveConfig;

    /**
     * 打开图片保存文件夹
     */
    @SuppressWarnings("unused")
    private void openFolder() {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        try {
            desktop.open(new File(savePath));
        } catch (IOException e) {
        }
    }

    /**
     * config转换成properties对象
     * 
     * @return 转换后的properties对象
     */
    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
        }
        properties.setProperty("savePath", getSavePath());
        properties.setProperty("defaultUpload", getDefaultUpload());
        return properties;
    }

    private void init() {
        // 先是初始化各个目录
        path = FileSystemView.getFileSystemView().getHomeDirectory();
        propDir = new File(path.getAbsolutePath() + File.separator + CONFIG_DIR);
        propFile = new File(propDir.getAbsolutePath() + File.separator
                + CONFIG_FILE);
        // 不存在时创建
        if (!propDir.exists()) {
            propDir.mkdir();
        }
        if (!propFile.exists()) {
            save();
        }
        // 读取prop设置
        read();
    }

    // 初始化
    {
        init();
    }

    public Config() {
    }

    private String savePath;

    private String defaultUpload;

    /**
     * 获取保存路径
     * 
     * @return 保存路径 当其为空时使用默认目录
     */
    public String getSavePath() {
        if (savePath == null) {
            savePath = propDir.getAbsolutePath();
        }
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    /**
     * 保存设置
     */
    public void save() {
        properties = getProperties();
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(propFile);
            properties.store(output, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取prop设置
     */
    public void read() {
        properties = getProperties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(propFile));
            properties.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.setSavePath(properties.getProperty("savePath"));
        this.setDefaultUpload(properties.getProperty("defaultUpload"));
        // imgurConfig
        if (properties.getProperty("imgur.date") != null) {
            imgurConfig = new ImgurConfig();
            try {
                imgurConfig.setDate(new SimpleDateFormat("yyyyMMddHHmmss")
                        .parse(properties.getProperty("imgur.date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            imgurConfig.setRefreshToken(properties
                    .getProperty("imgur.refreshToken"));
            imgurConfig.setAccessToken(properties
                    .getProperty("imgur.accessToken"));
        }
        if (properties.getProperty("dropbox.uid") != null) {
            dropboxConfig = new DropboxConfig();
            dropboxConfig.setAccessToken(properties
                    .getProperty("dropbox.accessToken"));
            dropboxConfig.setUid(properties.getProperty("dropbox.uid"));
        }
        if (properties.getProperty("gdrive.accessToken") != null) {
            gdriveConfig = new GDriveConfig();
            gdriveConfig.setAccessToken(properties
                    .getProperty("gdrive.accessToken"));
            gdriveConfig.setRefreshToken(properties
                    .getProperty("gdrive.refreshToken"));
        }
    }

    public ImgurConfig getImgurConfig() {
        if (imgurConfig == null) {
            imgurConfig = new ImgurConfig();
        }
        return imgurConfig;
    }

    public void setImgurConfig(ImgurConfig imgurConfig) {
        this.imgurConfig = imgurConfig;
    }

    public DropboxConfig getDropboxConfig() {
        if (dropboxConfig == null) {
            dropboxConfig = new DropboxConfig();
        }
        return dropboxConfig;
    }

    public void setDropboxConfig(DropboxConfig dropboxConfig) {
        this.dropboxConfig = dropboxConfig;
    }

    public String getDefaultUpload() {
        if (defaultUpload == null) {
            defaultUpload = "";
        }
        return defaultUpload;
    }

    public void setDefaultUpload(String defaultUpload) {
        this.defaultUpload = defaultUpload;
    }

    public GDriveConfig getGdriveConfig() {
        if (gdriveConfig == null) {
            gdriveConfig = new GDriveConfig();
        }
        return gdriveConfig;
    }

    public void setGdriveConfig(GDriveConfig gdriveConfig) {
        this.gdriveConfig = gdriveConfig;
    }

}
