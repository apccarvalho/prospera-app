package com.apccarvalho.prospera.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R

private val provedorGoogleFonts = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.prospera_fonts_certs,
)

// A fonte do sistema "sans-serif-condensed" é a própria Roboto Condensed e evita
// que a marca apareça em Roboto comum enquanto o download não termina ou se ele falhar.
val RobotoCondensed = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto Condensed"),
        fontProvider = provedorGoogleFonts,
        weight = FontWeight.Bold,
    ),
    Font(
        familyName = DeviceFontFamilyName("sans-serif-condensed"),
        weight = FontWeight.Bold,
    ),
)

val Typography = Typography()

val Typography.marca: TextStyle
    get() = TextStyle(
        fontFamily = RobotoCondensed,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 2.sp,
    )

val Typography.valorDestaque: TextStyle
    get() = displaySmall.copy(fontWeight = FontWeight.Bold)
