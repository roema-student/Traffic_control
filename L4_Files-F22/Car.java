// This file defines class "Car".

// This code uses
//      class Synch, which defines the semaphores and variables
//                   needed for synchronizing the cars.

public class Car extends Thread {
  int myName;  // The variable myName stores the name of this thread.
               // It is initialized in the constructor.



  // This is the constructor for class Car.  It has an integer parameter,
  // which is the name that is given to this thread.
  public Car(int name) {
    // copy the parameter value to local variable "MyName"
    myName = name;

    // Call threadStart to let the timeSim scheduler know that another
    // thread is starting.  timeSim needs to know how many threads there
    // are, so that it can accurately judge when all threads have finished
    // their current computation, so that simulated time can be advanced.
    Synch.timeSim.threadStart();
  }  // end of the constructor for class "Car"
  
  
  public static int TL_G = 100;
  public static int TL_R = 100;

// define a function to see the traffic light
  public int seeTL(int side) {
	  // side=0 eastside, eastside green from 0-TL_G, then green from TL_R+TL_G+TL_R-TL_R+TL_G+TL_R+TL_G
	  // side=1 westside
	  int result = 0;
	  int curtime = Synch.timeSim.curTime();
	  int timearea = curtime%(2*TL_R + 2*TL_G);
	  if(side == 0) {
		  if(0<timearea && timearea<TL_G) {
			  result = 1;
		  }
	  }
	  if(side == 1) {
		  if((TL_G+TL_R)<timearea && timearea<(2*TL_G + TL_R)) {
			  result = 1;
		  }
	  }
	  return result;
  }

  public void run () {
	  // claim red traffic light and green traffic light time
    for (int I=1;  I<= 4; I++) {

      // Simulate driving around Barriefield.
      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " is driving around Barriefield.\n");
      Synch.timeSim.doSleep(1, 500);

      // Now cross the causeway westbound, into Kingston.  This might
      // involve some waiting (if the westbound light is red).
      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " wants to cross westbound.\n");
      // *** Put synchronization code here, to make cars wait if the westbound
      // *** light is red.
          //     Note that there should be some reaction time, so when
	  //     the light turns green the first car starts crossing.  Then after a
	  //     short pause (simulated, for example, by "sleep(1)"), the second car
	  //     starts crossing, and so on.  This is more realistic as
	  //     when you are waiting in a long line of cars at a traffic light, you see the
	  //     light turn green up there, but it takes a while before all the cars ahead
	  //     of you get going, so you have to keep waiting even though the light is green.
	  //     In some cases, you don't even make it across on this green; you have to 
	  //     wait through another red cycle for the next green.
	  try { 
		  Synch.westside.acquire();
	  } catch(Exception e) {}
	  Synch.timeSim.doSleep(1);
      

      // Now we have permission to cross the causeway.  Crossing is simulated
      // by a sleep.  The sleep time is chosen to be relatively long (compared
      // to the sleep times for driving around and getting donuts), so that
      // it frequently happens that several cars are on the bridge.
      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " is starting to cross westbound.\n");
      Synch.timeSim.doSleep(100);

      // Simulate driving to petrol station, filling the car fuel tank with gasoil/diesel, leaving petrol station, and
      // driving back to the causeway.
      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " is filling the car fuel tank at the petrol station.\n");
      Synch.timeSim.doSleep(1, 500);

      // Now cross the causeway eastbound, back into Barriefield.
      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " wants to cross eastbound.\n");

      // *** Put synchronization code here, to make cars wait if the eastbound
      // *** light is red..
      try {
    	  Synch.eastside.acquire();
      }catch(Exception e) {}
      Synch.timeSim.doSleep(1);

      System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " is starting to cross eastbound.\n");
      Synch.timeSim.doSleep(100);

    } // end of "for" loop
    System.out.println("At time " + Synch.timeSim.curTime() + " Car "+ myName + " has stoped and its driver went out.\n");
    Synch.timeSim.threadEnd();  // Let timeSim know that this thread
                                // has ended.
  }  // end of "run" method
}  // end of class "Car"

