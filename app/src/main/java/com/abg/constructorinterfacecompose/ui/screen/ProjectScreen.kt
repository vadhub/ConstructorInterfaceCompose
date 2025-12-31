package com.abg.constructorinterfacecompose.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.abg.constructorinterfacecompose.model.Project
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    onOpenConstructor: (Project) -> Unit,
    onOpenRun: (Project) -> Unit
) {
    val context = LocalContext.current

    // Состояния
    var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var editingProject by remember { mutableStateOf<Project?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Project?>(null) }

    // Загрузка проектов
    LaunchedEffect(Unit) {
        loadProjects(context) { loadedProjects ->
            projects = loadedProjects
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Мои проекты",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Добавить проект") },
                text = { Text("Новый проект") },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (projects.isEmpty()) {
                EmptyStateView(
                    onAddProjectClick = { showCreateDialog = true }
                )
            } else {
                ProjectList(
                    projects = projects,
                    onProjectClick = onOpenRun,
                    onEditProject = { project ->
                        onOpenConstructor(project)
                    },
                    onRenameProject = { project ->
                        editingProject = project
                        showCreateDialog = true
                    },
                    onDeleteProject = { project ->
                        showDeleteDialog = project
                    }
                )
            }

            // Диалог создания/редактирования проекта
            if (showCreateDialog) {
                ProjectDialog(
                    existingProject = editingProject,
                    onDismiss = {
                        showCreateDialog = false
                        editingProject = null
                    },
                    onProjectCreated = { project ->
                        if (editingProject != null) {
                            updateProject(context, project) {
                                projects = projects.map {
                                    if (it.id == project.id) project else it
                                }
                            }
                        } else {
                            addProject(context, project) {
                                projects = listOf(project) + projects
                            }
                        }
                        showCreateDialog = false
                        editingProject = null
                    },
                    projects = projects
                )
            }

            // Диалог подтверждения удаления
            showDeleteDialog?.let { project ->
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = null },
                    title = { Text("Удалить проект") },
                    text = {
                        Text("Вы уверены, что хотите удалить проект \"${project.name}\"?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                deleteProject(context, project) {
                                    projects = projects.filter { it.id != project.id }
                                }
                                showDeleteDialog = null
                            }
                        ) {
                            Text("Удалить", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteDialog = null }
                        ) {
                            Text("Отмена")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyStateView(
    onAddProjectClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Нет проектов",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Создайте свой первый проект, чтобы начать работу",
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onAddProjectClick,
            modifier = Modifier.width(200.dp)
        ) {
            Text("Создать проект")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectList(
    projects: List<Project>,
    onProjectClick: (Project) -> Unit,
    onEditProject: (Project) -> Unit,
    onRenameProject: (Project) -> Unit,
    onDeleteProject: (Project) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(projects) { project ->
            ProjectItem(
                project = project,
                onClick = { onProjectClick(project) },
                onEditClick = { onEditProject(project) },
                onRenameClick = { onRenameProject(project) },
                onDeleteClick = { onDeleteProject(project) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectItem(
    project: Project,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = project.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Путь: ${project.path}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formatDate(project.createdAt),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Box {
                    IconButton(
                        onClick = { showMenu = true }
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Меню"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Запустить") },
                            onClick = {
                                onClick()
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Редактировать") },
                            onClick = {
                                onEditClick()
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Edit, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Переименовать") },
                            onClick = {
                                onRenameClick()
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Create, contentDescription = null)
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Удалить",
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                onDeleteClick()
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun ProjectDialog(
    existingProject: Project?,
    onDismiss: () -> Unit,
    onProjectCreated: (Project) -> Unit,
    projects: List<Project>
) {
    var projectName by remember(existingProject) {
        mutableStateOf(existingProject?.name ?: generateDefaultName())
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = if (existingProject != null) "Редактировать проект" else "Новый проект",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = projectName,
                    onValueChange = {
                        projectName = it
                        errorMessage = null
                    },
                    label = { Text("Название проекта") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Отмена")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (projectName.isBlank()) {
                                errorMessage = "Введите название проекта"
                                return@Button
                            }

                            val isDuplicate = projects.any {
                                it.name == projectName && it.id != existingProject?.id
                            }

                            if (isDuplicate) {
                                errorMessage = "Проект с таким именем уже существует"
                                return@Button
                            }

                            val project = existingProject?.copy(
                                name = projectName.trim()
                            ) ?: Project(
                                name = projectName.trim(),
                                path = "${System.currentTimeMillis()}_project"
                            )

                            onProjectCreated(project)
                        }
                    ) {
                        Text(if (existingProject != null) "Сохранить" else "Создать")
                    }
                }
            }
        }
    }
}

// Вспомогательные функции для работы с SharedPreferences
@SuppressLint("ApplySharedPref")
private fun loadProjects(
    context: android.content.Context,
    onLoaded: (List<Project>) -> Unit
) {
    val sharedPreferences = context.getSharedPreferences("projects", 0)
    val json = sharedPreferences.getString("project_list", null)

    if (json != null) {
        val type = com.google.gson.reflect.TypeToken.getParameterized(List::class.java, Project::class.java).type
        val projects: List<Project> = com.google.gson.Gson().fromJson(json, type) ?: emptyList()
        onLoaded(projects)
    } else {
        onLoaded(emptyList())
    }
}

@SuppressLint("ApplySharedPref")
private fun saveProject(
    context: android.content.Context,
    project: Project,
    onSuccess: () -> Unit = {}
) {
    val sharedPreferences = context.getSharedPreferences("projects", 0)
    val json = sharedPreferences.getString("project_list", null)
    val gson = com.google.gson.Gson()

    val projects = if (json != null) {
        val type = com.google.gson.reflect.TypeToken.getParameterized(MutableList::class.java, Project::class.java).type
        gson.fromJson<MutableList<Project>>(json, type) ?: mutableListOf()
    } else {
        mutableListOf()
    }

    val existingIndex = projects.indexOfFirst { it.id == project.id }
    if (existingIndex != -1) {
        projects[existingIndex] = project
    } else {
        projects.add(project)
    }

    sharedPreferences.edit {
        putString("project_list", gson.toJson(projects))
    }

    onSuccess()
}

@SuppressLint("ApplySharedPref")
private fun addProject(
    context: android.content.Context,
    project: Project,
    onSuccess: () -> Unit = {}
) {
    val sharedPreferences = context.getSharedPreferences("projects", 0)
    val json = sharedPreferences.getString("project_list", null)
    val gson = com.google.gson.Gson()

    val projects = if (json != null) {
        val type = com.google.gson.reflect.TypeToken.getParameterized(MutableList::class.java, Project::class.java).type
        gson.fromJson<MutableList<Project>>(json, type) ?: mutableListOf()
    } else {
        mutableListOf()
    }

    projects.add(project)

    sharedPreferences.edit {
        putString("project_list", gson.toJson(projects))
    }

    // Создаем директорию проекта
    val projectDir = File(context.filesDir, project.path)
    if (!projectDir.exists()) {
        projectDir.mkdirs()
    }

    onSuccess()
}

@SuppressLint("ApplySharedPref")
private fun updateProject(
    context: android.content.Context,
    project: Project,
    onSuccess: () -> Unit = {}
) {
    val sharedPreferences = context.getSharedPreferences("projects", 0)
    val json = sharedPreferences.getString("project_list", null)
    val gson = com.google.gson.Gson()

    if (json != null) {
        val type = com.google.gson.reflect.TypeToken.getParameterized(MutableList::class.java, Project::class.java).type
        val projects: MutableList<Project> = gson.fromJson(json, type) ?: mutableListOf()

        val index = projects.indexOfFirst { it.id == project.id }
        if (index != -1) {
            projects[index] = project
            sharedPreferences.edit {
                putString("project_list", gson.toJson(projects))
            }
            onSuccess()
        }
    }
}

@SuppressLint("ApplySharedPref")
private fun deleteProject(
    context: android.content.Context,
    project: Project,
    onSuccess: () -> Unit = {}
) {
    // Удаляем директорию проекта
    val projectDir = File(context.filesDir, project.path)
    if (projectDir.exists()) {
        projectDir.deleteRecursively()
    }

    // Удаляем из SharedPreferences
    val sharedPreferences = context.getSharedPreferences("projects", 0)
    val json = sharedPreferences.getString("project_list", null)
    val gson = com.google.gson.Gson()

    if (json != null) {
        val type = com.google.gson.reflect.TypeToken.getParameterized(MutableList::class.java, Project::class.java).type
        val projects: MutableList<Project> = gson.fromJson(json, type) ?: mutableListOf()

        val index = projects.indexOfFirst { it.id == project.id }
        if (index != -1) {
            projects.removeAt(index)
            sharedPreferences.edit()
                .putString("project_list", gson.toJson(projects))
                .apply()
            onSuccess()
        }
    }
}

// Вспомогательные функции
private fun generateDefaultName(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return "Проект ${dateFormat.format(Date())}"
}

private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}