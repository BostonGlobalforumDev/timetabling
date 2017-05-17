/*
 * Data preprocessing
 */

/**
 *
 * @author vimi Administrator
 *  Dinh Ngoc 10.08.2010
 *
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import model.Room;
import model.Course;
import model.Curriculum;
import model.Teacher;

import org.apache.log4j.Logger;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class InputReader {
    static Logger logger = Logger.getLogger(InputReader.class.getName());
    private static final EtmMonitor class_etmMonitor = EtmManager.getEtmMonitor();

    private String data_part="";
    public String InputFileName;

    // -------------------
    // 1. Header of ctt file
    // -------------------
    public String DataSetName;
    public int CNum=0; // Number of courses
    public int RNum=0; // Number of rooms
    public int DNum=0; // Number of days will be allocated
    public int Periods_per_day=0; // Number of timeslots per day

    public int CurriculaNum=0;
    public int ConstraintNum=0;

    // ----------------------
    // 2. internal structures contain data about courses, rooms, days, constraints, sequences of courses in curricurla
    // ----------------------
    public List<Course> Courses;
    private List<String> Teachers;
    public List<Room> Rooms;


    public List<Curriculum> Curricula;
    public SimpleDirectedGraph CurriculumGraph;


    // -------------------------------------------------
    // 3. encoded structures present domain data structures in a different covenient way,
    // so that timetabling solving algorithms can apply
    // -------------------------------------------------
    public int[][] HardCurriculum_Matrix; // qxq
    public int[][] SoftCurriculum_Matrix; // qxq
    
    private int[][] Course_Teacher_Matrix; // qxt
    public int[][] Courses_waTeacher_Matrix; // qxq


    public int[][] C_P_UnavailabilityMatrix; // q x p

    public int[][] C_R_Capacity_Assignment; // to explore compatibility between course with number of student and a room

    //public int[] C_R_MatchingMatrix; // answer, if a course requires a specific room for its lectures, only for extended ctt format
    //public int[][] C_R_MatchingMatrix2; // answer, which room does this course require for its lectures, only for extended ctt format

    


    public InputReader(String InputFileName)
    {
        this.InputFileName = InputFileName;

        this.Courses = new ArrayList<Course>();
        this.Rooms = new ArrayList<Room>();
        this.Teachers = new ArrayList<String>();

        importData();

    }


    // Utilities to import data
    private void importData()
    {
        try {
			BufferedReader in = new BufferedReader(new FileReader(InputFileName));
			int CCount = -1, RCount = -1;
			String str;

                        int blankLineNum = 0;

			while ((str = in.readLine()) != null) {
                                if (str.length()==0)
                                {
                                    System.out.println(blankLineNum++);
                                    data_part = "NONE";
                                }

                                // processing header
				if (str.indexOf("Name:") != -1)
					processDataNameHeader(str);             
                                else if (str.indexOf("Courses:") != -1)
                                        processCourseNumHeader(str);
                                else if (str.indexOf("Rooms:") != -1)
                                        processRoomsNumHeader(str);
                                else if (str.indexOf("Days:") != -1)
                                        processDaysNumHeader(str);
                                else if (str.indexOf("Periods_per_day:") != -1)
                                        processPpDNumHeader(str);
                                else if (str.indexOf("Curricula:") != -1)
                                        processCurriculaNumHeader(str);
                                else if (str.indexOf("Constraints:") != -1)
                                        processConstraintNumHeader(str);
                                
                                // retrieve data about courses
                                // else if (str.length()==0) data_part="";
                                
                                else if (str.indexOf("COURSES:") != -1)
                                        data_part = "COURSES";
                                else if (str.indexOf("ROOMS:") != -1)
                                        data_part = "ROOMS";
                                else if (str.indexOf("CURRICULA:") != -1)
                                        data_part = "CURRICULA";
                                else if (str.indexOf("UNAVAILABILITY_CONSTRAINTS:") != -1)
                                        data_part = "UNAVAILABILITY_CONSTRAINTS";

                                else if (data_part.equals("COURSES"))
                                        processActualCourse(str);
                                else if (data_part.equals("ROOMS"))
                                        processActualRoom(str);
                                else if (data_part.equals("CURRICULA"))
                                {
                                    //String str1= str;
                                    //while ((str1 = in.readLine()) != null && str1.length()>0) {
                                        calcCurriculumMatrix(str);
                                    //}
                                }
                                else if (data_part.equals("UNAVAILABILITY_CONSTRAINTS"))
                                {
                                        calcC_P_UnavailabilityMatrix(str);
                                }
                               
                   

                                else if (str.indexOf("END.") != -1)
                                        break;


                                else {
					logger.debug(str);
				}
			}// End of while
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

    private void initMatrices()
    {
        HardCurriculum_Matrix = new int[CNum][CNum]; // q x q
        // fill Curriculum Matrix with 0
        for (int i=0;i< HardCurriculum_Matrix.length;i++)
        {
            for (int j=0;j< HardCurriculum_Matrix[i].length;j++)
            {
                HardCurriculum_Matrix[i][j]=0;
            }
        }

        SoftCurriculum_Matrix = new int[CNum][CNum]; // q x q
        // fill Curriculum Matrix with 0
        for (int i=0;i< SoftCurriculum_Matrix.length;i++)
        {
            for (int j=0;j< SoftCurriculum_Matrix[i].length;j++)
            {
                SoftCurriculum_Matrix[i][j]=0;
            }
        }



        C_P_UnavailabilityMatrix = new int[CNum][DNum*Periods_per_day];
        // fill C_P_UnavailabilityMatrix with 0
        for (int i=0;i< C_P_UnavailabilityMatrix.length;i++)
        {
            for (int j=0;j< C_P_UnavailabilityMatrix[i].length;j++)
            {
                C_P_UnavailabilityMatrix[i][j]=0;
            }
        }

        C_R_Capacity_Assignment = new int[CNum][RNum]; // q x r
        // fill Capacity_Assignment between courses and rooms with 0
        for (int i=0;i< C_R_Capacity_Assignment.length;i++)
        {
            for (int j=0;j< C_R_Capacity_Assignment[i].length;j++)
            {
                C_R_Capacity_Assignment[i][j]=0;
            }
        }


        initCourseTeacherMatrix();

    }

    private void initCourseTeacherMatrix()
    {
        Course_Teacher_Matrix = new int[CNum][CNum]; // q x q
        // fill Curriculum Matrix with 0
        for (int i=0;i< Course_Teacher_Matrix.length;i++)
        {
            for (int j=0;j< Course_Teacher_Matrix[i].length;j++)
            {
                Course_Teacher_Matrix[i][j]=0;
            }
        }


        Courses_waTeacher_Matrix = new int[CNum][CNum]; // q x q
        // fill Curriculum Matrix with 0
        for (int i=0;i< Courses_waTeacher_Matrix.length;i++)
        {
            for (int j=0;j< Courses_waTeacher_Matrix[i].length;j++)
            {
                Courses_waTeacher_Matrix[i][j]=0;
            }
        }

    }

    private void processDataNameHeader(String str)
    {
        String s[] = str.split("\\s+");
	//String field = Integer.parseInt(s[0].trim());
	this.DataSetName = s[1].trim();
        
        logger.info("Name of DataSet loaded");
	
    }
    private void processCourseNumHeader(String str)
    {
        String s[] = str.split("\\s+");
	//String field = Integer.parseInt(s[0].trim());
	this.CNum = Integer.parseInt(s[1].trim());

        logger.info("Number of courses loaded");

    }
    private void processRoomsNumHeader(String str)
    {
        String s[] = str.split("\\s+");
	//String field = Integer.parseInt(s[0].trim());
	this.RNum = Integer.parseInt(s[1].trim());

        logger.info("Number of rooms loaded");
    }
    private void processDaysNumHeader(String str)
    {
        String s[] = str.split("\\s+");
	//String field = Integer.parseInt(s[0].trim());
	this.DNum = Integer.parseInt(s[1].trim());

        logger.info("Number of days loaded");
    }
    private void processPpDNumHeader(String str)
    {
        String s[] = str.split("\\s+");
	//String field = Integer.parseInt(s[0].trim());
	this.Periods_per_day = Integer.parseInt(s[1].trim());

        logger.info("Number of periods per day loaded");
    }
    private void processCurriculaNumHeader(String str)
    {
        String s[] = str.split("\\s+");
	//String field = Integer.parseInt(s[0].trim());
	this.CurriculaNum = Integer.parseInt(s[1].trim());

        logger.info("Number of curricula loaded");
    }
    private void processConstraintNumHeader(String str)
    {
        String s[] = str.split("\\s+");
	//String field = Integer.parseInt(s[0].trim());
	this.ConstraintNum = Integer.parseInt(s[1].trim());

        logger.info("Number of constraints loaded");

        initMatrices();
        logger.info("Matrices created");
    }

    private void processActualCourse(String str)
    {
        String s[] = str.split("\\s+");

        if (s.length==5)
        {
        Course c = new Course();
        c.setCourseID(s[0].trim());
        c.setTeacher(s[1].trim());
        processTeacherinActualCourse(s[1].trim());
        c.setLectureNum(Integer.parseInt(s[2].trim()));
        c.setMinWorkingDays(Integer.parseInt(s[3].trim()));
        c.setStudentNum(Integer.parseInt(s[4].trim()));

        c.index = this.Courses.toArray().length;

        this.Courses.add(c);


        if (Courses.size()>0 && Teachers.size()>0)
        {
            Course_Teacher_Matrix[Courses.size()-1][Teachers.indexOf(c.getTeacher())]=1;
        }

        logger.info("a Course added successfully");


        } else
        {
            logger.info("course record: format error");
        }
 
    }

    private void processTeacherinActualCourse(String teacher)
    {
        if (!Teachers.contains(teacher)) Teachers.add(teacher);
        // else System.out.println(teacher+" co roi");



    }

    private void processActualRoom(String str)
    {
        String s[] = str.split("\\s+");

        

        //System.out.println("S.length "+s.length);
        if (s.length >=2)
        {
            
            Room r = new Room();
            r.setId(s[0].trim());
            r.setCapacity(Integer.parseInt(s[1].trim()));

            r.index = this.Rooms.toArray().length;
            this.Rooms.add(r);

            logger.info("a Room added successfully");


        } else
        {
            logger.info("room record: format error");
        }

    }

    // q000  4 c0001 c0002 c0004 c0005
    private void calcCurriculumMatrix(String curricula)
    {
        String s[] = curricula.split("\\s+");
        int elementNum = Integer.parseInt(s[1].trim()); // 4

        if (s.length >2)
        {
            int i=0;
            int [] CurriculaRecord= new int[elementNum];

            // calculate array for this actual curricula record
            while (i<elementNum)
            {
                
                String IthElement = s[2+i];
                int IthIndex= Course.getPosofID(Courses, IthElement);

                CurriculaRecord[i]= IthIndex;

                i++;
            }

            // fill in the Curriculum Matrix with 1 at the suitable position
            for(int j=0;j<CurriculaRecord.length;j++)
            {
                int ElementIndex = CurriculaRecord[j];

                for(int m=0;m<CurriculaRecord.length;m++)
                {
                    if (m!=j)
                    {
                        int q1= ElementIndex;
                        int q2= CurriculaRecord[m];
                        if (SoftCurriculum_Matrix[q1][q2]!=0) System.out.println("2 same courses belong to the same curriculum at"+q1+":"+q2);
                        HardCurriculum_Matrix[q1][q2]=1;
                        SoftCurriculum_Matrix[q1][q2]=m-j;
                    }

                }
                
            }

            logger.info("curricula calculated successfully");

        } else
        {
            logger.info("curricula record: format error");
        }

    }

    // c0001 4 0
    private void calcC_P_UnavailabilityMatrix(String UnavailabilityRecord)
    {
        String s[] = UnavailabilityRecord.split("\\s+");

        if (s.length ==3)
        {
            int CourseIndex = Course.getPosofID(Courses, s[0].trim());
            int TimeslotIndex = Integer.parseInt(s[1].trim())*Periods_per_day+Integer.parseInt(s[2].trim());

            if (CourseIndex< C_P_UnavailabilityMatrix.length )
            {
              if (TimeslotIndex< C_P_UnavailabilityMatrix[0].length)
              {
                  C_P_UnavailabilityMatrix[CourseIndex][TimeslotIndex]=1;
                  logger.info("Unavailability record calculated successfully");
              }
            } 

        } else
        {
            logger.info("Unavailability record: format error");
        }

    }


    public void calcC_R_Capacity_Constraint()
    {


        Object [] _courses;
        Object [] _rooms;

        int index =-1;

        if (!Courses.isEmpty()&&!Rooms.isEmpty())
        {
            _courses= Courses.toArray();
            _rooms= Rooms.toArray();

            for (int i=0;i<_courses.length;i++)
            {
                Course IthCourse=(Course) _courses[i];

                int StudentNum = IthCourse.getStudentNum();
                for (int j=0;j<_rooms.length;j++)
                {
                    Room JthRoom=(Room) _rooms[j];
                    if (JthRoom.getCapacity()>=StudentNum)
                    {
                        C_R_Capacity_Assignment[i][j] = 1;
                    }
                }
                

            }
        }
        

    }


    private void printCourses_Teacher_Matrix()
    {
        System.out.println("Course_Teacher_Matrix");

        for (int i=0;i< Course_Teacher_Matrix.length;i++)
        {
            System.out.print(Courses.get(i).getCourseID()+": ");

            for (int j=0;j< Teachers.size();j++)
            {
                if (Course_Teacher_Matrix[i][j]==1) System.out.print("("+Teachers.get(j)+")");
                System.out.print(Course_Teacher_Matrix[i][j]+" ");

            }

            System.out.println(" ");

        }
    }

    public void calcCourses_waTeacher_Matrix()
    {
        printCourses_Teacher_Matrix();

        ArrayList<Integer> MemList = new ArrayList<Integer>();
        MemList.ensureCapacity(this.Teachers.size());

        for (int i=0;i<this.Teachers.size();i++)
        {
            MemList.clear();
            for (int j=0;j<this.Course_Teacher_Matrix.length;j++)
            {
                if (Course_Teacher_Matrix[j][i]==1)
                {
                    MemList.add(new Integer(j));
                    //System.out.println("Moi add "+MemList.get(MemList.size()-1));
                }
            }

            

            for (int m=0;m<MemList.size();m++)
            {
                int mthCourse = MemList.get(m).intValue();
                System.out.print("  --MemList: Course "+Courses.get(mthCourse).getCourseID()+"   mthCourse "+mthCourse+" ");

                for (int k=0;k<MemList.size();k++)
                {
                    int kthCourse = MemList.get(k).intValue();
                    if (mthCourse!= kthCourse)
                    {
                        this.Courses_waTeacher_Matrix[mthCourse][kthCourse]=1;
                        //this.Courses_waTeacher_Matrix[kthCourse][mthCourse]=1;
                    }
                }

            }

            System.out.println();
        }
    }

}
