drop table if exists survey_user;
create table if not exists survey_user
(
    survey_id bigint       not null,
    user_id   varchar(255) not null,

    foreign key (survey_id) references survey (id),
    foreign key (user_id) references app_user (id),
    primary key (user_id, survey_id)
);