package miniProject.piece;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import miniProject.board.ChessBoard;

public abstract class ChessPiece {
	protected BufferedImage image;
	public int x;
	public int y;
	public boolean isWhite;
//	ChessBoard chessBoard = new ChessBoard();

	public ChessPiece(int x, int y, boolean isWhite) {
		this.x = x;
		this.y = y;
		this.isWhite = isWhite;
		loadImage();
	}

	protected void loadImage() {
		try {
			String imagePath = "src/images/";
			String pieceType = getClass().getSimpleName().toLowerCase();
			String color = isWhite ? "white" : "black";
			image = ImageIO.read(new File(imagePath + pieceType + "_" + color + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract boolean inValidMove(int targetX, int targetY, ChessPiece[][] board);

	public void move(int targetX, int targetY) {
		this.x = targetX;
		this.y = targetY;
	}

	public void draw(Graphics g) {
		g.drawImage(image, x * ChessBoard.TILE_SIZE, y * ChessBoard.TILE_SIZE, ChessBoard.TILE_SIZE, ChessBoard.TILE_SIZE, null);
	}
	

}
