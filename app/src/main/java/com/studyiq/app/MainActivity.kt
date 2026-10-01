package com.studyiq.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF5B4BDB)
private val Lavender = Color(0xFFEAE7FF)
private val Canvas = Color(0xFFF7F7FB)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { StudyIQApp() } }
}

data class StudyItem(val title: String, val source: String, val format: String, val progress: Int)
private val demoItems = listOf(
    StudyItem("Photosynthesis essentials", "PDF · Biology", "Notes + Quiz", 72),
    StudyItem("Newton's laws", "YouTube · Physics", "Flashcards", 38),
    StudyItem("World War II timeline", "Question paper", "Mind Map", 100)
)

@Composable fun StudyIQApp() {
    var tab by remember { mutableStateOf("Home") }
    var showCreate by remember { mutableStateOf(false) }
    MaterialTheme(colorScheme = lightColorScheme(primary = Purple, background = Canvas, surface = Color.White, onSurface = Color(0xFF17171A))) {
        Scaffold(containerColor = Canvas, bottomBar = { BottomBar(tab) { tab = it } }, floatingActionButton = {
            FloatingActionButton(onClick = { showCreate = true }, containerColor = Purple, contentColor = Color.White, shape = CircleShape) { Icon(Icons.Default.Add, "Create") }
        }) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) { when (tab) {
                "Home" -> HomeScreen(onCreate = { showCreate = true }, onOpen = { tab = "Library" })
                "Library" -> LibraryScreen()
                "Progress" -> ProgressScreen()
                else -> ProfileScreen()
            } }
        }
        if (showCreate) CreateSheet(onDismiss = { showCreate = false })
    }
}

@Composable private fun BottomBar(selected: String, onSelect: (String) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        listOf("Home" to Icons.Default.Home, "Library" to Icons.Default.Folder, "Progress" to Icons.Default.Insights, "Profile" to Icons.Default.Person).forEach { (name, icon) ->
            NavigationBarItem(selected = selected == name, onClick = { onSelect(name) }, icon = { Icon(icon, name) }, label = { Text(name) })
        }
    }
}

@Composable private fun HomeScreen(onCreate: () -> Unit, onOpen: () -> Unit) {
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Column { Text("Good evening 👋", style = MaterialTheme.typography.titleMedium); Text("Ready to learn something new?", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF686872)) }; Avatar() } }
        item { OutlinedTextField(value = "", onValueChange = {}, enabled = false, modifier = Modifier.fillMaxWidth(), placeholder = { Text("What do you want to study?") }, leadingIcon = { Icon(Icons.Default.Search, null) }, shape = RoundedCornerShape(16.dp)) }
        item { Card(colors = CardDefaults.cardColors(containerColor = Purple), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth().clickable { onCreate() }) { Column(Modifier.padding(22.dp)) { Text("Turn anything into study material", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(6.dp)); Text("Paste a link, upload a file, or record a class.", color = Color.White.copy(.85f)); Spacer(Modifier.height(18.dp)); Button(onClick = onCreate, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Purple)) { Text("Create now") } } } }
        item { Text("Quick start", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) { listOf("YouTube" to Icons.Default.PlayCircle, "File" to Icons.Default.UploadFile, "Record" to Icons.Default.Mic, "Question" to Icons.Default.Description).forEach { (label, icon) -> QuickAction(label, icon, onCreate) } } }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Continue studying", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); TextButton(onClick = onOpen) { Text("See all") } } }
        items(demoItems.take(2)) { StudyCard(it, onOpen) }
        item { Text("Study tools", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        item { FeatureGrid() }
    }
}

@Composable private fun Avatar() { Box(Modifier.size(44.dp).clip(CircleShape).background(Lavender), contentAlignment = Alignment.Center) { Text("M", color = Purple, fontWeight = FontWeight.Bold) } }
@Composable private fun QuickAction(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, click: () -> Unit) { Column(Modifier.weight(1f).clickable { click() }, horizontalAlignment = Alignment.CenterHorizontally) { Box(Modifier.size(52.dp).clip(RoundedCornerShape(16.dp)).background(Lavender), contentAlignment = Alignment.Center) { Icon(icon, label, tint = Purple) }; Spacer(Modifier.height(6.dp)); Text(label, fontSize = 12.sp) } }
@Composable private fun FeatureGrid() { Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { listOf("AI Notes" to Icons.Default.Notes, "Quiz" to Icons.Default.Quiz, "Flashcards" to Icons.Default.Style, "Mind Map" to Icons.Default.AccountTree, "Ask IQ" to Icons.Default.Chat, "AI Writer" to Icons.Default.Edit).chunked(3).forEach { row -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) { row.forEach { (label, icon) -> Card(Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(14.dp)) { Icon(icon, null, tint = Purple); Spacer(Modifier.height(9.dp)); Text(label, fontWeight = FontWeight.SemiBold, fontSize = 13.sp) } } } } } } }
@Composable private fun StudyCard(item: StudyItem, click: () -> Unit) { Card(Modifier.fillMaxWidth().clickable { click() }, shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(16.dp)) { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Column(Modifier.weight(1f)) { Text(item.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis); Text(item.source, color = Color(0xFF686872), fontSize = 13.sp) }; Text(item.format, color = Purple, fontSize = 12.sp) }; Spacer(Modifier.height(14.dp)); LinearProgressIndicator(progress = { item.progress / 100f }, modifier = Modifier.fillMaxWidth(), color = Purple); Spacer(Modifier.height(5.dp)); Text("${item.progress}% complete", fontSize = 12.sp, color = Color(0xFF686872)) } } }

@Composable private fun CreateSheet(onDismiss: () -> Unit) { var source by remember { mutableStateOf("YouTube") }; var generated by remember { mutableStateOf(false) }; ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Color.White) { Column(Modifier.padding(horizontal = 22.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { Text(if (generated) "Choose what to create" else "Choose your source", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); if (!generated) { Text("One source → many study formats", color = Color(0xFF686872)); listOf("YouTube" to Icons.Default.PlayCircle, "Upload file" to Icons.Default.UploadFile, "Record class" to Icons.Default.Mic, "Question paper" to Icons.Default.Description).forEach { (name, icon) -> FilterChip(selected = source == name, onClick = { source = name }, label = { Text(name) }, leadingIcon = { Icon(icon, null) }) }; if (source == "YouTube") OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Paste YouTube link") }, singleLine = true); Button(onClick = { generated = true }, modifier = Modifier.fillMaxWidth()) { Text(if (source == "YouTube") "Analyze link" else "Continue") } } else { Text("Select one or more formats for your ${source.lowercase()}.", color = Color(0xFF686872)); listOf("Notes" to "Structured explanations", "Quiz" to "MCQs with explanations", "Flashcards" to "Recall practice", "Mind Map" to "Visual relationships", "AI Slides" to "Presentation deck").forEach { (title, subtitle) -> Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) { Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Checkbox(checked = title == "Notes" || title == "Quiz", onCheckedChange = {}); Column { Text(title, fontWeight = FontWeight.SemiBold); Text(subtitle, fontSize = 12.sp, color = Color(0xFF686872)) } } } }; Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Generate study material") } } } }

@Composable private fun LibraryScreen() { LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { item { Text("Library", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("Your saved study material", color = Color(0xFF686872)) }; item { OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Search library") }, leadingIcon = { Icon(Icons.Default.Search, null) }, shape = RoundedCornerShape(16.dp)) }; item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("All", "Notes", "Quiz", "Flashcards").forEach { AssistChip(onClick = {}, label = { Text(it) }) } } }; items(demoItems) { StudyCard(it) {} } }
}

@Composable private fun ProgressScreen() { LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { item { Text("Your progress", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("Small steps add up to big learning.", color = Color(0xFF686872)) }; item { Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) { Stat("7", "Day streak"); Stat("84%", "Quiz accuracy"); Stat("12", "Topics studied") } }; item { Card(shape = RoundedCornerShape(18.dp)) { Column(Modifier.padding(18.dp)) { Text("Weekly activity", fontWeight = FontWeight.Bold); Spacer(Modifier.height(18.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) { listOf(35, 65, 48, 90, 55, 72, 42).forEach { value -> Box(Modifier.width(22.dp).height((value / 2).dp).clip(RoundedCornerShape(8.dp)).background(Purple)) } } } } }; item { Card(colors = CardDefaults.cardColors(containerColor = Lavender), shape = RoundedCornerShape(18.dp)) { Column(Modifier.padding(18.dp)) { Text("Topics that need revision", fontWeight = FontWeight.Bold); Text("Newton's laws · Cell structure · Algebra", color = Color(0xFF686872)); TextButton(onClick = {}) { Text("Review now") } } } } } }
@Composable private fun Stat(value: String, label: String) { Card(Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(12.dp)) { Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Purple); Text(label, fontSize = 11.sp, color = Color(0xFF686872)) } } }
@Composable private fun ProfileScreen() { LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { item { Row(verticalAlignment = Alignment.CenterVertically) { Avatar(); Spacer(Modifier.width(12.dp)); Column { Text("Minesh", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Text("Student account", color = Color(0xFF686872)) } } }; item { Text("Settings", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }; items(listOf("Notifications", "Theme · Light", "Language · English", "Subscription", "Privacy", "Help & support")) { label -> ListItem(headlineContent = { Text(label) }, leadingContent = { Icon(Icons.Default.Settings, null, tint = Purple) }, trailingContent = { Icon(Icons.Default.ChevronRight, null) }, modifier = Modifier.clickable {}) } } }
