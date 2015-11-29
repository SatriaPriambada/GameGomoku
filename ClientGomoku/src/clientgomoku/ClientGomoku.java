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

import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 *
 * @author Satria
 */
public class ClientGomoku {
    static Socket socket;
    static DataInputStream in;
    static DataOutputStream out;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        //
        System.out.println("Starting client input IPadress and socket of server");
        Scanner sc = new Scanner(System.in);
        String IPServer = sc.nextLine();
        String SocketServer = sc.nextLine();
        System.out.println("Connecting to server...");      
        socket = new Socket(IPServer,Integer.valueOf(SocketServer));
        System.out.println("Connection sucessfull.");
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        Input input = new Input (in);
        Thread thread = new Thread(input);
        thread.start();
        
        while(true){
            String sendMessage = sc.nextLine();
            out.writeUTF(sendMessage);
        }
        
    }
    
}

class Input implements Runnable{
    DataInputStream in;
    //constructor default do nothing
    //constructor with parameter
    public Input(DataInputStream in){
        this.in = in;
    }
    
    public void run(){
        while(true){
            try {
                String message = in.readUTF();
                System.out.println(message);
            } catch (IOException ex) {
                Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class GomokuTable extends JFrame{
    JTable table;
    
    public GomokuTable(){
        setLayout(new FlowLayout());
        
        String[] columnNames = {"1"};
        table = new JTable(10,10);
    }
}