package io.loli.sc;

import io.loli.sc.api.DropboxAPI;
import io.loli.sc.api.ImgurAPI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
        setSize(380, 300);
        setVisible(true);
        this.setResizable(false);

    }

    private void addChoice(JComboBox<String> choice) {
        if (config.getImgurConfig().getAccessToken() != null)
            choice.addItem("imgur");
        if (config.getDropboxConfig().getAccessToken() != null)
            choice.addItem("dropbox");
    }

    private void initComponents() {
        savePathLabel = new JLabel("保存在");
        savePathField = new JTextField(20);
        browsePathButton = new JButton("浏览");
        jpanel = new JPanel();
        jpanel.setLayout(null);

        uploadChoice = new JComboBox<String>();
        addChoice(uploadChoice);
        uploadChoice.setSelectedItem(config.getDefaultUpload());

        okButton = new JButton("确定");
        cancelButton = new JButton("取消");
        chooser = new JFileChooser();
        savePathField.setText(config.getSavePath());

        imgurLabel = new JLabel("imgur");
        imgurAuthLabel = new JLabel();
        imgurAuthButton = new JButton("连接");
        imgurRemoveAuthButton = new JButton("移除");

        dropboxLabel = new JLabel("dropbox");
        dropboxAuthLabel = new JLabel();
        dropboxAuthButton = new JButton("连接");
        dropboxRemoveAuthButton = new JButton("移除");

        uploadToLabel = new JLabel("上传到");
    }

    private void initButton() {
        if (config.getImgurConfig().getAccessToken() == null) {
            imgurAuthLabel.setText("未连接");
            imgurRemoveAuthButton.setEnabled(false);
        } else {
            imgurAuthLabel.setText("已连接");
            imgurAuthButton.setEnabled(false);
        }
        if (config.getDropboxConfig().getAccessToken() == null) {
            dropboxAuthLabel.setText("未连接");
            dropboxRemoveAuthButton.setEnabled(false);
        } else {
            dropboxAuthLabel.setText("已连接");
            dropboxAuthButton.setEnabled(false);
        }

    }

    private void initComposition() {
        savePathLabel.setBounds(30, 40, 70, 30);
        savePathField.setBounds(90, 40, 180, 30);
        browsePathButton.setBounds(280, 40, 60, 30);
        okButton.setBounds(100, 205, 60, 30);
        cancelButton.setBounds(200, 205, 60, 30);
        uploadChoice.setBounds(90, 5, 140, 30);
        uploadToLabel.setBounds(30, 5, 60, 30);

        imgurLabel.setBounds(40, 75, 60, 30);
        imgurAuthLabel.setBounds(85, 75, 60, 30);
        imgurAuthButton.setBounds(185, 75, 60, 30);
        imgurRemoveAuthButton.setBounds(250, 75, 60, 30);

        dropboxLabel.setBounds(40, 110, 60, 30);
        dropboxAuthLabel.setBounds(110, 110, 60, 30);
        dropboxAuthButton.setBounds(185, 110, 60, 30);
        dropboxRemoveAuthButton.setBounds(250, 110, 60, 30);
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

    private JLabel dropboxLabel;
    private JLabel dropboxAuthLabel;
    private JButton dropboxAuthButton;
    private JButton dropboxRemoveAuthButton;

    private JComboBox<String> uploadChoice;

    private JLabel uploadToLabel;

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

        jpanel.add(dropboxLabel);
        jpanel.add(dropboxAuthLabel);
        jpanel.add(dropboxAuthButton);
        jpanel.add(dropboxRemoveAuthButton);
        jpanel.add(uploadChoice);
        jpanel.add(uploadToLabel);
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
                config.setSavePath(savePathField.getText());
                Object obj = uploadChoice.getSelectedItem();
                if (obj != null)
                    config.setDefaultUpload((String) obj);
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
                uploadChoice.addItem("imgur");
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
                uploadChoice.removeItem("imgur");
                removeAllItemsIfHasOnlyOne(uploadChoice);
            }

        });

        dropboxAuthButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DropboxAPI api = new DropboxAPI();
                api.auth();
                String pin = JOptionPane.showInputDialog("请输入认证码");
                DropboxAPI.AccessToken token = api.pinToToken(pin);
                config.getDropboxConfig().setAccessToken(
                        token.getAccess_token());

                config.getDropboxConfig().setUid(token.getUid());
                ;
                config.getDropboxConfig().updateProperties(
                        config.getProperties());
                config.save();
                dropboxAuthButton.setEnabled(false);
                dropboxRemoveAuthButton.setEnabled(true);
                dropboxAuthLabel.setText("已连接");
                uploadChoice.addItem("dropbox");
            }

        });

        dropboxRemoveAuthButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                config.getDropboxConfig().removeFromProperties(
                        config.getProperties());
                config.save();
                dropboxAuthButton.setEnabled(true);
                dropboxRemoveAuthButton.setEnabled(false);
                dropboxAuthLabel.setText("未连接");
                uploadChoice.removeItem("dropbox");
                removeAllItemsIfHasOnlyOne(uploadChoice);
            }

        });
    }

    private void removeAllItemsIfHasOnlyOne(JComboBox<String> choice) {
        if (choice.getItemCount() == 1) {
            choice.removeAllItems();
        }
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
