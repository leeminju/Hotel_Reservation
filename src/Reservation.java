import java.util.Date;
import java.util.UUID;

public class Reservation {

    private UUID reserved_ID;
    private Room room;//객실
    private String date;//숙박 날짜
    private Customer customer;

    public Reservation(UUID reserved_ID, Room room, Customer customer, String date) {
        this.reserved_ID = reserved_ID;
        this.room = room;
        this.customer = customer;
        this.date = date;
    }

    public void PrintReservationInfo() {
        System.out.println("예약 번호: " + this.reserved_ID + " | 객실 종류: " + this.room.size + " | 가격: " + this.room.price + " | 숙박 날짜: " + this.date + " | 예약 고객명: " + this.customer.getName()
                + " | 고객 전화번호: " + this.customer.getPhone_number());
    }

    public String getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

}
