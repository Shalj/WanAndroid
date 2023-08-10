package com.shalj.wanandroid.ui.screen.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalj.wanandroid.model.ArticleData
import com.shalj.wanandroid.ui.components.AnimatedHeart
import com.shalj.wanandroid.ui.theme.WanAndroidTheme

@Preview(showBackground = true)
@Composable
fun Preview() {
    WanAndroidTheme {
        ArticleItem(
            articleData = ArticleData(
                title = "Title of the article...",
                author = "作者",
                niceDate = "1天前",
                chapterName = "chapterName",
                superChapterName = "superChapterName",
            )
        ) {_, _ -> }
    }
}

@Composable
fun ArticleItem(
    articleData: ArticleData = ArticleData(),
    onClick: (link: String, title: String) -> Unit
) {
    var selected by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick(articleData.link.orEmpty(), articleData.title.orEmpty()) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 10.dp
        ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = "${articleData.superChapterName ?: ""}/${articleData.chapterName ?: ""}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f)
        )

        //标题
        Text(
            modifier = Modifier
                .heightIn(min = 40.dp)
                .padding(top = 8.dp, start = 10.dp, end = 10.dp),
            text = articleData.title ?: "",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                lineHeight = TextUnit(20f, TextUnitType.Sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
        )

        //作者
        Row(
            modifier = Modifier.padding(top = 8.dp, start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${(articleData.author ?: "").ifEmpty { articleData.shareUser ?: "" }} · ${articleData.niceDate ?: ""}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f)
            )
            Spacer(modifier = Modifier.weight(1f))
            AnimatedHeart(
                modifier = Modifier.size(20.dp),
                selected = selected,
                onToggle = { selected = it },
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
    }
}