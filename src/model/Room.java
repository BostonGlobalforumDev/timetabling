/*
 * ROOMS:
 *  B	200
 *  C	100
 */

package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vimi Administrator
 *
 */
public class Room {
        public int index=-1;
    	private String RoomID;

	private int Capacity;

	

	public String getId() {
		return RoomID;
	}

	public void setId(String id) {
		this.RoomID = id;
	}

	public int getCapacity() {
		return Capacity;
	}

	public void setCapacity(int capacity) {
		this.Capacity = capacity;
	}


        public static ArrayList<Room> sortByCapacity(List<Room> rooms)
        {

            ArrayList<Room> _sortedRooms= new ArrayList<Room>();

            Object [] _rooms;


            if (rooms != null)
            {
                _rooms= rooms.toArray();

                for (int i=0;i<_rooms.length;i++)
                {
                    Room IthRoom=(Room) _rooms[i];
                    Object [] _sorted_rooms = _sortedRooms.toArray();

                    if (_sortedRooms.isEmpty()) _sortedRooms.add(IthRoom);
                    else
                    {
                        int index=-1;

                        System.out.println("Ith Room to add "+IthRoom.getId()+" "+IthRoom.getCapacity());

                        for (int j=0;j<_sorted_rooms.length;j++)
                        {
                            Room JthRoom=(Room) _sorted_rooms[j];

                            System.out.println("Jth Room: "+JthRoom.getId()+" "+JthRoom.getCapacity());

                            if (IthRoom.getCapacity()>=JthRoom.getCapacity())
                            {
                                index =j;
                                break;
                            }

                        }
                        System.out.println("Index: "+index);
                        if (index!=-1) _sortedRooms.add(index, IthRoom);
                        else _sortedRooms.add(IthRoom);
                    }
                     //_sortedCourses.put(IthCourse.getStudentNum(), IthCourse);
                     // myDic.put(IthCourse.getStudentNum(), IthCourse);
                }
            }

            return _sortedRooms;

        }

        public static void show(List<Room> rooms)
        {
            Object [] _rooms;
            int index =-1;

            if (rooms != null)
            {
                _rooms= rooms.toArray();

                for (int i=0;i<_rooms.length;i++)
                {
                    Room IthRoom=(Room) _rooms[i];
                    System.out.print(" "+IthRoom.getId()+" ");
                    System.out.println(" Capacity "+IthRoom.getCapacity()+" ");
                }
            }
        }


}
