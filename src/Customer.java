import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    //고객은 이름, 전화번호, 소지금을 가진다.
    // 고객 소지금보다 비싼 방은 예약 불가
    String name;
    String phone_number;//하이픈?
    int cash;

    List<UUID> reservationList = new ArrayList<>();//예약 번호 저장

    public Customer(String name, String phone_number, int cash) {
        this.name = name;
        this.phone_number = phone_number;
        this.cash = cash;
    }

    void addReservedId(UUID uuid){
        reservationList.add(uuid);
    }

}
