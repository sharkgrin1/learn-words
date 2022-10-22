alter table if exists group_word
    drop constraint if exists FKdtky5akucjrkwdyv29cywnn5i;
alter table if exists group_word
    drop constraint if exists FKrhkgkxvdi60jrbyi2gur8nib0;
alter table if exists groups
    drop constraint if exists FK6lsrp1ywkyn1havsj48q87jul;
alter table if exists translation
    drop constraint if exists FKkobtf2ub1uhh53rtmi9q1h6h2;
alter table if exists user_role
    drop constraint if exists FKj345gk1bovqvfame88rcx7yyx;
alter table if exists word
    drop constraint if exists FKcbq2lbfi4wt3uhgfefj9k1gr1;
drop table if exists group_word cascade;
drop table if exists groups cascade;
drop table if exists translation cascade;
drop table if exists user_role cascade;
drop table if exists users cascade;
drop table if exists word cascade;
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start 2 increment 1;
create table group_word
(
    group_id int8 not null,
    word_id  int8 not null,
    primary key (group_id, word_id)
);
create table groups
(
    id              int8         not null,
    description     varchar(255),
    name_group      varchar(255) not null,
    group_author_id int8         not null,
    primary key (id)
);
create table translation
(
    word_id int8 not null,
    name    varchar(255)
);
create table user_role
(
    user_id int8 not null,
    roles   varchar(255)
);
create table users
(
    id              int8         not null,
    activation_code varchar(255),
    email           varchar(255),
    is_active       boolean      not null,
    password        varchar(255) not null,
    username        varchar(255) not null,
    primary key (id)
);
create table word
(
    id             int8         not null,
    name           varchar(255) not null,
    word_author_id int8         not null,
    primary key (id)
);
alter table if exists users
    add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);
alter table if exists group_word
    add constraint FKdtky5akucjrkwdyv29cywnn5i foreign key (word_id) references word;
alter table if exists group_word
    add constraint FKrhkgkxvdi60jrbyi2gur8nib0 foreign key (group_id) references groups;
alter table if exists groups
    add constraint FK6lsrp1ywkyn1havsj48q87jul foreign key (group_author_id) references users;
alter table if exists translation
    add constraint FKkobtf2ub1uhh53rtmi9q1h6h2 foreign key (word_id) references word;
alter table if exists user_role
    add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users;
alter table if exists word
    add constraint FKcbq2lbfi4wt3uhgfefj9k1gr1 foreign key (word_author_id) references users;