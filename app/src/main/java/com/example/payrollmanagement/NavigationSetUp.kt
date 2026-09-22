package com.example.payrollmanagement

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.payrollmanagement.presentation.view.createview.CreatePayrollScreen
import com.example.payrollmanagement.presentation.view.detailview.PayrollDetailScreen
import com.example.payrollmanagement.presentation.view.listview.PayrollListScreen
import com.example.payrollmanagement.presentation.view.splashview.SplashScreen
import kotlinx.coroutines.delay

sealed class Screen(val route: String) {

    object Splash : Screen("splash")

    object List : Screen("list")
    object Create : Screen("create")
    object Detail : Screen("detail")

}

@Composable
fun PayrollNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {

        composable(route = Screen.Splash.route){
            LaunchedEffect(Unit) {
                delay(3000)
                navController.navigate(Screen.List.route)
            }
            SplashScreen()
        }

        composable(route = Screen.List.route) {
            PayrollListScreen(
                onCreatePayrollClick = { navController.navigate(Screen.Create.route) },
                onPayrollClick = { id -> navController.navigate("${Screen.Detail.route}/$id") },
                onEditPayrollClick = { id -> navController.navigate("${Screen.Create.route}?payrollId=$id")}
            )
        }

        composable(
            route = "${Screen.Create.route}?payrollId={payrollId}",
            arguments = listOf(navArgument("payrollId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->

            CreatePayrollScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Screen.Detail.route}/{payrollId}",
            arguments = listOf(navArgument("payrollId") { type = NavType.LongType })
        ) { backStackEntry ->
            PayrollDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}