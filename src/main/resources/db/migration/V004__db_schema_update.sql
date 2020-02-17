drop table if exists survey_song;
drop table if exists user_song;

alter table if exists song drop column survey_id;
alter table if exists survey_user_song rename to option;
