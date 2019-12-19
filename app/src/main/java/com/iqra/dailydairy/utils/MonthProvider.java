package com.iqra.dailydairy.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MonthProvider {
    private static ArrayList<String> months = new ArrayList<String>(Collections.singletonList("January,February,March,April,May,June,July,August,September,October,November,December"));

    public static ArrayList<String> getMonths() {
        return months;
    }
}
