package com.numbergame.client.ui.lobby;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LobbyFrame extends JFrame {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton btnCreateRoom;
    private JButton btnJoinRoom;
    private JButton btnLeaderboard;
    private JLabel lblUserStatus;

    public LobbyFrame() {
        setTitle("Numbers Game - Sảnh Chờ");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Header Panel: Thông tin người dùng
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        lblUserStatus = new JLabel("Xin chào: Player1 | Thắng/Thua: 10/2");
        lblUserStatus.setFont(new Font("Arial", Font.BOLD, 14));
        btnLeaderboard = new JButton("Bảng Xếp Hạng");
        topPanel.add(lblUserStatus, BorderLayout.WEST);
        topPanel.add(btnLeaderboard, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Danh sách các phòng đấu (2-3 người/vòng)
        String[] columns = {"ID Phòng", "Tên Phòng", "Số Người", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        roomTable = new JTable(tableModel);
        
        // Mock dữ liệu mẫu để xem giao diện
        tableModel.addRow(new Object[]{"101", "Phòng Cao Thủ", "2/3", "Đang chờ"});
        tableModel.addRow(new Object[]{"102", "Giao Lưu Vui Vẻ", "3/3", "Đang chơi"});

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Phòng Chơi"));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Nút hành động
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnCreateRoom = new JButton("Tạo Phòng Mới");
        btnJoinRoom = new JButton("Vào Phòng");
        bottomPanel.add(btnCreateRoom);
        bottomPanel.add(btnJoinRoom);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LobbyFrame().setVisible(true));
    }
}