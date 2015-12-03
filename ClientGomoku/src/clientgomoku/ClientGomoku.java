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


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGomoku extends JApplet implements Runnable {
   private JTextField id;
   private JTextArea display;
   private JTextField sendPanel;
   private JButton buttonStart;
   private JButton buttonNewRoom;
   private JButton buttonJoinRoom;
   private JButton buttonDisconnect;
   private JPanel boardPanel, panelEast, panelSouth;
   private Square board[][], currentSquare;
   private Socket connection;
   private DataInputStream input;
   private DataOutputStream output;
   private Thread outputThread;
   private char myMark;
   private String inUsernameStr;
   private String inIPaddr;
   private boolean myTurn;
   // Set up user-interface and board
   @Override
   public void init()
   {
      display = new JTextArea( 4, 15 );
      sendPanel = new JTextField();
      sendPanel.setEditable(true);
      buttonStart = new JButton("Start");
      buttonNewRoom = new JButton("Create New Room");
      buttonJoinRoom = new JButton("Join Room");
      buttonDisconnect = new JButton("Disconnect");
      Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                
                String broadcastMessage = myMark + " : " + sendPanel.getText() + "\n";
                sendPanel.setText("");
                try {
                    output.writeUTF(broadcastMessage);
                } catch (IOException ex) {
                    Logger.getLogger(ClientGomoku.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
      sendPanel.addActionListener(action);
      
      buttonStart.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae){
             System.out.println("Clicked start");
             inUsernameStr = JOptionPane.showInputDialog(null, "Insert Username","Username", JOptionPane.PLAIN_MESSAGE);
             inIPaddr = JOptionPane.showInputDialog(null, "Insert IP Address", "IP Address" ,JOptionPane.PLAIN_MESSAGE);
             System.out.println("You have entered " + inUsernameStr);
             JOptionPane.showMessageDialog(null, "Your username : " + inUsernameStr,
                   "Confirm Username", JOptionPane.PLAIN_MESSAGE);
             int answer = JOptionPane.showConfirmDialog(null, "Are you sure to connect to IP " + inIPaddr,
                   "Confirm IP", JOptionPane.YES_NO_OPTION);
             switch (answer) {
                case JOptionPane.YES_OPTION:
                   System.out.println("You clicked YES");
                   id.repaint();
                   id.revalidate();
                   start(inIPaddr);
                   break;
                case JOptionPane.NO_OPTION:
                   System.out.println("You clicked NO"); break;
            }
         } 
      });
      
      buttonNewRoom.addActionListener(new ActionListener() {
        // When button is clicked, send message to the server
        // create new board of game on th server
         @Override
         public void actionPerformed(ActionEvent ae){
             System.out.println("Clicked");
             try {
                 output.writeUTF("CREATE_NEW_ROOM");
             } catch (IOException ex) {
                 Logger.getLogger(ClientGomoku.class.getName()).log(Level.SEVERE, null, ex);
             }
         } 
      });
      
      buttonJoinRoom.addActionListener(new ActionListener() {
        // When button is clicked, send message to the server
        // requesting the list of board that is being active in the server
        // Show the list of room which are ready to play
        // Choose the room by using the room 
         @Override
         public void actionPerformed(ActionEvent ae){
             System.out.println("Clicked");
             try {
                 output.writeUTF("SEND_ROOM_INFO");
             } catch (IOException ex) {
                 Logger.getLogger(ClientGomoku.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         
      });
      
      buttonDisconnect.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae){
             System.out.println("Clicked");
         } 
      });
             

      display.setEditable( false );
      getContentPane().add( new JScrollPane( display ),BorderLayout.EAST );
 
      boardPanel = new JPanel();
      GridLayout layout = new GridLayout( 20, 20, 0, 0 );
      boardPanel.setLayout( layout );
 
      board = new Square[ 20 ][ 20 ];
 
      // Create playing board 20 x 20
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
 
      id = new JTextField("Username:" + inUsernameStr);
      id.setEditable( true );
       
      
       
      panelEast = new JPanel();
      panelEast.add( boardPanel, BorderLayout.CENTER );
      panelSouth = new JPanel();
      panelSouth.add (buttonStart);
      panelSouth.add (buttonNewRoom);
      panelSouth.add (buttonJoinRoom);
      panelSouth.add (buttonDisconnect);
      
      getContentPane().add( panelSouth,BorderLayout.SOUTH);
      getContentPane().add( sendPanel,BorderLayout.NORTH);
      getContentPane().add( panelEast, BorderLayout.CENTER );
      
   }
 
   // Make connection to server and get associated streams.
   // Start separate thread to allow this applet to
   // continually update its output in text area display.
    public void start(String host)
    {
        
        try {
           connection = new Socket(InetAddress.getByName( host ), 7777 );
           input = new DataInputStream(connection.getInputStream() );
           output = new DataOutputStream( connection.getOutputStream() );
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
      // First get player's mark (X, O, or #)
      try {
         myMark = input.readChar();
         id.setText( "You are player \"" + inUsernameStr + "\"" );
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
            }else if (movingPlayer == 'W'){
                board[ loc / 20 ][ loc % 20 ].setMark('W');
            }
            
            board[ loc / 20 ][ loc % 20 ].repaint();
                  
            if (myTurn)
                display.append("Opponent moved. Your turn.\n" );
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
            output.writeUTF("MOVE");
            output.writeInt( loc );
            output.writeChar(myMark);
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