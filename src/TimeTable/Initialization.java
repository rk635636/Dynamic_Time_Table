package TimeTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import JDBC.retrive_data;

public class Initialization {
	
	//this class takes all inputs from a file. courseID, courseName, roomID's, subjects and professors associated with course
	//currently hardcoded by taking one course with 6 subjects and 6 teachers
	
	private ArrayList<Subject> subjects=new ArrayList();
	private ArrayList<Professor> professors=new ArrayList();
	private ArrayList<TimeTable> timetables=new ArrayList();
	private ArrayList<Lecture> classes=new ArrayList<>();
	private ArrayList<Combination> combinations=new ArrayList<>();
	retrive_data a= new retrive_data(); 

	//reads input from a file.
	
	public void readInput(int classNo) throws IOException{
		int n;
		n = classNo;
		
		ArrayList<ClassRoom> classroom=new ArrayList<>();
//		ClassRoom room1 = new ClassRoom("D301", 20, false, "ComputerScience");
//		classroom.add(room1);
//		ClassRoom room2 = new ClassRoom("A-310", 20, false, "ComputerScience");
//		classroom.add(room2);
		String ProfessorData[][] = TimeTableMain.ProfessorData;
		String ClassRoomData[][] = TimeTableMain.ClassRoomData;
		String SubjectData[][] = TimeTableMain.SubjectData;
		int ProfessorRows = TimeTableMain.ProfessorRows;
		int Professorcolumns = TimeTableMain.Professorcolumns;
		int ClassRoomRows = TimeTableMain.ClassRoomRows;
		int ClassRoomColumns = TimeTableMain.ClassRoomColumns;
		int SubjectRows = TimeTableMain.SubjectRows;
		int SubjectColumns = TimeTableMain.SubjectColumns;


		if(n==2)
		{

			ClassRoom room3 = new ClassRoom(ClassRoomData[0][1], Integer.parseInt(ClassRoomData[0][2]), Boolean.parseBoolean(ClassRoomData[0][3]), ClassRoomData[0][4]);
			classroom.add(room3);
			ClassRoom room4 = new ClassRoom(ClassRoomData[1][1], Integer.parseInt(ClassRoomData[1][2]), Boolean.parseBoolean(ClassRoomData[1][3]), ClassRoomData[1][4]);
			classroom.add(room4);
		}
		else if(n==1)
		{
			ClassRoom room5 = new ClassRoom(ClassRoomData[2][1], Integer.parseInt(ClassRoomData[2][2]), Boolean.parseBoolean(ClassRoomData[2][3]), ClassRoomData[2][4]);
			classroom.add(room5);
			ClassRoom room6 = new ClassRoom(ClassRoomData[3][1], Integer.parseInt(ClassRoomData[3][2]), Boolean.parseBoolean(ClassRoomData[3][3]), ClassRoomData[3][4]);
			classroom.add(room6);

		}
		else if(n==3)
		{
			ClassRoom room1 = new ClassRoom(ClassRoomData[4][1], Integer.parseInt(ClassRoomData[4][2]), Boolean.parseBoolean(ClassRoomData[4][3]), ClassRoomData[4][4]);
			classroom.add(room1);
			ClassRoom room2 = new ClassRoom(ClassRoomData[0][1], Integer.parseInt(ClassRoomData[5][2]), Boolean.parseBoolean(ClassRoomData[5][3]), ClassRoomData[5][4]);
			classroom.add(room2);
		}
		
		
		for(int i=0;i<ProfessorRows;i++){
				professors.add(new Professor(Integer.parseInt(ProfessorData[i][0]),ProfessorData[i][1],ProfessorData[i][2] ));
		}


		
		createLectures(professors);
		
		TimeTable timetb1=new TimeTable(classroom, classes);//, professors);
		
//				
		if(n==2)
		{
			int courseid = 1;
			String courseName="TE2";
			System.out.println("reading input.......");

			String subCombination = SubjectData[0][1];
			for(int i=0;i<SubjectRows;i++){
				if(i!=0){
					subCombination = subCombination + "/" + SubjectData[i][1];
				}
				subjects.add(new Subject(Integer.parseInt(SubjectData[i][0]),SubjectData[i][1],Integer.parseInt(SubjectData[i][2]),Boolean.parseBoolean(SubjectData[i][3]),SubjectData[i][4]));
			}


				
			System.out.println("new course creation.......");
			Course course1 = new Course(courseid, courseName, subjects);

			course1.createCombination(subCombination, 60);
			course1.createStudentGroups();
			
			ArrayList<StudentGroups> studentGroups = course1.getStudentGroups();
			timetb1.addStudentGroups(studentGroups);
		}
		else if(n==1)
		{
			subjects.clear();
			
			int course = 2;
			String Name="TE1";
			System.out.println("reading input.......");
			String subCombination = SubjectData[0][1]+"1";
			for(int i=0;i<SubjectRows;i++){
				if(i!=0){
					subCombination = subCombination + "/" + SubjectData[i][1]+"1";
				}
				subjects.add(new Subject(Integer.parseInt(SubjectData[i][0]),SubjectData[i][1]+"1",Integer.parseInt(SubjectData[i][2]),Boolean.parseBoolean(SubjectData[i][3]),SubjectData[i][4]));
			}

				
			System.out.println("new course creation.......");
			Course course2 = new Course(course, Name, subjects);
			course2.createCombination(subCombination, 60);
			course2.createStudentGroups();
			
			ArrayList<StudentGroups> studentGroups1 = course2.getStudentGroups();
			timetb1.addStudentGroups(studentGroups1);
		}
		else if(n==3)
		{
			subjects.clear();
			
			int course = 3;
			String Name="TE3";
			System.out.println("reading input.......");
			String subCombination = SubjectData[0][1]+"2";
			for(int i=0;i<SubjectRows;i++){
				if(i!=0){
					subCombination = subCombination + "/" + SubjectData[i][1]+"2";
				}
				subjects.add(new Subject(Integer.parseInt(SubjectData[i][0]),SubjectData[i][1]+"2",Integer.parseInt(SubjectData[i][2]),Boolean.parseBoolean(SubjectData[i][3]),SubjectData[i][4]));
			}


				
			System.out.println("new course creation.......");
			Course course3 = new Course(course, Name, subjects);
			course3.createCombination(subCombination, 60);
			course3.createStudentGroups();
			
			ArrayList<StudentGroups> studentGroups1 = course3.getStudentGroups();
			timetb1.addStudentGroups(studentGroups1);
		}
		
		System.out.println("Setting tt.......");
		
		System.out.println("adding tt.......");
		timetb1.initializeTimeTable();
		timetables.add(timetb1);
		
				
		System.out.println("populating.......");
		
		
		
		//display();
		
		populateTimeTable(timetb1);
		GeneticAlgorithm ge=new GeneticAlgorithm();
		
		//ge.fitness(timetb1);
//		timetb1.createTimeTableGroups(combinations);
		ge.populationAccepter(timetables);

	}
	
	public void populateTimeTable(TimeTable timetb1){
		int i=0;
		System.out.println("populating started.......");
		while(i<3){
			TimeTable tempTimetable = timetb1;
			ArrayList<ClassRoom> allrooms = tempTimetable.getRoom();
			Iterator<ClassRoom> allroomsIterator = allrooms.iterator();
			while(allroomsIterator.hasNext()){
				ClassRoom room = allroomsIterator.next();
				ArrayList<Day> weekdays = room.getWeek().getWeekDays();
				Collections.shuffle(weekdays);
				if(!room.isLaboratory()){
					Iterator<Day> daysIterator=weekdays.iterator();
					while(daysIterator.hasNext()){
						Day day = daysIterator.next();
						Collections.shuffle(day.getTimeSlot());
					}
				}				
			}
			timetables.add(tempTimetable);
			i++;
		}
		System.out.println("populating done.......");
		System.out.println("display called.......");
		display();
	}
	
	private void createLectures (ArrayList<Professor> professors) {
		// TODO Auto-generated method stub
		
		java.util.Iterator<Professor> professorIterator=professors.iterator();
		while(professorIterator.hasNext()){
			Professor professor=professorIterator.next();
			ArrayList<String> subjectsTaught = professor.getSubjectTaught();
			Iterator<String> subjectIterator = subjectsTaught.iterator();
			while(subjectIterator.hasNext()){
				String subject = subjectIterator.next();
				classes.add(new Lecture (professor, subject));
			}
		}
	}
	
	
	private void display() {
		// TODO Auto-generated method stub
//		System.out.println("+++++++++++++++================++++++++++++++================         ++++++++++++++++++=================");
		int i=1;
		System.out.println("displaying all tt's.......");
		Iterator<TimeTable> timetableIterator = timetables.iterator();
		while(timetableIterator.hasNext()){
			System.out.println("+++++++++++++++++++++++++++++++++++++++++\nTime Table no. "+i);
			TimeTable currentTimetable = timetableIterator.next();
			System.out.println("Score : "+currentTimetable.getFittness());
			ArrayList<ClassRoom> allrooms = currentTimetable.getRoom();
			Iterator<ClassRoom> allroomsIterator = allrooms.iterator();
			while(allroomsIterator.hasNext()){
				ClassRoom room = allroomsIterator.next();
				System.out.println("Room: "+room.getRoomNo());
				ArrayList<Day> weekdays = room.getWeek().getWeekDays();
				Iterator<Day> daysIterator=weekdays.iterator();
				while(daysIterator.hasNext()){
					Day day = daysIterator.next();
					ArrayList<TimeSlot> timeslots = day.getTimeSlot();
					Iterator<TimeSlot> timeslotIterator= timeslots.iterator();
					//System.out.print(""+day.getName()+": ");
					while(timeslotIterator.hasNext()){
						TimeSlot lecture = (TimeSlot) timeslotIterator.next();
						if(lecture.getLecture()!=null){
						//System.out.print(" (Subject: "+lecture.getLecture().getSubject()+" --> Professor: "+lecture.getLecture().getProfessor().getProfessorName()+" GrpName: "+lecture.getLecture().getStudentGroup().getName()+")");
							System.out.print("("+lecture.getLecture().getSubject()+"#"+lecture.getLecture().getProfessor().getProfessorName()+"#"+lecture.getLecture().getStudentGroup().getName().split("/")[0]+")");
						}
						else{
							System.out.print("   free   ");
						}
					}
					System.out.print("\n");
				}
				System.out.print("\n\n");
			}
			i++;
		}
	}
}
