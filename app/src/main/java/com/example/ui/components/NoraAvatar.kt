package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.NoraExpression
import com.example.ui.theme.NoraCoralPink
import com.example.ui.theme.NoraDarkNavy
import com.example.ui.theme.NoraElectricViolet
import com.example.ui.theme.NoraMintGreen
import com.example.ui.theme.NoraPrimaryPurple
import com.example.ui.theme.NoraSkyCyan
import com.example.ui.theme.NoraSunshineYellow
import com.example.ui.theme.NoraWhite

@Composable
fun NoraAvatar(
    expression: NoraExpression,
    modifier: Modifier = Modifier,
    size: Dp = 90.dp,
    showExpressionBadge: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "nora_float")

    // Gentle vertical floating motion
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "nora_y_offset"
    )

    // Pulse scale for active states (speaking, listening, celebrating)
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (expression == NoraExpression.LISTENING || expression == NoraExpression.SPEAKING) 1.05f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "nora_pulse"
    )

    // Aura ring color based on expression
    val auraColor by animateColorAsState(
        targetValue = when (expression) {
            NoraExpression.WELCOME -> NoraElectricViolet
            NoraExpression.SPEAKING -> NoraSkyCyan
            NoraExpression.LISTENING -> NoraCoralPink
            NoraExpression.THINKING -> NoraSunshineYellow
            NoraExpression.CELEBRATING -> NoraMintGreen
            NoraExpression.ENCOURAGING -> NoraSunshineYellow
            NoraExpression.CORRECTING -> NoraCoralPink
        },
        animationSpec = tween(500),
        label = "aura_color"
    )

    Box(
        modifier = modifier
            .offset(y = floatOffset.dp)
            .scale(pulseScale),
        contentAlignment = Alignment.Center
    ) {
        // Outer glowing halo
        Box(
            modifier = Modifier
                .size(size + 14.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            auraColor.copy(alpha = 0.45f),
                            auraColor.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Pulsing border ring
        Box(
            modifier = Modifier
                .size(size + 4.dp)
                .clip(CircleShape)
                .border(2.5.dp, Brush.linearGradient(listOf(NoraSkyCyan, auraColor)), CircleShape)
        )

        // Avatar Image
        Surface(
            modifier = Modifier
                .size(size)
                .shadow(12.dp, CircleShape),
            shape = CircleShape,
            color = NoraDarkNavy
        ) {
            Image(
                painter = painterResource(id = R.drawable.nora_portrait),
                contentDescription = "NORA AI English Tutor",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }

        // Floating expression status badge
        if (showExpressionBadge) {
            val (badgeText, badgeIcon, badgeBg) = when (expression) {
                NoraExpression.WELCOME -> Triple("Ready to Learn", Icons.Default.AutoAwesome, NoraPrimaryPurple)
                NoraExpression.SPEAKING -> Triple("Speaking", Icons.Default.RecordVoiceOver, NoraSkyCyan)
                NoraExpression.LISTENING -> Triple("Listening...", Icons.Default.Hearing, NoraCoralPink)
                NoraExpression.THINKING -> Triple("Thinking...", Icons.Default.Psychology, NoraSunshineYellow)
                NoraExpression.CELEBRATING -> Triple("Brilliant! 🎉", Icons.Default.AutoAwesome, NoraMintGreen)
                NoraExpression.ENCOURAGING -> Triple("Keep going!", Icons.Default.ThumbUp, NoraSunshineYellow)
                NoraExpression.CORRECTING -> Triple("Quick Tip", Icons.Default.AutoAwesome, NoraCoralPink)
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 12.dp),
                shape = RoundedCornerShape(12.dp),
                color = badgeBg,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = badgeIcon,
                        contentDescription = null,
                        modifier = Modifier.size(11.dp),
                        tint = if (badgeBg == NoraSunshineYellow) NoraDarkNavy else NoraWhite
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (badgeBg == NoraSunshineYellow) NoraDarkNavy else NoraWhite
                    )
                }
            }
        }
    }
}
