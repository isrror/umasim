package io.github.mee1080.umasim.compose.common.atoms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import io.github.mee1080.utility.filterBySearch

@Composable
fun <T> SelectBox(
    items: List<T>,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    outlined: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    itemToString: (T) -> String = { it.toString() },
    itemToMenuContent: @Composable (T) -> Unit = { Text(itemToString(it)) },
) {
    var expanded by remember { mutableStateOf(false) }
    SelectBox(
        expanded, { expanded = it },
        items, selectedItem, onSelect, modifier, outlined, label, itemToString, itemToMenuContent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectBox(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<T>,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    outlined: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    itemToString: (T) -> String = { it.toString() },
    itemToMenuContent: @Composable (T) -> Unit = { Text(itemToString(it)) },
) {
    ExposedDropdownMenuBox(expanded, onExpandedChange, modifier) {
        if (outlined) {
            OutlinedTextField(
                value = selectedItem?.let(itemToString) ?: "",
                onValueChange = {},
                label = label,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true)
            )
        } else {
            TextField(
                value = selectedItem?.let(itemToString) ?: "",
                onValueChange = {},
                label = label,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true)
            )
        }
        ExposedDropdownMenu(expanded, { onExpandedChange(false) }) {
            items.forEach {
                DropdownMenuItem(
                    text = { itemToMenuContent(it) },
                    onClick = {
                        onSelect(it)
                        onExpandedChange(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchableSelectBox(
    items: List<T>,
    selectedItem: T?,
    onSelect: (T) -> Unit,
    clearItem: T? = null,
    modifier: Modifier = Modifier,
    outlined: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    itemToString: (T) -> String = { it.toString() },
    itemToMenuContent: @Composable (T) -> Unit = { Text(itemToString(it)) },
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(selectedItem?.let(itemToString) ?: "") }
    val clearValue = clearItem

    LaunchedEffect(expanded, selectedItem, clearValue, itemToString) {
        if (!expanded) {
            searchText = selectedItem?.let(itemToString) ?: ""
        } else if (clearValue != null && selectedItem == clearValue && searchText == itemToString(clearValue)) {
            searchText = ""
        }
    }

    val filteredItems = remember(items, searchText, itemToString) {
        items.filterBySearch(searchText, itemToString)
    }

    ExposedDropdownMenuBox(expanded, { expanded = it }, modifier) {
        val fieldModifier = Modifier
            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
            .pointerHoverIcon(PointerIcon.Text, overrideDescendants = true)

        if (outlined) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    if (!expanded) {
                        expanded = true
                    }
                },
                label = label,
                singleLine = true,
                trailingIcon = {
                    Row {
                        if (clearValue != null && (selectedItem != null || searchText.isNotBlank())) {
                            IconButton(
                                onClick = {
                                    searchText = itemToString(clearValue)
                                    onSelect(clearValue)
                                    expanded = true
                                },
                                modifier = Modifier.size(24.dp),
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = fieldModifier,
            )
        } else {
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    if (!expanded) {
                        expanded = true
                    }
                },
                label = label,
                singleLine = true,
                trailingIcon = {
                    Row {
                        if (clearValue != null && (selectedItem != null || searchText.isNotBlank())) {
                            IconButton(
                                onClick = {
                                    searchText = itemToString(clearValue)
                                    onSelect(clearValue)
                                    expanded = true
                                },
                                modifier = Modifier.size(24.dp),
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = fieldModifier,
            )
        }
        ExposedDropdownMenu(expanded, { expanded = false }) {
            if (filteredItems.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("无结果") },
                    onClick = {},
                    enabled = false,
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            } else {
                filteredItems.forEach {
                    DropdownMenuItem(
                        text = { itemToMenuContent(it) },
                        onClick = {
                            onSelect(it)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}
