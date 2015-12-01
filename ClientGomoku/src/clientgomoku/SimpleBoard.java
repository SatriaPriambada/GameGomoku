package clientgomoku;

import java.io.BufferedReader;
import static java.lang.Math.pow;
import static java.lang.System.exit;
import java.util.Scanner;

public class SimpleBoard {

    enum State{Blank, X, O};

    int n = 5;
    int winCon = 3;
    int counterNeighbour =0;
    State[][] board = new State[n][n];
    int moveCount;
    public void initBoard(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = State.Blank;
                
            }
            
        }
    }
    
    public void printBoard(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j]+" ");
                
            }
            System.out.println();
        }
    }

    void Move(int x, int y, State s){
    	if(board[x][y] == State.Blank){
    		board[x][y] = s;
                System.out.println(s);
    	}
    	moveCount++;

    	//check end conditions

    	//check col
    	for(int i = 0; i < n; i++){
    		if(board[x][i] != s)
                    counterNeighbour = 0;
                else
                    counterNeighbour++;
    		if(winCon == counterNeighbour){
    			//report win for s
                    System.out.println("You Win " + s);
                    exit(1);

    		}
    	}

    	//check row
    	for(int i = 0; i < n; i++){
    		if(board[i][y] != s)
                    counterNeighbour = 0;
                else
                    counterNeighbour++;
    		if(winCon == counterNeighbour){
    			//report win for s
                    System.out.println("You Win " + s);
                    exit(1);
    		}
    	}

    	//check diag
    	if(x == y){
    		//we're on a diagonal
    		for(int i = 0; i < n; i++){
    			if(board[i][i] != s)
                            counterNeighbour = 0;
                        else
                            counterNeighbour++;
                        if(winCon == counterNeighbour){
    				//report win for s
                            System.out.println("You Win " + s);
                            exit(1);

    			}
    		}
    	}

            //check anti diag (thanks rampion)
    	for(int i = 0;i<n;i++){
                if(board[i][(n-1)-i] != s)
                    counterNeighbour = 0;
                else
                    counterNeighbour++;
    		if(winCon == counterNeighbour){
    			//report win for s
                    System.out.println("You Win " + s);
                    exit(1);

    		}
    	}

    	//check draw
    	if(moveCount == (pow(n,2) - 1)){
    		//report draw
            System.out.println("Its a draw ");
            exit(1);

    	}
    }
    
    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        SimpleBoard Game = new SimpleBoard();
        Game.initBoard();
        int n = 5;
        int move = (int) (pow(n,2) - 1);
        System.out.println(move);
        for (int i = 0; i <move; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            System.out.println(x + " " + y);
            if (i%2 == 0){
                Game.Move(x, y, State.X);
            } else {
                Game.Move(x, y, State.O);
            }
            Game.printBoard();
        }
    }
}