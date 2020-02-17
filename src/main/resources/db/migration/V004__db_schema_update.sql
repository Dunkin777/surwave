drop table if exists survey_song;
drop table if exists user_song;

alter table if exists song drop column if exists survey_id;
alter table if exists survey_user_song rename to option;
alter table if exists vote rename column survey_user_song_id to option_id;
alter table user_role add primary key (roles, user_id);