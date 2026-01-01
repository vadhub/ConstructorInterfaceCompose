package com.abg.constructorinterfacecompose.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abg.constructorinterfacecompose.model.Type
import com.abg.constructorinterfacecompose.ui.theme.ConstructorInterfaceComposeTheme

data class PaletteItem(
    val id: String,
    val emoji: String,
    val name: String,
    val type: Type
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstructorScreen(
    nameProject: String,
    pathProject: String,
    onNavigateToStarter: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Конструктор проекта $nameProject") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column {
            DragDropLayout(padding)
        }
    }
}

@Composable
fun DragDropLayout(paddingValues: PaddingValues) {
    // Состояния
    var showPlacementHint by remember { mutableStateOf(false) }
    var showTrashArea by remember { mutableStateOf(false) }
    val workAreaItems = remember { mutableStateListOf<PaletteItem>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Рабочая область (70% экрана)
        Box(
            modifier = Modifier
                .weight(7f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (workAreaItems.isEmpty()) {
                Text(
                    text = "Перетащите компоненты сюда",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 16.sp
                )
            } else {
                // Здесь будет отображаться добавленные элементы
                // В реальном приложении здесь будет LazyColumn или другой контейнер
                Text("Элементы: ${workAreaItems.size}")
            }
        }

        // Подсказка размещения
        if (showPlacementHint) {
            Text(
                text = "Перетащите элемент на рабочую область",
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE3F2FD))
                    .padding(8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        // Область корзины
        if (showTrashArea) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFCDD2))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🗑️",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = "Перетащите элемент сюда для удаления",
                    fontSize = 14.sp,
                    color = Color(0xFFD32F2F)
                )
            }
        }

        // Палитра компонентов (30% экрана)
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
        ) {
            ComponentPalette(
                onItemClick = { item ->
                    // Обработка клика по элементу палитры
                    workAreaItems.add(item.copy(id = "${item.id}_${System.currentTimeMillis()}"))
                }
            )
        }
    }
}

@Composable
fun ComponentPalette(onItemClick: (PaletteItem) -> Unit) {
    val paletteItems = listOf(
        PaletteItem("textView", "📝", "Текст", Type.TEXTVIEW),
        PaletteItem("editText", "✏️", "Поле ввода", Type.EDITTEXT),
        PaletteItem("button", "🔘", "Кнопка", Type.BUTTON),
        PaletteItem("spinner", "📃", "Выпадающий список", Type.SPINNER),
        PaletteItem("checkBox", "☑", "Чекбокс", Type.CHECKBOX),
        PaletteItem("other", "➕", "Другой", Type.OTHER)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(paletteItems) { item ->
            PaletteItemView(item = item, onClick = { onItemClick(item) })
        }
    }
}

@Composable
fun PaletteItemView(item: PaletteItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.emoji,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.name,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ConstructorPreview() {
    ConstructorInterfaceComposeTheme {
        ConstructorScreen("TestProject", "TestPath") {}
    }
}


