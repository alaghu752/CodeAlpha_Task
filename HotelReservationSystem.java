import java.io.*;
import java.util.*;

class Room {
    String roomId;
    String category;
    boolean isAvailable;

    Room(String roomId, String category, boolean isAvailable) {
        this.roomId = roomId;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return roomId + "," + category + "," + isAvailable;
    }

    static Room fromString(String line) {
        String[] parts = line.split(",");
        return new Room(parts[0], parts[1], Boolean.parseBoolean(parts[2]));
    }
}

class Booking {
    String bookingId;
    String roomId;
    String customerName;
    boolean paymentDone;

    Booking(String bookingId, String roomId, String customerName, boolean paymentDone) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.customerName = customerName;
        this.paymentDone = paymentDone;
    }

    @Override
    public String toString() {
        return bookingId + "," + roomId + "," + customerName + "," + paymentDone;
    }

    static Booking fromString(String line) {
        String[] parts = line.split(",");
        return new Booking(parts[0], parts[1], parts[2], Boolean.parseBoolean(parts[3]));
    }
}

class Hotel {
    List<Room> rooms = new ArrayList<>();
    List<Booking> bookings = new ArrayList<>();

    Hotel() {
        loadRooms();
        loadBookings();
    }

    void loadRooms() {
        File file = new File("rooms.txt");
        if (!file.exists()) {
            createDefaultRooms();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                rooms.add(Room.fromString(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveRooms() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("rooms.txt"))) {
            for (Room r : rooms) {
                bw.write(r.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadBookings() {
        File file = new File("bookings.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                bookings.add(Booking.fromString(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveBookings() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("bookings.txt"))) {
            for (Booking b : bookings) {
                bw.write(b.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void createDefaultRooms() {
        rooms.add(new Room("101", "Standard", true));
        rooms.add(new Room("102", "Deluxe", true));
        rooms.add(new Room("103", "Suite", true));
        saveRooms();
    }

    List<Room> searchRooms(String category) {
        List<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (r.category.equalsIgnoreCase(category) && r.isAvailable) {
                available.add(r);
            }
        }
        return available;
    }

    Booking bookRoom(String roomId, String customerName) {
        for (Room r : rooms) {
            if (r.roomId.equals(roomId) && r.isAvailable) {
                r.isAvailable = false;
                Booking b = new Booking(UUID.randomUUID().toString(), roomId, customerName, true);
                bookings.add(b);
                saveRooms();
                saveBookings();
                return b;
            }
        }
        return null;
    }

    boolean cancelBooking(String bookingId) {
        Iterator<Booking> it = bookings.iterator();
        while (it.hasNext()) {
            Booking b = it.next();
            if (b.bookingId.equals(bookingId)) {
                for (Room r : rooms) {
                    if (r.roomId.equals(b.roomId)) {
                        r.isAvailable = true;
                        break;
                    }
                }
                it.remove();
                saveRooms();
                saveBookings();
                return true;
            }
        }
        return false;
    }

    void viewBookings() {
        for (Booking b : bookings) {
            System.out.println("Booking ID: " + b.bookingId +
                    " | Room ID: " + b.roomId +
                    " | Customer: " + b.customerName +
                    " | Payment: " + b.paymentDone);
        }
    }
}

public class HotelReservationSystem {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();

        System.out.println("Available Deluxe Rooms:");
        for (Room r : hotel.searchRooms("Deluxe")) {
            System.out.println("Room ID: " + r.roomId);
        }

        System.out.println("\nBooking Room 102...");
        Booking b = hotel.bookRoom("102", "Alice");
        if (b != null) {
            System.out.println("Booking Successful! ID: " + b.bookingId);
        } else {
            System.out.println("Booking Failed.");
        }

        System.out.println("\nCurrent Bookings:");
        hotel.viewBookings();

        if (b != null) {
            System.out.println("\nCanceling Booking...");
            boolean cancelled = hotel.cancelBooking(b.bookingId);
            System.out.println(cancelled ? "Booking Canceled!" : "Cancel Failed.");
        }

        System.out.println("\nBookings after Cancellation:");
        hotel.viewBookings();
    }
}
