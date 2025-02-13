package com.rng350.mediatracker.screens.featuredmovies.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rng350.mediatracker.R
import com.rng350.mediatracker.ui.theme.LightDullBlue
import com.rng350.mediatracker.ui.theme.DarkDullBlue
import com.rng350.mediatracker.ui.theme.MidDullBlue

@Composable
fun TrendingToggleButton(
    selectedTrendingPeriod: TrendingPeriod,
    onToggle: (TrendingPeriod) -> Unit
) {
    val toggleWidth = 250.dp
    val toggleHeight = 50.dp
    val togglePadding = 0.dp
    val indicatorWidth = (toggleWidth / 2) - (togglePadding * 2)
    val offsetX by animateDpAsState(
        targetValue = if (selectedTrendingPeriod == TrendingPeriod.TODAY) togglePadding else (toggleWidth/2),
        label = "Trending Button Toggle Animation"
    )

    Box (
        modifier = Modifier
            .width(toggleWidth)
            .height(toggleHeight)
            .clip(RoundedCornerShape(25.dp))
            .background(LightDullBlue.copy(alpha = 0.2f))
            .clickable {
                onToggle(
                    if (selectedTrendingPeriod == TrendingPeriod.TODAY) TrendingPeriod.THIS_WEEK else TrendingPeriod.TODAY
                )
            }
    ) {
        Box(
            modifier = Modifier
                .size(width = indicatorWidth, height = toggleHeight - (togglePadding * 2))
                .offset(x = offsetX)
                .clip(RoundedCornerShape(25.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            DarkDullBlue,
                            MidDullBlue
                        )
                    )
                )
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable {
                        onToggle(TrendingPeriod.TODAY)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.today),
                    color = if (selectedTrendingPeriod == TrendingPeriod.TODAY) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable {
                        onToggle(TrendingPeriod.THIS_WEEK)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.this_week),
                    color = if (selectedTrendingPeriod == TrendingPeriod.THIS_WEEK) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrendingToggleButtonPreview() {
    TrendingToggleButton(
        selectedTrendingPeriod = TrendingPeriod.THIS_WEEK,
        onToggle = {}
    )
}