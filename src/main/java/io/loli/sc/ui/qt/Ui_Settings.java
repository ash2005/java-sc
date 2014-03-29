package io.loli.sc.ui.qt;

/********************************************************************************
 ** Form generated from reading ui file 'untitled.jui'
 **
 ** Created by: Qt User Interface Compiler version 4.7.0
 **
 ** WARNING! All changes made in this file will be lost when recompiling ui file!
 ********************************************************************************/
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QWidget;

/**
 * "设置"的界面, QT<br/>
 * User: choco(loli@linux.com) <br/>
 * Date: 2014年3月29日 <br/>
 * Time: 下午4:04:30 <br/>
 * 
 * @author choco
 */
public class Ui_Settings implements com.trolltech.qt.QUiForm<QDialog> {
    public QTabWidget tabWidget;
    public QWidget tab;
    public QLineEdit lineEdit;
    public QPushButton pushButton;
    public QLabel label;
    public QLabel label_2;
    public QLabel label_3;
    public QLineEdit lineEdit_2;
    public QLabel label_4;
    public QCheckBox checkBox;
    public QCheckBox checkBox_2;
    public QWidget tab_2;
    public QLabel label_5;
    public QComboBox comboBox;
    public QLabel label_6;
    public QPushButton pushButton_2;
    public QListWidget listWidget;
    public QPushButton pushButton_3;
    public QPushButton pushButton_4;

    private QDialog qDialog;

    public Ui_Settings() {
        super();
    }

    public void setupUi(QDialog Settings) {
        this.qDialog = Settings;
        qDialog.setWindowFlags(WindowType.createQFlags(
                WindowType.CustomizeWindowHint, WindowType.SubWindow,
                WindowType.WindowCloseButtonHint));
        Settings.setObjectName("Settings");
        // Settings.resize(new QSize(392, 487).expandedTo(Settings
        // .minimumSizeHint()));
        Settings.setFixedSize(new QSize(392, 487));
        tabWidget = new QTabWidget(Settings);
        tabWidget.setObjectName("tabWidget");
        tabWidget.setGeometry(new QRect(20, 20, 361, 411));
        tab = new QWidget();
        tab.setObjectName("tab");
        lineEdit = new QLineEdit(tab);
        lineEdit.setObjectName("lineEdit");
        lineEdit.setGeometry(new QRect(80, 50, 201, 31));
        pushButton = new QPushButton(tab);
        pushButton.setObjectName("pushButton");
        pushButton.setGeometry(new QRect(280, 50, 61, 31));
        label = new QLabel(tab);
        label.setObjectName("label");
        label.setGeometry(new QRect(30, 50, 41, 31));
        label_2 = new QLabel(tab);
        label_2.setObjectName("label_2");
        label_2.setGeometry(new QRect(10, 10, 81, 31));
        label_2.setStyleSheet("font: bold 10pt \"\u6587\u6cc9\u9a7f\u5fae\u7c73\u9ed1\";\n"
                + "");
        label_3 = new QLabel(tab);
        label_3.setObjectName("label_3");
        label_3.setGeometry(new QRect(10, 90, 111, 31));
        label_3.setStyleSheet("font: bold 10pt \"\u6587\u6cc9\u9a7f\u5fae\u7c73\u9ed1\";");
        lineEdit_2 = new QLineEdit(tab);
        lineEdit_2.setObjectName("lineEdit_2");
        lineEdit_2.setGeometry(new QRect(30, 120, 271, 31));
        label_4 = new QLabel(tab);
        label_4.setObjectName("label_4");
        label_4.setGeometry(new QRect(50, 150, 221, 21));
        checkBox = new QCheckBox(tab);
        checkBox.setObjectName("checkBox");
        checkBox.setGeometry(new QRect(30, 200, 131, 31));
        checkBox_2 = new QCheckBox(tab);
        checkBox_2.setObjectName("checkBox_2");
        checkBox_2.setGeometry(new QRect(30, 240, 291, 22));
        tabWidget.addTab(tab, com.trolltech.qt.core.QCoreApplication.translate(
                "Settings", "\u7b80\u5355\u8bbe\u7f6e", null));
        tab_2 = new QWidget();
        tab_2.setObjectName("tab_2");
        label_5 = new QLabel(tab_2);
        label_5.setObjectName("label_5");
        label_5.setGeometry(new QRect(50, 10, 81, 31));
        comboBox = new QComboBox(tab_2);
        comboBox.setObjectName("comboBox");
        comboBox.setGeometry(new QRect(140, 10, 121, 31));
        label_6 = new QLabel(tab_2);
        label_6.setObjectName("label_6");
        label_6.setGeometry(new QRect(10, 60, 161, 21));
        label_6.setStyleSheet("font: bold 10pt \"\u6587\u6cc9\u9a7f\u5fae\u7c73\u9ed1\";");
        pushButton_2 = new QPushButton(tab_2);
        pushButton_2.setObjectName("pushButton_2");
        pushButton_2.setGeometry(new QRect(250, 350, 90, 26));
        listWidget = new QListWidget(tab_2);
        listWidget.setObjectName("listWidget");
        listWidget.setGeometry(new QRect(20, 90, 321, 251));
        tabWidget.addTab(tab_2, com.trolltech.qt.core.QCoreApplication
                .translate("Settings", "\u8fde\u63a5\u7f51\u7ad9", null));
        pushButton_3 = new QPushButton(Settings);
        pushButton_3.setObjectName("pushButton_3");
        pushButton_3.setGeometry(new QRect(230, 450, 71, 26));
        pushButton_4 = new QPushButton(Settings);
        pushButton_4.setObjectName("pushButton_4");
        pushButton_4.setGeometry(new QRect(310, 450, 71, 26));
        retranslateUi(Settings);

        tabWidget.setCurrentIndex(0);

        Settings.connectSlotsByName();
        Settings.show();

    } // setupUi

    void retranslateUi(QDialog Settings) {
        Settings.setWindowTitle(com.trolltech.qt.core.QCoreApplication
                .translate("Settings", "\u8bbe\u7f6e", null));
        pushButton.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings", "\u6d4f\u89c8", null));
        label.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings", "\u5730\u5740\uff1a", null));
        label_2.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings", "\u622a\u56fe\u4fdd\u5b58\u5728", null));
        label_3.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings", "文件名格式", null));
        lineEdit_2.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings",
                "yy-MM-dd mm:ss\u65f6\u7684\u5c4f\u5e55\u622a\u56fe", null));
        label_4.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings",
                "\u4f8b: \u5f20\u4e09\u4e8eyy-MM-dd mm:ss\u7684\u622a\u56fe",
                null));
        checkBox.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings", "\u662f\u5426\u5f00\u673a\u81ea\u542f\u52a8", null));
        checkBox_2
                .setText(com.trolltech.qt.core.QCoreApplication
                        .translate(
                                "Settings",
                                "\u662f\u5426\u5728\u672c\u5730\u4fdd\u7559\u622a\u56fe(\u4e0a\u4f20\u540e\u662f\u5426\u8fd8\u5728\u672c\u5730)",
                                null));
        tabWidget.setTabText(tabWidget.indexOf(tab),
                com.trolltech.qt.core.QCoreApplication.translate("Settings",
                        "\u7b80\u5355\u8bbe\u7f6e", null));
        label_5.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings", "\u9ed8\u8ba4\u4e0a\u4f20\u5230", null));
        label_6.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings",
                "\u53ef\u4ee5\u4e0a\u4f20\u5230\u5982\u4e0b\u7f51\u7ad9", null));
        pushButton_2.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings", "\u8bbe\u7f6e", null));
        tabWidget.setTabText(tabWidget.indexOf(tab_2),
                com.trolltech.qt.core.QCoreApplication.translate("Settings",
                        "\u8fde\u63a5\u7f51\u7ad9", null));
        pushButton_3.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings", "\u786e\u5b9a", null));
        pushButton_4.setText(com.trolltech.qt.core.QCoreApplication.translate(
                "Settings", "\u53d6\u6d88", null));
    } // retranslateUi

}
