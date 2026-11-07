/*
 * Copyright (C) 2026 Open Transit Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.android.ui.compose.components

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.height
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.onebusaway.android.ui.compose.createUnconfinedComposeRule
import org.onebusaway.android.ui.compose.theme.ObaTheme

/**
 * Render checks for [LineBadge]'s single-word fit rule: a name like "M70" must never be broken
 * mid-word across two lines (as "M7 / 0"). The badge shrinks the font until the whole token fits on
 * one line, so the rendered height must equal a single-line badge — never two lines tall.
 */
@RunWith(AndroidJUnit4::class)
class LineBadgeRenderTest {

    @get:Rule
    val composeRule = createUnconfinedComposeRule()

    /**
     * "M70" is a single whitespace-free token. At the default badge width (64dp) the font shrinks
     * until "M70" fits on one line — the result must be no taller than a known single-line name ("8")
     * rendered at the same size settings.
     */
    @Test
    fun m70StaysOnOneLine() {
        val singleLineName = "8"
        composeRule.setContent {
            CompositionLocalProvider(LocalDensity provides Density(2f)) {
                ObaTheme {
                    LineBadge(text = singleLineName, modifier = androidx.compose.ui.Modifier.testTag("single"))
                    LineBadge(text = "M70", modifier = androidx.compose.ui.Modifier.testTag("m70"))
                }
            }
        }
        composeRule.waitForIdle()

        val singleHeight = composeRule.onNodeWithTag("single").getUnclippedBoundsInRoot().height
        val m70Height    = composeRule.onNodeWithTag("m70").getUnclippedBoundsInRoot().height

        assertTrue(
            "\"M70\" must render on one line (height $m70Height) — no taller than \"$singleLineName\" ($singleHeight)",
            m70Height <= singleHeight
        )
    }
}
