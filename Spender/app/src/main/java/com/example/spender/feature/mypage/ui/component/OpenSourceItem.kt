package com.example.spender.feature.mypage.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.glance.text.FontWeight
import com.example.spender.feature.mypage.domain.model.OpenSourceLicense
import com.example.spender.ui.theme.Typography

@Composable
fun OpenSourceItem(license: OpenSourceLicense) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(
            text = license.title,
            style = Typography.titleMedium,
        )
        Text(
            text = "Version: ${license.version}",
            style = Typography.bodySmall
        )
        Text(
            text = license.content,
            style = Typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = license.url,
            style = Typography.bodySmall,
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(license.url))
                    context.startActivity(intent)
                }
        )
    }
}

