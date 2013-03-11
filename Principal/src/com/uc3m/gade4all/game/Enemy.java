package com.uc3m.gade4all.game;

/**
 * Class that represents an enemy.
 * 
 * @author Sandra Garcia Rodriguez
 */
public class Enemy extends Element{

	/** Life points */
	public int life;
	/** Attack radius */
	public int attackRadius;
	/** Power attack */
	public int attackPower;
	/** Movement capacity */
	public int movement;
	
	/** Goal to reach*/
	public int goal[]; 
	
	/**
	 * Constructor by a Enemy object
	 * 
	 * @param X  Coordinate X.
	 * @param Y  Coordinate Y.
	 * @param vida  Lifepoints.
	 * @param aR  Attack radius.
	 * @param aP  Attack power.
	 * @param mov  Movement capacity.
	 */
	public Enemy(int X, int Y, int vida, int aR, int aP, int mov){
		posX=X;
		posY=Y;
		life=vida;
		attackRadius=aR;
		attackPower=aP;
		movement =mov;
		
//		valorGoal=-1;
		goal=new int[2];
		goal[0]=-1; //x
		goal[1]=-1; //y
		
	}
	
	/**
	 * Default constructor
	 */
	public Enemy(){}
	
	public String mostrar(){
		return "E";
	}
	
}
