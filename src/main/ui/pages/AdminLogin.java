package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class AdminLogin implements ActionListener {
    
    private JFrame frame = new JFrame();
    private JPanel contentPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton employeeLoginButton;
    private JLabel statusLabel;
    
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color PRIMARY_HOVER_COLOR = new Color(60, 120, 170);
    
    public AdminLogin() {
        frame.setTitle("Admin Login - Warehouse Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        contentPanel.setMaximumSize(new Dimension(500, 600));
        
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.Y_AXIS));
        usernamePanel.setOpaque(false);
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernamePanel.setMaximumSize(new Dimension(400, 70));
        
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(400, 40));
        
        usernamePanel.add(usernameLabel);
        usernamePanel.add(Box.createVerticalStrut(8));
        usernamePanel.add(usernameField);
        
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setOpaque(false);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPanel.setMaximumSize(new Dimension(400, 70));
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(400, 40));
        
        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createVerticalStrut(8));
        passwordPanel.add(passwordField);
        
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(400, 45));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(PRIMARY_HOVER_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        employeeLoginButton = new JButton("Employee Login");
        employeeLoginButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        employeeLoginButton.setBorderPainted(false);
        employeeLoginButton.setContentAreaFilled(false);
        employeeLoginButton.setForeground(PRIMARY_COLOR);
        employeeLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        employeeLoginButton.setMaximumSize(new Dimension(400, 30));
        employeeLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        employeeLoginButton.addActionListener(e -> {
            EmployeeLogin login = new EmployeeLogin();
            login.show();
            frame.dispose();
        });
        
        employeeLoginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                employeeLoginButton.setForeground(PRIMARY_HOVER_COLOR);
                employeeLoginButton.setText("<html><u>Employee Login</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                employeeLoginButton.setForeground(PRIMARY_COLOR);
                employeeLoginButton.setText("Employee Login");
            }
        });
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(usernamePanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(passwordPanel);
        contentPanel.add(Box.createVerticalStrut(40));
        contentPanel.add(loginButton);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(employeeLoginButton);
        
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        cardPanel.add(contentPanel, BorderLayout.CENTER);
        
        wrapperPanel.add(cardPanel);
        frame.add(wrapperPanel);
        
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
        
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginAdmin();
                }
            }
        });
    }
    
    public void show() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        usernameField.requestFocus();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            loginAdmin();
        }
    }
    
    private void loginAdmin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password are required");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        loginButton.setEnabled(false);
        loginButton.setBackground(new Color(150, 180, 210));
        statusLabel.setText("Logging in...");
        statusLabel.setForeground(new Color(70, 130, 180));
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Thread.sleep(800);
                return username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD);
            }
            
            @Override
            protected void done() {
                try {
                    boolean isValid = get();
                    if (isValid) {
                        statusLabel.setText("Login successful! Redirecting...");
                        statusLabel.setForeground(new Color(0, 128, 0));
                        
                        Timer timer = new Timer(1000, evt -> {
                            Home homePage = new Home();
                            homePage.show();
                            frame.dispose();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        statusLabel.setText("Invalid username or password");
                        statusLabel.setForeground(Color.RED);
                        loginButton.setEnabled(true);
                        loginButton.setBackground(PRIMARY_COLOR);
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                    statusLabel.setForeground(Color.RED);
                    loginButton.setEnabled(true);
                    loginButton.setBackground(PRIMARY_COLOR);
                }
            }
        };
        
        worker.execute();
    }
    
    public static void main(String[] args) {
        AdminLogin login = new AdminLogin();
        login.show();
    }
} 