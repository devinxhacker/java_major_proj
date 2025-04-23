package main.ui.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import main.ui.pages.*;

public class EmployeeHeader extends JPanel {

	private static final long serialVersionUID = -9076376554250493863L;
	private static final Color HEADER_COLOR = new Color(30, 64, 175); // Modern dark blue
    private static final Color HOVER_COLOR = new Color(37, 99, 235); // Lighter blue for hover
    private static final Color LOGOUT_COLOR = new Color(220, 38, 38); // Modern red for logout
    private static final Color LOGOUT_HOVER_COLOR = new Color(248, 113, 113); // Lighter red for logout hover
    
    public EmployeeHeader(JFrame currentFrame, String employeeName, int employeeId) {
        
        this.setBackground(HEADER_COLOR);
        this.setPreferredSize(new Dimension(1280, 70)); // Match AdminHeader height
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove border
        
        // Create a wrapper panel for the title with proper padding and vertical centering
        JPanel titleWrapper = new JPanel(new GridBagLayout());
        titleWrapper.setBackground(HEADER_COLOR);
        titleWrapper.setPreferredSize(new Dimension(400, 70)); // Match header height
        
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
        
        // Add hover effect with smooth transition
        title.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                title.setForeground(new Color(191, 219, 254)); // Light blue on hover
            }
            public void mouseExited(MouseEvent e) {
                title.setForeground(Color.WHITE);
            }
        });
        
        title.addActionListener(e -> {
            EmployeeRequests newFrame = new EmployeeRequests(employeeName, employeeId);
            newFrame.show();
            currentFrame.dispose();
        });
        
        titleWrapper.add(title, titleGbc);
        this.add(titleWrapper, BorderLayout.WEST);
        
        // Create a navigation panel with improved layout and vertical centering
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(HEADER_COLOR);
        navigationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        navigationPanel.setBorder(new EmptyBorder(0, 0, 0, 30));
        navigationPanel.setPreferredSize(new Dimension(500, 70)); // Increased width to accommodate welcome text
        
        // Create a panel for the welcome label and logout button
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(HEADER_COLOR);
        rightPanel.setPreferredSize(new Dimension(500, 70)); // Increased width to accommodate welcome text
        
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.weightx = 0.0;
        rightGbc.weighty = 1.0;
        rightGbc.anchor = GridBagConstraints.CENTER;
        rightGbc.insets = new Insets(0, 0, 0, 25);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + employeeName);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        
        // Create a separator between welcome label and logout
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setForeground(new Color(100, 116, 139)); // Slate gray
        separator.setPreferredSize(new Dimension(1, 30));
        separator.setMaximumSize(new Dimension(1, 30));
        
        // Create logout button with enhanced styling
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(LOGOUT_COLOR);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setVerticalAlignment(SwingConstants.CENTER);
        
        // Add hover effect for logout button
        logoutButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(LOGOUT_HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(LOGOUT_COLOR);
            }
        });
        
        logoutButton.addActionListener(e -> {
            EmployeeLogin login = new EmployeeLogin();
            login.show();
            currentFrame.dispose();
        });
        
        // Add components to right panel
        rightPanel.add(welcomeLabel, rightGbc);
        
        rightGbc.gridx = 1;
        rightGbc.insets = new Insets(0, 25, 0, 25);
        rightPanel.add(separator, rightGbc);
        
        rightGbc.gridx = 2;
        rightGbc.insets = new Insets(0, 0, 0, 0);
        rightPanel.add(logoutButton, rightGbc);
        
        navigationPanel.add(rightPanel);
        this.add(navigationPanel, BorderLayout.EAST);
    }
} 