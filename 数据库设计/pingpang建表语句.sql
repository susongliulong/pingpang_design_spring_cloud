/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2024/3/12 21:16:08                           */
/*==============================================================*/


drop table if exists account;

drop table if exists admin;

drop table if exists article;

drop table if exists article_category;

drop table if exists basic_information;

drop table if exists comment;

drop table if exists interest;

drop table if exists `match`;

drop table if exists media;

drop table if exists report;

drop table if exists tutorial;

drop table if exists user;

/*==============================================================*/
/* Table: account                                               */
/*==============================================================*/
create table account
(
   id                   bigint not null,
   name                 char(40),
   telephone            char(11),
   email                char(40),
   `desc`               char(100),
   primary key (id)
);

/*==============================================================*/
/* Table: admin                                                 */
/*==============================================================*/
create table admin
(
   adminId              bigint not null,
   account              char(20),
   password             char(20),
   level                int,
   primary key (adminId)
);

/*==============================================================*/
/* Table: article                                               */
/*==============================================================*/
create table article
(
   articleId            bigint not null,
   content              text,
   primary key (articleId)
);

/*==============================================================*/
/* Table: article_category                                      */
/*==============================================================*/
create table article_category
(
   id                   bigint not null,
   category             char(30),
   articleId            bigint,
   primary key (id)
);

/*==============================================================*/
/* Table: basic_information                                     */
/*==============================================================*/
create table basic_information
(
   authorId             bigint,
   id                   bigint not null,
   title                char(60),
   coverImage           char(100),
   publishTime          datetime,
   abstract             char(200),
   likes                int,
   comments             int,
   collects             int,
   pageView             int,
   state                int,
   userId               bigint,
   primary key (id)
);

/*==============================================================*/
/* Table: comment                                               */
/*==============================================================*/
create table comment
(
   commentId            bigint not null,
   ipAddress            char(20),
   primary key (commentId)
);

/*==============================================================*/
/* Table: interest                                              */
/*==============================================================*/
create table interest
(
   id                   bigint not null,
   code                 int,
   name                 char(20),
   userId               bigint,
   primary key (id)
);

alter table interest comment '描述用户感兴趣的话题';

/*==============================================================*/
/* Table: "match"                                               */
/*==============================================================*/
create table `match`
(
   matchId              bigint not null,
   time                 datetime,
   address              char(100),
   playNumer            int,
   `desc`               char(100),
   minPoints            int,
   awards               int,
   contact              char(40),
   primary key (matchId)
);

/*==============================================================*/
/* Table: media                                                 */
/*==============================================================*/
create table media
(
   id                   bigint not null,
   `desc`               char(100),
   size                 int,
   location             char(100),
   articleId            bigint,
   primary key (id)
);

/*==============================================================*/
/* Table: report                                                */
/*==============================================================*/
create table report
(
   id                   bigint not null,
   authorId             bigint,
   articleId            bigint,
   sourceId             bigint,
   `desc`              char(200),
   reportTime           datetime,
   category             int,
   primary key (id)
);

/*==============================================================*/
/* Table: tutorial                                              */
/*==============================================================*/
create table tutorial
(
   tutorialId           bigint not null,
   level                char(20),
   sutiableObject       char(100),
   content              text,
   primary key (tutorialId)
);

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   id                   bigint not null,
   avator               char(100),
   state                int,
   llastLoginTime       datetime,
   playYears            int,
   primary key (id)
);

alter table article_category add constraint FK_b_a_c foreign key (articleId)
      references basic_information (id) on delete restrict on update restrict;

alter table basic_information add constraint FK_Relationship_1 foreign key (userId)
      references user (id) on delete restrict on update restrict;

alter table interest add constraint FK_u_i foreign key (id)
      references user (id) on delete restrict on update restrict;

alter table media add constraint FK_b_i_m foreign key (articleId)
      references basic_information (id) on delete restrict on update restrict;
