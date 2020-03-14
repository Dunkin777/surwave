ALTER TABLE survey ALTER COLUMN description DROP NOT NULL;
ALTER SEQUENCE survey_user_song_id_seq RENAME TO option_id_seq;
