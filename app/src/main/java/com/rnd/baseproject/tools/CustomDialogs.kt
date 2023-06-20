package com.rnd.baseproject.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


var showLoadingDialog: MutableState<Boolean> = mutableStateOf(false)
var showAlertDialog: MutableState<Boolean> = mutableStateOf(false)

fun cancelDialog() {
    showAlertDialog.value = false
    showLoadingDialog.value = false
}

fun showAlertDialog() {
    showAlertDialog.value = true

}

fun isAlertDialogVisible(): Boolean {
    return showAlertDialog.value
}

@Composable
fun ShowLoadingDialog() {
    if (showLoadingDialog.value) {
        Dialog(
            onDismissRequest = { showLoadingDialog.value = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .width(350.dp)
                    .padding()
                    .padding(10.dp)
                    .background(
                        MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 0.dp)
                        .padding(10.dp),

                    )
                Text(
                    text = "Please Wait",
                    modifier = Modifier.padding(start = 20.dp, end = 10.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun ShowAlertDialog(
    title: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onclick: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(onDismissRequest = { showAlertDialog.value = false }, title = {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }, text = content, confirmButton = {
        Button(
            onClick = onclick
        ) {
            Text(text = positiveButtonText)
        }
    }, dismissButton = {
        ElevatedButton(onClick = {
            showAlertDialog.value = false
        }) {
            Text(text = negativeButtonText)
        }
    })
}

@Composable
fun ShowAlertDialog(
    neutralButtonText: String, title: String, onclick: () -> Unit, content: @Composable () -> Unit

) {
    AlertDialog(onDismissRequest = { showAlertDialog.value = false }, title = {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }, text = content, confirmButton = {
        TextButton(onClick = {
            onclick()
            showAlertDialog.value = false
        }

        ) {
            Text(text = neutralButtonText)
        }
    }, dismissButton = {})
}

@Composable
fun ShowAlertDialog(
    title: String,
    positiveButtonText: String,
    negativeButtonText: String,
    text: String,
    negativeClick: () -> Unit,
    onclick: () -> Unit
) {
    AlertDialog(onDismissRequest = { showAlertDialog.value = false }, title = {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }, text = { Text(text = text, fontSize = 16.sp) }, confirmButton = {
        Button(onClick = {
            onclick()
            showAlertDialog.value = false
        }) {
            Text(text = positiveButtonText)
        }
    }, dismissButton = {
        ElevatedButton(onClick = {
            negativeClick()
            showAlertDialog.value = false
        }) {
            Text(text = negativeButtonText)
        }
    })
}

@Composable
fun ShowSelectionDialog(
    neutralButtonText: String,
    title: String,
    arrayList: ArrayList<String>,
    onNeutralButtonClick: () -> Unit,
    onItemClick: (index: Int, s: String) -> Unit
) {

    AlertDialog(

        onDismissRequest = { showAlertDialog.value = false },
        title = {
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        },
        text = { ShowArrayList(arrayList, onItemClick) },
        confirmButton = {
            TextButton(onClick = {
                onNeutralButtonClick()
                showAlertDialog.value = false
            }

            ) {
                Text(text = neutralButtonText)
            }
        },
        dismissButton = {})

}

@Composable
fun ShowArrayList(arrayList: ArrayList<String>, onItemClick: (index: Int, s: String) -> Unit) {
    LazyColumn {
        itemsIndexed(arrayList) { index, s ->
            Text(text = s, modifier = Modifier
                .padding(10.dp)
                .clickable {
                    onItemClick(index, s)
                }
                .fillMaxWidth(), fontSize = 16.sp)
        }
    }
}





