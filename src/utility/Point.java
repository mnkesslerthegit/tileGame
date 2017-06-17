package utility;

public class Point {
	private int x;
	private int y;

	public Point(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Point add(Point direction) {
		Point result = new Point(x, y);
		result.x += direction.x;
		result.y += direction.y;
		return result;
	}

	public String toString() {
		return x + " " + y;
	}

}
