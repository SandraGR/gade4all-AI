package com.uc3m.gade4all.game;

/**
 * Class that represents an obstacle.
 * 
 * @author Sandra Garcia Rodriguez
 */
public class Obstacle extends Element{
		
	public Obstacle(int x, int y){
		posX=x;
		posY=y;
	}
	
	/**
	 * Default constructor
	 */
	public Obstacle(){}
	
	public String mostrar(){
		return "O";
	}
}
