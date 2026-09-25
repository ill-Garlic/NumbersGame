package com.numbergame.client.ui.game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JFrame {
    private JLabel lblTargetNumber;
    private JLabel lblTimer;
    private GameBoardPanel boardPanel;
    private JTextArea txtPlayerScores;

    public GamePanel() {
        setTitle("Numbers Game - Bàn Chơi");
        setSize(950, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));

        // 1. Top Panel: Hiển thị "Số Cần Tìm Hiện Tại" & "Đồng Hồ Đếm Ngược"
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblTargetNumber = new JLabel("SỐ CẦN TÌM: 42", SwingConstants.LEFT);
        lblTargetNumber.setFont(new Font("Arial", Font.BOLD, 26));
        lblTargetNumber.setForeground(new Color(217, 83, 79));

        lblTimer = new JLabel("Thời gian: 02:00", SwingConstants.RIGHT);
        lblTimer.setFont(new Font("Arial", Font.BOLD, 22));

        topPanel.add(lblTargetNumber);
        topPanel.add(lblTimer);
        add(topPanel, BorderLayout.NORTH);

        // 2. Center Panel: Lưới 100 ô số ngẫu nhiên
        boardPanel = new GameBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // 3. East Panel: Bảng điểm người chơi trong phòng (2-3 người) & Vật phẩm
        JPanel eastPanel = new JPanel(new BorderLayout(5, 5));
        eastPanel.setPreferredSize(new Dimension(220, 0));
        eastPanel.setBorder(BorderFactory.createTitledBorder("Điểm & Trạng Thái"));

        txtPlayerScores = new JTextArea();
        txtPlayerScores.setEditable(false);
        txtPlayerScores.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtPlayerScores.setText(
            "--- ĐIỂM SỐ ---\n\n" +
            "Player 1: 5 điểm\n" +
            "Player 2: 3 điểm\n" +
            "Player 3: 0 điểm\n"
        );
        eastPanel.add(new JScrollPane(txtPlayerScores), BorderLayout.CENTER);

        // Khung nút dùng vật phẩm (ItemBar)
        JPanel itemPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        itemPanel.setBorder(BorderFactory.createTitledBorder("Vật Phẩm"));
        JButton btnLucky = new JButton("Số May Mắn (x2)");
        JButton btnBlock = new JButton("Ưu Tiên (Che số)");
        itemPanel.add(btnLucky);
        itemPanel.add(btnBlock);
        eastPanel.add(itemPanel, BorderLayout.SOUTH);

        add(eastPanel, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GamePanel().setVisible(true));
    }
}