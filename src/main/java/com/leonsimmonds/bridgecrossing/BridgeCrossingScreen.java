/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonsimmonds.bridgecrossing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author lsimmonds
 */
public class BridgeCrossingScreen extends JFrame {

    public BridgeCrossingScreen() throws HeadlessException {
        int textWidth = 100;
        int textHeight = 30;
        Dimension textBoxSize = new Dimension(textWidth, textHeight);
        int rowOne = 26;
        int box1 = 100;
        
        setTitle("Bridge Crossing Parameters");
        setSize(600, 500); // default size is 0,0
        setLocation(30, 200); // default is 0,0 (top left corner)
        JLabel[] personLbls = {new JLabel("Person 1"), new JLabel("Person 2"), new JLabel("Person 3"), new JLabel("Person 4")};
        JTextField[] personData = {new JTextField("10"), new JTextField("1"), new JTextField("2"), new JTextField("5")};

        IntStream.range(0, personData.length).forEach(i -> {
            personLbls[i].setSize(textBoxSize);
            personLbls[i].setPreferredSize(textBoxSize);
            personLbls[i].setLocation(20+(box1*i), rowOne);
            personLbls[i].setBorder(BorderFactory.createLineBorder(Color.black));
            this.add(personLbls[i]);
            System.out.println("Set up personLbls["+i+"]: "+personLbls[i].getText()+", location: "+personLbls[i].getLocation());
        });

    }

    public static void main(String[] args) {
        JFrame bridgeCrossingwindow = new BridgeCrossingScreen();
        bridgeCrossingwindow.setVisible(true);

        BridgeCrossing test1 = new BridgeCrossing("test1");
        System.out.println("Testing Trip time: " + test1.makeTestingTrip());
        System.out.println("Tip Log: " + test1.getTripLog().toString());

        BridgeCrossing test2 = new BridgeCrossing("Perm1");
        List<BridgeCrossing> allRuns = new ArrayList<>();
        BridgeCrossing.makeAllTrips(test2, 0, allRuns);
        AtomicInteger minTime = new AtomicInteger((test2.getTravelers().get(0).getTravelTime() + test2.getTravelers().get(1).getTravelTime()
            + test2.getTravelers().get(2).getTravelTime() + test2.getTravelers().get(3).getTravelTime()) * 2);
        allRuns.forEach(crossingRun -> {
            System.out.println("Run " + crossingRun.getName() + " travelers: " + crossingRun.getTravelers());
            IntStream.range(0, crossingRun.getTripLog().size()).forEach(i -> {
                System.out.println("    Step " + i + ": " + crossingRun.getTripLog().get(i));
            });
            System.out.println("Run " + crossingRun.getName() + " totalTime: " + crossingRun.getTotalTime());
            if (crossingRun.getTotalTime() < minTime.get()) {
                minTime.getAndSet(crossingRun.getTotalTime());
            }
        });
        if (minTime.get() < test1.getTotalTime()) {
            System.out.println("Test algorithm failed!!!! Does not produce fastest time! (" + minTime.get() + " vs. " + test1.getTotalTime() + ")");
        } else {
            System.out.println("Test algorithm produced the best time! " + test1.getTotalTime());
        }

    }
}
