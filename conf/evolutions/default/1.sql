# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table message (
  id                        bigint not null,
  timestamp                 timestamp,
  url                       varchar(1024),
  content_type              varchar(255),
  content_encoding          varchar(255),
  data                      bytea,
  parent_id                 bigint,
  constraint pk_message primary key (id))
;

create sequence message_seq;

alter table message add constraint fk_message_parent_1 foreign key (parent_id) references message (id);
create index ix_message_parent_1 on message (parent_id);



# --- !Downs

drop table if exists message cascade;

drop sequence if exists message_seq;

