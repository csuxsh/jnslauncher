/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.jslauncher.steven;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import java.util.Locale;

public class LocalePicker extends com.android.internal.app.LocalePicker
        implements com.android.internal.app.LocalePicker.LocaleSelectionListener{

    private static final String TAG = "LocalePicker";
    private Activity activity;

   // private SettingsDialogFragment mDialogFragment;
    private static final String SAVE_TARGET_LOCALE = "locale";

    private Locale mTargetLocale;

    public void setActiviy(Activity activity) {
        this.activity = activity;
    }
    
    public LocalePicker() {
        super();
        setLocaleSelectionListener(this);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVE_TARGET_LOCALE)) {
            mTargetLocale = new Locale(savedInstanceState.getString(SAVE_TARGET_LOCALE));
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        final ListView list = (ListView) view.findViewById(android.R.id.list);
        Utils.forcePrepareCustomPreferencesList(container, view, list, false);
        return view;
    }

    @Override
    public void onLocaleSelected(final Locale locale) {
            getActivity().onBackPressed();
            LocalePicker.updateLocale(locale);
        	SharedPreferences sp = activity.getApplicationContext(). getSharedPreferences("setupwizard", Context.MODE_PRIVATE); 
    		SharedPreferences.Editor  edit = sp.edit();
    		edit.putInt("boolean", 1);
    		edit.commit();
            activity.finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mTargetLocale != null) {
            outState.putString(SAVE_TARGET_LOCALE, mTargetLocale.toString());
        }
    }

}
