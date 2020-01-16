drop table if exists survey;
create table if not exists survey
(
    id                       bigserial unique not null,
    type                     varchar(40)      not null,
    description              text             not null,
    proposals_by_user        int              not null,
    state                    varchar(40)      not null,
    is_hidden                boolean          not null,
    choices_by_user          int,
    logarithmic_rating_scale boolean,
    survey_type              varchar(255),
    primary key (id)
);

drop table if exists song;
create table if not exists song
(
    id         bigserial  unique  not null,
    comment    varchar(255),
    media_path varchar(255),
    performer  varchar(255) not null,
    survey_id  bigint,
    title      varchar(255) not null,
    primary key (id)
);

drop table if exists survey_song;
create table if not exists survey_song
(
    survey_id bigint not null,
    song_id   bigint not null,
    foreign key (survey_id) references survey (id),
    foreign key (song_id) references song (id),
    primary key (survey_id, song_id)
);

drop table if exists user_role;
drop table if exists app_user;

create table if not exists app_user
(
    id         varchar(100) unique not null,
    username   varchar(40)         not null,
    password   varchar(40),
    active     boolean             not null,
    email      varchar(40)         not null,
    avatar_url varchar(255)        not null,
    locale     varchar(40)         not null,
    last_visit date,
    primary key (id)
);

create table if not exists user_role
(
    roles   varchar(40)  not null,
    user_id varchar(255) not null,
    foreign key (user_id) references app_user (id)
);

drop table if exists user_song;
create table if not exists user_song
(
    user_id varchar(255) not null,
    song_id   bigint not null,
    foreign key (user_id) references app_user (id),
    foreign key (song_id) references song (id),
    primary key (user_id, song_id)
);