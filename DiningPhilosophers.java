package task6;
import java.util.Scanner;		// Task 4
import java.lang.Integer;		// Task 4
/**
 * Class DiningPhilosophers
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 * 
 * Student: 40195538 Fung Sim LEUNG
 */
public class DiningPhilosophers
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		try
		{
			/*
			 * TODO:
			 * Should be settable from the command line
			 * or the default if no arguments supplied.
			 */
			int iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;		
			
			
			/* Task 4
			 * Ask the user for the number of philosophers for this execution and use the default number if no input
			 * */
			Scanner keyboard = new Scanner(System.in);
			boolean isValid = true;
			String response = "";		// A string accepting the input from the user.
			do {
				try {
				 	System.out.println("How many philosophers do you want to create? Please enter a positive integer.");
					response = keyboard.nextLine();
					
					// Check if the input is valid.
				    if (response == null || response.isEmpty()) {
				    	iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
				    	isValid = true;
				    	System.out.println();
				    } else {
				    	iPhilosophers = Integer.parseInt(response);
				    	if (iPhilosophers > 0) {
				    		isValid = true;
				    	} else {
				    		isValid = false;
					    	// Print an error message.
					    	System.out.println("% java DiningPhilosophers " + response);
					    	System.out.println("\"" + response + "\" is not a positive decimal integer");
					    	System.out.println("Usage: java DiningPhilosophers [NUMBER_OF_PHILOSOPHERS]");
					    	System.out.println("%");
				    	}				    	
				    	System.out.println();
				    }			    	
			    } catch (NumberFormatException nfe) {
			    	isValid = false;
			    	// Print an error message.
			    	System.out.println("% java DiningPhilosophers " + response);
			    	System.out.println("\"" + response + "\" is not a positive decimal integer");
			    	System.out.println("Usage: java DiningPhilosophers [NUMBER_OF_PHILOSOPHERS]");
			    	System.out.println("%");

			    }
			} while (!isValid);		// Ask the user to input again if the response is not valid.
			
			keyboard.close();
			
			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

						
			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers];

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
