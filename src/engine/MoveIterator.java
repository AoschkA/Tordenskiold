package src.engine;

import src.entities.ChessBoard;

public class MoveIterator {
    public static int PLAYER;
    public static int PLAYER_WHITE = 1;
    public static int PLAYER_BLACK = 0;
    public static int VERIFIED_DEPTH = 5;
    public static String[] moveStack;

    /*
            Player parameter represents 1 or 0.
                        1. White
                        0. Black

            Alpha Beta pruning source: https://chessprogramming.wikispaces.com/Alpha-Beta
                alpha: -infinite
                beta: infinite
     */

    public static String alphaBetaMax3(int depth, int alpha, int beta, ChessBoard chessboard, int player, String previousMove) {
        /*
        if ( depthleft == 0 ) return evaluate();
        for ( all moves) {
            score = alphaBetaMin( alpha, beta, depthleft - 1 );
            if( score >= beta )
                return beta;   // fail hard beta-cutoff
            if( score > alpha )
                alpha = score; // alpha acts like max in MiniMax
         }
         return alpha;
         */
        if (depth == VERIFIED_DEPTH) moveStack = new String[VERIFIED_DEPTH+1];
        if (depth == 0){ return previousMove + "b" + MoveEvaluator.evaluateChessboard(chessboard, player);
        }

        String[] moveList;
        if  (player == 1) moveList = MoveGenerator.possibleMovesWhite(chessboard).split("-");
        else moveList = MoveGenerator.possibleMovesBlack(chessboard).split("-");
        String desiredMove = "BBBB";
        if (moveList.length != 0) {
            player = 1 - player;
            for (int i = 0; i < moveList.length; i++) {
                if (moveList[i].length() == 4) {
                    String movedBoard = ChessBoardFactory.simulateMove(moveList[i], BitBoardCalculations.chessBoardToString(chessboard));
                    if (!movedBoard.equals("BBBB")) {
                        String result = alphaBetaMin3(depth - 1, alpha, beta, ChessBoardFactory.generateChessBoardFromString(movedBoard), player, moveList[i]);
                        if (!result.substring(0, 4).equals("BBBB")) {
                            int value = Integer.valueOf(result.substring(5));
                            if (value >= beta) {
                                return "BBBBb" + beta;
                            }
                            if (value > alpha) {
                                alpha = value;
                                desiredMove = result.substring(0, 4);
                                moveStack[depth] = previousMove;
                            }
                        }
                    }
                }
            }
        }
        if (depth == VERIFIED_DEPTH) return moveStack[4] + "b" + alpha;
        return desiredMove + "b" + alpha;
    }

    public static String alphaBetaMin3(int depth, int alpha, int beta, ChessBoard chessboard, int player, String previousMove) {
        /*
        if ( depthleft == 0 ) return -evaluate();
        for ( all moves) {
            score = alphaBetaMax( alpha, beta, depthleft - 1 );
        if( score <= alpha )
            return alpha; // fail hard alpha-cutoff
        if( score < beta )
         beta = score; // beta acts like min in MiniMax
        }
        return beta;
         */


        if (depth == 0){ return previousMove + "b" + -MoveEvaluator.evaluateChessboard(chessboard, player);
        }

        String[] moveList;
        if  (player == 1) moveList = MoveGenerator.possibleMovesWhite(chessboard).split("-");
        else moveList = MoveGenerator.possibleMovesBlack(chessboard).split("-");
        player = 1 - player;
        String desiredMove = "BBBB";
        if (moveList.length != 0) {
            for (int i=0;i<moveList.length;i++) {
                if (moveList[i].length() == 4) {
                    String movedBoard = ChessBoardFactory.simulateMove(moveList[i], BitBoardCalculations.chessBoardToString(chessboard));
                    if (!movedBoard.equals("BBBB")) {
                        String result = alphaBetaMax3(depth - 1, alpha, beta, ChessBoardFactory.generateChessBoardFromString(movedBoard), player, moveList[i]);
                        if (!result.substring(0, 4).equals("BBBB")) {
                            int value = Integer.valueOf(result.substring(5));
                            if (value <= alpha) {
                                return "BBBBb" + alpha;
                            }
                            if (value < beta) {
                                beta = value;
                                desiredMove = result.substring(0, 4);
                                moveStack[depth] = previousMove;
                            }
                        }
                    }
                }
            }
        }
        return desiredMove + "b" + beta;
    }
}
