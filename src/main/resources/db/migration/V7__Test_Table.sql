alter table if exists test
    drop constraint if exists test_group_fk;

alter table if exists test
    drop constraint if exists test_user_fk;


drop table if exists test cascade;


create table test
(
    id             int8         not null,
    name_test      varchar(255) not null,
    count_words    int8         not null,
    group_id       int8         not null,
    test_author_id int8         not null,
    primary key (id)
);

alter table if exists test
    add constraint test_group_fk foreign key (group_id) references groups;
alter table if exists test
    add constraint test_user_fk foreign key (test_author_id) references users;