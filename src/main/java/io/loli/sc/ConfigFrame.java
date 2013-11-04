package io.loli.sc;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
        setSize(380, 100);
        setVisible(true);
        this.setResizable(false);

    }

    private void initComponents() {
        savePathLabel = new JLabel("保存在");
        savePathField = new JTextField(20);
        browsePathButton = new JButton("浏览");
        jpanel = new JPanel();
        jpanel.setLayout(new FlowLayout());

        okButton = new JButton("确定");
        cancelButton = new JButton("取消");
        chooser = new JFileChooser();
        savePathField.setText(config.getSavePath());
    }

    private JLabel savePathLabel;
    private JTextField savePathField;
    private JButton browsePathButton;

    private JButton okButton;
    private JButton cancelButton;

    private JFileChooser chooser;

    private void addcomponents() {

        jpanel.add(savePathLabel);
        jpanel.add(savePathField);
        jpanel.add(browsePathButton);
        jpanel.add(okButton);
        jpanel.add(cancelButton);
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
    }

    public ConfigFrame(Config config) {
        super("设置");
        setConfig(config);
        this.useSystemUI();
        this.init();
        this.initComponents();
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
