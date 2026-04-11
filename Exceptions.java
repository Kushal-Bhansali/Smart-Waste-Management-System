package smartwaste;

// Exception 1: Bin Overflow
class BinOverflowException extends Exception {
    public BinOverflowException(String message) { super(message); }
}

// Exception 2: Unauthorized Access (Role-based security)
class UnauthorizedAccessException extends Exception {
    public UnauthorizedAccessException(String message) { super(message); }
}

// Exception 3: Invalid Bin State
class InvalidBinOperationException extends Exception {
    public InvalidBinOperationException(String message) { super(message); }
}
