/*
 * MIT License
 *
 * Copyright (c) 2021 Andrew SASSOYE, Constantin GUNDUZ, Gregory VAN DER PLUIJM, Thomas LEUTSCHER
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

-- Populate table --

-- First user --
insert into game_stats (user_id, high_score, nb_won, nb_lost, highest_level)
values (5, 100, 10, 5, 8);

insert into tetrimino_stats (user_id, total_burns, single, doubles, triple, tetris, soft_drop,
                             hard_drop)
values (5, 3, 5, 6, 2, 6, 8, 4);

insert into user (id, username)
values (5, "Gregory");

-- Second user --

insert into game_stats (user_id, high_score, nb_won, nb_lost, highest_level)
values (5, 64, 542, 243, 76);

insert into tetrimino_stats (user_id, total_burns, single, doubles, triple, tetris, soft_drop,
                             hard_drop)
values (5, 8765, 5235, 567, 35, 6, 24, 4);

insert into user (id, username)
values (6, "Andrew");

-- Third user --

insert into game_stats (user_id, high_score, nb_won, nb_lost, highest_level)
values (5, 64, 542, 243, 76);

insert into tetrimino_stats (user_id, total_burns, single, doubles, triple, tetris, soft_drop,
                             hard_drop)
values (5, 85, 55, 567, 358, 63, 2, 84);

insert into user (id, username)
values (7, "Aro");

