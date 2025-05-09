package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import main.ui.components.AdminHeader;

import main.jdbc.ItemDAO.Item;
import main.jdbc.CategoryDAO.Category;

public class CompartmentDetail implements ActionListener {
    
    private static final Color BACKGROUND_COLOR = new Color(248, 250, 252); // Light gray-blue
    private static final Color CARD_BACKGROUND = new Color(255, 255, 255); // White
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Modern blue
    private static final Color ACCENT_COLOR = new Color(59, 130, 246); // Light blue
    private static final Color SUCCESS_COLOR = new Color(16, 185, 129); //  green
    private static final Color WARNING_COLOR = new Color(245, 158, 11); // Amber
    
    private JFrame frame = new JFrame();
    
    public CompartmentDetail(Category data) {
        
        frame.setTitle("Compartment Detail - " + data.name + " - Admin");
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
        topPanel.setPreferredSize(new Dimension(1080, 80));
        topPanel.setBackground(CARD_BACKGROUND);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setBorder(new EmptyBorder(0, 0, 0, 0));
        buttonWrapper.setBackground(CARD_BACKGROUND);
        
        JButton backButton = new JButton("Back to Warehouse");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setBackground(PRIMARY_COLOR);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setPreferredSize(new Dimension(180, 35));
        
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(ACCENT_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        backButton.addActionListener(e -> {
            Warehouse newFrame = new Warehouse();
            newFrame.show();
            frame.dispose();
        });
        buttonWrapper.add(backButton);
        topPanel.add(buttonWrapper, BorderLayout.WEST);
        
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(CARD_BACKGROUND);
        
        JLabel panelTitle = new JLabel(data.name);
        panelTitle.setForeground(new Color(15, 23, 42)); 
        panelTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        titlePanel.add(panelTitle);
        
        topPanel.add(titlePanel, BorderLayout.CENTER);
        
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(CARD_BACKGROUND);
        emptyPanel.setPreferredSize(new Dimension(180, 35)); 
        topPanel.add(emptyPanel, BorderLayout.EAST);
        
        wrapperPanel.add(topPanel);
        content.add(wrapperPanel, BorderLayout.NORTH);
        
        JPanel wrapperCompartmentPane = new JPanel();
        wrapperCompartmentPane.setBackground(BACKGROUND_COLOR);
        
        JPanel compartmentPane = new JPanel();
        compartmentPane.setLayout(new BoxLayout(compartmentPane, BoxLayout.Y_AXIS));
        compartmentPane.setPreferredSize(new Dimension(1080, 550));
        compartmentPane.setBackground(CARD_BACKGROUND);
        compartmentPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20) 
        ));
        
        
        compartmentPane.add(Box.createVerticalStrut(10));
        
        JPanel compartmentOverview = new JPanel();
        compartmentOverview.setLayout(new BoxLayout(compartmentOverview, BoxLayout.Y_AXIS));
        compartmentOverview.setBackground(CARD_BACKGROUND);
        compartmentOverview.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        compartmentOverview.setAlignmentX(Component.CENTER_ALIGNMENT);
        compartmentOverview.setMaximumSize(new Dimension(1020, 120));
        
        JPanel capacityInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        capacityInfoPanel.setBackground(CARD_BACKGROUND);
        
        JLabel totalCapacityLabel = new JLabel("Total Capacity: " + data.maxCapacity);
        totalCapacityLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalCapacityLabel.setForeground(new Color(15, 23, 42)); 
        
        JLabel spaceUsedLabel = new JLabel("Space Used: " + data.currentCapacity);
        spaceUsedLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        spaceUsedLabel.setForeground(new Color(15, 23, 42)); 
        
        JLabel spaceAvailableLabel = new JLabel("Available Space: " + data.availableSpace);
        spaceAvailableLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        spaceAvailableLabel.setForeground(new Color(15, 23, 42)); 
        
        capacityInfoPanel.add(totalCapacityLabel);
        capacityInfoPanel.add(spaceUsedLabel);
        capacityInfoPanel.add(spaceAvailableLabel);
        
        compartmentOverview.add(capacityInfoPanel);
        
        JPanel progressPanel = new JPanel();
        progressPanel.setBackground(CARD_BACKGROUND);
        progressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JProgressBar progressBar = new JProgressBar(0, data.maxCapacity);
        progressBar.setValue(data.currentCapacity);
        progressBar.setStringPainted(true);
        progressBar.setString(data.utilizationPercentage + "%");
        progressBar.setForeground(data.utilizationPercentage > 80 ? WARNING_COLOR : SUCCESS_COLOR);
        progressBar.setBackground(new Color(226, 232, 240)); // Light gray
        progressBar.setPreferredSize(new Dimension(800, 25));
        
        progressPanel.add(progressBar);
        compartmentOverview.add(progressPanel);
                
        compartmentPane.add(compartmentOverview);
        
       
        compartmentPane.add(Box.createVerticalStrut(10)); 
        
        JPanel itemsTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itemsTitlePanel.setBackground(CARD_BACKGROUND);
        
        JLabel itemsTitle = new JLabel("Items in Compartment:");
        itemsTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        itemsTitle.setForeground(new Color(15, 23, 42)); 
        itemsTitle.setBorder(new EmptyBorder(0, 23, 0, 0));
        
        itemsTitlePanel.add(itemsTitle);
        compartmentPane.add(itemsTitlePanel);
        
        compartmentPane.add(Box.createVerticalStrut(5)); 
        
        JPanel itemsWrapper = new JPanel();
        itemsWrapper.setBackground(CARD_BACKGROUND);
        
        JPanel itemsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        itemsGrid.setPreferredSize(new Dimension(1020, 350));
        itemsGrid.setBackground(CARD_BACKGROUND);
        itemsGrid.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20)); 
        
        for (Item item : data.items)
            itemsGrid.add(createItemCard(item));
        
        itemsWrapper.add(itemsGrid);
        compartmentPane.add(itemsWrapper);
        
        wrapperCompartmentPane.add(compartmentPane);
        content.add(wrapperCompartmentPane, BorderLayout.SOUTH);
        
        frame.add(content, BorderLayout.CENTER);
    }
    
    private JPanel createItemCard(Item item) {
        JPanel card = new JPanel();
        card.setBackground(CARD_BACKGROUND);
        card.setLayout(new BorderLayout()); 
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12) 
        ));
        
        card.setPreferredSize(new Dimension(300, 210)); 
        card.setMinimumSize(new Dimension(300, 210));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_BACKGROUND);
        
        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(15, 23, 42)); 
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(nameLabel);
        
        contentPanel.add(Box.createVerticalStrut(10)); 
        
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(CARD_BACKGROUND);
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel idLabel = new JLabel("ID: " + item.id);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setForeground(new Color(71, 85, 105));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(idLabel);
        
        detailsPanel.add(Box.createVerticalStrut(5));
        
        JLabel currentLabel = new JLabel("Current: " + item.currentQuantity);
        currentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13)); 
        currentLabel.setForeground(new Color(71, 85, 105)); 
        currentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(currentLabel);
        
        detailsPanel.add(Box.createVerticalStrut(5)); 
        
        JLabel maxLabel = new JLabel("Maximum: " + item.maxQuantity);
        maxLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        maxLabel.setForeground(new Color(71, 85, 105));
        maxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(maxLabel);
        
        contentPanel.add(detailsPanel);
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        progressPanel.setBackground(CARD_BACKGROUND);
        
        JProgressBar progressBar = new JProgressBar(0, item.maxQuantity);
        progressBar.setValue(item.currentQuantity);
        progressBar.setStringPainted(true);
        progressBar.setString(item.currentQuantity + "/" + item.maxQuantity);
        progressBar.setForeground(item.currentQuantity > item.maxQuantity * 0.8 ? WARNING_COLOR : SUCCESS_COLOR);
        progressBar.setBackground(new Color(226, 232, 240));
        progressBar.setPreferredSize(new Dimension(270, 18)); 
        
        progressPanel.add(progressBar);
        
        card.add(progressPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    public void show() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}