DROP TABLE IF EXISTS "user_post_likes";
DROP TABLE IF EXISTS "users";
DROP TABLE IF EXISTS "post";

CREATE TABLE "users" (
  "id" bigint generated by default as identity PRIMARY KEY,
  "created" timestamp,
  "updated" timestamp,
  "username" varchar(50) UNIQUE NOT NULL
);

CREATE TABLE "post" (
  "id" bigint generated by default as identity PRIMARY KEY,
  "created" timestamp,
  "updated" timestamp,
  "description" varchar(1500) NOT NULL,
  "filename" varchar(2048) NOT NULL
);

CREATE TABLE "user_post_likes" (
  "liker_id" bigint,
  "owner_id" bigint,
  "post_id" bigint
);

ALTER TABLE "user_post_likes" ADD FOREIGN KEY ("liker_id") REFERENCES "users" ("id");

ALTER TABLE "user_post_likes" ADD FOREIGN KEY ("owner_id") REFERENCES "users" ("id");

ALTER TABLE "user_post_likes" ADD FOREIGN KEY ("post_id") REFERENCES "post" ("id");
