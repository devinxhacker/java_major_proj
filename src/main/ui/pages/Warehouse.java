package main.ui.pages;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import main.api.*;
import main.api.ApiSchema.*;
import main.ui.components.Header;

public class Warehouse implements ActionListener, ComponentListener {
	
	private JFrame frame = new JFrame();
	private static JPanel compartments = new JPanel();
	
	public Warehouse() {
		
		frame.setTitle("Warehouse");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		
		// header starts here
		Header header = new Header(frame);
		frame.add(header, BorderLayout.NORTH);
		// header ends here
		
		
		// main panel starts here
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		content.setBackground(Color.yellow);
		
		
		// title + refresh button starts here
		JPanel wrapperPanel = new JPanel();
		wrapperPanel.setBackground(Color.yellow);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(1080, 60));
		topPanel.setBackground(Color.yellow);
		topPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
		
		
		JLabel panelTitle = new JLabel("Warehouse Compartments");
		panelTitle.setForeground(Color.blue);
		panelTitle.setFont(panelTitle.getFont().deriveFont(25f));
		panelTitle.setBorder(new EmptyBorder(0, 40, 0, 0));
		topPanel.add(panelTitle, BorderLayout.WEST);
		
		
		JPanel buttonWrapper = new JPanel();
		buttonWrapper.setBackground(Color.yellow);
		buttonWrapper.setBorder(new EmptyBorder(10, 0, 0, 40));
		
		JButton refreshButton = new JButton("Refresh");
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
		// title + refresh button ends here
		
		
		// compartments start here
        JPanel wrapperCompartment = new JPanel();
        wrapperCompartment.setBackground(Color.yellow);
        
		compartments.setLayout(new GridLayout(0, 3, 20, 20));
		compartments.setPreferredSize(new Dimension(1080, 550));
		compartments.setBackground(Color.yellow);
		compartments.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
	
		wrapperCompartment.add(compartments);
		content.add(wrapperCompartment, BorderLayout.SOUTH);
		// compartments end here
		
		frame.add(content, BorderLayout.CENTER);
		
		frame.addComponentListener(this);
	}
	
	private JPanel createCompartment(String compartmentName, int totalCapacity, int spaceUsed, CategoryData data) {
		
		JPanel card = new JPanel();
		card.setBackground(Color.pink);
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
		
		JLabel compartmentNameLabel = new JLabel(compartmentName);
		compartmentNameLabel.setFont(compartmentNameLabel.getFont().deriveFont(20f));
		compartmentNameLabel.setAlignmentX(Container.CENTER_ALIGNMENT);
		card.add(compartmentNameLabel);
		
		card.add(Box.createVerticalStrut(20));
		
		JLabel totalCapacityLabel = new JLabel("Total Capacity: " + totalCapacity);
		totalCapacityLabel.setAlignmentX(Container.CENTER_ALIGNMENT);
		card.add(totalCapacityLabel);
		
		card.add(Box.createVerticalStrut(20));
		
		JLabel spaceUsedLabel = new JLabel("Space Used: " + spaceUsed);
		spaceUsedLabel.setAlignmentX(Container.CENTER_ALIGNMENT);
		card.add(spaceUsedLabel);
		
		card.add(Box.createVerticalStrut(20));
		
		JLabel spaceAvailableLabel = new JLabel("Available Space: " + (totalCapacity - spaceUsed));
		spaceAvailableLabel.setAlignmentX(Container.CENTER_ALIGNMENT);
		card.add(spaceAvailableLabel);
		
		card.add(Box.createVerticalStrut(20));
		
		JProgressBar spaceBar = new JProgressBar(0, totalCapacity);
		spaceBar.setValue(spaceUsed);
		spaceBar.setStringPainted(true);
		spaceBar.setForeground(Color.green);
		spaceBar.setMaximumSize(new Dimension(200, 20));
		spaceBar.setAlignmentX(Container.CENTER_ALIGNMENT);
		card.add(spaceBar);
		
		card.add(Box.createVerticalStrut(20));
		
		JButton detailsButton = new JButton("View details");
		detailsButton.setAlignmentX(Container.CENTER_ALIGNMENT);
		detailsButton.addActionListener(e -> {
		    this.frame.dispose();
		    CompartmentDetail detailPage = new CompartmentDetail(data);
		    detailPage.show();
		});
		card.add(detailsButton);
		
		return card;
	}
	
	private void fetchData() {
		
		SwingWorker<WarehouseApiResponse, Void> worker = new SwingWorker<WarehouseApiResponse, Void>() {
			
			@Override
			protected WarehouseApiResponse doInBackground() throws Exception {
				
				ApiService service = new ApiService();
				WarehouseApiResponse response = service.fetchAllCompartments();
				return response;
			}
			
			@Override
			protected void done() {
				
				try {
					WarehouseApiResponse obj = (WarehouseApiResponse) get();
					if (obj.success) {
						compartments.removeAll();

						ApiSchema.CategoryData[] compartmentsArray = obj.data;
						for (int i = 0; i < compartmentsArray.length; i++) {
							compartments.add(createCompartment(
							    compartmentsArray[i].name, 
							    compartmentsArray[i].maxCapacity, 
							    compartmentsArray[i].currentCapacity,
							    compartmentsArray[i]
							));
						}

						compartments.revalidate();
						compartments.repaint();
					}
				} catch (Exception e) {
					System.err.println("lets cry about this");
				}
			}
		};
		
		worker.execute();
	}
	
	public void show() {
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		
		Warehouse obj = new Warehouse();
		obj.show();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

}
