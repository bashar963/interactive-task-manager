package tech.devbashar.interactivetaskmanager.presentation.screens.task.create

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tech.devbashar.interactivetaskmanager.presentation.screens.home.HomeScreen

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TaskCreateScreenTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<HomeScreen>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.onNodeWithTag("create_task_fab").performClick()
    }

    @Test
    fun testTaskCreationFlow() =
        runTest {
            composeTestRule.onNodeWithText("Create New Task").assertIsDisplayed()

            composeTestRule.onNodeWithText("Title")
                .performTextInput("Test Task")

            composeTestRule.onNodeWithText("Description")
                .performTextInput("Test Description")

            composeTestRule.onNodeWithText("MEDIUM")
                .performClick()

            composeTestRule.onNodeWithText("Create Task")
                .performClick()
        }

    @Test
    fun testEmptyTitleValidation() {
        composeTestRule.onNodeWithText("Create Task")
            .performClick()

        composeTestRule.onNodeWithText("Title is required")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Create New Task")
            .assertIsDisplayed()
    }

    @Test
    fun testPrioritySelection() {
        val priorities = listOf("LOW", "MEDIUM", "HIGH")

        priorities.forEach { priority ->
            composeTestRule.onNodeWithText(priority)
                .performClick()
            composeTestRule.waitForIdle()
        }
    }
}
