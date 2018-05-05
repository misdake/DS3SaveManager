package com.rs.tool.ds3.savemanager;

import com.melloware.jintellitype.JIntellitype;
import sun.audio.AudioPlayer;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static File saveFile = null;

    private static final File backupFile = new File("backups\\backup.sl2");

    public static void main(String[] args) {
        File saveFolder = new File(System.getenv("AppData") + "\\DarkSoulsIII\\");
        new File("backups").mkdirs();

        List<File> candidates = new ArrayList<>();

        if (saveFolder.exists()) {
            File[] files = saveFolder.listFiles(file -> file.isDirectory() && file.getName().matches("[0-9]+"));
            if (files != null) for (File file : files) {
                File saveFile = new File(file.getAbsoluteFile() + "\\DS30000.sl2");
                if (saveFile.exists()) {
                    candidates.add(saveFile);
                }
            }
        }

        if (candidates.isEmpty()) {
            JOptionPane.showMessageDialog(null, "未找到存档，将退出");
            System.exit(-1);
        } else if (candidates.size() > 1) {
            JDialog dialog = new JDialog();
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    if (saveFile == null) {
                        System.exit(0);
                    }
                }
            });
            dialog.setTitle("选择存档");
            JList<File> list = new JList<>();
            list.setListData(candidates.toArray(new File[0]));
            list.addListSelectionListener(event -> {
                int index = event.getFirstIndex();
                saveFile = candidates.get(index);
                dialog.dispose();
                start();
            });
            dialog.add(list);
            dialog.setVisible(true);
            dialog.pack();
        } else {
            saveFile = candidates.get(0);
            start();
        }

    }

    private static void start() {
        if (saveFile == null) {
            System.out.println("未确定存档，退出");
            return;
        }

        System.out.println("使用存档: " + saveFile.getAbsoluteFile());

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
