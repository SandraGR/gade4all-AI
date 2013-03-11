package com.uc3m.gade4all;

import java.util.List;
import java.util.Vector;

import com.uc3m.gade4all.R;
import com.uc3m.gade4all.AI.PathFinder;
import com.uc3m.gade4all.AI.PathFinder.Node;
import com.uc3m.gade4all.game.Cell;
import com.uc3m.gade4all.game.Character;
import com.uc3m.gade4all.game.Element;
import com.uc3m.gade4all.game.Enemy;
import com.uc3m.gade4all.game.IAResult;
import com.uc3m.gade4all.game.Obstacle;
import com.uc3m.gade4all.game.Tablero;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;



/*   
 * Specific game application
 * 
 * Sandra Garcia Rodriguez <sandragrodriguez@gmail.com>
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * 
 * Part I of the AI module. This is the main code to generate different 
 * scenario implementations. Regarding the model game rules (showed below), 
 * the AI procedure decides the best actions for each enemy regarding the players, 
 * obstacles, life points, specials cells and other factors that can influence on 
 * the choosing of the right decision. 
 *    
 * This code also implements a first approach to test the AI module 
 *  (set parameters in method test() ).
 *  
 */
 
/**
 * Main class. Specific game application
 * 
 * @author Sandra Garcia Rodriguez
 */
public class Principal extends Activity {
	
	Vector<Character> players = new Vector<Character>();
	Vector<Enemy> enemies = new Vector<Enemy>();
	Enemy currentEnemy;
    int map_width = 8;
    int map_height = 8;
    Vector<Cell> specialCell = new Vector<Cell>(); 
	Vector<Obstacle> obstacles = new Vector<Obstacle>();
	Tablero board;
	int gameLevel=0; // Current level (frome 0 to 10)
	
	int contadorEnemies=0; 
	int contadorPlayers = 0; 
	
	int MAX_LEVEL=10; //Max game level
 
    
	
	/* PART I of AI MODULE
	 * uses classes (part II) of com.uc3m.gade4all.AI
	 * 
	 * */
	
    /**
	 * Main AI decision method
	 *
	 * @param currentEnemy Current enemy to perform
	 * @param map_width Width of the current scenario
	 * @param map_height Heigh of the current scenario
	 * @param specialCell Special cell in the current scenario
	 * @param enemies NPCs in the current scenario
	 * @param players Players in the current scenario
	 * @param obstacles Obstacles in the current scenario
	 * @param difficulty Level
	 * @param tablero The current board.
	 * 
	 * @return resultados Actions performed
	 */
    public Vector<IAResult> generatelAActions(Enemy currentEnemy, int map_width, int map_height, 
		Vector<Cell> specialCell, Vector<Enemy> enemies, Vector<Character> players, 
		Vector<Obstacle> obstacles, int difficulty, Tablero tablero){	
    	
    	Vector<IAResult> resultados = new Vector<IAResult>();  
    	Cell celda=null;
    	
    	/* LEVELS CONTROL:
    	 *  Movement, the highest the level, the less probability to perform a random movement. */
    	int aleatorio=(int)(Math.random()*(MAX_LEVEL+1)); //set movement for the level
    	if( aleatorio < (MAX_LEVEL-gameLevel)){ //random
    	    System.out.println("random");
    		moverAleatorio(resultados,tablero);

    	}else{
	    	int [] goal = setGoal(currentEnemy,tablero);	    
	    	
	    	if(specialCell.size()>0) celda=specialCell.get(0);
	    	
			//MOVE?
			if( !(currentEnemy.posX == goal[0] && currentEnemy.posY==goal[1])){ //if already in goal, do NOT move
				
				long begin = System.currentTimeMillis();
		
				/* Call the pathfinding Astar */ 		
				PathFinder camino = new PathFinder(tablero, currentEnemy, goal);
				List<Node> nodes = camino.compute(new PathFinder.Node(currentEnemy.posX, currentEnemy.posY));
			
				if(nodes!=null){
					int x=nodes.get(1).x;
					int y=nodes.get(1).y;
			
					if( !(nodes.get(0).x == nodes.get(nodes.size()-1).x && nodes.get(0).y== nodes.get(nodes.size()-1).y) ){ //no es la meta
						if(!tablero.ocupada(x,y)){
							resultados.add(new IAResult(x,y,1));//movement	
							tablero.changePositionElement(currentEnemy,x, y);
						}
					}
				}
				
				/* Count and show time used by the Astar algorithm to solve the path finder*/
				long end = System.currentTimeMillis();
		
				System.out.println("Time = " + (end - begin) + " ms" );
				System.out.println("Expanded = " + camino.getExpandedCounter());
				System.out.println("Cost = " + camino.getCost());
				
				if(nodes == null)
						System.out.println("No path");
				else{
						System.out.print("Path = ");
						for(Node n : nodes)
								System.out.print(n);
						System.out.println();
				}

			}//end if MOVE?
    	}

   	
     	/* LEVELS CONTROL: ATTACK?
    	 *  the highest the level, the less probability to perform a random attack. */
    	aleatorio=(int)(Math.random()*(MAX_LEVEL+1));
    	if( aleatorio < (MAX_LEVEL-gameLevel)){ //random
    	    System.out.println("random");
    		atacarAleatorio(resultados,tablero);
    		
    	}else{
			boolean stop=false;
			/* if there is a cell (type attack) at the attack radius --> attack it */
			if(celda != null && celda.type==2 && board.canAttackTo(currentEnemy,celda.posX,celda.posY)){ 
				resultados.add(new IAResult(celda.posX,celda.posY,2));
				System.out.println("especial");
			}else{
				/* attack a player at the attack radius giving preference to those ones that can be eliminated directly */
				Vector<Element> aTiro=tablero.possibleToAttack(currentEnemy);
				for(int i=0; i<aTiro.size() && !stop;i++){
					if( (aTiro.get(i) instanceof Character) && ( (Character)aTiro.get(i)).life - currentEnemy.attackPower <=0 ){ //attack if we can kill it
						resultados.add(new IAResult((aTiro.get(i)).posX,(aTiro.get(i)).posY,2));//attack
						stop=true;
						
						board.atacar(currentEnemy.attackPower,(aTiro.get(i)).posX,(aTiro.get(i)).posY);
					}
				}
				if(!stop && aTiro.size() > 0){
					int elegido=(int)(aTiro.size()*Math.random()); //to provide a bit of randomness to the decisions
					resultados.add(new IAResult((aTiro.get(elegido)).posX,(aTiro.get(elegido)).posY,2));//attack
					
					board.atacar(currentEnemy.attackPower,(aTiro.get(elegido)).posX,(aTiro.get(elegido)).posY);
				}
				
			}
    	}//if
		
		tablero.pintar(); 	
		System.out.println();

   	
    	return resultados;
    }
    
    /**
	 * Perform a random movement
	 *
	 * @param resultados Actions
	 * @param tablero The current board.
	 */
    private void moverAleatorio(Vector<IAResult> resultados, Tablero tablero){
		Vector<Cell> opciones = tablero.possibleToMove(currentEnemy.posX,currentEnemy.posY,currentEnemy.movement);
		int elegido=(int)((opciones.size()+2)*Math.random()); //choose a feasible position to reach. +2 to consider the no-move mode too
		if(elegido < opciones.size()){ //if "elegido" is in a move mode
			resultados.add(new IAResult((opciones.get(elegido)).posX,(opciones.get(elegido)).posY,1));//move
			tablero.changePositionElement(currentEnemy,(opciones.get(elegido)).posX, (opciones.get(elegido)).posY);
		}

    }
    
    /**
	 * Random attack
	 *
	 * @param resultados Actions
	 * @param tablero The current board.
	 */
    private void atacarAleatorio(Vector<IAResult> resultados, Tablero tablero){
    	Vector<Element> aTiro=tablero.possibleToAttack(currentEnemy);
		int elegido=(int)((aTiro.size()+2)*Math.random()); //choose a player on target. +2 to consider the no-attack too
		if(elegido < aTiro.size()){ //if elegido is in an attack mode
			resultados.add(new IAResult((aTiro.get(elegido)).posX,(aTiro.get(elegido)).posY,2));//attack
			
			board.atacar(currentEnemy.attackPower,(aTiro.get(elegido)).posX,(aTiro.get(elegido)).posY);
		}
		
    }
    
    
    /**
	 * Decide the goal (cell) to reach. Redefine for each specific case.
	 *
	 * @param currentEnemy The current enemy to perform.
	 * @param tablero The current board.
	 * @return goal Coordinates y and x of the goal to reach
	 */
    private int[] setGoal(Enemy currentEnemy, Tablero tablero){
    	int goal[] = new int[3];

	   	double nota,valor;
    	int x,y;    
    	double A, B, C, D;
    	Cell celda;
    	
		if( (tablero.specialCells).size()>0){ //there is a special cell objective
    		celda=(tablero.specialCells).get(0);
			x=(tablero.specialCells.get(0)).posX;
			y=(tablero.specialCells.get(0)).posY;
			goal[0]=x;
			goal[1]=y;
		
			if( celda.type==3){//OCCUPY
				goal[2]=(tablero.specialCells.get(0)).type;//kind of special cell
			}else if(celda.type==1){ //DEFEND look for the best position that is around the currentEnemy
				goal[2]=-1;
				A=0.5;
				B=0.2; //number of player that can attack it
				C=0.2;
				D=0.1;
				nota=valoraPosicion(currentEnemy.goal[0], currentEnemy.goal[1], A,B,C,D);				
				int newX,newY;
				for (int movX=-1; movX<=1; movX++) {
					for (int movY=-1; movY<=1; movY++) {
						if( !(movX==0 && movY==0) ){
							newX=x+movX;
							newY=y+movY;
							if( nota > (valor=valoraPosicion(newX, newY, A,B,C,D) ) ){ //is a feasible position
								nota=valor;
								goal[0]=newX;
								goal[1]=newY;
							}
						}
					}
				}//for
				//if it is not possible to occupy any position which is around the special cell,it looks for the way to this position (common alg.)
			}else{//ATTACK look for the best cell that is around
				goal[2]=-1;
				A=0.5;
				B=0.1;
				C=0.3; //players to attack
				D=0.0;
				nota=valoraPosicion(currentEnemy.goal[0], currentEnemy.goal[1], A,B,C,D);
				int newX,newY;
				for (int movX=-1; movX<=1; movX++) {
					for (int movY=-1; movY<=1; movY++) {
						if( !(movX==0 && movY==0) ){
							newX=x+movX;
							newY=y+movY;
							if( nota > (valor=valoraPosicion(newX, newY, A,B,C,D) ) ){ //is a feasible position
								nota=valor;
								goal[0]=newX;
								goal[1]=newY;
							}
						}
					}
				}//for
	
			}//if-else
			
		}else{ //there is no objective, ATTACK to players and DEFEND
			goal[0]=currentEnemy.posX; //if it does not find anyone around, si no encuentra al rededor, que se situe cerca
			goal[1]=currentEnemy.posY;
			A=0.2;
			B=0.2;
			C=0.5; //players a atacar
			D=0.1;
			
			goal[0]=(tablero.players.get(0)).posX; //if no position around found, stay close
			goal[1]=(tablero.players.get(0)).posY;
    		for(int i=0; i<tablero.players.size();i++){
    			x=tablero.players.get(i).posX;
    			y=tablero.players.get(i).posY;
    			nota=valoraPosicion(x,y, A,B,C,D);	
    			
				int newX,newY;
				for (int movX=-1; movX<=1; movX++) {
					for (int movY=-1; movY<=1; movY++) {
						if( !(movX==0 && movY==0) ){
							newX=x+movX;
							newY=y+movY;
							if( nota > (valor=valoraPosicion(newX, newY, A,B,C,D) ) ){ //is a feasible position
								nota=valor;
								goal[0]=newX;
								goal[1]=newY;
							}
						}
					}
				}//for
			} //for

    	} // if-else
  //  	currentEnemy.valorGoal=nota;
    	currentEnemy.goal[0]=goal[0]; //X
    	currentEnemy.goal[1]=goal[1]; //Y
    	
      System.out.println("Goal: X="+goal[0]+" Y="+goal[1]);
    	return goal;
    }
    
  
    /**
	 * Values the fitness of the cell (y,x). MINIMIZE
	 *
	 * @param x coordinate
	 * @param y coordinate
	 * @param A weight
	 * @param B weight
	 * @param C weight
	 * @param D weight
	 * @return double Fitness value
	 */
    public double valoraPosicion(int x, int y, double A, double B, double C, double D){
    	double nota=0;
    	//can move?
    	if( !board.ocupada(x,y) ){
    		nota = Integer.MIN_VALUE;
    	}else{
   		//distance to position (Euclidean)
    	double distancia = Math.sqrt(Math.pow( (x - currentEnemy.posX),2) + Math.pow( (y - currentEnemy.posY),2) );
    	
    	//level of danger of the player (players around it that can attack it)
    	double peligro = board.numAtacantes(x,y);
    	
    	//number of possible players to attack
    	int ataque = (board.players).size() - board.numPlayersToAttack(x,y,currentEnemy.attackRadius);
    	
    	//near NPCs that can protect it
    	int amigosCercanos=(board.enemies).size() - board.numCloseFriends(currentEnemy,2);
    
    	nota = A*distancia+B*peligro+C*ataque+D*amigosCercanos;
    	}
    	    	
    	return nota;
    }
    
  
    /* INTERFACE AND SCENARIO SPECIFICATIONS  
     * remove if neccesary
     * 
     * */
    
    
    /** Set scenario with elements and other specifications 
     */
    public void test(){	
    	
    	gameLevel = 10; //MAX_LEVEL;	
        map_width = 10;
        map_height = 10;

    //    specialCell.add(new Cell(9,9,3)); //special cell en x=4 y =4

		enemies.add(new Enemy(5,1,20,2,2,1)); // x=0 y=2
		enemies.add(new Enemy(4,7,20,4,2,2));	// x=0 y=4		

		players.add(new Character(3,1,20,4,2,2));
		players.add(new Character(5,3,20,4,2,3));
		players.add(new Character(8,8,20,4,2,4));
		

		obstacles.add(new Obstacle(2,0)); // x=2 y=0
		obstacles.add(new Obstacle(4,6)); // x=2 y=3
		
		board= new Tablero( map_width, map_height, specialCell, 
        		enemies, players, obstacles);   
		
	//	board.board[specialCell.get(0).posY][specialCell.get(0).posX]=null;
		
		System.out.println("START");
		board.pintar();
	//	generatelAActions(enemies.get(contador), map_width, map_height, specialCell,enemies, players, obstacles, difficulty, board);

    }
    
    /** Interface actions
     * 
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
      
        test();//create new scenario
       
    }

    /**  Interface actions
     * 
     * @param menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_principal, menu);
        return true;
    }

    /** Method used to pass the turn to the next element to play (interface actions)
     * 
     * @param view
     */
    public void jugarSiguiente(View view){
    	contadorPlayers=0;
    	
		currentEnemy=enemies.get(contadorEnemies);
		generatelAActions(currentEnemy, map_width, map_height, specialCell,enemies, players, obstacles, gameLevel, board);
		
		System.out.println("\n\n");
		
		contadorEnemies++;
		contadorEnemies=contadorEnemies%enemies.size();
		if(contadorEnemies==0){
			moverPlayers();	
		}
    }
    
    
    /** Method used to perform the movement ordered by the user (interface actions)
     * 
     */
    private void moverPlayers(){ 
    	Vector<Cell> opciones=null;
    	Character c=players.get(contadorPlayers);
    	String s="";
	
		opciones=board.possibleToMove(c.posX, c.posY, c.movement);
		
		s+="PLAYER: "+c.posX+","+c.posY+" \n Mov.: "+c.movement+" --> ";
    	for(int j=0; j<opciones.size();j++){
    		s+="("+opciones.get(j).posX+","+opciones.get(j).posY+")\t";
    	}
    	s+="\n Ataque: "+c.attackRadius+" --> ";        	
    	
		int x = c.posX;
		int y = c.posY;	
		int newX,newY;		
		for (int movX=-c.attackRadius; movX<=c.attackRadius; movX++) {
			for (int movY=-c.attackRadius; movY<=c.attackRadius; movY++) {
				if( !(movX==0 && movY==0) ){
					newX=x+movX;
					newY=y+movY;
					
					if( newX>=0 && newX<board.board[0].length && newY>=0 && newY<board.board.length){						
						if (board.board[newY][newX] instanceof Enemy){//if a player is in this position 
							s+="("+newX+","+newY+")\t";	
						}else if ( (board.board[newY][newX] instanceof Cell) && ( (Cell)board.board[newY][newX]).type==2){//if this cell can be attacked
							s+="("+newX+","+newY+")\t";	
						}
					}
				}
			}
		}//for
		s+="\n";    	
    	
    	System.out.println(s);
    	
    	CheckedTextView mensaje = (CheckedTextView)findViewById(R.id.mostrador);
    	mensaje.setText(s);
    	    	
    }
    
    /** Method used to perform the movement ordered by the user (interface actions)
     * 
     * @param view
     */
    public void moverPersonaje(View view){
    	
    	try{
	    	EditText valorX = (EditText)findViewById(R.id.valorX);
	    	int x = Integer.parseInt(valorX.getText().toString());
	    	
	    	EditText valorY = (EditText)findViewById(R.id.valorY);
	    	int y = Integer.parseInt(valorY.getText().toString());
	    	
	    	if(x>=0 && y>=0){
	    		board.changePositionElement(players.get(contadorPlayers),x,y); //update position on board
	    	}
	    	
	    	board.pintar();  
    	}catch(NumberFormatException e){
    		e.printStackTrace();
    	}
    	
    }
    
    
    /** Method used to perform the attack ordered by the user (interface actions)
     * 
     * @param view
     */
    public void atacarPersonaje(View view){
    	try{
	    	EditText valorX = (EditText)findViewById(R.id.valorX);
	    	int x = Integer.parseInt(valorX.getText().toString());
	    	
	    	EditText valorY = (EditText)findViewById(R.id.valorY);
	    	int y = Integer.parseInt(valorY.getText().toString());
	    	
	    	if(x>=0 && y>=0){
	    		board.atacar(players.get(contadorPlayers).attackPower,x,y); //update position on board
	    	}
	    	
	    	board.pintar();  
    	}catch(NumberFormatException e){
    		e.printStackTrace();
    	}
    }
    
    /** Method used to pass the turn to the next player to play (interface actions)
     * 
     * @param view View
     */
    public void nextPersonaje(View view){
    	contadorPlayers++;
    	contadorPlayers=contadorPlayers%(players.size()); 
    	moverPlayers();
    }
    
    /** Finish (interface actions)
     * 
     * @param view View
     */
    public void fin(View view){
    	finish();
    }
    
    
    
}
