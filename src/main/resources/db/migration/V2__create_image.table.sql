create table question_images
(
    question_id  bigint not null,
    content_type varchar(255),
    image        longblob,
    name         varchar(255),
    size         bigint not null
);

alter table question_images
    add constraint question_images_fk foreign key (question_id) references question (id);
