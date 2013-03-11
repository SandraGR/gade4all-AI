package com.uc3m.gade4all.game;

import java.util.Vector;

import com.uc3m.gade4all.AI.PathFinder.Node;

/**
 * Class that represents the scenario of the game (board).
 * 
 * @author Sandra Garcia Rodriguez
 */
public class Tablero {
	
	/** Map width */
	public int map_width;
	/** Map heigh */
	public int map_height;
	
	/** List of the special cells */
	public Vector<Cell> specialCells;
	/** List of enemies */
	public Vector<Enemy> enemies;
	/** List of players */
	public Vector<Character> players;
	/** List of obstacles */
	public Vector<Obstacle> obstacles;
	
	/** Map pf the scenario */
	public Element [][] board;
	
	/**
	 * Constructor by a Tablero object. 
	 * Initialize the board and fill in it with the elements passed by arguments.
	 * 
	 * @param width  Map width
	 * @param height  Map heigh
	 * @param specialC  List of the special cells
	 * @param enemigos  List of the special enemies
	 * @param juagadores  List of the special players
	 * @param obstaculos  List of the special obstacles
	 */
	public Tablero(int width, int height, 
    		Vector<Cell> specialC, Vector<Enemy> enemigos, Vector<Character> juagadores, 
    		Vector<Obstacle> obstaculos){		
		
		    map_width = width;
		    map_height= height;
		    specialCells = specialC; 
			enemies = enemigos;
			players = juagadores;
			obstacles = obstaculos;
			
			board = new Element[width][height];
			//initialized at null
			board = new Element[map_width][map_height];
			for(int i=0; i<board.length;i++){
				for(int j=0; j<board[i].length;j++){
					board[i][j]=null;
				}
			}
						
			//Fill in the board
			Cell aux;
			for(int i=0; i<specialCells.size();i++){
				aux=specialCells.get(i);
				if(aux!=null){
					board[aux.posY][aux.posX]=aux;
				}
			}
			
			Character c;
			for(int i=0; i<players.size();i++){
				c=players.get(i);
				if(c!=null){
					board[c.posY][c.posX]=c;
				}
			}
			
			Enemy e;
			for(int i=0; i<enemies.size();i++){
				e=enemies.get(i);
				if(e!=null){
					board[e.posY][e.posX]=e;
				}
			}
			
			Obstacle o;
			for(int i=0; i<obstacles.size();i++){
				o=obstacles.get(i);
				if(o!=null){
					board[o.posY][o.posX]=o;
				}
			}
			
			
	}
	
	/**
	 * Default constructor
	 */
	public Tablero(){ 
		 int map_width = 8;
		    int map_height= 8;
		    specialCells = new Vector<Cell>(); 
			enemies = new Vector<Enemy>();
			players = new Vector<Character>();
			obstacles = new Vector<Obstacle>();
			
			board = new Element[map_width][map_height];
			for(int i=0; i<board.length;i++){
				for(int j=0; j<board[i].length;j++){
					board[i][j]=null;
				}
			}
	}
	

	 /**
	 * Perform an attack action
	 *
	 * @param powerAttack Attack power.
	 * @param x  Coordinate X.
	 * @param y  Coordinate Y.
	 */
	public void atacar(int powerAttack, int x, int y){
		if(board[y][x]!=null){
			if(board[y][x] instanceof Character){
				System.out.println(powerAttack+"  "+((Character)board[y][x]).life);
				((Character)board[y][x]).life-=powerAttack;
				if(((Character)board[y][x]).life<=0){
					board[y][x]=null;
					players.remove(board[y][x]);
				}			
			}else if(board[y][x] instanceof Enemy){
				System.out.println(powerAttack+"  "+((Enemy)board[y][x]).life);
				((Enemy)board[y][x]).life-=powerAttack;
				if(((Enemy)board[y][x]).life<=0){
					board[y][x]=null;
					enemies.remove(board[y][x]);
				}
			}
		}
	}
	
	
	/**
	 * Update position of the enemy on the board
	 *
	 * @param currentEnemy Current enemy.
	 * @param newX  New coordinate X.
	 * @param newY  New coordinate Y.
	 */
	public boolean changePositionElement(Element currentEnemy, int newX, int newY){
		if(board[currentEnemy.posY][currentEnemy.posX]==null)
			return false;
		board[currentEnemy.posY][currentEnemy.posX]=null;
		currentEnemy.posX=newX;
		currentEnemy.posY=newY;
		board[newY][newX]=currentEnemy;
		return true;
	}
	
	/**
	 * Calculate feasible cells to move the element
	 * 
	 * @param x  Coordinate X.
	 * @param y  Coordinate Y.
	 * @param movement Movement power
	 * 
	 * @return list-of-cells List of positions where the element in position (x,y) can move
	 */
	public Vector<Cell> possibleToMove(int x, int y, int movement){
		Vector<Cell> opciones = new Vector<Cell>();
		int newX,newY;
		for (int movX=-movement; movX<=movement; movX++) {
			for (int movY=-movement; movY<=movement; movY++) {
				if( !(movX==0 && movY==0) ){
					newX=x+movX;
					newY=y+movY;
					if(!ocupada(newX,newY)){ //if feasible position
						opciones.add(new Cell(newX, newY));
					}
				}
			}
		}//for

		
		return opciones;
	}
		
	
	/**
	 * Calculate feasible elements that the enemy can attack
	 * 
	 * @param e Current enemy
	 * 
	 * @return list-of-elements List of elements that the enemy can attack
	 */
	public Vector<Element> possibleToAttack(Enemy e){
		Vector<Element> atacables = new Vector<Element>();
		int x = e.posX;
		int y = e.posY;	
		int newX,newY;		
		for (int movX=-e.attackRadius; movX<=e.attackRadius; movX++) {
			for (int movY=-e.attackRadius; movY<=e.attackRadius; movY++) {
				if( !(movX==0 && movY==0) ){
					newX=x+movX;
					newY=y+movY;
					
					if( newX>=0 && newX<board[0].length && newY>=0 && newY<board.length){						
						if (board[newY][newX] instanceof Character){//si la casilla en un player 
							atacables.add(board[newY][newX]);	
						}else if ( (board[newY][newX] instanceof Cell) && ( (Cell)board[newY][newX]).type==2){//si es una celda atacable
							atacables.add(board[newY][newX]);		
						}
					}
				}
			}
		}//for
		
		return atacables;
	}
	

	/**
	 * Say if enemy can attack to position (enX,enY).
	 * 
	 * @param e Current enemy
	 * @param enX  Coordinate X.
	 * @param enY  Coordinate Y.
	 * 
	 * @return boolean True: yes. False: no
	 */
	public boolean canAttackTo(Enemy e,int enX, int enY){
		int x = e.posX;
		int y = e.posY;
		boolean ok=false;	
		int newX,newY;
		for (int movX=-e.attackRadius; movX<=e.attackRadius && !ok; movX++) {
			for (int movY=-e.attackRadius; movY<=e.attackRadius && !ok; movY++) {
				if( !(movX==0 && movY==0) ){
					newX=x+movX;
					newY=y+movY;
					if( newX==enX && newY==enY){
						ok=true;
					}
				}
			}
		}

		return ok;
	}
	
	
	
	
	/**
	 * Calculate the number of players that can be attacked from position (x,y) with the attack radius "ataque"
	 * 
	 * @param x  Coordinate X.
	 * @param y  Coordinate Y.
	 * @param ataque Attack radius
	 * 
	 * @return number-of-players Number of players that are reached
	 */
	public int numPlayersToAttack(int x, int y, int ataque){
		int cuantos=0;			
		int newX,newY;
		for (int movX=-ataque; movX<=ataque; movX++) {
			for (int movY=-ataque; movY<=ataque; movY++) { //not to be the same
				if( !(movX==0 && movY==0) ){
					newX=x+movX;
					newY=y+movY;
					if( newX>=0 && newX<board[0].length && newY>=0 && newY<board.length){						
						if (board[newY][newX] instanceof Character){//if there is a player 
							cuantos++;
						}else if ( (board[newY][newX] instanceof Cell) && ( (Cell)board[newY][newX]).type==2){//if this cell can be attacked
							cuantos++;
						}
					}
				}
			}
		}

		return cuantos;
	}
	

	/**
	 * Calculate the number of players that attack to the element at position (enX,enY).
	 * 
	 * @param enX  Coordinate X.
	 * @param enY  Coordinate Y.
	 * 
	 * @return sum-of-attack  The sum of all the attack powers
	 */
	public int numAtacantes(int enX, int enY){
		int cuantos=0;
		Character c;
		int x,y;
		for(int i=0; i<players.size();i++){
			c=players.get(i);
			x=players.get(i).posX;
			y=players.get(i).posY;
			
			int newX,newY;
			for (int movX=-c.attackRadius; movX<=c.attackRadius; movX++) {
				for (int movY=-c.attackRadius; movY<=c.attackRadius; movY++) {
	//				cont++;
					if( !(movX==0 && movY==0) ){
						newX=x+movX;
						newY=y+movY;
						if( newX==x && newY==y){
							//cuantos++;
							cuantos+=c.attackPower;
						}
					}
				}
			}

		}
		return cuantos;
	}
	
	//Dice si (x,y) esta ocupada por alguien
	/**
	 * Say if cell in position (x,y) is occupied
	 * 
	 * @param x  Coordinate X.
	 * @param y  Coordinate Y.
	 * 
	 * @return boolean True: yes. False: no
	 */
	public boolean ocupada(int x, int y){
		if(x>=0 && x<board[0].length && y>=0 && y<board.length){
			if(board[y][x]==null){
				return false;
			}else{
				if(board[y][x] instanceof Cell &&  ( (Cell) board[y][x]).type==3 ){ //there is a special cell that can be occupied
					return false;
				}else{
					return true;
				}
			}
		}else
			return true; //it cannot go there
	}
	
	/**
	 * Calculate the number of close enemies at a specific distance
	 * 
	 * @param e Current enemy
	 * @param distancia Distance
	 * 
	 * @return number-of-close-friends Number of enemies that are at less than a distance
	 */
	public int numCloseFriends(Enemy e, int distancia){
		int cuantos=0;
		for(int i=0;i<enemies.size();i++){
			if(enemies.get(i).posX!= e.posX && enemies.get(i).posY!=e.posY){//no es el mismo
				if( Math.abs(enemies.get(i).posX-e.posX) < distancia && Math.abs(enemies.get(i).posY-e.posY) < distancia ){ //son cercanos
					cuantos++;
				}
			}
		}
		return cuantos;
	}
	

	/**
	 * Paint on screen 
	 */
	public void pintar(){
		
		System.out.print("   ");
		for(int j=0; j<board[0].length;j++){
			System.out.print(" "+j);
		}
		System.out.println();
		for(int i =0; i<board.length;i++){
			System.out.print(" "+i+"  ");
			for(int j=0; j<board[0].length;j++){
				if(board[i][j]==null){
					System.out.print("- ");
				}else{
					System.out.print(""+board[i][j].mostrar()+" ");
				}
			}
			System.out.print("  "+i);
			System.out.println();
		}
		System.out.print("   ");
		for(int j=0; j<board[0].length;j++){
			System.out.print(" "+j);
		}
		System.out.println();
		
	}
	
	
/*	public boolean canAttack(Enemy e, int x, int y){
		if( ((e.posY-e.attackRadius)>=x) && ((e.posY+e.attackRadius)<=x) && ((e.posX-e.attackRadius)>=y) 
		&& ((e.posX+e.attackRadius)<=y) ){ 
				if( board[y][x] instanceof Character){ //hay un jugador en esa casilla
					return true;
				}
		}	
		return false;
	}
	
	public void attack(Character c){
		c.life--;
	}
	
	//true si se pudo a–adir, false en caso contrario
	public boolean addElement(Element e){
		if(board[e.posY][e.posX]==null){ //si la posicion esta vacia
			board[e.posY][e.posX]=e;
			if(e instanceof Character){
				players.add((Character)e);
			}else if(e instanceof Obstacle){
				obstacles.add((Obstacle)e);
			}else if(e instanceof Enemy){
				enemies.add((Enemy)e);
			}
			return true;
		}else{
			return false;
		}		
	}
	
	//devuelve true si lo encontro y lo elimino, false en caso contrario
	public boolean removeElement(Element e){
		for(int i =0; i<board.length;i++){
			for(int j=0; j<board[0].length;j++){
				if(e instanceof Enemy){
					return enemies.removeElement(e);
				}else if (e instanceof Cell){
					return specialCells.removeElement(e);
				}else if (e instanceof Character){
					return players.removeElement(e);
				}else if (e instanceof Obstacle){
					return obstacles.removeElement(e);
				}else{
					return false;
				}
			}
		}
		return false;
	}
*/			
	

	
	/*	//calcula posibles casillas a las que se puede mover
	public Vector<Element> cellsToReach(Enemy e){
		Vector<Element> lista = new Vector<Element>();
		
		for(int i=(e.posY-e.movement); i<=(e.posY-e.movement);i++){
			if(i>=0 && i<board.length){ //xa q no se salga del tablero
				for(int j=(e.posX-e.movement); j<=(e.posX-e.movement);j++){	
					if(j>=0 && j<board[i].length){	//xa q no se salga del tablero	
						if(board[i][j]==null){ //celda libre
							lista.add(new Cell(i,j,4)); //solo para indicar las casillas a las que se puede avanzar
						}
					}//if
				}
			}//if
		}
		
		return lista;
	}*/
	
/*	//True si puede avanzar a esa casilla, false en caso contrario
	public boolean isPossible(Enemy e, int x, int y){
		if( ((e.posY-e.movement)>=x) && ((e.posY+e.movement)<=x) && ((e.posX-e.movement)>=y) 
		&& ((e.posX+e.movement)<=y) ){ 
				if( board[y][x]==null){
					return true;
				}
		}	
		return false;
	}*/
	
	/*	//actualiza la nueva posicion si existia el elemento
	public boolean updatePosition(Element e, int newX, int newY){
		if(board[e.posY][e.posX]!=null){
			board[e.posY][e.posX]=null;
			board[newX][newY]=e;
			e.posY=newX;
			e.posX=newY;
			
			return true;
		}else{
			return false;
		}
	}

	public boolean canMoveTo(int y, int x){
		if(board[y][x]==null){
			return true;
		}else if(board[y][x] instanceof Cell && ((Cell)(board[y][x])).type==3){ //si es una celda especial pero se puede ocupar
			return true;
		}else{
			return false;
		}
	}*/
	
	/*	public Element getElement(int x, int y){
	return board[y][x];
}
*/
		
}// fin de clase (Saved by the bell)
