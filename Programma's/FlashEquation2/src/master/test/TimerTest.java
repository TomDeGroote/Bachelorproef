package master.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import master.Timer;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {
	
	private ArrayList<Integer> list = new ArrayList<Integer>();
	private static Timer timer;
	private int TIME_ON_CLOCK = 1000; // in milliseconds

	@Before
	public void initialize() {
		timer = new Timer(TIME_ON_CLOCK);
	}
	
	@Test
	public void startTimerTest() {
		int faultMargin = TIME_ON_CLOCK/50; // fault margin in milliseconds
		WhileTrue whileTrue = new WhileTrue(); // generate evaluateSimulator
		long timeStarted = System.currentTimeMillis(); 
		timer.start();
		whileTrue.run();
		
		// calculate the fault made by the system to stop
		long fault = (System.currentTimeMillis() - timeStarted) - TIME_ON_CLOCK;
		
		// check if the fault is reasonable, let the test fail if this is not the case or if no
		// task has been executed
		if(fault < 0 || fault > faultMargin) {
			fail("Timer did not end within the fault margin, fault: " + fault);
		} else if(list.isEmpty()) {
			fail("List does not have any elements");
		}
		System.out.println("Timertest => fault: " + fault + ""
				       + "\n          => Size created: " + list.size());
	}
	
	
	
	/**
	 * Method used to check if there is still time on the clock
	 * 
	 * Mainly implemented to show how Main class should work
	 * @return
	 * 		True if the there is no more time left
	 * 		False if there is time left
	 */
	public static boolean timesUp() {
		return timer.timesUp();
	}	
	
	/**
	 * Class to simulate how Evaluate should be using timer
	 * Also used to see if there can actually be worked while the timer is running
	 */
	class WhileTrue {
		
		/**
		 * The run method of whileTrue
		 * 
		 * This method will add numbers to a list while the timer from TimerTest is not expired
		 */
		public void run() {
			int i = 0;
			while(!TimerTest.timesUp()) {
				list.add(i++);
			}
		}
	}

}
