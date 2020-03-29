drop table if exists user_role;

create table if not exists user_role
(
    id      bigserial unique not null,
    role    varchar(40)      not null,
    user_id varchar(255)     not null,
    primary key (id),
    foreign key (user_id) references app_user (id)
);
