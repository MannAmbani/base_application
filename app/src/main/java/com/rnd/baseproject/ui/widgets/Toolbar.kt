package com.rnd.baseproject.ui.widgets

import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.findNavController
import com.rnd.baseproject.R

@Preview
@Composable
fun Toolbar(
    title: String = "Dashboard",
    context: Context = LocalContext.current,
    view: View? = LocalView.current,
    showRightIcon: Boolean = false,
    rightImageVector: ImageVector = Icons.Filled.MoreVert,
    rightIconOnClick: ((position: Int) -> Unit)? = null,
    arrayList: ArrayList<MenuItemsList> = ArrayList(),
    showRightRow: Boolean = false,
    rightRowContent: @Composable () -> Unit = { }
) {
    var showMenu by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {
            view?.findNavController()?.navigateUp()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )

        }
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (showRightIcon) {

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {


                    IconButton(

                        onClick = {

//                        rightIconOnClick?.let { it() }
                            showMenu = !showMenu
                        },
                    ) {
                        Icon(
                            imageVector = rightImageVector,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurface
                        )

                    }

                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        arrayList.forEachIndexed { index, it ->
                            DropdownMenuItem(text = { Text(text = it.name) }, onClick = {
                                showMenu = false
                                //it.onClick()
                                rightIconOnClick?.invoke(index)

                            })
                        }

                    }
                }
            }
        }

        if (showRightRow) {
            rightRowContent()
        }
    }
}
data class MenuItemsList(
    var name: String,
    var onClick: () -> Unit
)