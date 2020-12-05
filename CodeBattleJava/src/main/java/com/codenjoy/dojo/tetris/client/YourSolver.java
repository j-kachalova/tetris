package com.codenjoy.dojo.tetris.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.client.AbstractJsonSolver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.tetris.model.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class YourSolver extends AbstractJsonSolver<Board> {

    private Dice dice;
    private int counter = 0;

    private CommandChain[] left = new CommandChain[10];
    private CommandChain[] right = new CommandChain[10];

    public YourSolver(Dice dice) {
        this.dice = dice;
        for (int i = 0; i < 10; i++) {
            left[i] = new CommandChain();
            right[i] = new CommandChain();
        }
        left[0].then(Command.LEFT).then(Command.DOWN);
        right[0].then(Command.RIGHT).then(Command.DOWN);
        for (int i = 1; i < 10; i++) {
            left[i].then(Command.LEFT);
            left[i].then(left[i-1]);
            right[i].then(Command.RIGHT);
            right[i].then(right[i-1]);
        }
    }

    @Override
    public String getAnswer(Board board) {
        return getAnswerList(board).toString();
    }

    private CommandChain getAnswerList(Board board) {


        System.out.println(board.getGlass().getAt(board.getCurrentFigurePoint()));
        System.out.println(board.getCurrentFigureType());
        System.out.println(board.getGlass().getFreeSpace().get(0).getX());
        System.out.println(board.getGlass().getFreeSpace().get(0).getY());
        System.out.println(board.getGlass().getFreeSpace().get(0));
        System.out.println(board.getGlass().getFreeSpace());

        int pointX=board.getGlass().getFreeSpace().get(0).getX();
        int pointX1 = checkPlace(board).getX();
        System.out.println(pointX1);
        CommandChain res = new CommandChain();
        if(pointX1<8) res.then(left[7-pointX1]);
        else if(pointX1>8) res.then(right[pointX1-9]);
        else res.then(Command.DOWN);
        return res;

    }
    /**
     *  нахождение места под фигуру
     */
    public PointImpl checkPlace(Board board){

        GlassBoard glassBoard = board.getGlass();
        Elements type = board.getCurrentFigureType();
        int y; int x;
        for(int i=0;;) {
            y = glassBoard.getFreeSpace().get(i).getY();
            x = glassBoard.getFreeSpace().get(i).getX();
            System.out.println("ноль "+glassBoard.getFreeSpace().get(i));
            if (emptiness(x, y, board) == true){
                i++;
                System.out.println("раз "+glassBoard.getFreeSpace().get(i));
            }
            else{
                if(y!=0){
                    if (board.getGlass().isFree(x, y - 1) == true){
                        i++;
                        System.out.println("два "+glassBoard.getFreeSpace().get(i));
                    }
                    else {
                        if(type.index()==1){
                            if (board.getGlass().isFree(x+1, y - 1) == true){
                                i++;
                                System.out.println("три "+glassBoard.getFreeSpace().get(i));
                            }
                            else break;
                        }
                    /*if(type.index()==3)
                        if (board.getGlass().isFree(x-1, y - 1) == true && y!=0) i++;*/
                        else{
                            System.out.println("четыре "+glassBoard.getFreeSpace().get(i));
                            break;
                        }
                    }
                }
                else{
                    System.out.println("пять "+glassBoard.getFreeSpace().get(i));
                    break;
                }
            }
        }
        return new PointImpl(x,y);
    }
    /**
     * проверка доступа к нижней точке сверху вертикально
     */
    /*private boolean checkLine(int x, int y, Board board) {
        for(int i=y;i<18; i++) {
            if (!board.getGlass().isFree(x, i)) {
                return false;
            }
            if (!emptiness(x, i, board)){

            }
        }
    }*/
    /**
     * проверка на оставление фигурой пустот в данном месте
     * @param x x
     * @param y y
     * @return true если оставляет пустоты
     */
    private boolean emptiness(int x, int y, Board board){
        Elements typeFigure = board.getCurrentFigureType();
        if(typeFigure.index()==1){
            if(x==17) return true;
            else if (board.getGlass().isFree(x+1, y)==false ) return true;
        }
        /*if(typeFigure.index()==2) return false;
        if(typeFigure.index()==3) return false;
        for(int i=x; i<x+typeFigure.getMinSize(); i++){
            if (!board.getGlass().isFree(i, y)){
                return true;
            }

        }*/
        return false;
    }

    /*
    ........I.........
    ........I.........
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    I...I.............
    I...I.............
    IIOOI.I.I........I
    IIOOI.I.I........I
     */
    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // скопируйте сюда адрес из браузера, на который перейдете после регистрации/логина
                "http://codebattle2020.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/phxn1seylr4qai4f4v6n?code=7745898147695142163",
                new YourSolver(new RandomDice()),
                new Board());
    }

}
