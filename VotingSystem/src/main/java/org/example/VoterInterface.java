package com.example.votingsystem;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VoterInterface extends JFrame {
    public VoterInterface() {
        setTitle("Voting System - Voter Interface");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton voteButton = new JButton("Vote");
        JButton resultButton = new JButton("Result");

        voteButton.setBounds(50, 50, 100, 30);
        resultButton.setBounds(50, 100, 100, 30);

        // Action listener for Vote button
        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginSignupInterface(); // Opens login/signup interface
            }
        });

        resultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ResultsInterface();
            }
        });

        // Add buttons to layout
        add(voteButton);
        add(resultButton);

        setVisible(true);
    }
}
