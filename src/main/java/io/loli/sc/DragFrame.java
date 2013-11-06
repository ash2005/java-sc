package io.loli.sc;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputAdapter;

public class DragFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JFrame jframe;
    private JPanel imgPanel;
    private JLabel bgLabel;
    private BufferedImage img;
    private DragPanel dragPanel;

    private void useSystemUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void fullScreen() {
        setVisible(true);
        validate();
        // 必须加这句否则linux下无法全屏
        dispose();
        setUndecorated(true);
        requestFocus();
        jframe.getGraphicsConfiguration().getDevice()
                .setFullScreenWindow(jframe);
    }

    private int a1, b1;
    private int a2, b2;

    class DragPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        public void processParentEvent(AWTEvent e) {
            this.processEvent(e);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            int w = a2 - a1;
            int h = b2 - b1;
            BufferedImage subImg = null;
            if (w > 0 && h > 0)
                subImg = img.getSubimage(a1, b1, w, h);
            if (w < 0 && h > 0) {
                subImg = img.getSubimage(a2, b1, -w, h);
            }
            if (w > 0 && h < 0) {
                subImg = img.getSubimage(a1, b2, w, -h);
            }
            if (w < 0 && h < 0) {
                subImg = img.getSubimage(a2, b2, -w, -h);
            }
            this.setBorder(BorderFactory.createLineBorder(Color.red));
            g.drawImage(subImg, 0, 0, null);
        }

        // private ImageInputStream ImageToStream(BufferedImage img) {
        // img.flush();
        // InputStream is = null;
        // ByteArrayOutputStream bs = new ByteArrayOutputStream();
        //
        // ImageOutputStream imOut;
        // try {
        // imOut = ImageIO.createImageOutputStream(bs);
        // ImageIO.write(img, "png", imOut);
        // is = new ByteArrayInputStream(bs.toByteArray());
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // ImageInputStream iis = null;
        // try {
        // iis = ImageIO.createImageInputStream(is);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // return iis;
        // }

        public void resize() {
            int w = a2 - a1;
            int h = b2 - b1;

            if (w > 0 && h > 0)
                dragPanel.setBounds(a1, b1, w, h);
            if (w < 0 && h > 0) {
                dragPanel.setBounds(a2, b1, -w, h);
            }
            if (w > 0 && h < 0) {
                dragPanel.setBounds(a1, b2, w, -h);
            }
            if (w < 0 && h < 0) {
                dragPanel.setBounds(a2, b2, -w, -h);
            }
            dragPanel.setVisible(true);
        }

    }

    private void addComponents() {
        addDragPanel();
        addGreyPanel();

    }

    private void addDragPanel() {

        dragPanel = new DragPanel();
        add(dragPanel);
        dragPanel.setBackground(new Color(0, 0, 0, 80));
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        dragPanel.setPreferredSize(scrSize);
        dragPanel.setVisible(false);
    }

    private void setBackground() {
        img = ScreenCaptor.screenShot();
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", byteOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon background = new ImageIcon(byteOut.toByteArray());
        bgLabel = new JLabel(background);
        bgLabel.setBounds(0, 0, background.getIconWidth(),
                background.getIconHeight());

        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
        imgPanel = (JPanel) jframe.getContentPane();
        imgPanel.setOpaque(false);
        // 内容窗格默认的布局管理器为BorderLayout
        imgPanel.setLayout(null);

        getLayeredPane().setLayout(null);
        // 把背景图片添加到分层窗格的最底层作为背景
        getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));
    }

    public void addGreyPanel() {
        JPanel greyPanel = new JPanel();
        this.getContentPane().add(greyPanel);
        greyPanel.setBackground(new Color(0, 0, 0, 60));
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        greyPanel.setPreferredSize(scrSize);
        greyPanel.setVisible(true);
        greyPanel.setBounds(0, 0, (int) scrSize.getWidth(),
                (int) scrSize.getHeight());
    }

    private void init() {
        setVisible(false);
        this.jframe = this;
        setLayout(new FlowLayout());
        this.setBackground();
        this.addListeners();
    }

    private void addListeners() {

        addMouseMotionListener(new MouseInputAdapter() {
            boolean dragged = false;

            @Override
            public void mousePressed(MouseEvent e) {
                a1 = e.getX();
                b1 = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragged)
                    dragged = true;
                a2 = e.getX();
                b2 = e.getY();
                dragPanel.resize();
                jframe.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                a2 = e.getX();
                b2 = e.getY();
                if (dragged)
                    dragPanel.resize();
                jframe.repaint();
            }
        });
        addMouseListener(new MouseInputAdapter() {
            boolean dragged = false;

            @Override
            public void mousePressed(MouseEvent e) {
                dragPanel.setVisible(false);
                a1 = e.getX();
                b1 = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragged)
                    dragged = true;
                a2 = e.getX();
                b2 = e.getY();
                dragPanel.resize();
                jframe.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                a2 = e.getX();
                b2 = e.getY();
                if (dragged)
                    dragPanel.resize();
                jframe.repaint();
            }
        });
    }

    public DragFrame() {
        this.useSystemUI();
        this.init();
        this.addComponents();
        this.fullScreen();
    }

    public static void main(String[] args) {
        new DragFrame();
    }
}
