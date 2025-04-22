package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import main.jdbc.JDBCService;
import main.jdbc.JDBCService.RegisterPayload;
import main.jdbc.JDBCService.AuthResponse;

public class EmployeeRegister implements ActionListener {
    
    private JFrame frame = new JFrame();
    private JPanel contentPanel;
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton loginButton;
    private JLabel statusLabel;
    private JDBCService jdbcService;
    
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color PRIMARY_HOVER_COLOR = new Color(60, 120, 170);
    
    public EmployeeRegister() {
        jdbcService = new JDBCService();
        
        frame.setTitle("Employee Registration - Warehouse Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(30, 60, 30, 60));
        contentPanel.setMaximumSize(new Dimension(500, 650));
        
        JLabel titleLabel = new JLabel("Employee Registration");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);
        namePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        namePanel.setMaximumSize(new Dimension(400, 70));
        
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
        nameField = new JTextField();
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        nameField.setPreferredSize(new Dimension(400, 40));
        
        namePanel.add(nameLabel);
        namePanel.add(Box.createVerticalStrut(8));
        namePanel.add(nameField);
        
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
        
        JPanel confirmPasswordPanel = new JPanel();
        confirmPasswordPanel.setLayout(new BoxLayout(confirmPasswordPanel, BoxLayout.Y_AXIS));
        confirmPasswordPanel.setOpaque(false);
        confirmPasswordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmPasswordPanel.setMaximumSize(new Dimension(400, 70));
        
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        confirmPasswordField.setPreferredSize(new Dimension(400, 40));
        
        confirmPasswordPanel.add(confirmPasswordLabel);
        confirmPasswordPanel.add(Box.createVerticalStrut(8));
        confirmPasswordPanel.add(confirmPasswordField);
        
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        registerButton.setBackground(PRIMARY_COLOR);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(400, 45));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(this);
        
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(PRIMARY_HOVER_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        loginButton = new JButton("Already have an account? Login");
        loginButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setForeground(PRIMARY_COLOR);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(400, 30));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> {
            EmployeeLogin login = new EmployeeLogin();
            login.show();
            frame.dispose();
        });
        
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setForeground(PRIMARY_HOVER_COLOR);
                loginButton.setText("<html><u>Already have an account? Login</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setForeground(PRIMARY_COLOR);
                loginButton.setText("Already have an account? Login");
            }
        });
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(namePanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(usernamePanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(passwordPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(confirmPasswordPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(registerButton);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(loginButton);
        
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
        
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    usernameField.requestFocus();
                }
            }
        });
        
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
                    confirmPasswordField.requestFocus();
                }
            }
        });
        
        confirmPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registerEmployee();
                }
            }
        });
    }
    
    public void show() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        nameField.requestFocus();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            registerEmployee();
        }
    }
    
    private void registerEmployee() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("All fields are required");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            statusLabel.setForeground(Color.RED);
            confirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.RED));
            Timer timer = new Timer(2000, e -> {
                confirmPasswordField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
            });
            timer.setRepeats(false);
            timer.start();
            return;
        }
        
        registerButton.setEnabled(false);
        registerButton.setBackground(new Color(150, 180, 210));
        statusLabel.setText("Registering...");
        statusLabel.setForeground(new Color(70, 130, 180));
        
        SwingWorker<AuthResponse, Void> worker = new SwingWorker<AuthResponse, Void>() {
            @Override
            protected AuthResponse doInBackground() throws Exception {
                RegisterPayload payload = new RegisterPayload();
                payload.name = name;
                payload.username = username;
                payload.password = password;
                return jdbcService.registerEmployee(payload);
            }
            
            @Override
            protected void done() {
                try {
                    AuthResponse response = get();
                    if (response.success) {
                        statusLabel.setText("Registration successful! Redirecting to login...");
                        statusLabel.setForeground(new Color(0, 128, 0));
                        
                        Timer timer = new Timer(2000, evt -> {
                            EmployeeLogin login = new EmployeeLogin();
                            login.show();
                            frame.dispose();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        statusLabel.setText(response.message);
                        statusLabel.setForeground(Color.RED);
                        registerButton.setEnabled(true);
                        registerButton.setBackground(PRIMARY_COLOR);
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                    statusLabel.setForeground(Color.RED);
                    registerButton.setEnabled(true);
                    registerButton.setBackground(PRIMARY_COLOR);
                }
            }
        };
        
        worker.execute();
    }
    
    public static void main(String[] args) {
        EmployeeRegister register = new EmployeeRegister();
        register.show();
    }
} 