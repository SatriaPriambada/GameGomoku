/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * All code and works here are created by Satria Priambada and team
 * You are free to use and distribute the code
 * We do not take responsibilities for any damage caused by using this code
 * Connection learned from https://www.youtube.com/watch?v=_1ThWf9Fkfo
 * http://www.java-tips.org/java-se-tips-100019/15-javax-swing/584-a-game-of-tic-tac-toe-that-can-be-played-between-two-client-applets.html
 */
package servergomoku;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import static java.lang.Math.pow;
import javax.swing.*;
 
public class ServerGomoku extends JFrame {
 
   private byte board[]; //board permainan
   private Player players[]; //list dari pemain
   private ServerSocket server;//open serversocket untuk menerima request
   private int currentPlayer;// penanda pemain yang boleh main
   int counterNeighbour = 0; //menghitung berapa tetangga yang sama untuk mengecek kemenangan
   int winCon = 5; //Gomoku perlu 5 tetangga
   int moveCount = 0; //menghitung jumlah move, jika lebih berarti sudah draw
   boolean menang = false;
 
   private JTextArea output;//tempat menulis text
   
   public ServerGomoku()
   {
      super( "Gomoku Server" );
 
      board = new byte[ 400 ];
      players = new Player[ 3 ];
      currentPlayer = 0;
  
      // set up ServerSocket
      try {
         server = new ServerSocket( 7777, 3 );
      }
      catch( IOException e ) {
         e.printStackTrace();
         System.exit( 1 );
      }
 
      output = new JTextArea();
      getContentPane().add( output, BorderLayout.CENTER );
      output.setText( "Server awaiting connections\n" );
 
      setSize( 300, 300 );
      show();
   }
 
   // wait for two connections so game can be played
   public void execute()
   {
      for ( int i = 0; i < players.length; i++ ) {
         try {
            players[ i ] = new Player( server.accept(), this, i );
            players[ i ].start();
         }
         catch( IOException e ) {
            e.printStackTrace();
            System.exit( 1 );
         }
      }
 
      // Player X tidak bisa bergerak hingga semua player sudah connect.
      // Resume player X dan mulai permainan.          
      synchronized ( players[ 0 ] ) {
         players[ 0 ].threadSuspended = false;   
         players[ 0 ].notify();
      }
   
   }
    
   public void display( String s )
   {
      output.append( s + "\n" );
   }
  
   // Determine if a move is valid.
   // This method is synchronized because only one move can be
   // made at a time.
   public synchronized boolean validMove( int loc,int player )
   {
      while ( player != currentPlayer ) {
         try {
            wait();
         }
         catch( InterruptedException e ) {
            e.printStackTrace();
         }
      }
      char movingPlayer = 'a';
      if ( !isOccupied( loc ) ) {
         if (currentPlayer == 0){
            board[ loc ] =(byte) ('X');
            movingPlayer = 'X';
         }else if (currentPlayer == 1){
            board[ loc ] =(byte) ('O');
            movingPlayer = 'O';
         }else if (currentPlayer == 2){
            board[ loc ] =(byte) ('#');
            movingPlayer = '#';
         }
        //prints in all client boards
          for (int i = 0; i < players.length; i++) {
              players[i].otherPlayerMoved(loc,movingPlayer);
          }
         currentPlayer = ( currentPlayer + 1 ) % 3;
         //players[ currentPlayer ].otherPlayerMoved( loc );
         notify();    // tell waiting player to continue
         return true;
      }
      else
         return false;
   }
 
   public boolean isOccupied( int loc )
   {
      if ( board[ loc ] == 'X' || board [ loc ] == 'O' || board [ loc ] == '#')
          return true;
      else
          return false;
   }
 
   public boolean checkDiagonal (int x, int y, byte[] board, int winCondition, char sym, String check)
    {
        int j = y;
        boolean menang = false;
        int countWin = 0;
        if(menang == false)
        {
            if (check.equalsIgnoreCase("UR"))
            {
                for (int i=x; i>x-winCondition; i--)
                {
                    if (board[i + (20*j)] == sym)
                    {
                        countWin++;
                    }
                    else
                    {
                        countWin = 0;
                    }
                    if (countWin == winCondition)
                    {
                        // winner has been decided, exit game
                        menang = true;
                        break;
                    }
                    j++;
                }
            }
            else
            {
                for (int i=x; i<x+winCondition; i++)
                {
                    if (board[i + (20*j)] == sym)
                    {
                        countWin++;
                    }
                    else
                    {
                        countWin = 0;
                    }
                    if (countWin == winCondition)
                    {
                        // winner has been decided, exit game
                        menang = true;
                        break;
                    }
                    j++;
                }            
            }
        }
        return menang;
    }
   
   public boolean gameOver(int location, char currMark)
   {
      // Place code here to test for a winner of the game
        int n = 20;
        int x = location % n; // posisi secara horizontal alias kolom
        int y = location / n; //posisi secara vertikal alias baris
        
    	//check col
    	for(int i = 0; i < n; i++){
            if(board[(y*n) + i] == (byte) currMark)
                counterNeighbour++;
            else
                counterNeighbour = 0;
            if(winCon == counterNeighbour){
                //set winning col to W
                for (int j = 0; j < winCon; j++) {
                    board[(y*n) + i -j] = (byte)'W';
                    for (int l = 0; l < players.length; l++) {
                        players[l].otherPlayerMoved((y*n) + i -j,'W');
                    }
                }
                //broadcast winning message to all player
                for (int j = 0; j < players.length; j++) {
                    players[j].broadcast("We have a winner " + currMark);
                }
                //report win for s
                System.out.println("You Win " + currMark);
                return true;

            }
    	}

    	//check row
    	for(int i = 0; i < n; i++){
            if(board[(i*n) + x] != (byte) currMark)
                counterNeighbour = 0;
            else
                counterNeighbour++;
            if(winCon == counterNeighbour){
                //set winning row to W
                for (int j = 0; j < winCon; j++) {
                    board[((i-j)*n) + x] = (byte)'W';
                    for (int l = 0; l < players.length; l++) {
                        players[l].otherPlayerMoved(((i-j)*n) + x,'W');
                    }
                }
                //broadcast winning message to all player
                for (int j = 0; j < players.length; j++) {
                    players[j].broadcast("We have a winner " + currMark);
                }
    		//report win for s
                System.out.println("You Win " + currMark);
                return true;
            }
    	}

    	//check diag
        if (menang==false)
        {
            // diagonal (up right)
            
            for (int i=0; i<n; i++)
            {
                for (int j=0; j<n; j++)
                {
                    System.out.println(i+(j*n));
                    if (board[i+(j*n)] == currMark)
                    {
                        if ((i < winCon-1) || (j > board.length-winCon))
                        {
                            // do nothing
                        }
                        else
                        {
                            System.out.println(i + " " + j +" coord");
                            menang = checkDiagonal(i,j,board,winCon,currMark,"UR");
                    
                        }
                    }
                    if (menang==true)
                    {
                        //broadcast winning message to all player
                    for (int h = 0; h < players.length; h++) {
                        players[h].broadcast("We have a winner " + currMark);
                    }
                    //report win for s
                    System.out.println("You Win " + currMark);
                        return true;
                    }
                }
            }
        }
        
        //check anti diagonal
        
        if (menang==false)
        {
            // diagonal (down right)
            
            for (int i=0; i<n; i++)
            {
                for (int j=0; j<n; j++)
                {
                    if (board[i + (20*j)] == currMark)
                    {
                        if ((i > board.length-winCon) || (j > board.length-winCon))
                        {
                            // do nothing
                        }
                        else
                        {
                            System.out.println(i + " " + j +" coord");
                            menang = checkDiagonal(i,j,board,winCon,currMark,"DR");
                        }
                    }
                    if (menang==true)
                    {
                        //broadcast winning message to all player
                        for (int m = 0; m < players.length; m++) {
                            players[m].broadcast("We have a winner " + currMark);
                        }
                        //report win for s
                        System.out.println("You Win " + currMark);
                        return true;
                    }
                }
            }
        } 
        moveCount++;

    	//check draw
    	if(moveCount == (pow(n,2) - 1)){
            //report draw
            System.out.println("Its a draw ");
            return true;

    	}
        System.out.println(moveCount);
        return false;
   }
 
   public static void main( String args[] )
   {
      ServerGomoku game = new ServerGomoku();
 
      game.addWindowListener( new WindowAdapter() {
        public void windowClosing( WindowEvent e )
            {
               System.exit( 0 );
            }
         }
      );
 
      game.execute();
   }

    public void broadcastAll(String broadcastMessage) {
        for (int j = 0; j < players.length; j++) {
            players[j].broadcast(broadcastMessage);
        }
    }
}
 
// Player class to manage each Player as a thread
class Player extends Thread {
   private Socket connection;
   private DataInputStream input;
   private DataOutputStream output;
   private ServerGomoku control;
   private int number;
   private char mark;
   protected boolean threadSuspended = true;
 
   public Player( Socket s, ServerGomoku t, int num )
   {
      if ( num == 0 )
          mark = 'X';
      else if (num == 1)
          mark = 'O';
      else if (num == 2)
          mark = '#';
 
      connection = s;
       
      try {
         input = new DataInputStream(connection.getInputStream() );
         output = new DataOutputStream(connection.getOutputStream() );
      }
      catch( IOException e ) {
         e.printStackTrace();
         System.exit( 1 );
      }
 
      control = t;
      number = num;
   }
 
   public void otherPlayerMoved( int loc , char movingPlayer)
   {
      try {
         output.writeUTF( "Opponent moved" );
         output.writeInt( loc );
         output.writeChar(movingPlayer);
      }
      catch ( IOException e ) { e.printStackTrace(); }
   }
   
   public void broadcast( String broadcastM)
   {
      try {
         output.writeUTF( broadcastM);
      }
      catch ( IOException e ) { e.printStackTrace(); }
   }
 
   public void run()
   {
      boolean done = false;
 
      try {
         if (number == 0){
            control.display( "Player " + 'X' + " connected" );
         } else if (number == 1){
            control.display( "Player " + '0' + " connected" );
         } if (number == 2){
            control.display( "Player " + '#' + " connected" );
         }
         output.writeChar( mark );
         if (number == 0){
            output.writeUTF( "Player " + 'X' + " connected\n" );
         } else if (number == 1){
            output.writeUTF( "Player " + '0' + " connected\n" );
         } if (number == 2){
            output.writeUTF( "Player " + '#' + " connected. Please wait..\n" );
         }
 
         // wait for another player to arrive
         if ( mark == 'X' ) {
            output.writeUTF( "Waiting for another player" );
 
            try {
               synchronized( this ) {   
                  while ( threadSuspended )
                     wait();  
               }
            } 
            catch ( InterruptedException e ) {
               e.printStackTrace();
            }
 
            output.writeUTF(
               "Other player connected. Your move." );
         }
 
         // Play game
         char currMark ='a';
         String broadcastMessage = new String();
         while ( !done ) {
            broadcastMessage = input.readUTF();
            //read the message if started with MOVE then move else is a chat
            if (broadcastMessage.equals("MOVE")){
                int location = input.readInt();
                if ( control.validMove( location, number ) ) {
                   control.display( "loc: " + location );
                   output.writeUTF( "Valid move." );
                }
                else
                   output.writeUTF( "Invalid move, try again" );
                
                currMark = input.readChar();
                if ( control.gameOver(location,currMark) )
                    done = true;
            }else {
                control.broadcastAll(broadcastMessage);
            }
         }         
         output.writeUTF("WINNER " + currMark);
         connection.close();
      }
      catch( IOException e ) {
         e.printStackTrace();
         System.exit( 1 );
      }
   }
}                                                         
 