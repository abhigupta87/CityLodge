package APAssignment1;

import APAssignment1.Enums.RoomStatus;
import APAssignment1.Enums.RoomType;

public class StandardRoom extends Room {
	
	// Instance variables required for the 
	// StandardRoom type
	public static final double SINGLE_BED_DAILY_RATE = 59;
	public static final double DOUBLE_BED_DAILY_RATE = 99;
	public static final double FOUR_BED_DAILY_RATE = 199;
	public static final double LATE_FEE_DAILY_PERCENTAGE = 135 / 100;
	
	// Create constructors (minimise code duplication) 
	// by using super(...)
	public StandardRoom(String id, int numOfBeds, String feature, RoomType type, RoomStatus status) {
		super(id, numOfBeds, feature, type, status);
	}
	
	// Implement methods and / abstract methods
	// rent, return, toString, getDetails....
	@Override
	public boolean rent(String customerId, DateTime rentDate, int numOfRentDays) {
		boolean rentOK = false;
		boolean rentDaysOK = false;
		HiringRecord rentRecord = null;
		
		String id = getId() + "_" + customerId + "_" + rentDate.getEightDigitDate();
		DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDays);
					
		switch(rentDate.getNameOfDay()) {
	    case "Monday":
	    case "Tuesday":
	    case "Wednesday":
	    case "Thursday":
	    case "Friday":
	    	if(numOfRentDays < 2) {
	    		System.out.println("Invalid number of rent days!");
	    		break;
	    	}
	    	else {
	    		rentDaysOK = true;
	    		break;
	    	}
	    case "Saturday":
	    case "Sunday":
	    	if(numOfRentDays < 3) {
	    		System.out.println("Invalid number of rent days!");
				break;
	    	}
	    	else {
	    		rentDaysOK = true;
	    		break;
	    	}
		}
		
		if (rentDaysOK) {
			setStatus(RoomStatus.Rented);
			setNumOfRentDays(numOfRentDays);
			rentRecord = new HiringRecord(id, rentDate, estimatedReturnDate);
			setNewRentRecord(rentRecord);
			rentOK = true;
		}
		return rentOK;
	}

	@Override
	public boolean returnRoom(DateTime returnDate) {
		boolean returnOK = false;
		double rentalFee = 0.00;
		double lateFee = 0.00;
		int numOfBeds = getNumOfBeds();
		double lateDayRentalRate = 0.00;
		
		HiringRecord rentRecord = rentRecords.get(rentRecords.size() - 1);
		int rentedDays = DateTime.diffDays(rentRecord.getEstimatedReturnDate(), rentRecord.getRentDate());
		int lateDays = DateTime.diffDays(returnDate, rentRecord.getEstimatedReturnDate());
		
		if (rentedDays > 0) {
			switch(numOfBeds) {
			case 1:
				rentalFee = rentedDays * SINGLE_BED_DAILY_RATE;
				lateDayRentalRate = LATE_FEE_DAILY_PERCENTAGE * SINGLE_BED_DAILY_RATE;
				break;
			case 2:
				rentalFee = rentedDays * DOUBLE_BED_DAILY_RATE;
				lateDayRentalRate = LATE_FEE_DAILY_PERCENTAGE * DOUBLE_BED_DAILY_RATE;
				break;
			case 4:
				rentalFee = rentedDays * FOUR_BED_DAILY_RATE;
				lateDayRentalRate = LATE_FEE_DAILY_PERCENTAGE * FOUR_BED_DAILY_RATE;
				break;
			}
			
			setStatus(RoomStatus.Available);
			setNumOfRentDays(0);
			rentRecord.setActualReturnDate(returnDate);
			rentRecord.setRentalFee(rentalFee);
			
			if (lateDays > 0) {
				lateFee = lateDays * lateDayRentalRate;
				rentRecord.setLateFee(lateFee);
			}
			
			returnOK = true;
		}
		else {
			System.out.println("Invalid return date!");
		}
		return returnOK;
	}

	@Override
	public boolean performMaintenance() {
		// If room is rented or already under maintenance then this method should return false
		boolean performOK = false;
		RoomStatus status = getStatus();
		
		switch(status) {
		case Rented:
			System.out.println("Room " + getId() + " is rented at the moment.");
			break;
		case Maintenance:
			System.out.println("Room " + getId() + " is already under maintenance at the moment.");
			break;
		case Available:
			setStatus(RoomStatus.Maintenance);
			performOK = true;
		}
		return performOK;
	}

	@Override
	public boolean completeMaintenance(DateTime completionDate) {
		// If room is rented or available then this method should return false
		boolean completeOK = false;
		RoomStatus status = getStatus();
		
		switch(status) {
		case Rented:
			System.out.println("Room " + getId() + " is rented at the moment.");
			break;
		case Available:
			System.out.println("Room " + getId() + " is not under maintenance at the moment.");
			break;
		case Maintenance:
			setStatus(RoomStatus.Available);
			completeOK = true;
		}
		return completeOK;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public String getDetails() {
		return super.getDetails();
	}
}
