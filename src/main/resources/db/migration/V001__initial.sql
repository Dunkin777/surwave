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

drop table if exists survey_song_user;
create table if not exists survey_song_user
(
    survey_id bigint not null,
    song_id   bigint not null,
    foreign key (survey_id) references survey (id),
    foreign key (song_id) references song (id),
    primary key (survey_id, song_id)
);