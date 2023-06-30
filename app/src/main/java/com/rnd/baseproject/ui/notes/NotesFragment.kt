package com.rnd.baseproject.ui.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.rnd.baseproject.BaseFragment
import com.rnd.baseproject.database.entity.Note
import com.rnd.baseproject.tools.ShowSelectionDialog
import com.rnd.baseproject.tools.toast
import com.rnd.baseproject.ui.authpage.AuthViewModel
import com.rnd.baseproject.ui.theme.BaseProjectTheme
import com.rnd.baseproject.ui.widgets.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : BaseFragment() {
    private val viewModel by viewModels<AuthViewModel>()

    @OptIn(ExperimentalMaterialApi::class)
    val bottomState: ModalBottomSheetState = ModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var title: MutableState<String> = mutableStateOf("")
    var isErrorEnableTitle: MutableState<Boolean> = mutableStateOf(false)
    var titleError: MutableState<String> = mutableStateOf("")
    var description: MutableState<String> = mutableStateOf("")
    var isErrorEnableDescription: MutableState<Boolean> = mutableStateOf(false)
    var descriptionError: MutableState<String> = mutableStateOf("")

    private var noteList: MutableState<ArrayList<Note>> = mutableStateOf(ArrayList())
    private var showSelectionDialog: MutableState<Boolean> = mutableStateOf(false)
    private lateinit var note: Note
    private var isUpdate: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getNotes().observe(viewLifecycleOwner) {
            noteList.value = it as ArrayList<Note>
        }

        return ComposeView(requireContext()).apply {
            setContent {
                BaseProjectTheme {
                    NotesMainContent()
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
                            Log.d("TAG", noteList.value.toString())
                            coroutineScope.launch {
                                isUpdate = false
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    items(noteList.value) { note ->
                        NoteItem(note)
                    }
                    item {
                        if (showSelectionDialog.value) {
                            ShowSelectionDialog(
                                neutralButtonText = "Cancel",
                                title = "Select one",
                                arrayList = arrayListOf("Delete", "Update"),
                                onNeutralButtonClick = {
                                    showSelectionDialog.value = false
                                },
                                onItemClick = { index: Int, s: String ->

                                    when (s) {
                                        "Delete" -> {
                                            toast("Delete ${note.title}")
                                            viewModel.deleteNote(note)
                                            viewModel.getNotes().observe(viewLifecycleOwner) {
                                                noteList.value = it as ArrayList<Note>
                                            }
                                        }

                                        "Update" -> {

                                            coroutineScope.launch {
                                                isUpdate = true
                                                title.value = note.title
                                                description.value = note.note
                                                bottomState.show()

                                            }

                                        }

                                    }

                                    showSelectionDialog.value = false
                                }
                            )
                        }
                    }
                }

            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @Preview
    fun NoteItem(item: Note? = null) {
        Card(modifier = Modifier
            .size(150.dp)
            .padding(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            onClick = {
                note = item!!
                showSelectionDialog.value = true
            }) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item?.title!!,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp), thickness = 2.dp, color = Color.Black
                )
                Text(
                    text = item.note,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Justify
                )


            }
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
                singleLine = true,
                label = { Text(text = "Soc/Complex Name", fontSize = 12.sp) },
                shape = RoundedCornerShape(20.dp),
                isError = isErrorEnableTitle.value,
                supportingText =
                {
                    if (isErrorEnableTitle.value) {
                        Text(text = titleError.value)
                    }
                })

            OutlinedTextField(value = description.value,
                onValueChange = { value ->
                    description.value = value
                    isErrorEnableDescription.value = false
                },
                minLines = 4,
                textStyle = TextStyle(fontSize = 12.sp),
                label = {
                    Text(text = "Remark", fontSize = 12.sp)
                },
                isError = isErrorEnableDescription.value,
                shape = RoundedCornerShape(20.dp),

                singleLine = false,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
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
//                    toast(noteList.value[0].title)
                    Log.d("TAG", noteList.value.toString())
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


                if (isUpdate) {
                    val note1 = Note(title = title.value, note = description.value)
                    note1.id = note.id
                    viewModel.updateNote(note1)
                } else {
                    val note = Note(title = title.value, note = description.value)
                    viewModel.addNote(note)
                }
                viewModel.getNotes().observe(viewLifecycleOwner) {
                    noteList.value = it as ArrayList<Note>
                }

                title.value = ""
                description.value = ""


            }
        }

    }
}