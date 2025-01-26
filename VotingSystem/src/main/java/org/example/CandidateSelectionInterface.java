package com.example.votingsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CandidateSelectionInterface {
    public CandidateSelectionInterface(int electionId) {
        JFrame frame = new JFrame("Select Candidate");
        JLabel label = new JLabel("Select Candidate:");
        JComboBox<String> candidateComboBox = new JComboBox<>();
        JButton voteButton = new JButton("Vote");

        label.setBounds(50, 50, 150, 30);
        candidateComboBox.setBounds(200, 50, 150, 30);
        voteButton.setBounds(100, 100, 200, 40);

        frame.add(label);
        frame.add(candidateComboBox);
        frame.add(voteButton);
        frame.setSize(400, 200);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load candidates into the combo box based on the selected election
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT id, name FROM Candidates WHERE election_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, electionId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                candidateComboBox.addItem(resultSet.getInt("id") + ": " + resultSet.getString("name"));
            }
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading candidates: " + ex.getMessage());
        }

        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCandidate = (String) candidateComboBox.getSelectedItem();
                String candidateId = selectedCandidate.split(":")[0]; // Get ID from selected item
                castVote(Integer.parseInt(candidateId));
            }
        });
    }

    public CandidateSelectionInterface() {

    }

    private void castVote(int candidateId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Votes (voter_id, candidate_id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 1); // Assuming a single voter for demo purposes; you can modify it to use actual voter IDs.
            statement.setInt(2, candidateId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Vote cast successfully.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error casting vote: " + ex.getMessage());
        }
    }
}