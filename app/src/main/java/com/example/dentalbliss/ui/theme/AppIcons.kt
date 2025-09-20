package com.example.dentalbliss.ui.theme

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.dentalbliss.R

object AppIcons {
    @Composable
    fun ToothIcon(
        contentDescription: String?,
        modifier: Modifier = Modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_tooth),
            contentDescription = contentDescription,
            modifier = modifier
        )
    }
} 