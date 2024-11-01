package miniProject.board;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ChessFrame extends JFrame {
	private ChessBoard chessBoard;
	private TopPanel topPanel;
	private BottomPanel bottomPanel;

	public ChessFrame() {
		setTitle("Chess");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		chessBoard = new ChessBoard();
		topPanel = new TopPanel(this);
		bottomPanel = new BottomPanel(chessBoard);

		add(topPanel, BorderLayout.NORTH);
		add(chessBoard, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		pack();
		setVisible(true);
	}

	public void restartGame() {
		chessBoard.restartGame();
		bottomPanel.updateStatus();
	}

	public void finishGame() {
		dispose();
	}
}
