package master;

/* TimerThread.java
- Copyright (c) 2014, HerongYang.com, All Rights Reserved.
*/

class TimerThread2 extends Thread {
	
  private final long TIMER_STARTED; // the time this timer started (in milliseconds)
  private final long LENGTH_TIMER;
  private final static int CLOCK_INTERVAL = 100; // in milliseconds < 1000
  
  /**
   * Constructor of the time thread
   * @param lengthTimer
   * 		Number of seconds this timer needs to run
   */
  public TimerThread2(int lengthTimer) {
	  this.LENGTH_TIMER = lengthTimer;
	  this.TIMER_STARTED = System.currentTimeMillis();
  }
  
  /**
   * This methods runs the timer
   */
  public void run() {
     while (!isInterrupted()) {
        try {
           sleep(CLOCK_INTERVAL);
        } catch (InterruptedException e) {
           break; // the main thread wants this thread to end
        }
        
        // calculates how much time is left on the timer
        long timeLeft = LENGTH_TIMER - (System.currentTimeMillis() - TIMER_STARTED);
        if(timeLeft <= 0) {
        	// if the timeLeft < 0 leave the while loop and thus return
        	break;
        }  
     }
  }
}