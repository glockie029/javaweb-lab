create table app_user (
    id bigint primary key,
    username varchar(64) not null unique,
    password varchar(128) not null,
    nickname varchar(128) not null,
    email varchar(255) not null,
    role varchar(32) not null
);
