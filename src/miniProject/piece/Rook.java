package miniProject.piece;

public class Rook extends ChessPiece {
	public Rook(int x, int y, boolean isWhite) {
		super(x, y, isWhite);
		loadImage();
	}

	@Override
	public boolean inValidMove(int targetX, int targetY, ChessPiece[][] board) {
		if (targetX == x || targetY == y) {
			int stepX = Integer.compare(targetX, x);
			int stepY = Integer.compare(targetY, y);
			for (int i = 1; i < Math.max(Math.abs(targetX - x), Math.abs(targetY - y)); i++) {
				if (board[y + stepY * i][x + stepX * i] != null) {
					return false;
				}
			}
			return board[targetY][targetX] == null || board[targetY][targetX].isWhite != this.isWhite;
		}

		return false;
	}
}
