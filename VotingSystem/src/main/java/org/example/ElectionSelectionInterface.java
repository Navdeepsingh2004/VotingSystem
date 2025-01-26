package com.example.votingsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ElectionSelectionInterface {
    public ElectionSelectionInterface() {
        JFrame frame = new JFrame("Select Election");
        JLabel label = new JLabel("Select Election:");
        JComboBox<String> electionComboBox = new JComboBox<>();
        JButton selectButton = new JButton("Select");

        label.setBounds(50, 50, 150, 30);
        electionComboBox.setBounds(200, 50, 150, 30);
        selectButton.setBounds(100, 100, 200, 40);

        frame.add(label);
        frame.add(electionComboBox);
        frame.add(selectButton);
        frame.setSize(400, 200);
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

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedElection = (String) electionComboBox.getSelectedItem();
                String electionId = selectedElection.split(":")[0]; // Get ID from selected item
                new CandidateSelectionInterface(Integer.parseInt(electionId));
            }
        });
    }
}
