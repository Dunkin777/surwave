drop table if exists vote;

create table vote
(
    id                  bigserial not null,
    survey_user_song_id bigint,
    participant_id      varchar(255),
    rating              bigint,

    primary key (id),
    foreign key (survey_user_song_id) references survey_user_song (id),
    foreign key (participant_id) references app_user (id)
);
