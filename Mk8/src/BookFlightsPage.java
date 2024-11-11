import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookFlightsPage extends JFrame {
    private int currentUserId;
    private String destination;
    private JComboBox<String> flightsDropdown;
    private List<String> flights;

    public BookFlightsPage(int currentUserId, String destination) {
        this.currentUserId = currentUserId;
        this.destination = destination;

        setTitle("Available Flights to " + destination);
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        flights = generateFlights(destination);
        flightsDropdown = new JComboBox<>(flights.toArray(new String[0]));
        flightsDropdown.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton bookButton = new JButton("Book Selected Flight");
        bookButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookButton.setBackground(new Color(0, 123, 255));
        bookButton.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.add(new JLabel("Select a Flight:"));
        topPanel.add(flightsDropdown);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 253, 208));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(bookButton);

        add(mainPanel, BorderLayout.CENTER);

        bookButton.addActionListener(e -> bookFlight((String) flightsDropdown.getSelectedItem()));

        setVisible(true);
    }

    private List<String> generateFlights(String destination) {
        Random rand = new Random();
        List<String> generatedFlights = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String flightNumber = "FL" + (rand.nextInt(9000) + 1000);
            String departureTime = (rand.nextInt(12) + 1) + ":" + (rand.nextInt(60)) + (rand.nextBoolean() ? " AM" : " PM");
            int fare = (rand.nextInt(6) + 5) * 1000;

            String flightDetails = "Flight: " + flightNumber + " | Time: " + departureTime + " | Fare: ₹" + fare;
            generatedFlights.add(flightDetails);
        }

        return generatedFlights;
    }

    private void bookFlight(String selectedFlight) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String insertQuery = "INSERT INTO bookings (user_id, destination, flight_details) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setInt(1, currentUserId);
            stmt.setString(2, destination);
            stmt.setString(3, selectedFlight);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Flight booked successfully!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to book flight.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
