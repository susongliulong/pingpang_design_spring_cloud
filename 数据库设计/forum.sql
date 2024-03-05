drop table if exists annotation;

drop table if exists article;

drop table if exists draft;

drop table if exists hot_spot;

drop table if exists knowledge;

drop table if exists life_sharing;

drop table if exists passage;

drop table if exists recommend;

drop table if exists report;

drop table if exists search_message;

drop table if exists user;

drop table if exists user_message;

/*==============================================================*/
/* Table: annotation                                            */
/*==============================================================*/
create table annotation
(
   id                   bigint not null,
   user_id              bigint,
   art_id               bigint,
   content              text,
   likes                int,
   type                 int,
   orginal_content      text,
   level                char(15),
   ann_a                bigint,
   time                 date,
   primary key (id)
);

alter table annotation comment '段落批注的评论';


/*==============================================================*/
/* Table: article                                               */
/*==============================================================*/
create table article
(
   id                   bigint not null,
   user_id              bigint,
   title                text,
   main_content         text,
   img                  char(25),
   watchs               int,
   likes                int,
   collections          int,
   create_time          date,
   update_time          date,
   type                 char(12),
   state                int,
   primary key (id)
);


/*==============================================================*/
/* Table: draft                                                 */
/*==============================================================*/
create table draft
(
   id                   bigint not null,
   content              text,
   primary key (id)
);


/*==============================================================*/
/* Table: hot_spot                                              */
/*==============================================================*/
create table hot_spot
(
   id                   bigint not null,
   content              text,
   links                text,
   original             int,
   statement            text,
   primary key (id)
);


/*==============================================================*/
/* Table: knowledge                                             */
/*==============================================================*/
create table knowledge
(
   id                   bigint not null,
   art_id               bigint,
   end                  text,
   warning              text,
   passages             int,
   primary key (id)
);

/*==============================================================*/
/* Table: life_sharing                                          */
/*==============================================================*/
create table life_sharing
(
   id                   bigint not null,
   content              text,
   img1                 char(40),
   img2                 char(40),
   img3                 char(40),
   img4                 char(40),
   img5                 char(40),
   imgs                 text,
   primary key (id)
);


/*==============================================================*/
/* Table: passage                                               */
/*==============================================================*/
create table passage
(
   id                   bigint not null,
   main_content         text,
   argue                char(12),
   content              text,
   sequence             int,
   primary key (id)
);


/*==============================================================*/
/* Table: recommend                                             */
/*==============================================================*/
create table recommend
(
   id                   bigint not null,
   user_id              bigint,
   passages             int,
   end                  text,
   key_passage          int,
   content              text,
   primary key (id)
);

/*==============================================================*/
/* Table: report                                                */
/*==============================================================*/
create table report
(
   id                   bigint not null,
   user_id              bigint,
   time                 date,
   type                 int,
   content              text,
   primary key (id)
);

/*==============================================================*/
/* Table: search_message                                        */
/*==============================================================*/
create table search_message
(
   id                   bigint not null,
   user_id              bigint,
   key1                 char(15),
   key2                 char(15),
   key3                 char(15),
   times                int,
   primary key (id)
);

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   telephone            char(11),
   password             char(15),
   state                int,
   id                   bigint not null,
   primary key (id)
);

alter table user comment '储存用户的登录信息';


/*==============================================================*/
/* Table: user_message                                          */
/*==============================================================*/
create table user_message
(
   name                 char(12),
   img                  text,
   age                  int,
   birthday             date,
   sex                  char(4),
   job                  char(10),
   interest             text,
   fans                 int,
   focus                int,
   id                   bigint not null,
   primary key (id)
);


alter table annotation add constraint FK_article_a foreign key (art_id)
      references article (id) on delete cascade on update cascade;

alter table annotation add constraint FK_user_a foreign key (user_id)
      references user (id) on delete cascade on update cascade;

alter table article add constraint FK_user_article foreign key (user_id)
      references user (id) on delete cascade on update cascade;

alter table draft add constraint FK_Reference_14 foreign key (id)
      references article (id) on delete restrict on update restrict;

alter table hot_spot add constraint FK_Reference_15 foreign key (id)
      references article (id) on delete restrict on update restrict;

alter table knowledge add constraint FK_article_k foreign key (art_id)
      references article (id) on delete cascade on update cascade;

alter table life_sharing add constraint FK_Reference_13 foreign key (id)
      references article (id) on delete restrict on update restrict;

alter table passage add constraint FK_Reference_16 foreign key (id)
      references knowledge (id) on delete restrict on update restrict;

alter table recommend add constraint FK_user_re foreign key (user_id)
      references user (id) on delete cascade on update cascade;

alter table report add constraint FK_user_r foreign key (user_id)
      references user (id) on delete cascade on update cascade;

alter table search_message add constraint FK_user_s foreign key (user_id)
      references user (id) on delete cascade on update cascade;

alter table user_message add constraint FK_Reference_12 foreign key (id)
      references user (id) on delete restrict on update restrict;

