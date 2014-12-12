package master;

/**
 * This class represents a Timer
 * 
 * The goal is to ask this class after every action if there is still time to do the next action
 * 
 * @author tom
 *
 */
public class Timer {
	
  private long timerStarted; // the time this timer started (in milliseconds)
  private final long LENGTH_TIMER; // in milliseconds
  private long timeLeftPrevious = 0;
  /**
   * Constructor of the timer
   * @param lengthTimer
   * 		Number of milliseconds this timer needs to run
   */
  public Timer(int lengthTimer) {
	  this.LENGTH_TIMER = lengthTimer;
  }
  
  /**
   * Starts the timer
   */
  public void start() {
	  this.timerStarted = System.currentTimeMillis();
  }
  
  /**
   * @return 
   * 		True if the timer is finished
   * 		False if the timer is still running
   */
  public boolean timesUp()  {
     // calculates how much time is left on the timer
	 long currentTime = System.currentTimeMillis();
     long timeLeft = LENGTH_TIMER - (currentTime - timerStarted); 
	 long differencePrevious = (timeLeftPrevious - timeLeft);
     
     // smart part
     // timeNeededEstimate = differencePrevious (instead of 0)  
     if (timeLeft > differencePrevious) {  
    	 timeLeftPrevious = timeLeft;
    	 return false;
     }
     return true;
  }
}