package APAssignment1;

import APAssignment1.Enums.RoomStatus;
import APAssignment1.Enums.RoomType;

public class Suite extends Room {
	
	// Instance variables required for the 
	// Suite type
	public static final double SUITE_DAILY_RATE = 999;
	public static final double SUITE_LATE_FEE_DAILY_RATE = 1099;
	public static final int NUM_OF_BEDS = 6;
	public static final int MAINTENANCE_INTERVAL = 10;
	
	private DateTime lastMaintenanceDate;

	// Create constructors (minimise code duplication) 
	// by using super(...)
	public Suite(String id, String feature, RoomType type, RoomStatus status, DateTime lastMaintanenceDate) {
		super(id, NUM_OF_BEDS, feature, type, status);
		setLastMaintenanceDate(lastMaintanenceDate);
    }
	
	public DateTime getLastMaintenanceDate() {
		return lastMaintenanceDate;
	}

	public void setLastMaintenanceDate(DateTime lastMaintanenceDate) {
		this.lastMaintenanceDate = lastMaintanenceDate;
	}

	// Implement methods and / abstract methods
	// rent, return, toString, getDetails....
	@Override
	public boolean rent(String customerId, DateTime rentDate, int numOfRentDays) {
		boolean rentOK = false;
		HiringRecord rentRecord = null;
		
		String id = getId() + "_" + customerId + "_" + rentDate.getEightDigitDate();
		
		DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDays);
		DateTime nextMaintenanceDate = new DateTime(lastMaintenanceDate, MAINTENANCE_INTERVAL);
		int availableRentDays = DateTime.diffDays(nextMaintenanceDate, rentDate);
		
		if (numOfRentDays <= availableRentDays) {
			setStatus(RoomStatus.Rented);
			setNumOfRentDays(numOfRentDays);
			rentRecord = new HiringRecord(id, rentDate, estimatedReturnDate);
			setNewRentRecord(rentRecord);
			rentOK = true;
		}
		else
			System.out.println("Suite " + getId() + " cannot be rented for " + numOfRentDays + " days from rent date " + rentDate + ". It is scheduled for maintenance on " + nextMaintenanceDate);
		return rentOK;
	}

	@Override
	public boolean returnRoom(DateTime returnDate) {
		boolean returnOK = false;
		double rentalFee = 0.00;
		double lateFee = 0.00;
		
		HiringRecord rentRecord = rentRecords.get(rentRecords.size() - 1);
		int rentedDays = DateTime.diffDays(rentRecord.getEstimatedReturnDate(), rentRecord.getRentDate());
		int lateDays = DateTime.diffDays(returnDate, rentRecord.getEstimatedReturnDate());
		
		if (rentedDays > 0) {
			rentalFee = rentedDays * SUITE_DAILY_RATE;
			setStatus(RoomStatus.Available);
			setNumOfRentDays(0);
			rentRecord.setActualReturnDate(returnDate);
			rentRecord.setRentalFee(rentalFee);
			
			if (lateDays > 0) {
				lateFee = lateDays * SUITE_LATE_FEE_DAILY_RATE;
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
			System.out.println("Suite " + getId() + " is rented at the moment.");
			break;
		case Maintenance:
			System.out.println("Suite " + getId() + " is already under maintenance at the moment.");
			break;
		case Available:
			setStatus(RoomStatus.Maintenance);
			performOK = true;
		}
		return performOK;
	}

	public boolean completeMaintenance(DateTime completionDate) {
		// If room is rented or available then this method should return false
		boolean completeOK = false;
		RoomStatus status = getStatus();
		
		switch(status) {
		case Rented:
			System.out.println("Suite " + getId() + " is rented at the moment.");
			break;
		case Available:
			System.out.println("Suite " + getId() + " is not under maintenance at the moment.");
			break;
		case Maintenance:
			setStatus(RoomStatus.Available);
			setLastMaintenanceDate(completionDate);
			completeOK = true;
		}
		return completeOK;
	}

	@Override
	public String toString() {
		return getId() + ":" + getNumOfBeds() + ":" + getType() + ":" + getStatus() + ":" + getLastMaintenanceDate() + ":" + getFeature();
	}
	
	@Override
	protected String getDetails() {
		String roomDetails = null;
		roomDetails = "Room ID:		" + getId();
		roomDetails += System.lineSeparator() + "Number of beds:		" + getNumOfBeds();
		roomDetails += System.lineSeparator() + "Type:			" + getType();
		roomDetails += System.lineSeparator() + "Status:			" + getStatus();
		roomDetails += System.lineSeparator() + "Last Maintenance Date:	" + getLastMaintenanceDate();
		roomDetails += System.lineSeparator() + "Feature Summary:	" + getFeature();
    	
    	if (rentRecords.size() == 0)
    		roomDetails += System.lineSeparator() + "RENTAL RECORD:		empty";
    	else {
    		roomDetails += System.lineSeparator() + "RENTAL RECORD";
    		for (int i = rentRecords.size() - 1; i >= 0 ; i--)
    		{
    			roomDetails += System.lineSeparator() + rentRecords.get(i).getDetails();
    			if (i > 0) {
    				roomDetails += System.lineSeparator() + "-----------------------------------";
    			}
    		}
    	}
    	return roomDetails;	
	}

}
