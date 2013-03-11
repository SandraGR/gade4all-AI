package com.uc3m.gade4all.game;

/**
 * Class that represents an element.
 * 
 * @author Sandra Garcia Rodriguez
 */
public abstract class Element {

	/** Coordinate X position */
	public int posX;
	
	/** Coordinate Y position */
	public int posY;
	
	/** To paint on screen */
	char mostrar=' ';

	/**
	 * Constructor by a Element object
	 * 
	 * @param x  Coordinate X.
	 * @param y  Coordinate Y.
	 */
	public Element(int x, int y){
		posX=x;
		posY=y;
	}
	
	/**
	 * Default constructor
	 */
	public Element(){}
	
	/**
	 * To represent the kind of element
	 * 
	 * @return Symbol to paint the element.
	 */
	public String mostrar(){
		return "X";
	}
	
}
