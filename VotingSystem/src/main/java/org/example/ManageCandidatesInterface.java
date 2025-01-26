package com.example.votingsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageCandidatesInterface {
    private JFrame frame;
    private JTextArea candidatesArea;
    private JComboBox<String> electionComboBox;

    public ManageCandidatesInterface() {
        frame = new JFrame("Manage Candidates");
        JLabel label = new JLabel("Select Election ID:");
        electionComboBox = new JComboBox<>();
        JLabel nameLabel = new JLabel("Candidate Name:");
        JTextField nameField = new JTextField();

        label.setBounds(50, 50, 150, 30);
        electionComboBox.setBounds(200, 50, 150, 30);
        nameLabel.setBounds(50, 100, 150, 30);
        nameField.setBounds(200, 100, 150, 30);

        JButton addCandidateButton = new JButton("Add Candidate");
        addCandidateButton.setBounds(150, 150, 200, 40);

        candidatesArea = new JTextArea();
        candidatesArea.setBounds(50, 200, 300, 150);
        candidatesArea.setEditable(false);

        frame.add(label);
        frame.add(electionComboBox);
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(addCandidateButton);
        frame.add(candidatesArea);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setVisible(true);

        // Load elections into the combo box
        loadElections();

        addCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedElection = (String) electionComboBox.getSelectedItem();
                if (selectedElection != null) {
                    String electionId = selectedElection.split(":")[0]; // Get ID from selected item
                    String candidateName = nameField.getText();

                    if (candidateName.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a Candidate Name.");
                        return;
                    }

                    try (Connection connection = DatabaseConnection.getConnection()) {
                        String query = "INSERT INTO Candidates (name, election_id) VALUES (?, ?)";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setString(1, candidateName);
                        statement.setInt(2, Integer.parseInt(electionId));
                        statement.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Candidate added successfully.");
                        nameField.setText("");
                        loadCandidates(Integer.parseInt(electionId)); // Refresh candidates list for the selected election
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, "Error adding candidate: " + ex.getMessage());
                    }
                }
            }
        });

        electionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedElection = (String) electionComboBox.getSelectedItem();
                if (selectedElection != null) {
                    String electionId = selectedElection.split(":")[0];
                    loadCandidates(Integer.parseInt(electionId)); // Load candidates for the selected election
                }
            }
        });
    }

    private void loadElections() {
        electionComboBox.removeAllItems(); // Clear previous items
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT id, election_name FROM Elections";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                electionComboBox.addItem(resultSet.getInt("id") + ": " + resultSet.getString("election_name"));
            }
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading elections: " + ex.getMessage());
        }
    }

    private void loadCandidates(int electionId) {
        candidatesArea.setText(""); // Clear previous candidates list
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Candidates WHERE election_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, electionId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                candidatesArea.append(resultSet.getInt("id") + ": " + resultSet.getString("name") + "\n");
            }
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading candidates: " + ex.getMessage());
        }
    }
}