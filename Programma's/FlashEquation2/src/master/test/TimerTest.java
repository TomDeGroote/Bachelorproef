package master.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import master.TimerThread2;

import org.junit.Test;

public class TimerTest {

	@Test
	public void startTimerTest() {
		int time = 1000;
		int marge = 10; // in milli seconds
		TimerThread2 timer = new TimerThread2(time);
		long timeStarted = System.currentTimeMillis();
		ArrayList<Integer> list = new ArrayList<Integer>();
		try {
			timer.run();
			while(true) {
				
			}
		} catch (Exception e) {
			long finishedTime = time - (System.currentTimeMillis() - timeStarted);
			if(finishedTime > 0 || Math.abs(finishedTime) > marge) {
				fail("timer shouldn't be finished: " + (System.currentTimeMillis() - timeStarted));
			}
		}
		
	}

}
