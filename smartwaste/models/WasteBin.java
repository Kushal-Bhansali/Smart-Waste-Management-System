package models;

public class WasteBin {
    String binId;
    String type; 
    Double capacity; 
    Double currentLevel;
    Boolean isDamaged; 
    String location;
    
    MaintenanceLog[] maintenanceHistory = new MaintenanceLog[10];
    int logCount = 0;

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
    
    public WasteBin(String binId, String type, Double capacity, String location) {
        this.binId = binId;
        this.type = type;
        this.capacity = capacity;
        this.currentLevel = 0.0;
        this.isDamaged = false;
        this.location=location;
    }
    
    public WasteBin(String binId, String type, Double capacity, Double currentLevel, String location) {
        this.binId = binId;
        this.type = type;
        this.capacity = capacity;
        this.location = location;
        this.currentLevel = currentLevel;
    }

    public void addWaste(Double amount) throws BinOverflowException {
        if (this.currentLevel + amount > this.capacity) {
            throw new BinOverflowException("Bin " + this.binId + " is overflowing!"); 
        }
        this.currentLevel += amount;
    }

    public void emptyBin() { 
        this.currentLevel = 0.0; 
    }

    public void addMaintenanceRecord(String issue) {
        addMaintenanceRecord("Unknown Date", issue);
    }

    public void addMaintenanceRecord(String date, String issue) {
        if (logCount < maintenanceHistory.length) {
            maintenanceHistory[logCount++] = new MaintenanceLog(date, issue);
            this.isDamaged = true;
        } else 
            System.out.println("Maintenance log is full for bin: " + binId);
        
    }
    public void addMaintenanceRecord(String date, String... issues) {
        for (String issue: issues) {
            addMaintenanceRecord(date, issue); 
        }
    }

    public boolean isEmpty() { 
        return this.currentLevel == 0.0; 
    }
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
