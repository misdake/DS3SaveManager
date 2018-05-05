package com.rs.tool.ds3.savemanager;

import com.melloware.jintellitype.JIntellitype;
import sun.audio.AudioPlayer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static       File saveFile;
    private static final File backupFile = new File("backup.sl2");

    public static void main(String[] args) {
        File saveFolder = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\DarkSoulsIII\\");
        System.out.println(saveFolder);

        List<File> candidates = new ArrayList<>();

        if (saveFolder.exists()) {
            File[] files = saveFolder.listFiles(file -> file.isDirectory() && file.getName().matches("[0-9]+"));
            if (files != null) for (File file : files) {
                File saveFile = new File(file.getAbsoluteFile() + "\\DS30000.sl2");
                if (saveFile.exists()) {
                    candidates.add(file);
                }
            }
        }

        if (candidates.isEmpty()) {
            //TODO tell user
        } else if (candidates.size() > 1) {
            //TODO let user select one
        } else {
            saveFile = candidates.get(0);
        }

        if (saveFile == null) {
            return;
        }

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
