package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import main.ui.components.AdminHeader;

import main.jdbc.JDBCService;
import main.jdbc.JDBCService.*;
import main.jdbc.CategoryDAO.Category;

public class Warehouse implements ActionListener, ComponentListener {
	
	private static final Color BACKGROUND_COLOR = new Color(248, 250, 252); // Light gray-blue
	private static final Color CARD_BACKGROUND = new Color(255, 255, 255); // White
	private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Modern blue
	private static final Color ACCENT_COLOR = new Color(59, 130, 246); // Light blue
	private static final Color SUCCESS_COLOR = new Color(16, 185, 129); // Emerald green
	private static final Color WARNING_COLOR = new Color(245, 158, 11); // Amber
	
	private JFrame frame = new JFrame();
	private static JPanel compartments = new JPanel();
	private JDBCService jdbcService;

	public Warehouse() {
		jdbcService = new JDBCService();
		
		frame.setTitle("Warehouse - Admin");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.getContentPane().setBackground(BACKGROUND_COLOR);
		
		AdminHeader header = new AdminHeader(frame);
		frame.add(header, BorderLayout.NORTH);
		
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		content.setBackground(BACKGROUND_COLOR);
		
		JPanel wrapperPanel = new JPanel();
		wrapperPanel.setBackground(BACKGROUND_COLOR);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(1080, 60));
		topPanel.setBackground(CARD_BACKGROUND);
		topPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
			BorderFactory.createEmptyBorder(10, 20, 10, 20)
		));
		
		JLabel panelTitle = new JLabel("Warehouse Compartments");
		panelTitle.setForeground(new Color(15, 23, 42)); 
		panelTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		panelTitle.setBorder(new EmptyBorder(0, 40, 0, 0));
		topPanel.add(panelTitle, BorderLayout.WEST);
		
		JPanel buttonWrapper = new JPanel();
		buttonWrapper.setBorder(new EmptyBorder(10, 0, 0, 40));
		buttonWrapper.setBackground(CARD_BACKGROUND);
		
		JButton refreshButton = new JButton("Refresh");
		refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		refreshButton.setBackground(PRIMARY_COLOR);
		refreshButton.setForeground(Color.WHITE);
		refreshButton.setFocusPainted(false);
		refreshButton.setBorderPainted(false);
		refreshButton.setPreferredSize(new Dimension(100, 35));
		
		refreshButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				refreshButton.setBackground(ACCENT_COLOR);
			}
			public void mouseExited(MouseEvent e) {
				refreshButton.setBackground(PRIMARY_COLOR);
			}
		});
		
		refreshButton.addActionListener(e -> {
			compartments.removeAll();
			for (int i = 0; i < 5; i++)
				compartments.add(createCompartment("Refreshing...", 0, 0, null));
			compartments.revalidate();
			compartments.repaint();
			fetchData();
		});
		buttonWrapper.add(refreshButton);
		topPanel.add(buttonWrapper, BorderLayout.EAST);
		
		wrapperPanel.add(topPanel);
		content.add(wrapperPanel, BorderLayout.NORTH);
		
		JPanel wrapperCompartment = new JPanel();
		wrapperCompartment.setBackground(BACKGROUND_COLOR);
		
		compartments.setLayout(new GridLayout(0, 3, 20, 20));
		compartments.setPreferredSize(new Dimension(1080, 600));
		compartments.setBackground(BACKGROUND_COLOR);
		compartments.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	
		wrapperCompartment.add(compartments);
		content.add(wrapperCompartment, BorderLayout.SOUTH);
		
		frame.add(content, BorderLayout.CENTER);
		
		frame.addComponentListener(this);
	}
	
	private JPanel createCompartment(String compartmentName, int totalCapacity, int spaceUsed, Category data) {
		JPanel card = new JPanel();
		card.setBackground(CARD_BACKGROUND);
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
			BorderFactory.createEmptyBorder(20, 20, 20, 20)
		));
		
		card.setMinimumSize(new Dimension(300, 350));
		card.setPreferredSize(new Dimension(300, 350));
		
		card.add(Box.createVerticalStrut(10));
		
		JLabel compartmentNameLabel = new JLabel(compartmentName);
		compartmentNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
		compartmentNameLabel.setForeground(new Color(15, 23, 42)); 
		compartmentNameLabel.setAlignmentX(Container.CENTER_ALIGNMENT);
		card.add(compartmentNameLabel);
		
		card.add(Box.createVerticalStrut(20));
		
		JLabel totalCapacityLabel = new JLabel("Total Capacity: " + totalCapacity);
		totalCapacityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		totalCapacityLabel.setForeground(new Color(71, 85, 105)); 
		totalCapacityLabel.setAlignmentX(Container.CENTER_ALIGNMENT);
		card.add(totalCapacityLabel);
		
		card.add(Box.createVerticalStrut(15));
		
		JLabel spaceUsedLabel = new JLabel("Space Used: " + spaceUsed);
		spaceUsedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		spaceUsedLabel.setForeground(new Color(71, 85, 105));
		spaceUsedLabel.setAlignmentX(Container.CENTER_ALIGNMENT);
		card.add(spaceUsedLabel);
		
		card.add(Box.createVerticalStrut(15));
		
		JLabel spaceAvailableLabel = new JLabel("Available Space: " + (totalCapacity - spaceUsed));
		spaceAvailableLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		spaceAvailableLabel.setForeground(new Color(71, 85, 105)); 
		spaceAvailableLabel.setAlignmentX(Container.CENTER_ALIGNMENT);
		card.add(spaceAvailableLabel);
		
		card.add(Box.createVerticalStrut(20));
		
		JProgressBar spaceBar = new JProgressBar(0, totalCapacity);
		spaceBar.setValue(spaceUsed);
		spaceBar.setStringPainted(true);
		spaceBar.setForeground(spaceUsed > totalCapacity * 0.8 ? WARNING_COLOR : SUCCESS_COLOR);
		spaceBar.setBackground(new Color(226, 232, 240)); 
		spaceBar.setMaximumSize(new Dimension(200, 20));
		spaceBar.setAlignmentX(Container.CENTER_ALIGNMENT);
		card.add(spaceBar);
		
		card.add(Box.createVerticalGlue());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBackground(CARD_BACKGROUND);
		buttonPanel.setAlignmentX(Container.CENTER_ALIGNMENT);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		JButton detailsButton = new JButton("View details");
		detailsButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		detailsButton.setBackground(PRIMARY_COLOR);
		detailsButton.setForeground(Color.WHITE);
		detailsButton.setFocusPainted(false);
		detailsButton.setBorderPainted(false);
		detailsButton.setAlignmentX(Container.CENTER_ALIGNMENT);
		detailsButton.setPreferredSize(new Dimension(150, 35));
		detailsButton.setMaximumSize(new Dimension(150, 35));
		
		detailsButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				detailsButton.setBackground(ACCENT_COLOR);
			}
			public void mouseExited(MouseEvent e) {
				detailsButton.setBackground(PRIMARY_COLOR);
			}
		});
		
		detailsButton.addActionListener(e -> {
			frame.dispose();
			if (data != null) {
				CompartmentDetail detailPage = new CompartmentDetail(data);
				detailPage.show();
			} else {
				Warehouse newFrame = new Warehouse();
				newFrame.show();
			}
		});
		
		buttonPanel.add(detailsButton);
		card.add(buttonPanel);
		
		return card;
	}
	
	private void fetchData() {
		
		SwingWorker<WarehouseResponse, Void> worker = new SwingWorker<WarehouseResponse, Void>() {
			
			@Override
			protected WarehouseResponse doInBackground() throws Exception {
				return jdbcService.fetchAllCompartments();
			}
			
			@Override
			protected void done() {
				
				try {
					WarehouseResponse obj = get();
					if (obj.success) {
						compartments.removeAll();

						List<Category> categoriesList = obj.data;
						for (Category category : categoriesList) {
							compartments.add(createCompartment(
							    category.name, 
							    category.maxCapacity, 
							    category.currentCapacity,
							    category
							));
						}

						compartments.revalidate();
						compartments.repaint();
					}
				} catch (Exception e) {
					System.err.println("Error loading compartments: " + e.getMessage());
					e.printStackTrace();
				}
			}
		};
		
		worker.execute();
	}
	
	public void show() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		
		Warehouse obj = new Warehouse();
		obj.show();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		
	}

	@Override
	public void componentShown(ComponentEvent e) {

		compartments.removeAll();
        for (int i = 0; i < 5; i++) {
             compartments.add(createCompartment("Fetching...", 0, 0, null));
        }
        compartments.revalidate();
        compartments.repaint();
        
        fetchData(); 
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		
	}

}
