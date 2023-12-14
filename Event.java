/**
 * Represents a collision between a particle and another particle, or a particle and a wall.
 */
public class Event implements Comparable<Event> {
	private double _timeOfEvent;
	private double _timeEventCreated;
	private Particle p1;
	private Particle p2;
	private Wall wall;

	/**
	 * @param timeOfEvent the time when the collision will take place
	 * @param timeEventCreated the time when the event was first instantiated and added to the queue
	 */
	public Event (double timeOfEvent, double timeEventCreated) {
		_timeOfEvent = timeOfEvent;
		_timeEventCreated = timeEventCreated;
	}

	@Override
	/** 
	 * Compares two Events based on their event times. Since you are implementing a maximum heap,
	 * this method assumes that the event with the smaller event time should receive higher priority.
	 */
	public int compareTo (Event e) {
		if (_timeOfEvent < e._timeOfEvent) {
			return +1;
		} else if (_timeOfEvent == e._timeOfEvent) {
			return 0;
		} else {
			return -1;
		}
	}
	
	/* Getters */
	public double getTimeOfEvent () {
		return _timeOfEvent;
	}
	
	public double getTimeEventCreated () {
		return _timeEventCreated;
	}

	public Particle getP1() {
		return p1;
	}
	
	public Particle getP2() {
		return p2;
	}
	
	public Wall getWall() {
		return wall;
	}
}
