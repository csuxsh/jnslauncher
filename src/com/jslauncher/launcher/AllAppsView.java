/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.jslauncher.launcher;

import java.util.ArrayList;
import java.util.Comparator;

public interface AllAppsView {
    public interface Watcher {
        public void zoomed(float zoom);
    }

    public void setLauncher(Launcher launcher);

    public void setDragController(DragController dragger);

    public void zoom(float zoom, boolean animate);

    public boolean isVisible();

    public boolean isOpaque();

    public void setApps(ArrayList<ApplicationInfo> list, Comparator<ApplicationInfo> compareator);

    public void addApps(ArrayList<ApplicationInfo> list, Comparator<ApplicationInfo> compareator);

    public void removeApps(ArrayList<ApplicationInfo> list);

    public void updateApps(ArrayList<ApplicationInfo> list, Comparator<ApplicationInfo> compareator);
    
    public void dumpState();

    public void surrender();
}
