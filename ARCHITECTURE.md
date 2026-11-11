# Архітектура прикладу

## Рівні

### UI layer

- `NetworkDemoScreen.kt`
- `NetworkDemoViewModel.kt`
- `NetworkDemoUiState.kt`

UI відображає стан і надсилає події у ViewModel.

### Data layer

- `PostRepository`
- `NetworkPostRepository`
- `RemotePostDataSource`
- `PostApi`

`ViewModel` залежить від `PostRepository`, а не від Retrofit.

## Потік GET

```text
Button click
  -> ViewModel.loadAllPosts()
  -> Repository.getPosts()
  -> RemotePostDataSource.getPosts()
  -> Retrofit PostApi.getPosts()
  -> HTTP GET
  -> JSON
  -> PostDto
  -> mapper
  -> Post
  -> DataResult.Success
  -> UiState.posts
  -> Compose
```

## Потік POST

```text
Compose form
  -> NewPost
  -> Repository
  -> CreatePostRequestDto
  -> Retrofit @Body
  -> JSON
  -> HTTP POST
  -> PostDto
  -> Post
  -> UiState.createdPost
```

## Помилки

```text
Retrofit / OkHttp / serialization
  -> Throwable
  -> toNetworkError()
  -> DataResult.Error
  -> ViewModel
  -> errorMessage
  -> Compose ErrorCard
```

## Чому немає Room

Це приклад до лекції 10, тому source of truth тут — мережевий сервіс. Room додається в наступній темі про offline-first.

## REST methods covered

```text
GET    -> read list / read by ID
POST   -> create
PUT    -> full update
PATCH  -> partial update
DELETE -> delete
```

All methods still pass through the same Repository boundary.
