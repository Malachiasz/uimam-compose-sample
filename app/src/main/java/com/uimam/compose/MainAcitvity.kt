package com.uimam.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.google.accompanist.glide.rememberGlidePainter

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
    fun HelloContent(list: List<Row>, onRowClicked: (Row) -> Unit, onButtonClicked: () -> Unit = {}) {
        var enabled by remember { mutableStateOf(false) }
        val alpha: Float by animateFloatAsState(if (enabled) 1f else 0f, animationSpec = TweenSpec(3000))
        val size by animateDpAsState(if (enabled) 200.dp else 100.dp, animationSpec = TweenSpec(3000))

        Column() {
            LazyColumn(modifier = Modifier.height(100.dp)) {
                for (row in list) {
                    item { ARow(row, onRowClicked) }
                }
            }
            Surface(
                modifier = Modifier
                    .alpha(alpha)
                    .size(size)
            ) {
                Button(onClick = { enabled = !enabled }) {
                    Image(
                        painter = rememberGlidePainter(
                            request = "https://picsum.photos/300/300"
                        ),
                        contentDescription = "",
                    )
                }
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