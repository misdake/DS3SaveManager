package com.rs.tool.ds3.savemanager;

import com.melloware.jintellitype.JIntellitype;
import sun.audio.AudioPlayer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    private static final File saveFile   = new File("C:\\Users\\zzx\\AppData\\Roaming\\DarkSoulsIII\\0140000100000001\\DS30000.sl2");
    private static final File backupFile = new File("backup.sl2");

    public static void main(String[] args) {
        JIntellitype.getInstance().registerHotKey(1, "F1");
        JIntellitype.getInstance().registerHotKey(2, "F2");

        JIntellitype.getInstance().addHotKeyListener(bindingIndex -> {
            switch (bindingIndex) {
                case 1:
                    backup();
                    break;
                case 2:
                    restore();
                    break;
            }
        });

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.setAlwaysOnTop(true);
        frame.setLayout(new FlowLayout());
        JButton backupButton = new JButton("备份");
        JButton restoreButton = new JButton("还原");
        frame.add(backupButton);
        frame.add(restoreButton);
        frame.pack();

        backupButton.addActionListener(actionEvent -> backup());
        backupButton.addActionListener(actionEvent -> restore());
    }

    private static void backup() {
        boolean success = FileUtil.copy(saveFile, backupFile);
        if (success) {
            try {
                FileInputStream sound = new FileInputStream("backup.wav");
                AudioPlayer.player.start(sound);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void restore() {
        boolean success = FileUtil.copy(backupFile, saveFile);
        if (success) {
            try {
                FileInputStream sound = new FileInputStream("restore.wav");
                AudioPlayer.player.start(sound);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
}
