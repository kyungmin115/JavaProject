package miniProject.piece;

public class Pawn extends ChessPiece {

	public Pawn(int x, int y, boolean isWhite) {
		super(x, y, isWhite);
		loadImage();
	}

	@Override
	public boolean inValidMove(int targetX, int targetY, ChessPiece[][] board) {
		int direction = isWhite ? -1 : 1;
		// 앞으로 한 칸 이동
		if(targetX == x && targetY == y + direction && board[targetY][targetX] == null) {
			return true;
		}
		
		// 처음 이동 시 두 칸 
		if (targetX == x && targetY == y + 2 * direction && (y == (isWhite ? 6 : 1))
				&& board[y + direction][x] == null && board[targetY][targetX] == null 	 ) {
			return true;
		}
		
		//대각선으로 이동하여 상대 기물 잡기
		if (Math.abs(targetX - x) == 1 && targetY == y + direction && board[targetY][targetX] != null 
				&& board[targetY][targetX].isWhite != this.isWhite	 ) {
			return true;
		}
		
		return false;
	}

}
