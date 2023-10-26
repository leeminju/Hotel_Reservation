import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    //고객은 이름, 전화번호, 소지금을 가진다.
    // 고객 소지금보다 비싼 방은 예약 불가
    private String name;
    private String phone_number;//하이픈?
    int cash;

    private List<UUID> reservationList = new ArrayList<>();//예약 번호 저장

    public Customer(String name, String phone_number, int cash) {
        this.name = name;
        this.phone_number = phone_number;
        this.cash = cash;
    }

    void addReservedId(UUID uuid) {
        reservationList.add(uuid);
    }

    void chargeCash(int plus_cash) {
        this.cash += plus_cash;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void removeReseved_id(UUID uuid){
        reservationList.remove(uuid);
    }

}
