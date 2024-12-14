package com.example.mamduhtaskmanager.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mamduhtaskmanager.R
import com.example.mamduhtaskmanager.ui.theme.primaryColor
import com.example.mamduhtaskmanager.ui.theme.secondaryColor


@Composable
fun MyDrawer(modifier: Modifier = Modifier) {
    Surface(
        modifier
            .fillMaxHeight(),
        shadowElevation = 50.dp
    ) {
        Column (verticalArrangement = Arrangement.Top){
            DrawerTitle()

            LazyColumn {
                item(){
                    DrawerItem("Tasks") {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = modifier.padding(8.dp)
                        )
                    }
                }
                item() {
                    DrawerItem("Habits") {
                        Icon(
                            Icons.Default.Face,
                            contentDescription = null,
                            modifier = modifier.padding(8.dp)
                        )
                    }
                }
                item() {
                    DrawerItem("Lists") {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = null,
                            modifier = modifier.padding(8.dp)
                        )
                    }
                }
                item() {
                    DrawerItem("Recipies") {
                        Icon(
                            painterResource(R.drawable.restaurant_20px),
                            contentDescription = null,
                            modifier = modifier.padding(8.dp)
                        )
                    }
                }
                item() {
                    DrawerItem("Journaling") {
                        Icon(
                            painterResource(R.drawable.library_add_24px),
                            contentDescription = null,
                            modifier = modifier.padding(8.dp)
                        )
                    }
                }

                item() {
                    Divider(
                        Modifier
                            .width(300.dp)
                            .padding(vertical = 12.dp))

                    DrawerItem("Add a tag") {
                        Icon(
                            painterResource(R.drawable.sell_20px),
                            contentDescription = null,
                            modifier = modifier.padding(8.dp)
                        )
                    }
                }

                item() {
                    DrawerItem("Trash") {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            modifier = modifier.padding(8.dp)
                        )
                    }
                }


                item() {

                    DrawerItem("Archive") {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            modifier = modifier.padding(8.dp)
                        )
                    }

                }
                item() {
                    Divider(
                        Modifier
                            .width(300.dp)
                            .padding(vertical = 12.dp))

                    DrawerItem(
                        type = "Setting",
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = null,
                            modifier = modifier.padding(8.dp)
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun DrawerTitle(modifier: Modifier = Modifier.padding(16.dp)) {
    val gradient = Brush.linearGradient(
        listOf(primaryColor,
            secondaryColor)
    )
    Surface(
        modifier = Modifier.width(300.dp),
        shadowElevation = 10.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(gradient)
        ) {

            Text(
                stringResource(R.string.mamduh_room),
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier,
                color = Color.White
            )
        }
    }
}

@Composable
fun DrawerItem(
    type: String,
    modifier: Modifier = Modifier.padding(4.dp),
    icon:@Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        icon()

        Text(
            type,
            modifier = modifier
        )
    }
}
@Preview
@Composable
private fun MyDrawerPreview() {
    MyDrawer()
}
