package com.example.votingsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AdminInterface {
    AdminInterface() {
        JFrame frame = new JFrame("Admin Panel");
        JButton manageElectionButton = new JButton("Manage Elections");
        JButton manageCandidateButton = new JButton("Manage Candidates");

        manageElectionButton.setBounds(100, 100, 200, 40);
        manageCandidateButton.setBounds(100, 200, 200, 40);

        frame.add(manageElectionButton);
        frame.add(manageCandidateButton);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setVisible(true);

        manageElectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageElectionsInterface();
            }
        });

        manageCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageCandidatesInterface(); // This should work now
            }
        });
    }
}