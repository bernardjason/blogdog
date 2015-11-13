# --- !Ups


create table "BLOGS" ("ID" INTEGER PRIMARY KEY,"USER" VARCHAR NOT NULL,"WHEN" TIMESTAMP NOT NULL, "WHAT" VARCHAR NOT NULL);

create table "USERS" ("ID" INTEGER PRIMARY KEY,"USER" VARCHAR NOT NULL,"PASSWORD" VARCHAR NOT NULL);

insert into "USERS" ("USER","PASSWORD") values ("bernard","jason");	
# --- !Downs

drop table "BLOGS";

drop table "USERS";
