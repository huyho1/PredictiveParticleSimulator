/**
 * Represents a wall, which is the boundary of the simulation on all four sides
 */
public class Wall {
	private String _side;
	/**
	 * 
	 * @param type is the type of wall it is; "Horizontal" or "Vertical"
	 * @param side is the side of the wall; "Top", "Bottom", "Left", "Right"
	 */
	public Wall(String side) {
		this._side = side;
	}
	
	/**
	 * 
	 * @return the side of the wall
	 */
	public String getSide() {
		return _side;
	}
}
 