DROP TABLE IF EXISTS "user_post_likes";
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

CREATE TABLE "user_post_likes" (
  "user_id" bigint,
  "post_id" bigint
);

ALTER TABLE "user_post_likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "user_post_likes" ADD FOREIGN KEY ("post_id") REFERENCES "post" ("id");
