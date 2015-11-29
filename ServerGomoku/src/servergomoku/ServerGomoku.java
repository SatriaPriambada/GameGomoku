/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * All code and works here are created by Satria Priambada and team
 * You are free to use and distribute the code
 * We do not take responsibilities for any damage caused by using this code
 * Connection learned from https://www.youtube.com/watch?v=_1ThWf9Fkfo
 */
package servergomoku;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Satria
 */
public class ServerGomoku {
    static ServerSocket serverSocket;
    static Socket socket;
    static DataOutputStream out;
    static DataInputStream in;
    static int MaxUser = 5;
    static Users[] user = new Users[MaxUser];
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Starting server gomoku...");
        serverSocket = new ServerSocket(60301);
        System.out.println("Server started ...");
        while(true){
            //accepts any connection coming to the socket
            socket = serverSocket.accept();
            //Check for max user
            for (int i =0; i < MaxUser; i++){
                //check which sender send it
                System.out.println("Connection from: "+ socket.getInetAddress());
                //get input and output stream from clients
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                //Check is it existing user or new user
                if (user[i] == null){
                    user[i] = new Users(out,in,user);
                    Thread thread = new Thread(user[i]);
                    //start the new client
                    thread.start();
                    break;
                }
            }
            
        }
        
    }
    
}

class Users implements Runnable{
    private DataOutputStream out;
    private DataInputStream in;
    private int MaxUser = 5;
    Users[] user = new Users[MaxUser];
    String name;
    //constructor default do nothing
    //constructor with parameter
    public Users(DataOutputStream out, DataInputStream in, Users[] user){
        this.out = out;
        this.in = in;
        this.user = user;
    }
    public void run(){
        try {
            name = in.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true){
            try {
                String message = in.readUTF();
                for(int i = 0; i < MaxUser; i++){
                    if (user[i] != null){
                        user[i].out.writeUTF(name + ":"+ message);
                    }
                    //else user is not connected yet so user ==null
                }
            } catch (IOException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}