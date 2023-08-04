package aid.distaid.pricecount.ui

import aid.distaid.pricecount.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun CategoryDialog(
    open: Boolean,
    defaultText: String,
    textLabel: String,
    dialogLabel: String,
    confirmText: String,
    onConfirm: (String) -> Unit,
    onClose: () -> Unit
) {
    if (open) {
        var text by remember {
            mutableStateOf(defaultText)
        }

        Dialog(
            onDismissRequest = {
                text = ""
                onClose()
            }
        ) {
            Surface(
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (dialogLabel != "") {
                        Text(text = dialogLabel,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                    }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = text,
                        singleLine = true,
                        label = { Text(text = textLabel) },
                        onValueChange = { value -> text = value },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                    Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = {
                            onConfirm(text)
                            text = ""
                            onClose()
                        }) {
                            Text(text = confirmText.uppercase())
                        }
                        TextButton(onClick = {
                            text = ""
                            onClose()
                        }) {
                            Text(text = stringResource(id = R.string.cancel).uppercase())
                        }
                    }
                }
            }
        }
    }
}