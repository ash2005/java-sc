package io.loli.sc;

import io.loli.sc.api.ImgurAPI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ConfigFrame extends JFrame {
    private JFrame jframe;
    private JPanel jpanel;
    private static final long serialVersionUID = 1L;

    private Config config;

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

    private void init() {
        // 确保内部类可以调用到此jframe
        this.jframe = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 230);
        setVisible(true);
        this.setResizable(false);

    }

    private void initComponents() {
        savePathLabel = new JLabel("保存在");
        savePathField = new JTextField(20);
        browsePathButton = new JButton("浏览");
        jpanel = new JPanel();
        jpanel.setLayout(null);

        okButton = new JButton("确定");
        cancelButton = new JButton("取消");
        chooser = new JFileChooser();
        savePathField.setText(config.getSavePath());

        imgurLabel = new JLabel("imgur");
        imgurAuthLabel = new JLabel();
        imgurAuthButton = new JButton("连接");
        imgurRemoveAuthButton = new JButton("移除");
    }

    private void initButton() {
        if (config.getImgurConfig().getAccessToken() == null) {
            imgurAuthLabel.setText("未连接");
            imgurRemoveAuthButton.setEnabled(false);
        } else {
            imgurAuthLabel.setText("已连接");
            imgurAuthButton.setEnabled(false);
        }
    }

    private void initComposition() {
        savePathLabel.setBounds(30, 5, 70, 30);
        savePathField.setBounds(90, 5, 180, 30);
        browsePathButton.setBounds(280, 5, 60, 30);
        okButton.setBounds(100, 170, 60, 30);
        cancelButton.setBounds(200, 170, 60, 30);

        imgurLabel.setBounds(40, 40, 60, 30);
        imgurAuthLabel.setBounds(85, 40, 60, 30);
        imgurAuthButton.setBounds(145, 40, 60, 30);
        imgurRemoveAuthButton.setBounds(210, 40, 60, 30);

    }

    private JLabel savePathLabel;
    private JTextField savePathField;
    private JButton browsePathButton;

    private JButton okButton;
    private JButton cancelButton;

    private JFileChooser chooser;
    private JLabel imgurLabel;
    private JLabel imgurAuthLabel;
    private JButton imgurAuthButton;
    private JButton imgurRemoveAuthButton;

    private void addcomponents() {

        jpanel.add(savePathLabel);
        jpanel.add(savePathField);
        jpanel.add(browsePathButton);
        jpanel.add(okButton);
        jpanel.add(cancelButton);

        jpanel.add(imgurLabel);
        jpanel.add(imgurAuthLabel);
        jpanel.add(imgurAuthButton);
        jpanel.add(imgurRemoveAuthButton);

        add(jpanel);
        // add(chooser);
    }

    public void addListeners() {
        browsePathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result;
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                File file;
                result = chooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                    if (file.exists()) {
                        savePathField.setText(file.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(null, "文件不存在");
                    }
                } else if (result == JFileChooser.CANCEL_OPTION) {
                } else if (result == JFileChooser.ERROR_OPTION) {
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jframe.dispose();
            }
        });

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String savePath = savePathField.getText();
                config.setSavePath(savePath);
                config.save();
                jframe.dispose();
            }

        });

        imgurAuthButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ImgurAPI api = new ImgurAPI();
                api.auth();
                String pin = JOptionPane.showInputDialog("请输入认证后的PIN码");
                ImgurAPI.AccessToken token = api.pinToToken(pin);
                config.getImgurConfig().setAccessToken(token.getAccess_token());
                config.getImgurConfig().setRefreshToken(
                        token.getRefresh_token());
                config.getImgurConfig().setDate(new Date());
                config.getImgurConfig()
                        .updateProperties(config.getProperties());
                config.save();
                imgurAuthButton.setEnabled(false);
                imgurRemoveAuthButton.setEnabled(true);
                imgurAuthLabel.setText("已连接");
            }

        });

        imgurRemoveAuthButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                config.getImgurConfig().removeFromProperties(
                        config.getProperties());
                config.save();
                imgurAuthButton.setEnabled(true);
                imgurRemoveAuthButton.setEnabled(false);
                imgurAuthLabel.setText("未连接");
            }

        });
    }

    public ConfigFrame(Config config) {
        super("设置");
        setConfig(config);
        this.useSystemUI();
        this.init();
        this.initComponents();
        this.initComposition();
        this.initButton();
        this.addcomponents();
        this.addListeners();
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConfigFrame(new Config());
            }
        });
    }
}
