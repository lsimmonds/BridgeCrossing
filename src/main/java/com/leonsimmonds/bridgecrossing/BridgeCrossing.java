/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonsimmonds.bridgecrossing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 *
 * @author lsimmonds
 */
public class BridgeCrossing {

    private final List<Traveler> travelers = new ArrayList<>();
    private final List<TripStep> tripLog = new ArrayList<>();
    private int totalTime = 0;
    private String name;

    public BridgeCrossing(String name) {
        travelers.add(new Traveler(10));
        travelers.add(new Traveler(5));
        travelers.add(new Traveler(2));
        travelers.add(new Traveler(1));
        this.name = name;
    }

    public void goOver(Traveler person1, Traveler person2) {
        if (person1.isOver() || person2.isOver()) {
            System.out.println("Persons must both not be over to goOver");
            return;
        }
        person1.setIsOver(true);
        person2.setIsOver(true);
        TripStep goStep = new TripStep();
        goStep.addGoers(person1, person2);
        goStep.setTime(person1.getTravelTime() > person2.getTravelTime() ? person1.getTravelTime() : person2.getTravelTime());
        tripLog.add(goStep);
        totalTime += goStep.getTime();
    }

    public void goBack(Traveler person) {
        if (!person.isOver()) {
            System.out.println("Person must be over to goBack");
        }
        person.setIsOver(false);
        TripStep goStep = new TripStep();
        goStep.addBacker(person);
        goStep.setTime(person.getTravelTime());
        tripLog.add(goStep);
        totalTime += goStep.getTime();
    }

    /**
     * Try out assumed best algorithm: Cross pair with lowest possible sum of travel times first Cross one of them back Cross pair with highest possible travel
     * times Cross lowest travel time back (will be the remainder of the first pair) Cross the remaining pair
     *
     * @return sum of travel times
     */
    public int makeTestingTrip() {
        travelers.sort(new Comparator<Traveler>() {
            @Override
            public int compare(Traveler person1, Traveler person2) {
                return person1.getTravelTime() - person2.getTravelTime();
            }
        });
        goOver(travelers.get(0), travelers.get(1));
        goBack(travelers.get(0));
        goOver(travelers.get(2), travelers.get(3));
        goBack(travelers.get(1));
        goOver(travelers.get(0), travelers.get(1));
        return totalTime;
    }

    public List<Integer> getOvers() {
        List<Integer> overs = IntStream.range(0, travelers.size()).filter(i -> travelers.get(i).isOver()).
            collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        return overs;
    }

    public List<Integer> getNotOvers() {
        List<Integer> notOvers = IntStream.range(0, travelers.size()).filter(i -> !travelers.get(i).isOver()).
            collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        return notOvers;
    }

    public BridgeCrossing copy() {
        BridgeCrossing tempCrossing = new BridgeCrossing("Copy of " + this.name);
        IntStream.range(0, this.travelers.size()).forEach(i -> {
            tempCrossing.travelers.get(i).setTravelTime(this.travelers.get(i).getTravelTime());
            tempCrossing.travelers.get(i).setIsOver(this.travelers.get(i).isOver());
        });
        IntStream.range(0, this.tripLog.size()).forEach(i -> {
            tempCrossing.tripLog.add(this.tripLog.get(i));
        });
        tempCrossing.totalTime = this.totalTime;
        return tempCrossing;
    }

    public static int makeAllTrips(BridgeCrossing perm1, int step, List<BridgeCrossing> allRuns) {
        switch (step) {
            case 0:
                //NO,NO,NO,NO
                perm1.goOver(perm1.travelers.get(0), perm1.travelers.get(1));
                makeAllTrips(perm1, 1, allRuns);

                BridgeCrossing perm2 = new BridgeCrossing("Perm2");
                perm2.goOver(perm2.travelers.get(0), perm2.travelers.get(2));
                makeAllTrips(perm2, 1, allRuns);

                BridgeCrossing perm3 = new BridgeCrossing("Perm3");
                perm3.goOver(perm3.travelers.get(0), perm3.travelers.get(3));
                makeAllTrips(perm3, 1, allRuns);

                BridgeCrossing perm4 = new BridgeCrossing("Perm4");
                perm4.goOver(perm4.travelers.get(1), perm4.travelers.get(2));
                makeAllTrips(perm4, 1, allRuns);

                BridgeCrossing perm5 = new BridgeCrossing("Perm5");
                perm5.goOver(perm5.travelers.get(1), perm5.travelers.get(3));
                makeAllTrips(perm5, 1, allRuns);

                BridgeCrossing perm6 = new BridgeCrossing("Perm6");
                perm6.goOver(perm6.travelers.get(2), perm6.travelers.get(3));
                makeAllTrips(perm6, 1, allRuns);
                break;
            case 1:
                //NO,NO,O,O
                List<Integer> stepOneOvers = perm1.getOvers();
                BridgeCrossing perm7 = perm1.copy();
                perm7.name = "Perm7 from " + perm1.name;
                perm1.goBack(perm1.travelers.get(stepOneOvers.get(0)));
                perm7.goBack(perm7.travelers.get(stepOneOvers.get(1)));
                makeAllTrips(perm1, 2, allRuns);
                makeAllTrips(perm7, 2, allRuns);
                break;
            case 2:
                //NO,NO,NO,O
                List<Integer> stepTwoNotOvers = perm1.getNotOvers();
                BridgeCrossing perm8 = perm1.copy();
                perm8.name = "Perm8 from " + perm1.name;
                BridgeCrossing perm9 = perm1.copy();
                perm9.name = "Perm9 from " + perm1.name;
                perm1.goOver(perm1.travelers.get(stepTwoNotOvers.get(0)), perm1.travelers.get(stepTwoNotOvers.get(1)));
                perm8.goOver(perm8.travelers.get(stepTwoNotOvers.get(0)), perm8.travelers.get(stepTwoNotOvers.get(2)));
                perm9.goOver(perm9.travelers.get(stepTwoNotOvers.get(1)), perm9.travelers.get(stepTwoNotOvers.get(2)));
                makeAllTrips(perm1, 3, allRuns);
                makeAllTrips(perm8, 3, allRuns);
                makeAllTrips(perm9, 3, allRuns);
                break;
            case 3:
                //NO,O,O,O
                List<Integer> stepThreeOvers = perm1.getOvers();
                BridgeCrossing perm10 = perm1.copy();
                perm10.name = "Perm10 from " + perm1.name;
                BridgeCrossing perm11 = perm1.copy();
                perm11.name = "Perm11 from " + perm1.name;
                perm1.goBack(perm1.travelers.get(stepThreeOvers.get(0)));
                perm10.goBack(perm10.travelers.get(stepThreeOvers.get(1)));
                perm11.goBack(perm11.travelers.get(stepThreeOvers.get(2)));
                makeAllTrips(perm1, 4, allRuns);
                makeAllTrips(perm10, 4, allRuns);
                makeAllTrips(perm11, 4, allRuns);
                break;
            case 4:
                //NO,NO,O,O
                List<Integer> stepFourNotOvers = perm1.getNotOvers();
                perm1.goOver(perm1.travelers.get(stepFourNotOvers.get(0)), perm1.travelers.get(stepFourNotOvers.get(1)));
                makeAllTrips(perm1, 5, allRuns);
                break;
            case 5:
                //O,O,O,O Done!
                allRuns.add(perm1);
                break;
            default:
                throw new AssertionError();
        }
        return perm1.totalTime;
    }

    public static void main(String[] args) {
        BridgeCrossing test1 = new BridgeCrossing("test1");
        System.out.println("Testing Trip time: " + test1.makeTestingTrip());
        System.out.println("Tip Log: " + test1.tripLog.toString());

        BridgeCrossing test2 = new BridgeCrossing("Perm1");
        List<BridgeCrossing> allRuns = new ArrayList<>();
        makeAllTrips(test2, 0, allRuns);
        AtomicInteger minTime = new AtomicInteger((test2.travelers.get(0).getTravelTime() + test2.travelers.get(1).getTravelTime() + test2.travelers.get(2).getTravelTime()
            + test2.travelers.get(3).getTravelTime()) * 2);
        allRuns.forEach(crossingRun -> {
            System.out.println("Run " + crossingRun.name + " travelers: " + crossingRun.travelers);
            IntStream.range(0, crossingRun.tripLog.size()).forEach(i -> {
                System.out.println("    Step " + i + ": " + crossingRun.tripLog.get(i));
            });
            System.out.println("Run " + crossingRun.name + " totalTime: " + crossingRun.totalTime);
            if (crossingRun.totalTime < minTime.get()) {
                minTime.getAndSet(crossingRun.totalTime);
            }
        });
        if (minTime.get() < test1.totalTime) {
            System.out.println("Test algorithm failed!!!! Does not produce fastest time! (" + minTime.get() + " vs. " + test1.totalTime + ")");
        } else {
            System.out.println("Test algorithm produced the best time! " + test1.totalTime);
        }
    }

    class Traveler {

        private int travelTime;
        private boolean isOver = false;

        public Traveler(int travelTime) {
            this.travelTime = travelTime;
        }

        public int getTravelTime() {
            return travelTime;
        }

        public void setTravelTime(int travelTime) {
            this.travelTime = travelTime;
        }

        public boolean isOver() {
            return isOver;
        }

        public void setIsOver(boolean isOver) {
            this.isOver = isOver;
        }

        @Override
        public String toString() {
            return "Traveler{" + "travelTime=" + travelTime + ", isOver=" + isOver + '}';
        }
    }

    class TripStep {

        private Set<Traveler> participants;
        private int time;
        private String type;

        public TripStep() {
            this.participants = new HashSet<>();
            this.time = 0;
        }

        public Set<Traveler> getGoers() {
            return participants;
        }

        public Set<Traveler> getBackers() {
            return getGoers();
        }

        public void addGoers(Traveler person1, Traveler person2) {
            this.participants.clear();
            this.participants.add(person1);
            this.participants.add(person2);
            this.type = "Over";
        }

        public void addBacker(Traveler person) {
            this.participants.clear();
            this.participants.add(person);
            this.type = "Back";
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "TripStep{type=" + type + ", participants=" + participants + ", time=" + time + '}';
        }
    }
}
