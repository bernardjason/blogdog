# --- !Ups


create table "USERS" ("ID" INTEGER PRIMARY KEY,"USER" VARCHAR NOT NULL,"PASSWORD" VARCHAR NOT NULL,"NICKNAME" VARCHAR NOT NULL);

create table "BLOGS" (ID INTEGER PRIMARY KEY,USERS_ID INTEGER NOT NULL, USER VARCHAR NOT NULL,"WHEN" TIMESTAMP NOT NULL, WHAT VARCHAR NOT NULL , FOREIGN KEY(USERS_ID) REFERENCES USERS(ID));

insert into "USERS" ("USER","PASSWORD","NICKNAME") values ("bernard","jason","Dont call me Bernie");	

insert into "USERS" ("USER","PASSWORD","NICKNAME") values ("delete","me","Showing integrity");	

# --- !Downs

drop table "BLOGS";

drop table "USERS";
