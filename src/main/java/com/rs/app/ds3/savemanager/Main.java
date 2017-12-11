package com.rs.app.ds3.savemanager;

import com.melloware.jintellitype.JIntellitype;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {
    public static void main(String[] args) {
        System.out.println("!");

        JIntellitype.getInstance().registerHotKey(1, "WIN+numpad1");

        JIntellitype.getInstance().addHotKeyListener(bindingIndex -> {
            switch (bindingIndex) {
                case 1:
                    System.out.println("WIN+numpad1 hotkey pressed");
                    break;
            }
        });

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
