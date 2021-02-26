create table sign_ins (
  id                      varchar2(36)        not null constraint sign_ins primary key,
  version                 smallint,
  email                   varchar2(255 char),
  otp                     varchar2(10)        not null,
  created_at              timestamp           not null,
  otp_sent_at             timestamp           not null
);