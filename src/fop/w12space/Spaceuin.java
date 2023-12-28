package fop.w12space;

import java.util.ArrayList;
import java.util.List;

public class Spaceuin extends Thread {

	List<Beacon> last = new ArrayList<Beacon>();
	List<Beacon> last2 = new ArrayList<Beacon>();
	List<Spaceuin> others = new ArrayList<Spaceuin>();
    Beacon current;
    Beacon mission;
    Beacon destination = null;
    FlightRecorder logs;
    boolean exploring = true;
 
    public Spaceuin(Beacon start, Beacon destination, FlightRecorder flightRecorder) {
        current = start;
        mission = destination;
        logs = flightRecorder;
        last.add(current);
		logs.recordArrival(current);
    }
    public void run()
    {
    	while(exploring)
    	{
    		
    		if(current  == mission)
    		{
    			logs.tellStory();
    			for(Thread t : Thread.getAllStackTraces().keySet())
    			{
    				if(t != this)
    				{
        				t.interrupt();
    				}
    			}
    			break;
    		}
    		if(destination == null)
    		{
    			for(BeaconConnection c : current.connections())
    			{
    				if(c.beacon() == mission)
    				{       				
        				if(c.type() == ConnectionType.NORMAL)
        				{
        					if(last.contains(c.beacon()))
        					{
        						destination = null;
        					}
        					else
        					{
        						destination = c.beacon();
        					}
        				}
    				}
    				
    				else
    				{
    					if(c.type() == ConnectionType.WORMHOLE && !last2.contains(c.beacon()))
        				{
        					last2.add(c.beacon());
        					Spaceuin x = new Spaceuin(c.beacon(),mission,new SimpleFlightRecorder());
        					x.start();
        					
        				}
        				
        				if(destination == null && c.type() == ConnectionType.NORMAL)
        				{
        					if(last.contains(c.beacon()))
        					{
        						destination = null;
        					}
        					else
        					{
        						destination = c.beacon();
        					}
        				}
    				}
    				
    			}
    		}
    		
    		if(destination == null && last.indexOf(current) > 0)
    		{
    			destination = last.get(last.indexOf(current)-1);
    		}
    		
    		if(destination  == null)
    		{
    			break;
    		}
    		last.add(destination);
    		logs.recordDeparture(current);
    		current = destination;
    		logs.recordArrival(destination);
    		destination = null;
    	}
		System.exit(0);
  	
    }
    
    
    @Override
    public String toString() {
        // changing that might be useful for testing
        return super.toString();
    }
}
