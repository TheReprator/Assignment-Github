package dev.reprator.github.util.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import github.sharedcode.generated.resources.Res
import github.sharedcode.generated.resources.app_error_generic
import github.sharedcode.generated.resources.app_retry
import github.sharedcode.generated.resources.test_tag_user_screen_loader
import github.sharedcode.generated.resources.user_detail_screen_repo_no
import org.jetbrains.compose.resources.stringResource

@Composable
fun WidgetRetry(
    error: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            onRetry()
        }) {
            Text(text = stringResource(Res.string.app_retry), modifier = Modifier.padding(10.dp))
        }
        Text(text = error ?: stringResource(Res.string.app_error_generic), textAlign = TextAlign.Center,)
    }
}

@Composable
fun WidgetLoader(
    modifier: Modifier = Modifier
) {
    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(Modifier.testTag(stringResource(Res.string.test_tag_user_screen_loader)))
    }
}

@Composable
fun WidgetEmpty(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(message)
    }
}