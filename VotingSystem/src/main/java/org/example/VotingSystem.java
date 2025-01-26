package com.example.votingsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VotingSystem {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Voting System");
        JButton adminButton = new JButton("Admin");
        JButton voterButton = new JButton("Voter");

        adminButton.setBounds(100, 100, 200, 40);
        voterButton.setBounds(100, 200, 200, 40);

        frame.add(adminButton);
        frame.add(voterButton);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminInterface();
            }
        });

        voterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VoterInterface();
            }
        });
    }
}