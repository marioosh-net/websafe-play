# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table message (
  id                        bigint not null,
  timestamp                 timestamp,
  url                       varchar(1024),
  host                      varchar(1024),
  content_type              varchar(255),
  content_encoding          varchar(255),
  description               varchar(1024),
  title                     varchar(1024),
  clicks                    integer,
  data                      bytea,
  parent_id                 bigint,
  constraint pk_message primary key (id))
;

create table tag (
  id                        bigint not null,
  name                      varchar(255),
  timestamp                 timestamp,
  constraint pk_tag primary key (id))
;


create table messages_tags (
  message_id                     bigint not null,
  tag_id                         bigint not null,
  constraint pk_messages_tags primary key (message_id, tag_id))
;
create sequence message_seq;

create sequence tag_seq;

alter table message add constraint fk_message_parent_1 foreign key (parent_id) references message (id);
create index ix_message_parent_1 on message (parent_id);



alter table messages_tags add constraint fk_messages_tags_message_01 foreign key (message_id) references message (id);

alter table messages_tags add constraint fk_messages_tags_tag_02 foreign key (tag_id) references tag (id);

# --- !Downs

drop table if exists message cascade;

drop table if exists messages_tags cascade;

drop table if exists tag cascade;

drop sequence if exists message_seq;

drop sequence if exists tag_seq;

