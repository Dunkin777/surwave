drop table if exists song_rating;
drop table if exists answer;

create table answer
(
    id        bigserial not null,
    survey_id bigint,
    user_id   varchar(255),

    primary key (id),
    foreign key (survey_id) references survey (id),
    foreign key (user_id) references app_user (id)
);

create table song_rating
(
    answer_id bigint,
    song_id   bigint not null,
    rating    integer,


    primary key (answer_id, song_id),
    foreign key (answer_id) references answer (id),
    foreign key (song_id) references song (id)
);
