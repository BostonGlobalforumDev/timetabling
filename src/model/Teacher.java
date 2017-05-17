/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *
 * @author vimi Administrator
 */
public class Teacher {
    private String TeacherID;

    public void Teacher(String _TeacherID)
    {
        this.TeacherID = _TeacherID;
    }

    public String getTeacher()
    {
        return TeacherID;
    }
    public void setTeacher(String TeacherID)
    {
        this.TeacherID= TeacherID;
    }

}
