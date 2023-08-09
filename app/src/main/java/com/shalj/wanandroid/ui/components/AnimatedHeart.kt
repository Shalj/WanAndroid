package com.shalj.wanandroid.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.shalj.wanandroid.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun AnimatedHeart(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onToggle: (selected: Boolean) -> Unit = {}
) {
    var needScale by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (needScale) 1.2f else 1f, label = "scale")
    val scope = rememberCoroutineScope()

    Icon(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onToggle(!selected)
                if (!selected) {
                    scope.launch {
                        needScale = true
                        delay(200)
                        needScale = false
                    }
                }
            },
        painter = painterResource(id = R.drawable.ic_heart),
        contentDescription = "Like",
        tint = if (selected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = .5f)
    )
}

