package com.example.spender.core.ui

import android.R.attr.fontFamily
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.spender.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val NotoSansFamily = FontFamily(
    Font(R.font.notosans_bold, FontWeight.Bold),
    Font(R.font.notosans_regular, FontWeight.Normal)
)

val MyTypography = Typography(
    // 제일 큰 글씨, 온보딩과 예산설정 등 (볼드 적용 O)
    titleLarge = TextStyle(
        fontFamily = NotoSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = Color(0xFF222836)
    ),
    // 앱바 타이틀 등 크게 잘 보여야 하는 글씨들 (볼드 적용 O)
    titleMedium = TextStyle(
        fontFamily = NotoSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color(0xFF222836)
    ),
    // 본문 내 카테고리나 다이얼로그의 타이틀 (볼드 적용 O)
    titleSmall = TextStyle(
        fontFamily = NotoSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color(0xFF222836)
    ),

    // 탭에서 선택되지 않은 항목을 위함 (연한색, 볼드 적용 X)
    bodyLarge = TextStyle(
        fontFamily = NotoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        color = Color(0xFF222836)
    ),

    // 어플의 기본 폰트 사이즈 (볼드 적용 X)
    bodyMedium = TextStyle(
        fontFamily = NotoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color(0xFF222836)
    ),

    // (볼드 적용 X)
    bodySmall = TextStyle(
        fontFamily = NotoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color(0xFF222836)
    ),

    // (연한색, 볼드 적용 X)
    labelLarge = TextStyle(
        fontFamily = NotoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color(0xFF7E8287)
    ),

    // (연한색, 볼드 적용 X)
    labelMedium = TextStyle(
        fontFamily = NotoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color(0xFF7E8287)
    ),

    // (연한색, 볼드 적용 X)
    labelSmall = TextStyle(
        fontFamily = NotoSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        color = Color(0xFF7E8287)
    ),
)