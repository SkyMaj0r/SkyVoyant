import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpPage extends JFrame {
    public SignUpPage() {
        setTitle("Sign Up");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField originField = new JTextField();
        JTextField destinationField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField usernameField = new JTextField();  // Username field
        JPasswordField passwordField = new JPasswordField();

        nameField.setPreferredSize(new Dimension(200, 30));
        ageField.setPreferredSize(new Dimension(200, 30));
        originField.setPreferredSize(new Dimension(200, 30));
        destinationField.setPreferredSize(new Dimension(200, 30));
        emailField.setPreferredSize(new Dimension(200, 30));
        phoneField.setPreferredSize(new Dimension(200, 30));
        usernameField.setPreferredSize(new Dimension(200, 30));
        passwordField.setPreferredSize(new Dimension(200, 30));

        JButton registerButton = new JButton("Register");

        String[] labels = {"Name:", "Age:", "Origin City:", "Destination City:", "Email:", "Phone:", "Username:", "Password:"};
        JTextField[] fields = {nameField, ageField, originField, destinationField, emailField, phoneField, usernameField, passwordField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        gbc.gridx = 1;
        gbc.gridy = labels.length;
        panel.add(registerButton, gbc);

        add(panel);

        // Register button action listener
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String originCity = originField.getText();
                String destinationCity = destinationField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String username = usernameField.getText();  // Retrieve username
                String password = new String(passwordField.getPassword());

                try {
                    registerUser(name, age, originCity, destinationCity, email, phone, username, password);
                    JOptionPane.showMessageDialog(SignUpPage.this, "Registration Successful!");
                    dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SignUpPage.this, "Error registering user.");
                }
            }
        });
    }

    // Register user in the database
    private void registerUser(String name, int age, String originCity, String destinationCity, String email, String phone, String username, String password) throws SQLException {
        Connection conn = DatabaseManager.getConnection();
        String query = "INSERT INTO profiles (name, age, origin_city, destination_city, email, phone, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setInt(2, age);
        stmt.setString(3, originCity);
        stmt.setString(4, destinationCity);
        stmt.setString(5, email);
        stmt.setString(6, phone);
        stmt.setString(7, username);  // Insert username
        stmt.setString(8, password);  // Insert password
        stmt.executeUpdate();
    }
}
