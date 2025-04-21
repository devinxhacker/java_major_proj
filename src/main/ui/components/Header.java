package main.ui.components;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import main.ui.pages.*;

public class Header extends JPanel {
    
    public Header(JFrame currentFrame) {
    	
		this.setBackground(Color.cyan);
		this.setPreferredSize(new Dimension(1280, 40));
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		JButton title = new JButton("Warehouse Management System");
		title.setBorderPainted(false);
		title.setContentAreaFilled(false);
		title.setFocusPainted(false);
		title.setBorder(new EmptyBorder(0, 30, 0, 0));
		title.setFont(title.getFont().deriveFont(20f));
		title.addActionListener(e -> {
            Home newFrame = new Home();
            newFrame.show();
            currentFrame.dispose();
		});
		this.add(title, BorderLayout.WEST);
		
		
		JPanel navigationPanel = new JPanel();
		navigationPanel.setBackground(Color.cyan);
		navigationPanel.setBorder(new EmptyBorder(2, 0, 0, 30));
		
		JButton warehouseButton = new JButton("Warehouse");
		warehouseButton.addActionListener(e -> {
            Warehouse newFrame = new Warehouse();
            newFrame.show();
            currentFrame.dispose();
		});
		
		JButton sendReceiveButton = new JButton("Send/Receive");
		sendReceiveButton.addActionListener(e -> {
            SendReceive newFrame = new SendReceive();
            newFrame.show();
            currentFrame.dispose();
		});
		
		JButton transactionsButton = new JButton("Transactions");
		transactionsButton.addActionListener(e -> {
            Transactions newFrame = new Transactions();
            newFrame.show();
            currentFrame.dispose();
		});

		navigationPanel.add(warehouseButton);
		navigationPanel.add(Box.createHorizontalStrut(20));
		navigationPanel.add(sendReceiveButton);
		navigationPanel.add(Box.createHorizontalStrut(20));
		navigationPanel.add(transactionsButton);

		this.add(navigationPanel, BorderLayout.EAST);
	}

	public static void main(String[] args) {
		

	}

}
