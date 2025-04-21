package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import main.api.ApiSchema.*;
import main.ui.components.Header;

public class CompartmentDetail implements ActionListener {
    
    private JFrame frame = new JFrame();
    
    public CompartmentDetail(CategoryData data) {
        
        frame.setTitle("Compartment Details - " + data.name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        
        // header starts here
        Header header = new Header(frame);
        frame.add(header, BorderLayout.NORTH);
        
        // main panel starts here
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        
        // navigation panel starts here
        JPanel wrapperPanel = new JPanel();
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(1080, 60));
        topPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        
        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setBorder(new EmptyBorder(10, 50, 0, 0));
        
        JButton backButton = new JButton("Back to Warehouse");
        backButton.addActionListener(e -> {
            Warehouse newFrame = new Warehouse();
            newFrame.show();
            frame.dispose();
        });
        buttonWrapper.add(backButton);
        topPanel.add(buttonWrapper, BorderLayout.WEST);
        
        JPanel titleWrapper = new JPanel();
        
        JLabel panelTitle = new JLabel(data.name);
        panelTitle.setForeground(Color.blue);
        panelTitle.setFont(panelTitle.getFont().deriveFont(25f));
        panelTitle.setBorder(new EmptyBorder(8, 0, 0, 0));
        
        titleWrapper.add(panelTitle);
        topPanel.add(titleWrapper, BorderLayout.CENTER);
        
        topPanel.add(Box.createHorizontalStrut(200), BorderLayout.EAST);
        
        wrapperPanel.add(topPanel);
        content.add(wrapperPanel, BorderLayout.NORTH);
        // navigation panel ends here
        
        // compartment details starts here
        JPanel wrapperCompartmentPane = new JPanel();
        
        JPanel compartmentPane = new JPanel();
        compartmentPane.setLayout(new BoxLayout(compartmentPane, BoxLayout.Y_AXIS));
        compartmentPane.setPreferredSize(new Dimension(1080, 550));
        compartmentPane.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        
        compartmentPane.add(Box.createVerticalStrut(20));
        
        // compartment overview starts here
        JPanel compartmentOverview = new JPanel();
        compartmentOverview.setLayout(new BoxLayout(compartmentOverview, BoxLayout.Y_AXIS));
        compartmentOverview.setBackground(Color.pink);
        compartmentOverview.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        compartmentOverview.setAlignmentX(Component.CENTER_ALIGNMENT);
        compartmentOverview.setMaximumSize(new Dimension(1020, 120));
        
        JPanel capacityInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        capacityInfoPanel.setBackground(Color.pink);
        
        JLabel totalCapacityLabel = new JLabel("Total Capacity: " + data.maxCapacity);
        totalCapacityLabel.setFont(totalCapacityLabel.getFont().deriveFont(16f));
        
        JLabel spaceUsedLabel = new JLabel("Space Used: " + data.currentCapacity);
        spaceUsedLabel.setFont(spaceUsedLabel.getFont().deriveFont(16f));
        
        JLabel spaceAvailableLabel = new JLabel("Available Space: " + data.availableSpace);
        spaceAvailableLabel.setFont(spaceAvailableLabel.getFont().deriveFont(16f));
        
        capacityInfoPanel.add(totalCapacityLabel);
        capacityInfoPanel.add(spaceUsedLabel);
        capacityInfoPanel.add(spaceAvailableLabel);
        
        compartmentOverview.add(capacityInfoPanel);
        
        JPanel progressPanel = new JPanel();
        progressPanel.setBackground(Color.pink);
        progressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JProgressBar progressBar = new JProgressBar(0, data.maxCapacity);
        progressBar.setValue(data.currentCapacity);
        progressBar.setStringPainted(true);
        progressBar.setString(data.utilizationPercentage + "%");
        progressBar.setForeground(Color.green);
        progressBar.setPreferredSize(new Dimension(800, 25));
        
        progressPanel.add(progressBar);
        compartmentOverview.add(progressPanel);
                
        compartmentPane.add(compartmentOverview);
        // compartment overview ends here
        
        compartmentPane.add(Box.createVerticalStrut(20));
        
        // items section title starts here
        JPanel itemsTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel itemsTitle = new JLabel("Items in Compartment:");
        itemsTitle.setFont(itemsTitle.getFont().deriveFont(Font.BOLD, 18f));
        itemsTitle.setBorder(new EmptyBorder(0, 23, 0, 0));
        
        itemsTitlePanel.add(itemsTitle);
        compartmentPane.add(itemsTitlePanel);
        // items section title ends here        
        
        // items grid starts here
        JPanel itemsWrapper = new JPanel();
        
        JPanel itemsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        itemsGrid.setPreferredSize(new Dimension(1020, 350));
        itemsGrid.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        
        for (ItemData item : data.items)
        	itemsGrid.add(createItemCard(item));
        
        itemsWrapper.add(itemsGrid);
        compartmentPane.add(itemsWrapper);
        
        wrapperCompartmentPane.add(compartmentPane);
        content.add(wrapperCompartmentPane, BorderLayout.SOUTH);
        // compartment details end here
        
        frame.add(content, BorderLayout.CENTER);
    }
    
    private JPanel createItemCard(ItemData item) {
        JPanel card = new JPanel();
        card.setBackground(Color.pink);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        
        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(nameLabel.getFont().deriveFont(16f));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);
        
        card.add(Box.createVerticalStrut(10));
        
        JLabel idLabel = new JLabel("ID: " + item.id);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(idLabel);
        
        card.add(Box.createVerticalStrut(10));
        
        JLabel currentLabel = new JLabel("Current: " + item.currentQuantity);
        currentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(currentLabel);
        
        card.add(Box.createVerticalStrut(10));
        
        JLabel maxLabel = new JLabel("Maximum: " + item.maxQuantity);
        maxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(maxLabel);
        
        card.add(Box.createVerticalStrut(15));
        
        JProgressBar progressBar = new JProgressBar(0, item.maxQuantity);
        progressBar.setValue(item.currentQuantity);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.green);
        progressBar.setMaximumSize(new Dimension(250, 20));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(progressBar);
        
        card.add(Box.createVerticalStrut(10));
        
        return card;
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle action events if needed
    }
}