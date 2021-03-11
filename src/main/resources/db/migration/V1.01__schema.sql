
CREATE TABLE IF NOT EXISTS sign_ins (
  id                      varchar(36)        not null constraint sign_ins primary key,
  version                 smallint,
  email                   varchar(255),
  otp                     varchar(10)        not null,
  created_at              timestamp          not null,
  otp_sent_at             timestamp          not null
);