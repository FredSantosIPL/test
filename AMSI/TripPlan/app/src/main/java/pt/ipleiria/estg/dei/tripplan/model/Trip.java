package pt.ipleiria.estg.dei.tripplan.model;

public class Trip {
    private long id;
    private String title;
    private String destination;
    private String startDate;
    private double budget;

    public Trip(long id, String title, String destination, String startDate, double budget) {
        this.id = id;
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.budget = budget;
    }

    public long getId() {return id;}
    public void set(long id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDestination() {return destination;}
    public void setDestination(String destination) {this.destination = destination;}

    public String getStartDate() {return startDate;}
    public void setStartDate(String startDate) {this.startDate = startDate;}

    public double getBudget() {return budget;}
    public void setBudget(double budget) {this.budget = budget;}
}
