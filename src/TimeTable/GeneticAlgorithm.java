																																																																								package TimeTable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.util.*;
import java.sql.*;
public class GeneticAlgorithm {	
	
	//private int numberOfRooms;
	//private ArrayList <String> subjects=new ArrayList();
	//private ArrayList <ClassRoom> rooms=new ArrayList();
	// HashMap<Integer,TimeTable> ttscore=new HashMap();
	private  TimeTable GlobalBestTimetable;
	private  int min=1000;
	private  ArrayList<String>weekDayNames=new ArrayList<>();
	private  ArrayList<String>lectureTimings=new ArrayList<>();
	private  ArrayList<Integer> slot = new ArrayList<>();
	private  ArrayList<Lecture> TheoryLect = new ArrayList<>();
	private  Stack<Lecture> st = new Stack<Lecture>();
	private  Stack<Lecture> st1 = new Stack<Lecture>();
	
	public  void populationAccepter(ArrayList<TimeTable> timeTableCollection) throws IOException{
		// randomly got population from the initialization class
		Iterator<TimeTable> timetableIterator=timeTableCollection.iterator();		
		for (Iterator<TimeTable> iterator = timeTableCollection.iterator(); iterator.hasNext();) {
			TimeTable tt = iterator.next();
				fitness(tt);			
		}		
		createWeek();
		createLectureTime();
		selection(timeTableCollection);		
	}
	
	private  void createWeek(){
		String[] weekDaysName=new DateFormatSymbols().getWeekdays();
		for (int i = 1; i < weekDaysName.length; i++) {
	        System.out.println("weekday = " + weekDaysName[i]);
	    	//if(!(weekDaysName[i].equalsIgnoreCase("Sunday"))){
	    	if (!(i == Calendar.SUNDAY) && !(i == Calendar.SATURDAY))
	    	weekDayNames.add(weekDaysName[i]);
	    		}
	    	}
	
	private  void createLectureTime(){
//		for(int i=9; i<16; i++){
//			//if(i!=12){
//				lectureTimings.add(i+":00"+" TO "+(i+1)+":00");
//			//}			
//		}
		
		lectureTimings.add(8+":00"+" TO "+(9)+":00");
		lectureTimings.add(9+":00"+" TO "+(10)+":00");
		lectureTimings.add(10+":15"+" TO "+(11)+":15");
		lectureTimings.add(10+":15"+" TO "+(12)+":15");
		lectureTimings.add(1+":00"+" TO "+(2)+":00");
		lectureTimings.add(2+":00"+" TO "+(3)+":00");
	}

	public  void selection(ArrayList<TimeTable> timetables) throws IOException{
		int iterations=50;
		int i=1;
		ArrayList<TimeTable> mutants=new ArrayList<>();
		Iterator<TimeTable> ttItr=timetables.iterator();
		while(ttItr.hasNext()){
			fitness(ttItr.next());
		}
		while(iterations!=0){
		//Iterator<Integer> scoreIterator=ttscore.keySet().iterator();
			Iterator<TimeTable> timetableIterator=timetables.iterator();
			//Iterator<TimeTable> tempIterator=timetableIterator;		
		//min= timetableIterator.next().getFittness();
		
		while (timetableIterator.hasNext()) {
			TimeTable tt = timetableIterator.next();
			int score=tt.getFittness();		
			if(score<min){
				min=score;
				GlobalBestTimetable=tt;
				display();
				//placeLectures();
				//display();
				writeToExcelFile();
				
			}			
			}
		
		if(min==0){
			//ArrayList<TimeTable> timeTable=new ArrayList();
			//timeTable.add(GlobalBestTimetable);
			display();
			System.exit(0);
		}
		else{
			System.out.println("Iteration :"+i);
			i++;
			iterations--;			
		//timetables.remove(GlobalBestTimetable);			
			for (Iterator<TimeTable> iterator = timetables.iterator(); iterator.hasNext();) {
				TimeTable timetable1 = iterator.next();
				//TimeTable timetable2 = (TimeTable) iterator.next();				
//				SingleTimeTable timetable1=ttscore.get(key1);
//				SingleTimeTable timetable2=ttscore.get(key2);				
				TimeTable childTimetable=crossOver(timetable1);	
//				if(childTimetable.getFittness()< GlobalBestTimetable.getFittness()){
//					GlobalBestTimetable=childTimetable;
//				}	
//				for (int j = 0; j < arr.length; j++) {
//					TimeTable singleTimeTable = arr[j];					
					TimeTable mutant=Mutation(childTimetable);
//					if(childTimetable.getFittness()< GlobalBestTimetable.getFittness()){
//						GlobalBestTimetable=childTimetable;
//					}
					mutants.add(mutant);
				//}		
			}			
			
			timetables.clear();			
			for (int j = 0; j < mutants.size(); j++) {
				fitness(mutants.get(j));
				timetables.add(mutants.get(j));
			}
			mutants.clear();
		}	
		}		
		display();
	}
	
	public  void fitness(TimeTable timetable){		
		ArrayList<ClassRoom> rooms=timetable.getRoom();
		Iterator<ClassRoom> roomIterator1 = rooms.iterator();		
		while(roomIterator1.hasNext()){			
			int score=0;
			ClassRoom room1 = roomIterator1.next(); 
			Iterator<ClassRoom> roomIterator2 = rooms.iterator();
			while(roomIterator2.hasNext()){		
				ClassRoom room2 = roomIterator2.next();
				if(room2!=room1){
					ArrayList<Day> weekdays1 = room1.getWeek().getWeekDays();
					ArrayList<Day> weekdays2 = room2.getWeek().getWeekDays();
					Iterator<Day> daysIterator1=weekdays1.iterator();
					Iterator<Day> daysIterator2=weekdays2.iterator();
					while(daysIterator1.hasNext() && daysIterator2.hasNext()){
						Day day1 = daysIterator1.next();
						Day day2 = daysIterator2.next();
						ArrayList<TimeSlot> timeslots1 = day1.getTimeSlot();
						ArrayList<TimeSlot> timeslots2 = day2.getTimeSlot();
						Iterator<TimeSlot> timeslotIterator1= timeslots1.iterator();
						Iterator<TimeSlot> timeslotIterator2= timeslots2.iterator();
						while(timeslotIterator1.hasNext() && timeslotIterator2.hasNext()){
							TimeSlot lecture1=timeslotIterator1.next();
							TimeSlot lecture2=timeslotIterator2.next();							
							if(lecture1.getLecture()!=null  &&  lecture2.getLecture()!=null){
//							String subject1=lecture1.getLecture().getSubject();
//							String subject2=lecture2.getLecture().getSubject();							
							String professorName1=lecture1.getLecture().getProfessor().getProfessorName();
							String professorName2=lecture2.getLecture().getProfessor().getProfessorName();							
							String stgrp1=lecture1.getLecture().getStudentGroup().getName();
							String stgrp2=lecture2.getLecture().getStudentGroup().getName();							
							if(stgrp1.equals(stgrp2) || professorName1.equals(professorName2)){
								score=score+1;
							}
								ArrayList<Combination> stcomb1 = lecture1.getLecture().getStudentGroup().getCombination();
								Iterator<Combination> stcombItr = stcomb1.iterator();
								while(stcombItr.hasNext()){
									if(lecture2.getLecture().getStudentGroup().getCombination().contains(stcombItr.next())){
										score = score+1;
										break;
									}
								}
							
							}
						}
					}
				}
			}
			timetable.setFittness(score);			
			//ttscore.put(score,timetable);
			//System.out.println("\nScore : "+score);
			}
		System.out.println("Score..................................."+timetable.getFittness());
//		Iterator iterator = ttscore.keySet().iterator(); 
//		while (iterator.hasNext()) {  
//			   ClassRoom key = (ClassRoom) iterator.next();  
//			   int value = (int) ttscore.get(key);  
//			   
//			   System.out.println("\nScore : "+value);  
//			}  
	}

	private  TimeTable Mutation(TimeTable parentTimetable) {		
		TimeTable mutantTimeTable=parentTimetable;
		int rnd1,rnd2;
		Random randomGenerator = new Random();
		ArrayList<ClassRoom> presentClassroom=mutantTimeTable.getRoom();
		for (Iterator<ClassRoom> iterator = presentClassroom.iterator(); iterator.hasNext();) {
			ClassRoom classRoom = iterator.next();			
			//for (Iterator <Day> iterator2 = classRoom.getWeek().getWeekDays().iterator(); iterator2.hasNext();) {
				
				// i have got the two days here which i have to exchange... but wat i actually 
				//want to shuffle is not the days but the schedule for the day!				
				rnd1=randomGenerator.nextInt(5);
				rnd2=-1;
				while(rnd1!=rnd2){
					rnd2=randomGenerator.nextInt(5);
				}
				ArrayList<Day> weekDays = classRoom.getWeek().getWeekDays();
				Day day1=weekDays.get(rnd1);
				Day day2=weekDays.get(rnd2);
				
				
				ArrayList<TimeSlot> timeSlotsOfday1=day1.getTimeSlot();
				ArrayList<TimeSlot> timeSlotsOfday2=day2.getTimeSlot();
				
				day1.setTimeSlot(timeSlotsOfday2);
				day2.setTimeSlot(timeSlotsOfday1);
				
				// if i am limiting this to two days i am breaking out... 
				//or else all the days will get exchanged in a sorted order
				//like monday-tue,wed thu,fri sat in pairs!
				break;
			//}			
		}		
		// apply repairstrategy here! check whether mutant 
		//better than parent and vice versa and choose the best		
		return mutantTimeTable;	
	}

	private  TimeTable crossOver(TimeTable fatherTimeTable){
		// let us say that we give father the priority to stay as the checker!
		// in the outer loop		
		Random randomGenerator = new Random();
		Iterator<ClassRoom> parentTimeTableClassRooms=fatherTimeTable.getRoom().iterator();		
		while(parentTimeTableClassRooms.hasNext()) {
			ClassRoom room = parentTimeTableClassRooms.next();
			if(!room.isLaboratory()){
				ArrayList<Day> days = room.getWeek().getWeekDays();
				int i=0;
				while(i<3){
					int rnd=randomGenerator.nextInt(5);
					Day day = days.get(rnd);
					Collections.shuffle(day.getTimeSlot());
					i++;
				}			
			}
			
		}
		return fatherTimeTable;
	}
	private  void getLectures()
	{
		ArrayList<ClassRoom> allrooms = GlobalBestTimetable.getRoom();
		Iterator<ClassRoom> allroomsIterator = allrooms.iterator();
		while(allroomsIterator.hasNext())
		{
			ClassRoom room = allroomsIterator.next();
			if(room.isLaboratory())
			{
				ArrayList<Day> weekdays = room.getWeek().getWeekDays();
				Iterator<Day> daysIterator=weekdays.iterator();
				while(daysIterator.hasNext())
				{
					Day day = daysIterator.next();
					ArrayList<TimeSlot> timeslots = day.getTimeSlot();
					boolean bool = false;
					TimeSlot lect = null;
					for(int k=0; k<timeslots.size();k++){
							TimeSlot lecture = timeslots.get(k);
							if(bool==true)
							{
								lecture.setLecture(lect.getLecture());
								bool = false;
								continue;
							}
							if(lecture.getLecture()!=null)
							{
								lect = lecture;
								slot.add(k);
								slot.add(k+1);
								bool = true;
							}
						}
				}
			}
		}
		
	}
//	private  void getThLect()
//	{
//		
//		ArrayList<ClassRoom> allrooms = GlobalBestTimetable.getRoom();
//		Iterator<ClassRoom> allroomsIterator = allrooms.iterator();
//		while(allroomsIterator.hasNext())
//		{
//			ClassRoom room = allroomsIterator.next();
//			if(!room.isLaboratory())
//			{
//				ArrayList<Day> weekdays = room.getWeek().getWeekDays();
//				Iterator<Day> daysIterator=weekdays.iterator();
////				int k=0;
//				while(daysIterator.hasNext())
//				{
//					Day day = daysIterator.next();
//					ArrayList<TimeSlot> timeslots = day.getTimeSlot();
//					
//					TimeSlot lecture = timeslots.get(st1.pop());
//					TheoryLect.add(lecture.getLecture());
//					lecture.setLecture(null);
//					
//					TimeSlot lecture1 = timeslots.get(st1.pop());
//					TheoryLect.add(lecture1.getLecture());	
//					lecture1.setLecture(null);
//
//				}
//			}
//		}
//	}
	private  void writeToExcelFile() throws IOException{
		
		int[] arr = new int[100];
		int l=0;
		Iterator<Integer> itr = slot.iterator();
		while(itr.hasNext())
		{
			arr[l++]=itr.next();
		}
		FileWriter writer = new FileWriter("timetable.csv",true);
		//PrintWriter pw = new PrintWriter(writer);
		int i=0;
		writer.append("\n\nMinimum : "+min);
		writer.append("\n\nScore : "+GlobalBestTimetable.getFittness());
		writer.append("\n\n (Subject#Professor#Student Group)");
			ArrayList<ClassRoom> allrooms = GlobalBestTimetable.getRoom();
			Iterator<ClassRoom> allroomsIterator = allrooms.iterator();
			while(allroomsIterator.hasNext()){
				ClassRoom room = allroomsIterator.next();
				writer.append("\n\nRoom Number: "+room.getRoomNo());
				ArrayList<Day> weekdays = room.getWeek().getWeekDays();
				Iterator<Day> daysIterator=weekdays.iterator();
				Iterator<String> lectTimeItr = lectureTimings.iterator();
				writer.append("\n\nTimings: ,");
				while(lectTimeItr.hasNext()){
					writer.append(lectTimeItr.next()+",");
				}
				i=0;
				int n=0;
				writer.append("\nDays\n");
				while(daysIterator.hasNext()){
					Day day = daysIterator.next();
					writer.append(/*Day: */""+weekDayNames.get(i)+",");
					ArrayList<TimeSlot> timeslots = day.getTimeSlot();
					i++;
					for(int k=0; k<timeslots.size();k++){
//						if(k==4){
//							writer.append("BREAK,");
//						}
						TimeSlot lecture = timeslots.get(k);
							
							if(!room.isLaboratory())
							{
															
								if(lecture.getLecture()!=null && (k!=arr[n] && k!=arr[n+1])){
								//System.out.print(" (Subject: "+lecture.getLecture().getSubject()+" --> Professor: "+lecture.getLecture().getProfessor().getProfessorName()+" GrpName: "+lecture.getLecture().getStudentGroup().getName()+")");
									writer.append("  ("+lecture.getLecture().getSubject()+" # "+lecture.getLecture().getProfessor().getProfessorName()+"#"+lecture.getLecture().getStudentGroup().getName().split("/")[0]+")"+",");
								}
								else
								{
									if( (k==arr[n] || k==arr[n+1]))
									{
										//TheoryLect.add(lecture.getLecture());
										writer.append("LAB,");
									}
									else
									{
										if(!st.empty())
										{
											Lecture lect1 = st.pop();
											writer.append("  ("+lect1.getSubject()+" # "+lect1.getProfessor().getProfessorName()+"#"+lect1.getStudentGroup().getName().split("/")[0]+")"+",");
										}
										else
										{
											writer.append("FREE LECTURE,");
										}
									}
								}
							}
							else
							{
								if(lecture.getLecture()!=null)
								
								{
									writer.append("("+lecture.getLecture().getSubject()+"#"+lecture.getLecture().getProfessor().getProfessorName()+"#"+lecture.getLecture().getStudentGroup().getName().split("/")[0]+")"+",");
								}
								
								else
								{
									writer.append("FREE LECTURE,");
								}
							
							}
						}
					writer.append("\n");
					n+=2;
				}
				writer.append("\n");
			}
			
			writer.flush();
		    writer.close();
	}
	

	private  void placeLectures()
	{
				int i=0,j=0;
				int[] arr = new int[100];
				int l=0;
				Iterator<Integer> itr = slot.iterator();
				while(itr.hasNext())
				{
					arr[l++]=itr.next();
				}
				
				System.out.println("Minimum : "+min);
				System.out.println("\nScore : "+GlobalBestTimetable.getFittness());
					ArrayList<ClassRoom> allrooms = GlobalBestTimetable.getRoom();
					Iterator<ClassRoom> allroomsIterator = allrooms.iterator();
					while(allroomsIterator.hasNext()){
						ClassRoom room = allroomsIterator.next();
						System.out.println("\nRoom: "+room.getRoomNo());
						ArrayList<Day> weekdays = room.getWeek().getWeekDays();
						Iterator<Day> daysIterator=weekdays.iterator();
						Iterator<String> lectTimeItr = lectureTimings.iterator();
						System.out.print("\nTimings:    ");
						while(lectTimeItr.hasNext()){
							System.out.print(" "+lectTimeItr.next()+" ");
						}
						i=0;
						int n=0;
						int m=0;
						System.out.print("\n");
						while(daysIterator.hasNext()){
							Day day = daysIterator.next();
							System.out.print("Day: "+weekDayNames.get(i));
							ArrayList<TimeSlot> timeslots = day.getTimeSlot();
							//Iterator<TimeSlot> timeslotIterator= timeslots.iterator();
							i++;
							//System.out.print(""+day.getName()+": ");
							for(int k=0; k<timeslots.size();k++){
							TimeSlot lecture = timeslots.get(k);
							if(!room.isLaboratory())
							{
															
								if(lecture.getLecture()!=null && (k!=arr[n] && k!=arr[n+1])){
								//System.out.print(" (Subject: "+lecture.getLecture().getSubject()+" --> Professor: "+lecture.getLecture().getProfessor().getProfessorName()+" GrpName: "+lecture.getLecture().getStudentGroup().getName()+")");
								System.out.print("  ("+lecture.getLecture().getSubject()+" # "+lecture.getLecture().getProfessor().getProfessorName()+"#"+lecture.getLecture().getStudentGroup().getName().split("/")[0]+")");
								}
								else
								{
									if( (k==arr[n] || k==arr[n+1]))
									{
										//TheoryLect.add(lecture.getLecture());
										System.out.print(" LAB ");
									}
									else
									{
										if(!st1.empty())
										{
											Lecture lect1 = st1.pop();
											System.out.print("  ("+lect1.getSubject()+" # "+lect1.getProfessor().getProfessorName()+"#"+lect1.getStudentGroup().getName().split("/")[0]+")");
										}
										else
										{
											System.out.print(" FREE LECTURE ");
										}
									}
								}
							}
							else
							{
								//TimeSlot lecture = timeslots.get(k);
								if(lecture.getLecture()!=null){
								//System.out.print(" (Subject: "+lecture.getLecture().getSubject()+" --> Professor: "+lecture.getLecture().getProfessor().getProfessorName()+" GrpName: "+lecture.getLecture().getStudentGroup().getName()+")");
									System.out.print("  ("+lecture.getLecture().getSubject()+" # "+lecture.getLecture().getProfessor().getProfessorName()+"#"+lecture.getLecture().getStudentGroup().getName().split("/")[0]+")");
								}
								else{
									System.out.print(" FREE LECTURE ");
								}
							}
														
						}
							System.out.print("\n");
							n+=2;
						}
						System.out.print("\n");
					}
	}
	private  void display() {
		// TODO Auto-generated method stub
		TheoryLect.clear();
		getLectures();
		int i=0,j=0;
		int[] arr = new int[100];
		int l=0;
		Iterator<Integer> itr = slot.iterator();
		while(itr.hasNext())
		{
			arr[l++]=itr.next();
		}
		
//			System.out.println("Minimum : "+min);
//			System.out.println("\nScore : "+GlobalBestTimetable.getFittness());
			ArrayList<ClassRoom> allrooms = GlobalBestTimetable.getRoom();
			Iterator<ClassRoom> allroomsIterator = allrooms.iterator();
			while(allroomsIterator.hasNext()){
				ClassRoom room = allroomsIterator.next();
//				System.out.println("\nRoom: "+room.getRoomNo());
				ArrayList<Day> weekdays = room.getWeek().getWeekDays();
				Iterator<Day> daysIterator=weekdays.iterator();
				Iterator<String> lectTimeItr = lectureTimings.iterator();
//				System.out.print("\nTimings:    ");
				while(lectTimeItr.hasNext()){
					lectTimeItr.next();
					
				}
				i=0;
				int n=0;
				System.out.print("\n");
				while(daysIterator.hasNext()){
					Day day = daysIterator.next();
//					System.out.print("Day: "+weekDayNames.get(i));
					ArrayList<TimeSlot> timeslots = day.getTimeSlot();
					//Iterator<TimeSlot> timeslotIterator= timeslots.iterator();
					i++;
					//System.out.print(""+day.getName()+": ");
					for(int k=0; k<timeslots.size();k++){
	//						if(k==3){
	//							System.out.print("       BREAK       ");
	//						}
						TimeSlot lecture = timeslots.get(k);
							if(!room.isLaboratory())
							{
								
								if(lecture.getLecture()!=null && (k!=arr[n] && k!=arr[n+1])){
								//System.out.print(" (Subject: "+lecture.getLecture().getSubject()+" --> Professor: "+lecture.getLecture().getProfessor().getProfessorName()+" GrpName: "+lecture.getLecture().getStudentGroup().getName()+")");
//									System.out.print("  ("+lecture.getLecture().getSubject()+" # "+lecture.getLecture().getProfessor().getProfessorName()+"#"+lecture.getLecture().getStudentGroup().getName().split("/")[0]+")");
								}
								else
								{
									if( (k==arr[n] || k==arr[n+1]))
									{
										TheoryLect.add(lecture.getLecture());
//										System.out.print(" LAB ");
									}
									else{
										
//										System.out.print(" FREE LECTURE ");
									}
								}
							}
							else
							{
								//TimeSlot lecture = timeslots.get(k);
								if(lecture.getLecture()!=null){
								//System.out.print(" (Subject: "+lecture.getLecture().getSubject()+" --> Professor: "+lecture.getLecture().getProfessor().getProfessorName()+" GrpName: "+lecture.getLecture().getStudentGroup().getName()+")");
//									System.out.print("  ("+lecture.getLecture().getSubject()+" # "+lecture.getLecture().getProfessor().getProfessorName()+"#"+lecture.getLecture().getStudentGroup().getName().split("/")[0]+")");
								}
								else{
//									System.out.print(" FREE LECTURE ");
								}
							}
						}
					System.out.print("\n");
					n+=2;
				}
				System.out.print("\n");
			}
			
//			System.out.println("  SIZE ++++ "+TheoryLect.size());
			Iterator<Lecture> le = TheoryLect.iterator();
			while(le.hasNext())
			{
				Lecture lect = le.next();
				if(lect!=null)
				{
					st1.push(lect);
					st.push(lect);
				}
			}
			
			System.out.println("\n\n\n\n=============FINAL TIMETABLE============\n\n");
			
			placeLectures();
			
																																												
	}
}


