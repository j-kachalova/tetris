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
import com.codenjoy.dojo.services.Command;
import com.codenjoy.dojo.services.CommandChain;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

        import static com.codenjoy.dojo.services.Command.*;

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class YourSolver extends AbstractJsonSolver<Board> {

    private Dice dice;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String getAnswer(Board board) {
        return getAnswerList(board).toString();
    }

    private CommandChain getAnswerList(Board board) {
        System.out.println(board.getGlass().getAt(board.getCurrentFigurePoint()));
        System.out.println(board.getCurrentFigureType());
        CommandChain left1 = new CommandChain();
        left1.then(LEFT);
        CommandChain left2 = left1.then(LEFT);
        CommandChain left3 = left2.then(LEFT);
        CommandChain left4 = left3.then(LEFT);
        CommandChain left5 = left4.then(LEFT);
        CommandChain left6 = left5.then(LEFT);
        CommandChain left7 = left6.then(LEFT);
        CommandChain left8 = left7.then(LEFT);
        CommandChain right1 = new CommandChain();
        right1.then(RIGHT);
        CommandChain right2 = right1.then(RIGHT);
        CommandChain right3 = right2.then(RIGHT);
        CommandChain right4 = right3.then(RIGHT);
        CommandChain right5 = right4.then(RIGHT);
        CommandChain right6 = right5.then(RIGHT);
        CommandChain right7 = right6.then(RIGHT);
        CommandChain right8 = right7.then(RIGHT);
        return right8.then(Command.DOWN);
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // скопируйте сюда адрес из браузера, на который перейдете после регистрации/логина
                "http://codebattle2020.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/phxn1seylr4qai4f4v6n?code=7745898147695142163",
                new YourSolver(new RandomDice()),
                new Board());
    }

}
