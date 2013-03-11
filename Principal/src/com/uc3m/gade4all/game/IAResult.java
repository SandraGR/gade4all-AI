package com.uc3m.gade4all.game;

/**
 * Class that represents the action to perform.
 * 
 * @author Sandra Garcia Rodriguez
 */
public class IAResult {
	
	/** Coordinate X of goal / objective */
	public int posX;
	
	/** Coordinate Y of goal / objective*/
	public int posY;
	
	/** Kind of action to perform: 1 MOVEMENT, 2 ATTACK*/
	public int type; // 1 MOVEMENT, 2 ATTACK
	
	/**
	 * Constructor by a IAResult object
	 * 
	 * @param x  Coordinate X.
	 * @param y  Coordinate Y.
	 * @param t  Kind of action to perform: 1 MOVEMENT, 2 ATTACKLife points.
	 */
	public IAResult(int x, int y, int t){
		posX=x;
		posY=y;
		type=t;
		
		if(type==1)
			System.out.println("MOVEMENT to x="+posX+" y="+posY);
		else
			System.out.println("ATTACK to x="+posX+" y="+posY);
	}

}
