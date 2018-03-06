package com.ilkengin.proposalapp.utils;

/**
 * Created by ilkengin on 19.09.2017.
 */

public class Math {

    private static final String TAG = Math.class.getSimpleName();

    public static <T extends Comparable<T>> T max(T a1, T a2) {
        return a1.compareTo(a2) > 0 ? a1 : a2;
    }

    public static <T extends Comparable<T>> T min(T a1, T a2) {
        return a1.compareTo(a2) > 0 ? a2 : a1;
    }

}
