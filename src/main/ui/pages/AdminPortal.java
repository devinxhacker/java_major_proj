package main.ui.pages;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import main.ui.components.AdminHeader;
import main.jdbc.JDBCService;
import main.jdbc.JDBCService.*;
import main.jdbc.RequestsDAO.Request;

public class AdminPortal implements ActionListener {

	JFrame frame = new JFrame();
	JPanel contentPanel;
	JButton refreshButton;
	JTable requestTable;
	JScrollPane scrollPane;
	private JDBCService jdbcService;
	private DefaultTableModel tableModel;
	private Font tableFont = new Font("Arial", Font.PLAIN, 14);
	private Font headerFont = new Font("Arial", Font.BOLD, 16);

	public AdminPortal() {
		jdbcService = new JDBCService();

		frame.setTitle("Admin Portal - Requests");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.getContentPane().setBackground(new Color(245, 247, 250));

		AdminHeader header = new AdminHeader(frame);
		frame.add(header, BorderLayout.NORTH);

		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(new Color(245, 247, 250));

		JPanel titlePanel = new JPanel(new BorderLayout(10, 10));
		titlePanel.setBorder(new EmptyBorder(20, 40, 20, 40));
		titlePanel.setBackground(new Color(245, 247, 250));

		JLabel title = new JLabel("Requests", SwingConstants.CENTER);
		title.setFont(new Font("Serif", Font.BOLD, 28));

		refreshButton = new JButton("🔄 Refresh Data");
		refreshButton.setFocusPainted(false);
		refreshButton.setBackground(new Color(235, 240, 255));
		refreshButton.setForeground(new Color(43, 85, 222));
		refreshButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
		refreshButton.setBorder(new LineBorder(new Color(43, 85, 222), 1));
		refreshButton.setPreferredSize(new Dimension(160, 40));
		refreshButton.addActionListener(this);

		titlePanel.add(Box.createHorizontalStrut(160), BorderLayout.WEST);
		titlePanel.add(title, BorderLayout.CENTER);
		titlePanel.add(refreshButton, BorderLayout.EAST);

		contentPanel.add(titlePanel, BorderLayout.NORTH);

		String[] columnNames = { "Request ID", "Item Name", "Item ID", "Quantity", "Type", "Status", "Accept", "Deny" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 6 || columnIndex == 7) {
					return JPanel.class;
				} else if (columnIndex == 5) {
					return String.class;
				}
				return Object.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 6 || column == 7;
			}
		};
		requestTable = new JTable(tableModel);
		requestTable.setRowHeight(30);
		requestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		requestTable.setDefaultRenderer(JPanel.class, new RadioButtonRenderer());
		requestTable.setDefaultEditor(JPanel.class, new RadioButtonEditor(new JCheckBox()));
		requestTable.setDefaultRenderer(String.class, new StatusRenderer());
		requestTable.getTableHeader().setFont(headerFont);
		requestTable.setFont(tableFont);
		requestTable.getColumnModel().getColumn(0).setPreferredWidth(80);
		requestTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		requestTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		requestTable.getColumnModel().getColumn(3).setPreferredWidth(80);
		requestTable.getColumnModel().getColumn(4).setPreferredWidth(80);
		requestTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		requestTable.getColumnModel().getColumn(6).setPreferredWidth(80);
		requestTable.getColumnModel().getColumn(7).setPreferredWidth(80);
		requestTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

		fetchRequests();
	}

	public void show() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		AdminPortal obj = new AdminPortal();
		obj.show();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == refreshButton) {
			fetchRequests();
		}
	}

	private void fetchRequests() {
		tableModel.setRowCount(0);
		SwingWorker<RequestResponse, Void> worker = new SwingWorker<RequestResponse, Void>() {
			@Override
			protected RequestResponse doInBackground() throws Exception {
				return jdbcService.fetchAllRequests();
			}

			@Override
			protected void done() {
				try {
					RequestResponse response = get();
					if (response != null && response.success) {
						displayRequests(response.data);
					} else {
						tableModel.addRow(new Object[] { "", "", "", "", "",
								"Failed to load requests. Please try again.", "", "" });
					}
				} catch (Exception e) {
					System.err.println("Error fetching requests: " + e.getMessage());
					tableModel.addRow(
							new Object[] { "", "", "", "", "", "Error loading requests. Please try again.", "", "" });
				}
			}
		};

		worker.execute();
	}

	private void displayRequests(List<Request> requests) {
		tableModel.setRowCount(0);

		if (requests == null || requests.isEmpty()) {
			tableModel.addRow(new Object[] { "", "", "", "", "", "No requests found.", "", "" });
		} else {
			for (Request r : requests) {
				JRadioButton acceptRadio = new JRadioButton();
				JRadioButton denyRadio = new JRadioButton();
				JLabel acceptLoadingLabel = new JLabel("");
				JLabel denyLoadingLabel = new JLabel("");

				JPanel acceptPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				acceptPanel.add(acceptRadio);
				acceptPanel.add(acceptLoadingLabel);
				acceptPanel.setOpaque(false);

				JPanel denyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				denyPanel.add(denyRadio);
				denyPanel.add(denyLoadingLabel);
				denyPanel.setOpaque(false);

				ButtonGroup buttonGroup = new ButtonGroup();
				buttonGroup.add(acceptRadio);
				buttonGroup.add(denyRadio);

				String statusText;
				if (r.status == 1) {
					acceptRadio.setSelected(true);
					denyRadio.setEnabled(false);
					acceptRadio.setEnabled(false);
					statusText = "Accepted";
				} else if (r.status == 0) {
					denyRadio.setSelected(true);
					acceptRadio.setEnabled(false);
					denyRadio.setEnabled(false);
					statusText = "Denied";
				} else {
					acceptRadio.setSelected(false);
					denyRadio.setSelected(false);
					statusText = "Pending";
				}

				acceptRadio.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (acceptRadio.isSelected()) {
							acceptLoadingLabel.setText("Accepting...");
							acceptLoadingLabel.setForeground(Color.BLUE);
							acceptPanel.revalidate();
							acceptPanel.repaint();
							acceptRadio.setEnabled(false);
							denyRadio.setEnabled(false);
							SwingWorker<BasicResponse, Void> worker = new SwingWorker<BasicResponse, Void>() {
								@Override
								protected BasicResponse doInBackground() throws Exception {
									return jdbcService.acceptRequest(r.requestId);
								}

								@Override
								protected void done() {
									try {
										BasicResponse response = get();
										if (response.success) {
											JOptionPane.showMessageDialog(frame, response.message, "Success",
													JOptionPane.INFORMATION_MESSAGE);
											fetchRequests();
										} else {
											JOptionPane.showMessageDialog(frame, response.message, "Error",
													JOptionPane.ERROR_MESSAGE);
										}
									} catch (Exception ex) {
										ex.printStackTrace();
										JOptionPane.showMessageDialog(frame, "An error occurred.", "Error",
												JOptionPane.ERROR_MESSAGE);
									} finally {
										acceptLoadingLabel.setText("");
										acceptPanel.revalidate();
										acceptPanel.repaint();
										acceptRadio.setEnabled(true);
										denyRadio.setEnabled(true);
									}
								}
							};
							worker.execute();
						}
					}
				});
				denyRadio.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (denyRadio.isSelected()) {
							denyLoadingLabel.setText("Denying...");
							denyLoadingLabel.setForeground(Color.RED);
							denyPanel.revalidate();
							denyPanel.repaint();
							acceptRadio.setEnabled(false);
							denyRadio.setEnabled(false);
							SwingWorker<BasicResponse, Void> worker = new SwingWorker<BasicResponse, Void>() {
								@Override
								protected BasicResponse doInBackground() throws Exception {
									return jdbcService.denyRequest(r.requestId);
								}

								@Override
								protected void done() {
									try {
										BasicResponse response = get();
										if (response.success) {
											JOptionPane.showMessageDialog(frame, response.message, "Success",
													JOptionPane.INFORMATION_MESSAGE);
											fetchRequests();
										} else {
											JOptionPane.showMessageDialog(frame, response.message, "Error",
													JOptionPane.ERROR_MESSAGE);
										}
									} catch (Exception ex) {
										ex.printStackTrace();
										JOptionPane.showMessageDialog(frame, "An error occurred.", "Error",
												JOptionPane.ERROR_MESSAGE);
									} finally {
										denyLoadingLabel.setText("");
										denyPanel.revalidate();
										denyPanel.repaint();
										acceptRadio.setEnabled(true);
										denyRadio.setEnabled(true);
									}
								}
							};
							worker.execute();
						}
					}
				});
				tableModel.addRow(new Object[] { r.requestId, r.itemName, r.itemId, r.quantity, r.requestType,
						statusText, acceptPanel, denyPanel });
			}
		}
	}

	class RadioButtonRenderer implements TableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (value == null)
				return null;
			if (value instanceof JPanel) {
				return (JPanel) value;
			} else {
				return new JLabel();
			}
		}
	}

	class RadioButtonEditor extends DefaultCellEditor implements MouseListener {
		private JPanel panel;
		private JRadioButton button;

		public RadioButtonEditor(JCheckBox checkBox) {
			super(checkBox);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			if (value == null)
				return null;
			if (value instanceof JPanel) {
				panel = (JPanel) value;
				for (Component comp : panel.getComponents()) {
					if (comp instanceof JRadioButton) {
						button = (JRadioButton) comp;
						button.addMouseListener(this);
					}
				}
				return panel;
			}
			return null;
		}

		@Override
		public Object getCellEditorValue() {
			return panel;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (button != null) {
				button.doClick();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

	class StatusRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			String status = (String) value;
			if ("Accepted".equals(status)) {
				c.setBackground(new Color(204, 255, 204));
			} else if ("Denied".equals(status)) {
				c.setBackground(new Color(255, 204, 204));
			} else {
				c.setBackground(table.getBackground());
			}
			c.setFont(tableFont);
			setBorder(new EmptyBorder(5, 10, 5, 10));
			return c;
		}
	}
}
