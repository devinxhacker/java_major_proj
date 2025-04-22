package main.ui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import main.ui.pages.*;

public class AdminHeader extends JPanel {
    
	private static final long serialVersionUID = -1093948901238630570L;
	private static final Color HEADER_COLOR = new Color(70, 130, 180);
    
    public AdminHeader(JFrame currentFrame) {
        
        this.setBackground(HEADER_COLOR);
        this.setPreferredSize(new Dimension(1280, 50));
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        JButton title = new JButton("Warehouse Management System - Admin");
        title.setBorderPainted(false);
        title.setContentAreaFilled(false);
        title.setFocusPainted(false);
        title.setBorder(new EmptyBorder(0, 30, 0, 0));
        title.setFont(new Font("Serif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setCursor(new Cursor(Cursor.HAND_CURSOR));
        title.addActionListener(e -> {
            Home newFrame = new Home();
            newFrame.show();
            currentFrame.dispose();
        });
        this.add(title, BorderLayout.WEST);
        
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(HEADER_COLOR);
        navigationPanel.setBorder(new EmptyBorder(2, 0, 0, 30));
        
        JButton warehouseButton = createNavButton("Warehouse");
        warehouseButton.addActionListener(e -> {
            Warehouse newFrame = new Warehouse();
            newFrame.show();
            currentFrame.dispose();
        });
        
        JButton requestsButton = createNavButton("Requests");
        requestsButton.addActionListener(e -> {
            AdminPortal newFrame = new AdminPortal();
            newFrame.show();
            currentFrame.dispose();
        });
        
        JButton sendReceiveButton = createNavButton("Send/Receive");
        sendReceiveButton.addActionListener(e -> {
        	SendReceive newFrame = new SendReceive();
        	newFrame.show();
        	currentFrame.dispose();
        });
        
        JButton transactionsButton = createNavButton("Transactions");
        transactionsButton.addActionListener(e -> {
            Transactions newFrame = new Transactions();
            newFrame.show();
            currentFrame.dispose();
        });
        
        JButton logoutButton = createNavButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            AdminLogin login = new AdminLogin();
            login.show();
            currentFrame.dispose();
        });
        
        navigationPanel.add(warehouseButton);
        navigationPanel.add(Box.createHorizontalStrut(15));
        navigationPanel.add(requestsButton);
        navigationPanel.add(Box.createHorizontalStrut(15));
        navigationPanel.add(sendReceiveButton);
        navigationPanel.add(Box.createHorizontalStrut(15));
        navigationPanel.add(transactionsButton);
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
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (!text.equals("Logout")) {
                    button.setBackground(new Color(60, 116, 162));
                } else {
                    button.setBackground(new Color(200, 35, 51));
                }
            }
            
            public void mouseExited(MouseEvent evt) {
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