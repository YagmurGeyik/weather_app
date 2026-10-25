package com.example.hava.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import com.example.hava.ui.viewmodel.CityViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hava.data.model.CityDto
import com.example.hava.data.model.WeatherResponse
import com.example.hava.ui.state.LoadState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

private fun weatherGradient(code: Int, isDark: Boolean): List<Color> = when (code) {
    0    -> if (isDark) listOf(Color(0xFF0D1B2E), Color(0xFF1A3A5C), Color(0xFF0D2E4A))
            else listOf(Color(0xFF1565C0), Color(0xFF42A5F5), Color(0xFF81D4FA))
    1, 2, 3 -> if (isDark) listOf(Color(0xFF0D1B2E), Color(0xFF1B2838), Color(0xFF243447))
               else listOf(Color(0xFF37474F), Color(0xFF546E7A), Color(0xFF78909C))
    45, 48 -> if (isDark) listOf(Color(0xFF1A1A2E), Color(0xFF2D2D44), Color(0xFF16213E))
              else listOf(Color(0xFF607D8B), Color(0xFF78909C), Color(0xFF90A4AE))
    in 51..67 -> if (isDark) listOf(Color(0xFF0A0F1E), Color(0xFF0D1B3E), Color(0xFF0A2040))
                 else listOf(Color(0xFF1A237E), Color(0xFF283593), Color(0xFF3949AB))
    in 71..77 -> if (isDark) listOf(Color(0xFF1A1A3E), Color(0xFF1C2A4A), Color(0xFF0D1B2E))
                 else listOf(Color(0xFF90CAF9), Color(0xFFBBDEFB), Color(0xFFE3F2FD))
    in 80..82 -> if (isDark) listOf(Color(0xFF0A0F1E), Color(0xFF0D1830), Color(0xFF0A1428))
                 else listOf(Color(0xFF1565C0), Color(0xFF0D47A1), Color(0xFF01579B))
    95, 96, 99 -> if (isDark) listOf(Color(0xFF0A0A1A), Color(0xFF12112B), Color(0xFF0D0D20))
                  else listOf(Color(0xFF311B92), Color(0xFF4527A0), Color(0xFF512DA8))
    else -> if (isDark) listOf(Color(0xFF050D1A), Color(0xFF0A1628), Color(0xFF111E35))
            else listOf(Color(0xFF1565C0), Color(0xFF1976D2), Color(0xFF42A5F5))
}

private fun weatherEmoji(code: Int): String = when (code) {
    0 -> "☀️"; 1 -> "🌤"; 2 -> "⛅"; 3 -> "☁️"
    45, 48 -> "🌫️"; in 51..55 -> "🌦"; 56, 57 -> "🌨"
    in 61..65 -> "🌧"; 66, 67 -> "🌨"; in 71..77 -> "❄️"
    in 80..82 -> "⛈"; 95 -> "🌩"; 96, 99 -> "⛈"
    else -> "🌡️"
}

fun getWeatherDescription(code: Int): String = when (code) {
    0 -> "Açık"; 1, 2, 3 -> "Parçalı Bulutlu"; 45, 48 -> "Sisli"
    51, 53, 55, 56, 57 -> "Çiseleyen Yağmur"; in 61..67 -> "Yağmurlu"
    in 71..77 -> "Karlı"; in 80..82 -> "Sağanak Yağışlı"
    95, 96, 99 -> "Fırtınalı"; else -> "Bulutlu"
}

fun formatHour(isoTime: String): String = try {
    LocalDateTime.parse(isoTime).format(DateTimeFormatter.ofPattern("HH:mm"))
} catch (_: Exception) { isoTime }

fun formatDayName(isoDate: String, index: Int): String {
    if (index == 0) return "Bugün"
    return try {
        LocalDateTime.parse("${isoDate}T00:00:00")
            .format(DateTimeFormatter.ofPattern("EEEE", Locale.forLanguageTag("tr")))
            .replaceFirstChar { it.uppercase() }
    } catch (_: Exception) { isoDate }
}

@Composable
private fun WeatherAnimation(weatherCode: Int, modifier: Modifier = Modifier) {
    val inf = rememberInfiniteTransition(label = "weather_anim")

    val fastTime by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "fast"
    )
    val slowTime by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "slow"
    )
    val sunRot by inf.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(12000, easing = LinearEasing)),
        label = "sunRot"
    )
    val glow by inf.animateFloat(
        0.6f, 1f,
        infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )
    val flash by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "flash"
    )

    Canvas(modifier = modifier) {
        when (weatherCode) {
            0                -> drawSunEffect(sunRot, glow)
            1, 2, 3          -> { drawCloudEffect(slowTime); if (weatherCode == 3) drawCloudEffect((slowTime + 0.5f) % 1f, alpha = 0.12f) }
            45, 48           -> drawFogEffect(slowTime)
            in 51..57        -> { drawRainEffect(fastTime, intensity = 30); drawCloudEffect(slowTime, alpha = 0.15f) }
            in 61..67        -> { drawRainEffect(fastTime, intensity = 60); drawCloudEffect(slowTime, alpha = 0.18f) }
            in 71..77        -> drawSnowEffect(fastTime)
            in 80..82        -> { drawRainEffect(fastTime, intensity = 80, heavy = true); drawCloudEffect(slowTime, alpha = 0.2f) }
            95, 96, 99       -> { drawThunderEffect(fastTime, flash); drawRainEffect(fastTime, intensity = 70, heavy = true) }
            else             -> drawCloudEffect(slowTime)
        }
    }
}

private fun DrawScope.drawSunEffect(rotation: Float, glow: Float) {
    val cx = size.width * 0.78f
    val cy = size.height * 0.18f
    val innerR = size.minDimension * 0.10f
    val outerR  = size.minDimension * 0.16f

    drawCircle(
        brush = Brush.radialGradient(
            listOf(Color(0x55FFD54F), Color.Transparent),
            center = Offset(cx, cy),
            radius = outerR * 2.5f * glow
        ),
        radius = outerR * 2.5f * glow,
        center = Offset(cx, cy)
    )

    rotate(rotation, Offset(cx, cy)) {
        val rayCount = 12
        for (i in 0 until rayCount) {
            val angle = (i.toFloat() / rayCount * 2 * PI).toFloat()
            val startX = cx + cos(angle) * innerR * 1.3f
            val startY = cy + sin(angle) * innerR * 1.3f
            val endX   = cx + cos(angle) * outerR
            val endY   = cy + sin(angle) * outerR
            val alpha  = if (i % 2 == 0) 0.55f else 0.30f
            drawLine(
                Color(0xFFFFD54F).copy(alpha = alpha * glow),
                Offset(startX, startY), Offset(endX, endY),
                strokeWidth = size.minDimension * 0.012f,
                cap = StrokeCap.Round
            )
        }
    }

    drawCircle(
        brush = Brush.radialGradient(
            listOf(Color(0xFFFFF9C4), Color(0xFFFFD54F)),
            center = Offset(cx, cy)
        ),
        radius = innerR,
        center = Offset(cx, cy)
    )
}

private data class CloudDef(val yFrac: Float, val speed: Float, val scale: Float, val alpha: Float)

private val cloudDefs = listOf(
    CloudDef(0.08f, 0.04f, 1.0f, 0.18f),
    CloudDef(0.18f, 0.07f, 0.75f, 0.14f),
    CloudDef(0.04f, 0.10f, 0.55f, 0.12f),
    CloudDef(0.26f, 0.05f, 0.85f, 0.10f),
)

private fun DrawScope.drawCloudEffect(time: Float, alpha: Float = 1f) {
    for (cloud in cloudDefs) {
        val rawX = (time * cloud.speed * size.width * 0.5f + cloud.yFrac * size.width * 2.7f) % (size.width + 300f)
        val x = rawX - 150f
        val y = cloud.yFrac * size.height
        drawSingleCloud(x, y, cloud.scale, cloud.alpha * alpha)
    }
}

private fun DrawScope.drawSingleCloud(cx: Float, cy: Float, scale: Float, alpha: Float) {
    val r = size.minDimension * 0.12f * scale
    val cloudColor = Color.White.copy(alpha = alpha)

    drawOval(cloudColor, topLeft = Offset(cx - r, cy - r * 0.5f), size = Size(r * 2f, r))
    drawOval(cloudColor, topLeft = Offset(cx + r * 0.5f, cy - r * 0.8f), size = Size(r * 1.4f, r * 0.9f))
    drawOval(cloudColor, topLeft = Offset(cx - r * 1.4f, cy - r * 0.3f), size = Size(r * 1.2f, r * 0.7f))
    drawRect(cloudColor, topLeft = Offset(cx - r * 1.2f, cy - r * 0.05f), size = Size(r * 3f, r * 0.6f))
}

private fun DrawScope.drawRainEffect(time: Float, intensity: Int = 50, heavy: Boolean = false) {
    val dropH  = size.height * (if (heavy) 0.045f else 0.032f)
    val sway   = size.width * 0.06f
    val alpha  = if (heavy) 0.55f else 0.40f

    for (i in 0 until intensity) {
        val hash     = (i * 137.508f) % 1f
        val xBase    = hash * size.width
        val yPhase   = (time + i * 0.0371f) % 1f
        val x        = xBase + sway * yPhase
        val y        = yPhase * (size.height + dropH) - dropH

        drawLine(
            Color.White.copy(alpha = alpha * (0.6f + hash * 0.4f)),
            start = Offset(x, y),
            end   = Offset(x + sway * 0.08f, y + dropH),
            strokeWidth = if (heavy) 2.2f else 1.4f,
            cap   = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawSnowEffect(time: Float) {
    val flakeCount = 45
    for (i in 0 until flakeCount) {
        val hash    = (i * 137.508f) % 1f
        val xBase   = hash * size.width
        val yPhase  = (time * 0.5f + i * 0.0333f) % 1f
        val swayX   = sin((time * 2.5f + i * 2.1f).toDouble()).toFloat() * size.width * 0.02f
        val radius  = size.minDimension * 0.006f * (0.5f + hash * 0.8f)

        drawCircle(
            Color.White.copy(alpha = 0.65f + hash * 0.25f),
            radius = radius,
            center = Offset(xBase + swayX, yPhase * size.height)
        )
    }
}

private fun DrawScope.drawFogEffect(time: Float) {
    val fogCount = 5
    for (i in 0 until fogCount) {
        val yFrac = 0.1f + i * 0.18f
        val phase = (time * 0.03f + i * 0.2f) % 1f
        val alpha = 0.06f + abs(sin(phase * PI).toFloat()) * 0.06f
        drawRect(
            Color.White.copy(alpha = alpha),
            topLeft = Offset(0f, yFrac * size.height),
            size = Size(size.width, size.height * 0.09f)
        )
    }
}

private fun DrawScope.drawThunderEffect(time: Float, flash: Float) {
    val flashPhase = (flash * 5) % 1f
    if (flashPhase < 0.06f) {
        drawRect(Color.White.copy(alpha = flashPhase * 4f * 0.3f), size = size)
    }
    drawRect(
        brush = Brush.verticalGradient(
            listOf(Color(0x1AFFFFFF), Color.Transparent)
        ),
        size = Size(size.width, size.height * 0.25f)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailScreen(
    city: CityDto,
    weatherState: LoadState<WeatherResponse>?,
    viewModel: CityViewModel,
    onBackClick: () -> Unit,
    isDarkTheme: Boolean = true
) {
    val weatherCode = if (weatherState is LoadState.Success<*>) {
        @Suppress("UNCHECKED_CAST")
        (weatherState as LoadState.Success<WeatherResponse>).data.current?.weatherCode ?: 0
    } else 0

    val gradient    = weatherGradient(weatherCode, isDarkTheme)
    val glassColor  = Color(0x22FFFFFF)
    val glassBorder = Color(0x44FFFFFF)
    val textPrimary = Color.White
    val textSub     = Color.White.copy(alpha = 0.65f)
    val accent      = Color(0xFF4FC3F7)

    val inf = rememberInfiniteTransition(label = "orb")
    val pulse by inf.animateFloat(
        0.85f, 1.15f,
        infiniteRepeatable(tween(3500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradient))
    ) {
        if (weatherState is LoadState.Success) {
            WeatherAnimation(
                weatherCode = weatherCode,
                modifier    = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .size(350.dp).offset(x = (-60).dp, y = (-60).dp)
                .scale(pulse).blur(80.dp).clip(CircleShape)
                .background(Brush.radialGradient(listOf(Color.White.copy(0.07f), Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .size(250.dp).align(Alignment.BottomEnd).offset(x = 60.dp, y = 60.dp)
                .scale(2f - pulse).blur(60.dp).clip(CircleShape)
                .background(Brush.radialGradient(listOf(Color.White.copy(0.05f), Color.Transparent)))
        )

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Column {
                        Text(city.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        if (city.subtitle.isNotBlank())
                            Text(city.subtitle, fontSize = 12.sp, color = textSub)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(start = 4.dp).size(40.dp)
                            .clip(CircleShape).background(glassColor)
                            .border(1.dp, glassBorder, CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri", tint = textPrimary, modifier = Modifier.size(20.dp))
                    }
                },
                actions = {
                    val favorites by viewModel.favorites.collectAsState()
                    val isFav = favorites.any { it.id == city.id }
                    IconButton(
                        onClick = { viewModel.toggleFavorite(city) },
                        modifier = Modifier
                            .padding(end = 12.dp).size(40.dp)
                            .clip(CircleShape).background(glassColor)
                            .border(1.dp, glassBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = if (isFav) Color.Red else textPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            when (weatherState) {
                is LoadState.Loading, null -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        val emojiScale by inf.animateFloat(
                            0.8f, 1.2f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "e"
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(weatherEmoji(0), fontSize = 64.sp, modifier = Modifier.scale(emojiScale))
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressIndicator(color = accent)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Hava bilgisi yükleniyor...", color = textSub, fontSize = 14.sp)
                        }
                    }
                }

                is LoadState.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    val weather = (weatherState as LoadState.Success<WeatherResponse>).data
                    val current = weather.current
                    val hourly  = weather.hourly
                    val daily   = weather.daily

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(28.dp))
                                .background(glassColor)
                                .border(1.dp, glassBorder, RoundedCornerShape(28.dp))
                                .padding(28.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(weatherEmoji(current?.weatherCode ?: 0), fontSize = 72.sp)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "${current?.temperature?.toInt() ?: 0}°",
                                    fontSize = 88.sp, fontWeight = FontWeight.Black,
                                    color = textPrimary, lineHeight = 88.sp
                                )
                                Text(
                                    getWeatherDescription(current?.weatherCode ?: 0),
                                    fontSize = 20.sp, fontWeight = FontWeight.Medium, color = textSub
                                )
                                Spacer(Modifier.height(12.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    StatPill("Hissedilen", "${current?.apparentTemperature?.toInt() ?: 0}°", Color(0x33FFFFFF))
                                    StatPill("Nem", "%${current?.humidity ?: 0}", Color(0x334FC3F7))
                                    StatPill("Rüzgâr", "${current?.windSpeed?.toInt() ?: 0} km/s", Color(0x3390CAF9))
                                }
                            }
                        }

                        if (hourly != null && hourly.time.isNotEmpty()) {
                            SectionTitle("⏱  Saatlik Tahmin", textPrimary, accent)

                            val startIdx = maxOf(0, hourly.time.indexOfFirst {
                                try { LocalDateTime.parse(it).isAfter(LocalDateTime.now()) }
                                catch (_: Exception) { false }
                            })
                            val count = minOf(24, hourly.time.size - startIdx)

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                itemsIndexed(hourly.time.subList(startIdx, startIdx + count)) { i, timeStr ->
                                    val idx   = startIdx + i
                                    val temp  = hourly.temperature.getOrNull(idx)?.toInt() ?: 0
                                    val code  = hourly.weatherCode.getOrNull(idx) ?: 0
                                    HourlyCard(
                                        hour      = formatHour(timeStr),
                                        emoji     = weatherEmoji(code),
                                        temp      = "$temp°",
                                        condition = getWeatherDescription(code),
                                        glass     = glassColor,
                                        border    = glassBorder,
                                        textColor = textPrimary,
                                        textSub   = textSub
                                    )
                                }
                            }
                        }

                        if (daily != null && daily.time.isNotEmpty()) {
                            SectionTitle("📅  7 Günlük Tahmin", textPrimary, accent)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(glassColor)
                                    .border(1.dp, glassBorder, RoundedCornerShape(24.dp))
                                    .padding(vertical = 8.dp)
                            ) {
                                Column {
                                    daily.time.indices.forEach { i ->
                                        DailyRow(
                                            day         = formatDayName(daily.time[i], i),
                                            emoji       = weatherEmoji(daily.weatherCode.getOrNull(i) ?: 0),
                                            condition   = getWeatherDescription(daily.weatherCode.getOrNull(i) ?: 0),
                                            rainProb    = daily.precipitationProbability?.getOrNull(i) ?: 0,
                                            minTemp     = "${daily.temperatureMin.getOrNull(i)?.toInt() ?: 0}°",
                                            maxTemp     = "${daily.temperatureMax.getOrNull(i)?.toInt() ?: 0}°",
                                            textColor   = textPrimary,
                                            textSub     = textSub,
                                            accent      = accent,
                                            showDivider = i < daily.time.indices.last
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                is LoadState.Error -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Box(
                            modifier = Modifier.padding(24.dp).clip(RoundedCornerShape(20.dp))
                                .background(Color(0x33FF5252)).border(1.dp, Color(0x55FF5252), RoundedCornerShape(20.dp)).padding(24.dp)
                        ) {
                            Text("⚠️  ${weatherState.message}", color = Color(0xFFFF8A80), fontSize = 15.sp, textAlign = TextAlign.Center)
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun StatPill(label: String, value: String, bgColor: Color) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(bgColor).padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f), letterSpacing = 0.3.sp)
        }
    }
}

@Composable
private fun SectionTitle(title: String, textColor: Color, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.width(3.dp).height(16.dp).clip(RoundedCornerShape(2.dp)).background(accent))
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textColor, letterSpacing = 0.3.sp)
    }
}

@Composable
fun HourlyCard(
    hour: String, emoji: String, temp: String, condition: String,
    glass: Color, border: Color, textColor: Color, textSub: Color
) {
    Box(
        modifier = Modifier.width(80.dp).clip(RoundedCornerShape(20.dp))
            .background(glass).border(1.dp, border, RoundedCornerShape(20.dp))
            .padding(vertical = 14.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(hour, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = textSub)
            Text(emoji, fontSize = 26.sp)
            Text(temp, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
            Text(condition, fontSize = 9.sp, color = textSub, textAlign = TextAlign.Center, lineHeight = 12.sp, maxLines = 2)
        }
    }
}

@Composable
private fun DailyRow(
    day: String, emoji: String, condition: String,
    rainProb: Int, minTemp: String, maxTemp: String,
    textColor: Color, textSub: Color, accent: Color, showDivider: Boolean
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(day, modifier = Modifier.width(90.dp), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
            Text(emoji, fontSize = 22.sp)
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(condition, fontSize = 12.sp, color = textSub, maxLines = 1)
                if (rainProb > 0) Text("%$rainProb", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(minTemp, fontSize = 14.sp, color = textSub)
                Text(maxTemp, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
        }
        if (showDivider) {
            Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).padding(horizontal = 16.dp).background(Color.White.copy(0.12f)))
        }
    }
}
