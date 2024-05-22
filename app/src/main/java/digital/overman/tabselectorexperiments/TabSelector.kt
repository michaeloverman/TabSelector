@file:OptIn(ExperimentalAnimationApi::class)

package digital.overman.tabselectorexperiments

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import digital.overman.tabselectorexperiments.ui.theme.TabSelectorExperimentsTheme

@Composable
fun TabSelector(
    tabs: List<String>
) {

}

@Composable
fun ItemContainer(
    content: List<String>,
    modifier: Modifier = Modifier
) {
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val positions = remember { mutableStateListOf<Int>() }
    var itemSize by remember { mutableStateOf(IntSize(0, 0)) }
    val itemWidthDp = with(LocalDensity.current) { itemSize.width.toDp() }
    val itemHeightDp = with(LocalDensity.current) { itemSize.height.toDp() }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        content.forEachIndexed { index, item ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100))
                    .weight(1f)
            ) {
                TabItem(
                    label = item,
                    onClick = { selectedItemIndex = index },
                    modifier = modifier
                        .onSizeChanged {
                            itemSize = it
                        }
                        .onGloballyPositioned {
                            positions.add(
                                index,
                                it.parentLayoutCoordinates?.boundsInParent()?.left?.toInt() ?: 0
                            )
                        }
                )
            }
        }
    }

    val offset by animateIntOffsetAsState(
        targetValue = IntOffset(
            if (positions.isNotEmpty()) with(LocalDensity.current) { positions[selectedItemIndex] }
            else 0,
            0
        ),
        label = "offset"
    )
    Box(
        modifier = modifier
            .offset { offset }
            .padding(8.dp)
            .defaultMinSize(minWidth = itemWidthDp, minHeight = itemHeightDp)
            .clip(RoundedCornerShape(100))
            .background(Color.Yellow),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = selectedItemIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally(animationSpec = tween(durationMillis = 300)) { 100 } +
                            fadeIn() togetherWith slideOutHorizontally(
                        animationSpec = tween(durationMillis = 150)
                    ) { -100 } + fadeOut()
                } else {
                    slideInHorizontally(animationSpec = tween(durationMillis = 300)) { -100 } +
                            fadeIn() togetherWith slideOutHorizontally(
                        animationSpec = tween(durationMillis = 150)
                    ) { 100 } + fadeOut()
                }
            },
            label = "moving_text"
        ) { index ->
            Text(text = content[index])
        }
    }
}

@Preview
@Composable
private fun ItemContainerPreview() {
    TabSelectorExperimentsTheme {
        ItemContainer(
            content = listOf(
                "All",
                "Mine",
                "Yours",
                "Theirs"
            )
        )
    }
}

@Composable
fun TabItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .clip(RoundedCornerShape(100))
            .background(
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            )
            .padding(16.dp)
    ) {
        Text(
            text = label,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun TabSelectorPreview() {
    TabSelectorExperimentsTheme {
        TabSelector(
            listOf(
                "All",
                "Mine"
            )
        )
    }
}