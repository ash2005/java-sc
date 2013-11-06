package io.loli.sc;

import io.loli.sc.api.API;
import io.loli.sc.api.DropboxAPI;
import io.loli.sc.api.ImgurAPI;

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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class ScreenCaptor {
    private Config config;
    private String apiStr;
    private String link;

    private ScreenCaptor(String apiStr) {
        this.apiStr = apiStr;
        this.config = new Config();
        File scf = this.screenShot(config);
        API api = this.getAPI();
        link = api.upload(scf);
        this.copyToClipboard(link);
    }

    public static ScreenCaptor newInstance(String apiStr) {
        return new ScreenCaptor(apiStr);
    }

    private API getAPI() {
        API api = null;
        if (apiStr.equals("imgur")) {
            api = new ImgurAPI(config);
        } else if (apiStr.equals("dropbox")) {
            api = new DropboxAPI(config);
        }
        return api;
    }

    private void copyToClipboard(String content) {
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
    private File screenShot(Config config) {
        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit()
                .getScreenSize());
        BufferedImage screenCapture = null;
        try {
            screenCapture = new Robot().createScreenCapture(screen);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
        String name = sdf.format(new Date());
        File f = new File(config.getSavePath() + File.separator + name + ".png");
        try {
            // png格式比jpg格式清晰很多
            ImageIO.write(screenCapture, "png", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public String getLink() {
        return link;
    }

    
    public static void main(String[] args) {
        ScreenCaptor.newInstance("imgur");
        System.out.println("------");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}