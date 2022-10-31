package task6;
import java.lang.Thread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 * 
 * Student: 40195538 Fung Sim LEUNG
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;
	
	/* Task 1 */	
	public static int total_num_phil = 0;	

	/* Task 1 */
	public Philosopher() {
		total_num_phil++;		// We need to know the number of philosophers.
	}
	

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	
	/* Task 1 */
	public void eat()
	{
			// ...
			
			System.out.println("Philosopher " + getTID() + " has started eating.");		// Task 1
			Thread.yield();		// Task 1
			sleep();
			Thread.yield();		// Task 1
			System.out.println("Philosopher " + getTID() + " has done eating.");		// Task 1
			
			// ...
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	
	/* Task 1 */
	public void think()
	{
			System.out.println("Philosopher " + getTID() + " has started thinking.");		// Task 1
			Thread.yield();		// Task 1
			sleep();		// Task 1
			Thread.yield();		// Task 1
			System.out.println("Philosopher " + getTID() + " has done thinking.");		// Task 1
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	
	 public void talk()
	{
			// ...
		 	
			Thread.yield();		// Task 1
			System.out.println("Philosopher " + getTID() + " has started talking.");		// Task 1
			saySomething();		// Task 1
			Thread.yield();		// Task 1
			System.out.println("Philosopher " + getTID() + " has done talking.");		// Task 1
			
			// ...
	}
	
	/**
	 * Task 1 and 2
	 * The act of sleeping.
	 * - Print the fact that a given phil (their TID) has started sleeping.
	 * - yield
	 * - Sleep for a random duration of time
	 * - yield
	 * - The print that they are done sleeping.
	 */
		
	public void sleep () {
		try {
			DiningPhilosophers.soMonitor.FallAsleep(getTID());		// Task 2
			Thread.yield();
			System.out.println("Philosopher " + getTID() + " has started sleeping.");		// Task 1
			sleep((long)(Math.random() * TIME_TO_WASTE));
			Thread.yield();
			System.out.println("Philosopher " + getTID() + " has done sleeping.");		// Task 1
			DiningPhilosophers.soMonitor.WakeUp(getTID());		// Task 2
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.sleep():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}
	
	
	

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	
	/* Task 1 */
	public void run()
	{
		Thread.yield();
		
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			
			DiningPhilosophers.soMonitor.pickUp(getTID());		// Task 2
			
			eat();
			
			DiningPhilosophers.soMonitor.putDown(getTID());		// Task 2
						
			think();

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			int sayornot;		// Task 1
			
			sayornot = (int) Math.random()*30;		// Task 1
			
			// There are 33% chance that a philosopher says something.
			if(sayornot % 3 == 0)		// Task 1
			{
				// Some monitor ops down here...
				DiningPhilosophers.soMonitor.requestTalk(getTID());		// Task 2
				
				talk();
				
				DiningPhilosophers.soMonitor.endTalk(getTID());		// Task 2
				// ...
			}

			
			Thread.yield();
		}
	} // run()
	

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
