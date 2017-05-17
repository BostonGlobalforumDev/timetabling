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


public class FirstSolFinder {
    // a. Input
    private InputReader _inputReader;
    public List<Course> CourseDomain; // sorted by StudentNum
    private List<String> LecturesDomain; // sorted by StudentNum
    public List<Room> RoomDomain; // sorted by capacity

    // b. redundant data structures
    private int[] intRoomDomain; // order number, sorted by capacity
    private int[] TimeslotDomain;

    // c. help semi structures
    private Pair<String,String> R_P_Pair;
    private List<Pair> R_P_Pairs;
    
    private List<Triple> L_R_P_Triples;

    // d. output structure
    public int [][] CP_R_Solution; // CP_R_Solution[c][p] = RoomIndex | NotSet
    



    public void setInputReader(InputReader inputReader)
    {
        this._inputReader = inputReader;

        LecturesDomain= new ArrayList();
        R_P_Pairs = new ArrayList();

        L_R_P_Triples = new ArrayList();
    }

    //
    private void initSolutionMatrix()
    {
        CP_R_Solution = new int[_inputReader.CNum][_inputReader.DNum*_inputReader.Periods_per_day];

        for (int i=0;i<CP_R_Solution.length;i++)
        {
            for (int j=0;j<CP_R_Solution[0].length;j++)
            {
                CP_R_Solution[i][j]=-1;
            }
        }
    }

    private void initArrayLists()
    {
        
        if (this._inputReader!= null)
        {
            CourseDomain = Course.sortByStuNum(_inputReader.Courses);
            RoomDomain = Room.sortByCapacity(_inputReader.Rooms);

            // convert CourseDomain to LecturesDomain
            for (int i=0;i<CourseDomain.size();i++)
            {
                Course IthCourse = (Course) CourseDomain.toArray()[i];
                int LecNum = IthCourse.getLectureNum();

                for (int j=0;j<LecNum;j++)
                {
                    this.LecturesDomain.add(i+"");
                }


            }

        }
    }


    public void test()
    {
        initArrayLists();
        initIntRoomDomain();
        initTimeslotDomain();
        generateRPPairs();
        generateLRPTriples();
        print();

    }

    public void print()
    {
        for (int i=0;i<CourseDomain.size();i++)
        {
            Course _IthCourse = CourseDomain.get(i);

            System.out.print("Course "+_IthCourse.getCourseID()+" StuNum"+_IthCourse.getStudentNum());
            System.out.println("");

        }

        for (int i=0;i<RoomDomain.size();i++)
        {
            Room _IthRoom = RoomDomain.get(i);

            System.out.print("Room "+_IthRoom.getId()+" Capacity"+_IthRoom.getCapacity());
            System.out.println("");

        }

        // Print R_P_Pairs
        for (int i=0;i<R_P_Pairs.size();i++)
        {
            System.out.println(R_P_Pairs.get(i).getFirst()+"-"+R_P_Pairs.get(i).getSecond());
        }

        // Print LecturesDomain
        for (int i=0;i<LecturesDomain.size();i++)
        {
            System.out.println("Lecture "+LecturesDomain.get(i));
        }

        // Print L_R_P_Triple
        System.out.println("L_R_P_Triples.size()"+L_R_P_Triples.size());
        for (int i=0;i<L_R_P_Triples.size();i++)
        {
            System.out.println(L_R_P_Triples.get(i).getFirst()+"-"+L_R_P_Triples.get(i).getSecond()+"-"+L_R_P_Triples.get(i).getThird());
        }


    }

    // 1. R P Pairs Generator
    // a.
    private void initIntRoomDomain()
    {
        intRoomDomain = new int[RoomDomain.size()];

        for (int i=0;i<intRoomDomain.length;i++)
        {
            intRoomDomain[i] =i;
        }
    }

    // b.
    private void initTimeslotDomain()
    {
        TimeslotDomain = new int[_inputReader.DNum*_inputReader.Periods_per_day];

        for (int i=0;i<TimeslotDomain.length;i++)
        {
            TimeslotDomain[i] =i;
        }


    }

    // c.
    private void generateRPPairs()
    {
        for (int i=0;i<intRoomDomain.length;i++)
        {
            for (int j=0;j<TimeslotDomain.length;j++)
            {
                Pair _IJPair = new Pair(intRoomDomain[i],TimeslotDomain[j]);
                R_P_Pairs.add(_IJPair);
            }
        }
    }

    // 2. Assign Lectures  with R_P_Pairs
    // a. Constraint 1, Constraint 3, Constraint 4 relax
    private void generateLRPTriples()
    {
        // local variables 
        List<Pair> _R_P_Pairs = new ArrayList();
        // copy all objects from R_P_Pairs to local variable
        for (int i=0;i<R_P_Pairs.size();i++)
        {
            Pair IthPair = R_P_Pairs.get(i);

            _R_P_Pairs.add(new Pair(IthPair.getFirst(),IthPair.getSecond()));
        }
        

        List<String> _LecturesDomain = new ArrayList();
        // copy all objects from LecturesDomain to local variable
        for (int i=0;i<LecturesDomain.size();i++)
        {
            String IthString = LecturesDomain.get(i);
            _LecturesDomain.add(IthString);
        }

        
        boolean success= false;
        boolean timeout= false;
        int interation = 0;

        boolean HC1 = true; // AllLectures
        boolean HC2 = true; // RoomOccupancy
        boolean HC3 = true; // TeacherCurriculumConflicts
        boolean HC4 = true; // Availabilities

        boolean SC1 = true; // RoomCapacity
        boolean SC2 = true; // MinimumWorkingDays
        boolean SC3 = true; // CurriculumCompactness
        boolean SC4 = true; // RoomStability

        while (!success) // || !timeout
        {
            String curLecture = _LecturesDomain.get(0);

            for (int j=0;j<_R_P_Pairs.size();j++)
            {
                Pair _JthPair = _R_P_Pairs.get(j);

                if (HC2 && HC4 && SC1)
                {
                  Triple _IJTriple = new Triple(curLecture,_JthPair.getFirst(),_JthPair.getSecond()); // Assign

                  this.L_R_P_Triples.add(_IJTriple);

                  _LecturesDomain.remove(0);
                  _R_P_Pairs.remove(j);

                  //System.out.println("Dzo day j"+j+"  _R_P_Pairs.get(0)"+_R_P_Pairs.get(0).getFirst()+"-"+_R_P_Pairs.get(0).getSecond());
                  break;
                }
            }

            if (_LecturesDomain.size()==0) success = true; // HC1

            interation++;
            if (interation > 100000) break;
        }


        /*
        for (int i=0;i<_LecturesDomain.size();i++)
        {
            for (int j=0;j<R_P_Pairs.size();j++)
            {
                Pair _JthPair = R_P_Pairs.get(j);
                Triple _IJTriple = new Triple(LecturesDomain.get(i),_JthPair.getFirst(),_JthPair.getSecond());
            }
        }
         *
         */

        

    }


    /*
    public boolean generateFSol4Course(int index)
    {
        if (index >= CP_R_Solution.length || index<0) return false;
        

        Course course = this._inputReader.Courses.get(index);

        // 1. Allocate periods for this course
        //     LectureNum < PeriodsNum
        if (course.getLectureNum()>CP_R_Solution[index].length) return false;

        int allocatedLectures=0;
        int t=0;
        int last_allocated_t=-1;
        while (allocatedLectures < course.getLectureNum())
        {
            // check availability
            if (_inputReader.C_P_UnavailabilityMatrix[index][t]==1) t++;
            else
            {
                CP_R_Solution[index][t]= 0;
                last_allocated_t = t;
                allocatedLectures++;

                t++;

            }


        }


        return true;
    }



    public void printSolMatrix()
    {
        System.out.println("CP_R_Solution");
        for (int i=0;i< CP_R_Solution.length;i++)
        {
            System.out.print(_inputReader.Courses.get(i).getCourseID()+": ");

            for (int j=0;j< CP_R_Solution[i].length;j++)
            {
                System.out.print(CP_R_Solution[i][j]+" ");
            }

            System.out.println(" ");

        }
    }
     *
     * 
     */


}
