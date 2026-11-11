# REST endpoints у прикладі

Базова адреса:

```text
https://jsonplaceholder.typicode.com/
```

## GET список

```http
GET /posts
```

Retrofit:

```kotlin
@GET("posts")
suspend fun getPosts(
    @Query("userId") userId: Int? = null
): Response<List<PostDto>>
```

## GET з query parameter

```http
GET /posts?userId=1
```

Демонструє `@Query`.

## GET за ID

```http
GET /posts/1
```

Демонструє `@Path`.

## POST

```http
POST /posts
Content-Type: application/json

{
  "title": "Compose + Retrofit",
  "body": "Створено з Android-застосунку",
  "userId": 1
}
```

Демонструє `@Body` і JSON serialization.

## 404

```http
GET /lecture10-this-route-does-not-exist
```

Демонструє `HttpException` і перетворення технічної помилки в `NetworkError.Http`.

## PUT — повне оновлення

```http
PUT /posts/1
```

У тілі передається повне представлення Post.

## PATCH — часткове оновлення

```http
PATCH /posts/1
```

У прикладі змінюється лише `title`.

## DELETE

```http
DELETE /posts/1
```

Сервер повертає успішний статус, але JSONPlaceholder фактично не видаляє ресурс назавжди.
