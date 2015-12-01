/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * All code and works here are created by Satria Priambada and team
 * You are free to use and distribute the code
 * We do not take responsibilities for any damage caused by using this code
 * Connection learned from https://www.youtube.com/watch?v=_1ThWf9Fkfo
 */
package clientgomoku;
//
//import java.io.DataInputStream;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javafx.application.Application;
//import javafx.geometry.Pos;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.input.MouseButton;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//import javax.swing.JFrame;
//import javax.swing.JTable;
//
///**
// *
// * @author Satria
// */
//public class ClientGomoku extends Application{
//    static Socket socket;
//    static DataInputStream in;
//    static DataOutputStream out;
//    
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) throws Exception{
//        //
//        System.out.println("Starting client input IPadress and socket of server");
//        Scanner sc = new Scanner(System.in);
//        System.out.println("input IP adress");
//        String IPServer = sc.nextLine();
//        System.out.println("input port number");
//        String SocketServer = sc.nextLine();
//        System.out.println("Connecting to server...");      
//        socket = new Socket(IPServer,Integer.valueOf(SocketServer));
//        System.out.println("Connection sucessfull.");
//        in = new DataInputStream(socket.getInputStream());
//        out = new DataOutputStream(socket.getOutputStream());
//        Input input = new Input (in);
//        Thread thread = new Thread(input);
//        thread.start();
//        boolean runOnce = true;
//        while(true){
//            if (runOnce){
//                launch(args);
//                runOnce = false;
//            }
//            
//            String sendMessage = sc.nextLine();
//            out.writeUTF(sendMessage);
//            
//        }
//        
//        
//    }
//    private class Tile extends StackPane {
//        private Text text = new Text();
//
//        public Tile() {
//            Rectangle border = new Rectangle(30, 30);
//            border.setFill(null);
//            border.setStroke(Color.BLACK);
//
//            text.setFont(Font.font(30));
//
//            setAlignment(Pos.CENTER);
//            getChildren().addAll(border, text);
//
//            setOnMouseClicked(event -> {
//                if (!playable)
//                    return;
//
//                if (event.getButton() == MouseButton.PRIMARY) {
//                    if (!turnX)
//                        return;
//
//                    drawX();
//                    turnX = false;
//                    System.out.println("coord X: "+ this.getCenterX());
//                    System.out.println("coord Y: "+ this.getCenterY());
////                    checkState();
//                }
//                else if (event.getButton() == MouseButton.SECONDARY) {
//                    if (turnX)
//                        return;
//
//                    drawO();
//                    turnX = true;
////                    checkState();
//                }
//            });
//        }
//
//        public double getCenterX() {
//            return getTranslateX() + 15;
//        }
//
//        public double getCenterY() {
//            return getTranslateY() + 15;
//        }
//
//        public String getValue() {
//            return text.getText();
//        }
//
//        private void drawX() {
//            text.setText("X");
//        }
//
//        private void drawO() {
//            text.setText("O");
//        }
//    }
//    private boolean playable = true;
//    private boolean turnX = true;
//    private Tile[][] board = new Tile[20][20];
////    private List<SimpleBoard.Combo> combos = new ArrayList<>();
//
//    private Pane root = new Pane();
//
//    private Parent createContent() {
//        root.setPrefSize(800, 1000);
//
//        for (int i = 0; i < 20; i++) {
//            for (int j = 0; j < 20; j++) {
//                Tile tile = new Tile();
//                tile.setTranslateX(j * 30);
//                tile.setTranslateY(i * 30);
//
//                root.getChildren().add(tile);
//
//                board[i][j] = tile;
//            }
//        }
////
////        // horizontal
////        for (int y = 0; y < 3; y++) {
////            combos.add(new SimpleBoard.Combo(board[0][y], board[1][y], board[2][y]));
////        }
////
////        // vertical
////        for (int x = 0; x < 3; x++) {
////            combos.add(new SimpleBoard.Combo(board[x][0], board[x][1], board[x][2]));
////        }
////
////        // diagonals
////        combos.add(new SimpleBoard.Combo(board[0][0], board[1][1], board[2][2]));
////        combos.add(new SimpleBoard.Combo(board[2][0], board[1][1], board[0][2]));
//
//        return root;
//    }
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setScene(new Scene(createContent()));
//        primaryStage.show();
//    }
//    
//}
//
//class Input implements Runnable{
//    DataInputStream in;
//    //constructor default do nothing
//    //constructor with parameter
//    public Input(DataInputStream in){
//        this.in = in;
//    }
//    
//    @Override
//    public void run(){
//        while(true){
//            try {
//                String message = in.readUTF();
//                System.out.println(message);
//            } catch (IOException ex) {
//                Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//}

// Client class to let a user play Tic-Tac-Toe with
// another user across a network.
public class ClientGomoku extends JApplet
                             implements Runnable {
   private JTextField id;
   private JTextArea display;
   private JPanel boardPanel, panel2;
   private Square board[][], currentSquare;
   private Socket connection;
   private DataInputStream input;
   private DataOutputStream output;
   private Thread outputThread;
   private char myMark;
   private boolean myTurn;
 
   // Set up user-interface and board
   public void init()
   {
      display = new JTextArea( 4, 30 );
      display.setEditable( false );
      getContentPane().add( new JScrollPane( display ),
                            BorderLayout.SOUTH );
 
      boardPanel = new JPanel();
      GridLayout layout = new GridLayout( 20, 20, 0, 0 );
      boardPanel.setLayout( layout );
 
      board = new Square[ 20 ][ 20 ];
 
      // When creating a Square, the location argument to the
      // constructor is a value from 0 to 8 indicating the
      // position of the Square on the board. Values 0, 1,
      // and 2 are the first row, values 3, 4, and 5 are the
      // second row. Values 6, 7, and 8 are the third row.
      for ( int row = 0; row < board.length; row++ )
      {
         for ( int col = 0;col < board[ row ].length; col++ ) {
            board[ row ][ col ] =
               new Square( ' ', row * 20 + col );
            board[ row ][ col ].addMouseListener((MouseListener) new SquareListener(
                  this, board[ row ][ col ] ));
 
            boardPanel.add( board[ row ][ col ] );        
         }
      }
 
      id = new JTextField();
      id.setEditable( false );
       
      getContentPane().add( id, BorderLayout.NORTH );
       
      panel2 = new JPanel();
      panel2.add( boardPanel, BorderLayout.CENTER );
      getContentPane().add( panel2, BorderLayout.CENTER );
   }
 
   // Make connection to server and get associated streams.
   // Start separate thread to allow this applet to
   // continually update its output in text area display.
   public void start()
   {
      try {
         connection = new Socket(
            InetAddress.getByName( "127.0.0.1" ), 5000 );
         input = new DataInputStream(
                        connection.getInputStream() );
         output = new DataOutputStream(
                        connection.getOutputStream() );
      }
      catch ( IOException e ) {
         e.printStackTrace();         
      }
 
      outputThread = new Thread( this );
      outputThread.start();
   }
 
   // Control thread that allows continuous update of the
   // text area display.
   public void run()
   {
      // First get player's mark (X or O)
      try {
         myMark = input.readChar();
         id.setText( "You are player \"" + myMark + "\"" );
         if( myMark == 'X' ){
             myTurn = true;
         }else if( myMark == 'O' ){
             myTurn = false;
         }else if( myMark == '#' ){
             myTurn = false;
         } 
      }
      catch ( IOException e ) {
         e.printStackTrace();         
      }
 
      // Receive messages sent to client
      while ( true ) {
         try {
            String s = input.readUTF();
            processMessage( s );
         }
         catch ( IOException e ) {
            e.printStackTrace();         
         }
      }
   }
 
   // Process messages sent to client
   public void processMessage( String s )
   {
      if ( s.equals( "Valid move." ) ) {
         display.append( "Valid move, please wait.\n" );
         currentSquare.setMark( myMark );
         currentSquare.repaint();
      }
      else if ( s.equals( "Invalid move, try again" ) ) {
         display.append( s + "\n" );
         myTurn = true;
      }
      else if ( s.equals( "Opponent moved" ) ) {
         try {
            int loc = input.readInt();
            char movingPlayer = input.readChar();
            if (movingPlayer == 'X'){
                board[ loc / 20 ][ loc % 20 ].setMark('X');
            }else if (movingPlayer == 'O'){
                board[ loc / 20 ][ loc % 20 ].setMark('O');
            }else if (movingPlayer == '#'){
                board[ loc / 20 ][ loc % 20 ].setMark('#');
            }
            
            board[ loc / 20 ][ loc % 20 ].repaint();
                  
            display.append(
               "Opponent moved. Your turn.\n" );
            myTurn = true;
         }
         catch ( IOException e ) {
            e.printStackTrace();         
         }
      }
      else
         display.append( s + "\n" );
 
      display.setCaretPosition(
         display.getText().length() );
   }
 
   public void sendClickedSquare( int loc )
   {
      if ( myTurn )
         try {
            output.writeInt( loc );
            myTurn = false;
         }
         catch ( IOException ie ) {
            ie.printStackTrace();         
         }
   }
 
   public void setCurrentSquare( Square s )
   {
      currentSquare = s;
   }
}
 
// Maintains one square on the board
class Square extends JPanel {
   private char mark;
   private int location;
 
   public Square( char m, int loc)
   {
      mark = m;
      location = loc;
      setSize ( 25, 25 );
       
      setVisible(true);
   }
 
   public Dimension getPreferredSize() { 
      return ( new Dimension( 25, 25 ) );
   }
 
   public Dimension getMinimumSize() {
      return ( getPreferredSize() );
   }
 
   public void setMark( char c ) { mark = c; }
 
   public int getSquareLocation() { return location; }
 
   public void paintComponent( Graphics g )
   {
      super.paintComponent( g );
      g.drawRect( 0, 0, 25, 25 );
      g.drawString( String.valueOf( mark ), 11, 20 );   
   }
}
 
class SquareListener extends MouseAdapter {
   private ClientGomoku applet;
   private Square square;
 
   public SquareListener( ClientGomoku t, Square s )
   {
      applet = t;
      square = s;
   }
 
   public void mouseReleased( MouseEvent e )
   {
      applet.setCurrentSquare( square );
      applet.sendClickedSquare( square.getSquareLocation() );
   }
}