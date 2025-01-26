package com.example.votingsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultsInterface {
    public ResultsInterface() {
        JFrame frame = new JFrame("Election Results");
        JLabel label = new JLabel("Select Election:");
        JComboBox<String> electionComboBox = new JComboBox<>();
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        JButton showResultsButton = new JButton("Show Results");

        label.setBounds(50, 50, 150, 30);
        electionComboBox.setBounds(200, 50, 150, 30);
        resultsArea.setBounds(50, 100, 300, 200);
        showResultsButton.setBounds(100, 320, 200, 40);

        frame.add(label);
        frame.add(electionComboBox);
        frame.add(resultsArea);
        frame.add(showResultsButton);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load elections into the combo box
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

        showResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedElection = (String) electionComboBox.getSelectedItem();
                if (selectedElection != null) {
                    String electionId = selectedElection.split(":")[0]; // Get the election ID
                    loadResults(Integer.parseInt(electionId), resultsArea);
                }
            }
        });
    }

    private void loadResults(int electionId, JTextArea resultsArea) {
        resultsArea.setText(""); // Clear previous results
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT C.name, COUNT(V.candidate_id) AS votes " +
                    "FROM Candidates C LEFT JOIN Votes V ON C.id = V.candidate_id " +
                    "WHERE C.election_id = ? " +
                    "GROUP BY C.id ORDER BY votes DESC";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, electionId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultsArea.append(resultSet.getString("name") + ": " + resultSet.getInt("votes") + " votes\n");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading results: " + ex.getMessage());
        }
    }
}
