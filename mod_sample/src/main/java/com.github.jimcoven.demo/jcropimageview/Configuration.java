/*
 *  Copyright (c) 2015. Jim Coven
 *  http://www.github.com/jimcoven
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.jimcoven.demo.jcropimageview;

import android.os.Parcel;
import android.os.Parcelable;

class Configuration implements Parcelable {
    final int cropType;
    final int cropAlign;

    Configuration(int type, int align) {
        cropType = type;
        cropAlign = align;
    }

    String getConfigurationName() {
        return getCropType(cropType) + " + " + getCropAlign(cropAlign);
    }

    private static String getCropType(int id) {
        switch(id) {
            case 0: return "FIT_WIDTH";
            case 1: return "FIT_FILL";
            case 2: return "FIT_HEIGHT";
        }
        return "NA";
    }

    private static String getCropAlign(int id) {
        switch(id) {
            case 0: return "ALIGN_TOP";
            case 1: return "ALIGN_BOTTOM";
            case 2: return "ALIGN_CENTER";
            case 3: return "ALIGN_LEFT";
            case 4: return "ALIGN_RIGHT";
        }
        return "NA";
    }

    Configuration(Parcel in) {
        cropType = in.readInt();
        cropAlign = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cropType);
        dest.writeInt(cropAlign);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Configuration> CREATOR
            = new Parcelable.Creator<Configuration>() {

        public Configuration createFromParcel(Parcel in) {
            return new Configuration(in);
        }

        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    };
}
