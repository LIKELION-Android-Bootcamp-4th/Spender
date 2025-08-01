package com.example.spender.feature.analysis.ui.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.spender.ui.theme.NotoSansFamily
import com.example.spender.ui.theme.PointColor
import com.example.spender.ui.theme.Typography

@Composable
fun GraphScreen(navHostController: NavHostController) {

    fun clickDatePicker() {

    }

    fun clickNext() {

    }

    fun clickPrevious() {

    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {
        Column (modifier = Modifier.padding(horizontal = 24.dp)) {
            //header
            Text("이번 달 중 가장 지출이 많았던 날은", style = Typography.titleMedium)
            Row {
                Text("0월 00일 ", style = TextStyle(fontFamily = NotoSansFamily, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PointColor))
                Text("이에요", style = Typography.titleMedium)
            }
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text("월", style = Typography.titleMedium)
                Spacer(Modifier.width(10.dp))
                Text("2000", style = Typography.bodyMedium)
                Spacer(Modifier.width(10.dp))
                IconButton(
                    onClick = { clickDatePicker() },
                    modifier = Modifier.size(20.dp).align(Alignment.Bottom)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "datePicker",
                        tint = Color.DarkGray
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = { clickPrevious() },
                    modifier = Modifier.size(20.dp).align(Alignment.Bottom)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "previous month",
                        tint = Color.DarkGray
                    )
                }
                IconButton(
                    onClick = { clickNext() },
                    modifier = Modifier.size(20.dp).align(Alignment.Bottom)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "next month",
                        tint = Color.DarkGray
                    )
                }
            }
            //body

        }
    }
}