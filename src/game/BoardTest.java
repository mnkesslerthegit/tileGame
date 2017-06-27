package game;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

import junit.framework.TestCase;
import utility.Move;

public class BoardTest extends TestCase {
	Board myBoard;

	@Override
	protected void setUp() {

	}

	@Override
	protected void tearDown() {

		// TODO I don't know what to put here

	}

	@Test
	public void testRepeatMovesRemoved() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		Field field = Board.class.getDeclaredField("place");
		field.setAccessible(true);

		// Method prune = Board.class.getDeclaredMethod("pruneHistory",
		// (Class<?>[]) null);
		// prune.setAccessible(true);

		Board myBoard = new Board();
		// printDimArrays((int[][]) field.get(myBoard));
		for (int i = 0; i < 3; i++) {
			myBoard.playerMove(Move.LEFT);
			// printDimArrays((int[][]) field.get(myBoard));
		
			myBoard.playerMove(Move.UP);
	
			// printDimArrays((int[][]) field.get(myBoard));
			myBoard.playerMove(Move.RIGHT);
			// printDimArrays((int[][]) field.get(myBoard));
			myBoard.playerMove(Move.DOWN);
		}
		
	
		
		// printDimArrays((int[][]) field.get(myBoard));

		// prune.invoke(myBoard, (Object[]) null);
		assertTrue("Didn't cut out useless moves", myBoard.states.size() == 1);
		// System.out.println(myBoard.states);

	}

	public void testPruneHistory() throws NoSuchMethodException, SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
	//	System.out.println("testing prune history");
		Board myBoard = new Board();
		Field field = Board.class.getDeclaredField("place");
		field.setAccessible(true);

		Method prune = Board.class.getDeclaredMethod("pruneHistory", (Class<?>[]) null);
		prune.setAccessible(true);

		// printDimArrays((int[][]) field.get(myBoard));
		myBoard.playerMove(Move.RIGHT);
		// printDimArrays((int[][]) field.get(myBoard));
		myBoard.playerMove(Move.UP);
		// printDimArrays((int[][]) field.get(myBoard));
		myBoard.playerMove(Move.UP);
		// printDimArrays((int[][]) field.get(myBoard));
		myBoard.playerMove(Move.LEFT);
		// printDimArrays((int[][]) field.get(myBoard));
		myBoard.playerMove(Move.LEFT);
		// printDimArrays((int[][]) field.get(myBoard));
		myBoard.playerMove(Move.DOWN);
		// printDimArrays((int[][]) field.get(myBoard));
		myBoard.playerMove(Move.DOWN);
		// printDimArrays((int[][]) field.get(myBoard));

		for (int i = 0; i < 10000; i++) {
			prune.invoke(myBoard, (Object[]) null);
		}

		assertTrue(myBoard.states.size() == 8);

		// prune.invoke(myBoard, (Object[]) null);

	}

	public void testConstructor()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Board myBoard = new Board();
		int[][] correctArray = { { 1, 2, 3 }, { 4, 8, 7 }, { 6, 0, 9 } };

		Field field = Board.class.getDeclaredField("place");
		field.setAccessible(true);
		// printDimArrays((int[][]) field.get(myBoard));
		// assertTrue();
		assertTrue("the board set up wrong", (Arrays.deepEquals((Object[]) field.get(myBoard), correctArray)));

	}

	/**
	 * Uses reflection to look at a the private array inside the board class.
	 * Tests to see if swapping tiles on the board produces the predicted state.
	 * 
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Test
	public void testMove()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Board myBoard = new Board();
		myBoard.playerMove(Move.LEFT);
		int[][] correctArray = { { 1, 2, 3 }, { 4, 8, 7 }, { 0, 6, 9 } };

		// System.out.println("Correct array: ");
		// printDimArrays(correctArray);
		// System.out.println("Real array: ");
		// printDimArrays(myBoard.);
		Field field = Board.class.getDeclaredField("place");
		field.setAccessible(true);
		// printDimArrays((int[][]) field.get(myBoard));
		// assertTrue();
		assertTrue("moving left broke the board", (Arrays.deepEquals((Object[]) field.get(myBoard), correctArray)));

	}

	/**
	 * I don't actually need this. Oops.
	 * 
	 * @param ary
	 * @return
	 */
	// private Integer[][] convertArrays(int[][] ary) {
	// Integer[][] result = new Integer[ary.length][];
	// for (int i = 0; i < ary.length; i++) {
	// result[i] = new Integer[ary[i].length];
	// for (int q = 0; q < ary[i].length; q++) {
	// result[i][q] = new Integer(ary[i][q]);
	// }
	// }
	// return result;
	//
	// }

	public void printDimArrays(int[][] correctArray) {
		for (int i = 0; i < correctArray.length; i++) {
			System.out.println(Arrays.toString(correctArray[i]));
		}
		System.out.println();
	}

	public void printDimArrays(Integer[][] correctArray) {
		for (int i = 0; i < correctArray.length; i++) {
			System.out.println(Arrays.toString(correctArray[i]));
		}
		System.out.println();
	}

}
