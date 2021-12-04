package rmit.tuong.s3818196;

public class SiteModel {
    private int id;
    private String name;
    private double longitude;
    private double latitude;
    private int leaderID;
    private String leaderName;
    private int numOfPeopleTested;
    private String listVolunteer;
    private int numOfVolunteer;

    // Constructor
    public SiteModel() {
    }

    public SiteModel(int id, String name, double longitude, double latitude, int leaderID, String leaderName, int numOfPeopleTested, String listVolunteer, int numOfVolunteer) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.leaderID = leaderID;
        this.leaderName = leaderName;
        this.numOfPeopleTested = numOfPeopleTested;
        this.listVolunteer = listVolunteer;
        this.numOfVolunteer = numOfVolunteer;
    }

    public SiteModel(int id, String name, double longitude, double latitude, int leaderID, String leaderName, int numOfPeopleTested) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.leaderID = leaderID;
        this.leaderName = leaderName;
        this.numOfPeopleTested = numOfPeopleTested;
    }

    // Getter and Setter for all attribute

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(int leaderID) {
        this.leaderID = leaderID;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public int getNumOfPeopleTested() {
        return numOfPeopleTested;
    }

    public void setNumOfPeopleTested(int numOfPeopleTested) {
        this.numOfPeopleTested = numOfPeopleTested;
    }

    public String getListVolunteer() {
        return listVolunteer;
    }

    public void setListVolunteer(String listVolunteer) {
        this.listVolunteer = listVolunteer;
    }

    public int getNumOfVolunteer() {
        return numOfVolunteer;
    }

    public void setNumOfVolunteer(int numOfVolunteer) {
        this.numOfVolunteer = numOfVolunteer;
    }

    @Override
    public String toString() {
        return "SiteModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", leaderID=" + leaderID +
                ", leaderName='" + leaderName + '\'' +
                ", numOfPeopleTested=" + numOfPeopleTested +
                '}';
    }
}
