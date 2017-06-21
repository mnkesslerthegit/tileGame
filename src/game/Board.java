package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import utility.ArrayReader;
import utility.Point;

/**
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

	private static final int GRIDWIDTH = 3;
	private static final int GRIDLENGTH = 3;
	private int[][] place;

	// trying to replace these with a state variable
	// private Point hole;
	// private Move last;
	State current;

	/**
	 * remember if a state was generated
	 */
	Hashtable<Integer, Boolean> visitedStates = new Hashtable<Integer, Boolean>(362880);
	ArrayList<State> states = new ArrayList<>();

	/**
	 * Keep track of the last move
	 */

	public Board() {
		ArrayReader reader = new ArrayReader("tiles.txt");
		place = reader.read();
		Point hole = null;

		for (int i = 0; i < place.length; i++) {
			for (int q = 0; q < place[i].length; q++) {
				if (place[i][q] == 0)
					hole = new Point(i, q);
			}
		}

		current = new State(state(place), hole, null);
		// visitedStates.clear();
	}

	/**
	 * Saves the place array as an integer, and adds it to move history, and
	 * hashes it into a table.
	 */
	private void saveState(State current) {
		Integer key = state(place);

		states.add(current);
		// System.out.println(key.hashCode());
		// visitedStates.clear();
		// visitedStates.put(1, true);
		Boolean temp = visitedStates.put(key, true);
		if (temp != null && temp) { // if we found a collision, it means
									// we've been here before, and all
									// the steps before were useless.

			states.remove(states.size() - 1); // remove the repeat key
			// System.out.println(states.size() + " before");
			while (!states.isEmpty() && !states.get(states.size() - 1).state.equals(key)) {
				// remove all keys before the original
				visitedStates.put(states.get(states.size() - 1).state, false);
				states.remove(states.size() - 1);

				// i can't make the values null again, for some reason, so I
				// make them false.
			}
			// System.out.println(states.size() + " after");
			// System.out.println("Found a duplicate state" + states.get(0));
		}
	}

	Scanner slow = new Scanner(System.in);

	/**
	 * Keep making moves until the board enters a win state
	 */
	public void test() {
		int i = 0;
		System.out.println("First one:");
		print();
		// saveState();

		int count = 0;
		while (!winCondition()) {
			// while (count++ < 10) {

			// print();
			Move nextMove = nextMove(current);
			// time to switch the hole and one of the squares
			Point swapTarget = nextMove.doMove(current.hole);

			place[current.hole.getX()][current.hole.getY()] = place[swapTarget.getX()][swapTarget.getY()];
			place[swapTarget.getX()][swapTarget.getY()] = 0;
			State nextState = new State(state(place), swapTarget, nextMove);

			// save the new state
			saveState(nextState);
			// current state becomes new state
			current = nextState;

			// String test = slow.nextLine();
			// System.out.println("Progress?");
			// print();
		}
		System.out.println("Solved in: " + states.size() + " steps");
		pruneHistory();

		System.out.println("Solved in: " + states.size() + " steps");
		System.out.println(winCondition());

	}

	/**
	 * Takes in an array, and the state associated with it, and makes one valid
	 * move.
	 * 
	 * @param tiles
	 * @return gives the resulting state from making the move.
	 */
	private State swapTile(int[][] tiles, State info) {
		// last = info.lastMove;

		Move next = nextMove(info);

		Point hole = info.hole;
		// System.out.print("hole is: " + hole);

		// time to switch the hole and one of the squares
		Point swapTarget = next.doMove(hole);
		// System.out.println("here's the hole: " + hole);

		tiles[hole.getX()][hole.getY()] = tiles[swapTarget.getX()][swapTarget.getY()];
		tiles[swapTarget.getX()][swapTarget.getY()] = 0;

		return new State(state(tiles), swapTarget, next);

	}

	/**
	 * Finds the zero in a 3x3 two dimensional array
	 * 
	 * @param arr
	 * @return a point where a zero is, or null
	 */
	private Point getHole(int[][] arr) {
		for (int i = 0; i < this.GRIDLENGTH; i++) {
			for (int q = 0; q < this.GRIDWIDTH; q++) {
				if (arr[i][q] == 0) {
					return new Point(i, q);
				}
			}
		}
		return null;
	}

	private class State {
		private final Integer state;
		private final Point hole;
		private final Move lastMove;

		public State(int state, Point hole, Move last) {
			this.state = state;
			this.hole = hole;
			this.lastMove = last;

		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}

			if (!(other instanceof State)) {
				return false;
			}

			// typecast o to Complex so that we can compare data members
			State otherState = (State) other;

			return otherState.state.equals(state);

		}

		public String toString() {
			return state + " last move: " + lastMove;
		}

	}

	int exitCount = 0;

	/**
	 * Assumes we have an arraylist of states, and the last state is the winning
	 * state. apparently the states list holds the last state in index 0.
	 * 
	 */
	private void pruneHistory() {

		boolean result = false;

		// System.out.println(i);
		State nextState = states.get(states.size() - 1);
		ArrayList<State> altSteps = new ArrayList<State>();

		// System.out.println("here's the state: ");

		int[][] branch = fromState(nextState.state);
		// print(branch);
		for (int q = 0; q < states.size(); q++) { // iterate until we've
													// moved too much to
													// shortcut

			// generate new states
			// State test = swapTile(branch, nextState);
			nextState = swapTile(branch, nextState);
			altSteps.add(nextState);
			int index = states.indexOf(nextState);
			if (index != -1) {
				System.out.println("index is: " + index);
				// at first, its fine if index is slightly less than the size
				// then it needs to be zero eventually
				if (index < states.size() - altSteps.size()) {
					
				}
			}

			// System.out.println(nextState);
			// TODO: get this to match

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

	public void printHistory() {
		for (int i = 0; i < states.size(); i++) {
			place = fromState(states.get(i).state);
			print();
			System.out.println("VICTORY LAP");
		}
		// System.out.println("why not finished?");

	}

	/**
	 * 
	 * Not sure how to make sure that moves always get added to move history...
	 * Also, I set myself up so choosing random moves near walls and corners
	 * creates unnecessary steps.
	 * 
	 * @return a new move that isn't the same move as the last move. This method
	 *         always remembers the last move
	 */
	private Move nextMove(State current) {
		Move result = randomMove();
		if (current.lastMove != null) {

			while (result.reverse(current.lastMove) || !inBounds(result.doMove(current.hole))) {
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
			while (!inBounds(result.doMove(current.hole))) {
				if (randy.nextBoolean()) {
					result = result.decrement();
				} else {
					result = result.increment();
				}
			}
		}
		// TODO: I used to remember the last move here
		// last = result;
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
				if (i == 0 && q == 0 || i == 2 && q == 2) {
					if (place[i][q] != 1 && place[i][q] != 9 && place[i][q] != 0) {
						return false;
					}
				} else {

					if (place[i][q] != count && place[i][q] != 0) {
						return false;
					}
				}
				count++;
			}
		}
		System.out.println("YOU WIN");
		// print();
		// printHistory();
		return true;
	}

	public void print(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int q = 0; q < board[i].length; q++) {
				System.out.print(board[i][q] + "	");
			}
			System.out.println();
		}
	}

	public void print() {

		print(place);
	}

	public Integer state(int[][] board) {
		String temp = "";
		for (int i = 0; i < board.length; i++) {
			for (int q = 0; q < board[i].length; q++) {
				temp += board[i][q];
			}
		}
		Integer result = new Integer(Integer.parseInt(temp));

		return result;
	}

	public int[][] fromState(Integer state) {
		String str = "" + state;
		// System.out.println("STR IS: " + str);

		int[][] result = new int[GRIDLENGTH][GRIDWIDTH];
		if (str.length() == 8) {
			result[0][0] = 0;
			for (int i = 1; i < str.length() + 1; i++) {
				// the array sees i+1, but the string just sees i
				result[i / GRIDLENGTH][i % 3] = str.charAt(i - 1) - 48;
			}

		} else {

			for (int i = 0; i < str.length(); i++) {
				result[i / 3][i % 3] = str.charAt(i) - 48;
			}
		}

		return result;

	}

}
