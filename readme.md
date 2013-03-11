This code has been financed by the project GADE4ALL: Plataforma genérica para facilitar el 
desarrollo de videojuegos y software de entretenimiento multiplataforma, ref. TSI-090302-2011-11


Author:  Sandra Garcia Rodriguez. <sandragrodriguez@gmail.com>
   
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

The package contains an eclipse project that implements an artificial intelligence module applied 
to a strategy game with some specific rules. However, this module can be adapted to different 
games with other rules by modifying some methods of the JAVA files. You are free to reuse the 
code for your own interest.

A Java documentation in “doc” folder is also provided to help the understanding of the code.

Components:

•   com.uc3m.gade4all/Principal.java: Part I of the AI module. This is the main code to generate 
different scenario implementations. Regarding the model game rules (showed below), the AI 
procedure decides the best actions for each enemy regarding the players, obstacles, life points, 
specials cells and other factors that can influence on the choosing of the right decision. 
This class also implements a first approach to test the AI module (set parameters in method test() ).
•	com.uc3m.gade4all/AI: Path finding algorithm implementation. A* can be replaced by any other.
•	com.uc3m.gade4all/game: Game elements.


Instructions to execute:

1)	Open the project in eclipse.
2)	Run as an Android application
3)	Play from the screen of your android mobile and the shell of the computer.



