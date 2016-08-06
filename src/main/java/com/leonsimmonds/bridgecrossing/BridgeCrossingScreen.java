/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonsimmonds.bridgecrossing;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author lsimmonds
 */
public class BridgeCrossingScreen extends JFrame {

	private JTextField[] personData = { new JTextField("10"), new JTextField("1"), new JTextField("2"), new JTextField("5") };
	JLabel results = new JLabel("", JLabel.CENTER);
	JButton detailsBtn = new JButton("Show details");
	StringBuilder testRunDetails = new StringBuilder();

	public BridgeCrossingScreen() throws HeadlessException {
		int textWidth = 100;
		int textHeight = 30;
		Dimension textBoxSize = new Dimension(textWidth, textHeight);
		Dimension buttonSize = new Dimension(260, 30);
		int rowOne = 26;
		int box1 = 100;

		setTitle("Bridge Crossing Parameters");
		setSize(600, 300);
		setLocation(30, 60);
		JLabel[] personLbls = { new JLabel("Person 1", JLabel.CENTER), new JLabel("Person 2", JLabel.CENTER), new JLabel("Person 3", JLabel.CENTER),
				new JLabel("Person 4", JLabel.CENTER) };

		IntStream.range(0, personData.length).forEach(i -> {
			personLbls[i].setSize(textBoxSize);
			personLbls[i].setPreferredSize(textBoxSize);
			personLbls[i].setLocation(20 + (box1 * i), rowOne);
			this.add(personLbls[i]);
			personData[i].setSize(textBoxSize);
			personData[i].setPreferredSize(textBoxSize);
			personData[i].setLocation(20 + (box1 * i), rowOne + 50);
			this.add(personData[i]);
		});
		JButton startBtn = new JButton("Run the Bridge Crossing test");
		startBtn.setSize(buttonSize);
		startBtn.setPreferredSize(buttonSize);
		startBtn.setLocation(20, rowOne + 80);
		startBtn.addActionListener(buttonActionListener);
		detailsBtn.setSize(buttonSize);
		detailsBtn.setPreferredSize(buttonSize);
		detailsBtn.setLocation((int) (20 + buttonSize.getWidth()), rowOne + 80);
		detailsBtn.addActionListener(buttonActionListener);
		detailsBtn.setVisible(false);
		results.setLocation(10, 100);
		this.add(startBtn);
		this.add(detailsBtn);
		this.add(results);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	ActionListener buttonActionListener = (ActionEvent e) -> {
		if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
			if (e.getActionCommand() == "Run the Bridge Crossing test") {
				testRunDetails.setLength(0); // Clear out details text
				BridgeCrossing test1 = new BridgeCrossing("test1", personData);
				testRunDetails.append("Control Run " + test1.getName() + " travelers: " + test1.getTravelers()+"\n");
				test1.makeTestingTrip();
				IntStream.range(0, test1.getTripLog().size()).forEach(i -> {
					testRunDetails.append("    Step " + i + ": " + test1.getTripLog().get(i)+"\n");
				});
				testRunDetails.append("Control Run " + test1.getName() + " totalTime: " + test1.getTotalTime()+"\n");

				testRunDetails.append("===========\n");

				BridgeCrossing test2 = new BridgeCrossing("Perm1", personData);
				List<BridgeCrossing> allRuns = new ArrayList<>();
				BridgeCrossing.makeAllTrips(test2, 0, allRuns);
				AtomicInteger minTime = new AtomicInteger((test2.getTravelers().get(0).getTravelTime() + test2.getTravelers().get(1).getTravelTime()
						+ test2.getTravelers().get(2).getTravelTime() + test2.getTravelers().get(3).getTravelTime()) * 2);
				allRuns.forEach(crossingRun -> {
					testRunDetails.append("Run " + crossingRun.getName() + " travelers: " + crossingRun.getTravelers()+"\n");
					IntStream.range(0, crossingRun.getTripLog().size()).forEach(i -> {
						testRunDetails.append("    Step " + i + ": " + crossingRun.getTripLog().get(i)+"\n");
					});
					testRunDetails.append("Run " + crossingRun.getName() + " totalTime: " + crossingRun.getTotalTime()+"\n");
					if (crossingRun.getTotalTime() < minTime.get()) {
						minTime.getAndSet(crossingRun.getTotalTime());
					}
				});
				detailsBtn.setVisible(true);
				if (minTime.get() < test1.getTotalTime()) {
					results.setText("\nTest algorithm failed!!!! Does not produce fastest time! (" + test1.getTotalTime() + " vs. " + minTime.get() + ")");
				} else {
					results.setText("\nTest algorithm produced the best time! " + test1.getTotalTime());
				}
			} else if (e.getActionCommand() == "Show details") {
				JFrame detailWindow = new JFrame();
				detailWindow.setTitle("Bridge Crossing Test Run Details");
				detailWindow.setSize(1000, 600);
				detailWindow.setLocation(120, 100);
				JTextArea textArea = new JTextArea();
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				JScrollPane jScrollPane1 = new JScrollPane(textArea);
				jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				jScrollPane1.setPreferredSize(new Dimension(250, 250));
				textArea.setText(testRunDetails.toString());
				detailWindow.add(jScrollPane1);
				detailWindow.setVisible(true);
			}
		}
	};

	public static void main(String[] args) {
		JFrame bridgeCrossingwindow = new BridgeCrossingScreen();
		bridgeCrossingwindow.setVisible(true);

		BridgeCrossing test1 = new BridgeCrossing("test1");
		System.out.println("Testing Trip time: " + test1.makeTestingTrip());
		System.out.println("Tip Log: " + test1.getTripLog().toString());

		BridgeCrossing test2 = new BridgeCrossing("Perm1");
		List<BridgeCrossing> allRuns = new ArrayList<>();
		BridgeCrossing.makeAllTrips(test2, 0, allRuns);
		StringBuilder testRunDetails = new StringBuilder();
		AtomicInteger minTime = new AtomicInteger((test2.getTravelers().get(0).getTravelTime() + test2.getTravelers().get(1).getTravelTime()
				+ test2.getTravelers().get(2).getTravelTime() + test2.getTravelers().get(3).getTravelTime()) * 2);
		allRuns.forEach(crossingRun -> {
			testRunDetails.append("Run " + crossingRun.getName() + " travelers: " + crossingRun.getTravelers());
			IntStream.range(0, crossingRun.getTripLog().size()).forEach(i -> {
				testRunDetails.append("    Step " + i + ": " + crossingRun.getTripLog().get(i));
			});
			testRunDetails.append("Run " + crossingRun.getName() + " totalTime: " + crossingRun.getTotalTime());
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
