import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

public class EnhancedTaskManagementApp {
    private JFrame frame;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JTextField taskField, searchField;
    private JComboBox<String> priorityBox, categoryBox;

    public EnhancedTaskManagementApp() {
        // Main Frame Setup
        frame = new JFrame("Enhanced Task Management App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        // Header
        JLabel header = new JLabel("Task Management App", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setOpaque(true);
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(frame.getWidth(), 50));

        // Panel Setup
        JPanel panel = new JPanel(new BorderLayout());

        // Table Setup
        String[] columns = {"Task", "Category", "Priority", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        taskTable = new JTable(tableModel);
        taskTable.setFont(new Font("Arial", Font.PLAIN, 14));
        taskTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(taskTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JPanel topRow = new JPanel(new FlowLayout());
        JPanel midRow = new JPanel(new FlowLayout());
        JPanel bottomRow = new JPanel(new FlowLayout());

        taskField = new JTextField(20);
        priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        categoryBox = new JComboBox<>(new String[]{"Work", "Personal", "Other"});
        JButton addButton = new JButton("➕ Add Task");

        topRow.add(new JLabel("Task: "));
        topRow.add(taskField);
        topRow.add(new JLabel("Category: "));
        topRow.add(categoryBox);
        topRow.add(new JLabel("Priority: "));
        topRow.add(priorityBox);
        topRow.add(addButton);

        // Action Buttons
        JButton deleteButton = new JButton("❌ Delete Task");
        JButton markCompleteButton = new JButton("✔️ Mark Complete");
        JButton saveButton = new JButton("💾 Save Tasks");
        JButton loadButton = new JButton("📂 Load Tasks");

        midRow.add(deleteButton);
        midRow.add(markCompleteButton);
        midRow.add(saveButton);
        midRow.add(loadButton);

        // Search and Stats
        searchField = new JTextField(20);
        JButton searchButton = new JButton("🔍 Search");
        JLabel statsLabel = new JLabel("Pending: 0 | Completed: 0");

        bottomRow.add(new JLabel("Search: "));
        bottomRow.add(searchField);
        bottomRow.add(searchButton);
        bottomRow.add(statsLabel);

        inputPanel.add(topRow);
        inputPanel.add(midRow);
        inputPanel.add(bottomRow);

        panel.add(inputPanel, BorderLayout.SOUTH);

        // Add Action Listeners
        addButton.addActionListener(e -> {
            String task = taskField.getText().trim();
            String category = (String) categoryBox.getSelectedItem();
            String priority = (String) priorityBox.getSelectedItem();

            if (!task.isEmpty()) {
                tableModel.addRow(new Object[]{task, category, priority, "Pending"});
                taskField.setText("");
                updateStats(statsLabel);
            } else {
                JOptionPane.showMessageDialog(frame, "Task cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                updateStats(statsLabel);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a task to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        markCompleteButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.setValueAt("Completed", selectedRow, 3);
                updateStats(statsLabel);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a task to mark as complete!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            if (!keyword.isEmpty()) {
                for (int i = 0; i < taskTable.getRowCount(); i++) {
                    String task = taskTable.getValueAt(i, 0).toString().toLowerCase();
                    if (!task.contains(keyword)) {
                        tableModel.removeRow(i);
                        i--;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Search field cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        saveButton.addActionListener(e -> saveTasks());
        loadButton.addActionListener(e -> loadTasks());

        // Add Components to Frame
        frame.add(header, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tasks.dat"))) {
            oos.writeObject(tableModel.getDataVector());
            JOptionPane.showMessageDialog(frame, "Tasks saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving tasks!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tasks.dat"))) {
            Vector<Vector> data = (Vector<Vector>) ois.readObject();
            tableModel.setRowCount(0); // Clear existing data
            for (Vector row : data) {
                tableModel.addRow(row);
            }
            JOptionPane.showMessageDialog(frame, "Tasks loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Error loading tasks!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStats(JLabel statsLabel) {
        int pendingCount = 0, completedCount = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String status = tableModel.getValueAt(i, 3).toString();
            if (status.equals("Pending")) pendingCount++;
            else if (status.equals("Completed")) completedCount++;
        }
        statsLabel.setText("Pending: " + pendingCount + " | Completed: " + completedCount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EnhancedTaskManagementApp::new);
    }
}
