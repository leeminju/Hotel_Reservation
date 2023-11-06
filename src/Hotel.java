import javax.sound.midi.Soundbank;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;


public class Hotel {
    //호텔은 모든 예약 목록을 조회 할 수 있다.
    //고객 목록 저장
    Scanner sc = new Scanner(System.in);

    Map<String, Integer[]> remainRoomByDate = new HashMap<>();//

    List<Customer> customers = new ArrayList<>();//고객 목록
    Map<UUID, Reservation> reservationMap = new HashMap<>();//전체 예약 목록

    Room[] weekday_roomlist = {
            new Room("Standard", 100000),
            new Room("Deluxe", 200000),
            new Room("Suite", 500000)
    };

    Room[] weekend_roomlist = {
            new Room("Standard", 130000),
            new Room("Deluxe", 230000),
            new Room("Suite", 530000)
    };

    static final int ROOM_KIND = 3;
    static final int STANDARD = 0;
    static final int DELUXE = 1;
    static final int SUITE = 2;

    final String managerPassword = "1234";
    static final String HOTEL_NAME = "P땀눈물";

    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    Customer current_logined_customer;


    void start() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        DataSetting();
        LoginMenu();
    }


    void DataSetting() {
        Customer customer1 = new Customer("이민주", "010-1111-1111", 200000);
        Customer customer2 = new Customer("문형원", "010-2222-2222", 500000);
        Customer customer3 = new Customer("정영도", "010-3333-3333", 300000);
        Customer customer4 = new Customer("홍지운", "010-4444-4444", 400000);

        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
    }

    void PrintCurrentDateTime() {
        Date current_date = new Date();
        String today_str = dateFormat.format(current_date);
        System.out.println("현재 시간 : " + today_str);
    }


    void LoginMenu() {

        PrintCurrentDateTime();
        System.out.println(HOTEL_NAME + " Hotel에 오신것을 환영합니다!");
        System.out.println("1.고객 정보 등록");
        System.out.println("2.로그인");
        System.out.println("3.전체 예약 조회(관리자 전용)");
        System.out.println("4.종료");

        SelectLoginMenu();
    }

    void SelectLoginMenu() {
        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    Signin();
                    break;
                case 2:
                    Login();
                    break;
                case 3:
                    CheckTotalReservation();
                    break;
                case 4:
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
                    SelectLoginMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
            sc = new Scanner(System.in);
            SelectLoginMenu();
        }
    }


    int findCustomer(String name, String phone) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);

            if (customer.getName().equals(name) && customer.getPhone_number().equals(phone)) {
                return i;
            }
        }
        return -1;
    }

    void Login() {
        System.out.println("이름을 입력하세요:");
        sc = new Scanner(System.in);
        String name = sc.nextLine();

        String phone;
        do {

            System.out.println("전화번호를 입력하세요(-포함):");
            phone = sc.nextLine();
            String regEx = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";

            if (Pattern.matches(regEx, phone) && phone.length() == 13) {
                break;
            } else {
                System.out.println("맞는 형식의 전화번호가 아닙니다");
            }

        } while (true);


        int find = findCustomer(name, phone);
        if (find != -1) {
            System.out.println("로그인 완료!");
            current_logined_customer = customers.get(find);
            MainMenu();
        } else {
            System.out.println("회원정보가 없습니다");
            Login();
        }
    }


    void Signin() {

        //동명이인은 허용
        System.out.println("이름을 입력하세요:");
        sc = new Scanner(System.in);
        String name = sc.nextLine();


        String phone;


        //같은 번호 허용X
        while (true) {

            System.out.println("전화번호를 입력하세요(-포함):");
            phone = sc.nextLine();

            String regEx = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";

            if (Pattern.matches(regEx, phone) && phone.length() == 13) {

                if (findPhoneNumber(phone)) {
                    System.out.println("동일한 번호가 존재합니다!");
                    continue;
                }
                break;
            } else {
                System.out.println("맞는 형식의 전화번호가 아닙니다");
                continue;
            }
        }

        int cash = 0;


        while (true) {
            try {
                System.out.println("소지금을 입력하세요:");
                cash = sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                PrintBadInput("숫자");
                sc = new Scanner(System.in);
                continue;
            }
        }
        customers.add(new Customer(name, phone, cash));
        System.out.println("회원 등록이 완료 되었습니다!");
        LoginMenu();


    }

    void MainMenu() {

        PrintCurrentDateTime();
        System.out.println(HOTEL_NAME + " Hotel MENU 입니다!");
        System.out.println("[" + current_logined_customer.getName() + "님이 로그인 중입니다!    잔액 :" + current_logined_customer.cash + "]");

        System.out.println("1. 객실 예약");
        System.out.println("2. " + current_logined_customer.getName() + "님의 예약 목록 조회");//로그인 된 사람의 예약 목록
        System.out.println("3. 캐시 충전");
        System.out.println("4. 회원 정보 변경");
        System.out.println("5. 로그아웃");//원래 메뉴로 돌아가기!

        SelectMainMenu();
    }

    private void SelectMainMenu() {
        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    ReservedRoom();
                    break;
                case 2:
                    CheckMyReservation();
                    break;
                case 3:

                    ChargeCash();
                    break;
                case 4:
                    ChangeCustomerInfo();

                case 5:
                    Logout();
                    break;
                default:
                    System.out.println("잘못 입력하셨습니다! 다시 입력해주세요");
                    SelectLoginMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("잘못 입력하셨습니다! 다시 입력해주세요");
            sc = new Scanner(System.in);
            SelectLoginMenu();
        }
    }


    private void ChangeCustomerInfo() {
        System.out.println("무엇을 변경 하시겠습니까??");
        System.out.println("1.이름");
        System.out.println("2.전화번호");
        System.out.println();
        System.out.println("0.메인 메뉴");

        int select = sc.nextInt();

        try {
            switch (select) {
                case 0:
                    TimeSleep("메인 메뉴");
                    MainMenu();
                    break;
                case 1:
                    ChangeName();
                    break;
                case 2:
                    ChangePhoneNumber();
                    break;
                default:
                    PrintBadInput("숫자");
                    ChangeCustomerInfo();
            }
        } catch (InputMismatchException e) {
            PrintBadInput("숫자");
            sc = new Scanner(System.in);
            ChangeCustomerInfo();
        }
    }

    boolean findPhoneNumber(String phone) {
        for (Customer customer : customers) {
            if (phone.equals(customer.getPhone_number())) {
                return true;
            }
        }
        return false;
    }

    private void ChangePhoneNumber() {
        sc = new Scanner(System.in);
        String current = current_logined_customer.getPhone_number();
        System.out.println("현재 전화 번호는 " + current + " 입니다!");


        String change_phone;
        loop:
        while (true) {
            System.out.println("새로운 전화 번호을 입력해 주세요!(-포함)");
            change_phone = sc.nextLine();
            String regEx = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";

            if (Pattern.matches(regEx, change_phone) && change_phone.length() == 13) {
                if (current.equals(change_phone)) {
                    System.out.println("현재 번호와 동일합니다!");
                    continue;
                }

                if (findPhoneNumber(change_phone)) {
                    System.out.println("동일한 번호의 사용자가 존재합니다!");
                    continue;
                }

                current_logined_customer.setPhone_number(change_phone);
                System.out.println("전화번호가 변경 되었습니다!");
                break;
            } else {
                System.out.println("맞는 형식의 전화번호가 아닙니다");
                continue;
            }
        }
        TimeSleep("회원 정보 변경");
        ChangeCustomerInfo();

    }

    private void ChangeName() {
        String current = current_logined_customer.getName();
        sc = new Scanner(System.in);
        System.out.println("현재 이름은 " + current + "입니다!");
        do {
            System.out.println("새로운 이름을 입력해 주세요!");
            String change_name = sc.nextLine();

            if (current.equals(change_name)) {
                System.out.println("현재 이름과 동일합니다");
                continue;
            }
            current_logined_customer.setName(change_name);
            break;
        } while (true);
        System.out.println("이름이 변경 되었습니다!");

        TimeSleep("회원 정보 변경");
        ChangeCustomerInfo();
    }


    private void ChargeCash() {
        int plus_cash = 0;
        System.out.println("얼마를 충전하시겠습니까?");
        try {
            plus_cash = sc.nextInt();
        } catch (InputMismatchException e) {
            PrintBadInput("숫자");
            ChargeCash();
        }

        current_logined_customer.chargeCash(plus_cash);
        System.out.println(plus_cash + "원을 충전하여 현재 잔액은 " + current_logined_customer.cash + "원 입니다!");
        TimeSleep("메인 메뉴");
        MainMenu();
    }

    private void CheckMyReservation() {
        List<UUID> my_reservation = current_logined_customer.getReservationList(); //예약순으로 저장

        if (my_reservation.isEmpty()) {
            System.out.println("현재 예약된 정보가 없습니다.");
            TimeSleep("메인 메뉴");
            MainMenu();
        }

        System.out.println("[" + current_logined_customer.getName() + "님의 예약 목록 ]");
        int idx = 1;
        for (int i = 0; i < my_reservation.size(); i++) {
            UUID uuid = my_reservation.get(i);
            Reservation reservation = reservationMap.get(uuid);
            System.out.print(idx++ + ". ");
            reservation.PrintReservationInfo();
        }
        System.out.println("예약 취소를 진행하시겠습니까?");
        System.out.println("1.네    2. 아니요");
        SelectCancel("메인 메뉴");
    }


    private void CheckTotalReservation() {

        List<UUID> keySet = new ArrayList<>(reservationMap.keySet());

        // 날짜순으로 정렬
        keySet.sort((o1, o2) -> reservationMap.get(o1).getDate().compareTo(reservationMap.get(o2).getDate()));


        System.out.println("관리자 비밀번호를 입력하세요.");
        Scanner sc = new Scanner(System.in);
        String insertPassword = sc.nextLine(); //직원이 입력하는 비밀번호

        // if문 활용해서 관리자 비밀번호 일치하는지 여부 확인
        if (insertPassword.equals(managerPassword)) {
            if (reservationMap.isEmpty()) {
                System.out.println("현재 예약된 객실이 존재하지 않습니다."); //예약 없을 경우
                TimeSleep("시작 메뉴");
                LoginMenu();
            } else {
                //호텔 전체 예약 목록 띄우기
                System.out.println("[ 전체 예약 목록 ]");
                int i = 1;
                for (UUID uuid : keySet) {
                    Reservation reservation = reservationMap.get(uuid);
                    System.out.print(i++ + ". ");
                    reservation.PrintReservationInfo();
                }
                System.out.println();

                System.out.println("예약 취소를 진행하시겠습니까?");
                System.out.println("1.네    2. 아니요");
                SelectCancel("시작 메뉴");
            }
        } else {
            System.out.println("잘못된 접근입니다.");
            CheckTotalReservation();
        }

    }

    void Cancel(UUID uuid) {
        Reservation reservation = reservationMap.get(uuid);// 예약정보

        String size = reservation.getRoom().size;
        int room_price = reservation.getRoom().price;
        String reseved_date = reservation.getDate();

        reservation.getCustomer().chargeCash(room_price);//환불
        reservation.getCustomer().removeReseved_id(uuid);//고객의 예약번호 리스트에서도 삭제
        reservationMap.remove(uuid);//전체 맵에서도 삭제(키값으로 삭제)

        Integer[] remain_rooms = remainRoomByDate.get(reseved_date);//잔여객실 수 증가
        if (size.equals("Standard")) {
            remain_rooms[STANDARD]++;
        } else if (size.equals("Deluxe")) {
            remain_rooms[DELUXE]++;
        } else if (size.equals("Suite")) {
            remain_rooms[SUITE]++;
        }

        //remainRoomByDate.put(reseved_date, remain_rooms); ->이걸 안해줘도 괜찮다! Integer[]라서!
    }

    void SelectCancel(String next) {
        try {
            int select = sc.nextInt();
            if (select == 1) {
                UUID uuid;
                while (true) {
                    System.out.println("취소할 예약번호를 입력하세요:");
                    sc = new Scanner(System.in);
                    String reseved_id = sc.nextLine();

                    try {
                        uuid = UUID.fromString(reseved_id);
                    } catch (IllegalArgumentException e) {
                        System.out.println("예약번호 형식이 맞지 않습니다");
                        continue;
                    }
                    if (reservationMap.containsKey(uuid)) {

                        if (next.equals("메인 메뉴")) {
                            //if (reservationMap.get(uuid).getCustomer().equals(current_logined_customer)) {// -> 안됨
                            if (reservationMap.get(uuid).getCustomer() != current_logined_customer) {// 주소값 비교, 비교하려는 객체가 동일한 객체인지를 판별
                                System.out.println("본인의 예약 정보가 아니라 삭제 불가합니다!");
                                continue;
                            }
                        }


                        reservationMap.get(uuid).PrintReservationInfo();
                        System.out.println("해당 예약 취소하시겠습니까?");
                        System.out.println("1.네   2.아니요");
                        break;
                    } else {
                        System.out.println("해당 번호가 없습니다!");
                        continue;
                    }
                }

                int choice = sc.nextInt();
                if (choice == 1) {
                    Cancel(uuid);
                    System.out.println("예약 취소와 환불 진행 되었습니다!");

                    if (next.equals("시작 메뉴")) {
                        TimeSleep("시작 메뉴");
                        LoginMenu();
                    } else if (next.equals("메인 메뉴")) {
                        TimeSleep("메인 메뉴");
                        MainMenu();
                    }

                } else if (choice == 2) {
                    if (next.equals("시작 메뉴")) {
                        TimeSleep("시작 메뉴");
                        LoginMenu();
                    } else if (next.equals("메인 메뉴")) {
                        TimeSleep("메인 메뉴");
                        MainMenu();
                    }
                }


            } else if (select == 2) {
                if (next.equals("시작 메뉴")) {
                    TimeSleep("시작 메뉴");
                    LoginMenu();
                } else if (next.equals("메인 메뉴")) {
                    TimeSleep("메인 메뉴");
                    MainMenu();
                }
            } else {
                PrintBadInput("숫자");
                SelectCancel(next);
            }
        } catch (InputMismatchException e) {
            PrintBadInput("숫자");
            sc = new Scanner(System.in);
            SelectCancel(next);
        }
    }

    private Date StringToDate(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH:mm:ss");
        return formatter.parse(str);
    }

    private Date StringToDate2(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return formatter.parse(str);
    }

    void ReservedRoom() {

        //날짜 입력 받기
        String date_str;
        int daynum;
        String dayOfWeek;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("숙박 날짜를 입력해주세요(숫자8개): ");
                String date = sc.nextLine();

                if (date.length() != 8 || date.contains("[^0-9]")) {
                    System.out.println("날짜형식이 잘못 되었습니다! 다시 입력해주세요!");
                    continue;
                }

                // 문자열
                String checkInDate = date + "15:00:00";

                // 문자열 -> Date
                Date input_date = StringToDate(checkInDate);
                date_str = dateFormat.format(input_date);

                Calendar cal = Calendar.getInstance();

                cal.add(Calendar.DATE, 30);
                Date after_month_date = cal.getTime();//한달 뒤
                String after_month_str = dateFormat.format(after_month_date);

                if (input_date.before(new Date())) {
                    System.out.println("지난 날짜는 예약 불가합니다!");
                    continue;
                }
                if (input_date.after(after_month_date)) {
                    System.out.println(after_month_str + "이후 예약 불가합니다!");
                    continue;
                }

                cal.setTime(input_date);//입력 날짜
                daynum = cal.get(Calendar.DAY_OF_WEEK);//요일 1-일요일 7-토요일  2-6번 주중
                dayOfWeek = getDayStr(daynum);
                break;

            } catch (ParseException e) {
                System.out.println("날짜형식이 잘못 되었습니다! 다시 입력해주세요!");
                continue;
            }
        }

        if (!remainRoomByDate.containsKey(date_str)) {

            //날짜 없으면(예약된 게 없다)
            Integer[] arr = {4, 4, 4};// 각 크기 당 10개방 배치

            remainRoomByDate.put(date_str, arr);//
        }

        //객실정보 가져오기
        Integer[] select_date_rooms = remainRoomByDate.get(date_str);


        if (isSoldOut(select_date_rooms)) {
            System.out.println("해당 날짜에 남은 객실이 없습니다!!");
            System.out.println("1.날짜 입력      2. 메인 메뉴");
            SelectReturnReserve();
            return;
        }

        ShowRemainRooms(date_str, dayOfWeek);
    }


    boolean isWeekday(String day) {
        if (day.equals("SUN") || day.equals(("SAT")))
            return false;

        return true;
    }

    public String getDayStr(int dayNum) {
        String day = "";
        switch (dayNum) {
            case 1:
                day = "SUN";
                break;
            case 2:
                day = "MON";
                break;
            case 3:
                day = "TUE";
                break;
            case 4:
                day = "WED";
                break;
            case 5:
                day = "THU";
                break;
            case 6:
                day = "FRI";
                break;
            case 7:
                day = "SUN";
                break;
        }
        return day;
    }

    void SelectReturnReserve() {

        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    ReservedRoom();
                    break;
                case 2:
                    MainMenu();
                    break;
                default:
                    PrintBadInput("숫자");
                    SelectReturnReserve();

            }
        } catch (InputMismatchException e) {
            PrintBadInput("숫자");
            sc = new Scanner(System.in);
            SelectReturnReserve();

        }
    }

    boolean isSoldOut(Integer[] select_date_rooms) {
        for (int i = 0; i < ROOM_KIND; i++) {
            if (select_date_rooms[i] > 0) {
                return false;
            }
        }
        return true;
    }

    private void ShowRemainRooms(String date, String dayOfWeek) {
        Integer[] select_date_rooms = remainRoomByDate.get(date);
        System.out.println(date + "(" + dayOfWeek + ")" + " 객실 현황 입니다");

        Room[] current;
        boolean isweekday = isWeekday(dayOfWeek);
        if (isWeekday(dayOfWeek)) {
            current = weekday_roomlist;
            System.out.println("[주중 가격]");
        } else {
            current = weekend_roomlist;
            System.out.println("[주말 가격]");
        }


        for (int i = 0; i < ROOM_KIND; i++) {
            System.out.println((i + 1) + "." + current[i].size + "(" + select_date_rooms[i] + "개 남음)          가격:" + current[i].price + "원");
        }


        System.out.println("0. 메인 메뉴");

        SelectRoomSize(date, dayOfWeek);
    }


    private void SelectRoomSize(String date, String dayOfweek) {
        Integer[] select_date_rooms = remainRoomByDate.get(date);
        try {
            int select = sc.nextInt();
            if (1 <= select && select <= ROOM_KIND) {
                int room_size = select - 1;// 실제 배열은 0-2까지라서 -1 해준다!

                if (select_date_rooms[room_size] == 0) {
                    System.out.println("해당 객실은 예약 마감되었습니다");
                    System.out.println("다른 객실을 선택해주세요!");
                    SelectRoomSize(date, dayOfweek);
                } else {
                    Room[] current;
                    if (isWeekday(dayOfweek)) {
                        current = weekday_roomlist;
                    } else {
                        current = weekend_roomlist;
                    }

                    System.out.println(date + "에 " + current[select - 1].size + " Room을 예약 하시겠습니까??");
                    System.out.println("가격은 " + current[select - 1].price + "원 입니다!");

                    System.out.println(current_logined_customer.getName() + "님 잔액 :  " + current_logined_customer.cash + "원");
                    System.out.println("결제 하시겠습니까??");
                    System.out.println("1. 결제    2.취소");
                    Confirmpayment(date, room_size, dayOfweek);
                }
            } else if (select == 0) {
                TimeSleep("메인 메뉴");
                MainMenu();
            } else {
                PrintBadInput("숫자");
                SelectRoomSize(date, dayOfweek);
            }

        } catch (InputMismatchException e) {
            sc = new Scanner(System.in);
            SelectRoomSize(date, dayOfweek);
        }
    }

    //결제 확인
    private void Confirmpayment(String date, int room_size, String dayOfWeek) {
        Integer[] select_date_rooms = remainRoomByDate.get(date);
        try {
            int select = sc.nextInt();
            switch (select) {
                //결제
                case 1:
                    Room[] current;
                    if (isWeekday(dayOfWeek)) {
                        current = weekday_roomlist;
                    } else {
                        current = weekend_roomlist;
                    }

                    if (current_logined_customer.cash >= current[room_size].price) {
                        current_logined_customer.cash -= current[room_size].price;//캐쉬 차감

                        UUID reserved_id = UUID.randomUUID(); //UUid 생성
                        Reservation reservation = new Reservation(reserved_id, current[room_size], current_logined_customer, date);

                        reservationMap.put(reserved_id, reservation);//예약 Map에 추가
                        current_logined_customer.addReservedId(reserved_id);//로그인된 손님에게 예약번호 저장

                        select_date_rooms[room_size] -= 1;//해당 크기 방 개수 1 감소
                        remainRoomByDate.put(date, select_date_rooms);
                        System.out.println("예약번호 : " + reserved_id);
                        System.out.println(date + " 체크인");


                        Date checkin = StringToDate2(date);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(checkin);
                        cal.add(Calendar.DATE, 1);//다음 날
                        cal.add(Calendar.HOUR, -4);//4시간 전
                        String checkout = dateFormat.format(cal.getTime());

                        System.out.println(checkout + " 체크아웃");
                        System.out.println(current[room_size].size + " Room 예약 완료!!!");

                        TimeSleep("메인 메뉴");
                        MainMenu();
                    } else {
                        System.out.println("잔액이 부족합니다!");
                        TimeSleep("객실 선택");
                        ShowRemainRooms(date, dayOfWeek);
                    }
                    break;
                //취소
                case 2:
                    TimeSleep("객실 선택");
                    ShowRemainRooms(date, dayOfWeek);
                default:
                    PrintBadInput("숫자");
                    Confirmpayment(date, room_size, dayOfWeek);
            }
        } catch (InputMismatchException e) {
            PrintBadInput("숫자");
            sc = new Scanner(System.in);
            Confirmpayment(date, room_size, dayOfWeek);
        } catch (ParseException e) {
            throw new RuntimeException(e);

        }
    }


    private void Logout() {
        System.out.println("로그아웃 하시겠습니까??");
        System.out.println("1. 네       2. 아니요");

        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    System.out.println("로그아웃 합니다");
                    current_logined_customer = null;
                    LoginMenu();
                case 2:
                    TimeSleep("메인 메뉴");
                    MainMenu();
                default:
                    PrintBadInput("숫자");
                    Logout();
            }
        } catch (InputMismatchException e) {
            PrintBadInput("숫자");
            sc = new Scanner(System.in);
            Logout();
        }

    }


    void TimeSleep(String menu) {
        System.out.println("3초 후 " + menu + "로 이동합니다!");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void PrintBadInput(String type) {
        System.out.println("잘못 입력하셨습니다 " + type + "를 다시 입력해주세요");
    }

}