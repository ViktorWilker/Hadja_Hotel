package br.com.hadja_hotel.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.hadja_hotel.service.AirConditioningService
import br.com.hadja_hotel.ui.screens.AirConditioningScreen
import br.com.hadja_hotel.ui.screens.EventScreen
import br.com.hadja_hotel.ui.screens.FuelScreen
import br.com.hadja_hotel.ui.screens.GuestDetailScreen
import br.com.hadja_hotel.ui.screens.GuestScreen
import br.com.hadja_hotel.ui.screens.HomeScreen
import br.com.hadja_hotel.ui.screens.LoginScreen
import br.com.hadja_hotel.ui.screens.ReportScreen
import br.com.hadja_hotel.ui.screens.ReservationScreen
import br.com.hadja_hotel.viewmodel.HotelViewModel

@Composable
fun NavGraph(viewModel: HotelViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        composable("login"){
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { navController.navigate("home")}
            )
        }

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    navController.popBackStack("login", inclusive = true)
                    navController.navigate("login")
                }
            )
        }

        composable("reservation") {
            ReservationScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("guest") {
            GuestScreen(
                viewModel = viewModel,
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable("fuel") {
            FuelScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("report") {
            ReportScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("event"){
            EventScreen(
                viewModel = viewModel,
                onBack = {navController.popBackStack()}
            )
        }

        composable("airconditioning"){
            AirConditioningScreen(
                viewModel = viewModel,
                onBack = {navController.popBackStack()}
            )
        }

        composable("guest_detail/{guestName}") { backStackEntry ->
            val guestName = backStackEntry.arguments?.getString("guestName") ?: ""
            GuestDetailScreen(
                viewModel = viewModel,
                guestName = guestName,
                onBack = { navController.popBackStack() }
            )
        }

    }
}
