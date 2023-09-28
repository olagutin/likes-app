CREATE TABLE IF NOT EXISTS likes.users
(
    id        SERIAL    NOT NULL,
    firstname varchar   NOT NULL,
    lastname  varchar   NOT NULL,
    nickname  varchar   NOT NULL,
    likes     int8      NULL,
    created   timestamp NULL,
    updated   timestamp NULL,

    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT nickname_unique UNIQUE (nickname)
    );


CREATE TABLE IF NOT EXISTS likes.history
(
    id       SERIAL    NOT NULL,
    nickname varchar   NULL,
    likes    int8      NULL,
    status   varchar   NULL,
    created  timestamp NULL,

    CONSTRAINT history_pk PRIMARY KEY (id)
    );

INSERT INTO likes.users (id, firstname, lastname, nickname, likes, created, updated)
VALUES (1, 'John', 'Doe', 'Spring best practice', 0, now(), now());

DELETE FROM likes.users;
