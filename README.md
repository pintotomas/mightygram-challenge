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
