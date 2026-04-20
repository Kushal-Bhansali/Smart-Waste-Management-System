package management;

import models.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class SmartWasteManagement {
    int employeeCount = 0;
    final int MAX_CAPACITY = 3;
    void registerEmployee(String username, String role, User[] users) throws ArrayIndexOutOfBoundsException {
        Scanner sc1 = new Scanner(System.in);
        if (employeeCount >= users.length) {
            sc1.close();
            throw new ArrayIndexOutOfBoundsException("Employee registration limit reached.");
        }
        if (role.equalsIgnoreCase("admin")) {
            users[employeeCount++] = new Admin(username);
        } else {
            System.out.print("Enter location for staff member " + username + "(A, B, C): ");
            String location = sc1.next();
            users[employeeCount++] = new CollectionStaff(username, location);
        }
        System.out.println("Registered " + role + ": " + username);
        users[employeeCount - 1].showDashboard();
    }

    void performCollection(WasteBin bin, User activeUser) throws UnauthorizedAccessException{
        if (activeUser instanceof CollectionStaff) {
            CollectionStaff staff = (CollectionStaff) activeUser;
            if(bin.getCurrentLevel() <= 0){
                return;
            }
            if (bin.getLocation().equals(staff.getLocation())) {
                System.out.println("Bin " + bin.getBinId() + " has been emptied by " + activeUser.getUsername());
                logEvent(bin, "Bin emptied by " + activeUser.getUsername());
                return;
            }
            else {
                throw new UnauthorizedAccessException("User "+activeUser.getUsername()+" is not authorized to collect from bin "+bin.getBinId()+" due to location mismatch.");
            }
        }
        throw new UnauthorizedAccessException("User "+activeUser.getUsername()+" is not authorized to perform collection due to insufficient permissions.");
    }

    void logEvent(WasteBin bin, String event) {
        try (FileWriter writer = new FileWriter("waste_management_log.txt", true)) {
            writer.write("Bin " + bin.getBinId() + ": " + event + "\n");
        } catch (IOException e) {
            System.out.println("Error logging event: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int numEmployees = 0;
        int numBins = 0;
        User[] users;
        WasteBin[] bins;
        Scanner sc = new Scanner(System.in);
        AnalyticsEngine engine = new AnalyticsEngine();
        SmartWasteManagement system = new SmartWasteManagement();
        String[] types = { "organic", "recyclable" };
        String[] locations = { "BITS Pilani, Goa Campus",
                "BITS Pilani, Hyderabad Campus", "BITS Pilani, Pilani Campus" };
        Random rand = new Random();

        System.out.println("Welcome to Smart Waste Management System");
        System.out.print("Enter number of employees to register: ");
        numEmployees = sc.nextInt();
        numBins = rand.nextInt(numEmployees+2);
        users = new User[numEmployees];
        bins = new WasteBin[numBins];
        for (int i = 0; i < numBins; i++) {
            String id = "B" + String.format("%03d", i + 1);
            String type = types[rand.nextInt(types.length)];
            double capacity = 100.0 + (rand.nextDouble() * 100.0); // Capacity between 100-200
            String loc = locations[rand.nextInt(locations.length)];

            if (rand.nextBoolean()) {
                bins[i] = new WasteBin(id, type, capacity, loc);
            } else {
                double currentFill = rand.nextDouble() * capacity;
                bins[i] = new WasteBin(id, type, capacity, currentFill, loc);
            }
        }

        for (int i = 0; i < numEmployees; i++) {
            System.out.print("Enter username for employee " + (i + 1) + ": ");
            String empUsername = sc.next();
            System.out.print("Enter role for employee " + (i + 1) + " (admin/staff): ");
            String empRole = sc.next();
            try {
                system.registerEmployee(empUsername, empRole, users);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
                break;
            }
        }

        for (int i = 0; i < bins.length; i++) {
            try {
                bins[i].addWaste(rand.nextDouble() * 50);
            } catch (BinOverflowException e) {
                engine.triggerAlert(e.getMessage(), "High");
            }
        }
        for (int i = 0; i < bins.length; i++) {
            if (bins[i].getCurrentLevel() > 0.8 * bins[i].getCapacity()) {
                engine.triggerAlert("Bin " + bins[i].getBinId() + " is nearing capacity!", "Medium");
            }
        }
        for (int i = 0; i < bins.length; i++) {
            if (bins[i].getCurrentLevel() > bins[i].getCapacity()) {
                engine.triggerAlert("Bin " + bins[i].getBinId() + " is overflowing!", "High");
            }
        }

        // Simulate collection
        int binsCollected = 0;
        for(int i = 0; i < system.MAX_CAPACITY; i++) {
            for (User user : users) {
                int currentBinIndex = 0;
                if (user instanceof CollectionStaff) {
                    boolean collectionAttempted = false;
                    int attempts = 0;
                    while (!collectionAttempted && attempts < bins.length) {
                        WasteBin currentBin = bins[currentBinIndex];
                        
                            
                        try {
                            if (!currentBin.isEmpty()) {
                                system.performCollection(currentBin, user);
                                engine.recordCollection(currentBin);
                                binsCollected++;
                                collectionAttempted = true;
                            }
                        } catch (UnauthorizedAccessException e) {
                            if (e.getMessage().contains("location mismatch")) {
                                continue;
                            } else {
                                System.out.println(e.getMessage());
                            }
                        } finally {
                            currentBinIndex = (currentBinIndex + 1) % bins.length;
                            attempts++;
                        }
                    }
                }
            }
        }

        // Simulate missed pickups
        WasteBin[] missedBins = new WasteBin[bins.length - binsCollected];
        if(missedBins.length == 0) {
            System.out.println("All bins were collected successfully. No missed pickups to report.");
        }
        else{
            int missedIndex = 0;
            for (int i = 0; i < bins.length; i++) {
                if(bins[i].isEmpty()) continue;
                bins[i].addMaintenanceRecord((new Date()).toString(), "Missed pickup", "Staff shortage");
                missedBins[missedIndex++] = bins[i];
            }
            engine.logMissedPickups(missedBins);
        }
        System.out.println("\nEnter name of user to access reports: ");
        String userName = sc.next();
        
        User activeUser = null;
        for(User user : users) {
            if (user.getUsername().equalsIgnoreCase(userName)) {
                activeUser = user;
                break;
            }
        }

        try{
            if(activeUser == null) {
                sc.close();
                throw new UnauthorizedAccessException("User not found: " + userName);
            }
            if(activeUser instanceof Admin) {
                ((Admin) activeUser).generateFullReport(engine);
            }
            else {
                sc.close();
                throw new UnauthorizedAccessException("User "+activeUser.getUsername()+" does not have permission to access reports.");
            }
        } catch (UnauthorizedAccessException e) {
            System.out.println(e.getMessage());
        }
        
        
        sc.close();
    }
}