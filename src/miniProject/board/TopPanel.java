package miniProject.board;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
	public TopPanel(ChessFrame frame) {
		setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel titleLabel = new JLabel("Chess");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

		JButton newGameButton = new JButton("New Game");
		JButton finishButton = new JButton("Game Finish");

		newGameButton.addActionListener(e -> frame.restartGame());
		finishButton.addActionListener(e -> frame.finishGame());

		add(titleLabel);
		add(newGameButton);
		add(finishButton);
	}
}