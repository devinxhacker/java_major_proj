package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;


import main.ui.components.AdminHeader;

import main.jdbc.JDBCService;
import main.jdbc.JDBCService.*;
import main.jdbc.ItemDAO.Item;

public class AdminSendReceive implements ActionListener {

	JFrame frame = new JFrame();
	private JTextField textField;
	private JComboBox<String> comboBox;
	private JDBCService jdbcService;
	private List<Item> itemsList;

	public AdminSendReceive() {
		jdbcService = new JDBCService();
		itemsList = new ArrayList<>();

		frame.setTitle("Send/Receive Items - Admin");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.getContentPane().setBackground(new Color(245, 247, 250));
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		AdminHeader header = new AdminHeader(frame);
		frame.getContentPane().add(header, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(350, 68, 574, 508);
		panel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel = new JLabel("Send/Receive Items");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNewLabel.setBounds(139, 48, 264, 49);
		panel_1.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Select Item");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(228, 120, 101, 23);
		panel_1.add(lblNewLabel_1);

		comboBox = new JComboBox<>();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBox.setBounds(111, 153, 351, 31);
		panel_1.add(comboBox);

		JLabel lblNewLabel_1_1 = new JLabel("Enter Quantity");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(228, 239, 101, 23);
		panel_1.add(lblNewLabel_1_1);

		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setToolTipText("Enter Quantity");
		textField.setBackground(SystemColor.controlHighlight);
		textField.setBounds(111, 272, 351, 31);
		panel_1.add(textField);
		textField.setColumns(10);

		JButton btnReceiveItems = new JButton("Receive Items");
		btnReceiveItems.setForeground(new Color(255, 255, 255));
		btnReceiveItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				receiveItem();
			}
		});
		btnReceiveItems.setSelectedIcon(null);
		btnReceiveItems.setIcon(null);
		btnReceiveItems.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnReceiveItems.setBackground(new Color(0, 128, 0));
		btnReceiveItems.setBounds(300, 358, 162, 31);
		panel_1.add(btnReceiveItems);

		JButton btnSendItems = new JButton("Send Items");
		btnSendItems.setForeground(new Color(255, 255, 255));
		btnSendItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendItem();
			}
		});
		btnSendItems.setSelectedIcon(null);
		btnSendItems.setIcon(null);
		btnSendItems.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSendItems.setBackground(new Color(255, 0, 0));
		btnSendItems.setBounds(111, 358, 169, 31);
		panel_1.add(btnSendItems);

		loadItems();
	}

	private void loadItems() {
        comboBox.removeAllItems();
        comboBox.addItem("Loading items...");
        comboBox.setEnabled(false);
		
		SwingWorker<ItemsResponse, Void> worker = new SwingWorker<ItemsResponse, Void>() {
			@Override
			protected ItemsResponse doInBackground() throws Exception {
				return jdbcService.fetchAllItems();
			}
			
			@Override
			protected void done() {
				try {
					ItemsResponse response = get();
					if (response != null && response.success && response.data != null) {
						itemsList.clear();
						comboBox.removeAllItems();
						comboBox.addItem("Select an item...");
						for (Item item : response.data) {
							itemsList.add(item);
							comboBox.addItem(item.name);
						}
						comboBox.setEnabled(true);
					} else {
						comboBox.removeAllItems();
						comboBox.addItem("Failed to load items");
						JOptionPane.showMessageDialog(frame, "Failed to load items.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					comboBox.removeAllItems();
					comboBox.addItem("Error loading items");
					comboBox.setEnabled(true);
					JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		
		worker.execute();
	}

	private void sendItem() {
		handleTransaction("send");
	}

	private void receiveItem() {
		handleTransaction("receive");
	}

	private void handleTransaction(String type) {
		String selectedItemName = (String) comboBox.getSelectedItem();
		if (selectedItemName == null || selectedItemName.equals("Select an item...")) {
			JOptionPane.showMessageDialog(frame, "Please select an item.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String quantityText = textField.getText();
		if (quantityText.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Please enter a quantity.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			int quantity = Integer.parseInt(quantityText);
			if (quantity <= 0) {
				JOptionPane.showMessageDialog(frame, "Quantity must be greater than zero.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			Item selectedItem = null;
			for (Item item : itemsList) {
				if (item.name.equals(selectedItemName)) {
					selectedItem = item;
					break;
				}
			}

			if (selectedItem == null) {
				JOptionPane.showMessageDialog(frame, "Selected item not found.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			SendReceivePayload payload = new SendReceivePayload();
			payload.itemId = selectedItem.id;
			payload.quantity = quantity;
			String transactionType = type;

			SwingWorker<BasicResponse, Void> worker = new SwingWorker<BasicResponse, Void>() {
				@Override
				protected BasicResponse doInBackground() throws Exception {
					if (transactionType.equals("send")) {
						return jdbcService.sendTransaction(payload);
					} else {
						return jdbcService.receiveTransaction(payload);
					}
				}
				
				@Override
				protected void done() {
					try {
						BasicResponse response = get();
						if (response != null && response.success) {
							JOptionPane.showMessageDialog(frame, "Item " + transactionType + " successful.", "Success",
									JOptionPane.INFORMATION_MESSAGE);
							textField.setText("");
							loadItems();
						} else {
							JOptionPane.showMessageDialog(frame,
									"Item " + transactionType + " failed: " + (response != null ? response.message : "Unknown error"), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + e.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					} 
				}
			};
			
			worker.execute();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Invalid quantity format.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void show() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		AdminSendReceive obj = new AdminSendReceive();
		obj.show();
	}

	@Override
	public void actionPerformed(ActionEvent e) { 	
	} 

}