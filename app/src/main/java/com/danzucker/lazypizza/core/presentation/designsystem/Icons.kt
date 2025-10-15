package com.danzucker.lazypizza.core.presentation.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.danzucker.lazypizza.R

val DeleteIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.delete)


val PlusIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.plus)

val MinusIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.minus)

val PhoneIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.phone)

val PhoneFilledIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.phone_filled)

val SearchIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.search)

val BackIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.back_arrow)