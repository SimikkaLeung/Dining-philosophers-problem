package task6;


/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 * 
 * Student: 40195538 Fung Sim LEUNG
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	
	
	public static int chopsticks;		// Task 2
	
	enum aState {THINKING, HUNGRY, EATING, TALKING} ;		// Task 2
	enum sleepState {SLEEPING, AWAKE};		// Task 2
	
	public static aState[] state;		// Task 2
	public static sleepState[] sleepingState; 		// Task 2
	
	public static int[] priority;		// Task 3: The array holds the priority of all philosophers.	
	
	enum pepper {USING, NOT_USING};		// Task 6		
	public static pepper[] pepperState;		// Task 6: Record if each philosopher is using a pepper shaker.
	public static int pepper_available = 2;		//	Task 6: Initially there are two pepper shakers available.
	
	//

	
	 /* Constructor */
	
	/* Task 2 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		
		chopsticks = piNumberOfPhilosophers;			// Task 2
		state = new aState[chopsticks];			// Task 2
		sleepingState = new sleepState[chopsticks];			// Task 2
		priority = new int[chopsticks];			// Task 3
		pepperState = new pepper[chopsticks];		// Task 6
		initialized_code();
		
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */
	
	/* Task 2 and 3 */
	/* Assign an initial state to all philosopher. */
	public synchronized void initialized_code() {
		for (int i = 0; i < chopsticks; i++) {
			state[i] = aState.THINKING;			// Task 2
			sleepingState[i] = sleepState.AWAKE;			// Task 2
			priority[i] = i+1;			// Task 3
			pepperState[i] = pepper.NOT_USING;			// Task 6
		}
	}
	
	
	/* Task 3 
	 * The method prints the priority of all philosophers.
	 * */
	public synchronized void print_priority() {
		for (int i = 0; i < chopsticks; i++) {
			System.out.print("Phil: " + (i+1) + " Priority: " + priority[i] + "    ");
		}
		System.out.println();
	}
	
	/* Task 3 
	 * The method increase the priority (decrease the value in the array) of philosophers if their priorities 
	 * are lower than the philosopher who is going to eat.
	 * */
	public synchronized void update_priority(int hungriest_phil) {
		int priority_start = priority[hungriest_phil-1];	// We will update the priority if it is higher that this reference point.

		for (int j = 0; j < chopsticks; j++) {
			if (j == hungriest_phil - 1) {
				priority [j] = chopsticks;
			} else if (priority[j] > priority_start) {
				priority [j] = priority [j]-1;
			}
		}
	}
	
	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait().
	 * The assignment does not specify under what condition a philosopher should wait. 
	 * So in my program, if two philosophers are hungry, the one with the highest priority 
	 * will eat first. Even if that philosopher does not have chopsticks yet, 
	 * the other philosopher still needs to wait. 
	 */
	/* Task 2, 3 and 6*/
	public synchronized void pickUp(final int piTID)
	{
		// ...
		try {
			while (true) {
				int hungriest_phil = piTID;		// Task 2: Assume the hungriest philosopher is the current on for now.
				
				for (int d = 0; d < 10; d++) {		// Just to delay the thread a bit for better readability.
					Thread.sleep((long)(1));
					}
				
				state[piTID-1] = aState.HUNGRY;
				int current_priority = priority[piTID-1];
				for (int i = 0; i < chopsticks; i++) {
					if ( state[i]==aState.HUNGRY && priority[i] < current_priority) {
						hungriest_phil = i+1;
						current_priority = priority[i];
					}
				}
				
				
				/* Check if the adjacent philosophers are eating.	
				 * If the chopsticks are available, the current philosopher can eat if they have the highest priority. */
				
				
				// Task 2
				boolean chopsticks_available = false;
				if ( (state[(piTID-1 + (chopsticks-1) ) % chopsticks] != aState.EATING) && 
						(state[(piTID-1+1) % chopsticks] != aState.EATING)) {
					chopsticks_available = true;
				}
				
				// Task 3				
				if (hungriest_phil != piTID) {
					System.out.println("Philosopher " + piTID + " is hungry but they let philosopher " + hungriest_phil + " eat first.");
				} else if (chopsticks_available) {
					System.out.print("Philosopher " + piTID + " is hungry and it's their turn to eat.");
					if (pepper_available >= 1) {			// Task 6
						pepper_available--;					// Task 6
						pepperState[piTID-1] = pepper.USING;			// Task 6
						System.out.println(" They can also use a pepper shaker.");			// Task 6
					} else {
						System.out.println(" No pepper shaker is available.");			// Task 6
					}
					state[piTID-1] = aState.EATING;
				} else if (!chopsticks_available) {
					System.out.println("Philosopher " + piTID + " is hungry but their chopsticks are not available.");
				}
							
				
				
				// Task 2
				if (state[piTID-1] != aState.EATING) {
					Thread.sleep((long)(1));
					wait();					
				} else {
					System.out.println("The priority list before philosopher " + piTID + " starts eating:");
					print_priority();
					update_priority(hungriest_phil);
					System.out.println("The priority list after philosopher " + piTID + " starts eating:");
					print_priority();
					break;
				}
			}


			
		}	catch(InterruptedException e)
		{
			System.err.println("Monitor.pickUP():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	
	}
	
	
	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	/* Task 2 and 6 */
	public synchronized void putDown(final int piTID)
	{
		// ...
		state[piTID-1] = aState.THINKING;			// Task 2
		
		/* Task 6
		 * Update the quantity of available pepper shakers. */
		if (pepperState[piTID-1] == pepper.USING) {
			pepper_available++;
			pepperState[piTID-1] = pepper.NOT_USING;
			System.out.println("Philosopher " + piTID + " has put down a pepper shaker on the table.");
		}
		notifyAll();
	}

	/**
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	/* Task 2 */
	public synchronized void requestTalk(final int piTID)
	{
			 
		// ...
		try {
			while(true) {
				
				/* The philosopher checks if anyone is sleeping or talking. */
				boolean nobody_talking_or_sleeping = true;
				for (int i = 0; i < chopsticks; i++) {
					if (state[i] == aState.TALKING || sleepingState[i] == sleepState.SLEEPING) {
						nobody_talking_or_sleeping = false;
					}
				}
				
				/* If no one else is sleeping or talking, the current philosopher can talk. */
				if ( nobody_talking_or_sleeping ) {
						state[piTID-1] = aState.TALKING;
				}
				
				
				if (state[piTID-1] != aState.TALKING) {
					wait();
				} else {
					break;
				}				
			}
			
		}	catch(InterruptedException e)
		{
			System.err.println("Monitor.requestTalk():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}		
			
	}
	
	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	/* Task 2 */
	public synchronized void endTalk(final int piTID)
	{
		// ...
		state[piTID-1] = aState.THINKING;
		notifyAll();
	}
	
	
	/**
	 * No one can enter the sleeping state if someone is talking.
	 */
	/* Task 2 */
	public synchronized void FallAsleep(final int piTID)
	{
			 
		// ...
		try {
			while (true) {
				
				/* If someone is talking, the current philospher needs to delay their sleep. */				
				boolean nobody_talking = true;
				for (int i = 0; i < chopsticks; i++) {
					if (state[i] == aState.TALKING) {
						nobody_talking = false;
					}
				}
				if ( nobody_talking ) {
					sleepingState[piTID-1] = sleepState.SLEEPING;					
				}
				
				if (sleepingState[piTID-1] != sleepState.SLEEPING) {
					wait();
				} else {
					break;
				}
			}	
		}	catch(InterruptedException e)
		{
			System.err.println("Monitor.requestTalk():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}		
		
	}
	
	/**
	 * When a philosopher is wake up, notify other philosophers.
	 */
	/* Task 2 */
	public synchronized void WakeUp(final int piTID)
	{
		// ...
		sleepingState[piTID-1] = sleepState.AWAKE;
		notifyAll();
	}
}

// EOF
