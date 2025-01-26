package com.example.votingsystem;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;           // Import this
import java.sql.DriverManager;       // Import this
import java.sql.PreparedStatement;    // Import this
import java.sql.ResultSet;            // Import this
import java.sql.SQLException;         // Import this

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginSignupInterface extends JFrame {
    private JTextField nameField;
    private JTextField idField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginSignupInterface() {
        setTitle("Login / Sign Up");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Sign Up button
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(50, 200, 100, 30);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSignUpForm();
            }
        });

        // Log In button
        JButton logInButton = new JButton("Log In");
        logInButton.setBounds(200, 200, 100, 30);
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginForm();
            }
        });

        add(signUpButton);
        add(logInButton);

        setVisible(true);
    }

    private void showSignUpForm() {
        JFrame signUpFrame = new JFrame("Sign Up");
        signUpFrame.setSize(400, 300);
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setLayout(null);

        // Sign Up form fields
        nameField = new JTextField();
        nameField.setBounds(150, 30, 200, 30);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 30, 100, 30);

        idField = new JTextField();
        idField.setBounds(150, 70, 200, 30);
        JLabel idLabel = new JLabel("ID (Aadhar):");
        idLabel.setBounds(50, 70, 100, 30);

        emailField = new JTextField();
        emailField.setBounds(150, 110, 200, 30);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 110, 100, 30);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 150, 200, 30);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 150, 100, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(150, 200, 100, 30);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String identification = idField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (saveUserData(name, identification, email, password)) {
                    JOptionPane.showMessageDialog(null, "Sign Up Successful! You can now log in.");
                    signUpFrame.dispose(); // Close sign-up form
                } else {
                    JOptionPane.showMessageDialog(null, "Error occurred during sign-up!");
                }
            }
        });

        // Add components to sign-up frame
        signUpFrame.add(nameField);
        signUpFrame.add(nameLabel);
        signUpFrame.add(idField);
        signUpFrame.add(idLabel);
        signUpFrame.add(emailField);
        signUpFrame.add(emailLabel);
        signUpFrame.add(passwordField);
        signUpFrame.add(passwordLabel);
        signUpFrame.add(submitButton);

        signUpFrame.setVisible(true);
    }

    private void showLoginForm() {
        JFrame loginFrame = new JFrame("Log In");
        loginFrame.setSize(400, 200);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLayout(null);

        emailField = new JTextField();
        emailField.setBounds(150, 30, 200, 30);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 30, 100, 30);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 70, 200, 30);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 70, 100, 30);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 110, 100, 30);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (verifyUser(email, password)) {
                    loginFrame.dispose(); // Close login form
                    new ElectionSelectionInterface(); // Open the election selection interface
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials, please try again.");
                }
            }
        });

        // Add components to login frame
        loginFrame.add(emailField);
        loginFrame.add(emailLabel);
        loginFrame.add(passwordField);
        loginFrame.add(passwordLabel);
        loginFrame.add(loginButton);

        loginFrame.setVisible(true);
    }


    private boolean saveUserData(String name, String identification, String email, String password) {
        // Database connection logic (assume a method DatabaseConnection.getConnection() is available)
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO new_voters (name, identification, email, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, identification);
                stmt.setString(3, email);
                stmt.setString(4, password);
                stmt.executeUpdate();
            }
            return true; // Sign up successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Sign up failed
        }
    }

    private boolean verifyUser(String email, String password) {
        // Database connection logic
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM new_voters WHERE email = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                return rs.next(); // Returns true if a user is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Login failed
        }
    }
}

