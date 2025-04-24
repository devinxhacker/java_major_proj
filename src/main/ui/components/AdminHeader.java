package main.ui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import main.ui.pages.*;

public class AdminHeader extends JPanel {
    
	private static final long serialVersionUID = -1093948901238630570L;
	private static final Color HEADER_COLOR = new Color(30, 64, 175); // Modern dark blue
	private static final Color ACTIVE_COLOR = new Color(59, 130, 246); // Active page indicator
    private static final Color LOGOUT_COLOR = new Color(220, 38, 38); // Modern red for logout
    private static final Color LOGOUT_HOVER_COLOR = new Color(248, 113, 113); // Lighter red for logout hover
    
    private String currentPage = "";
    
    public AdminHeader(JFrame currentFrame) {
        
        this.setBackground(HEADER_COLOR);
        this.setPreferredSize(new Dimension(1280, 70));
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
        
        
        JPanel titleWrapper = new JPanel(new GridBagLayout());
        titleWrapper.setBackground(HEADER_COLOR);
        titleWrapper.setPreferredSize(new Dimension(400, 70)); 
        
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.weightx = 0.0;
        titleGbc.weighty = 1.0;
        titleGbc.anchor = GridBagConstraints.WEST;
        titleGbc.insets = new Insets(0, 30, 0, 0);
        
        JButton title = new JButton("Warehouse Management System");
        title.setBorderPainted(false);
        title.setContentAreaFilled(false);
        title.setFocusPainted(false);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setCursor(new Cursor(Cursor.HAND_CURSOR));
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setVerticalTextPosition(SwingConstants.CENTER);
        
        
        title.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                title.setForeground(new Color(191, 219, 254)); // Light blue on hover
            }
            public void mouseExited(MouseEvent e) {
                title.setForeground(Color.WHITE);
            }
        });
        
        title.addActionListener(e -> {
            Home newFrame = new Home();
            newFrame.show();
            currentFrame.dispose();
        });
        
        titleWrapper.add(title, titleGbc);
        this.add(titleWrapper, BorderLayout.WEST);
        
        
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(HEADER_COLOR);
        navigationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        navigationPanel.setBorder(new EmptyBorder(0, 0, 0, 30));
        navigationPanel.setPreferredSize(new Dimension(800, 70)); 
        
        
        JPanel mainNavPanel = new JPanel(new GridBagLayout());
        mainNavPanel.setBackground(HEADER_COLOR);
        mainNavPanel.setPreferredSize(new Dimension(600, 70)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 10); 
        
        
        String frameTitle = currentFrame.getTitle().toLowerCase();
        if (frameTitle.contains("warehouse")) {
            currentPage = "warehouse";
        } else if (frameTitle.contains("requests") || frameTitle.contains("admin portal")) {
            currentPage = "requests";
        } else if (frameTitle.contains("send") || frameTitle.contains("receive")) {
            currentPage = "sendreceive";
        } else if (frameTitle.contains("transactions")) {
            currentPage = "transactions";
        }
        
        JButton warehouseButton = createNavButton("Warehouse", "warehouse");
        warehouseButton.addActionListener(e -> {
            Warehouse newFrame = new Warehouse();
            newFrame.show();
            currentFrame.dispose();
        });
        
        JButton requestsButton = createNavButton("Requests", "requests");
        requestsButton.addActionListener(e -> {
            AdminPortal newFrame = new AdminPortal();
            newFrame.show();
            currentFrame.dispose();
        });
        
        JButton sendReceiveButton = createNavButton("Send/Receive", "sendreceive");
        sendReceiveButton.addActionListener(e -> {
            AdminSendReceive newFrame = new AdminSendReceive();
            newFrame.show();
            currentFrame.dispose();
        });
        
        JButton transactionsButton = createNavButton("Transactions", "transactions");
        transactionsButton.addActionListener(e -> {
            Transactions newFrame = new Transactions();
            newFrame.show();
            currentFrame.dispose();
        });
        
        
        mainNavPanel.add(warehouseButton, gbc);
        
        gbc.gridx = 1;
        mainNavPanel.add(requestsButton, gbc);
        
        gbc.gridx = 2;
        mainNavPanel.add(sendReceiveButton, gbc);
        
        gbc.gridx = 3;
        mainNavPanel.add(transactionsButton, gbc);
        
        
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setForeground(new Color(100, 116, 139)); // Slate gray
        separator.setPreferredSize(new Dimension(1, 30));
        separator.setMaximumSize(new Dimension(1, 30));
        
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(LOGOUT_COLOR);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setVerticalAlignment(SwingConstants.CENTER);
        
        
        logoutButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(LOGOUT_HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(LOGOUT_COLOR);
            }
        });
        
        logoutButton.addActionListener(e -> {
            AdminLogin login = new AdminLogin();
            login.show();
            currentFrame.dispose();
        });
        
        
        navigationPanel.add(mainNavPanel);
        navigationPanel.add(Box.createHorizontalStrut(10)); 
        navigationPanel.add(separator);
        navigationPanel.add(Box.createHorizontalStrut(10)); 
        navigationPanel.add(logoutButton);
        
        this.add(navigationPanel, BorderLayout.EAST);
    }
    
    private JButton createNavButton(String text, String pageId) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(HEADER_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        int width = text.equals("Send/Receive") ? 140 : 120;
        button.setPreferredSize(new Dimension(width, 35));
        
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        
        
        if (pageId.equals(currentPage)) {
            button.setForeground(new Color(191, 219, 254)); // Light blue for active page
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, ACTIVE_COLOR),
                BorderFactory.createEmptyBorder(0, 0, 5, 0)
            ));
        }
        
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!pageId.equals(currentPage)) {
                    button.setForeground(new Color(191, 219, 254)); // Light blue on hover
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!pageId.equals(currentPage)) {
                    button.setForeground(Color.WHITE);
                }
            }
        });
        
        return button;
    }
} 