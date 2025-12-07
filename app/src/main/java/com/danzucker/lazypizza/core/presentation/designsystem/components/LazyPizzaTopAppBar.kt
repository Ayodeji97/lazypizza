@file:OptIn(ExperimentalMaterial3Api::class)

package com.danzucker.lazypizza.core.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.PhoneFilledIcon
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.sizeExtraSmall4
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.sizeMedium16
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.sizeSmallMedium12

@Composable
fun LazyPizzaTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    customerPhoneNumber: String? = null,
    onCustomerPhoneNumberClick: () -> Unit = {},
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: (@Composable () -> Unit)? = null,
    endContent: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                startContent?.invoke()
                Spacer(modifier = Modifier.width(sizeExtraSmall4))
                if (!title.isNullOrEmpty()) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = titleColor,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (!customerPhoneNumber.isNullOrEmpty()) {
                    IconButton(
                        onClick = onCustomerPhoneNumberClick,
                        modifier = Modifier.size(sizeSmallMedium12)
                    ) {
                        Icon(
                            imageVector = PhoneFilledIcon,
                            contentDescription = stringResource(R.string.phone_icon_description),
                            tint = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                    Text(
                        text = customerPhoneNumber,
                        style = MaterialTheme.typography.bodyLarge,
                        color = titleColor,
                        modifier = Modifier
                            .clickable(
                                onClick = onCustomerPhoneNumberClick
                            )
                    )
                    Spacer(modifier = Modifier.width(sizeExtraSmall4))
                }
                endContent?.invoke()
                Spacer(modifier = Modifier.width(sizeSmallMedium12))
            }
        },
        modifier = modifier,
        navigationIcon = {
            navigationIcon?.invoke()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        scrollBehavior = scrollBehavior
    )
}

@Preview
@Composable
private fun LazyPizzaTopAppBarPreview() {
    LazyPizzaTheme {
        LazyPizzaTopAppBar(
            startContent = {
                Image(
                    painter = painterResource(R.drawable.lazy_pizza_logo),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            title = stringResource(R.string.lazy_pizza),
            customerPhoneNumber = "123-456-7890",
            onCustomerPhoneNumberClick = { }
        )
    }
}