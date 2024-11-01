package miniProject.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import miniProject.piece.*;

public class ChessBoard extends JPanel {
	// 한 타일의 크기를 정의
	public static final int TILE_SIZE = 80;

	// 체스판을 8x8 배열로 설정
	private ChessPiece[][] board = new ChessPiece[8][8];

	// 현재 턴이 흰색인지 여부를 나타냄
	private boolean isWhiteTurn = true;

	// 선택된 체스 말 객체를 저장
	private ChessPiece chessPiece = null;

	// ChessBoard 생성자 - 보드 초기화 및 마우스 클릭 이벤트 리스너 설정
	public ChessBoard() {
		initBoard(); // 보드 초기화
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// 마우스 클릭 시, 클릭한 타일의 좌표를 계산
				int x = e.getX() / TILE_SIZE;
				int y = e.getY() / TILE_SIZE;
				handleMouseClick(x, y); // 클릭한 위치 처리
			}
		});
	}

	// 체스판을 초기화하는 메서드
	private void initBoard() {
		// 폰 말 배치
		for (int i = 0; i < 8; i++) {
			board[1][i] = new Pawn(i, 1, false); // 검은색 폰
			board[6][i] = new Pawn(i, 6, true); // 흰색 폰
		}
		// 검은색 말 배치
		board[0][0] = new Rook(0, 0, false);
		board[0][1] = new Knight(1, 0, false);
		board[0][2] = new Bishop(2, 0, false);
		board[0][3] = new Queen(3, 0, false);
		board[0][4] = new King(4, 0, false);
		board[0][5] = new Bishop(5, 0, false);
		board[0][6] = new Knight(6, 0, false);
		board[0][7] = new Rook(7, 0, false);

		// 흰색 말 배치
		board[7][0] = new Rook(0, 7, true);
		board[7][1] = new Knight(1, 7, true);
		board[7][2] = new Bishop(2, 7, true);
		board[7][3] = new Queen(3, 7, true);
		board[7][4] = new King(4, 7, true);
		board[7][5] = new Bishop(5, 7, true);
		board[7][6] = new Knight(6, 7, true);
		board[7][7] = new Rook(7, 7, true);
	}

	public interface TurnChangeListener {
		void onTurnChanged(boolean isWhiteTurn);
	}

	private TurnChangeListener turnChangeListener;

	public void setTurnChangeListener(TurnChangeListener listener) {
		this.turnChangeListener = listener;
	}

	private void toggleTurn() {
		isWhiteTurn = !isWhiteTurn;
		if (turnChangeListener != null) {
			turnChangeListener.onTurnChanged(isWhiteTurn);
		}
		repaint();
	}

	private List<Point> possibleMoves = new ArrayList<>();

	// 마우스 클릭을 처리하는 메서드
	private void handleMouseClick(int x, int y) {
		if (chessPiece == null) {
			// 기물 선택
			if (board[y][x] != null && board[y][x].isWhite == isWhiteTurn) {
				chessPiece = board[y][x]; // 선택된 기물 저장

				calculatePossibleMoves(chessPiece);
			}
		} else {
			if (isPossibleMove(x, y)) {
				ChessPiece temp = board[y][x];
				board[y][x] = chessPiece;
				board[chessPiece.y][chessPiece.x] = null;
				chessPiece.move(x, y);

				if (!isInCheck(isWhiteTurn)) {
					toggleTurn();

					if (isCheckmate(isWhiteTurn)) {
						String winner = isWhiteTurn ? "흰색 " : "검은색";
						JOptionPane.showMessageDialog(this, winner + "이 승리했습니다! 체크메이트!", "게임 종료",
								JOptionPane.INFORMATION_MESSAGE);
						restartGame();
					} else if (isInCheck(isWhiteTurn)) {
						JOptionPane.showMessageDialog(this, "체크!", "주의", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					board[chessPiece.y][chessPiece.x] = chessPiece;
					board[y][x] = temp;
				}
			}
			chessPiece = null;
			possibleMoves.clear();
		}
		repaint(); // 재그리기
	}

	private void calculatePossibleMoves(ChessPiece piece) {
		possibleMoves.clear();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (piece.inValidMove(col, row, board)) {
					possibleMoves.add(new Point(col, row));
				}
			}
		}
	}

	private boolean isPossibleMove(int x, int y) {
		return possibleMoves.contains(new Point(x, y));
	}

	public String getCurrentTurn() {
		return isWhiteTurn ? "흰색 턴" : "검은색 턴";
	}

	// 주어진 색이 체크 상황인지 확인하는 메서드
	private boolean isInCheck(boolean isWhite) {
		King king = null;
		// 해당 색의 킹 찾기
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] instanceof King && board[row][col].isWhite == isWhite) {
					king = (King) board[row][col];
					break;
				}
			}
			if (king != null)
				break;
		}

		if (king == null) {
//			System.out.println("킹이 null입니다. 킹이 보드에 없거나 초기화되지 않았습니다.");
			return true; // 또는 true로 처리할 수 있습니다.
		}
		// 상대편 말들이 킹을 공격할 수 있는지 확인
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] != null && board[row][col].isWhite != isWhite) {
					if (board[row][col].inValidMove(king.x, king.y, board)) {
						return true; // 체크 상황임
					}
				}
			}
		}
		return false; // 체크 상황이 아님
	}

	// 주어진 색이 체크메이트인지 확인하는 메서드
	private boolean isCheckmate(boolean isWhite) {
		// 체크가 아니면 체크메이트일 수 없음
		if (!isInCheck(isWhite)) {
			return false;
		}

		// 가능한 모든 이동을 확인하여 체크에서 벗어날 수 있는지 확인
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] != null && board[row][col].isWhite == isWhite) {
					for (int targetRow = 0; targetRow < 8; targetRow++) {
						for (int targetCol = 0; targetCol < 8; targetCol++) {
							if (board[row][col].inValidMove(targetCol, targetRow, board)) {
								ChessPiece temp = board[targetRow][targetCol];
								board[targetRow][targetCol] = board[row][col];
								board[row][col] = null;

								// 이동 후 체크에서 벗어날 수 있는지 확인
								if (!isInCheck(isWhite)) {
									board[row][col] = board[targetRow][targetCol];
									board[targetRow][targetCol] = temp;
									return false;
								}

								board[row][col] = board[targetRow][targetCol];
								board[targetRow][targetCol] = temp;
							}
						}
					}
				}
			}
		}
		String loser = isWhite ? "흰색" : "검은색";
		JOptionPane.showMessageDialog(this, loser + "이 체크메이트 되었습니다, 게임 종료", "게임 종료", JOptionPane.INFORMATION_MESSAGE);
		restartGame();
		return true; // 체크메이트
	}

	// 체스판을 그리는 메서드
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 체스판 타일 그리기
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				boolean isLight = (row + col) % 2 == 0; // 밝은 타일 여부
				g.setColor(isLight ? Color.WHITE : Color.GRAY); // 타일 색 설정
				g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);

				// 체스 말 그리기
				if (board[row][col] != null) {
					board[row][col].draw(g);

					g.setColor(Color.GREEN);
					for (Point p : possibleMoves) {
						int x = p.x * TILE_SIZE + TILE_SIZE / 2;
						int y = p.y * TILE_SIZE + TILE_SIZE / 2;
						g.fillOval(x - 5, y - 5, 10, 10);
					}
				}
			}
		}

		// 현재 턴 표시
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		String turnText = isWhiteTurn ? "흰색 턴" : "검은색 턴";
		g.drawString(turnText, 10, 20);

		// 체크 여부 표시
		if (isInCheck(isWhiteTurn)) {
			g.setColor(Color.RED);
			g.drawString("체크!", 10, 40);
		}

		// 체크메이트 여부 표시
		if (isCheckmate(isWhiteTurn)) {
			g.setColor(Color.RED);
			g.drawString("체크메이트 !", 10, 60);
		}
	}

	// 게임을 다시 시작하는 메서드
	public void restartGame() {
		int option = JOptionPane.showConfirmDialog(this, "게임을 다시 시작하시겠습니까?", "게임 재시작", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			// 모든 체스 말 제거
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					board[row][col] = null;
				}
			}
			initBoard(); // 보드 재초기화
			isWhiteTurn = true; // 흰색부터 시작
			repaint(); // 화면 다시 그리기
		} else {
			System.exit(0);
		}

	}

	// JPanel의 기본 크기를 설정하는 메서드
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(8 * TILE_SIZE, 8 * TILE_SIZE); // 8x
	}
}