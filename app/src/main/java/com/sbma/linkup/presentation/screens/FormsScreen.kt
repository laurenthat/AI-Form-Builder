package com.sbma.linkup.presentation.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import org.json.JSONArray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinkUpTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val json = """
//                        {
//                            "predictions":[
//                                {
//                                    "type":"FormLabel",
//                                    "class":"label",
//                                    "order":1,
//                                    "label":"Name"
//                                },
//                                {
//                                    "type":"FormImage",
//                                    "class":"image",
//                                    "order":2
//                                },
//                                {
//                                    "type":"FormTextfield",
//                                    "class":"input",
//                                    "order":3,
//                                    "label":"Address"
//                                },
//                                {
//                                    "type":"FormLabel",
//                                    "class":"label",
//                                    "order":4,
//                                    "label":"City"
//                                },
//                                {
//                                    "type":"FormLabel",
//                                    "class":"label",
//                                    "order":5,
//                                    "label":"Zip Code"
//                                },
//                                {
//                                    "type":"FormTextfield",
//                                    "class":"input",
//                                    "order":6
//                                },
//                                {
//                                    "type":"FormLabel",
//                                    "class":"label",
//                                    "order":7,
//                                    "label":"Email"
//                                },
//                                {
//                                    "type":"FormCheckbox",
//                                    "class":"checkbox",
//                                    "order":8
//                                },
//                                {
//                                    "type":"FormToggleSwitch",
//                                    "class":"radio",
//                                    "order":9
//                                },
//                                {
//                                    "type":"FormLabel",
//                                    "class":"label",
//                                    "order":10,
//                                    "label":"Date of Birth"
//                                },
//                                {
//                                    "type":"FormLabel",
//                                    "class":"label",
//                                    "order":11,
//                                    "label":"Gender"
//                                },
//                                {
//                                    "type":"FormTextfield",
//                                    "class":"input",
//                                    "order":12
//                                },
//                                {
//                                    "type":"FormLabel",
//                                    "class":"label",
//                                    "order":13,
//                                    "label":"Agree to Terms"
//                                },
//                                {
//                                    "type":"FormToggleSwitch",
//                                    "class":"toggle",
//                                    "order":14
//                                },
//                                {
//                                    "type":"FormLabel",
//                                    "class":"label",
//                                    "order":15,
//                                    "label":"Submit"
//                                },
//                                {
//                                    "type":"FormButton",
//                                    "class":"button",
//                                    "order":16
//                                }
//                            ]
//                        }
//                    """
                    val jsonString = """[
                [
                    "FormImage",
                    {
                        "id": "",
                        "formId": "",
                        "order": 1,
                        "imageId": ""
                    },
                    {
                        "type": "FormImage",
                        "class": "image",
                        "order": 1
                    }
                ],
                [
                    "FormButton",
                    {
                        "id": "",
                        "formId": "",
                        "type": "submit",
                        "order": 2,
                        "label": "SAVE"
                    },
                    {
                        "type": "FormButton",
                        "class": "button",
                        "order": 2,
                        "label": "SAVE"
                    }
                ],
                [
                    "FormLabel",
                    {
                        "id": "",
                        "formId": "",
                        "order": 3,
                        "label": "TURN OFF"
                    },
                    {
                        "type": "FormLabel",
                        "class": "label",
                        "order": 3,
                        "label": "TURN OFF"
                    }
                ],
                [
                    "FormToggleSwitch",
                    {
                        "id": "",
                        "formId": "",
                        "order": 4
                    },
                    {
                        "type": "FormToggleSwitch",
                        "class": "toggle",
                        "order": 4
                    }
                ]
            ]"""

                    val jsonArray = JSONArray(jsonString)
                    val uiElements = parseJson(jsonArray)
                    DynamicUI(uiElements)
                }

            }
        }
    }

    //    private fun parseJson(jsonString: String): List<UIElement> {
//        val json = JSONObject(jsonString)
//        val predictions = json.getJSONArray("predictions")
//        val uiElements = mutableListOf<UIElement>()
//
//        for (i in 0 until predictions.length()) {
//            val prediction = predictions.getJSONObject(i)
//            val type = prediction.getString("type")
//            val order = prediction.getInt("order")
//            val label = prediction.optString("label", "")
//
//            val uiElement = when (type) {
//                "FormLabel" -> FormLabel(order, label)
//                "FormImage" -> FormImage(order)
//                "FormTextfield" -> FormTextfield(order, label)
//                "FormCheckbox" -> FormCheckbox(order)
//                "FormToggleSwitch" -> FormToggleSwitch(order)
//                "FormButton" -> FormButton(order)
//                else -> throw IllegalArgumentException("Unknown type: $type")
//            }
//
//            uiElements.add(uiElement)
//        }
//
//        return uiElements.sortedBy { it.order }
//    }

}

@Composable
fun parseJson(jsonArray: JSONArray): List<UIElement> {
    val uiElements = mutableListOf<UIElement>()

    for (i in 0 until jsonArray.length()) {
        val jsonElementArray = jsonArray.getJSONArray(i)
        val type = jsonElementArray.getString(0)
        val elementJson = jsonElementArray.getJSONObject(1)
        val order = elementJson.getInt("order")
        val label = elementJson.optString("label", "")

        val uiElement = when (type) {
            "FormLabel" -> FormLabel(order, label)
            "FormImage" -> FormImage(order)
            "FormTextfield" -> FormTextfield(order, label)
            "FormCheckbox" -> FormCheckbox(order)
            "FormToggleSwitch" -> FormToggleSwitch(order)
            "FormButton" -> FormButton(order, label)
            else -> throw IllegalArgumentException("Unknown type: $type")
        }

        uiElements.add(uiElement)
    }
    return uiElements.sortedBy { it.order }
}


data class FormLabel(override val order: Int, val label: String) : UIElement
data class FormImage(override val order: Int) : UIElement
data class FormTextfield(override val order: Int, val label: String) : UIElement
data class FormCheckbox(override val order: Int) : UIElement
data class FormToggleSwitch(override val order: Int) : UIElement
data class FormButton(override val order: Int, val label: String) : UIElement

interface UIElement {
    val order: Int
}

@Composable
fun DynamicUI(elements: List<UIElement>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        for (element in elements) {
            when (element) {
                is FormLabel -> Label(element.label)
                is FormImage -> FormAsyncImage(
                    url = "https://placekitten.com/400/300",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(shape = MaterialTheme.shapes.medium),
                )

                is FormTextfield -> Textfield(element.label)
                is FormCheckbox -> Checkbox()
                is FormToggleSwitch -> ToggleSwitch()
                is FormButton -> DynamicFormButton(element.label)
            }
        }
    }
}

@Composable
fun Label(label: String) {
    Text(text = label, modifier = Modifier.padding(8.dp))
}

@Composable
fun FormAsyncImage(url: String, modifier: Modifier) {
    AsyncImage(
        model = url,
        contentDescription = "profile photo",
        modifier = Modifier,
//            .size(120.dp),
        contentScale = ContentScale.Crop
//            .clip(RoundedCornerShape(50.dp))
    )
}

@Composable
fun Textfield(label: String) {
    OutlinedTextField(
        value = remember { mutableStateOf("") }.value,
        onValueChange = { /*TODO*/ },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun Checkbox() {
    Checkbox(
        checked = remember { mutableStateOf(true).value },
        onCheckedChange = { /*TODO*/ },
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun ToggleSwitch() {
    Switch(
        checked = remember { mutableStateOf(true).value },
        onCheckedChange = { /*TODO*/ },
        modifier = Modifier.padding(8.dp)
    )
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

@Preview(showBackground = true)
//@Composable
//fun PreviewDynamicUI() {
//    val sampleElements = listOf(
//        FormLabel(1, "Name"),
//        FormImage(2),
//        FormTextfield(3, "Address"),
//        FormLabel(4, "City"),
//        FormLabel(5, "Zip Code"),
//        FormTextfield(6, "Email"),
//        FormCheckbox(7),
//        FormToggleSwitch(8),
//        FormLabel(9, "Date of Birth"),
//        FormLabel(10, "Gender"),
//        FormTextfield(11, ""),
//        FormLabel(12, "Agree to Terms"),
//        FormToggleSwitch(13),
//        FormLabel(14, "Submit"),
//        FormButton(15)
//    )
//    DynamicUI(sampleElements)
//}

@Composable
fun PreviewDynamicUI() {
    val jsonString = """[
                [
                    "FormImage",
                    {
                        "id": "",
                        "formId": "",
                        "order": 1,
                        "imageId": ""
                    },
                    {
                        "type": "FormImage",
                        "class": "image",
                        "order": 1
                    }
                ],
                [
                    "FormButton",
                    {
                        "id": "",
                        "formId": "",
                        "type": "submit",
                        "order": 2,
                        "label": "SAVE"
                    },
                    {
                        "type": "FormButton",
                        "class": "button",
                        "order": 2,
                        "label": "SAVE"
                    }
                ],
                [
                    "FormLabel",
                    {
                        "id": "",
                        "formId": "",
                        "order": 3,
                        "value": "TURN OFF"
                    },
                    {
                        "type": "FormLabel",
                        "class": "label",
                        "order": 3,
                        "label": "TURN OFF"
                    }
                ],
                [
                    "FormToggleSwitch",
                    {
                        "id": "",
                        "formId": "",
                        "order": 4
                    },
                    {
                        "type": "FormToggleSwitch",
                        "class": "toggle",
                        "order": 4
                    }
                ]
            ]"""

    val jsonArray = JSONArray(jsonString)
    val uiElements = parseJson(jsonArray)
    DynamicUI(uiElements)
}
