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
    private List<PointImpl> emptyСells= new ArrayList<>();
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
        CommandChain res = new CommandChain();
        int pointX=board.getGlass().getFreeSpace().get(0).getX();
        int pointX1 = checkPlace(board, res).getX();
        System.out.println(pointX1);
        if(pointX1<8) res.then(left[7-pointX1]);
        else if(pointX1>8) res.then(right[pointX1-9]);
        else res.then(Command.DOWN);
        return res;

    }
    /**
     *  нахождение места под фигуру
     */
    public PointImpl checkPlace(Board board, CommandChain res){

        GlassBoard glassBoard = board.getGlass();
        Elements type = board.getCurrentFigureType();
        int y; int x;
        for(int i=0;;i++) {
            y = glassBoard.getFreeSpace().get(i).getY();
            x = glassBoard.getFreeSpace().get(i).getX();
            System.out.println("ноль "+glassBoard.getFreeSpace().get(i));
            if(emptiness(x, y, board, res) == false){
                if(!emptyСells.contains(glassBoard.getFreeSpace().get(i))){
                    if(y!=0){
                            if(type.index()==1){
                                if (board.getGlass().isFree(x, y - 1) == true ||
                                        board.getGlass().isFree(x+1, y - 1) == true) break;
                            }
                            if (type.index() == 3) {
                                if (board.getGlass().isFree(x, y - 1) == false &&
                                        board.getGlass().isFree(x - 1, y - 1) == false){
                                    System.out.println("проверочка");
                                    break;
                                }
                                else{
                                    System.out.println("проверочка 2");
                                }
                            }
                        /*if(type.index()==3)
                            if (board.getGlass().isFree(x-1, y - 1) == true && y!=0) i++;*/
                            else{
                                System.out.println("четыре "+glassBoard.getFreeSpace().get(i));
                                break;
                            }
                    }
                    else{
                        System.out.println("пять "+glassBoard.getFreeSpace().get(i));
                        break;
                    }
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
    private boolean emptiness(int x, int y, Board board, CommandChain res){
        Elements typeFigure = board.getCurrentFigureType();
        if(typeFigure.index()==1){
            System.out.println("проверка квадрата 1");
            if(x==17){
                System.out.println("проверка квадрата 2");
                emptyСells.add(new PointImpl(x,y));
                return true;
            }
            else if (board.getGlass().isFree(x+1, y)==false ){
                System.out.println("проверка квадрата 3");
                emptyСells.add(new PointImpl(x,y));
                return true;
            }
        }
        if(typeFigure.index()==2){
            boolean cell2 = board.getGlass().isFree(x+1, y);
            boolean cell3 = board.getGlass().isFree(x+2, y);
            boolean cell4 = board.getGlass().isFree(x+3, y);
            if(cell2==true && cell3==true && cell4==true){
                res.then(Command.ROTATE_CLOCKWISE_90)
                        .then(Command.RIGHT)
                        .then(Command.RIGHT);
            }
            return false;
        }
        if(typeFigure.index()==3){
            /** проверка поворота на 90 градусов*/
            boolean cellOne90 = board.getGlass().isFree(x+1, y);
            boolean cellTwo90 = board.getGlass().isFree(x+2, y);
            if(x!=17 && cellOne90==true && cellTwo90==true){
                res.then(Command.ROTATE_CLOCKWISE_90);
                return false;
            }
            else{
                if(x<16 && cellOne90==true) return false;
                else{
                    if(x!=0 || x!=1){
                        /** проверка поворота на 270 градусов*/
                        boolean cellOne270 = board.getGlass().isFree(x-1, y);
                        boolean cellTwo270 = board.getGlass().isFree(x-2, y);
                        boolean cellThree270 = board.getGlass().isFree(x-1, y+1);
                        boolean cellFour270 = board.getGlass().isFree(x-2, y+1);
                        System.out.println("проверочка 3");
                        if(cellOne270==false && cellTwo270==false && cellThree270==true && cellFour270==true){
                            res.then(Command.ROTATE_CLOCKWISE_270).then(Command.LEFT).then(Command.LEFT);
                            System.out.println("проверочка 4");
                            return false;
                        }
                    }
                    if(x!=17){
                        boolean cellOne180 = board.getGlass().isFree(x+1, y);
                        boolean cellTwo180 = board.getGlass().isFree(x+1, y+1);
                        boolean cellThree180 = board.getGlass().isFree(x+1, y+2);
                        System.out.println("проверочка 5");
                        if(cellOne180==false && cellTwo180==false && cellThree180==true){
                            res.then(Command.ROTATE_CLOCKWISE_180).then(Command.LEFT);
                            System.out.println("проверочка 6");
                            return false;
                        }
                        else{
                            emptyСells.add(new PointImpl(x,y));
                            return true;
                        }
                    }
                    else{
                        System.out.println("проверочка 7");
                        emptyСells.add(new PointImpl(x,y));
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // скопируйте сюда адрес из браузера, на который перейдете после регистрации/логина
                "http://codebattle2020.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/ufbhiou615sio4unf5pv?code=5507207268535741977&gameName=tetris",
                new YourSolver(new RandomDice()),
                new Board());
    }

}
