package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import utility.ArrayReader;
import utility.Point;

/**
 * Still need to create some representation for the boards state, (number) and a
 * hash table to store the states in
 * 
 * use brute force get every number in the right place, and then use breadth on
 * the states to see if they yield short cuts to later states. iterating over
 * the whole list looking for shortcuts until the number of steps stops
 * changing.
 * 
 * As extra work, try to figure out a faster way to get the answer than brute
 * force? one thing is don't allow moves that go to a previous state, and cut
 * those out of the solution early.
 * 
 * @author Max
 *
 */
public class Board {

	private final int[][] place;
	private Point hole;
	private List<Move> moveHistory = new ArrayList();

	public Board() {
		ArrayReader reader = new ArrayReader("tiles.txt");
		place = reader.read();

		for (int i = 0; i < place.length; i++) {
			for (int q = 0; q < place[i].length; q++) {
				if (place[i][q] == 0)
					hole = new Point(i, q);
			}
		}
	}

	Scanner slow = new Scanner(System.in);

	/**
	 * Keep making moves until the board enters a win state
	 */
	public void test() {
		int i=0;
		while (!winCondition()) {

			Move next = nextMove();
			// time to switch the hole and one of the squares
			Point swapTarget = next.doMove(hole);

			place[hole.getX()][hole.getY()] = place[swapTarget.getX()][swapTarget.getY()];
			place[swapTarget.getX()][swapTarget.getY()] = 0;
			hole = swapTarget;

			if( i++ % 10000 == 0){
			//System.out.println();
			print();
			}

			// String test = slow.nextLine();

		}
	}

	/**
	 * ASSUMES A SQUARE ARRAY TODO: need to fix the thing where the width and
	 * length constants are in the main class and not here
	 * 
	 * @param test
	 * @return
	 */
	private boolean inBounds(Point test) {
		if (test.getX() >= place.length || test.getY() >= place[0].length || test.getX() < 0 || test.getY() < 0) {
			// System.out.println(test + " is not in bounds");
			return false;
		}
		return true;
	}

	Random randy = new Random();

	private Move randomMove() {
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

	/**
	 * Not sure how to make sure that moves always get added to move history...
	 * Also, I set myself up so choosing random moves near walls and corners
	 * creates unnecessary steps.
	 * 
	 * @return
	 */
	private Move nextMove() {
		Move result = randomMove();
		if (!moveHistory.isEmpty()) {
			Move last = moveHistory.get(moveHistory.size() - 1);
			while (result.reverse(last) || !inBounds(result.doMove(hole))) {
				if (randy.nextBoolean()) {
					// System.out.println("Decrementing: " + result);
					result = result.decrement();
					// System.out.println("decrementing finished: " + result);

				} else {
					// System.out.println("Incrementing" + result);

					result = result.increment();

					// System.out.println("Incrementing finished: " + result);
				}
				// System.out.println(result);

			}
		} else {
			while (!inBounds(result.doMove(hole))) {
				if (randy.nextBoolean()) {

					result = result.decrement();

				} else {

					result = result.increment();

				}
			}
		}

		moveHistory.add(result);
		// System.out.println("MOVING: " + result);
		return result;
	}

	private enum Move {
		UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

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

	public boolean winCondition() {
		int count = 1;
		for (int i = 0; i < place.length; i++) {
			for (int q = 0; q < place[i].length; q++) {
				if (place[i][q] != count && place[i][q] != 0) {
					return false;
				}
				count++;
			}
		}
		return true;
	}

	public void print() {
		for (int i = 0; i < place.length; i++) {
			for (int q = 0; q < place[i].length; q++) {
				System.out.print(place[i][q] + "	");
			}
			System.out.println();
		}
	}

}
