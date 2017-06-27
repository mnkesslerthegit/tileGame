package utility;

import java.util.Random;

public enum Move {
	UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

	private final int horz;
	private final int vert;

	private Move(int horz, int vert) {
		this.horz = horz;
		this.vert = vert;

	}

	/**
	 * tells you if the move is the reverse of the move you just did. This
	 * is a preliminary attempt to not generate purely random moves.
	 * 
	 * @param other
	 * @return
	 */
	public boolean reverse(Move other) {
		switch (this) {
		case UP:
			return other.equals(DOWN);
		case LEFT:
			return other.equals(RIGHT);
		case RIGHT:
			return other.equals(LEFT);
		case DOWN:
			return other.equals(UP);
		}
		System.out.println("ERROR 1");
		return false;
	}

	public Move increment() {
		switch (this) {
		case UP:
			return RIGHT;
		case RIGHT:
			return DOWN;
		case DOWN:
			return LEFT;
		case LEFT:
			return UP;
		}
		System.out.println("ERROR 2");
		return UP;

	}

	private static Random randy = new Random();
	public static Move randomMove() {
		switch (randy.nextInt(4) + 1) {
		case 1:
			return Move.DOWN;
		case 2:
			return Move.UP;
		case 3:
			return Move.LEFT;
		case 4:
			return Move.RIGHT;
		default:
			return Move.UP;

		}
	}
	
	public Move decrement() {
		switch (this) {
		case RIGHT:
			return UP;
		case DOWN:
			return RIGHT;
		case LEFT:
			return DOWN;
		case UP:
			return LEFT;
		}
		System.out.println("ERROR 2");
		return UP;

	}

	public Point doMove(Point start) {
		return start.add(new Point(horz, vert));
	}

}