package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppType
import com.example.ui.PhoneViewModel
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NexusOS()
            }
        }
    }
}

@Composable
fun NexusOS(viewModel: PhoneViewModel = viewModel()) {
    val activeApp by viewModel.activeApp.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A), Color(0xFF020617))
                )
            )
    ) {
        // Content Area
        Crossfade(targetState = activeApp, label = "AppTransition") { app ->
            when (app) {
                AppType.HOME -> HomeScreen(viewModel, currentTime, currentDate)
                AppType.CALCULATOR -> CalculatorApp(viewModel)
                AppType.NOTES -> PlaceholderApp("Notes", Icons.Default.Note, viewModel)
                AppType.WEATHER -> PlaceholderApp("Weather", Icons.Default.Cloud, viewModel)
                AppType.SETTINGS -> PlaceholderApp("Settings", Icons.Default.Settings, viewModel)
            }
        }

        // Status Bar
        StatusBar(currentTime)
    }
}

@Composable
fun StatusBar(time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = time, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Wifi, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.BatteryFull, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun HomeScreen(viewModel: PhoneViewModel, time: String, date: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large Clock Widget
        Text(text = time, color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.Thin)
        Text(text = date, color = Color.White.copy(alpha = 0.7f), fontSize = 18.sp, fontWeight = FontWeight.Light)

        Spacer(modifier = Modifier.height(60.dp))

        // App Grid
        val apps = listOf(
            AppInfo("Calc", Icons.Default.Calculate, iOSOrange, AppType.CALCULATOR),
            AppInfo("Notes", Icons.Default.Note, Color(0xFFFACC15), AppType.NOTES),
            AppInfo("Weather", Icons.Default.Cloud, iOSBlue, AppType.WEATHER),
            AppInfo("Settings", Icons.Default.Settings, Color.Gray, AppType.SETTINGS),
            AppInfo("Mail", Icons.Default.Mail, iOSBlue, AppType.HOME),
            AppInfo("Photos", Icons.Default.Image, iOSGreen, AppType.HOME),
            AppInfo("Music", Icons.Default.MusicNote, Color(0xFFF43F5E), AppType.HOME),
            AppInfo("Health", Icons.Default.Favorite, Color.Red, AppType.HOME)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(apps) { app ->
                AppIcon(app) { viewModel.openApp(app.type) }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Dock
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                .fillMaxWidth()
                .height(84.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(GlassWhite)
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DockIcon(Icons.Default.Phone, iOSGreen)
                DockIcon(Icons.Default.Message, iOSBlue)
                DockIcon(Icons.Default.Public, iOSBlue)
                DockIcon(Icons.Default.CameraAlt, Color.Gray)
            }
        }
    }
}

data class AppInfo(val name: String, val icon: ImageVector, val color: Color, val type: AppType)

@Composable
fun AppIcon(app: AppInfo, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(app.color),
            contentAlignment = Alignment.Center
        ) {
            Icon(app.icon, contentDescription = app.name, tint = Color.White, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = app.name, color = Color.White, fontSize = 11.sp)
    }
}

@Composable
fun DockIcon(icon: ImageVector, color: Color) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
    }
}

@Composable
fun CalculatorApp(viewModel: PhoneViewModel) {
    val display by viewModel.calcDisplay.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = 40.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(text = display, color = Color.White, fontSize = 80.sp, fontWeight = FontWeight.Light)
        }

        val buttons = listOf(
            listOf("C", "±", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "=")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { char ->
                    CalcButton(
                        text = char,
                        modifier = Modifier.weight(if (char == "0") 2f else 1f),
                        color = when {
                            char in "0123456789." -> Color(0xFF333333)
                            char in "÷×-+=" -> iOSOrange
                            else -> Color(0xFFA5A5A5)
                        },
                        textColor = if (char in "C±%") Color.Black else Color.White
                    ) {
                        if (char == "C") viewModel.clearCalc()
                        else viewModel.updateCalc(char)
                    }
                }
            }
        }

        // Home Indicator
        HomeIndicator { viewModel.goHome() }
    }
}

@Composable
fun CalcButton(text: String, modifier: Modifier, color: Color, textColor: Color, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .aspectRatio(if (text == "0") 2.1f else 1f)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = textColor, fontSize = 32.sp, fontWeight = FontWeight.Normal)
    }
}

@Composable
fun PlaceholderApp(name: String, icon: ImageVector, viewModel: PhoneViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(100.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "$name App", color = Color.White, fontSize = 24.sp)
            Text(text = "Coming Soon in Nexus OS v2", color = Color.Gray, fontSize = 14.sp)
        }
        
        HomeIndicator { viewModel.goHome() }
    }
}

@Composable
fun HomeIndicator(onHome: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(5.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onHome() }
        )
    }
}
