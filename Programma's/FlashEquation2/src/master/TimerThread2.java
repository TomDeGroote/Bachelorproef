package master;


/* TimerThread.java
- Copyright (c) 2014, HerongYang.com, All Rights Reserved.
*/

public class TimerThread2 extends Thread {
	
  private final long TIMER_STARTED; // the time this timer started (in milliseconds)
  private final long LENGTH_TIMER; // in milliseconds
  private final static int CLOCK_INTERVAL = 100; // in milliseconds < 1000
  
  /**
   * Constructor of the time thread
   * @param lengthTimer
   * 		Number of milliseconds this timer needs to run
   */
  public TimerThread2(int lengthTimer) {
	  this.LENGTH_TIMER = lengthTimer;
	  this.TIMER_STARTED = System.currentTimeMillis();
  }
  
  /**
   * This methods runs the timer
   */
  public void run() {
	 long timeLeft = LENGTH_TIMER;
     while (timeLeft >= 0) {      
    	 try {
			Thread.sleep(CLOCK_INTERVAL);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
         // calculates how much time is left on the timer
         timeLeft = LENGTH_TIMER - (System.currentTimeMillis() - TIMER_STARTED); 
     }
     throw new NullPointerException(); // TODO vervang door eigen exception
  }
}