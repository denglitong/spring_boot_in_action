create table Reader
(
    id       serial primary key,
    username varchar(25) unique not null,
    password varchar(25)        not null,
    fullname varchar(50)        not null
);

create table Book
(
    id              serial primary key,
    author          varchar(50)   not null,
    description     varchar(1000) not null,
    isbn            varchar(10)   not null,
    title           varchar(250)  not null,
    reader_username varchar(25)   not null,
    foreign key (reader_username) references Reader (username)
);

insert into Reader (username, password, fullname)
values ('denglitong', '123456', 'denglitong'),
       ('user', '123456', 'user');