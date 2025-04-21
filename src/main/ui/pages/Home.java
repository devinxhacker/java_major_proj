package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import main.ui.components.Header;

public class Home implements ActionListener {
	
	JFrame frame = new JFrame();
	
	public Home() {
		
		frame.setTitle("Home");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.getContentPane().setBackground(Color.magenta);
		
		// header starts here
		Header header = new Header(frame);
		frame.add(header, BorderLayout.NORTH);
		// header ends here

		JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // Welcome Panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(180, 210, 255));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 50, 80)); // increased horizontal padding + bottom margin
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));

        JLabel welcomeText = new JLabel("Welcome to the Warehouse Management System");
        welcomeText.setFont(new Font("Serif", Font.BOLD, 30));
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subText = new JLabel("Efficiently manage your warehouse inventory with our comprehensive solution");
        subText.setFont(new Font("Serif", Font.PLAIN, 20));
        subText.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.add(welcomeText);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(subText);

        centerPanel.add(welcomePanel, BorderLayout.NORTH);

        // Cards Panel (from warehousetemp.java)
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(1, 3, 20, 10));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 30, 40)); // more space above cards

        cardsPanel.add(createCard("Warehouse Overview", "View all compartments and their contents at a glance", "View Warehouse"));
        cardsPanel.add(createCard("Send/Receive Items", "Process incoming and outgoing items", "Send/Receive Items"));
        cardsPanel.add(createCard("Transaction History", "Track all inventory movements and changes", "View Transactions"));

        centerPanel.add(cardsPanel, BorderLayout.CENTER);

        frame.add(centerPanel, BorderLayout.CENTER);
        
        // Unused buttons, kept as placeholders (not added to frame)
        JButton warehouseButton = new JButton("View Warehouse");
		warehouseButton.addActionListener(e -> {
            Warehouse newFrame = new Warehouse();
            newFrame.show();
		});
		
		JButton sendReceiveButton = new JButton("Send/Receive Items");
		sendReceiveButton.addActionListener(e -> {
            SendReceive newFrame = new SendReceive();
            newFrame.show();
		});
		
		JButton transactionsButton = new JButton("View Transactions");
		transactionsButton.addActionListener(e -> {
            Transactions newFrame = new Transactions();
            newFrame.show();
		});
	}
	
	public void show() {
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
	    card.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)
	    ));
	    card.setBackground(Color.WHITE);

	    JLabel titleLabel = new JLabel(title);
	    titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
	    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    // More descriptive content per card
	    String extendedDescription = switch (title) {
	        case "Warehouse Overview" -> description + 
	            "<br><br>Easily navigate your storage layout, check available space, and manage compartments efficiently.";
	        case "Send/Receive Items" -> description + 
	            "<br><br>Streamline the logistics of incoming supplies and outgoing shipments, reducing manual effort.";
	        case "Transaction History" -> description + 
	            "<br><br>Keep a complete record of item movements for tracking, auditing, and reporting purposes.";
	        default -> description;
	    };

	    JLabel descLabel = new JLabel("<html><div style='text-align: center; font-weight: plain;'>" + extendedDescription + "</div></html>");
	    descLabel.setFont(new Font("Serif", Font.BOLD, 16));  // Font is now bold
	    descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JButton button = new JButton(buttonText);
	    button.setFont(new Font("Serif", Font.BOLD, 16));
	    button.setBackground(new Color(30, 90, 255));
	    button.setForeground(Color.WHITE);
	    button.setFocusPainted(false);
	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    button.setMaximumSize(new Dimension(200, 45));

	    button.addActionListener(e -> {
	        frame.dispose();
	        switch (buttonText) {
	            case "View Warehouse" -> new Warehouse().show();
	            case "Send/Receive Items" -> new SendReceive().show();
	            case "View Transactions" -> new Transactions().show();
	            default -> JOptionPane.showMessageDialog(frame, "Unknown action: " + buttonText);
	        }
	    });

	    card.add(titleLabel);
	    card.add(Box.createRigidArea(new Dimension(0, 10)));
	    card.add(descLabel);
	    card.add(Box.createRigidArea(new Dimension(0, 15)));
	    card.add(button);

	    return card;
	}

}
