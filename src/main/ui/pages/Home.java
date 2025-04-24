package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import main.ui.components.AdminHeader;

public class Home implements ActionListener {
	
	private static final Color BACKGROUND_COLOR = new Color(248, 250, 252); // Light gray-blue
	private static final Color CARD_BACKGROUND = new Color(255, 255, 255); // White
	private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Modern blue
	private static final Color ACCENT_COLOR = new Color(59, 130, 246); // Light blue
	
	JFrame frame = new JFrame();
	
	public Home() {
		
		frame.setTitle("Home - Admin");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.getContentPane().setBackground(BACKGROUND_COLOR);
		
		AdminHeader header = new AdminHeader(frame);
		frame.add(header, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(CARD_BACKGROUND);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(40, 80, 50, 80)
        ));
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));

        JLabel welcomeText = new JLabel("Welcome to the Warehouse Management System");
        welcomeText.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeText.setForeground(new Color(15, 23, 42)); 
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subText = new JLabel("Efficiently manage your warehouse inventory with our comprehensive solution");
        subText.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subText.setForeground(new Color(71, 85, 105)); 
        subText.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.add(welcomeText);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        welcomePanel.add(subText);

        centerPanel.add(welcomePanel, BorderLayout.NORTH);

        
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(1, 3, 20, 10));
        cardsPanel.setBackground(BACKGROUND_COLOR);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 30, 40));

        cardsPanel.add(createCard("Warehouse Overview", "View all compartments and their contents at a glance", "View Warehouse"));
        cardsPanel.add(createCard("Send/Receive Items", "Process incoming and outgoing items", "Send/Receive Items"));
        cardsPanel.add(createCard("Transaction History", "Track all inventory movements and changes", "View Transactions"));

        centerPanel.add(cardsPanel, BorderLayout.CENTER);

        frame.add(centerPanel, BorderLayout.CENTER);
        
        JButton warehouseButton = new JButton("View Warehouse");
		warehouseButton.addActionListener(e -> {
            Warehouse newFrame = new Warehouse();
            newFrame.show();
		});
		
		JButton sendReceiveButton = new JButton("Send/Receive Items");
		sendReceiveButton.addActionListener(e -> {
            AdminSendReceive newFrame = new AdminSendReceive();
            newFrame.show();
		});
		
		JButton transactionsButton = new JButton("View Transactions");
		transactionsButton.addActionListener(e -> {
            Transactions newFrame = new Transactions();
            newFrame.show();
		});
	}
	
	public void show() {
        frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Home obj = new Home();
		obj.show();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	private JPanel createCard(String title, String description, String buttonText) {
	    JPanel card = new JPanel();
	    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
	    card.setBackground(CARD_BACKGROUND);
	    card.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
	            BorderFactory.createEmptyBorder(24, 24, 24, 24)
	    ));

	    JLabel titleLabel = new JLabel(title);
	    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
	    titleLabel.setForeground(new Color(15, 23, 42));
	    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    String extendedDescription = switch (title) {
	        case "Warehouse Overview" -> description + 
	            "<br><br>Easily navigate your storage layout, check available space, and manage compartments efficiently.";
	        case "Send/Receive Items" -> description + 
	            "<br><br>Streamline the logistics of incoming supplies and outgoing shipments, reducing manual effort.";
	        case "Transaction History" -> description + 
	            "<br><br>Keep a complete record of item movements for tracking, auditing, and reporting purposes.";
	        default -> description;
	    };

	    JLabel descLabel = new JLabel("<html><div style='text-align: center; color: #475569;'>" + extendedDescription + "</div></html>");
	    descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	    descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JPanel descPanel = new JPanel();
	    descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
	    descPanel.setBackground(CARD_BACKGROUND);
	    descPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150)); 
	    descPanel.add(descLabel);

	    JButton button = new JButton(buttonText);
	    button.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    button.setBackground(PRIMARY_COLOR);
	    button.setForeground(Color.WHITE);
	    button.setFocusPainted(false);
	    button.setBorderPainted(false);
	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    button.setMaximumSize(new Dimension(200, 45));
	    
	    button.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent e) {
	            button.setBackground(ACCENT_COLOR);
	        }
	        public void mouseExited(MouseEvent e) {
	            button.setBackground(PRIMARY_COLOR);
	        }
	    });

	    button.addActionListener(e -> {
	        frame.dispose();
	        switch (buttonText) {
	            case "View Warehouse" -> new Warehouse().show();
	            case "Send/Receive Items" -> new AdminSendReceive().show();
	            case "View Transactions" -> new Transactions().show();
	            default -> JOptionPane.showMessageDialog(frame, "Unknown action: " + buttonText);
	        }
	    });

	    card.add(titleLabel);
	    card.add(Box.createRigidArea(new Dimension(0, 15)));
	    card.add(descPanel);
	    card.add(Box.createRigidArea(new Dimension(0, 45))); 
	    card.add(button);

	    return card;
	}

}
