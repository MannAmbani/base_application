package com.rnd.baseproject.ui.authpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.rnd.baseproject.BaseFragment
import com.rnd.baseproject.database.entity.Note
import com.rnd.baseproject.ui.theme.BaseProjectTheme
import com.rnd.baseproject.ui.widgets.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment:BaseFragment() {
    private val viewModel by viewModels<AuthViewModel>()
    @OptIn(ExperimentalMaterialApi::class)
    val bottomState: ModalBottomSheetState = ModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var title:MutableState<String> = mutableStateOf("")
    var isErrorEnableTitle:MutableState<Boolean> = mutableStateOf(false)
    var titleError: MutableState<String> = mutableStateOf("")
    var description:MutableState<String> = mutableStateOf("")
    var isErrorEnableDescription:MutableState<Boolean> = mutableStateOf(false)
    var descriptionError:MutableState<String> = mutableStateOf("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BaseProjectTheme {

                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Preview
    @Composable
    fun NotesMainContent() {
        val coroutineScope = rememberCoroutineScope()
        ModalBottomSheetLayout(sheetState = bottomState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
            sheetElevation = 5.dp,
            sheetContent = {
                BottomSheetContentInvoice(coroutineScope)
            }) {
        Scaffold(topBar = {
            Toolbar("Notes")
        }, bottomBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                FloatingActionButton(
                    onClick = {

                        coroutineScope.launch {
                            bottomState.show()
                        }


                    }, modifier = Modifier
                        .align(
                            Alignment.BottomEnd
                        )
                        .padding(20.dp)
                ) {

                    Icon(imageVector = Icons.Outlined.Add, contentDescription = "")
                }
            }
        }) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(200.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

            }

        }
    }
    }

    @Composable
    @Preview
    fun noteItem(){
        Card {

        }
    }

    @Composable
    @Preview
    fun BottomSheetContentInvoice(coroutineScope: CoroutineScope = rememberCoroutineScope()) {

        Column(modifier = Modifier.fillMaxWidth()) {


            OutlinedTextField(
                value = title.value,
                onValueChange = { email ->
                    title.value = email
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Soc/Complex Name", fontSize = 12.sp) },
                shape = RoundedCornerShape(20.dp),
                isError = isErrorEnableTitle.value,
                supportingText =
                {
                    if (isErrorEnableTitle.value) {
                        Text(text = titleError.value)
                    }
                })

            OutlinedTextField(value = description.value, onValueChange = { value ->
                description.value = value
                isErrorEnableDescription.value = false
            }, minLines = 4,
                textStyle = TextStyle(fontSize = 12.sp), label = {
                    Text(text = "Remark", fontSize = 12.sp)
                }, isError = isErrorEnableDescription.value, shape = RoundedCornerShape(20.dp),

                singleLine = false, maxLines = 4, modifier = Modifier.fillMaxWidth(), supportingText = {
                    if (isErrorEnableDescription.value) {
                        Text(text = descriptionError.value)
                    }
                })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp, end = 20.dp)
            ) {
                Button(onClick = {
                    noteValidation(coroutineScope)
                }, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text(text = "Add Note")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun noteValidation(coroutineScope: CoroutineScope) {

        when {
            title.value.isNullOrEmpty() -> {
                isErrorEnableTitle.value = true
                titleError.value = "Please enter Title"
            }
            description.value.isNullOrEmpty() -> {
                isErrorEnableDescription.value = true
                descriptionError.value = "Please enter Description"
            }
            else -> {
                coroutineScope.launch {
                    bottomState.hide()
                }

                val note = Note(title.value,description.value)
                viewModel.addNote(note)


            }
        }

    }
}