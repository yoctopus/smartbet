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

package me.yoctopus.fastdb;

import java.util.Comparator;

public final class Column<D> {
    static Comparator<Column> ascending =
            new Comparator<Column>() {
                @Override
                public int compare(Column o1, Column o2) {
                    return o1.getIndex() > o2.getIndex() ? 1 :
                            o1.getIndex() < o2.getIndex() ? -1 : 0;
                }
            };
    private String name;
    private String description;
    private D value;
    private int index;

    public Column(String name, Type type) {
        this(name, type, 0);
    }

    public Column(String name, Type type, int index) {
        this(name, type.s, index);
    }

    private Column(String name, String description, int index) {
        this.name = name;
        this.description = description;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    protected D getValue() {
        return value;
    }

    public void set(D value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    protected String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "`" + name + "`" + " " + description;
    }

    private enum NULL {
        YES, NO
    }

    public static class Type {
        private String s;

        Type(String s) {
            this.s = s;
        }

        private static Type newType(String prefix, NULL n, String s) {
            String g = n != NULL.NO ? "" : " NOT NULL";
            return new Type(prefix + g + " "+ s);
        }

        private static Type newType(String prefix, String s) {
            return newType(prefix, NULL.YES, s);
        }

        private static Type newType(String prefix, NULL n) {
            return newType(prefix, n, "");
        }

        private static Type newType(String prefix) {
            return newType(prefix, "");
        }

        public static class INTEGER {
            private static final String prefix =
                    "INTEGER";

            public static Type NOT_NULL() {
                return newType(prefix, NULL.NO);
            }

            public static Type NULLABLE() {
                return newType(prefix, NULL.YES);
            }

            public static Type PRIMARY_KEY() {
                return newType(prefix, "PRIMARY KEY");
            }

            public static Type PRIMARY_KEY_AUTOINCREMENT() {
                return newType(prefix, "PRIMARY KEY AUTOINCREMENT");
            }

            public static Type DEFAULT(int d) {
                return newType(prefix, "DEFAULT " + d);
            }
        }

        public static class TEXT {
            private static final String prefix =
                    "TEXT";

            public static Type NULLABLE() {
                return newType(prefix, NULL.YES);
            }

            public static Type NOT_NULL() {
                return newType(prefix, NULL.NO);
            }
        }

        public static class BLOB {
            private static final String prefix =
                    "BLOB";

            public static Type NULLABLE() {
                return newType(prefix, NULL.YES);
            }

            public static Type NOT_NULL() {
                return newType(prefix, NULL.NO);
            }
        }

        public static class REAL {
            private static final String prefix =
                    "REAL";

            public static Type NULLABLE() {
                return newType(prefix, NULL.YES);
            }

            public static Type NOT_NULL() {
                return newType(prefix, NULL.NO);
            }
        }
    }
}
