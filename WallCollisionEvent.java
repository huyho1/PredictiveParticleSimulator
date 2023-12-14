
public class WallCollisionEvent extends Event{
	Particle p1;
	Wall w1;
	
	public WallCollisionEvent (Particle part1, Wall w, double timeOfEvent, double timeEventCreated) {
		super(timeOfEvent, timeEventCreated);
		this.p1 = part1;
		this.w1 = w;
	}
	
	/* Getters */
	public Particle getP1() {
		return p1;
	}
	
	public Wall getWall () {
		return w1;
	}
}