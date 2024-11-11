import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShowBookingsPage extends JFrame {
    private int currentUserId;

    public ShowBookingsPage(int currentUserId) {
        this.currentUserId = currentUserId;

        setTitle("Your Bookings");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel bookingsPanel = new JPanel();
        bookingsPanel.setBackground(new Color(255, 253, 208));
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));

        loadBookings(bookingsPanel);

        JButton cancelBookingsButton = new JButton("Cancel Bookings");
        cancelBookingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CancelBookingPage(currentUserId).setVisible(true);
                dispose();
            }
        });
        JPanel bottomPanel = new JPanel();
        
        bottomPanel.add(cancelBookingsButton);

         add(new JScrollPane(bookingsPanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void loadBookings(JPanel panel) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT destination, flight_details, booking_date FROM bookings WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {  // Check if ResultSet is empty
                panel.add(new JLabel("No bookings found."));
                dispose();
            } else {
                while (rs.next()) {
                    String destination = rs.getString("destination");
                    String flightDetails = rs.getString("flight_details");
                    String bookingDate = rs.getString("booking_date");

                    JLabel bookingLabel = new JLabel(
                        "<html>Destination: " + destination +
                        "<br>Flight Details: " + flightDetails +
                        "<br>Booked On: " + bookingDate + "<br><br></html>"
                    );
                    panel.add(bookingLabel);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        dispose();
    }
}
