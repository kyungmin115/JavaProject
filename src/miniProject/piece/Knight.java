package miniProject.piece;

public class Knight extends ChessPiece {
	public Knight(int x, int y, boolean isWhite) {
		super(x, y, isWhite);
		loadImage();
	}

	@Override
	public boolean inValidMove(int targetX, int targetY, ChessPiece[][] board) {
		int dx = Math.abs(targetX - x);
		int dy = Math.abs(targetY - y);
		return (dx == 2 && dy == 1 || dx == 1 && dy == 2)
				&& (board[targetY][targetX] == null || board[targetY][targetX].isWhite != this.isWhite);
	}
}
