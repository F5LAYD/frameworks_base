/*
 * Copyright (C) 2025 the AxionAOSP Project
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
package com.android.systemui.weather

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.android.systemui.res.R

/**
 * A self-contained TextView that displays weather temperature and condition info
 * by delegating all logic to [WeatherViewController].
 *
 * The controller requires both an ImageView and a TextView target, plus a
 * container View whose visibility it manages. When this view is used standalone
 * (i.e. without a sibling WeatherImageView), we pass `this` as both the TextView
 * AND as the container so the controller can show/hide us correctly.
 * The ImageView slot is left as a no-op stub because this view only renders text.
 */
class WeatherTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TextView(context, attrs, defStyle) {

    private val mWeatherViewController: WeatherViewController

    init {
        visibility = View.GONE

        val stubIcon = android.widget.ImageView(context)

        mWeatherViewController = WeatherViewController(
            context = context,
            weatherIcon = stubIcon,
            weatherTemp = this,
            weatherInfoView = this,
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mWeatherViewController.init()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mWeatherViewController.removeObserver()
    }
}
