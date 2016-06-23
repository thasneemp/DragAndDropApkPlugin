package com.thasneem.draganddrop.actions.views;

import com.google.common.io.Files;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;

public class DaggerClass extends JDialog implements FileDrop.Listener {
    private Project data;
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel clickToUploas;
    private JProgressBar progressBar1;

    public DaggerClass(Project data) {
        setTitle("APK Installer");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setLocationRelativeTo(null);
        this.data = data;
        FileDrop fileDrop = new FileDrop(contentPane, this);
        progressBar1.setVisible(false);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });


// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        clickToUploas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FileFilter filter = new FileNameExtensionFilter("Android package", "apk");
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(filter);
                int revalue = chooser.showDialog(DaggerClass.this, "Select APK file");
                if (revalue == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    installApk(file);
                }
                super.mouseClicked(e);
            }
        });
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    @Override
    public void filesDropped(File[] files) {
        installApk(files[0]);
    }

    private void installApk(File file) {
        if (Files.getFileExtension(file.getName()).equalsIgnoreCase("apk")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        progressBar1.setVisible(true);
                        String[] commands = new String[3];
                        commands[0] = "adb";
                        commands[1] = "install";
                        commands[2] = file.getAbsolutePath();
                        Process p1 = Runtime.getRuntime().exec(commands, null);
                        int i = p1.waitFor();
                        if(i==0) {
                            showBalloon("Apk file successfully installed on your device!", MessageType.INFO);
                        } else {
                            showBalloon("Installation failed, Please check your device!",MessageType.ERROR);
                        }
                    } catch (Exception e1) {
                        progressBar1.setVisible(false);
                        System.err.println(e1);
                        showBalloon("Installation failed, Please check your device!",MessageType.ERROR);

                    }
                }
            }).start();

        } else {
            showBalloon("Please check your apk file",MessageType.ERROR);
        }


    }

    public void showBalloon(String msg, MessageType type) {
        progressBar1.setVisible(false);
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(data);
        JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(msg, type, new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {

            }
        }).setFadeoutTime(1000 * 5).setDialogMode(true).createBalloon().show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
    }

}
