/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Dictionary;


/**
 *
 * @author vimi Administrator
 */
public class Course {
    public int index=-1;
    private String CourseID;
    private String Teacher;
    private int LectureNum;
    private int MinWorkingDays;

    private int StudentNum;
    

    // public boolean DoubleLectures; // for extended ctt format

    public Course()
    {
        
    }

    // Setter and getter
    public String getCourseID()
    {
        return CourseID;
    }
    public void setCourseID(String ID)
    {
        this.CourseID= ID;
    }


    public String getTeacher()
    {
        return Teacher;
    }
    public void setTeacher(String Teacher)
    {
        this.Teacher= Teacher;
    }


    public int getLectureNum()
    {
        return LectureNum;
    }
    public void setLectureNum(int LectureNum)
    {
        this.LectureNum= LectureNum;
    }


    public int getMinWorkingDays()
    {
        return MinWorkingDays;
    }
    public void setMinWorkingDays(int minWorkingDays)
    {
        this.MinWorkingDays= minWorkingDays;
    }


    public int getStudentNum()
    {
        return StudentNum;
    }
    public void setStudentNum(int StudentNum)
    {
        this.StudentNum= StudentNum;
    }


    public static int getPosofID(List<Course> courses,String ID)
    {
        Object [] _courses;
        int index =-1;

        if (courses != null)
        {
            _courses= courses.toArray();

            for (int i=0;i<_courses.length;i++)
            {
                Course IthCourse=(Course) _courses[i];
                if (ID.equals(IthCourse.CourseID))
                {
                    index = i;
                    break;
                }
            }
        }
        
        return index;
    }

    public static void showCourses(List<Course> courses)
    {
        Object [] _courses;
        int index =-1;

        if (courses != null)
        {
            _courses= courses.toArray();

            for (int i=0;i<_courses.length;i++)
            {
                Course IthCourse=(Course) _courses[i];
                System.out.print(" "+IthCourse.getCourseID()+" ");
                System.out.println(" StuNum "+IthCourse.getStudentNum()+" ");
            }
        }
    }

    public static ArrayList<Course> sortByStuNum(List<Course> courses)
    {
        
        ArrayList<Course> _sortedCourses= new ArrayList<Course>();

        Object [] _courses;
        

        if (courses != null)
        {
            _courses= courses.toArray();

            for (int i=0;i<_courses.length;i++)
            {
                Course IthCourse=(Course) _courses[i];
                Object [] _sorted_courses = _sortedCourses.toArray();

                if (_sortedCourses.isEmpty()) _sortedCourses.add(IthCourse);
                else
                {
                    int index=-1;

                    System.out.println("Ith Course to add "+IthCourse.getCourseID()+" "+IthCourse.getStudentNum());

                    for (int j=0;j<_sorted_courses.length;j++)
                    {
                        Course JthCourse=(Course) _sorted_courses[j];

                        System.out.println("Jth Course: "+JthCourse.getCourseID()+" "+JthCourse.getStudentNum());

                        if (IthCourse.getStudentNum()>=JthCourse.getStudentNum())
                        {
                            index =j;
                            break;
                        }

                    }
                    System.out.println("Index: "+index);
                    if (index!=-1) _sortedCourses.add(index, IthCourse);
                    else _sortedCourses.add(IthCourse);
                }
                 //_sortedCourses.put(IthCourse.getStudentNum(), IthCourse);
                 // myDic.put(IthCourse.getStudentNum(), IthCourse);
            }
        }

        Object [] _sorted_Courses= _sortedCourses.toArray();

        for (int i=0;i<_sorted_Courses.length;i++)
        {
             Course IthCourse=(Course) _sorted_Courses[i];
             System.out.print(" "+IthCourse.getCourseID()+" ");
             System.out.println(" StuNum "+IthCourse.getStudentNum()+" ");
             
        }

        return _sortedCourses;

    }


    public static ArrayList<Course> sortByCurriculumWStuNum(List<Course> courses)
    {

        ArrayList<Course> _sortedCourses= new ArrayList<Course>();

        Object [] _courses;


        if (courses != null)
        {
            _courses= courses.toArray();
        }

        

        return _sortedCourses;
        
    }




}
