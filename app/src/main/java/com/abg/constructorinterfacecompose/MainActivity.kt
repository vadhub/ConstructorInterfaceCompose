package com.abg.constructorinterfacecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abg.constructorinterfacecompose.ui.screen.ConstructorScreen
import com.abg.constructorinterfacecompose.ui.screen.ProjectScreen
import com.abg.constructorinterfacecompose.ui.screen.StarterScreen
import com.abg.constructorinterfacecompose.ui.screen.StarterTemplate
import com.abg.constructorinterfacecompose.ui.theme.ConstructorInterfaceComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConstructorInterfaceComposeTheme {
                AppTheme {
                    AppContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFF0066CC),
            secondary = androidx.compose.ui.graphics.Color(0xFF66BB6A),
            tertiary = androidx.compose.ui.graphics.Color(0xFF5C6BC0)
        ),
        content = content
    )
}

@Composable
fun AppContent() {
    // Состояние для отслеживания текущего экрана
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Constructor) }

    // Данные, передаваемые между экранами
    var projectName by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Мобильное приложение") }
    var selectedStarter by remember { mutableStateOf<StarterTemplate?>(null) }

    when (currentScreen) {
        is Screen.Constructor -> {
            ConstructorScreen (
                projectName = projectName,
                projectDescription = projectDescription,
                selectedType = selectedType,
                onProjectNameChange = { projectName = it },
                onProjectDescriptionChange = { projectDescription = it },
                onSelectedTypeChange = { selectedType = it },
                onNavigateToStarter = {
                    if (projectName.isNotEmpty()) {
                        currentScreen = Screen.Starter
                    }
                }
            )
        }
        is Screen.Starter -> {
            StarterScreen (
                selectedStarter = selectedStarter,
                onStarterSelected = { starter -> selectedStarter = starter },
                onNavigateToProject = {
                    if (selectedStarter != null) {
                        currentScreen = Screen.Project
                    }
                },
                onNavigateBack = { currentScreen = Screen.Constructor }
            )
        }
        is Screen.Project -> {
            ProjectScreen (
                onOpenConstructor = {

                },
                onOpenRun = {

                }
            )
        }
    }
}

// Enum для определения экранов
sealed class Screen {
    object Constructor : Screen()
    object Starter : Screen()
    object Project : Screen()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ConstructorInterfaceComposeTheme {
       AppContent()
    }
}