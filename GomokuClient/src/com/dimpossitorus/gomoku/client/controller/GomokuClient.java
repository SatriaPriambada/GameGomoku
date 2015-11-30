/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimpossitorus.gomoku.client.controller;

/**
 *
 * @author Dimpos Sitorus
 */
public class GomokuClient {

    /**
     * @param args the command line arguments
     */
    
    private int[][] board; // Board permainan
    private boolean playerStatus; // Status untuk bermain
    private int playerNum;
    
    public GomokuClient() {
        board = new int[20][];
        for (int i=0; i<20; i++) {
            board[i]=new int[20];
        }
    }
    
    public int getPlayerNum() {
        return playerNum;
    }
    
    public void setPlayerNum(int pNum) {
        playerNum = pNum;
    }
    
    public boolean getPlayerStatus() {
        return playerStatus;
    }
    
    public void setPlayerStatus (boolean play) {
        playerStatus = play;
    }
    
    public void boardChosen(int i, int j) {
        board[i][j]=playerNum;
    }
    
    public void sendPacket() {
        // Code here to sent the chosen board to the server
        // Content of Packet sent is :
        // 1. Board Selected
        // 
    }
    
    public void receivePacket() {
        // Code here to receive the packet
        // that contain the chosen board of another player and play session
        // Content of Packet received is :
        // 1. Who's playing now
        // 2. Board selected of previous player
        // 3. Winner
        // 
    }
}
