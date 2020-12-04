package com.codenjoy.dojo.tetris.client;

import static com.codenjoy.dojo.services.Command.DOWN;
import static com.codenjoy.dojo.services.Command.LEFT;
import static com.codenjoy.dojo.services.Command.RIGHT;

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

        return left[5];

    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // скопируйте сюда адрес из браузера, на который перейдете после регистрации/логина
                "http://codebattle2020.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/phxn1seylr4qai4f4v6n?code=7745898147695142163",
                new YourSolver(new RandomDice()),
                new Board());
    }

}
