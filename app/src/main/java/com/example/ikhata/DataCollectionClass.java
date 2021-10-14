package com.example.ikhata;

import android.provider.BaseColumns;

public class DataCollectionClass {
    public DataCollectionClass () {}

    public class Collection implements BaseColumns {
        public static final String TABLE_NAME = "khata";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_DESC = "description";
        public static final String TIME = "time";
    }
}
