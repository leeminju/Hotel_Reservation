import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;

public class Hotel {
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    Map<String, Map<RoomType, Integer>> remainRoomByDate = new HashMap<>();
    List<Customer> customers = new ArrayList<>();//고객 목록
    Map<UUID, Reservation> reservations = new HashMap<>();//전체 예약 목록
    Room[] weekdayRoomList = {
            new Room("Standard", 100000),
            new Room("Deluxe", 200000),
            new Room("Suite", 500000)
    };

    Room[] weekendRoomList = {
            new Room("Standard", 130000),
            new Room("Deluxe", 230000),
            new Room("Suite", 530000)
    };

    enum RoomType {
        STANDARD,
        DELUXE,
        SUITE,
    }

    static final int STANDARD = 0;
    static final int DELUXE = 1;
    static final int SUITE = 2;
    final String managerPassword = "1234";
    static final String HOTEL_NAME = "P땀눈물";

    void dataSetting() {
        Customer customer1 = new Customer("이민주", "010-1111-1111", 200000);
        Customer customer2 = new Customer("문형원", "010-2222-2222", 500000);
        Customer customer3 = new Customer("정영도", "010-3333-3333", 300000);
        Customer customer4 = new Customer("홍지운", "010-4444-4444", 400000);

        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
    }

    void printCurrentDateTime() {
        Date current_date = new Date();
        String today_str = dateFormat.format(current_date);
        System.out.println("현재 시간 : " + today_str);
    }

    //고객조회
    int findCustomer(String name, String phone) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (customer.getName().equals(name) && customer.getPhone_number().equals(phone)) {
                return i;
            }
        }
        return -1;
    }

    //해당 번호 존재 유무
    boolean isExistingPhoneNumber(String phone) {
        for (Customer customer : customers) {
            if (phone.equals(customer.getPhone_number())) {
                return true;
            }
        }
        return false;
    }

    //해당 날짜 품절인지 확인
    boolean isSoldOut(Map<RoomType, Integer> remainRooms) {
        for (Integer room_cnt : remainRooms.values()) {
            if (room_cnt > 0)
                return false;
        }
        return true;
    }

    Date StringToDate(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH:mm:ss");
        return formatter.parse(str);
    }

    Date StringToDate2(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return formatter.parse(str);
    }

    boolean isWeekday(DayOfWeek dayOfWeek) {
        if (dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY)
            return false;
        return true;
    }


}
