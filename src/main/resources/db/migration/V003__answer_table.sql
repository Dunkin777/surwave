drop table if exists vote;

create table vote
(
    id        bigserial not null,
    survey_id bigint,
    user_id   varchar(255),
    song_id bigint,
    rating bigint,

    primary key (id),
    foreign key (survey_id) references survey (id),
    foreign key (user_id) references app_user (id),
    foreign key (song_id) references song (id)
);
