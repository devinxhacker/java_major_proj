package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import main.ui.components.EmployeeHeader;
import main.jdbc.JDBCService;
import main.jdbc.JDBCService.*;
import main.jdbc.RequestsDAO.Request;

public class EmployeeRequests implements ActionListener {

    private JFrame frame = new JFrame();
    private JPanel contentPanel;
    private JButton refreshButton;
    private JButton sendRequestButton;
    private JButton receiveRequestButton;
    private JTable requestTable;
    private JScrollPane scrollPane;
    private JDBCService jdbcService;
    private DefaultTableModel tableModel;
    private Font tableFont = new Font("Arial", Font.PLAIN, 14);
    private Font headerFont = new Font("Arial", Font.BOLD, 16);
    
    private String employeeName;
    private int employeeId;
    
    public EmployeeRequests(String name, int empId) {
        this.employeeName = name;
        this.employeeId = empId;
        jdbcService = new JDBCService();
        
        frame.setTitle("Employee Requests - " + employeeName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.getContentPane().setBackground(new Color(245, 247, 250));
        
        EmployeeHeader header = new EmployeeHeader(frame, employeeName, employeeId);
        frame.add(header, BorderLayout.NORTH);
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        
        JPanel titlePanel = new JPanel(new BorderLayout(10, 10));
        titlePanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        titlePanel.setBackground(new Color(245, 247, 250));
        
        JLabel title = new JLabel("Requests", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setOpaque(false);
        
        refreshButton = new JButton("🔄 Refresh");
        refreshButton.setFocusPainted(false);
        refreshButton.setBackground(new Color(235, 240, 255));
        refreshButton.setForeground(new Color(43, 85, 222));
        refreshButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        refreshButton.setBorder(new LineBorder(new Color(43, 85, 222), 1));
        refreshButton.setPreferredSize(new Dimension(120, 40));
        refreshButton.addActionListener(this);
        
        sendRequestButton = new JButton("Send Request");
        sendRequestButton.setFocusPainted(false);
        sendRequestButton.setBackground(new Color(235, 240, 255));
        sendRequestButton.setForeground(new Color(43, 85, 222));
        sendRequestButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sendRequestButton.setBorder(new LineBorder(new Color(43, 85, 222), 1));
        sendRequestButton.setPreferredSize(new Dimension(150, 40));
        sendRequestButton.addActionListener(this);
        
        receiveRequestButton = new JButton("Receive Request");
        receiveRequestButton.setFocusPainted(false);
        receiveRequestButton.setBackground(new Color(235, 240, 255));
        receiveRequestButton.setForeground(new Color(43, 85, 222));
        receiveRequestButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        receiveRequestButton.setBorder(new LineBorder(new Color(43, 85, 222), 1));
        receiveRequestButton.setPreferredSize(new Dimension(150, 40));
        receiveRequestButton.addActionListener(this);
        
        controlsPanel.add(sendRequestButton);
        controlsPanel.add(Box.createHorizontalStrut(10));
        controlsPanel.add(receiveRequestButton);
        controlsPanel.add(Box.createHorizontalStrut(20));
        controlsPanel.add(refreshButton);
        
        titlePanel.add(title, BorderLayout.CENTER);
        titlePanel.add(controlsPanel, BorderLayout.EAST);
        
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        
        String[] columnNames = { "Request ID", "Item Name", "Item ID", "Quantity", "Type", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = -7808548067883688534L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        requestTable = new JTable(tableModel);
        requestTable.setRowHeight(30);
        requestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestTable.getTableHeader().setFont(headerFont);
        requestTable.setFont(tableFont);
        requestTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        requestTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        requestTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        requestTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        requestTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        requestTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        requestTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 151668628600733011L;

			@Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                c.setFont(tableFont);
                setBorder(new EmptyBorder(5, 10, 5, 10));
                if (row % 2 == 0) {
                    c.setBackground(new Color(240, 240, 240));
                } else {
                    c.setBackground(table.getBackground());
                }
                return c;
            }
        });
        
        scrollPane = new JScrollPane(requestTable);
        scrollPane.setBorder(new EmptyBorder(10, 40, 10, 40));
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(contentPanel, BorderLayout.CENTER);
        
        fetchRequests(employeeId);
    }
    
    public void show() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshButton) {
            fetchRequests(employeeId);
        } else if (e.getSource() == sendRequestButton) {
            showCreateRequestDialog("SEND");
        } else if (e.getSource() == receiveRequestButton) {
            showCreateRequestDialog("RECEIVE");
        }
    }
    
    private void fetchRequests(int empId) {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[] { "Loading...", "", "", "", "", "" });
        
        SwingWorker<RequestResponse, Void> worker = new SwingWorker<RequestResponse, Void>() {
            @Override
            protected RequestResponse doInBackground() throws Exception {
                return jdbcService.fetchParticularEmployeeRequests(empId);
            }
            
            @Override
            protected void done() {
                try {
                    RequestResponse response = get();
                    tableModel.setRowCount(0);
                    
                    if (response != null && response.success) {
                        displayRequests(response.data);
                    } else {
                        tableModel.addRow(new Object[] { "", "Failed to load requests. Please try again.", "", "", "", "" });
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching requests: " + e.getMessage());
                    tableModel.setRowCount(0);
                    tableModel.addRow(new Object[] { "", "Error loading requests. Please try again.", "", "", "", "" });
                }
            }
        };
        
        worker.execute();
    }
    
    private void displayRequests(List<Request> requests) {
        if (requests == null || requests.isEmpty()) {
            tableModel.addRow(new Object[] { "", "No requests found.", "", "", "", "" });
        } else {
            for (Request r : requests) {
                String statusText;
                if (r.status == 1) {
                    statusText = "Accepted";
                } else if (r.status == 0) {
                    statusText = "Denied";
                } else {
                    statusText = "Pending";
                }
                
                tableModel.addRow(new Object[] {
                    r.requestId,
                    r.itemName,
                    r.itemId,
                    r.quantity,
                    r.requestType,
                    statusText
                });
            }
        }
    }
    
    private void showCreateRequestDialog(String requestType) {
        SwingWorker<ItemsResponse, Void> worker = new SwingWorker<ItemsResponse, Void>() {
            @Override
            protected ItemsResponse doInBackground() throws Exception {
                return jdbcService.fetchAllItems();
            }
            
            @Override
            protected void done() {
                try {
                    ItemsResponse response = get();
                    if (response != null && response.success && !response.data.isEmpty()) {
                        JDialog dialog = new JDialog(frame, "Create " + requestType + " Request", true);
                        dialog.setSize(500, 400);
                        dialog.setLayout(new BorderLayout());
                        dialog.getContentPane().setBackground(new Color(245, 247, 250));
                        
                        // Title Panel
                        JPanel titlePanel = new JPanel(new BorderLayout());
                        titlePanel.setBackground(new Color(245, 247, 250));
                        titlePanel.setBorder(new EmptyBorder(20, 30, 20, 30));
                        
                        JLabel titleLabel = new JLabel("Create " + requestType + " Request", SwingConstants.CENTER);
                        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
                        titleLabel.setForeground(new Color(30, 64, 175));
                        titlePanel.add(titleLabel, BorderLayout.CENTER);
                        
                        // Content Panel
                        JPanel contentPanel = new JPanel();
                        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
                        contentPanel.setBackground(new Color(245, 247, 250));
                        contentPanel.setBorder(new EmptyBorder(0, 30, 20, 30));
                        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        
                        // Item Selection Panel
                        JPanel itemPanel = new JPanel();
                        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
                        itemPanel.setBackground(new Color(245, 247, 250));
                        itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        itemPanel.setMaximumSize(new Dimension(440, 80));
                        
                        JLabel itemLabel = new JLabel("Select Item", SwingConstants.CENTER);
                        itemLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                        itemLabel.setForeground(new Color(30, 64, 175));
                        itemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        
                        DefaultComboBoxModel<String> itemModel = new DefaultComboBoxModel<>();
                        int[] itemIds = new int[response.data.size()];
                        int index = 0;
                        
                        for (var item : response.data) {
                            itemModel.addElement(item.name);
                            itemIds[index++] = item.id;
                        }
                        
                        JComboBox<String> itemComboBox = new JComboBox<>(itemModel);
                        itemComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
                        itemComboBox.setBackground(Color.WHITE);
                        itemComboBox.setMaximumSize(new Dimension(440, 35));
                        itemComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
                        
                        itemPanel.add(itemLabel);
                        itemPanel.add(Box.createVerticalStrut(8));
                        itemPanel.add(itemComboBox);
                        
                        // Quantity Panel
                        JPanel quantityPanel = new JPanel();
                        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.Y_AXIS));
                        quantityPanel.setBackground(new Color(245, 247, 250));
                        quantityPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        quantityPanel.setMaximumSize(new Dimension(440, 80));
                        
                        JLabel quantityLabel = new JLabel("Quantity", SwingConstants.CENTER);
                        quantityLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                        quantityLabel.setForeground(new Color(30, 64, 175));
                        quantityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        
                        JTextField quantityField = new JTextField();
                        quantityField.setFont(new Font("SansSerif", Font.PLAIN, 14));
                        quantityField.setMaximumSize(new Dimension(440, 35));
                        quantityField.setAlignmentX(Component.CENTER_ALIGNMENT);
                        quantityField.setHorizontalAlignment(JTextField.CENTER);
                        
                        quantityPanel.add(quantityLabel);
                        quantityPanel.add(Box.createVerticalStrut(8));
                        quantityPanel.add(quantityField);
                        
                        // Buttons Panel
                        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
                        buttonsPanel.setBackground(new Color(245, 247, 250));
                        
                        JButton submitButton = new JButton("Submit");
                        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
                        submitButton.setForeground(Color.WHITE);
                        submitButton.setBackground(new Color(43, 85, 222));
                        submitButton.setFocusPainted(false);
                        submitButton.setBorderPainted(false);
                        submitButton.setPreferredSize(new Dimension(120, 40));
                        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        
                        JButton cancelButton = new JButton("Cancel");
                        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
                        cancelButton.setForeground(new Color(43, 85, 222));
                        cancelButton.setBackground(new Color(235, 240, 255));
                        cancelButton.setFocusPainted(false);
                        cancelButton.setBorder(new LineBorder(new Color(43, 85, 222), 1));
                        cancelButton.setPreferredSize(new Dimension(120, 40));
                        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        
                        // Add hover effects
                        submitButton.addMouseListener(new MouseAdapter() {
                            public void mouseEntered(MouseEvent e) {
                                submitButton.setBackground(new Color(37, 99, 235));
                            }
                            public void mouseExited(MouseEvent e) {
                                submitButton.setBackground(new Color(43, 85, 222));
                            }
                        });
                        
                        cancelButton.addMouseListener(new MouseAdapter() {
                            public void mouseEntered(MouseEvent e) {
                                cancelButton.setBackground(new Color(225, 230, 245));
                            }
                            public void mouseExited(MouseEvent e) {
                                cancelButton.setBackground(new Color(235, 240, 255));
                            }
                        });
                        
                        buttonsPanel.add(submitButton);
                        buttonsPanel.add(cancelButton);
                        
                        // Add all panels to content panel with proper alignment
                        contentPanel.add(Box.createVerticalStrut(20));
                        contentPanel.add(itemPanel);
                        contentPanel.add(Box.createVerticalStrut(20));
                        contentPanel.add(quantityPanel);
                        contentPanel.add(Box.createVerticalStrut(30));
                        contentPanel.add(buttonsPanel);
                        
                        // Add panels to dialog
                        dialog.add(titlePanel, BorderLayout.NORTH);
                        dialog.add(contentPanel, BorderLayout.CENTER);
                        
                        submitButton.addActionListener(e -> {
                            try {
                                int quantity = Integer.parseInt(quantityField.getText().trim());
                                int selectedItemIndex = itemComboBox.getSelectedIndex();
                                int itemId = itemIds[selectedItemIndex];
                                
                                if (quantity <= 0) {
                                    JOptionPane.showMessageDialog(dialog, "Quantity must be greater than zero", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                
                                dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                
                                SwingWorker<BasicResponse, Void> requestWorker = new SwingWorker<BasicResponse, Void>() {
                                    @Override
                                    protected BasicResponse doInBackground() throws Exception {
                                        CreateRequestPayload payload = new CreateRequestPayload();
                                        payload.itemId = itemId;
                                        payload.empId = employeeId;
                                        payload.quantity = quantity;
                                        payload.type = requestType;
                                        return jdbcService.createRequest(payload);
                                    }
                                    
                                    @Override
                                    protected void done() {
                                        dialog.setCursor(Cursor.getDefaultCursor());
                                        try {
                                            BasicResponse response = get();
                                            if (response.success) {
                                                JOptionPane.showMessageDialog(dialog, "Request created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                                dialog.dispose();
                                                fetchRequests(employeeId);
                                            } else {
                                                JOptionPane.showMessageDialog(dialog, response.message, "Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        } catch (Exception ex) {
                                            JOptionPane.showMessageDialog(dialog, "Error creating request: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                };
                                
                                requestWorker.execute();
                                
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for quantity", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                        
                        cancelButton.addActionListener(e -> dialog.dispose());
                        
                        dialog.setLocationRelativeTo(frame);
                        dialog.setVisible(true);
                        
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to load items. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Error loading items: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    public static void main(String[] args) {
        EmployeeRequests requests = new EmployeeRequests("Test Employee", 1);
        requests.show();
    }
} 