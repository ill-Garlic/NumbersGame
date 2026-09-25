package com.numbergame.client.ui.game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoardPanel extends JPanel {
    private final JButton[] numberButtons = new JButton[100];

    public GameBoardPanel() {
        // Lưới 10 hàng x 10 cột cho 100 số
        setLayout(new GridLayout(10, 10, 4, 4));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initBoard();
    }

    private void initBoard() {
        // Trộn ngẫu nhiên các số từ 1 đến 100 theo yêu cầu đề bài
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 100; i++) numbers.add(i);
        Collections.shuffle(numbers);

        for (int i = 0; i < 100; i++) {
            int num = numbers.get(i);
            JButton btn = new JButton(String.valueOf(num));
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);

            // Khi click, sau này sẽ gửi ClickRequest lên Server
            btn.addActionListener(e -> {
                System.out.println("Đã bấm số: " + num);
            });

            numberButtons[i] = btn;
            add(btn);
        }
    }

    // Hàm đổi màu ô khi Server xác nhận có người bấm trúng
    public void markFound(int number, Color playerColor) {
        for (JButton btn : numberButtons) {
            if (btn.getText().equals(String.valueOf(number))) {
                btn.setBackground(playerColor);
                btn.setEnabled(false);
                break;
            }
        }
    }
}