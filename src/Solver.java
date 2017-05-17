/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vimi Administrator
 */
import java.math.*;


public class Solver {
    public int[][] Course_Period_Matrix; // q x p
    public int[][] Course_Room_Matrix; // q x r
    public int[][] Room_Period_Matrix; // r x p

    // Course C in Room R at Period P
    // Array of Pair (R,P)
    //

    // Solution presentation
    // Integer valued matrix: Xij = -1 -> Course i was not scheduled at period j
    //                        Xij = r (r>=0) -> Course i was scheduled at period j and assigned with room r
    public int [][] CP_R_Solution;


    private InputReader _inputReader;

    public Solver(InputReader inputReader)
    {
        this._inputReader = inputReader;
    }


    // Utility
    public static boolean passLectureNumandMinWDays(int[] CourseTSol,int Periods_per_day ,int LectureNum, int MinWorkingDays)
    {
        boolean result = false;
        int ArrayLecNum =0;
        int SolWorkingDays = 0;

        int preDay=-1;
        int actualDay=0;


        for (int i=0;i<CourseTSol.length;i++)
        {
            actualDay= Math.round(i / Periods_per_day);
            System.out.println("actual Day: "+actualDay);

            if (CourseTSol[i]==1)
            {
                ArrayLecNum += CourseTSol[i];

                if (actualDay!=preDay)
                {
                    SolWorkingDays ++;
                    preDay = actualDay;
                }
            }

        }

        System.out.println("ArrayLecNum "+ArrayLecNum);
        System.out.println("SolWorkingDays "+SolWorkingDays);

        return ((ArrayLecNum == LectureNum)&&(SolWorkingDays >= MinWorkingDays));

    }

}
