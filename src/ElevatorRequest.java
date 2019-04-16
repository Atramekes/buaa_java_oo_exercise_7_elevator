class ElevatorRequest {
    private final int fromFloor;
    private final int toFloor;
    private final int personId;
    
    ElevatorRequest(int fromFloor, int toFloor, int personId) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.personId = personId;
    }
    
    int getFromFloor() {
        return this.fromFloor;
    }
    
    int getToFloor() {
        return this.toFloor;
    }
    
    int getPersonId() {
        return this.personId;
    }
}
