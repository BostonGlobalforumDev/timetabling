/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.lang.*;
/**
 *
 * @author vimi Administrator
 */
public class Pair<T, U> {
    private T first;
    private U second;

    public Pair() { first = null; second = null; }

    public Pair(T first, U second) { this.first = first; this.second = second; }

    // getter & setter
    public T getFirst() { return first; }
    public U getSecond() { return second; }

    public void setFirst(T newValue) { first = newValue; }
    public void setSecond(U newValue) { second = newValue; }

    public static <T> T getMiddle(T[] a)
    {
        return a[a.length / 2];
    }

    // <T extends Comparable & Serializable>
    public static <T extends Comparable> T min(T[] a) // almost correct
    {
        if (a == null || a.length == 0) return null;
        T smallest = a[0];
        for (int i = 1; i < a.length; i++)
        if (smallest.compareTo(a[i]) > 0) smallest = a[i];
        return smallest;
    }

}
