package com.example.yugioh.ui.screens

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yugioh.R
import com.example.yugioh.model.CardModel

@Composable
fun CreateCardScreen(
    onSubmit: (
        context: Context,
        name: String,
        type: String,
        description: String,
        atk: Int?,
        def: Int?
        ) -> Unit
    ) {
    Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current
        var nameInput by remember { mutableStateOf("") }
        var typeInput by remember { mutableStateOf("") }
        var atkInput by remember { mutableStateOf("") }
        var defInput by remember { mutableStateOf("") }
        var descriptionInput by remember { mutableStateOf("") }

        EditField(
            label = R.string.form_name,
            value = nameInput,
            onValueChange = { nameInput = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions( onNext = { focusManager.moveFocus(FocusDirection.Down) } )
        )

        Spacer(modifier = Modifier.size(4.dp))

        EditField(
            label = R.string.form_type,
            value = typeInput,
            onValueChange = { typeInput = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions( onNext = { focusManager.moveFocus(FocusDirection.Down) } )
        )

        Spacer(modifier = Modifier.size(4.dp))

        if (typeInput.contains("Monster")) {
            EditField(
                label = R.string.form_atk,
                value = atkInput,
                onValueChange = { atkInput = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions( onNext = { focusManager.moveFocus(FocusDirection.Down) } )
            )

            Spacer(modifier = Modifier.size(4.dp))

            EditField(
                label = R.string.form_def,
                value = defInput,
                onValueChange = { defInput = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions( onNext = { focusManager.moveFocus(FocusDirection.Down) } )
            )

            Spacer(modifier = Modifier.size(4.dp))
        }

        EditField(
            label = R.string.form_description,
            value = descriptionInput,
            onValueChange = { descriptionInput = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions( onDone = { focusManager.clearFocus() } ),
            isSingleLine = false,
            maxLines = 5
        )

        Spacer(modifier = Modifier.size(4.dp))

        Button(
            onClick = { onSubmit(
                context,
                nameInput,
                typeInput,
                descriptionInput,
                if (atkInput.isNullOrEmpty()) null else atkInput.toInt(),
                if (defInput.isNullOrEmpty()) null else defInput.toInt()
            ) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Create Card",
                style=MaterialTheme.typography.h2,
            )
        }
    }
}
//
//@Composable
//fun EditDropdown(
//    expanded: Boolean,
//    onDismissRequest: () -> Unit,
//    modifier: Modifier = Modifier,
//    content:
//) {
//
//}

@Composable
fun EditField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChange: (String) -> Unit,
    isSingleLine: Boolean = true,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(label)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = isSingleLine,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}