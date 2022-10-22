alter table if exists group_subscribers
    drop constraint if exists group_subscribers_group_fk;

alter table if exists group_subscribers
    drop constraint if exists group_subscribers_user_fk;


drop table if exists group_subscribers cascade;


create table group_subscribers
(
    group_id int8 not null,
    user_id  int8 not null,
    primary key (group_id, user_id)
);

alter table if exists group_subscribers
    add constraint group_subscribers_group_fk foreign key (group_id) references groups;
alter table if exists group_subscribers
    add constraint group_subscribers_user_fk foreign key (user_id) references users;