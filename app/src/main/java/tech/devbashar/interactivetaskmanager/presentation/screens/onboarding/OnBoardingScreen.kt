package tech.devbashar.interactivetaskmanager.presentation.screens.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tech.devbashar.interactivetaskmanager.R
import tech.devbashar.interactivetaskmanager.domain.model.OnBoardModel
import tech.devbashar.interactivetaskmanager.presentation.components.widgets.PageIndicator
import tech.devbashar.interactivetaskmanager.presentation.screens.home.HomeScreen
import tech.devbashar.interactivetaskmanager.presentation.screens.home.HomeViewModel
import tech.devbashar.interactivetaskmanager.presentation.theme.AppTheme
import kotlin.math.absoluteValue

@AndroidEntryPoint
class OnBoardingScreen : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val primaryColor by viewModel.primaryColor.collectAsState()
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val useSystemDefault by viewModel.useSystemDefault.collectAsState()
            AppTheme(
                seedColor = primaryColor,
                darkTheme = if (useSystemDefault) isSystemInDarkTheme() else isDarkMode,
            ) {
                OnBoardingScreenContent()
            }
        }
    }

    @Composable
    fun OnBoardingScreenContent() {
        val onBoardItemsModel =
            listOf(
                OnBoardModel(
                    title = R.string.on_boarding_title_1,
                    description = R.string.on_boarding_description_1,
                    color = MaterialTheme.colorScheme.primary,
                    imageRes = R.drawable.ic_onboarding_1,
                ),
                OnBoardModel(
                    title = R.string.on_boarding_title_2,
                    description = R.string.on_boarding_description_2,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    imageRes = R.drawable.ic_onboarding_2,
                ),
                OnBoardModel(
                    title = R.string.on_boarding_title_3,
                    description = R.string.on_boarding_description_3,
                    color = MaterialTheme.colorScheme.tertiary,
                    imageRes = R.drawable.ic_onboarding_3,
                ),
            )

        val pagerState = rememberPagerState(initialPage = 0, pageCount = { onBoardItemsModel.size })
        val item = onBoardItemsModel[pagerState.currentPage]
        val animationScope = rememberCoroutineScope()
        val animatedColor by animateColorAsState(
            targetValue = item.color,
            animationSpec = tween(durationMillis = 500),
        )

        val colorFilter = ColorFilter.tint(animatedColor)
        Surface {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
            ) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.TopCenter),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_oval_shape),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        colorFilter = colorFilter,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.45f)
                                .padding(24.dp),
                    )
                }
                HorizontalPager(
                    state = pagerState,
                    modifier =
                        Modifier
                            .fillMaxSize(),
                ) { page ->
                    val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    val scale = 1f - 0.35f * pageOffset.absoluteValue
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    alpha = 1f - 0.8f * pageOffset.absoluteValue
                                },
                    ) {
                        OnBoardItem(onBoardItemsModel[page])
                    }
                }

                Row(
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(WindowInsets.safeContent.asPaddingValues()),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PageIndicator(
                        size = pagerState.pageCount,
                        selectedIndex = pagerState.currentPage,
                    )
                    OnBoardingNextButton(iconRes = R.drawable.ic_forward_arrow) {
                        animationScope.launch {
                            if (pagerState.currentPage < pagerState.pageCount - 1) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            } else if (pagerState.currentPage == pagerState.pageCount - 1) {
                                val intent = Intent(this@OnBoardingScreen, HomeScreen::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}
