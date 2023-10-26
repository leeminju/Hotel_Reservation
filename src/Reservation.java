import java.util.UUID;

public class Reservation {
    // 예약은 객실, 고객의 이름, 고객의 전화번호, 예약 날짜를 가지고 있다.
    //    1. 전화 번호 제한(XXX-XXXX-XXXX) 정규 표현식  **(선택)**
    //    2. 예약 날짜  ****
    //
    //        날짜는 ISO 8601 형식으로
    //        조합된 UTC 날짜 및 시간
    //        예) 2016-10-27T17:13:40+00:00

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
        System.out.println("예약 번호: " + this.reserved_ID + " | 객실 종류: " + this.room.size + " | 숙박 날짜: " + this.date + " | 예약 고객명: " + this.customer.getName()
                + " | 고객 전화번호: " + this.customer.getPhone_number());
    }

    public String getDate() {
        return date;
    }
}
