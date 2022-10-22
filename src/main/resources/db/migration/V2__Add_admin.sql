insert into users (id,username,email,password,is_active)
values (1,'admin','admin@admin.admin','1',true);

insert into user_role (user_id,roles)
values (1,'USER'), (1,'ADMIN');