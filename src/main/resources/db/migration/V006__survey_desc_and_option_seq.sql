DROP TABLE if exists option CASCADE;

CREATE TABLE option
(
    id        bigserial not null,
    song_id   bigint,
    survey_id bigint,
    user_id   varchar(255),
    comment   varchar(255),

    primary key (id),
    foreign key (song_id) references song (id),
    foreign key (survey_id) references survey (id),
    foreign key (user_id) references app_user (id)
);

ALTER TABLE survey ALTER COLUMN description DROP NOT NULL;
