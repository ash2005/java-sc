package io.loli.sc;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
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
            g.setColor(Color.red);
            // 设置线条粗细
            float thick = 2f;
            ((Graphics2D) g).setStroke(new BasicStroke(thick,
                    BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

            int w = a2 - a1;
            int h = b2 - b1;
            if (w > 0 && h > 0)
                g.drawRect(a1, b1, w, h);
            if (w < 0 && h > 0)
                g.drawRect(a2, b1, -w, h);
            if (w > 0 && h < 0)
                g.drawRect(a1, b2, w, -h);
            if (w < 0 && h < 0)
                g.drawRect(a2, b2, -w, -h);
        }
    }

    private void addComponents() {

        dragPanel = new DragPanel();
        this.getContentPane().add(dragPanel);
        dragPanel.setBackground(new Color(0, 0, 0, 40));
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        dragPanel.setPreferredSize(scrSize);
        dragPanel.setBounds(0, 0, (int) scrSize.getWidth(),
                (int) scrSize.getHeight());
        dragPanel.addMouseMotionListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                a1 = e.getX();
                b1 = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                a2 = e.getX();
                b2 = e.getY();
                jframe.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                a2 = e.getX();
                b2 = e.getY();
                jframe.repaint();
            }
        });
        dragPanel.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                a1 = e.getX();
                b1 = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                a2 = e.getX();
                b2 = e.getY();
                jframe.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                a2 = e.getX();
                b2 = e.getY();
                jframe.repaint();
            }
        });
    }

    private void setBackground() {
        BufferedImage img = ScreenCaptor.screenShot();
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

    private void init() {
        setVisible(false);
        this.jframe = this;
        setLayout(new FlowLayout());
        this.setBackground();

        addMouseMotionListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPanel.processParentEvent(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                dragPanel.processParentEvent(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragPanel.processParentEvent(e);
            }
        });
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPanel.processParentEvent(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                dragPanel.processParentEvent(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragPanel.processParentEvent(e);
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
