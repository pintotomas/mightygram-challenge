 INSERT INTO users
            (id,
             created,
             updated,
             username)
VALUES      (1,
             '1/19/2021',
             '12/22/2020',
             'gfraczkiewicz0');

INSERT INTO users
            (id,
             created,
             updated,
             username)
VALUES      (2,
             '12/4/2020',
             '7/31/2021',
             'mjonin1');

INSERT INTO users
            (id,
             created,
             updated,
             username)
VALUES      (3,
             '5/21/2021',
             '5/31/2021',
             'jchippin2');

INSERT INTO users
            (id,
             created,
             updated,
             username)
VALUES      (4,
             '10/10/2021',
             '8/28/2021',
             'nmullard3');

INSERT INTO users
            (id,
             created,
             updated,
             username)
VALUES      (5,
             '2/25/2021',
             '10/15/2021',
             'rguerreru4');

INSERT INTO users
            (id,
             created,
             updated,
             username)
VALUES      (6,
             '2/25/2021',
             '10/15/2021',
             'admin');

INSERT INTO post
            (id,
             created,
             updated,
             description,
             filename)
VALUES      (1,
             '5/5/2021',
             '12/16/2020',
             'Ágios Týchon',
             'img.png');

INSERT INTO post
            (id,
             created,
             updated,
             description,
             filename)
VALUES      (2,
             '2/5/2021',
             '11/18/2020',
             'Syntul',
             'img.png');

INSERT INTO post
            (id,
             created,
             updated,
             description,
             filename)
VALUES      (3,
             '11/21/2020',
             '12/11/2020',
             'Sidowayah Lor',
             'img.png');

INSERT INTO post
            (id,
             created,
             updated,
             description,
             filename)
VALUES      (4,
             '4/16/2021',
             '2/12/2021',
             'Ludza',
             'img.png');

INSERT INTO post
            (id,
             created,
             updated,
             description,
             filename)
VALUES      (5,
             '5/19/2021',
             '9/5/2021',
             'Zhihe',
             'img.png');

INSERT INTO post
            (id,
             created,
             updated,
             description,
             filename)
VALUES      (6,
             '7/11/2021',
             '11/11/2020',
             'Bang Len',
             'img.png');

INSERT INTO post
            (id,
             created,
             updated,
             description,
             filename)
VALUES      (7,
             '7/8/2021',
             '7/29/2021',
             'Furudate',
             'img.png');

INSERT INTO post
            (id,
             created,
             updated,
             description,
             filename)
VALUES      (8,
             '5/27/2021',
             '4/22/2021',
             'Donetsk',
             'img.png');

INSERT INTO post
            (id,
             created,
             updated,
             description,
             filename)
VALUES      (9,
             '5/14/2021',
             '4/17/2021',
             'Nandayure',
             'img.png');

INSERT INTO post
            (id,
             created,
             updated,
             description,
             filename)
VALUES      (10,
             '6/24/2021',
             '8/6/2021',
             'Manūjān',
             'img.png');

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (1,
             1,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (1,
             3,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (1,
             4,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (1,
             6,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (2,
             1,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (2,
             2,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (2,
             8,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (3,
             8,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (3,
             4,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (3,
             5,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (4,
             7,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (4,
             9,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (5,
             1,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (5,
             2,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id, 
            owner_id)
VALUES      (5,
             3,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id,
             owner_id)
VALUES      (5,
             4,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id,
              owner_id)
VALUES      (5,
             6,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id,
              owner_id)
VALUES      (5,
             7,
             6);

INSERT INTO user_post_likes
            (liker_id,
             post_id,
              owner_id)
VALUES      (5,
             8,
             6);

ALTER TABLE users ALTER COLUMN id RESTART WITH 6;
ALTER TABLE post ALTER COLUMN id RESTART WITH 11;
