CREATE TABLE "user" (
  "id" bigint PRIMARY KEY,
  "created" timestamp,
  "updated" timestamp,
  "username" varchar UNIQUE NOT NULL
);

CREATE TABLE "post" (
  "id" bigint PRIMARY KEY,
  "created" timestamp,
  "updated" timestamp,
  "description" varchar(1500),
  "photo_url" varchar(2048)
);

CREATE TABLE "user_post" (
  "id_user" bigint,
  "id_post" bigint
);

ALTER TABLE "user_post" ADD FOREIGN KEY ("id_user") REFERENCES "user" ("id");

ALTER TABLE "user_post" ADD FOREIGN KEY ("id_post") REFERENCES "post" ("id");
