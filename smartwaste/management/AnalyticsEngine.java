package management;
import models.WasteBin;

class AnalyticsEngine implements Reportable, AlertSystem {
    private double totalRecyclableCollected = 0;
    private double totalNonRecyclableCollected = 0;

    public void recordCollection(WasteBin bin) {
        if (bin.getType().equalsIgnoreCase("recyclable")) {
            totalRecyclableCollected += bin.getCurrentLevel();
        } else {
            totalNonRecyclableCollected += bin.getCurrentLevel();
        }
        bin.emptyBin();
    }

    public void printStats() {
        System.out.println("Total Waste: " + (totalRecyclableCollected + 
            totalNonRecyclableCollected) + " kg");
    }

    public void printStats(String category) {
        if (category.equalsIgnoreCase("recyclable")) 
            System.out.println("Recyclable Collected: " + totalRecyclableCollected + " kg");
        else 
            System.out.println("Non-Recyclable Collected: " + totalNonRecyclableCollected + " kg");
    }

    public void printStats(boolean detailed) {
        if (detailed) {
            System.out.println("--- Detailed Environmental Impact ---");
            printStats("recyclable");
            printStats("organic");
            double efficiency = (totalRecyclableCollected / (totalRecyclableCollected + 
                totalNonRecyclableCollected + 0.1)) * 100;
            System.out.println("Recycling Efficiency: " + String.format("%.2f", 
                efficiency) + "%");
        } else {
            printStats();
        }
    }

    public void logMissedPickups(WasteBin[] missedBinIds) {
        System.out.print("ALERT: Missed pickups for bins: ");
        for (WasteBin bin : missedBinIds) {
            System.out.println(bin.getBinId() + " " + bin.getLocation());
        }
        System.out.println();
    }

    public void generateSummary() { 
        printStats(true); 
    }

    public void triggerAlert(String message, String severity) {
        System.out.println("[" + severity.toUpperCase() + " ALERT] " + message);
    }
}