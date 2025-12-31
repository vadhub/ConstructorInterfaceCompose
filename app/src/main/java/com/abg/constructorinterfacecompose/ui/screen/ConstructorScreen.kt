package com.abg.constructorinterfacecompose.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstructorScreen(
    projectName: String,
    projectDescription: String,
    selectedType: String,
    onProjectNameChange: (String) -> Unit,
    onProjectDescriptionChange: (String) -> Unit,
    onSelectedTypeChange: (String) -> Unit,
    onNavigateToStarter: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Конструктор проекта") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Создание нового проекта",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = projectName,
                onValueChange = onProjectNameChange,
                label = { Text("Название проекта*") },
                placeholder = { Text("Введите название проекта") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = projectName.isEmpty()
            )

            if (projectName.isEmpty()) {
                Text(
                    text = "Название проекта обязательно",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = projectDescription,
                onValueChange = onProjectDescriptionChange,
                label = { Text("Описание проекта") },
                placeholder = { Text("Опишите ваш проект") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Тип проекта:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val projectTypes = listOf(
                "Мобильное приложение",
                "Веб-сайт",
                "Десктопное приложение",
                "Серверное приложение"
            )

            projectTypes.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedType == type,
                        onClick = { onSelectedTypeChange(type) }
                    )
                    Text(
                        text = type,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateToStarter,
                modifier = Modifier.fillMaxWidth(),
                enabled = projectName.isNotEmpty()
            ) {
                Text("Далее: Выбор стартера")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}
