package com.uc3m.gade4all.game;


/**
 * Class that represents a character.
 * 
 * @author Sandra Garcia Rodriguez
 */
public class Character extends Element{

	/** Life points */
	public int life; 
	/** Attack radius */
	public int attackRadius;
	/** Power attack */
	public int attackPower; 
	/** Movement capacity */
	public int movement;

	/**
	 * Constructor by a Character object
	 * 
	 * @param X  Coordinate X.
	 * @param Y  Coordinate Y.
	 * @param vida  Lifepoints.
	 * @param aR  Attack radius.
	 * @param aP  Attack power.
	 * @param mov  Movement capacity.
	 */
	public Character(int X, int Y, int vida, int aR, int aP, int mov){
		posX=X;
		posY=Y;
		life=vida;
		attackRadius=aR;
		attackPower=aP;
		movement =mov;
	}
	
	/**
	 * Default constructor
	 */
	public Character(){}
	
	public String mostrar(){
		return "P";
	}
}
