import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class HealthTrackingApp {
    // Variables to store health data
    private int totalSteps = 0;
    private int totalCaloriesBurned = 0;
    private int totalSleepHours = 0;
    private int totalCaloriesConsumed = 0;
    private int goalSteps = 10000; // Default daily goal for steps
    private int goalCaloriesBurned = 2500; // Default goal for calories burned
    private int goalSleepHours = 8; // Default sleep hours goal

    // GUI Components
    private JFrame frame;
    private JTextField stepsField, caloriesBurnedField, caloriesConsumedField, sleepField;
    private JLabel summaryLabel;
    private JLabel progressLabel;
    private JButton logButton, summaryButton, setGoalsButton, exportButton;

    // User Profile data
    private String userName = "User";
    private int userHeight = 170; // Default height in cm
    private int userWeight = 70; // Default weight in kg

    // Health Data (for storing historical data)
    private Map<String, String> healthData = new HashMap<>();
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HealthTrackingApp app = new HealthTrackingApp();
            app.createAndShowGUI();
        });
    }

    // Method to create and show the GUI
    public void createAndShowGUI() {
        // Create the main frame
        frame = new JFrame("Health Tracking App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        // Create the panel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));

        // Add labels and text fields for input
        panel.add(new JLabel("Enter steps walked today:"));
        stepsField = new JTextField();
        panel.add(stepsField);

        panel.add(new JLabel("Enter calories burned today:"));
        caloriesBurnedField = new JTextField();
        panel.add(caloriesBurnedField);

        panel.add(new JLabel("Enter calories consumed today:"));
        caloriesConsumedField = new JTextField();
        panel.add(caloriesConsumedField);

        panel.add(new JLabel("Enter hours of sleep:"));
        sleepField = new JTextField();
        panel.add(sleepField);

        // Buttons
        logButton = new JButton("Log Data");
        logButton.addActionListener(e -> logData());
        panel.add(logButton);

        summaryButton = new JButton("View Summary");
        summaryButton.addActionListener(e -> showSummary());
        panel.add(summaryButton);

        setGoalsButton = new JButton("Set Goals");
        setGoalsButton.addActionListener(e -> setGoals());
        panel.add(setGoalsButton);

        exportButton = new JButton("Export Data");
        exportButton.addActionListener(e -> exportData());
        panel.add(exportButton);

        // Progress and summary label
        progressLabel = new JLabel("Track your health metrics!", JLabel.CENTER);
        summaryLabel = new JLabel("Enter data to start tracking.", JLabel.CENTER);

        frame.add(progressLabel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(summaryLabel, BorderLayout.SOUTH);

        // Show the frame
        frame.setVisible(true);
    }

    // Method to log the health data (steps, calories, sleep)
    private void logData() {
        try {
            int steps = Integer.parseInt(stepsField.getText());
            int caloriesBurned = Integer.parseInt(caloriesBurnedField.getText());
            int caloriesConsumed = Integer.parseInt(caloriesConsumedField.getText());
            int sleep = Integer.parseInt(sleepField.getText());

            // Add the entered data to the totals
            totalSteps += steps;
            totalCaloriesBurned += caloriesBurned;
            totalCaloriesConsumed += caloriesConsumed;
            totalSleepHours += sleep;

            // Save today's data
            String today = new Date().toString();
            healthData.put(today, "Steps: " + steps + ", Calories Burned: " + caloriesBurned + ", Calories Consumed: " + caloriesConsumed + ", Sleep: " + sleep);

            // Update progress
            updateProgress();

            // Clear input fields
            stepsField.setText("");
            caloriesBurnedField.setText("");
            caloriesConsumedField.setText("");
            sleepField.setText("");

            summaryLabel.setText("Data logged successfully!");
        } catch (NumberFormatException e) {
            summaryLabel.setText("Please enter valid numbers.");
        }
    }

    // Method to update progress
    private void updateProgress() {
        int stepsProgress = (totalSteps * 100) / goalSteps;
        int caloriesProgress = (totalCaloriesBurned * 100) / goalCaloriesBurned;
        int sleepProgress = (totalSleepHours * 100) / goalSleepHours;

        progressLabel.setText("<html>Steps: " + stepsProgress + "% of Goal<br>" +
                               "Calories Burned: " + caloriesProgress + "% of Goal<br>" +
                               "Sleep Hours: " + sleepProgress + "% of Goal</html>");
    }

    // Method to show the summary of the day
    private void showSummary() {
        summaryLabel.setText("<html>Today's Health Summary:<br>" +
                "Total Steps: " + totalSteps + "<br>" +
                "Total Calories Burned: " + totalCaloriesBurned + "<br>" +
                "Total Calories Consumed: " + totalCaloriesConsumed + "<br>" +
                "Total Sleep Hours: " + totalSleepHours + "</html>");
    }

    // Method to set goals for steps, calories, and sleep
    private void setGoals() {
        String stepsGoal = JOptionPane.showInputDialog(frame, "Set your daily steps goal:");
        String caloriesGoal = JOptionPane.showInputDialog(frame, "Set your daily calories burned goal:");
        String sleepGoal = JOptionPane.showInputDialog(frame, "Set your daily sleep hours goal:");

        goalSteps = Integer.parseInt(stepsGoal);
        goalCaloriesBurned = Integer.parseInt(caloriesGoal);
        goalSleepHours = Integer.parseInt(sleepGoal);

        summaryLabel.setText("Goals set successfully!");
        updateProgress();
    }

    // Method to export data to a file
    private void exportData() {
        try {
            FileWriter writer = new FileWriter("healthData.txt");
            for (Map.Entry<String, String> entry : healthData.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            writer.close();
            summaryLabel.setText("Data exported successfully!");
        } catch (IOException e) {
            summaryLabel.setText("Error exporting data.");
        }
    }
}
