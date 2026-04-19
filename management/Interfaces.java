package smartwaste.management;

public interface Reportable {
    void generateSummary();
}

public interface AlertSystem {
    void triggerAlert(String message, String severity); 
}
