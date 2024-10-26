package application;

public class Book {
    private String title;
    private String authors;
    private int physicalCopies;
    private double price;
    private int soldCopies;

    public Book(String title, String authors, int physicalCopies, double price, int soldCopies) {
        this.title = title;
        this.authors = authors;
        this.physicalCopies = physicalCopies;
        this.price = price;
        this.soldCopies = soldCopies;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public int getPhysicalCopies() {
        return physicalCopies;
    }

    public void setPhysicalCopies(int physicalCopies) {
        this.physicalCopies = physicalCopies; // Setter method for physical copies
    }

    public double getPrice() {
        return price;
    }

    public int getSoldCopies() {
        return soldCopies;
    }

    public void setSoldCopies(int soldCopies) {
        this.soldCopies = soldCopies; // Optional, if you want to update sold copies
    }
}
