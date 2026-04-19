package models;

public class WasteBin {
    private String binId;
    private String type; 
    private Double capacity; 
    private Double currentLevel;
    private Boolean isDamaged; 
    private String location;
    //maintenance array for maintenance log of bins
    private MaintenanceLog[] maintenanceHistory = new MaintenanceLog[10];
    private int logCount = 0;

    public class MaintenanceLog {
        String date;
        String issue;
        public MaintenanceLog(String date, String issue) {
            this.date = date;
            this.issue = issue;
        }
        public String getLog() { 
            return date + ": " + issue; 
        }
    }
    //constructor for wastebin
    public WasteBin(String binId, String type, Double capacity, String location) {
        this.binId = binId;
        this.type = type;
        this.capacity = capacity;
        this.currentLevel = 0.0;
        this.isDamaged = false;
        this.location=location;
    }
    //overloaded constructor corresponding to wastebin
    public WasteBin(String binId, String type, Double capacity, Double currentLevel, String location) {
        this(binId, type, capacity, location);
        this.currentLevel = currentLevel;
    }

    public void addWaste(Double amount) throws BinOverflowException {
        if (this.currentLevel + amount > this.capacity) {
            throw new BinOverflowException("Bin " + binId + " is overflowing!"); 
        }
        this.currentLevel += amount;
    }

    public void emptyBin() { this.currentLevel = 0.0; }

    public void addMaintenanceRecord(String issue) {
        addMaintenanceRecord("Unknown Date", issue);
    }
    //corresponding overloaded methods
    public void addMaintenanceRecord(String date, String issue) {
        if (logCount < maintenanceHistory.length) {
            maintenanceHistory[logCount++] = new MaintenanceLog(date, issue);
            this.isDamaged = true;
        } else {
            System.out.println("Maintenance log is full for bin: " + binId);
        }
    }
    public void addMaintenanceRecord(String date, String... issues) {
        for (int i = 0; i < issues.length; i++) {
            addMaintenanceRecord(date, issues[i]); 
        }
    }

    //getters for wastebin variables to be accessed by other classes
    public String getBinId() { 
        return binId; 
    }

    public String getType() { 
        return type; 
    }

    public Double getCapacity() { 
        return capacity; 
    }

    public Double getCurrentLevel() { 
        return currentLevel; 
    }
    
    public Boolean getIsDamaged() { 
        return isDamaged; 
    }
    
    public Boolean getStatus() { 
        return currentLevel >= capacity; 
    } 
    
    public String getLocation() {
        return location;
    }
}
