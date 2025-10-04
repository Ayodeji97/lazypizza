package com.danzucker.lazypizza.core.presentation.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.lazypizza.R
import com.danzucker.lazypizza.core.presentation.designsystem.SearchIcon
import com.danzucker.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.cornerRadiusExtraLarge24
import com.danzucker.lazypizza.core.presentation.designsystem.values.Dimens.cornerRadiusLarge16

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = cornerRadiusLarge16)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(cornerRadiusExtraLarge24),
                spotColor = Color(0x0A03131F),
                ambientColor = Color(0x0A03131F)
            )
            .onFocusChanged {
                isFocused = it.isFocused
            },
        maxLines = 1,
        singleLine = true,
        shape = RoundedCornerShape(cornerRadiusExtraLarge24),
        placeholder = {
            if (!isFocused && searchText.isEmpty()) {
                Text(
                    text = stringResource(R.string.search_placeholder),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            }
        },
        leadingIcon = {
            Icon(
                imageVector = SearchIcon,
                contentDescription = stringResource(R.string.search_icon),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

@Preview
@Composable
private fun SearchBarPreview() {
    LazyPizzaTheme {
        SearchBar(
            searchText = stringResource(R.string.search_placeholder),
            onSearchTextChange = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}