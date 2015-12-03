/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientgomoku;

/**
 *
 * @author Ben
 */

import java.io.BufferedReader;
import static java.lang.Math.pow;
import static java.lang.System.exit;
import java.util.Scanner;


public class SimpleBoard2 {
    

    public static int countWin = 0;
    
    public static void makeBoard(String[][] board)
    {
        for (int i=0; i<board.length; i++)
        {
            for (int j=0; j<board.length; j++)
            {
                board[i][j] = " ";
            }
        }
    }
    
    public static void printBoard(String[][] board)
    {

        for (int i=0; i<board.length; i++)
        {
            System.out.print(" ");
            for(int k=0; k<board.length; k++)
            {
                System.out.print (" _  ");
            }
            System.out.println();
            System.out.print("| ");
            for (int j=0; j<board.length; j++)
            {
                System.out.print (board[i][j]);
                System.out.print (" | ");
            }
            System.out.println ();
        }
        System.out.print(" ");
        for(int i=0; i<board.length; i++)
        {
            System.out.print (" _  ");
        }
    }
    
    public static boolean moveValidation (int x, int y, String[][] board, String sym, boolean valid)
    {
        valid = false;
        if (board[x][y].equalsIgnoreCase(" "))
        {
            board[x][y] = sym;
            valid = true;
        }
        else
        {
            // do nothing
        }
        return valid;
    }
    
    public static boolean checkDiagonal (int x, int y, String[][] board, int winCondition, String sym, String check)
    {
        int j = y;
        boolean menang = false;
        countWin = 0;
        if(menang == false)
        {
            if (check.equalsIgnoreCase("UR"))
            {
                for (int i=x; i>x-winCondition; i--)
                {
                    if (board[i][j].equalsIgnoreCase(sym))
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
                    if (board[i][j].equalsIgnoreCase(sym))
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
    
    public static boolean checkWin (int x, int y, String[][] board, String sym, int winCondition)
    {
        // check win condition
        boolean menang = false;
        if (menang==false)
        {
            // col
            for (int i=0; i<board.length; i++)
            {
                if (board[x][i].equalsIgnoreCase(sym))
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
            }
        }  
        
        if (menang==false)
        {
            // row
            for (int i=0; i<board.length; i++)
            {
                if (board[i][y].equalsIgnoreCase(sym))
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
            }
        }
                
        if (menang==false)
        {
            // diagonal (up right)
            
            for (int i=0; i<board.length; i++)
            {
                for (int j=0; j<board.length; j++)
                {
                    if (board[i][j].equalsIgnoreCase(sym))
                    {
                        if ((i<winCondition-1) || (j>board.length-winCondition))
                        {
                            // do nothing
                        }
                        else
                        {
                            menang = checkDiagonal(i,j,board,winCondition,sym,"UR");
                    
                        }
                    }
                    if (menang==true)
                    {
                        break;
                    }
                }
                if (menang==true)
                {
                    break;
                }
            }
        }
        
        if (menang==false)
        {
            // diagonal (down right)
            
            for (int i=0; i<board.length; i++)
            {
                for (int j=0; j<board.length; j++)
                {
                    if (board[i][j].equalsIgnoreCase(sym))
                    {
                        if ((i>board.length-winCondition) || (j>board.length-winCondition))
                        {
                            
                            // do nothing
                        }
                        else
                        {
                            menang = checkDiagonal(i,j,board,winCondition,sym,"DR");
                        }
                    }
                    if (menang==true)
                    {
                        break;
                    }
                }
                if (menang==true)
                {
                    break;
                }
            }
        } 

        return menang;
    }
            
    
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        // ukuran board
        System.out.print ("Masukkan ukuran board : ");
        int boardSize = input.nextInt();
        String[][] boardGame = new String[boardSize][boardSize];
        makeBoard(boardGame);
        
        //jumlah n maks
        System.out.print ("Masukkan jumlah n maks : ");
        int winCondition = input.nextInt();
        input.nextLine();

        // jumlah pemain dan input pemain
        System.out.print ("Masukkan jumlah pemain : ");
        int playerCount = input.nextInt();
        input.nextLine();
        String[] Player = new String[playerCount];
        for (int i=0; i<playerCount; i++)
        {
            System.out.print ("Player " + i + " (masukkan symbol): ");
            Player[i] = input.nextLine();
        }
        System.out.println();
        
        
        // game on
        boolean valid = false;
        boolean menang = false;
        int maxMove = (int) (pow(boardSize,2));
        int x = 0;
        int y = 0;
        for (int i=0; i<maxMove; i++)
        {
            // validate move
            valid = false;
            while (valid==false)
            {
                System.out.println ("Player " + (i%playerCount+1) + " turn");
                System.out.print ("Masukkan koordinat : ");
                x = input.nextInt();
                y = input.nextInt();
                valid = moveValidation(x,y,boardGame,Player[i%playerCount],valid);
                if (valid==false)
                {
                    System.out.println ("Masukkan salah, mohon input kembali");
                }
            }
            printBoard(boardGame);
            System.out.println();
            menang = checkWin(x,y,boardGame,Player[i%playerCount],winCondition);
            if (menang==true)
            {
                System.out.println ("Congratulation ! Player " + (i%playerCount+1) + " win the game <3");
                break;
            }
        }
        
    }
}
