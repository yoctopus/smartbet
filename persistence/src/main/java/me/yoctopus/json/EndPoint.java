/*
 * Copyright 2017, Solutech RMS
 * Licensed under the Apache License, Version 2.0, "Solutech Limited".
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.json;


public class EndPoint {
    private String relativeUrl;
    private String extension;
    private String readArrayName = "";

    public static class Type {
        private String s;

        public Type(String s) {
            this.s = s;
        }

        public static Type PHP() {
            return new Type(".php");
        }
        public static Type GO() {
            return new Type(".go");
        }
        public static Type REST() {
            return new Type("");
        }
    }

    public EndPoint() {
    }

    public EndPoint(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }

    public EndPoint(String relativeUrl, Type type) {
        this.relativeUrl = relativeUrl;
        this.extension = type.s;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public void setRelativeUrl(String name) {
        this.relativeUrl = name;
    }

    public String getReadArrayName() {
        return readArrayName;
    }

    public void setReadArrayName(String readArrayName) {
        this.readArrayName = readArrayName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        if (extension == null || extension.isEmpty()) {
            return relativeUrl;
        }
        return relativeUrl.concat(".").concat(extension);
    }
}
