# BridgeCrossing
I was given a simple puzzle in a job interview: 
You have four people on one side of a bridge, who need to cross it.
* Each person has a unique crossing time.
* You must cross over in pairs.
* After the intial crossing someone must cross back before the next pair crosses.
* Each crossing will take the time of the largest crossing time among the people crossing.

The goal is to find the most effiecit time it takes to cross. For the set up I was given (times of 10,5,2 and 1) the solution was:
* cross the shortest pair (1 & 2 - total now 2).
* cross the shortest time back (1 - total now 3).
* now cross the longest pair (10 & 5 - total now 13)
* cross the reminder of the intial short pair back (3 - total now 15)
* cross the everyone left over (1 & 2 - final total 17)

The same inteview also wanted a quick code sample. I am not great at inventing thingd to code,
I generally need to be given a task, but drving home I was wondering if the algorithm I used was
universal: would it work for any set of time values?

So thus my code sample was born. It's a simple GUI to run any set of 4 values. The code will run the values by the algorithm
described above, and then run all other permutations and compare. It lets one plug in any 4 values and compare the results.
(So is the algorithm always best? Nope! There is a specific type of case, where the 2 middle values are too close too each other
that it does not produce the best time, how close gets larger as the middle numbers get larger. It seems to work in all other cases though)

To run:
* Checkout project in some dir
* cd to that dir
* run mvn clean install <- you will need mvn installed, of course
* run java -jar <whatever jar mvn built for you>



