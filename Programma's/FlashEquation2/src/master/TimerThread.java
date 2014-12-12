package master;

/* TimerThread.java
- Copyright (c) 2014, HerongYang.com, All Rights Reserved.
*/
import java.util.*;
import java.text.*;
class TimerThread extends Thread {
  private int c_millisecond, c_second, c_minute, c_hour;
  private static int clock_interval = 100; // in milliseconds < 1000
  
  public TimerThread(int remaining_seconds) {

     	c_hour = remaining_seconds/3600;
     	c_minute = remaining_seconds/60;
        c_second = remaining_seconds%60;
        c_millisecond = 0;

  }
  public void run() {
     while (!isInterrupted()) {
        try {
           sleep(clock_interval);
        } catch (InterruptedException e) {
           break; // the main thread wants this thread to end
        }
        c_millisecond -= clock_interval;      
        if (c_millisecond>=1000) {
           c_second += c_millisecond/1000;
     	    c_millisecond = c_millisecond%1000;
        }
        if (c_second>=60) {
     	    c_minute += c_second/60;
     	    c_second = c_second%60;
        }
        if (c_minute>=60) {
     	    c_hour += c_minute/60;
           c_minute = c_minute%60;
        }
        if (c_millisecond<0) {
     	    c_second--;
           c_millisecond += 1000;
        }
        if (c_second<0) {
     	    c_minute--;
           c_second += 60;
        }
        if (c_minute<0) {
           c_hour--;
           c_minute += 60;
        }
        if (c_hour<0) {
           c_hour = 0;
           break; // end this thread 
        }
     }
  }
}