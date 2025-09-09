package rajeev.ranjan.recipeapp.core.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

internal fun exitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
        // A -> B : Screen A fades out slowly
        fadeOut(animationSpec = tween(1000))
    }

internal fun enterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
        // A -> B : Screen B slides in
        slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
    }

internal fun popExitTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
        // B -> A : Screen B slides out
        slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
    }

internal fun popEnterTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
        // B -> A : Screen A fades in quickly
        fadeIn(animationSpec = tween(150))
    }

