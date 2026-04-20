public class Board{
	private int[][] matrix;
	private int gameSpeed;
	private Block currentBlock;
	private final int width = 10;
	private final int height = 20;
	private bool isPaused;
	private bool isStarted;
	private int score;
	private int level;
	public Board() {
		matrix = new int[20][10];
		isPaused = false;
		isStarted = false;
	}
	public void spawnBlock() {
		
	}
	//return number of lines removed
	public int removeFullLines() {
		return -1;
	}
	public void stopPiece() {
		
	}
	public void drop() {
		
	}
}