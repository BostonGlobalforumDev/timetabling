/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import model.Course;
import model.Room;

/**
 *
 * @author Administrator
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //InputReader myInputReader = new InputReader("C://comp12.ctt");
        InputReader myInputReader = new InputReader("C://comp01.ctt");

        System.out.println("Number of courses: "+myInputReader.CNum);

        System.out.println("#Courses :"+myInputReader.Courses.size());
        System.out.println("#Rooms :"+myInputReader.Rooms.size());

        System.out.println("HardCurriculum_Matrix");
        for (int i=0;i< myInputReader.HardCurriculum_Matrix.length;i++)
        {
            System.out.print(myInputReader.Courses.get(i).getCourseID()+": ");
            
            for (int j=0;j< myInputReader.HardCurriculum_Matrix[i].length;j++)
            {
                System.out.print(myInputReader.HardCurriculum_Matrix[i][j]+" ");
            }

            System.out.println(" ");
            
        }

        String testID = myInputReader.Courses.get(2).getCourseID();
        System.out.println("Index of "+testID+" is "+Course.getPosofID(myInputReader.Courses,testID));
        System.out.println();

        System.out.println("C_P_UnavailabilityMatrix");
        for (int i=0;i< myInputReader.C_P_UnavailabilityMatrix.length;i++)
        {
            System.out.print(myInputReader.Courses.get(i).getCourseID()+": "+" index: "+myInputReader.Courses.get(i).index+"  ");
            
            for (int j=0;j< myInputReader.C_P_UnavailabilityMatrix[i].length;j++)
            {
                System.out.print(myInputReader.C_P_UnavailabilityMatrix[i][j]+" ");
            }

            System.out.println(" ");
            
        }

        myInputReader.calcC_R_Capacity_Constraint();
        System.out.println("C_R_Capacity_Assignment");
        
        for (int i=0;i< myInputReader.C_R_Capacity_Assignment.length;i++)
        {
            System.out.print(myInputReader.Courses.get(i).getCourseID()+": ");

            for (int j=0;j< myInputReader.C_R_Capacity_Assignment[i].length;j++)
            {
                System.out.print(myInputReader.C_R_Capacity_Assignment[i][j]+" ");
            }

            System.out.println(" ");

        }


        myInputReader.calcCourses_waTeacher_Matrix();
        System.out.println("Courses_waTeacher_Matrix");

        for (int i=0;i< myInputReader.Courses_waTeacher_Matrix.length;i++)
        {
            System.out.print(myInputReader.Courses.get(i).getCourseID()+": ");

            for (int j=0;j< myInputReader.Courses_waTeacher_Matrix[i].length;j++)
            {
                System.out.print(myInputReader.Courses_waTeacher_Matrix[i][j]+" ");
            }

            System.out.println(" ");

        }


        Course.showCourses(myInputReader.Courses);
        System.out.println("Test Course Sort");
        Course.showCourses(Course.sortByStuNum(myInputReader.Courses));

        System.out.println("Test Room Sort");
        Room.show(Room.sortByCapacity(myInputReader.Rooms));

        System.out.println("########################################");
        System.out.println("Test Solver.passLectureNumandMinWDays( )");
        int [] sol = {1,0,0,0,1,0};
        System.out.println(Solver.passLectureNumandMinWDays(sol, 2, 2, 2));

        // Test FirstSolFinder
        FirstSolFinder myFirstSolFinder = new FirstSolFinder();

        myFirstSolFinder.setInputReader(myInputReader);
        myFirstSolFinder.test();



        /*
        myFirstSolFinder.initSolutionMatrix();
        myFirstSolFinder.generateFSol4Course(0);
        myFirstSolFinder.printSolMatrix();
         *
         */

        


    }



    

}
