/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vimi Administrator
 */
import model.Course;
import model.Room;
import model.Pair;
import model.Triple;

import java.util.List;
import java.util.ArrayList;
import java.lang.String;


public class PernaltyCounter {
    public List<Course> CourseDomain;
    public List<Room> RoomDomain; 
    public int [][] CP_R_Solution; // CP_R_Solution[c][p] = RoomIndex | NotSet

    // Hard constraints
    public int AllLecturesOnTT()
    {
        int violations = 0;

        for (int c=0;c<CP_R_Solution.length;c++)
        {
            int CourseLectureNum = 0;

            for (int p=0;p<CP_R_Solution[0].length;p++)
            {
                if (CP_R_Solution[c][p]>-1) CourseLectureNum++;
            }

            Course cThCourse = CourseDomain.get(c);
            int diff = Math.abs(cThCourse.getLectureNum()-CourseLectureNum);
            if (diff>0) violations+= diff;

        }


        return violations;

    }

    public int RoomOccupancy()
    {
        int violations = 0;

        if (CP_R_Solution.length>0)
        {

        for (int p=0;p<CP_R_Solution[0].length;p++)
        {
            int [] violatedRoomArray= new int[RoomDomain.size()];
            // clear violatedRoomArray
            for (int r=0;r<violatedRoomArray.length;r++)
            {
                violatedRoomArray[r] = 0;
            }

            int CourseLectureNum = 0;

            for (int c=0;c<CP_R_Solution.length;c++)
            {
                if (CP_R_Solution[c][p]>-1)
                {
                    int r = CP_R_Solution[c][p];
                    violatedRoomArray[r]++;
                    if (violatedRoomArray[r]>1) violations++;
                }
            }
        }

        }

        return violations;

    }

    public int sameTeacherLecturesConflict(int[][] Courses_waTeacher_Matrix)
    {
        int violations = 0;

        int [] CoursesAtPeriodArray= new int[CourseDomain.size()];
        int ArraylastIndex =-1;
            // clear violatedRoomArray
        for (int c=0;c<CoursesAtPeriodArray.length;c++)
        {
                CoursesAtPeriodArray[c] = 0;
        }



        if (CP_R_Solution.length>0)
        {

            for (int p=0;p<CP_R_Solution[0].length;p++)
            {
                for (int c=0;c<CP_R_Solution.length;c++)
                {
                    if (CP_R_Solution[c][p]>-1)
                    {
                        ArraylastIndex++;
                        CoursesAtPeriodArray[ArraylastIndex]=c;
                    }
                }
            }
        }

        for (int c1=0;c1<ArraylastIndex;c1++)
        {
            int c1Index = CoursesAtPeriodArray[c1];
            for (int c2=0;c2<ArraylastIndex;c2++)
            {
                int c2Index= CoursesAtPeriodArray[c2];

                if (Courses_waTeacher_Matrix[c1Index][c2Index]==1) violations++;

            }
        }

        return violations*2;
    }

    public int sameCurriculumLecturesConflict(int[][] HardCurriculum_Matrix)
    {
        int violations = 0;

        int [] CoursesAtPeriodArray= new int[CourseDomain.size()];
        int ArraylastIndex =-1;
            // clear violatedRoomArray
        for (int c=0;c<CoursesAtPeriodArray.length;c++)
        {
                CoursesAtPeriodArray[c] = 0;
        }



        if (CP_R_Solution.length>0)
        {

            for (int p=0;p<CP_R_Solution[0].length;p++)
            {
                for (int c=0;c<CP_R_Solution.length;c++)
                {
                    if (CP_R_Solution[c][p]>-1)
                    {
                        ArraylastIndex++;
                        CoursesAtPeriodArray[ArraylastIndex]=c;
                    }
                }
            }
        }

        for (int c1=0;c1<ArraylastIndex;c1++)
        {
            int c1Index = CoursesAtPeriodArray[c1];
            for (int c2=0;c2<ArraylastIndex;c2++)
            {
                int c2Index= CoursesAtPeriodArray[c2];

                if (HardCurriculum_Matrix[c1Index][c2Index]==1) violations++;

            }
        }

        return violations*2;
    }

    public int TeacherAvailabilities(int[][] C_P_UnavailabilityMatrix)
    {
        int violations = 0;

        for (int c=0;c<CP_R_Solution.length;c++)
        {
            int CourseLectureNum = 0;

            for (int p=0;p<CP_R_Solution[0].length;p++)
            {
                if ((CP_R_Solution[c][p]>-1)&&(C_P_UnavailabilityMatrix[c][p]==1)) violations++;
            }

        }


        return violations;
    }

    // Soft constraints
    public int RoomCapacity()
    {
        int violations = 0;

        for (int c=0;c<CP_R_Solution.length;c++)
        {
            for (int p=0;p<CP_R_Solution[0].length;p++)
            {
                if (CP_R_Solution[c][p]>-1)
                {
                   int r=CP_R_Solution[c][p];

                   Course cThCourse = CourseDomain.get(c);
                   Room givenRoom = RoomDomain.get(r);

                   int StuNumOver = cThCourse.getStudentNum()-givenRoom.getCapacity();
                   
                   if (StuNumOver>0) violations += StuNumOver;
                }
            }
        }


        return violations;
    }

    public int MinimumWorkingDays(int Periods_per_day)
    {
        int DaysbelowMinimum =0;

        for (int c=0;c<CP_R_Solution.length;c++)
        {
            Course cThCourse = CourseDomain.get(c);
            int MinimumWorkingDays = cThCourse.getMinWorkingDays();

            int actualWorkingDay = 0;
            
            int lastDay =-1;
            int actualDay = 0;

            for (int p=0;p<CP_R_Solution[0].length;p++)
            {
                actualDay= Math.round(p / Periods_per_day);
                if (actualDay!=lastDay)
                { 
                  if (CP_R_Solution[c][p]>-1)
                  {
                     actualWorkingDay++;
                     lastDay = actualDay;
                  }
                }
                
            }

            if (actualWorkingDay<MinimumWorkingDays) DaysbelowMinimum += MinimumWorkingDays - actualWorkingDay;

        }

        return DaysbelowMinimum*5;
    }

    

}
