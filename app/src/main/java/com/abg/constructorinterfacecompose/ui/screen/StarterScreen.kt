package com.abg.constructorinterfacecompose.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StarterTemplate(
    val id: String,
    val name: String,
    val description: String,
    val technologies: List<String>,
    val icon: Int? = null
)

// ЭКРАН 2: StarterScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarterScreen(
    selectedStarter: StarterTemplate?,
    onStarterSelected: (StarterTemplate) -> Unit,
    onNavigateToProject: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val starters = remember {
        listOf(
            StarterTemplate(
                id = "android_kotlin",
                name = "Android + Kotlin",
                description = "Современное Android приложение на Kotlin с Material Design 3",
                technologies = listOf("Kotlin", "Jetpack Compose", "Material 3", "Coroutines")
            ),
            StarterTemplate(
                id = "android_java",
                name = "Android + Java",
                description = "Android приложение на Java с традиционным View-системой",
                technologies = listOf("Java", "XML Layouts", "View System")
            ),
            StarterTemplate(
                id = "multiplatform",
                name = "Kotlin Multiplatform",
                description = "Кроссплатформенное приложение для iOS и Android",
                technologies = listOf("Kotlin", "Compose Multiplatform", "iOS", "Android")
            ),
            StarterTemplate(
                id = "clean_arch",
                name = "Clean Architecture",
                description = "Проект с чистой архитектурой и модульной структурой",
                technologies = listOf("Clean Architecture", "MVVM", "DI", "Testing")
            )
        )
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Выбор стартера") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        },
        floatingActionButton = {

        }
    ) { padding ->
        Column (
            modifier = Modifier

                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Выберите стартовый шаблон",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Шаблон определит начальную структуру и настройки вашего проекта",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(starters) { starter ->
                    StarterCard(
                        starter = starter,
                        isSelected = selectedStarter?.id == starter.id,
                        onSelect = { onStarterSelected(starter) }
                    )
                }
            }

            if (selectedStarter != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Выбран: ${selectedStarter.name}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = selectedStarter.description,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarterCard(
    starter: StarterTemplate,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surface
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        ),
        onClick = onSelect,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = starter.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = starter.description,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                starter.technologies.forEach { tech ->
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        },
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(
                            text = tech,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            if (isSelected) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Выбрано",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}