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

-- Create tables --

create table sqlite_sequence
(
    name,
    seq
);

CREATE TABLE user
(
    id       INTEGER not null
        constraint user_pk
            primary key autoincrement,
    username varchar not null
);

create table game_stats
(
    user_id       int           not null
        references user,
    high_score    int default 0 not null,
    nb_won        int default 0 not null,
    nb_lost       int default 0 not null,
    highest_level int default 0
);

create unique index game_stats_user_id_uindex
    on game_stats (user_id);


CREATE TABLE tetrimino_stats
(
    user_id     int           not null
        references user,
    total_burns int default 0 not null,
    single      int default 0 not null,
    doubles     int default 0 not null,
    triple      int default 0 not null,
    tetris      int default 0 not null,
    soft_drop   int default 0 not null,
    hard_drop   int default 0 not null
);


create unique index tetrimino_stats_user_id_uindex
    on tetrimino_stats (user_id);


