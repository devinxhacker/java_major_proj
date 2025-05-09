package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

import main.ui.components.AdminHeader;

import main.jdbc.JDBCService;
import main.jdbc.JDBCService.*;
import main.jdbc.TransactionDAO.Transaction;

public class Transactions implements ActionListener {

	JFrame frame = new JFrame();
	JPanel contentPanel;
	JButton refreshButton;
	JPanel transactionList;
	JScrollPane scrollPane;
	private JDBCService jdbcService;

	public Transactions() {
		jdbcService = new JDBCService();

		frame.setTitle("Transactions History - Admin");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.getContentPane().setBackground(new Color(245, 247, 250));

		AdminHeader header = new AdminHeader(frame);
		frame.add(header, BorderLayout.NORTH);

		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(new Color(245, 247, 250));

		JPanel headerContainer = new JPanel();
		headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
		headerContainer.setBackground(new Color(245, 247, 250));
		headerContainer.setBorder(new EmptyBorder(20, 40, 0, 40));

		JPanel titleCardContainer = new JPanel();
		titleCardContainer.setLayout(new BorderLayout());
		titleCardContainer.setBackground(new Color(245, 247, 250));
		titleCardContainer.setBorder(new EmptyBorder(10, 20, 10, 20));
		titleCardContainer.setMaximumSize(new Dimension(1000, 80));
		titleCardContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel titlePanel = new JPanel(new BorderLayout(10, 0));
		titlePanel.setBackground(new Color(245, 247, 250));

		JLabel title = new JLabel("Transaction History");
		title.setFont(new Font("Serif", Font.BOLD, 28));

		refreshButton = new JButton("🔄 Refresh Data");
		refreshButton.setFocusPainted(false);
		refreshButton.setBackground(new Color(235, 240, 255));
		refreshButton.setForeground(new Color(43, 85, 222));
		refreshButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
		refreshButton.setBorder(new LineBorder(new Color(43, 85, 222), 1));
		refreshButton.setPreferredSize(new Dimension(160, 40));
		refreshButton.addActionListener(this);

		titlePanel.add(title, BorderLayout.WEST);
		titlePanel.add(refreshButton, BorderLayout.EAST);

		titleCardContainer.add(titlePanel, BorderLayout.CENTER);

		headerContainer.add(titleCardContainer);
		headerContainer.add(Box.createVerticalStrut(10));

		contentPanel.add(headerContainer, BorderLayout.NORTH);

		transactionList = new JPanel();
		transactionList.setLayout(new BoxLayout(transactionList, BoxLayout.Y_AXIS));
		transactionList.setOpaque(false);
		transactionList.setBorder(new EmptyBorder(10, 40, 10, 40));

		JLabel loadingLabel = new JLabel("Loading transactions...");
		loadingLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
		loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		transactionList.add(loadingLabel);

		scrollPane = new JScrollPane(transactionList);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(new Color(245, 247, 250));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		contentPanel.add(scrollPane, BorderLayout.CENTER);
		frame.add(contentPanel, BorderLayout.CENTER);

		fetchTransactions();
	}

	public void show() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Transactions obj = new Transactions();
		obj.show();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == refreshButton) {
			fetchTransactions();
		}
	}

	private void fetchTransactions() {

		transactionList.removeAll();
		JLabel loadingLabel = new JLabel("Loading transactions...");
		loadingLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
		loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		transactionList.add(loadingLabel);
		transactionList.revalidate();
		transactionList.repaint();

		SwingWorker<TransactionResponse, Void> worker = new SwingWorker<TransactionResponse, Void>() {
			@Override
			protected TransactionResponse doInBackground() throws Exception {
				return jdbcService.fetchAllTransactions();
			}

			@Override
			protected void done() {
				try {
					TransactionResponse response = get();
					if (response != null && response.success) {
						displayTransactions(response.data);
					} else {
						transactionList.removeAll();
						JLabel errorLabel = new JLabel("Failed to load transactions. Please try again.");
						errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
						errorLabel.setForeground(Color.RED);
						errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
						transactionList.add(errorLabel);
						transactionList.revalidate();
						transactionList.repaint();
					}
				} catch (Exception e) {
					System.err.println("Error fetching transactions: " + e.getMessage());
					transactionList.removeAll();
					JLabel errorLabel = new JLabel("Error loading transactions. Please try again.");
					errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
					errorLabel.setForeground(Color.RED);
					errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
					transactionList.add(errorLabel);
					transactionList.revalidate();
					transactionList.repaint();
				}
			}
		};

		worker.execute();
	}

	private void displayTransactions(List<Transaction> transactions) {
		transactionList.removeAll();

		if (transactions == null || transactions.isEmpty()) {
			JLabel noDataLabel = new JLabel("No transactions found.");
			noDataLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
			noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			transactionList.add(noDataLabel);
		} else {

			for (Transaction t : transactions) {
				JPanel card = new JPanel();
				card.setLayout(new BorderLayout());
				card.setMaximumSize(new Dimension(1000, 120));
				card.setBorder(new CompoundBorder(new EmptyBorder(10, 20, 10, 20), new LineBorder(Color.LIGHT_GRAY)));
				card.setBackground(t.type.equals("RECEIVE") ? new Color(235, 255, 235) : new Color(255, 235, 235));
				card.setAlignmentX(Component.CENTER_ALIGNMENT);

				String typeText = t.type.equals("RECEIVE") ? " 📥 RECEIVE" : " 📤 SEND";
				JLabel typeLabel = new JLabel(typeText);
				typeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
				typeLabel.setForeground(t.type.equals("RECEIVE") ? new Color(0, 128, 0) : new Color(200, 0, 0));

				JLabel dateLabel = new JLabel();
				if (t.date != null) {
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy • HH:mm");
					dateLabel.setText("📅 " + sdf.format(t.date));
				} else {
					dateLabel.setText("📅 Date not available");
				}
				dateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
				dateLabel.setForeground(new Color(60, 60, 100));
				dateLabel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
					BorderFactory.createEmptyBorder(4, 8, 4, 8)
				));
				dateLabel.setBackground(new Color(240, 240, 250));
				dateLabel.setOpaque(true);

				JPanel top = new JPanel(new BorderLayout());
				top.setOpaque(false);
				top.add(typeLabel, BorderLayout.WEST);
				top.add(dateLabel, BorderLayout.EAST);

				JLabel item = new JLabel("Item: " + t.itemName);
				JLabel id = new JLabel("Item ID: " + t.itemId);
				JLabel qty = new JLabel("Quantity: " + t.quantity);
				JLabel category = new JLabel("Category: " + t.categoryName);

				Font infoFont = new Font("SansSerif", Font.PLAIN, 16);
				item.setFont(infoFont);
				id.setFont(infoFont);
				qty.setFont(infoFont);
				category.setFont(infoFont);

				JPanel details = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
				details.setOpaque(false);
				details.add(item);
				details.add(category);
				details.add(qty);
				details.add(id);

				card.add(top, BorderLayout.NORTH);
				card.add(details, BorderLayout.CENTER);

				transactionList.add(card);
				transactionList.add(Box.createVerticalStrut(15));
			}
		}

		transactionList.revalidate();
		transactionList.repaint();
	}
}
