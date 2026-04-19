package smartwaste.models;

public class WasteBin {
    private String binId;
    private String type; 
    private Double capacity; // Wrapper
    private Double currentLevel; // Wrapper
    private Boolean isDamaged; // Wrapper
    
    private MaintenanceLog[] maintenanceHistory = new MaintenanceLog[10];
    private int logCount = 0;

    // Nested Class (Requirement 1)
    public class MaintenanceLog {
        String date;
        String issue;
        public MaintenanceLog(String date, String issue) {
            this.date = date;
            this.issue = issue;
        }
        public String getLog() { return date + ": " + issue; }
    }

    // Overloaded Constructor 1 (Requirement 9)
    public WasteBin(String binId, String type, Double capacity) {
        this.binId = binId;
        this.type = type;
        this.capacity = capacity;
        this.currentLevel = 0.0;
        this.isDamaged = false;
    }

    // Overloaded Constructor 2 (Requirement 9)
    public WasteBin(String binId, String type, Double capacity, Double currentLevel) {
        this(binId, type, capacity);
        this.currentLevel = currentLevel;
    }

    public void addWaste(Double amount) throws BinOverflowException { // Exception handling basis
        if (this.currentLevel + amount > this.capacity) {
            // Assuming BinOverflowException is a custom exception you will create elsewhere
            throw new BinOverflowException("Bin " + binId + " is overflowing!"); 
        }
        this.currentLevel += amount;
    }

    public void emptyBin() { this.currentLevel = 0.0; }
    
    // ---------------------------------------------------------
    // Requirement 8: Overloaded methods (Minimum 3)
    // Requirement 10: Vararg overloading (Minimum 1)
    // ---------------------------------------------------------

    // Overloaded Method 1: Standard single entry
    public void addMaintenanceRecord(String date, String issue) {
        if (logCount < maintenanceHistory.length) {
            maintenanceHistory[logCount++] = new MaintenanceLog(date, issue);
            this.isDamaged = true;
        } else {
            System.out.println("Maintenance log is full for bin: " + binId);
        }
    }

    // Overloaded Method 2: Single entry without a date (defaults to "Unknown Date")
    public void addMaintenanceRecord(String issue) {
        addMaintenanceRecord("Unknown Date", issue);
    }

    // Overloaded Method 3 & Vararg Overloading: Multiple issues on a specific date
    public void addMaintenanceRecord(String date, String... issues) {
        for (int i = 0; i < issues.length; i++) {
            // Reuses the first method to add each issue in the vararg array
            addMaintenanceRecord(date, issues[i]); 
        }
    }

    // Getters
    public String getBinId() { return binId; }
    public String getType() { return type; }
    public Double getCapacity() { return capacity; }
    public Double getCurrentLevel() { return currentLevel; }
    public Boolean getStatus() { return currentLevel >= capacity; } 
}
