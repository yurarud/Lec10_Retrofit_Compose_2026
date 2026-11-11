# Lecture 10 — REST API + Retrofit + Repository + Jetpack Compose

Готовий навчальний Android-проєкт до **лекції 10 «Робота з мережею: REST API, Retrofit та Repository»**.

Проєкт навмисно побудований так, щоб кожен блок лекції можна було показати на живому коді.

## Що демонструє застосунок

- HTTP/HTTPS і REST endpoints;
- `GET /posts`;
- `GET /posts?userId=1` — `@Query`;
- `GET /posts/{id}` — `@Path`;
- `POST /posts` — `@Body`;
- `PUT /posts/{id}` — повне оновлення;
- `PATCH /posts/{id}` — часткове оновлення;
- `DELETE /posts/{id}` — видалення;
- JSON -> DTO через `kotlinx.serialization`;
- DTO -> domain model через mapper;
- Retrofit 3 + OkHttp;
- `RemoteDataSource`;
- Repository як єдину точку доступу data layer;
- `ViewModel + StateFlow + UiState`;
- Loading / Success / Error;
- HTTP status codes (200, 201, 404);
- `HttpException`, відсутність мережі, timeout, serialization та IO errors;
- OkHttp BODY logging у Logcat;
- Jetpack Compose UI.

## API

Використовується безкоштовний **JSONPlaceholder**:

`https://jsonplaceholder.typicode.com/`

API не потребує ключа. POST/PUT/PATCH/DELETE імітуються: сервер повертає коректну відповідь, але зміни фактично не зберігаються назавжди.

## Архітектура

```text
Compose UI
    |
    v
NetworkDemoViewModel
    |
    v
PostRepository (interface)
    |
    v
NetworkPostRepository
    |
    v
RemotePostDataSource
    |
    v
PostApi (Retrofit)
    |
    v
JSONPlaceholder REST API
```

Моделі розділені:

```text
JSON
  -> PostDto / CreatePostRequestDto
  -> mapper
  -> Post / NewPost (domain)
  -> UiState
  -> Compose
```

## Структура проєкту

```text
app/src/main/java/com/example/lec10network/
├── MainActivity.kt
├── AppContainer.kt
├── data/
│   ├── mapper/
│   ├── remote/
│   │   ├── dto/
│   │   ├── NetworkModule.kt
│   │   ├── PostApi.kt
│   │   └── RemotePostDataSource.kt
│   ├── repository/
│   └── result/
├── domain/model/
└── ui/
    ├── NetworkDemoUiState.kt
    ├── NetworkDemoViewModel.kt
    └── NetworkDemoScreen.kt
```

## Запуск на Windows

Розпакуйте проєкт у шлях **без кирилиці**, наприклад:

```text
E:\Android\Lec10_Retrofit_Compose_2026
```

Якщо `gradlew.bat` ще немає, один раз виконайте:

```powershell
.\bootstrap-wrapper.bat
```

Після цього:

```powershell
.\gradlew.bat :app:assembleDebug
```

Або просто відкрийте корінь проєкту в Android Studio після створення Gradle Wrapper.

## Сценарій демонстрації на лекції

Див. [LECTURE_WALKTHROUGH.md](LECTURE_WALKTHROUGH.md).

## Що показати в Logcat

`NetworkModule.kt` містить `HttpLoggingInterceptor(Level.BODY)`. У Logcat видно:

- HTTP method;
- URL;
- headers;
- JSON request body;
- status code;
- JSON response body.

Для production-застосунку BODY logging зазвичай вмикають лише для debug build.

## Версії

- Android Gradle Plugin: 9.4.0
- Gradle: 9.6.0
- Kotlin: 2.4.10
- Compose BOM: 2026.09.00
- Activity Compose: 1.13.0
- Lifecycle: 2.11.0
- Retrofit: 3.0.0
- kotlinx.serialization JSON: 1.12.0

## Навчальний акцент

Цей приклад **не є offline-first**. Repository читає безпосередньо з мережевого data source. Це навмисно: наступна лекція додає Room як локальне source of truth і синхронізацію Room + REST API.
