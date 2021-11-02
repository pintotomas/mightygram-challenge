# mightygram-challenge
[![Java Build with Maven](https://github.com/pintotomas/mightygram-challenge/actions/workflows/maven.yml/badge.svg)](https://github.com/pintotomas/mightygram-challenge/actions/workflows/maven.yml)

## Diagrams

### Database Diagram (ERD)

![dbschema_documentation](https://user-images.githubusercontent.com/15394292/139922850-e8bd87d4-3cce-4080-98c1-5b5cb58db995.jpeg)

### Flow Diagrams

#### Post Creation

![post_creation](https://user-images.githubusercontent.com/15394292/139922252-731b2ef7-cd41-4b49-a113-f3710668beea.jpeg)

#### Liking a post

![post_like](https://user-images.githubusercontent.com/15394292/139922338-a5d2f8c0-1f96-40f4-8bef-7e81328da18d.jpeg)

#### Counting a post like

![count_post_like](https://user-images.githubusercontent.com/15394292/139922374-dfda1cce-0812-4b22-87e9-e75a4cb609d5.jpeg)


## Api Documentation

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

For getting the associated resource to the post (the image) you must make a request like this one:

curl --location --request GET 'http://localhost:8080/posts/11/photo'

If the post is found and there are no storage errors you will get the associated file. 

If there are storage problems, you will get one of these errors:

`{
    "errorCode": "STORAGE_ERROR",
    "errorNumber": 106,
    "message": "..."
}`

`{
    "errorCode": "FILE_NOT_FOUND",
    "errorNumber": 107,
    "message": "..."
}`

### Getting Posts paginated

To do so, send a request like this:

curl --location --request GET 'http://localhost:8080/posts/all?page=0&size=2'

The request parameters are page and size. The only restriction is that the minimum of the page is 0 and the size is 1.

You should get a response like this:

`{
    "content": [
        {
            "id": 11,
            "description": "description",
            "filename": "photo-2021-11-02T14:05:17.809011718",
            "created": "2021-11-02T14:05:17.834082",
            "likeCount": 0,
            "ownerId": 1
        },
        {
            "id": 6,
            "description": "Bang Len",
            "filename": "img.png",
            "created": "2021-07-11T00:00:00",
            "likeCount": 2,
            "ownerId": 6
        }
    ],
    "pageable": {
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 2,
        "paged": true,
        "unpaged": false
    },
    "totalPages": 6,
    "totalElements": 11,
    "last": false,
    "size": 2,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 2,
    "first": true,
    "empty": false
}`

### Liking and disliking a post

The requests are:

curl --location --request POST 'http://localhost:8080/posts/11/like' \
--header 'Content-Type: application/json' \
--data-raw '{
    "likerId": 4
}'

And

curl --location --request POST 'http://localhost:8080/posts/11/dislike' \
--header 'Content-Type: application/json' \
--data-raw '{
    "likerId": 4
}'

Both have the postId as a path variable and you must send a JSON with the likerId (The user who is liking the post) 

When liking, on success the post information will be returned and you should see an increase in the likeCount (or decreasing if disliking)

Aside of post or user not found errors you can also expect responses like this if the user has already liked a post or if the user didnt like a post before disliking it:

412 PRECONDITION FAILED

`{
    "errorCode": "POST_ALREADY_LIKED",
    "errorNumber": 103,
    "message": "User 4 has already liked post 11 from the owner 1"
}`

412 PRECONDITION FAILED

`{
    "errorCode": "POST_NOT_LIKED",
    "errorNumber": 102,
    "message": "User 4 does not like post 11 from owner 1"
}`

### Registering a parenthood

To do so, send a request like this:

curl --location --request POST 'http://localhost:8080/users/parenthood' \
--header 'Content-Type: application/json' \
--data-raw '{
    "parentId": 1,
    "childId": 2
}'

The json body contains parentId and childId. Returns OK if both exist and the child doesnt have a parent. If the child already has a parent, the response will be:

412 PRECONDITION FAILED

`{
    "errorCode": "USER_ALREADY_HAS_PARENT",
    "errorNumber": 108,
    "message": "User 2 has already a parent assigned"
}`

If a user tries to parent self, the response will be:

405 METHOD NOT ALLOWED

`{
    "errorCode": "USER_CANT_PARENT_SELF",
    "errorNumber": 109,
    "message": "User 1 attempted to parent self"
}`





