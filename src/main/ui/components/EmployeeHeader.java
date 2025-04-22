package main.ui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import main.ui.pages.*;

public class EmployeeHeader extends JPanel {

	private static final long serialVersionUID = -9076376554250493863L;
	private static final Color HEADER_COLOR = new Color(70, 130, 180);
    
    public EmployeeHeader(JFrame currentFrame, String employeeName) {
        
        this.setBackground(HEADER_COLOR);
        this.setPreferredSize(new Dimension(1280, 50));
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        JButton title = new JButton("Warehouse Management System - Employee");
        title.setBorderPainted(false);
        title.setContentAreaFilled(false);
        title.setFocusPainted(false);
        title.setBorder(new EmptyBorder(0, 30, 0, 0));
        title.setFont(new Font("Serif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        title.addActionListener(e -> {
            EmployeeRequests newFrame = new EmployeeRequests(employeeName);
            newFrame.show();
            currentFrame.dispose();
        });
        this.add(title, BorderLayout.WEST);
        
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(HEADER_COLOR);
        navigationPanel.setBorder(new EmptyBorder(2, 0, 0, 30));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + employeeName);
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(new EmptyBorder(0, 0, 0, 15));

        JButton logoutButton = createNavButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            EmployeeLogin login = new EmployeeLogin();
            login.show();
            currentFrame.dispose();
        });
        
        navigationPanel.add(welcomeLabel);
        navigationPanel.add(Box.createHorizontalStrut(25));
        navigationPanel.add(logoutButton);
        
        this.add(navigationPanel, BorderLayout.EAST);
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(HEADER_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!text.equals("Logout")) {
                    button.setBackground(new Color(60, 116, 162));
                } else {
                    button.setBackground(new Color(200, 35, 51));
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!text.equals("Logout")) {
                    button.setBackground(HEADER_COLOR);
                } else {
                    button.setBackground(new Color(220, 53, 69));
                }
            }
        });
        
        return button;
    }
} 