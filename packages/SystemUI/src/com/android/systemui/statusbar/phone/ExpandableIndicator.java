/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.android.systemui.res.R;

public class ExpandableIndicator extends ImageView {

    private boolean mExpanded;
    private boolean mIsDefaultDirection = true;
    private static final int ROTATION_DURATION = 250;

    public ExpandableIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        updateIndicatorDrawable();
        setContentDescription(getContentDescription(mExpanded));
    }

    public void setExpanded(boolean expanded) {
        if (expanded == mExpanded) return;
        mExpanded = expanded;
        animateRotation(expanded);
        setContentDescription(getContentDescription(expanded));
    }

    /** Whether the icons are using the default direction or the opposite */
    public void setDefaultDirection(boolean isDefaultDirection) {
        mIsDefaultDirection = isDefaultDirection;
        updateIndicatorDrawable();
    }

    private void animateRotation(boolean expanded) {
        animate().cancel();
        float targetRotation = expanded ? 180f : 0f;
        if (!mIsDefaultDirection) {
            targetRotation = -targetRotation;
        }
        animate()
                .rotation(targetRotation)
                .setDuration(ROTATION_DURATION)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private String getContentDescription(boolean expanded) {
        return expanded ? mContext.getString(R.string.accessibility_quick_settings_collapse)
                : mContext.getString(R.string.accessibility_quick_settings_expand);
    }

    private void updateIndicatorDrawable() {
        setImageResource(R.drawable.volume_settings);
        float initialRotation = mExpanded ? 180f : 0f;
        if (!mIsDefaultDirection) {
            initialRotation = -initialRotation;
        }
        setRotation(initialRotation);
    }
}
