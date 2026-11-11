package com.example.lec10network.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lec10network.AppContainer
import com.example.lec10network.domain.model.Post

private enum class DemoTab(val title: String) {
    LIST("GET список"),
    DETAILS("GET ID"),
    CREATE("POST"),
    CHANGE("PUT/PATCH/DELETE"),
    ERRORS("Помилки")
}

@Composable
fun NetworkDemoRoute() {
    val viewModel: NetworkDemoViewModel = viewModel(
        factory = NetworkDemoViewModel.factory(AppContainer.postRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NetworkDemoScreen(
        uiState = uiState,
        onUserIdChanged = viewModel::onUserIdFilterChanged,
        onLoadAll = viewModel::loadAllPosts,
        onLoadByUser = viewModel::loadPostsByUser,
        onPostIdChanged = viewModel::onPostIdChanged,
        onLoadPostById = viewModel::loadPostById,
        onNewPostUserIdChanged = viewModel::onNewPostUserIdChanged,
        onNewPostTitleChanged = viewModel::onNewPostTitleChanged,
        onNewPostBodyChanged = viewModel::onNewPostBodyChanged,
        onCreatePost = viewModel::createPost,
        onChangePostIdChanged = viewModel::onChangePostIdChanged,
        onChangePostUserIdChanged = viewModel::onChangePostUserIdChanged,
        onChangePostTitleChanged = viewModel::onChangePostTitleChanged,
        onChangePostBodyChanged = viewModel::onChangePostBodyChanged,
        onPatchTitleChanged = viewModel::onPatchTitleChanged,
        onPutPost = viewModel::putPost,
        onPatchPost = viewModel::patchPostTitle,
        onDeletePost = viewModel::deletePost,
        onForce404 = viewModel::forceHttp404,
        onClearError = viewModel::clearError
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NetworkDemoScreen(
    uiState: NetworkDemoUiState,
    onUserIdChanged: (String) -> Unit,
    onLoadAll: () -> Unit,
    onLoadByUser: () -> Unit,
    onPostIdChanged: (String) -> Unit,
    onLoadPostById: () -> Unit,
    onNewPostUserIdChanged: (String) -> Unit,
    onNewPostTitleChanged: (String) -> Unit,
    onNewPostBodyChanged: (String) -> Unit,
    onCreatePost: () -> Unit,
    onChangePostIdChanged: (String) -> Unit,
    onChangePostUserIdChanged: (String) -> Unit,
    onChangePostTitleChanged: (String) -> Unit,
    onChangePostBodyChanged: (String) -> Unit,
    onPatchTitleChanged: (String) -> Unit,
    onPutPost: () -> Unit,
    onPatchPost: () -> Unit,
    onDeletePost: () -> Unit,
    onForce404: () -> Unit,
    onClearError: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = DemoTab.entries

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Лекція 10 · REST + Retrofit")
                        Text(
                            text = "JSONPlaceholder · Repository · UiState",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            RequestStatusCard(uiState)

            if (uiState.errorMessage != null) {
                ErrorCard(
                    message = uiState.errorMessage,
                    onDismiss = onClearError
                )
            }

            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(tab.title) }
                    )
                }
            }

            when (tabs[selectedTab]) {
                DemoTab.LIST -> PostsListTab(
                    state = uiState,
                    onUserIdChanged = onUserIdChanged,
                    onLoadAll = onLoadAll,
                    onLoadByUser = onLoadByUser
                )

                DemoTab.DETAILS -> PostDetailsTab(
                    state = uiState,
                    onPostIdChanged = onPostIdChanged,
                    onLoadPostById = onLoadPostById
                )

                DemoTab.CREATE -> CreatePostTab(
                    state = uiState,
                    onUserIdChanged = onNewPostUserIdChanged,
                    onTitleChanged = onNewPostTitleChanged,
                    onBodyChanged = onNewPostBodyChanged,
                    onCreatePost = onCreatePost
                )

                DemoTab.CHANGE -> ChangePostTab(
                    state = uiState,
                    onIdChanged = onChangePostIdChanged,
                    onUserIdChanged = onChangePostUserIdChanged,
                    onTitleChanged = onChangePostTitleChanged,
                    onBodyChanged = onChangePostBodyChanged,
                    onPatchTitleChanged = onPatchTitleChanged,
                    onPut = onPutPost,
                    onPatch = onPatchPost,
                    onDelete = onDeletePost
                )

                DemoTab.ERRORS -> ErrorsTab(
                    isLoading = uiState.isLoading,
                    onForce404 = onForce404,
                    onLoadAll = onLoadAll
                )
            }
        }
    }
}

@Composable
private fun RequestStatusCard(state: NetworkDemoUiState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Останній запит", fontWeight = FontWeight.Bold)
                Text(state.lastRequest, style = MaterialTheme.typography.bodyMedium)
            }
            when {
                state.isLoading -> CircularProgressIndicator()
                state.lastHttpStatusCode != null -> Text(
                    text = "HTTP ${state.lastHttpStatusCode}",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(message: String, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            TextButton(onClick = onDismiss) {
                Text("Закрити")
            }
        }
    }
}

@Composable
private fun PostsListTab(
    state: NetworkDemoUiState,
    onUserIdChanged: (String) -> Unit,
    onLoadAll: () -> Unit,
    onLoadByUser: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "@GET + @Query",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text("GET /posts або GET /posts?userId=1")

            OutlinedTextField(
                value = state.userIdFilter,
                onValueChange = onUserIdChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("userId для @Query") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onLoadAll,
                    enabled = !state.isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Усі пости")
                }
                OutlinedButton(
                    onClick = onLoadByUser,
                    enabled = !state.isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Фільтр userId")
                }
            }

            Text(
                text = "Отримано: ${state.posts.size}",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelLarge
            )
        }

        HorizontalDivider()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = state.posts,
                key = { it.id }
            ) { post ->
                PostCard(post)
            }
        }
    }
}

@Composable
private fun PostDetailsTab(
    state: NetworkDemoUiState,
    onPostIdChanged: (String) -> Unit,
    onLoadPostById: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "@GET + @Path",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text("GET /posts/{id}")
        }

        item {
            OutlinedTextField(
                value = state.postIdInput,
                onValueChange = onPostIdChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("ID поста") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        item {
            Button(
                onClick = onLoadPostById,
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GET /posts/${state.postIdInput.ifBlank { "{id}" }}")
            }
        }

        state.selectedPost?.let { post ->
            item { PostCard(post) }
        }
    }
}

@Composable
private fun CreatePostTab(
    state: NetworkDemoUiState,
    onUserIdChanged: (String) -> Unit,
    onTitleChanged: (String) -> Unit,
    onBodyChanged: (String) -> Unit,
    onCreatePost: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "@POST + @Body",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text("POST /posts — DTO серіалізується в JSON")
        }

        item {
            OutlinedTextField(
                value = state.newPostUserId,
                onValueChange = onUserIdChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("userId") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = state.newPostTitle,
                onValueChange = onTitleChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("title") }
            )
        }

        item {
            OutlinedTextField(
                value = state.newPostBody,
                onValueChange = onBodyChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("body") },
                minLines = 4
            )
        }

        item {
            Button(
                onClick = onCreatePost,
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Створити POST-запит")
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    text = "JSONPlaceholder імітує створення: сервер поверне новий об'єкт (зазвичай id=101), але не збереже його після запиту.",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        state.createdPost?.let { post ->
            item {
                Text("Відповідь сервера", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                PostCard(post)
            }
        }
    }
}

@Composable
private fun ChangePostTab(
    state: NetworkDemoUiState,
    onIdChanged: (String) -> Unit,
    onUserIdChanged: (String) -> Unit,
    onTitleChanged: (String) -> Unit,
    onBodyChanged: (String) -> Unit,
    onPatchTitleChanged: (String) -> Unit,
    onPut: () -> Unit,
    onPatch: () -> Unit,
    onDelete: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("PUT / PATCH / DELETE", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("JSONPlaceholder імітує оновлення та видалення ресурсів.")
        }
        item {
            OutlinedTextField(
                value = state.changePostId, onValueChange = onIdChanged, modifier = Modifier.fillMaxWidth(),
                label = { Text("post id") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = state.changePostUserId, onValueChange = onUserIdChanged, modifier = Modifier.fillMaxWidth(),
                label = { Text("userId для PUT") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = state.changePostTitle, onValueChange = onTitleChanged, modifier = Modifier.fillMaxWidth(),
                label = { Text("title для PUT") }
            )
        }
        item {
            OutlinedTextField(
                value = state.changePostBody, onValueChange = onBodyChanged, modifier = Modifier.fillMaxWidth(),
                label = { Text("body для PUT") }, minLines = 3
            )
        }
        item {
            Button(onClick = onPut, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth()) {
                Text("PUT — повністю оновити ресурс")
            }
        }
        item {
            OutlinedTextField(
                value = state.patchTitle, onValueChange = onPatchTitleChanged, modifier = Modifier.fillMaxWidth(),
                label = { Text("Новий title для PATCH") }
            )
        }
        item {
            OutlinedButton(onClick = onPatch, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth()) {
                Text("PATCH — змінити лише title")
            }
        }
        item {
            OutlinedButton(onClick = onDelete, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth()) {
                Text("DELETE — видалити ресурс")
            }
        }
        state.changedPost?.let { post ->
            item {
                Text("Відповідь сервера", fontWeight = FontWeight.Bold)
                PostCard(post)
            }
        }
        state.deleteMessage?.let { message ->
            item { Card { Text(message, modifier = Modifier.padding(12.dp)) } }
        }
    }
}

@Composable
private fun ErrorsTab(
    isLoading: Boolean,
    onForce404: () -> Unit,
    onLoadAll: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Обробка мережевих помилок",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Text(
                "Repository перетворює винятки Retrofit/OkHttp/serialization на NetworkError: HTTP, NoInternet, Timeout, Serialization, IO або Unknown."
            )
        }

        item {
            Button(
                onClick = onForce404,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Навмисно отримати HTTP 404")
            }
        }

        item {
            OutlinedButton(
                onClick = onLoadAll,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Повторити нормальний GET /posts")
            }
        }

        item {
            Card {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Як перевірити інші помилки", fontWeight = FontWeight.Bold)
                    Text("1. Вимкніть Wi‑Fi/мобільні дані й натисніть GET — побачите NoInternet/IO.")
                    Text("2. У NetworkModule налаштовано connect/read/write timeout.")
                    Text("3. Змініть DTO так, щоб тип поля не відповідав JSON — отримаєте помилку десеріалізації.")
                    Text("4. Логи OkHttp BODY видно в Logcat.")
                }
            }
        }
    }
}

@Composable
private fun PostCard(post: Post) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "#${post.id} · userId=${post.userId}",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = post.body,
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
