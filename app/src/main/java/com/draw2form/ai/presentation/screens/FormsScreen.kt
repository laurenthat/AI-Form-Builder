package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.draw2form.ai.api.ApiFormButton
import com.draw2form.ai.api.ApiFormCheckbox
import com.draw2form.ai.api.ApiFormCheckboxResponse
import com.draw2form.ai.api.ApiFormImage
import com.draw2form.ai.api.ApiFormLabel
import com.draw2form.ai.api.ApiFormTextField
import com.draw2form.ai.api.ApiFormTextFieldResponse
import com.draw2form.ai.api.ApiFormToggleSwitch
import com.draw2form.ai.api.ApiFormToggleSwitchResponse
import com.draw2form.ai.models.Resources

data class UIComponent(
    val textField: ApiFormTextField? = null,
    val textFieldResponse: ApiFormTextFieldResponse? = null,
    val label: ApiFormLabel? = null,
    val checkbox: ApiFormCheckbox? = null,
    val checkboxResponse: ApiFormCheckboxResponse? = null,
    val toggleSwitch: ApiFormToggleSwitch? = null,
    val toggleSwitchResponse: ApiFormToggleSwitchResponse? = null,
    val button: ApiFormButton? = null,
    val image: ApiFormImage? = null,
) {
    fun order(): Int {
        return textField?.order
            ?: label?.order
            ?: checkbox?.order
            ?: toggleSwitch?.order
            ?: button?.order
            ?: image?.order
            ?: 0
    }

    fun formId(): String {
        return textField?.formId
            ?: label?.formId
            ?: checkbox?.formId
            ?: toggleSwitch?.formId
            ?: button?.formId
            ?: image?.formId
            ?: ""
    }

    fun id(): String {
        return textField?.id
            ?: label?.id
            ?: checkbox?.id
            ?: toggleSwitch?.id
            ?: button?.id
            ?: image?.id
            ?: ""
    }


    fun updateOrder(order: Int): UIComponent {
        this.textField?.let {
            return this.copy(
                textField = this.textField.copy(order = order)
            )
        }
        this.label?.let {
            return this.copy(
                label = this.label.copy(order = order)
            )
        }
        this.checkbox?.let {
            return this.copy(
                checkbox = this.checkbox.copy(order = order)
            )
        }
        this.toggleSwitch?.let {
            return this.copy(
                toggleSwitch = this.toggleSwitch.copy(order = order)
            )
        }
        this.toggleSwitch?.let {
            return this.copy(
                toggleSwitch = this.toggleSwitch.copy(order = order)
            )
        }
        this.button?.let {
            return this.copy(
                button = this.button.copy(order = order)
            )
        }
        this.image?.let {
            return this.copy(
                image = this.image.copy(order = order)
            )
        }
        throw Exception("Empty UIComponent not allowed")

    }

    fun resourceType(): Resources {
        this.textField?.let {
            return Resources.FormTextField
        }
        this.label?.let {
            return Resources.FormLabel

        }
        this.checkbox?.let {
            return Resources.FormCheckbox

        }
        this.toggleSwitch?.let {
            return Resources.FormToggleSwitch

        }
        this.button?.let {
            return Resources.FormButton

        }
        this.image?.let {
            return Resources.FormImage

        }
        throw Exception("Bad UIComponent Resource not allowed")
    }


}

@Composable
fun Label(label: String) {
    Text(
        text = label,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun FormAsyncImage(
    url: String, modifier: Modifier
) {
    val imageModifier = Modifier
        .fillMaxSize()
    AsyncImage(
        model = url,
        contentDescription = "profile photo",
        modifier = imageModifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun TextField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onChange(it)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun Checkbox(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Checkbox(
            checked = remember { mutableStateOf(true).value },
            onCheckedChange = { /*TODO*/ },
        )
    }

}

@Composable
fun ToggleSwitch(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(
            checked = remember { mutableStateOf(true).value },
            onCheckedChange = { /*TODO*/ },
        )
    }
}

@Composable
fun DynamicFormButton(label: String) {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = label, modifier = Modifier.padding(8.dp))
    }
}
