package com.uc3m.gade4all.AI;

import java.util.*;

import com.uc3m.gade4all.game.Cell;
import com.uc3m.gade4all.game.Element;
import com.uc3m.gade4all.game.Enemy;
import com.uc3m.gade4all.game.Tablero;


/**
 * Pathfinder algorithm specification.
 * 
 * Part II of AI module.
 * 
* Path finder adapted to a specific kind of game.It uses Astar algorithm, 
* but it any other pathfinder algorithm may be used too.
 * 
 * @author Sandra Garcia Rodriguez
 */
public class PathFinder extends Astar<PathFinder.Node>{
	
	/** Current scenario */
	Tablero board;
	
	/** Current enemy */
	Enemy currentEnemy;
	
	/** Map of the current scenario */
 	private Element[][] map;
 		
 	/** Coordinate X of the goal position */
 	int goalX;
 	
 	/** Coordinate X of the goal position */
 	int goalY;
 	
 	/** Way to reach the goal: 0-stay on it. 1-stay close */
 	int modo; 
 	
 	/** Function weight G */
	double G=0.4;
	/** Function weight H */
	double H=0.4;
	/** Function weight E */
	double E=0.1;
	/** Function weight D */
	double D=0.1; 
	
	/** Radius of movement */
 	int MOVEMENTS=1;

 		/** Class Node
 		 * used by the path finder
 		 */
		public static class Node{
				public int x;
				public int y;
				
				/**
				 * Constructor by coordinates
				 * 
				 * @param x coordinate
				 * @param y coordinate
				 */
				public Node(int x, int y){
						this.x = x; 
						this.y = y;
				}
				
				/**
				 * Method toString
				 * 
				 * @return string Coordinates of the node
				 */
				public String toString(){
					return "(" + x + ", " + y + ") ";
				} 
		} // end class Node

		/**
		 * Constructor by map
		 * 
		 * @param map Board with cells
		 */
		public PathFinder(Element[][] map){
			this.map = map;
		}
		
		/**
		 * Constructor by board, enemy and goals
		 * 
		 * @param t Board 
		 * @param e Current enemy
		 * @param goal Coordinates y and x to reach
		 */
		public PathFinder(Tablero t, Enemy e, int [] goal){
			this.map = t.board;
			board=t;
			MOVEMENTS=e.movement;
			currentEnemy=e;
			if(goal[0]!=-1){ //special cell exists
			 	goalX = goal[0];
			 	goalY = goal[1];
			 	modo=goal[2];
			}else{
				goalX = map[0].length - 1;
			 	goalY = map.length - 1;
			 	modo=-1;
			}
		}
		
			
		protected boolean isGoal(Node node){
			return (node.x == goalX) && (node.y == goalY);
		}
		
		/**
		 * Cost for the operation to go to <code>to</code> from
		 * <code>from</from>
		 *
		 * @param from The node we are leaving.
		 * @param to The node we are reaching.
		 * @return The cost of the operation. 0 if from==to; 1 if from "from" can move to "to"; Double.MAX_Value otherwise
		 */
		protected Double g(Node from, Node to){
				if(from.x == to.x && from.y == to.y)
					return 0.0;
				if(canMoveTo(to.y, to.x))
					return 1.0;			
				
				return Double.MAX_VALUE; //avoids stepping on obstacles
		}

		/**
		 * Heuristic function to calculate the estimated cost to reach a goal node.
		 * Choose between euclidean and Manhatan distance (Redefine for each specific game)
		 * <code>from</from>.
		 *
		 * @param from The node we are leaving.
		 * @param to The node we are reaching.
		 * @return The estimated cost to reach an object.
		 */
		protected Double h(Node from, Node to){
				/* Use the Manhattan distance heuristic.  */
			//	return new Double(Math.abs(map[0].length - 1 - to.x) + Math.abs(map.length - 1 - to.y)); //uncomment if neccesary
			
			/* Use the Euclidean distance heuristic.  */
			return new Double( Math.sqrt(Math.pow( (map[0].length - 1 - to.x),2) + Math.pow( (map.length - 1 - to.y),2) ) );
			//return new Double( Math.sqrt(Math.pow( (goalX - to.x),2) + Math.pow( (goalY - to.y),2) ) ); //uncomment if neccesary

		}
		
		/**
		 * The total cost can be defined:
		 * 		 g(x) + h(x)
		 * 		with weights: f(x) = A*g(x) + B*h(x) + c*e(x) + d*n(x).
		 * Redefine for each specific game
		 * @param from The node we are leaving.
		 * @param to The node we are reaching.
		 * @return The total cost.
		 */
		protected Double f(Path p, Node from, Node to){
			
				Double g =  g(from, to) + ((p.parent != null) ? p.parent.g : 0.0);
				Double h = h(from, to);
				p.g = g;
				p.f = g + h;
	/* Weighted total cost */						
		//		p.f = G*g + H*h + E*board.numAtacantes(to.x, to.y) + D*board.possibleToAttack(to.x,to.y,currentEnemy.attackRadius);
	
				return p.f;
		}
		
		
		
		/**
		 * Generate the successors for a given node. Redefine for each specific game
		 *
		 * @param node The node we want to expand.
		 * @return A list of possible next steps.
		 */
		protected List<Node> generateSuccessors(Node node){			
			List<Node> ret = new LinkedList<Node>();
			int x = node.x;
			int y = node.y;
			int newX,newY;
			for (int movX=-MOVEMENTS; movX<=MOVEMENTS; movX++) {
				for (int movY=-MOVEMENTS; movY<=MOVEMENTS; movY++) {
					if( !(movX==0 && movY==0) ){
						newX=x+movX;
						newY=y+movY;
						if( newX>=0 && newX<map[0].length && newY>=0 && newY<map.length && canMoveTo(newY,newX)){ //es posicion factible
							ret.add(new Node(x+movX, y+movY));
						}
					}
				}
			}//for

			return ret;
		}
		

		
		/**
		 * Checks if it is possible to move to a cell given by coordinates (y,x). Redefine for each specific game
		 *It controls the board limits. The the cell to reach is already occupied, situate close to it
		 *
		 * @param y coordinate
		 * @param x coordinate
		 * @return boolean (True can move, false cannot)
		 */
		boolean canMoveTo(int y, int x){
			if(map[y][x]==null){
				return true;
			}else{
				if( (map[y][x] instanceof Cell) && ((Cell)(map[y][x])).type==3){ //if it is special cell but it can be occupied
					return true;
				}else if(x==goalX && y==goalY){//if it is the goal, use to calculate the way but never step on it (controlled when IAResul is created)
					return true;
				}else{//if it is occupied by an element that it is not the goal
					return false;
				}
			}
		}

		
}// end class