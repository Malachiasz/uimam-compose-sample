package com.uimam.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DetailScreen(navController: NavController, rowId: Int) {
    DetailContent(navController = navController, row = Row(rowId, "wwdd", "yeah"))
}

@Composable
fun DetailContent(navController: NavController, row: Row) {
    Column(Modifier.padding(16.dp)) {
        Text(text = row.name)
        Text(text = row.description)

        Button(onClick = { navController.popBackStack() }) {
            Text("Go Back")
        }

    }
}