package com.example.votingsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class ManageElectionsInterface {
    private JFrame frame;
    private JTextArea electionsArea;
    private JScrollPane scrollPane;

    ManageElectionsInterface() {
        frame = new JFrame("Manage Elections");
        JLabel label = new JLabel("Enter Election Name:");
        JTextField electionNameField = new JTextField();
        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        JTextField startDateField = new JTextField();
        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        JTextField endDateField = new JTextField();

        label.setBounds(50, 50, 150, 30);
        electionNameField.setBounds(200, 50, 150, 30);
        startDateLabel.setBounds(50, 100, 150, 30);
        startDateField.setBounds(200, 100, 150, 30);
        endDateLabel.setBounds(50, 150, 150, 30);
        endDateField.setBounds(200, 150, 150, 30);

        JButton createElectionButton = new JButton("Create Election");
        createElectionButton.setBounds(150, 200, 200, 40);

        electionsArea = new JTextArea();
        electionsArea.setEditable(false);  // Make it non-editable
        electionsArea.setLineWrap(true);   // Enable line wrapping
        electionsArea.setWrapStyleWord(true); // Wrap on word boundaries

        // Add the text area to a scroll pane
        scrollPane = new JScrollPane(electionsArea);
        scrollPane.setBounds(50, 250, 300, 100);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(label);
        frame.add(electionNameField);
        frame.add(startDateLabel);
        frame.add(startDateField);
        frame.add(endDateLabel);
        frame.add(endDateField);
        frame.add(createElectionButton);
        frame.add(scrollPane);  // Add the scroll pane instead of just the text area
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setVisible(true);

        // Load elections when the interface is opened
        loadElections();

        createElectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String electionName = electionNameField.getText();
                String startDate = startDateField.getText();
                String endDate = endDateField.getText();

                if (electionName.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                    return;
                }

                try {
                    Connection connection = DatabaseConnection.getConnection();
                    String query = "INSERT INTO Elections (election_name, start_date, end_date) VALUES (?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, electionName);
                    statement.setString(2, startDate);
                    statement.setString(3, endDate);
                    statement.executeUpdate();
                    connection.close();
                    JOptionPane.showMessageDialog(frame, "Election created successfully.");
                    electionNameField.setText("");
                    startDateField.setText("");
                    endDateField.setText("");
                    loadElections(); // Refresh elections list after creation
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error creating election: " + ex.getMessage());
                }
            }
        });
    }

    private void loadElections() {
        electionsArea.setText(""); // Clear previous list
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM Elections";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                electionsArea.append(resultSet.getInt("id") + ": " + resultSet.getString("election_name") + "\n");
            }
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading elections: " + ex.getMessage());
        }
    }
}
