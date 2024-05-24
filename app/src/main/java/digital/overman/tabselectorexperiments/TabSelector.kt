@file:OptIn(ExperimentalAnimationApi::class)

package digital.overman.tabselectorexperiments

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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


data class TabItem(
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun ItemContainer(
    tabItems: List<TabItem>,
    modifier: Modifier = Modifier
) {
    // Keep track of which item is selected, for highlighting purposes
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    // Keep track of where each option is positioned
    val positions = remember { mutableStateListOf<Int>() }

    // Keep track of the size of the options, so the animated highlight will match
    var itemSize by remember { mutableStateOf(IntSize(0, 0)) }
    val itemWidthDp = with(LocalDensity.current) { itemSize.width.toDp() }
    val itemHeightDp = with(LocalDensity.current) { itemSize.height.toDp() }

    // This is required by the clickable modifier, in order to use the .clickable override that will take a null interaction value
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        tabItems.forEachIndexed { index, item ->
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(100))
                    .weight(1f)
                    .onSizeChanged {
                        itemSize = it
                    }
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                    .onGloballyPositioned {
                        positions.add(
                            index,
                            it.boundsInParent().left.toInt()
                        )
                    }
                    .clickable(
                        interactionSource = interactionSource, // this is required in order to be able to send in an indication value
                        indication = null, // turn off the default color animation typically applied when clicking
                        onClick = {
                            selectedItemIndex = index
                            item.onClick()
                        },
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = item.label,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
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
        label = "offset",
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
                    slideInHorizontally(animationSpec = spring()) { 200 } togetherWith slideOutHorizontally(
                        animationSpec = tween(durationMillis = 100)
                    ) { -200 }
                } else {
                    slideInHorizontally(animationSpec = spring()) { -200 } togetherWith slideOutHorizontally(
                        animationSpec = tween(durationMillis = 100)
                    ) { 200 }
                }
            },
            label = "moving_text"
        ) { index ->
            Text(text = tabItems[index].label)
        }
    }
}

/**
 * Because the preview does not actually lay things out, the moving color animation does not
 * receive the positions of each menu item. So the interactive preview does not show an actual
 * animation.
 */
@Preview
@Composable
private fun ItemContainerPreview() {
    TabSelectorExperimentsTheme {
        ItemContainer(
            tabItems = listOf(
                TabItem("All", {}),
                TabItem("Mine", {}),
                TabItem("Yours", {}),
                TabItem("Theirs", {})
            )
        )
    }
}