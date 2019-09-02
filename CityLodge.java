package APAssignment1;

import java.util.ArrayList;
import java.util.Scanner;
import APAssignment1.Enums.RoomStatus;
import APAssignment1.Enums.RoomType;

public class CityLodge {

	Scanner scan = new Scanner(System.in);
	
	// must have 1 single collection of type Room
	// all methods must work with Room type
	private ArrayList<Room> rooms;
	int choice;
	
	// constructor
	public CityLodge() {
		rooms = new ArrayList<Room>();		
	}
	
	// methods
	private void showMenu() {
		System.out.println("****** CITYLODGE MAIN MENU ******");
		System.out.println("Add Room:                   1");
		System.out.println("Rent Room:                  2");
		System.out.println("Return Room:                3");
		System.out.println("Room Maintenance:           4");
		System.out.println("Complete Maintenance:       5");
		System.out.println("Display All Rooms:          6");
		System.out.println("Exit Program:               7");
		System.out.println("Enter you choice: ");
	}
		
	private Room findRoomById(String roomId) {
		Room room = null;
		
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getId().compareTo(roomId) == 0) {
				room = rooms.get(i);
				break;
			}
		}
		return room;
	}
		
	private void addRoom() {
		// to be called when user selects add room option
		Room room = null;
		System.out.println("Enter the room id: ");
		String roomId = scan.next();
		room = findRoomById(roomId);
						
		if (room != null)
			System.out.println((room.getType() == RoomType.Suite ? "Suite " : "Room ") + room.getId() + " already exists!");
		else if (roomId.charAt(0) == 'R' && roomId.charAt(1) == '_') {
			System.out.println("Enter the number of beds: ");
			
			try {
				int numOfBeds = Integer.parseInt(scan.next());
				if (numOfBeds == 1 || numOfBeds == 2 || numOfBeds == 4) {
					
					System.out.println("Enter the features: ");
					scan.nextLine(); //this is to clear the keyboard buffer
					String feature = scan.nextLine();
					StandardRoom standardRoom = new StandardRoom(roomId, numOfBeds, feature, RoomType.Standard, RoomStatus.Available);
					rooms.add(standardRoom);
					System.out.println("Room " + roomId + " is now added to CityLodge System");
				}
				else 
					System.out.println("Invalid number of beds!");
			}
			catch (NumberFormatException nfe) {
				System.out.println("Invalid input format!");
			}
		}
		else if (roomId.charAt(0) == 'S' && roomId.charAt(1) == '_') {
			System.out.println("Enter the features: ");
			scan.nextLine(); //this is to clear the keyboard buffer
			String feature = scan.nextLine();
			System.out.println("Enter the last Maintanence Date (dd/mm/yyyy): ");
			String date = scan.next();
			
			try {
				int day = Integer.parseInt(date.substring(0, 2));
				int month = Integer.parseInt(date.substring(3, 5));
				int year = Integer.parseInt(date.substring(6, 10));
				
				DateTime lastMaintanenceDate = new DateTime(day, month, year);
				Suite suite = new Suite(roomId, feature, RoomType.Suite, RoomStatus.Available, lastMaintanenceDate);
				rooms.add(suite);
				System.out.println("Suite " + roomId + " is now added to CityLodge System");
			}
			catch (NumberFormatException nfe) {
				System.out.println("Invalid input format!");
			}
		}
		else
			System.out.println("Invalid room id!");
	}
		
	private void rentRoom() {
		// to be called when user selects rent option
		Room room = null;
		boolean rentOK = false;
		
		System.out.println("Enter the room id: ");
		String roomId = scan.next();
		room = findRoomById(roomId);
		
		if (room != null && (room.getStatus() == RoomStatus.Rented || room.getStatus() == RoomStatus.Maintenance))
			System.out.println((room.getType() == RoomType.Suite ? "Suite " : "Room ") + room.getId() + " is not available at the moment.");
		else if (room != null && room.getStatus() == RoomStatus.Available) {
			System.out.println("Enter the customer id: ");
			String customerId = scan.next();
			System.out.println("Enter the rent date (dd/mm/yyyy): ");
			String date = scan.next();
			
			try {
				int day = Integer.parseInt(date.substring(0, 2));
				int month = Integer.parseInt(date.substring(3, 5));
				int year = Integer.parseInt(date.substring(6, 10));
				
				DateTime rentDate = new DateTime(day, month, year);
				DateTime todaysDate = new DateTime();
				int rentDateValid = DateTime.diffDays(rentDate, todaysDate);
				
				if (rentDateValid >= -1) {
					System.out.println("Enter the number of days to rent for: ");
					int numOfRentDays = Integer.parseInt(scan.next());
					
					if (numOfRentDays > 10) {
			    		System.out.println("Invalid number of rent days!");
			    	}
					else
						rentOK = room.rent(customerId, rentDate, numOfRentDays);
				    	
					if (rentOK)
						System.out.println((room.getType() == RoomType.Suite ? "Suite " : "Room ") + room.getId() + " is now rented by customer " + customerId);
				}
				else
					System.out.println("Invalid rent date! Rent date cannot be less than today.");
			}
			catch (NumberFormatException nfe) {
				System.out.println("Invalid input format!");
			}
		}
		else
			System.out.println("Invalid room id!");
	}
	
	private void returnRoom() {
		// to be called when user selects return option
		Room room = null;
		boolean returnOK = false;
		
		System.out.println("Enter the room id: ");
		String roomId = scan.next();
		room = findRoomById(roomId);
		
		if (room != null && (room.getStatus() == RoomStatus.Maintenance || room.getStatus() == RoomStatus.Available))
			System.out.println((room.getType() == RoomType.Suite ? "Suite " : "Room ") + room.getId() + " is not rented at the moment.");
		else if (room != null && room.getStatus() == RoomStatus.Rented) {
			System.out.println("Enter the return date (dd/mm/yyyy): ");
			String date = scan.next();
			
			try {
				int day = Integer.parseInt(date.substring(0, 2));
				int month = Integer.parseInt(date.substring(3, 5));
				int year = Integer.parseInt(date.substring(6, 10));
				
				DateTime returnDate = new DateTime(day, month, year);
				returnOK = room.returnRoom(returnDate);
				
				if (returnOK) {
					System.out.println((room.getType() == RoomType.Suite ? "Suite " : "Room ") + room.getId() + " is now returned.");
					System.out.println(room.latestRentalDetails() + System.lineSeparator());
				}
			}
			catch (NumberFormatException nfe) {
				System.out.println("Invalid input format!");
			}
		}
		else
			System.out.println("Invalid room id!");
	}
	
	private void performMaintenance() {
		// to be called when user selects perform maintenance option
		Room room = null;
		boolean performOK = false;
		
		System.out.println("Enter the room id: ");
		String roomId = scan.next();
		room = findRoomById(roomId);
		
		if (room != null) {
			performOK = room.performMaintenance();
			if (performOK)
				System.out.println((room.getType() == RoomType.Suite ? "Suite " : "Room ") + room.getId() + " is now under maintenance.");
		}
		else
			System.out.println("Invalid room id!");
	}
	
	private void completeMaintenance() {
		// to be called when user selects complete maintenance option
		Room room = null;
		boolean completeOK = false;
		DateTime completionDate = null;
		
		System.out.println("Enter the room id: ");
		String roomId = scan.next();
		room = findRoomById(roomId);
		
		if (room != null) {
			if (room.getType() == RoomType.Suite) {
				System.out.println("Enter the maintenance completion date (dd/mm/yyyy): ");
				String date = scan.next();
				
				try {
					int day = Integer.parseInt(date.substring(0, 2));
					int month = Integer.parseInt(date.substring(3, 5));
					int year = Integer.parseInt(date.substring(6, 10));
					
					completionDate = new DateTime(day, month, year);
				}
				catch (NumberFormatException nfe) {
					System.out.println("Invalid input format!");
				}
			}
			
			completeOK = room.completeMaintenance(completionDate);
			if (completeOK)
				System.out.println((room.getType() == RoomType.Suite ? "Suite " : "Room ") + room.getId() + " has all maintenance operations completed and is now ready for rent.");
		}
		else
			System.out.println("Invalid room id!");
	}
	
	private void displayAllRooms() {
		System.out.println(System.lineSeparator());
		for (int i = 0; i < rooms.size(); i++) {
			System.out.println(rooms.get(i).getDetails() + System.lineSeparator());
		}
	}
			
	// GOAL high cohesion low coupling, separate your code into methods
	// one method does one job
	 
	public void runApp() {
		do {
			showMenu();
			
			try {
				choice = Integer.parseInt(scan.next());
				
				switch(choice) {	
				case 1:
					System.out.println("Loading add room module...");
					addRoom();
					break;
					
				case 2:
					System.out.println("Loading rent room module...");
					rentRoom();
			    	break;
			    	
				case 3:
					System.out.println("Loading return room module...");
					returnRoom();
					break;
					
				case 4:
					System.out.println("Loading perform maintenance module...");
					performMaintenance();
					break;
		        
				case 5:
					System.out.println("Loading complete maintenance module...");
					completeMaintenance();
					break;
				
				case 6:
					System.out.println("Loading display all rooms module...");
					displayAllRooms();
					break;
					
				case 7:
					System.out.println("Exiting program...");
					scan.close();
					break;
					
				default:
					System.out.println("Wrong menu input! Please enter your choice again.");
					continue;
				}
			}
			catch (NumberFormatException nfe) {
				System.out.println("Invalid input format!");
			}
			
		} while (choice != 7);
	}
}
