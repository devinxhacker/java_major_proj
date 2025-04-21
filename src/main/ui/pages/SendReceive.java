package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;

import main.api.ApiSchema;
import main.api.ApiService;
import main.ui.components.Header;
import main.api.ApiSchema.*;

public class SendReceive implements ActionListener {

	JFrame frame = new JFrame();
	private JTextField textField;
	private JComboBox<String> comboBox;
	private ApiService apiService;
	private List<ItemsData> itemsDataList;

	public SendReceive() {
		apiService = new ApiService();
		itemsDataList = new ArrayList<>();

		frame.setTitle("Send/Receive");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.getContentPane().setBackground(new Color(255, 255, 255));
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		// header starts here
		Header header = new Header(frame);
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
		// header ends here

		loadItems();
	}

	private void loadItems() {
		try {
			ItemsApiResponse response = apiService.fetchAllItems();
			if (response != null && response.success && response.data != null) {
				itemsDataList.clear();
				comboBox.removeAllItems();
				comboBox.addItem("Select an item...");
				for (ItemsData item : response.data) {
					itemsDataList.add(item);
					comboBox.addItem(item.name);
				}
			} else {
				JOptionPane.showMessageDialog(frame, "Failed to load items.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
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

			ItemsData selectedItem = null;
			for (ItemsData item : itemsDataList) {
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

			PostResponse response;
			if (type.equals("send")) {
				response = apiService.sendTransaction(payload);
			} else {
				response = apiService.receiveTransaction(payload);
			}

			if (response != null && response.success) {
				JOptionPane.showMessageDialog(frame, "Item " + type + " successful.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				textField.setText("");
				loadItems();
			} else {
				JOptionPane.showMessageDialog(frame,
						"Item " + type + " failed: " + (response != null ? response.message : "Unknown error"), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Invalid quantity format.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void show() {
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SendReceive obj = new SendReceive();
		obj.show();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
