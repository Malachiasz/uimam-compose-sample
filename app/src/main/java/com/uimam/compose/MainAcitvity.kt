package com.uimam.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.compose.jetchat.R

class MainAcitvity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init()
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "list") {
                composable("list") { ListScreen(viewModel, navController) }
                composable(
                    "detail?rowId={rowId}",
                    arguments = listOf(navArgument("rowId") { type = NavType.IntType })
                ) {
                    DetailScreen(navController, it.arguments?.getInt("rowId") ?: 0)
                }
            }

        }
    }

    @Composable
    fun ListScreen(viewModel: MainViewModel, navController: NavController) {
        val list: List<Row> by viewModel.list.collectAsState()
        HelloContent(list = list, onRowClicked = {
//            viewModel.deleteRow(it)
            navController.navigate("detail?rowId=${it.id}")
        })
    }

    @Composable
    fun HelloContent(list: List<Row>, onRowClicked: (Row) -> Unit) {
        LazyColumn {
            for (row in list) {
                item { ARow(row, onRowClicked) }
            }
        }
    }

    @Composable
    fun ARow(row: Row, onRowClicked: (Row) -> Unit) {
        Card(
            Modifier
                .shadow(10.dp)
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = row.name)
                Image(
                    painter = painterResource(id = R.drawable.ic_jetchat), contentDescription = "",
                    modifier = Modifier
                        .clickable { onRowClicked(row) }
                        .indication(
                            indication = rememberRipple(),
                            interactionSource = MutableInteractionSource()
                        )
                )
            }
        }
    }

    @Preview(widthDp = 400, heightDp = 600)
    @Composable
    fun HelloContentPreview() {
        HelloContent(
            list = listOf(
                Row(1, "abra", "abra"),
                Row(2, "cadabra", "cadabra"),
                Row(3, "bum", "bum")
            ), {})
    }
}