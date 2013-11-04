package io.loli.sc;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class ScreenCaptor {
    public static void main(String[] args) {
        ScreenCaptor.screenShot(new Config());
    }

    /**
     * 截图
     * 
     * @param savePath
     *            保存图片的路径
     * @return 图片文件
     */
    public static File screenShot(Config config) {
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
}
