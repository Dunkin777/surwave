CREATE TYPE survey_type AS ENUM ('CLASSIC', 'RANGED');
CREATE TYPE survey_status AS ENUM ('CREATED', 'CLOSED', 'ENDED');

drop table if exists "user";
create table if not exists "user" (
  id bigserial unique not null primary key,
  login varchar(255) not null unique,
  password varchar(255) not null
);

drop table if exists "survey";
create table if not exists "survey" (
  id bigserial unique not null primary key,
  type survey_type not null,
  description text not null,
  proposals_number int not null,
  choices_number int not null,
  status survey_status not null,
  url varchar(255) not null,
  comment text not null
);

drop table if exists "song";
create table if not exists "song" (
  id bigserial unique not null primary key,
  performer varchar(255) not null,
  title varchar(255) not null,
  comment text not null,
  mediaPath varchar(255) not null,
  survey_id bigint references "survey"(id),
  created_by bigint references "user"(id)
);

create table if not exists "answer" (
  id bigserial unique not null primary key,
  participant_id bigint references "user"(id),
  song_id bigint references "song"(id),
  created_date timestamp not null default current_timestamp
);
