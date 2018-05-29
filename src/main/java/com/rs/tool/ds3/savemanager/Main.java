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

    public static void main(String[] args) {
        File backupFile = new File("backups\\backup.sl2");

        File saveFolder = new File(System.getenv("AppData") + "\\DarkSoulsIII\\");
        new File("backups").mkdirs();

        List<File> candidates = new ArrayList<>();

        if (saveFolder.exists()) {
            File[] files = saveFolder.listFiles(file -> file.isDirectory() && file.getName().matches("[0-9a-zA-Z]+"));
            if (files != null) for (File file : files) {
                File s = new File(file.getAbsoluteFile() + "\\DS30000.sl2");
                if (s.exists()) {
                    candidates.add(s);
                }
            }
        }

        if (candidates.isEmpty()) {
            JOptionPane.showMessageDialog(null, "未找到存档，将退出");
            System.exit(-1);
        } else if (candidates.size() > 1) {
            boolean[] selected = {false};
            JDialog dialog = new JDialog();
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    if (!selected[0]) {
                        System.exit(0);
                    }
                }
            });
            dialog.setTitle("选择存档");
            JList<File> list = new JList<>();
            list.setListData(candidates.toArray(new File[0]));
            list.addListSelectionListener(event -> {
                int index = event.getFirstIndex();
                File saveFile = candidates.get(index);
                selected[0] = true;
                dialog.dispose();
                start(saveFile, backupFile);
            });
            dialog.add(list);
            dialog.setVisible(true);
            dialog.pack();
        } else {
            File saveFile = candidates.get(0);
            start(saveFile, backupFile);
        }

    }

    private static void start(File saveFile, File backupFile) {
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
                    backup(saveFile, backupFile);
                    break;
                case 2:
                    restore(backupFile, saveFile);
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

        backupButton.addActionListener(actionEvent -> backup(saveFile, backupFile));
        restoreButton.addActionListener(actionEvent -> restore(backupFile, saveFile));
    }

    private static void backup(File saveFile, File backupFile) {
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
    private static void restore(File backupFile, File saveFile) {
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
