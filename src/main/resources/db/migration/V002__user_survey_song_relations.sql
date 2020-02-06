drop table if exists survey_user_song_link;
create table survey_user_song_link
(
    id        bigserial not null,
    song_id   bigint,
    survey_id bigint,
    user_id   varchar(255),

    primary key (id),
    foreign key (song_id) references song (id),
    foreign key (survey_id) references survey (id),
    foreign key (user_id) references app_user (id)
);