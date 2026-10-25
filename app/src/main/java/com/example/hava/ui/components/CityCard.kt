package com.example.hava.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hava.data.model.CityDto
import com.example.hava.ui.state.LoadState
import com.example.hava.ui.viewmodel.CityViewModel

private data class CityMeta(val plate: String, val emoji: String, val color: Color)

private val cityMeta = mapOf(
    "Adana" to CityMeta("01", "🌶", Color(0xFFC62828)),
    "Adıyaman" to CityMeta("02", "🏔", Color(0xFFD32F2F)),
    "Afyonkarahisar" to CityMeta("03", "🏛", Color(0xFF7B1FA2)),
    "Ağrı" to CityMeta("04", "⛰", Color(0xFF512DA8)),
    "Amasya" to CityMeta("05", "🍎", Color(0xFF303F9F)),
    "Ankara" to CityMeta("06", "🏛", Color(0xFF6A1B9A)),
    "Antalya" to CityMeta("07", "🏖", Color(0xFFAD1457)),
    "Artvin" to CityMeta("08", "🌲", Color(0xFF00796B)),
    "Aydın" to CityMeta("09", "🌿", Color(0xFF388E3C)),
    "Balıkesir" to CityMeta("10", "🌊", Color(0xFF0288D1)),
    "Bilecik" to CityMeta("11", "🏛", Color(0xFF4527A0)),
    "Bingöl" to CityMeta("12", "⛰", Color(0xFF5C6BC0)),
    "Bitlis" to CityMeta("13", "🏰", Color(0xFF3949AB)),
    "Bolu" to CityMeta("14", "🌲", Color(0xFF00838F)),
    "Burdur" to CityMeta("15", "🦆", Color(0xFF00695C)),
    "Bursa" to CityMeta("16", "⛰", Color(0xFF4E342E)),
    "Çanakkale" to CityMeta("17", "⚓", Color(0xFFC2185B)),
    "Çankırı" to CityMeta("18", "🧂", Color(0xFF512DA8)),
    "Çorum" to CityMeta("19", "🏛", Color(0xFF6A1B9A)),
    "Denizli" to CityMeta("20", "🏛", Color(0xFF00695C)),
    "Diyarbakır" to CityMeta("21", "🍉", Color(0xFFE64A19)),
    "Edirne" to CityMeta("22", "🕌", Color(0xFFC62828)),
    "Elazığ" to CityMeta("23", "🍇", Color(0xFF8E24AA)),
    "Erzincan" to CityMeta("24", "⛰", Color(0xFF3949AB)),
    "Erzurum" to CityMeta("25", "⛷", Color(0xFF1976D2)),
    "Eskişehir" to CityMeta("26", "🏛", Color(0xFF0288D1)),
    "Gaziantep" to CityMeta("27", "🥙", Color(0xFF558B2F)),
    "Giresun" to CityMeta("28", "🌰", Color(0xFF388E3C)),
    "Gümüşhane" to CityMeta("29", "⛰", Color(0xFF00796B)),
    "Hakkâri" to CityMeta("30", "🏔", Color(0xFF4527A0)),
    "Hakkari" to CityMeta("30", "🏔", Color(0xFF4527A0)),
    "Hatay" to CityMeta("31", "⛪", Color(0xFFD81B60)),
    "Isparta" to CityMeta("32", "🌷", Color(0xFFC2185B)),
    "Mersin" to CityMeta("33", "🍊", Color(0xFFF57C00)),
    "İçel" to CityMeta("33", "🍊", Color(0xFFF57C00)),
    "İstanbul" to CityMeta("34", "🌉", Color(0xFF1565C0)),
    "İzmir" to CityMeta("35", "⛵", Color(0xFF00695C)),
    "Kars" to CityMeta("36", "🧀", Color(0xFF303F9F)),
    "Kastamonu" to CityMeta("37", "🌲", Color(0xFF00838F)),
    "Kayseri" to CityMeta("38", "🏔", Color(0xFF4527A0)),
    "Kırklareli" to CityMeta("39", "🌳", Color(0xFF2E7D32)),
    "Kırşehir" to CityMeta("40", "🏛", Color(0xFF558B2F)),
    "Kocaeli" to CityMeta("41", "🏭", Color(0xFF0277BD)),
    "Konya" to CityMeta("42", "🕌", Color(0xFF2E7D32)),
    "Kütahya" to CityMeta("43", "🏺", Color(0xFF6A1B9A)),
    "Malatya" to CityMeta("44", "🍑", Color(0xFFE64A19)),
    "Manisa" to CityMeta("45", "🍇", Color(0xFF7B1FA2)),
    "Kahramanmaraş" to CityMeta("46", "🌶", Color(0xFFD32F2F)),
    "Mardin" to CityMeta("47", "🏰", Color(0xFFC2185B)),
    "Muğla" to CityMeta("48", "🏖", Color(0xFF00838F)),
    "Muş" to CityMeta("49", "🌷", Color(0xFF5C6BC0)),
    "Nevşehir" to CityMeta("50", "🎈", Color(0xFFD81B60)),
    "Niğde" to CityMeta("51", "🍎", Color(0xFF8E24AA)),
    "Ordu" to CityMeta("52", "🌰", Color(0xFF2E7D32)),
    "Rize" to CityMeta("53", "🍵", Color(0xFF388E3C)),
    "Sakarya" to CityMeta("54", "🌊", Color(0xFF0288D1)),
    "Samsun" to CityMeta("55", "🚢", Color(0xFF1565C0)),
    "Siirt" to CityMeta("56", "🥜", Color(0xFF6D4C41)),
    "Sinop" to CityMeta("57", "⛵", Color(0xFF00838F)),
    "Sivas" to CityMeta("58", "🦁", Color(0xFF4527A0)),
    "Tekirdağ" to CityMeta("59", "🍇", Color(0xFF7B1FA2)),
    "Tokat" to CityMeta("60", "🌿", Color(0xFF2E7D32)),
    "Trabzon" to CityMeta("61", "🌊", Color(0xFF00838F)),
    "Tunceli" to CityMeta("62", "⛰", Color(0xFF512DA8)),
    "Şanlıurfa" to CityMeta("63", "🏛", Color(0xFFC62828)),
    "Uşak" to CityMeta("64", "🧶", Color(0xFFAD1457)),
    "Van" to CityMeta("65", "🐈", Color(0xFF0288D1)),
    "Yozgat" to CityMeta("66", "🏛", Color(0xFF5C6BC0)),
    "Zonguldak" to CityMeta("67", "⛏", Color(0xFF37474F)),
    "Aksaray" to CityMeta("68", "🏛", Color(0xFF7B1FA2)),
    "Bayburt" to CityMeta("69", "⛰", Color(0xFF4527A0)),
    "Karaman" to CityMeta("70", "🍎", Color(0xFFC2185B)),
    "Kırıkkale" to CityMeta("71", "🛡", Color(0xFF1976D2)),
    "Batman" to CityMeta("72", "🛢", Color(0xFF424242)),
    "Şırnak" to CityMeta("73", "⛰", Color(0xFF5E35B1)),
    "Bartın" to CityMeta("74", "🌲", Color(0xFF00796B)),
    "Ardahan" to CityMeta("75", "🧀", Color(0xFF303F9F)),
    "Iğdır" to CityMeta("76", "⛰", Color(0xFF00838F)),
    "Yalova" to CityMeta("77", "🌿", Color(0xFF2E7D32)),
    "Karabük" to CityMeta("78", "🚂", Color(0xFF546E7A)),
    "Kilis" to CityMeta("79", "🍇", Color(0xFFD81B60)),
    "Osmaniye" to CityMeta("80", "🥜", Color(0xFFE64A19)),
    "Düzce" to CityMeta("81", "🌲", Color(0xFF00695C))
)

private object DarkColors {
    val bgTop       = Color(0xFF050D1A)
    val bgMid       = Color(0xFF0A1628)
    val bgBot       = Color(0xFF111E35)
    val topBar      = Color(0xFF08111F)
    val glass       = Color(0x1AFFFFFF)
    val glassBorder = Color(0x33FFFFFF)
    val text        = Color.White
    val textSub     = Color(0xFFB0C4D8)
    val accent      = Color(0xFF4FC3F7)
    val orb1        = Color(0x551E88E5)
    val orb2        = Color(0x336A1B9A)
}

private object LightColors {
    val bgTop       = Color(0xFFF0F8FF)
    val bgMid       = Color(0xFFDBEEFF)
    val bgBot       = Color(0xFFC2E0FF)
    val topBar      = Color(0xFFF5FBFF)
    val glass       = Color(0x70FFFFFF)
    val glassBorder = Color(0xC0FFFFFF)
    val text        = Color(0xFF0B1D35)
    val textSub     = Color(0xFF2A4D6E)
    val accent      = Color(0xFF0276C2)
    val orb1        = Color(0x7087CEEB)
    val orb2        = Color(0x5090CAF9)
}

@Composable
fun ThemeToggleSwitch(
    isDarkTheme: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val trackWidth  = 64.dp
    val thumbSize   = 26.dp
    val thumbPad    = 3.dp

    val thumbOffset by animateDpAsState(
        targetValue = if (isDarkTheme) thumbPad else trackWidth - thumbSize - thumbPad,
        animationSpec = spring<androidx.compose.ui.unit.Dp>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessMedium
        ),
        label = "thumbOffset"
    )

    val trackColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xFF1A2845) else Color(0xFF4FC3F7),
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label = "trackColor"
    )

    val thumbBg1 = if (isDarkTheme) Color(0xFF3D5A80) else Color(0xFFFFC107)
    val thumbBg2 = if (isDarkTheme) Color(0xFF1B2A4E) else Color(0xFFFF8F00)

    var accumulated by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(thumbSize + thumbPad * 2)
            .clip(RoundedCornerShape(50.dp))
            .background(trackColor)
            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(50.dp))
            .pointerInput(isDarkTheme) {
                detectHorizontalDragGestures(
                    onDragStart  = { accumulated = 0f },
                    onDragEnd    = {
                        if (isDarkTheme  && accumulated >  30f) onToggle()
                        if (!isDarkTheme && accumulated < -30f) onToggle()
                        accumulated = 0f
                    },
                    onHorizontalDrag = { change, delta ->
                        change.consume()
                        accumulated += delta
                    }
                )
            }
            .clickable { onToggle() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text("🌙", fontSize = 12.sp)
            Text("☀️", fontSize = 12.sp)
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = thumbOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(thumbBg1, thumbBg2))
                )
                .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(if (isDarkTheme) "🌙" else "☀️", fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(
    viewModel: CityViewModel,
    listState: LazyListState,
    onCityClick: (CityDto) -> Unit,
    isDarkTheme: Boolean = true,
    onThemeToggle: () -> Unit = {},
    onOpenFavorites: () -> Unit = {}
) {
    val searchQuery by viewModel.query.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val focusManager = LocalFocusManager.current

    val spec = tween<Color>(500, easing = FastOutSlowInEasing)

    val bgTop       by animateColorAsState(if (isDarkTheme) DarkColors.bgTop  else LightColors.bgTop,  spec, "bgTop")
    val bgMid       by animateColorAsState(if (isDarkTheme) DarkColors.bgMid  else LightColors.bgMid,  spec, "bgMid")
    val bgBot       by animateColorAsState(if (isDarkTheme) DarkColors.bgBot  else LightColors.bgBot,  spec, "bgBot")
    val topBarBg    by animateColorAsState(if (isDarkTheme) DarkColors.topBar else LightColors.topBar, spec, "topBar")
    val glass       by animateColorAsState(if (isDarkTheme) DarkColors.glass  else LightColors.glass,  spec, "glass")
    val glassBorder by animateColorAsState(if (isDarkTheme) DarkColors.glassBorder else LightColors.glassBorder, spec, "border")
    val textColor   by animateColorAsState(if (isDarkTheme) DarkColors.text   else LightColors.text,   spec, "text")
    val textSub     by animateColorAsState(if (isDarkTheme) DarkColors.textSub else LightColors.textSub, spec, "sub")
    val accent      by animateColorAsState(if (isDarkTheme) DarkColors.accent  else LightColors.accent, spec, "accent")
    val orb1Color   by animateColorAsState(if (isDarkTheme) DarkColors.orb1   else LightColors.orb1,   spec, "orb1")
    val orb2Color   by animateColorAsState(if (isDarkTheme) DarkColors.orb2   else LightColors.orb2,   spec, "orb2")

    val inf = rememberInfiniteTransition(label = "orb")
    val pulse by inf.animateFloat(
        initialValue = 0.85f, targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(3200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    val cities: List<CityDto> = when (val s = uiState) {
        is LoadState.Success<*> -> @Suppress("UNCHECKED_CAST") s.data as? List<CityDto> ?: emptyList()
        else -> emptyList()
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(Color(0xFF1565C0), accent))),
                            contentAlignment = Alignment.Center
                        ) { Text("🌤", fontSize = 18.sp) }
                        Column(verticalArrangement = Arrangement.spacedBy((-3).dp)) {
                            Text("Hava Durumu", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor, letterSpacing = (-0.3).sp)
                            Text("Türkiye & Dünya", fontSize = 10.sp, color = accent.copy(alpha = 0.8f), letterSpacing = 0.8.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                },
                actions = {
                    val favBg = if (isDarkTheme) Color(0x33FF5252) else Color(0xFFFFEBEE)
                    val favBorder = if (isDarkTheme) Color(0x66FF5252) else Color(0xFFFFCDD2)

                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp, end = 6.dp)
                            .size(40.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(favBg)
                            .border(1.dp, favBorder, RoundedCornerShape(14.dp))
                            .clickable { onOpenFavorites() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favoriler",
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    ThemeToggleSwitch(
                        isDarkTheme = isDarkTheme,
                        onToggle    = onThemeToggle,
                        modifier    = Modifier.padding(top = 6.dp, end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarBg)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(bgTop, bgMid, bgBot)))
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .size(320.dp).offset(x = (-80).dp, y = (-40).dp)
                    .scale(pulse).blur(60.dp).clip(CircleShape)
                    .background(Brush.radialGradient(listOf(orb1Color, Color.Transparent)))
            )
            Box(
                modifier = Modifier
                    .size(240.dp).align(Alignment.BottomEnd).offset(x = 40.dp, y = 40.dp)
                    .scale(2f - pulse).blur(50.dp).clip(CircleShape)
                    .background(Brush.radialGradient(listOf(orb2Color, Color.Transparent)))
            )

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(glass)
                            .border(1.dp, glassBorder, RoundedCornerShape(20.dp))
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { newVal ->
                                viewModel.onQueryChanged(newVal)
                            },
                            placeholder = { Text("Şehir ara...", color = textColor.copy(alpha = 0.35f), fontSize = 15.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Ara",
                                    tint = if (searchQuery.isNotEmpty()) accent else textColor.copy(alpha = 0.4f),
                                    modifier = Modifier.size(22.dp))
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            viewModel.onQueryChanged("")
                                            focusManager.clearFocus()
                                        },
                                        modifier = Modifier.size(36.dp).clip(CircleShape).background(textColor.copy(alpha = 0.1f))
                                    ) {
                                        Icon(Icons.Default.Clear, contentDescription = "Temizle",
                                            tint = textColor.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = textColor, unfocusedTextColor = textColor,
                                focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent,
                                cursorColor = accent,
                                focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrectEnabled = false, keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(onSearch = { viewModel.search(searchQuery); focusManager.clearFocus() }),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (uiState is LoadState.Loading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = accent)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Aranıyor...", color = textColor.copy(alpha = 0.5f), fontSize = 14.sp)
                            }
                        }
                    }
                }

                if (uiState is LoadState.Success<*> && cities.isNotEmpty()) {
                    if (searchQuery.isEmpty()) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Box(modifier = Modifier.width(3.dp).height(14.dp).clip(RoundedCornerShape(2.dp)).background(accent))
                                Text(
                                    "POPÜLER ŞEHİRLER",
                                    fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSub, letterSpacing = 1.2.sp
                                )
                            }
                        }
                    }
                    items(cities) { city ->
                        val meta = cityMeta[city.name]
                        val isFav = favorites.any { it.id == city.id }
                        ModernCityCard(
                            city = city, plateCode = meta?.plate,
                            plateColor = meta?.color ?: Color(0xFF37474F),
                            emoji = meta?.emoji ?: "🌍",
                            glass = glass, glassBorder = glassBorder,
                            textColor = textColor, textSub = textSub, accent = accent,
                            isFavorite = isFav,
                            onFavoriteClick = { viewModel.toggleFavorite(city) },
                            onClick = { onCityClick(city) }
                        )
                    }
                }

                if (uiState == LoadState.Empty) {
                    item {
                        Column(modifier = Modifier.fillMaxWidth().padding(40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("\"$searchQuery\" bulunamadı", color = textColor.copy(alpha = 0.4f), fontSize = 14.sp)
                        }
                    }
                }

                if (uiState is LoadState.Error) {
                    val errorMsg = (uiState as LoadState.Error).message
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                                .background(Color(0x33FF5252)).border(1.dp, Color(0x55FF5252), RoundedCornerShape(16.dp)).padding(20.dp)
                        ) { Text("⚠️  $errorMsg", color = Color(0xFFFF8A80), fontSize = 14.sp) }
                    }
                }
            }
        }
    }
}

@Composable
fun PlateBadge(code: String, plateColor: Color) {
    Row(
        modifier = Modifier.height(22.dp).clip(RoundedCornerShape(4.dp))
            .border(1.dp, plateColor.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
    ) {
        Box(modifier = Modifier.fillMaxSize().weight(1.4f).background(plateColor), contentAlignment = Alignment.Center) {
            Text(code, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, fontFamily = FontFamily.Monospace, letterSpacing = 0.5.sp)
        }
        Box(modifier = Modifier.fillMaxSize().weight(1f).background(Color.White), contentAlignment = Alignment.Center) {
            Text("TR", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1), letterSpacing = 0.5.sp)
        }
    }
}

@Composable
fun ModernCityCard(
    city: CityDto, plateCode: String?, plateColor: Color, emoji: String,
    glass: Color, glassBorder: Color, textColor: Color, textSub: Color, accent: Color,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
            .background(glass).border(1.dp, glassBorder, RoundedCornerShape(20.dp)).clickable { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(plateColor.copy(alpha = 0.8f), plateColor.copy(alpha = 0.45f)))),
                contentAlignment = Alignment.Center
            ) { Text(emoji, fontSize = 24.sp) }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(city.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = textColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(city.subtitle, fontSize = 12.sp, color = textSub.copy(alpha = 0.7f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favori",
                    tint = if (isFavorite) Color.Red else textColor.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(textColor.copy(alpha = 0.07f)).padding(horizontal = 8.dp, vertical = 4.dp)
            ) { Text("›", fontSize = 20.sp, color = accent, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
fun CityCard(city: CityDto, onClick: () -> Unit, modifier: Modifier = Modifier, isSelected: Boolean = false) {
    val meta = cityMeta[city.name]
    ModernCityCard(
        city = city, plateCode = meta?.plate, plateColor = meta?.color ?: Color(0xFF37474F),
        emoji = meta?.emoji ?: "🌍", glass = DarkColors.glass, glassBorder = DarkColors.glassBorder,
        textColor = DarkColors.text, textSub = DarkColors.textSub, accent = DarkColors.accent,
        onClick = onClick, modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: CityViewModel,
    onBackClick: () -> Unit,
    onCityClick: (CityDto) -> Unit,
    isDarkTheme: Boolean = true,
    onThemeToggle: () -> Unit = {}
) {
    val favorites by viewModel.favorites.collectAsState()

    val spec = tween<Color>(500, easing = FastOutSlowInEasing)
    val bgTop       by animateColorAsState(if (isDarkTheme) DarkColors.bgTop  else LightColors.bgTop,  spec, "bgTop")
    val bgMid       by animateColorAsState(if (isDarkTheme) DarkColors.bgMid  else LightColors.bgMid,  spec, "bgMid")
    val bgBot       by animateColorAsState(if (isDarkTheme) DarkColors.bgBot  else LightColors.bgBot,  spec, "bgBot")
    val topBarBg    by animateColorAsState(if (isDarkTheme) DarkColors.topBar else LightColors.topBar, spec, "topBar")
    val glass       by animateColorAsState(if (isDarkTheme) DarkColors.glass  else LightColors.glass,  spec, "glass")
    val glassBorder by animateColorAsState(if (isDarkTheme) DarkColors.glassBorder else LightColors.glassBorder, spec, "border")
    val textColor   by animateColorAsState(if (isDarkTheme) DarkColors.text   else LightColors.text,   spec, "text")
    val textSub     by animateColorAsState(if (isDarkTheme) DarkColors.textSub else LightColors.textSub, spec, "sub")
    val accent      by animateColorAsState(if (isDarkTheme) DarkColors.accent  else LightColors.accent, spec, "accent")

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        val favBg = if (isDarkTheme) Color(0x33FF5252) else Color(0xFFFFEBEE)
                        val favBorder = if (isDarkTheme) Color(0x66FF5252) else Color(0xFFFFCDD2)

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(favBg)
                                .border(1.dp, favBorder, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favori",
                                tint = Color(0xFFE53935),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy((-3).dp)) {
                            Text("Favori Şehirler", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor, letterSpacing = (-0.3).sp)
                            Text("Kayıtlı Şehirleriniz", fontSize = 10.sp, color = accent.copy(alpha = 0.8f), letterSpacing = 0.8.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(top = 6.dp, start = 8.dp).size(36.dp)
                            .clip(CircleShape).background(textColor.copy(alpha = 0.08f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri", tint = textColor, modifier = Modifier.size(18.dp))
                    }
                },
                actions = {
                    ThemeToggleSwitch(
                        isDarkTheme = isDarkTheme,
                        onToggle    = onThemeToggle,
                        modifier    = Modifier.padding(top = 6.dp, end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarBg)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(bgTop, bgMid, bgBot)))
                .padding(innerPadding)
        ) {
            if (favorites.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🤍", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Henüz favori şehir eklenmedi", color = textColor.copy(alpha = 0.5f), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(favorites) { city ->
                        val meta = cityMeta[city.name]
                        val isFav = viewModel.isFavorite(city)
                        ModernCityCard(
                            city = city, plateCode = meta?.plate,
                            plateColor = meta?.color ?: Color(0xFF37474F),
                            emoji = meta?.emoji ?: "🌍",
                            glass = glass, glassBorder = glassBorder,
                            textColor = textColor, textSub = textSub, accent = accent,
                            isFavorite = isFav,
                            onFavoriteClick = { viewModel.toggleFavorite(city) },
                            onClick = { onCityClick(city) }
                        )
                    }
                }
            }
        }
    }
}
