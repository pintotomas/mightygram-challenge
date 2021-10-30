DROP TABLE IF EXISTS "user_post";
DROP TABLE IF EXISTS "users";
DROP TABLE IF EXISTS "post";

CREATE TABLE "users" (
  "id" bigint PRIMARY KEY,
  "created" timestamp,
  "updated" timestamp,
  "username" varchar(50) UNIQUE NOT NULL
);

CREATE TABLE "post" (
  "id" bigint PRIMARY KEY,
  "created" timestamp,
  "updated" timestamp,
  "description" varchar(1500) NOT NULL,
  "photo_url" varchar(2048) NOT NULL
);

CREATE TABLE "user_post" (
  "id_user" bigint,
  "id_post" bigint
);

ALTER TABLE "user_post" ADD FOREIGN KEY ("id_user") REFERENCES "users" ("id");

ALTER TABLE "user_post" ADD FOREIGN KEY ("id_post") REFERENCES "post" ("id");
