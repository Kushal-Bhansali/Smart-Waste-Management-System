package smartwaste.management;

// We must import the models package to use WasteBin
import smartwaste.models.WasteBin;

public class AnalyticsEngine implements Reportable, AlertSystem {
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

    // Overloaded Method 1
    public void printStats() {
        System.out.println("Total Waste: " + (totalRecyclableCollected + totalNonRecyclableCollected) + " kg");
    }

    // Overloaded Method 2
    public void printStats(String category) {
        if (category.equalsIgnoreCase("recyclable")) 
            System.out.println("Recyclable Collected: " + totalRecyclableCollected + " kg");
        else 
            System.out.println("Non-Recyclable Collected: " + totalNonRecyclableCollected + " kg");
    }

    // Overloaded Method 3
    public void printStats(boolean detailed) {
        if (detailed) {
            System.out.println("--- Detailed Environmental Impact ---");
            printStats("recyclable");
            printStats("organic");
            double efficiency = (totalRecyclableCollected / (totalRecyclableCollected + totalNonRecyclableCollected + 0.1)) * 100;
            System.out.println("Recycling Efficiency: " + String.format("%.2f", efficiency) + "%");
        } else {
            printStats();
        }
    }

    // Vararg Method
    public void logMissedPickups(String... missedBinIds) {
        System.out.print("ALERT: Missed pickups for bins: ");
        for (String id : missedBinIds) {
            System.out.print(id + " ");
        }
        System.out.println();
    }

    @Override
    public void generateSummary() { printStats(true); }

    @Override
    public void triggerAlert(String message, String severity) {
        System.out.println("[" + severity.toUpperCase() + " ALERT] " + message);
    }
}