package com.e1i3.spender.feature.home.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.e1i3.spender.feature.home.domain.model.TierHistory
import com.e1i3.spender.ui.theme.Typography

@Composable
fun TierItem(tier: TierHistory){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(110.dp)
            .padding(vertical = 10.dp, horizontal = 5.dp)
    ) {
        Image(
            painter = painterResource(id = tierDrawableRes(tier.level)),
            contentDescription = "티어",
            modifier = Modifier
                .size(75.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = tier.month.replace("-", "."),
            style = Typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            fontSize = 16.sp
        )
    }
}