import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SkyVoyant extends JFrame {
    private int loggedInUserId; // Variable to store the logged-in user's ID

    public SkyVoyant() {
        setTitle("SkyVoyant by SAV Industries");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set up the main panel with a background color
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 253, 208));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title label with styling
        JLabel titleLabel = new JLabel("SkyVoyant Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);

        // Username and password fields with custom styling
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setForeground(Color.WHITE);
        usernameField.setBackground(new Color(80, 80, 80));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setForeground(Color.WHITE);
        passwordField.setBackground(new Color(80, 80, 80));

        // Login and Sign Up buttons with custom colors and styling
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 122, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(60, 120, 60));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Add components with layout adjustments
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(loginButton, gbc);
        gbc.gridx = 1;
        panel.add(signUpButton, gbc);

        add(panel);

        // Login button action listener
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticateUser(username, password)) {
                    loggedInUserId = getUserId(username, password); // Get the logged-in user's ID
                    if (loggedInUserId != -1) {
                        MainMenuPage mainMenu = new MainMenuPage(loggedInUserId); // Pass user ID to main menu
                        mainMenu.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(SkyVoyant.this, "Error retrieving user ID.");
                    }
                } else {
                    JOptionPane.showMessageDialog(SkyVoyant.this, "Invalid username or password. Please try again.");
                }
            }
        });

        // Sign-up button action listener
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SignUpPage().setVisible(true);
            }
        });

        setVisible(true);
    }

    // Function to authenticate user
    private boolean authenticateUser(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM profiles WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a matching user is found
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Function to get user ID based on username and password
    private int getUserId(String username, String password) {
        int userId = -1; // Default value if no user is found
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT id FROM profiles WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id"); // Fetch the user's ID if credentials match
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userId;
    }

    public static void main(String[] args) {
        new SkyVoyant();
    }
}
