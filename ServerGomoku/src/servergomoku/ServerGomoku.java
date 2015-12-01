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
import javax.swing.*;
 
public class ServerGomoku extends JFrame {
 
   private byte board[];
   private boolean xMove;
   private JTextArea output;
   private Player players[];
   private ServerSocket server;
   private int currentPlayer;
 
   public ServerGomoku()
   {
      super( "Gomoku Server" );
 
      board = new byte[ 400 ];
      xMove = true;
      players = new Player[ 3 ];
      currentPlayer = 0;
  
      // set up ServerSocket
      try {
         server = new ServerSocket( 5000, 3 );
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
            players[ i ] =
               new Player( server.accept(), this, i );
            players[ i ].start();
         }
         catch( IOException e ) {
            e.printStackTrace();
            System.exit( 1 );
         }
      }
 
      // Player X is suspended until Player O connects.
      // Resume player X now.          
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
   public synchronized boolean validMove( int loc,
                                          int player )
   {
      boolean moveDone = false;
 
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
            board[ loc ] =(byte) ('0');
            movingPlayer = 'O';
         }else if (currentPlayer == 2){
            board[ loc ] =(byte) ('#');
            movingPlayer = '#';
         }
        
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
 
   public boolean gameOver()
   {
      // Place code here to test for a winner of the game
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
         input = new DataInputStream(
                    connection.getInputStream() );
         output = new DataOutputStream(
                    connection.getOutputStream() );
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
         while ( !done ) {
            int location = input.readInt();
 
            if ( control.validMove( location, number ) ) {
               control.display( "loc: " + location );
               output.writeUTF( "Valid move." );
            }
            else
               output.writeUTF( "Invalid move, try again" );
 
            if ( control.gameOver() )
               done = true;
         }         
 
         connection.close();
      }
      catch( IOException e ) {
         e.printStackTrace();
         System.exit( 1 );
      }
   }
}                                                         
 