/*
 * Copyright (C) 2011 Make Ramen, LLC
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

package com.cg.quickaction;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioGroup;

import com.cg.snazmed.R;

public class SegmentedRadioGroup extends RadioGroup {

	public SegmentedRadioGroup(Context context) {
		super(context);
	}

	public SegmentedRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		changeButtonsImages();
	}

	private void changeButtonsImages(){
		int count = super.getChildCount();

		if(count > 1){
			super.getChildAt(0).setBackgroundResource(R.drawable.segment_radio_left);
			super.getChildAt(count-1).setBackgroundResource(R.drawable.segment_radio_right);
		}else if (count == 1){
			super.getChildAt(0).setBackgroundResource(R.drawable.segment_button);
		}
		
	}
	
	public void changeCheckedItem(int pos)
	{
		Log.i("thread","$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		/*super.getParent();*/
	}
}