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
import utility.Move;
import utility.Point;

/**
 * 
 *
 * Need to construct test class to figure out what's going on when I trim the
 * move history down
 * 
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
	List<State> states = new ArrayList<>();

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
		saveState(current);
		// visitedStates.clear();
	}

	/**
	 * Adds the state to the array or states, and also puts it in a hashtable.
	 * AUTOMATICALLY ALTERS THE ARRAY AND TABLE to remove duplicate states.
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

			// do one valid move
			current = makeRandomMove(place, current);
			// save the new state
			saveState(current);

			// String test = slow.nextLine();
			// System.out.println("Progress?");
			// print();
		}
		System.out.println("Solved in: " + states.size() + " steps");
		for (int q = 0; q < 100000; q++) {
			pruneHistory();
		}

		System.out.println("Solved in: " + states.size() + " steps");
		System.out.println(winCondition());
		printHistory();

	}

	public boolean playerMove(Move next) {
		Point nextHole = next.doMove(current.hole);
		if (inBounds(nextHole)) {

			// swap the tiles
			swapHelper(place, current.hole, nextHole);

			// generate new state
			current = new State(state(place), nextHole, next);
			// save the new state
			saveState(current);
			return true;
		}
		return false;
	}

	/**
	 * @return a new move that isn't the same move as the last move. This method
	 *         always remembers the last move
	 */
	private Move nextMove(State current) {
		Move result = Move.randomMove();
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

	/**
	 * Takes in an array, and the state associated with it, and makes one valid
	 * move.
	 * 
	 * @param tiles
	 * @return gives the resulting state from making the move.
	 */
	private State makeRandomMove(int[][] tiles, State info) {
		// last = info.lastMove;

		Move next = nextMove(info);

		Point hole = info.hole;
		// System.out.print("hole is: " + hole);

		// time to switch the hole and one of the squares
		Point swapTarget = next.doMove(hole);
		// System.out.println("here's the hole: " + hole);

		// tiles[hole.getX()][hole.getY()] =
		// tiles[swapTarget.getX()][swapTarget.getY()];
		// tiles[swapTarget.getX()][swapTarget.getY()] = 0;
		swapHelper(tiles, swapTarget, hole);

		return new State(state(tiles), swapTarget, next);

	}

	/**
	 * swaps two locations in a two dimensional ary
	 * 
	 * @param ary
	 * @param hole
	 * @param target
	 */
	private void swapHelper(int[][] ary, Point a, Point b) {
		int temp = ary[a.getX()][a.getY()];
		ary[a.getX()][a.getY()] = ary[b.getX()][b.getY()];
		ary[b.getX()][b.getY()] = temp;
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

	int exitCount = 0;

	/**
	 * Assumes we have an arraylist of states, and the last state is the winning
	 * state.
	 * 
	 */
	private boolean pruneHistory() {

		// Creat an alternative state. It
		State nextState = states.get(0);
		ArrayList<State> altSteps = new ArrayList<State>();
		// add the first state
		altSteps.add(nextState);

		int[][] branch = fromState(nextState.state);
		// iterate until the alternative state has taken more steps
		for (int q = 0; q < states.size(); q++) {
			// generate new states
			
			//TODO: write a test which makes sure random move never gives back the same move
		//	State old = nextState;
			nextState = makeRandomMove(branch, nextState);
			altSteps.add(nextState);
//			if(old.state.equals( nextState.state)){
//				System.out.println("problems");
//				System.exit(0);
//			}

			// check if we have this state in the original state array
			int index = states.indexOf(nextState);
			// if we have, check if it took less seps this time
			if (index != -1 && index > altSteps.size()) {
				// if it did, we need to replace the old steps
				System.out.println("found  duplicate");
				states.removeAll(states.subList(0, index+1));
				
//				if(states.get(0).equals(altSteps.get(altSteps.size()-1))){
//					System.out.println("this happened");
//					System.out.println(states.get(0).state + "     " + altSteps.get(altSteps.size()-1).state );
//				}else{
//					System.out.println("this didn't happen WOLOLOLO");
//				}
				altSteps.addAll(states);
				
				states = altSteps;
				return true;

			}

		}
		return false;

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

	// public void playerMove()

	private boolean winCondition() {
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

	public void printHistory() {
		for (int i = 0; i < states.size(); i++) {
			place = fromState(states.get(i).state);
			print();
			System.out.println("VICTORY LAP");
		}

	}

	private Integer state(int[][] board) {
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
