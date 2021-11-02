# mightygram-challenge
[![Java Build with Maven](https://github.com/pintotomas/mightygram-challenge/actions/workflows/maven.yml/badge.svg)](https://github.com/pintotomas/mightygram-challenge/actions/workflows/maven.yml)

## Api Documentation

## Posts

**Base Url**: /posts

### Creating a Post

A Multipart Form request must be sent, example:

curl --location --request POST 'http://localhost:8080/posts' \
--form 'photo=@"/home/tomas/Imágenes/img.png"' \
--form 'description="description"' \
--form 'ownerId="1"'

Parameters are:

**photo**: A path to a file (It must be JPG or PNG)

**description**: A description of the post

**ownerId**: The user who is making the post

If the creation success, then the reponse will be 200 OK status with a body like:

`{
    "id": 11,
    "description": "description",
    "filename": "photo-2021-11-02T13:42:47.973612201",
    "created": "2021-11-02T13:42:48.083755103",
    "likeCount": 0,
    "ownerId": 1
}`

If there is any constraint violation (a file that is not a PNG/JPG file or a very large description) then the reponse will be 412 PRECONDITION FAILED with a body like:

`{
    "errorCode": "CONSTRAINT_VIOLATION",
    "errorNumber": 105,
    "message": "createPost.photo: Invalid file type"
}`

If the user with ownerId does not exist, then the reponse will be 404 NOT FOUND with a body like:

`{
    "errorCode": "USER_NOT_FOUND",
    "errorNumber": 104,
    "message": "User not found with id 1234"
}`

### Getting information about a Post

A simple GET request must be sent, for example:

curl --location --request GET 'http://localhost:8080/posts/12'

On success, the response will be a 200 OK status with a body like:

`{
    "id": 12,
    "description": "description",
    "filename": "photo-2021-11-02T04:17:09.728620741",
    "created": "2021-11-02T04:17:09.853846",
    "likeCount": 0
}`

If the post is not found, 404 NOT FOUND status will be sent with a body like:

`{
    "errorCode": "POST_NOT_FOUND",
    "errorNumber": 101,
    "message": "Post with id 123 not found"
}`



