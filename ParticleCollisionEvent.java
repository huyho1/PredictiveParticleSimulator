
public class ParticleCollisionEvent extends Event{
	Particle p1;
	Particle p2;
	
	public ParticleCollisionEvent (Particle part1, Particle part2, double timeOfEvent, double timeEventCreated) {
		super(timeOfEvent, timeEventCreated);
		this.p1 = part1;
		this.p2 = part2;
	}
	
	/* Getters*/
	public Particle getP1() {
		return p1;
	}
	
	public Particle getP2() {
		return p2;
	}
}
