package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.draw2form.ai.api.ApiFormButton
import com.draw2form.ai.api.ApiFormCheckbox
import com.draw2form.ai.api.ApiFormImage
import com.draw2form.ai.api.ApiFormLabel
import com.draw2form.ai.api.ApiFormTextField
import com.draw2form.ai.api.ApiFormToggleSwitch
import org.json.JSONArray

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            LinkUpTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val jsonString = """[
//                [
//                    "FormImage",
//                    {
//                        "id": "",
//                        "formId": "",
//                        "order": 1,
//                        "imageId": ""
//                    },
//                    {
//                        "type": "FormImage",
//                        "class": "image",
//                        "order": 1,
//                        "coordinates": [
//                            530,
//                            330,
//                            960,
//                            580
//                        ]
//                    }
//                ],
//                [
//                    "FormTextField",
//                    {
//                        "id": "",
//                        "formId": "",
//                        "order": 2
//                    },
//                    {
//                        "type": "FormTextField",
//                        "class": "input",
//                        "order": 2,
//                        "coordinates": [
//                            590,
//                            650,
//                            970,
//                            760
//                        ]
//                    }
//                ],
//                [
//                    "FormTextField",
//                    {
//                        "id": "",
//                        "formId": "",
//                        "order": 3
//                    },
//                    {
//                        "type": "FormTextField",
//                        "class": "input",
//                        "order": 3,
//                        "coordinates": [
//                            600,
//                            810,
//                            990,
//                            920
//                        ]
//                    }
//                ],
//                [
//                    "FormCheckbox",
//                    {
//                        "id": "",
//                        "formId": "",
//                        "order": 4,
//                        "label": "I AGREE"
//                    },
//                    {
//                        "type": "FormCheckbox",
//                        "class": "checkbox",
//                        "order": 4,
//                        "coordinates": [
//                            660,
//                            970,
//                            760,
//                            1090
//                        ],
//                        "label": "I AGREE"
//                    }
//                ],
//                [
//                    "FormLabel",
//                    {
//                        "id": "",
//                        "formId": "",
//                        "order": 5,
//                        "label": "COMPANY"
//                    },
//                    {
//                        "type": "FormLabel",
//                        "class": "label",
//                        "order": 5,
//                        "label": "COMPANY",
//                        "coordinates": [
//                            230,
//                            670,
//                            510,
//                            740
//                        ]
//                    }
//                ],
//                [
//                    "FormLabel",
//                    {
//                        "id": "",
//                        "formId": "",
//                        "order": 6,
//                        "label": "ADDRESS"
//                    },
//                    {
//                        "type": "FormLabel",
//                        "class": "label",
//                        "order": 6,
//                        "label": "ADDRESS",
//                        "coordinates": [
//                            220,
//                            850,
//                            460,
//                            930
//                        ]
//                    }
//                ],
//                [
//                    "FormLabel",
//                    {
//                        "id": "",
//                        "formId": "",
//                        "order": 7,
//                        "label": "JOB FUNCTION"
//                    },
//                    {
//                        "type": "FormLabel",
//                        "class": "label",
//                        "order": 7,
//                        "label": "JOB FUNCTION",
//                        "coordinates": [
//                            180,
//                            1200,
//                            650,
//                            1290
//                        ]
//                    }
//                ],
//                [
//                    "FormLabel",
//                    {
//                        "id": "",
//                        "formId": "",
//                        "order": 8,
//                        "label": "1 EMPLOYEE"
//                    },
//                    {
//                        "type": "FormLabel",
//                        "class": "label",
//                        "order": 8,
//                        "label": "1 EMPLOYEE",
//                        "coordinates": [
//                            770,
//                            1290,
//                            1220,
//                            1400
//                        ]
//                    }
//                ],
//                [
//                    "FormToggleSwitch",
//                    {
//                        "id": "",
//                        "formId": "",
//                        "order": 9,
//                        "label": "ON / OFF"
//                    },
//                    {
//                        "type": "FormToggleSwitch",
//                        "class": "toggle",
//                        "order": 9,
//                        "coordinates": [
//                            730,
//                            1490,
//                            880,
//                            1580
//                        ],
//                        "label": "ON / OFF"
//                    }
//                ],
//                [
//                    "FormButton",
//                    {
//                        "id": "",
//                        "formId": "",
//                        "type": "submit",
//                        "order": 10,
//                        "label": "SEND"
//                    },
//                    {
//                        "type": "FormButton",
//                        "class": "button",
//                        "order": 10,
//                        "coordinates": [
//                            550,
//                            1680,
//                            850,
//                            1850
//                        ],
//                        "label": "SEND"
//                    }
//                ]
//            ]"""
//
//                    val jsonArray = JSONArray(jsonString)
//                    val uiElements = parseJson(jsonArray)
////                    DynamicUI(uiElements)
//                }
//
//            }
//        }
//    }
//}

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
            "FormLabel" -> ApiFormLabel("", null, "", order, label)
            "FormImage" -> ApiFormImage("", null, "", order, "")
            "FormTextField" -> ApiFormTextField("", null, "", label, order, null)
            "FormCheckbox" -> ApiFormCheckbox("", label, null, "", order, null)
            "FormToggleSwitch" -> ApiFormToggleSwitch("", label, null, "", order, null)
            "FormButton" -> ApiFormButton("", label, null, "", order, "")
            else -> throw IllegalArgumentException("Unknown type: $type")
        }

        uiElements.add(uiElement)
    }
    return uiElements.sortedBy { it.order }
}

interface UIElement {
    val order: Int
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
fun TextField(label: String) {
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
fun Checkbox(label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(label)
        Checkbox(
            checked = remember { mutableStateOf(true).value },
            onCheckedChange = { /*TODO*/ },
            modifier = Modifier.padding(8.dp)
        )
    }

}

@Composable
fun ToggleSwitch(label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(label)
        Switch(
            checked = remember { mutableStateOf(true).value },
            onCheckedChange = { /*TODO*/ },
            modifier = Modifier.padding(8.dp)
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

@Preview(showBackground = true)
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
                        "order": 1,
                        "coordinates": [
                            530,
                            330,
                            960,
                            580
                        ]
                    }
                ],
                [
                    "FormTextField",
                    {
                        "id": "",
                        "formId": "",
                        "order": 2
                    },
                    {
                        "type": "FormTextField",
                        "class": "input",
                        "order": 2,
                        "coordinates": [
                            590,
                            650,
                            970,
                            760
                        ]
                    }
                ],
                [
                    "FormTextField",
                    {
                        "id": "",
                        "formId": "",
                        "order": 3
                    },
                    {
                        "type": "FormTextField",
                        "class": "input",
                        "order": 3,
                        "coordinates": [
                            600,
                            810,
                            990,
                            920
                        ]
                    }
                ],
                [
                    "FormCheckbox",
                    {
                        "id": "",
                        "formId": "",
                        "order": 4,
                        "label": "I AGREE"
                    },
                    {
                        "type": "FormCheckbox",
                        "class": "checkbox",
                        "order": 4,
                        "coordinates": [
                            660,
                            970,
                            760,
                            1090
                        ],
                        "label": "I AGREE"
                    }
                ],
                [
                    "FormLabel",
                    {
                        "id": "",
                        "formId": "",
                        "order": 5,
                        "label": "COMPANY"
                    },
                    {
                        "type": "FormLabel",
                        "class": "label",
                        "order": 5,
                        "label": "COMPANY",
                        "coordinates": [
                            230,
                            670,
                            510,
                            740
                        ]
                    }
                ],
                [
                    "FormLabel",
                    {
                        "id": "",
                        "formId": "",
                        "order": 6,
                        "label": "ADDRESS"
                    },
                    {
                        "type": "FormLabel",
                        "class": "label",
                        "order": 6,
                        "label": "ADDRESS",
                        "coordinates": [
                            220,
                            850,
                            460,
                            930
                        ]
                    }
                ],
                [
                    "FormLabel",
                    {
                        "id": "",
                        "formId": "",
                        "order": 7,
                        "label": "JOB FUNCTION"
                    },
                    {
                        "type": "FormLabel",
                        "class": "label",
                        "order": 7,
                        "label": "JOB FUNCTION",
                        "coordinates": [
                            180,
                            1200,
                            650,
                            1290
                        ]
                    }
                ],
                [
                    "FormLabel",
                    {
                        "id": "",
                        "formId": "",
                        "order": 8,
                        "label": "1 EMPLOYEE"
                    },
                    {
                        "type": "FormLabel",
                        "class": "label",
                        "order": 8,
                        "label": "1 EMPLOYEE",
                        "coordinates": [
                            770,
                            1290,
                            1220,
                            1400
                        ]
                    }
                ],
                [
                    "FormToggleSwitch",
                    {
                        "id": "",
                        "formId": "",
                        "order": 9,
                        "label": "ON / OFF"
                    },
                    {
                        "type": "FormToggleSwitch",
                        "class": "toggle",
                        "order": 9,
                        "coordinates": [
                            730,
                            1490,
                            880,
                            1580
                        ],
                        "label": "ON / OFF"
                    }
                ],
                [
                    "FormButton",
                    {
                        "id": "",
                        "formId": "",
                        "type": "submit",
                        "order": 10,
                        "label": "SEND"
                    },
                    {
                        "type": "FormButton",
                        "class": "button",
                        "order": 10,
                        "coordinates": [
                            550,
                            1680,
                            850,
                            1850
                        ],
                        "label": "SEND"
                    }
                ]
            ]"""

    val jsonArray = JSONArray(jsonString)
    val uiElements = parseJson(jsonArray)
//    DynamicUI(uiElements)
}
