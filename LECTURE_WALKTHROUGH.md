# Сценарій демонстрації — лекція 10

## 1. Почати з `AndroidManifest.xml`

Показати:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Без permission застосунок Android не може виконувати мережеві запити.

## 2. `NetworkModule.kt`

Показати:

1. `BASE_URL`;
2. `Json { ignoreUnknownKeys = true }`;
3. `HttpLoggingInterceptor`;
4. timeouts OkHttp;
5. `Retrofit.Builder()`;
6. `asConverterFactory()`;
7. створення `PostApi`.

Після цього відкрити Logcat і показати реальний HTTP traffic.

## 3. DTO

Відкрити `PostDto.kt`.

Пояснити, що DTO описує структуру JSON API і не повинен автоматично ставати моделлю UI.

## 4. `PostApi.kt`

Послідовно показати:

```kotlin
@GET("posts")
@Query("userId")
```

```kotlin
@GET("posts/{id}")
@Path("id")
```

```kotlin
@POST("posts")
@Body
```

Запустити кожен сценарій у застосунку.

## 5. HTTP status codes

У UI після запиту показується код відповіді.

Очікувані приклади:

- GET -> HTTP 200;
- POST -> HTTP 201;
- навмисна помилка -> HTTP 404.

## 6. `RemotePostDataSource.kt`

Пояснити відповідальність data source: тільки робота з конкретним джерелом — REST API.

Він не працює з Compose і не знає про ViewModel.

## 7. DTO -> domain mapper

Показати `PostMappers.kt`:

```text
PostDto -> Post
NewPost -> CreatePostRequestDto
```

Пояснити, чому network model не слід тягнути до UI.

## 8. Repository

Показати `PostRepository` і `NetworkPostRepository`.

Ключова теза:

```text
UI / ViewModel НЕ знають про Retrofit.
```

Repository приховує джерело даних.

## 9. PUT / PATCH / DELETE

Відкрити вкладку `PUT/PATCH/DELETE` і показати різницю:

- `PUT` передає повне представлення ресурсу;
- `PATCH` — тільки змінюване поле;
- `DELETE` — видаляє ресурс.

Порівняти анотації `@PUT`, `@PATCH`, `@DELETE` у `PostApi.kt`.

JSONPlaceholder лише імітує ці зміни, тому після нового GET сервер повертає вихідні тестові дані.

## 10. Помилки

Показати `NetworkErrorMapper.kt`.

Демонстрації:

1. натиснути «Навмисно отримати HTTP 404»;
2. вимкнути мережу і повторити GET;
3. показати timeouts у `NetworkModule`;
4. пояснити `SerializationException`.

## 11. ViewModel + UiState

Показати:

```text
User event
   -> ViewModel
   -> Repository
   -> Network
   -> DataResult
   -> UiState
   -> Compose recomposition
```

Звернути увагу на `StateFlow` і `collectAsStateWithLifecycle()`.

## 12. POST

Заповнити форму і натиснути POST.

Показати в Logcat JSON request body та HTTP 201.

Пояснити: JSONPlaceholder імітує створення і не зберігає ресурс назавжди.

## 13. Міст до лекції 11

Завершити схемою:

```text
Сьогодні:
Repository -> REST API

Наступна лекція:
             Repository
             /        \
          Room        REST API
```

Це перехід до offline-first.
