package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.presentation.ui.theme.LinkUpTheme
import com.draw2form.ai.user.UserViewModel


@Composable
fun FormsListScreen(userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    val userForms = userViewModel.apiUserForms.collectAsState(emptyList())
    val forms = userForms.value

    CategorizedLazyColumn(forms = forms)

    LaunchedEffect(true) {
        userViewModel.getUserForms()
    }
}


@Composable
fun FormItem(form: ApiForm) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = form.name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        /*Text(
            text = "Date: ${form.date}", textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp),
            style = MaterialTheme.typography.bodyLarge
        )*/
        Text(
            text = form.status, textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }

}


@Composable
private fun CategorizedLazyColumn(
    forms: List<ApiForm>,
    modifier: Modifier = Modifier
) {
    val sortedForms = forms.sortedBy { it.name }

    LazyColumn(
        modifier = modifier
    ) {
        items(sortedForms) { form ->
            FormItem(form)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormsListScreenPreview() {
    LinkUpTheme {
        FormsListScreen()
    }
}



