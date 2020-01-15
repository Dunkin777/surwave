drop table if exists user_role;
drop table if exists app_user;

create table if not exists app_user
(
    id         varchar(100) unique  not null,
    username   varchar(40)          not null,
    password   varchar(40),
    active     boolean  not null,
    email      varchar(40)          not null,
    avatar_url varchar(255)         not null,
    locale     varchar(40)          not null,
    last_visit date                 ,
    primary key (id)
);

create table if not exists user_role
(
    roles   varchar(40)  not null,
    user_id varchar(255) not null,
    foreign key (user_id) references app_user (id)
);