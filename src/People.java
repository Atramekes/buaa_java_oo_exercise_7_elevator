class People {
    private int toFloor;
    private final int personId;
    private boolean special;
    private int toNewFloor;
    
    People(int toFloor, int personId, Elevator el) {
        this.toFloor = toFloor;
        this.personId = personId;
        this.special = false;
        char name = el.getElevatorName();
        int floor = el.getFloor();
        if (name == 'A' && !el.isInA(toFloor)) {
            this.toNewFloor = toFloor;
            this.toFloor = 1;
            this.special = true;
        } else if (name == 'B' && !el.isInB(toFloor)) {
            this.toNewFloor = toFloor;
            if (toFloor == 3) {
                if (floor <= 1) {
                    this.toFloor = 1;
                } else {
                    this.toFloor = 5;
                }
            } else {
                this.toFloor = 1;
            }
            this.special = true;
        } else if (name == 'C' && !el.isInC(toFloor)) {
            this.toNewFloor = toFloor;
            if (el.isInA(toFloor)) {
                this.toFloor = 1;
            } else {
                if (floor <= toFloor) {
                    this.toFloor -= 1;
                } else {
                    this.toFloor += 1;
                }
            }
            this.special = true;
        }
    }
    
    int getPersonId() {
        return personId;
    }
    
    int getToFloor() {
        return toFloor;
    }
    
    boolean isSpecial() {
        return special;
    }
    
    int getToNewFloor() {
        return toNewFloor;
    }
}
