package com.uc3m.gade4all.game;


/**
 * Class that represents a cell of the board.
 * 
 * @author Sandra Garcia Rodriguez
 */
public class Cell extends Element{

	/** Kind of cell:  1 DEFEND, 2 ATTACK, 3- OCCUPY, 4-NOTHING */
	public int type; // 1 DEFEND, 2 ATTACK, 3- OCCUPY, 4-NOTHING
	
	/**
	 * Constructor by a Cell object
	 * 
	 * @param X  Coordinate X.
	 * @param Y  Coordinate Y.
	 */
	public Cell(int X, int Y){
		super(X,Y);
	}
	
	/**
	 * Constructor by a Cell object
	 * 
	 * @param X  Coordinate X.
	 * @param Y  Coordinate Y.
	 * @param tipo  Type of cell.
	 */
	public Cell(int X, int Y, int tipo){
		this(X,Y);
		type=tipo;
	}
	
	
	public String mostrar(){
		return "S";
	}
}
