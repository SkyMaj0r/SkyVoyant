import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPage extends JFrame {

    public MainMenuPage(int currentUserId) {
        setTitle("Main Menu");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Main panel with light yellow background
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(255, 253, 208)); // Light yellow background

        // Title label with font styling
        JLabel titleLabel = new JLabel("Main Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(60, 63, 65)); // Dark gray text
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(20)); // Add spacing

        // Destination dropdown section
        JLabel destinationLabel = new JLabel("Select Destination:");
        destinationLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        destinationLabel.setForeground(Color.DARK_GRAY);
        destinationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] locations = {"Jamshedpur", "Patna", "Lucknow", "Hyderabad", "Ayodhya"};
        JComboBox<String> locationDropdown = new JComboBox<>(locations);
        locationDropdown.setMaximumSize(new Dimension(200, 30));
        locationDropdown.setFont(new Font("Arial", Font.PLAIN, 14));
        locationDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(destinationLabel);
        panel.add(Box.createVerticalStrut(10)); // Spacing
        panel.add(locationDropdown);

        // Button panel with grid layout and light yellow background
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 15)); // 1 column layout with spacing
        buttonPanel.setBackground(new Color(255, 253, 208)); // Matching background color

        // Create and style buttons
        JButton bookFlightsButton = createButton("Book Flights");
        JButton showBookingsButton = createButton("Show Flight Bookings");
        JButton logoutButton = createButton("Logout");

        // Add buttons to the button panel
        buttonPanel.add(bookFlightsButton);
        buttonPanel.add(showBookingsButton);
        buttonPanel.add(logoutButton);

        panel.add(Box.createVerticalStrut(20)); // Add spacing before buttons
        panel.add(buttonPanel);
        add(panel);

        // Button action listeners
        bookFlightsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedLocation = (String) locationDropdown.getSelectedItem();
                new BookFlightsPage(currentUserId, selectedLocation).setVisible(true);
            }
        });

        showBookingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ShowBookingsPage(currentUserId).setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SkyVoyant().setVisible(true);
            }
        });
    }

    // Helper method to create and style buttons
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(180, 45));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180)); // Steel blue color
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return button;
    }

    public static void main(String[] args) {
        // Example usage with user ID = 1
        new MainMenuPage(1).setVisible(true);
    }
}
