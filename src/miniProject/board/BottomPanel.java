package miniProject.board;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel implements ChessBoard.TurnChangeListener {
	private JLabel statusLabel;
	private ChessBoard chessBoard;

	public BottomPanel(ChessBoard chessBoard) {
		this.chessBoard = chessBoard;
		setLayout(new FlowLayout(FlowLayout.CENTER));
		statusLabel = new JLabel(chessBoard.getCurrentTurn());
		add(statusLabel);

		chessBoard.setTurnChangeListener(this);
	}

	@Override
	public void onTurnChanged(boolean isWhiteTurn) {
		updateStatus();
	}

	public void updateStatus() {
		statusLabel.setText(chessBoard.getCurrentTurn());
	}
}