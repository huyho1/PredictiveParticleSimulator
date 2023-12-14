import java.util.*;
import java.util.function.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.sound.sampled.*;

public class ParticleSimulator extends JPanel {
	private Heap<Event> _events;
	private java.util.List<Particle> _particles;
	private double _duration;
	private int _width;
	private java.util.List<Wall> _walls;

	/**
	 * @param filename the name of the file to parse containing the particles
	 */
	public ParticleSimulator (String filename) throws IOException {
		_events = new HeapImpl<>();
		_walls = new ArrayList<>();
		
		Wall right = new Wall("Right");
		Wall left = new Wall("Left");
		Wall top = new Wall("Top");
		Wall bottom = new Wall("Bottom");

		// Parse the specified file and load all the particles.
		Scanner s = new Scanner(new File(filename));
		_width = s.nextInt();
		_duration = s.nextDouble();
		s.nextLine();
		_particles = new ArrayList<>();
		while (s.hasNext()) {
			String line = s.nextLine();
			Particle particle = Particle.build(line);
			_particles.add(particle);
		}
		_walls.add(right);
		_walls.add(left);
		_walls.add(top);
		_walls.add(bottom);

		setPreferredSize(new Dimension(_width, _width));
	}

	@Override
	/**
	 * Draws all the particles on the screen at their current locations
	 * DO NOT MODIFY THIS METHOD
	 */
        public void paintComponent (Graphics g) {
		g.clearRect(0, 0, _width, _width);
		for (Particle p : _particles) {
			p.draw(g);
		}
	}

	// Helper class to signify the final event of the simulation.
	private class TerminationEvent extends Event {
		TerminationEvent (double timeOfEvent) {
			super(timeOfEvent, 0);
		}
	}

	/**
	 * Helper method to update the positions of all the particles based on their current velocities.
	 */
	private void updateAllParticles (double delta) {
		for (Particle p : _particles) {
			p.update(delta);
		}
	}
	
	/**
	 * Enqueue will create a new Event from the particleList and the pre-existing _particles
	 * @param particleList 
	 * @param lastTime
	 */
	private void enqueue (java.util.List<Particle> particleList, double lastTime) {
		for (int i = 0; i < particleList.size(); i++) {
			for (int j = 0; j < _particles.size(); j++) {
				if (!_particles.get(j).equals(particleList.get(i))) {
					double particleCollisionTime = _particles.get(j).getCollisionTime(particleList.get(i));
					if (particleCollisionTime != Double.POSITIVE_INFINITY) {
						_events.add(new ParticleCollisionEvent(_particles.get(j), particleList.get(i), lastTime + particleCollisionTime, lastTime));
					}
				}
			}
			for (Wall w : _walls) {
				double wallCollisionTime = particleList.get(i).getWallCollisionTime(w, _width);
				if(wallCollisionTime != Double.POSITIVE_INFINITY) {
					_events.add(new WallCollisionEvent (particleList.get(i), w, lastTime + wallCollisionTime, lastTime));	
				}					
			}
		}
	}

	/**
	 * Executes the actual simulation.
	 */
	private void simulate (boolean show) {
		double lastTime = 0;

		// Create initial events, i.e., all the possible
		// collisions between all the particles and each other,
		// and all the particles and the walls.
		enqueue(_particles, lastTime);	
		
		_events.add(new TerminationEvent(_duration));
		while (_events.size() > 0) {
			Event event = _events.removeFirst();
			double delta = event.getTimeOfEvent() - lastTime;
			
			if (event instanceof TerminationEvent) {
				updateAllParticles(delta);
				break;
			}
			
			// Check if event still valid; if not, then skip this event
			if (event instanceof ParticleCollisionEvent) {
				ParticleCollisionEvent check = (ParticleCollisionEvent) event;
				Particle part1 = (Particle) check.p1;
				Particle part2 = (Particle) check.p2;
				if (part1.getLastUpdateTime() > event.getTimeEventCreated() || part2.getLastUpdateTime() > event.getTimeEventCreated()) 
					continue;
			}
			else if (event instanceof WallCollisionEvent) {
				if (event.getP1().getLastUpdateTime() > event.getTimeEventCreated())
					continue;
			}

			// Since the event is valid, then pause the simulation for the right
			// amount of time, and then update the screen.
			if (show) {
				try {
					Thread.sleep((long) delta);
				} catch (InterruptedException ie) {}
			}

			// Update positions of all particles
			updateAllParticles(delta);
			
			// Update the time of our simulation
			lastTime = event.getTimeOfEvent();
			
			java.util.List<Particle> _newParticles;
            _newParticles = new ArrayList<>();
            
			// Update the velocity of the particle(s) involved in the collision
			// (either for a particle-wall collision or a particle-particle collision).
			// You should call the Particle.updateAfterCollision method at some point.
			// Enqueue new events for the particle(s) that were involved in this event.
			if (event instanceof WallCollisionEvent) {
				event.getP1().updateAfterWalls(event.getWall(), lastTime);
                _newParticles.add(event.getP1());
                enqueue(_newParticles, lastTime);	
			}
			if (event instanceof ParticleCollisionEvent) {
				event.getP1().updateAfterCollision(lastTime, event.getP2());
				_newParticles.add(event.getP1());
				_newParticles.add(event.getP2());
				enqueue(_newParticles, lastTime);
			}
						
			// Redraw the screen
			if (show) {
				repaint();
			}
		}

		// Print out the final state of the simulation
		System.out.println(_width);
		System.out.println(_duration);
		for (Particle p : _particles) {
			System.out.println(p);
		}
	}

	public static void main (String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: java ParticalSimulator <filename>");
			System.exit(1);
		}

		ParticleSimulator simulator;

		simulator = new ParticleSimulator(args[0]);
		JFrame frame = new JFrame();
		frame.setTitle("Particle Simulator");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(simulator, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		simulator.simulate(true);
	}
}
