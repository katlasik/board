create table answer
(
    id          bigint        not null auto_increment,
    content     varchar(5000) not null,
    created_on  datetime(6),
    question_id bigint        not null,
    user_id     bigint        not null,
    primary key (id)
);

create table question
(
    id         bigint        not null auto_increment,
    content    varchar(5000) not null,
    created_on datetime(6),
    title      varchar(500)  not null,
    user_id    bigint        not null,
    primary key (id)
);

create table user
(
    id       bigint       not null auto_increment,
    active   bit          not null,
    email    varchar(255) not null unique ,
    name     varchar(255) not null unique,
    password varchar(255) not null,
    role     varchar(255) not null,
    primary key (id)
);

alter table answer
    add constraint answer_question_fk foreign key (question_id) references question (id);

alter table answer
    add constraint answer_user_fk foreign key (user_id) references user (id);

alter table question
    add constraint question_user_fk foreign key (user_id) references user (id);
