import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CancelBookingPage extends JFrame {
    private int currentUserId;
    private JComboBox<String> bookingsDropdown;
    private List<Integer> bookingIds;

    public CancelBookingPage(int currentUserId) {
        this.currentUserId = currentUserId;

        setTitle("Cancel a Booking");
        setSize(800, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        bookingIds = new ArrayList<>();
        bookingsDropdown = new JComboBox<>();

        loadBookings();

        JButton cancelButton = new JButton("Cancel Selected Booking");

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(255, 253, 208));
        topPanel.add(new JLabel("Select Booking to Cancel:"));
        topPanel.add(bookingsDropdown);

        add(topPanel, BorderLayout.CENTER);
        add(cancelButton, BorderLayout.SOUTH);

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = bookingsDropdown.getSelectedIndex();
                if (selectedIndex >= 0) {
                    int bookingId = bookingIds.get(selectedIndex);
                    cancelBooking(bookingId);
                }
            }
        });

        setVisible(true);
    }

    private void loadBookings() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT booking_id, destination, flight_details, booking_date FROM bookings WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                String destination = rs.getString("destination");
                String flightDetails = rs.getString("flight_details");
                String bookingDate = rs.getString("booking_date");

                bookingIds.add(bookingId);
                bookingsDropdown.addItem("To " + destination + " | " + flightDetails + " | " + bookingDate);
            }

            if (bookingIds.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No bookings available to cancel.");
                dispose();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cancelBooking(int bookingId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String deleteQuery = "DELETE FROM bookings WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteQuery);
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Booking cancelled successfully!");

            // Refresh the dropdown
            bookingsDropdown.removeAllItems();
            bookingIds.clear();
            loadBookings();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to cancel booking.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
