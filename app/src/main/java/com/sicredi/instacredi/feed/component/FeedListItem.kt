package com.sicredi.instacredi.feed.component

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.sicredi.core.extensions.AppBackground
import com.sicredi.core.extensions.asFormattedPrice
import com.sicredi.core.ui.compose.AppTheme
import com.sicredi.core.ui.compose.component.AppImage

@Composable
fun FeedListItem(
    modifier: Modifier = Modifier,
    image: String = "",
    title: String = "",
    price: Double = 0.0,
    onClick: () -> Unit = {}
) {
    Column {
        Card(modifier = modifier, onClick = onClick) {
            Column {
                AppImage(
                    modifier = Modifier.aspectRatio(ratio = 1f, matchHeightConstraintsFirst = true),
                    uri = remember { Uri.parse(image) },
                    contentScale = ContentScale.Crop
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        modifier = Modifier
                            .weight(weight = 1f, fill = false)
                            .padding(all = AppTheme.Dimens.spacing.tiny),
                        text = title,
                        style = AppTheme.typography.material.subtitle2,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                    Text(
                        modifier = Modifier
                            .padding(all = AppTheme.Dimens.spacing.tiny),
                        text = price.asFormattedPrice,
                        style = AppTheme.typography.material.subtitle1
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xtiny))
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeedListItemPreviewDark() {
    FeedListItemPreview()
}

@Composable
private fun FeedListItemPreview() {
    AppTheme {
        AppBackground {
            FeedListItem(
                title = "Some huge text to see how some similar text will appear in this layout. Ok, " +
                        "for smartphone it's already big enough.",
                price = 42.50
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun FeedListItemPreviewLight() {
    FeedListItemPreview()
}