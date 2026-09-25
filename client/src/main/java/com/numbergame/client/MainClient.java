package com.numbergame.client;

import com.numbergame.client.ui.auth.LoginFrame;
import com.numbergame.client.ui.lobby.LobbyFrame;
import com.numbergame.client.ui.game.GamePanel;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MainClient {
    private LoginFrame loginFrame;
    private LobbyFrame lobbyFrame;
    private GamePanel gamePanel;

    public void start() {
        loginFrame = new LoginFrame();
        
        // Mô phỏng: Khi bấm nút "Đăng nhập" ở LoginFrame
        // Thay vì chỉ in ra console, ta chuyển cảnh sang Sảnh Chờ (Lobby)
        JButton btnLogin = findButton(loginFrame, "Đăng nhập");
        if (btnLogin != null) {
            btnLogin.addActionListener(e -> {
                loginFrame.dispose(); // Đóng cửa sổ Login
                openLobby();          // Mở Sảnh Chờ
            });
        }

        loginFrame.setVisible(true);
    }

    private void openLobby() {
        lobbyFrame = new LobbyFrame();

        // Mô phỏng: Khi ở Lobby bấm "Vào Phòng" -> Mở Bàn Chơi Game
        JButton btnJoin = findButton(lobbyFrame, "Vào Phòng");
        if (btnJoin != null) {
            btnJoin.addActionListener(e -> {
                lobbyFrame.dispose(); // Đóng cửa sổ Sảnh Chờ
                openGame();           // Mở Màn hình Bàn Chơi
            });
        }

        lobbyFrame.setVisible(true);
    }

    private void openGame() {
        gamePanel = new GamePanel();
        gamePanel.setVisible(true);
    }

    // Hàm tiện ích hỗ trợ tìm nút bấm theo Text trên giao diện để gắn sự kiện test luồng
    private JButton findButton(JFrame frame, String text) {
        for (java.awt.Component comp : getAllComponents(frame.getContentPane())) {
            if (comp instanceof JButton btn && text.equals(btn.getText())) {
                return btn;
            }
        }
        return null;
    }

    private java.util.List<java.awt.Component> getAllComponents(final java.awt.Container c) {
        java.awt.Component[] comps = c.getComponents();
        java.util.List<java.awt.Component> compList = new java.util.ArrayList<>();
        for (java.awt.Component comp : comps) {
            compList.add(comp);
            if (comp instanceof java.awt.Container container) {
                compList.addAll(getAllComponents(container));
            }
        }
        return compList;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainClient().start());
    }
}