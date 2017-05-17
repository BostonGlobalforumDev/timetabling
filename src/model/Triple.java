/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;


/**
 *
 * @author vimi Administrator
 */
public class Triple<X,Y,Z> {
    private X first;
    private Y second;
    private Z third;

    public Triple() { first = null; second = null; third = null;}

    public Triple(X first, Y second, Z third) { this.first = first; this.second = second; this.third = third;}

    // getter & setter
    public X getFirst() { return first; }
    public Y getSecond() { return second; }
    public Z getThird() { return third; }

    public void setFirst(X newValue) { first = newValue; }
    public void setSecond(Y newValue) { second = newValue; }
    public void setThird(Z newValue) { third = newValue; }

}
