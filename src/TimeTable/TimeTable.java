package TimeTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class TimeTable {
	private ArrayList<ClassRoom> rooms = new ArrayList<ClassRoom>();
	private int fittness;
	private ArrayList<Lecture> classes=new ArrayList<>();
	private ArrayList<Day> da = new ArrayList<>();
	private ArrayList<Integer> rand_slot = new ArrayList<>();
	private ArrayList<ClassRoom> classs = new ArrayList<>();
	private ArrayList<StudentGroups> studentGroups=new ArrayList<>(); 
	private ArrayList<ClassRoom> practicalRooms = new ArrayList<ClassRoom>();
	private ArrayList<ClassRoom> theoryRooms = new ArrayList<ClassRoom>();
	private ArrayList<StudentGroups> theoryStudentGroups=new ArrayList<>(); 
	private ArrayList<StudentGroups> practicalStudentGroups=new ArrayList<>(); 
	private HashMap<Combination, Week> personalTimeTable= new HashMap<Combination, Week>();
	//private ArrayList<Professor> professors=new ArrayList<>();
	//adds more rooms to timetable

	public TimeTable(ArrayList<ClassRoom> classroom, ArrayList<Lecture> lectures){//, ArrayList<Professor> professors){
		this.rooms=classroom;
		this.classes=lectures;
		this.fittness=999;
//		this.professors=professors;
	}
	
//	public void initialization(ArrayList<ClassRoom> classroom, ArrayList<Lecture> lectures){
//		this.rooms=classroom;
//		this.classes=lectures;
//		this.fittness=999;
//	}
	
	public int getFittness() {
		return fittness;
	}
	
	public void setFittness(int fittness) {
		this.fittness = fittness;
	}

	public void addStudentGroups(ArrayList<StudentGroups> studentgrps) {
		// TODO Auto-generated method stub
		studentGroups.addAll(studentgrps);
	}
	
	public void initializeTimeTable(){
		for (Iterator<ClassRoom> roomsIterator = rooms.iterator(); roomsIterator.hasNext();) {
			ClassRoom room = roomsIterator.next();
			if(room.isLaboratory()){
				practicalRooms.add(room);
			}
			else{
				theoryRooms.add(room);
			}
		}
		for (Iterator<StudentGroups> studentGroupIterator = studentGroups.iterator(); studentGroupIterator.hasNext();) {
			StudentGroups studentGroup = studentGroupIterator.next();
			if(studentGroup.isPractical()){
				practicalStudentGroups.add(studentGroup);
				//System.out.println("======+++++++++PRACT =================");
			}
			else{
				theoryStudentGroups.add(studentGroup);
				//System.out.println("======+++++++++THER =================");
			}
		}
		rooms.clear();
		//studentGroups.clear();
		setTimeTable(practicalStudentGroups, practicalRooms, "practical");
		setTimeTable(theoryStudentGroups, theoryRooms, "theory");
		rooms.addAll(practicalRooms);
		rooms.addAll(theoryRooms);
		//studentGroups.addAll(practicalStudentGroups);
		//studentGroups.addAll(theoryStudentGroups);
	}
	
	public void setTimeTable(ArrayList<StudentGroups> studentGroups2, ArrayList<ClassRoom> rooms2, String string) {
		// TODO Auto-generated method stub
		Collections.shuffle(studentGroups2);
		Stack<Lecture> lecturesStack=new Stack<Lecture>();
		for (Iterator<StudentGroups> sdtGrpIterator = studentGroups2.iterator(); sdtGrpIterator.hasNext();) {			
			StudentGroups studentGrp = sdtGrpIterator.next();
			String subject = studentGrp.getSubjectName();
			System.out.println("======+++++++++SUB = "+subject);
			int noOfLectures = studentGrp.getNoOfLecturePerWeek();
			for(int i=0; i<noOfLectures; i++){
				Collections.shuffle(classes);
				Iterator<Lecture> classIterator = classes.iterator();
				while(classIterator.hasNext()){
					Lecture lecture = classIterator.next();
					if(lecture.getSubject().equalsIgnoreCase(subject)){
						Lecture mainLecture=new Lecture(lecture.getProfessor(), lecture.getSubject());
						mainLecture.setStudentGroup(studentGrp);
						lecturesStack.push(mainLecture);
						//System.out.println("======+++++++++MAIN = "+mainLecture.getSubject()+"+++++++++++"+mainLecture.getProfessor().getProfessorName());
						break;
					}
				}
			}
		}
		while(!(lecturesStack.empty())){
				Collections.shuffle(lecturesStack);
				Lecture lecture2 = lecturesStack.pop();
//				lecture2.setSubject("L1");
//				lecture2.setSubject("P1");
				
				if(string.equalsIgnoreCase("theory")){
					placeTheoryLecture(lecture2, rooms2);
					
					System.out.println("======++++++++========+TH+++============");
				}
				if(string.equalsIgnoreCase("practical")){
					placePracticalLecture(lecture2, rooms2);
//					pract(classs);
					//System.out.println("======++++++++========+PP+++============");
				}
		}	
	}	
	
		
	
	private void placePracticalLecture(Lecture lecture2, ArrayList<ClassRoom> rooms2) {
		// TODO Auto-generated method stub
		int size = lecture2.getStudentGroup().getSize();
		String dept=lecture2.getStudentGroup().getDepartment();
		int i=0;
		boolean invalid=true;
		ClassRoom room = null;
		Collections.shuffle(rooms2);
		while(invalid){
		room=getBestRoom(size, rooms2);
		if(!classs.contains(room))
		{
			classs.add(room);
		}
		if(room.getDepartment().equalsIgnoreCase(dept)){
			invalid=false;
			Collections.shuffle(rooms2);
			}
		else{
			Collections.shuffle(rooms2);
			}
		}
		ArrayList<Day> weekdays = room.getWeek().getWeekDays();
		//Collections.shuffle(weekdays);
		Iterator<Day> daysIterator=weekdays.iterator();
		int rand;
		while(daysIterator.hasNext())
		{
			Day day = daysIterator.next();
			
			if(!da.contains(day))
			{
				da.add(day);
				ArrayList<TimeSlot> timeslots = day.getTimeSlot();
				//System.out.println("+++++++++==============SIZE = "+timeslots.size());
				Collections.shuffle(timeslots);
				TimeSlot lecture3 = timeslots.get(rand());
				lecture3.setLecture(lecture2);
				break;
			}
			else
			{
				continue;
			}
			
		}
		
		
	
	}
	
//	private void pract(ArrayList<ClassRoom> cla)
//	{
//		Iterator<ClassRoom> itr = cla.iterator();
//		while(itr.hasNext())
//		{
//			ClassRoom c = itr.next();
//			ArrayList<Day> weekdays = c.getWeek().getWeekDays();
//			Iterator<Day> daysIterator=weekdays.iterator();
//			while(daysIterator.hasNext()){
//				Day day = daysIterator.next();
//				ArrayList<TimeSlot> timeslots = day.getTimeSlot();
//				Iterator<TimeSlot> timeslotIterator= timeslots.iterator();
//				int i=-1;
//				while(timeslotIterator.hasNext()){
//					i++;
//					TimeSlot lecture2 = timeslotIterator.next();
//					if(lecture2.getLecture()!=null){
//					rand_slot.add(i);			
//					}
//					
//				}
//			}
//			
//		}
//	}

	private int rand()
	{
		int r;
		
		while(true)
		{
			r = (int)(Math.random()*5 + 0);
			if((r%2)!=0)
			{
				continue;
			}
			else
			{
				break;
			}
		}
		rand_slot.add(r);
		return r;
		
	}

	private void placeTheoryLecture(Lecture lecture, ArrayList<ClassRoom> rooms2) {
		// TODO Auto-generated method stub
		int size = lecture.getStudentGroup().getSize();
		String dept=lecture.getStudentGroup().getDepartment();
		boolean invalid=true;
		ClassRoom room = null;
		Collections.shuffle(rooms2);
		while(invalid){
			room=getBestRoom(size, rooms2);
			if(room.getDepartment().equalsIgnoreCase(dept)){
				invalid=false;
				Collections.shuffle(rooms2);
				}
			else{
				Collections.shuffle(rooms2);
				}
			}
		
//		{
//			ArrayList<Day> weekdays1 = room.getWeek().getWeekDays();
//			Iterator<Day> daysIterator=weekdays1.iterator();
//			int i=0;
//			while(daysIterator.hasNext())
//			{
//				Day day = daysIterator.next();
//				ArrayList<TimeSlot> timeslots = day.getTimeSlot();
//				TimeSlot lect = timeslots.get(rand_slot.get(i));
//				System.out.println("+++++++++++=========++++++++++SLOT = "+rand_slot.get(i));
//				//lect.setLecture(new Lecture((new Professor(1,"shintre","TOC")),"TOC"));
//				i++;
//			}
//			
//		}
		
		ArrayList<Day> weekdays = room.getWeek().getWeekDays();
		Iterator<Day> daysIterator=weekdays.iterator();
		while(daysIterator.hasNext()){
			Day day = daysIterator.next();
			ArrayList<TimeSlot> timeslots = day.getTimeSlot();
//			Collections.shuffle(timeslots);
			Iterator<TimeSlot> timeslotIterator= timeslots.iterator();
			int i=0;
			while(timeslotIterator.hasNext()){
				i++;
				TimeSlot lecture2 = timeslotIterator.next();
				if(lecture2.getLecture()==null){
				lecture2.setLecture(lecture);
				return;				
				}
//				if(i==4)
//				{
//					break;
//				}
			}
		}		
	}



	private boolean checkOccupiedRoom(ClassRoom tempRoom, ArrayList<ClassRoom> rooms2) {
		// TODO Auto-generated method stub
		for (Iterator<ClassRoom> roomsIterator = rooms2.iterator(); roomsIterator.hasNext();){
			ClassRoom room = roomsIterator.next();
			if(room.equals(tempRoom)){
			ArrayList<Day> weekdays = room.getWeek().getWeekDays();
			Iterator<Day> daysIterator=weekdays.iterator();
			while(daysIterator.hasNext()){
				Day day = daysIterator.next();
				ArrayList<TimeSlot> timeslots = day.getTimeSlot();
				Iterator<TimeSlot> timeslotIterator= timeslots.iterator();
				while(timeslotIterator.hasNext()){
					TimeSlot lecture = timeslotIterator.next();
					if(lecture.getLecture()==null){
						return false;
					}
				}
			}
			return true;
			}		
		}
		return false;
	}



	private ClassRoom getBestRoom(int size, ArrayList<ClassRoom> rooms2) {
		// TODO Auto-generated method stub
		int delta = 1000;
		ClassRoom room = null;
		for (Iterator<ClassRoom> roomsIterator = rooms2.iterator(); roomsIterator.hasNext();){
			ClassRoom tempRoom = roomsIterator.next();
			if(!checkOccupiedRoom(tempRoom, rooms2)){
		        int tmp = Math.abs(size - tempRoom.getSize());
		        if(tmp < delta){
		            delta = tmp;
		            room = tempRoom;
		    }
			}
		}
		return room;
	}

	public ArrayList<ClassRoom> getRoom() {
		return rooms;
	}

	public void setRoom(ArrayList<ClassRoom> room) {
		this.rooms = room;
	}



	public ArrayList<ClassRoom> getPracticalRooms() {
		return practicalRooms;
	}



	public void setPracticalRooms(ArrayList<ClassRoom> practicalRooms) {
		this.practicalRooms = practicalRooms;
	}



	public ArrayList<ClassRoom> getTheoryRooms() {
		return theoryRooms;
	}



	public void setTheoryRooms(ArrayList<ClassRoom> theoryRooms) {
		theoryRooms = theoryRooms;
	}



	public ArrayList<StudentGroups> getTheoryStudentGroups() {
		return theoryStudentGroups;
	}



	public void setTheoryStudentGroups(ArrayList<StudentGroups> theoryStudentGroups) {
		this.theoryStudentGroups = theoryStudentGroups;
	}



	public ArrayList<StudentGroups> getPracticalStudentGroups() {
		return practicalStudentGroups;
	}



	public void setPracticalStudentGroups(ArrayList<StudentGroups> practicalStudentGroups) {
		this.practicalStudentGroups = practicalStudentGroups;
	}
}
