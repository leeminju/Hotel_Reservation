import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;


public class HotelApplication extends Hotel {

    //호텔은 모든 예약 목록을 조회 할 수 있다.
    //고객 목록 저장
    Scanner sc = new Scanner(System.in);
    Customer currentLoginedCustomer;

    void start() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        dataSetting();
        loginMenu();
    }

    //로그인 메뉴
    private void loginMenu() {
        printCurrentDateTime();
        System.out.println(HOTEL_NAME + " Hotel에 오신것을 환영합니다!");
        System.out.println("1.고객 정보 등록");
        System.out.println("2.로그인");
        System.out.println("3.전체 예약 조회(관리자 전용)");
        System.out.println("4.종료");

        selectLoginMenu();
    }

    //로그인 메뉴 선택
    private void selectLoginMenu() {
        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    signup();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    checkTotalReservation();
                    break;
                case 4:
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
                    selectLoginMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
            sc = new Scanner(System.in);
            selectLoginMenu();
        }
    }

    //1. 회원가입
    private void signup() {
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
                if (isExistingPhoneNumber(phone)) {
                    System.out.println("동일한 번호가 존재합니다!");
                    continue;
                }
                break;
            } else {
                System.out.println("맞는 형식의 전화번호가 아닙니다");
            }
        }

        int cash;

        while (true) {
            try {
                System.out.println("소지금을 입력하세요:");
                cash = sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                printBadInput("숫자");
                sc = new Scanner(System.in);
                continue;
            }
        }
        customers.add(new Customer(name, phone, cash));
        System.out.println("회원 등록이 완료 되었습니다!");
        loginMenu();
    }

    //2. 로그인
    private void login() {
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
            currentLoginedCustomer = customers.get(find);
            mainMenu();
        } else {
            System.out.println("회원정보가 없습니다");
            login();
        }
    }

    //3.전체 예약 목록
    private void checkTotalReservation() {
        List<UUID> keySet = new ArrayList<>(reservations.keySet());
        // 날짜순으로 정렬
        keySet.sort((o1, o2) -> reservations.get(o1).getDate().compareTo(reservations.get(o2).getDate()));

        System.out.println("관리자 비밀번호를 입력하세요.");
        Scanner sc = new Scanner(System.in);
        String insertPassword = sc.nextLine(); //직원이 입력하는 비밀번호

        // if문 활용해서 관리자 비밀번호 일치하는지 여부 확인
        if (insertPassword.equals(managerPassword)) {
            if (reservations.isEmpty()) {
                System.out.println("현재 예약된 객실이 존재하지 않습니다."); //예약 없을 경우
                timeSleep("시작 메뉴");
                loginMenu();
            } else {
                //호텔 전체 예약 목록 띄우기
                System.out.println("[ 전체 예약 목록 ]");
                int i = 1;
                for (UUID uuid : keySet) {
                    Reservation reservation = reservations.get(uuid);
                    System.out.print(i++ + ". ");
                    reservation.PrintReservationInfo();
                }
                System.out.println();

                System.out.println("예약 취소를 진행하시겠습니까?");
                System.out.println("1.네    2. 아니요");
                selectCancel("시작 메뉴");
            }
        } else {
            System.out.println("잘못된 접근입니다.");
            checkTotalReservation();
        }
    }


    //메인메뉴
    private void mainMenu() {
        printCurrentDateTime();
        System.out.println(HOTEL_NAME + " Hotel MENU 입니다!");
        System.out.println("[" + currentLoginedCustomer.getName() + "님이 로그인 중입니다!    잔액 :" + currentLoginedCustomer.cash + "]");

        System.out.println("1. 객실 예약");
        System.out.println("2. " + currentLoginedCustomer.getName() + "님의 예약 목록 조회");//로그인 된 사람의 예약 목록
        System.out.println("3. 캐시 충전");
        System.out.println("4. 회원 정보 변경");
        System.out.println("5. 로그아웃");//원래 메뉴로 돌아가기!

        selectMainMenu();
    }

    private void selectMainMenu() {
        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    reserveRoom();
                    break;
                case 2:
                    checkMyReservation();
                    break;
                case 3:
                    chargeCash();
                    break;
                case 4:
                    changeCustomerInfo();
                case 5:
                    logout();
                    break;
                default:
                    System.out.println("잘못 입력하셨습니다! 다시 입력해주세요");
                    selectLoginMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("잘못 입력하셨습니다! 다시 입력해주세요");
            sc = new Scanner(System.in);
            selectLoginMenu();
        }
    }

    //1. 예약하기
    private void reserveRoom() {
        //날짜 입력 받기
        String date_str;
        DayOfWeek dayOfWeek;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("숙박 날짜를 입력해주세요(숫자8개): ");
                String date = sc.nextLine();

                if (date.length() != 8 || date.contains("[^0-9]")) {
                    System.out.println("날짜형식이 잘못 되었습니다! 다시 입력해주세요!");
                    continue;
                }
                String checkInDate = date + "15:00:00";//날짜에 시간 추가
                Date input_date = StringToDate(checkInDate);//Date 타입으로 변경
                date_str = dateFormat.format(input_date);//ISO 형식으로 변환

                //요일 구하기
                LocalDate localDate = input_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dayOfWeek = localDate.getDayOfWeek();

                //한달 뒤 날짜 구하기
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
                break;
            } catch (ParseException e) {
                System.out.println("날짜형식이 잘못 되었습니다! 다시 입력해주세요!");
            }
        }

        if (!remainRoomByDate.containsKey(date_str)) {
            //날짜 없으면(예약된 게 없다)
            Map<RoomType, Integer> remainRooms = new HashMap<>();
            remainRooms.put(RoomType.STANDARD, 5);
            remainRooms.put(RoomType.DELUXE, 5);
            remainRooms.put(RoomType.SUITE, 5);

            remainRoomByDate.put(date_str, remainRooms);
        }

        //객실정보 가져오기
        Map<RoomType, Integer> remainRooms = remainRoomByDate.get(date_str);

        if (isSoldOut(remainRooms)) {
            System.out.println("해당 날짜에 남은 객실이 없습니다!!");
            System.out.println("1.날짜 입력      2. 메인 메뉴");
            selectReturnReserve();
        }

        showRemainRooms(date_str, dayOfWeek);
    }

    //1번누르면 예약하기 2번누르면 메인메뉴
    private void selectReturnReserve() {
        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    reserveRoom();
                    break;
                case 2:
                    mainMenu();
                    break;
                default:
                    printBadInput("숫자");
                    selectReturnReserve();
            }
        } catch (InputMismatchException e) {
            printBadInput("숫자");
            sc = new Scanner(System.in);
            selectReturnReserve();
        }
    }

    //객실 현황 선택
    private void showRemainRooms(String date, DayOfWeek dayOfWeek) {
        Map<RoomType, Integer> remainRooms = remainRoomByDate.get(date);
        System.out.println(date + "(" + dayOfWeek + ")" + " 객실 현황 입니다");

        Room[] current;
        if (isWeekday(dayOfWeek)) {
            current = weekdayRoomList;
            System.out.println("[주중 가격]");
        } else {
            current = weekendRoomList;
            System.out.println("[주말 가격]");
        }

        System.out.println("1." + current[STANDARD].size + "(" + remainRooms.get(RoomType.STANDARD) + "개 남음)          가격:" + current[STANDARD].price + "원");
        System.out.println("2." + current[DELUXE].size + "(" + remainRooms.get(RoomType.DELUXE) + "개 남음)          가격:" + current[SUITE].price + "원");
        System.out.println("3." + current[SUITE].size + "(" + remainRooms.get(RoomType.SUITE) + "개 남음)          가격:" + current[DELUXE].price + "원");

        System.out.println("0. 메인 메뉴");
        selectRoomSize(date, dayOfWeek);
    }

    //방 크기 선택
    private void selectRoomSize(String date, DayOfWeek dayOfweek) {
        Map<RoomType, Integer> remainRooms = remainRoomByDate.get(date);
        try {
            int select = sc.nextInt();
            if (1 <= select && select <= 3) {
                int room_size = select - 1;// 실제 배열은 0-2까지라서 -1 해준다!

                int cnt = 0;

                switch (room_size) {
                    case STANDARD -> {
                        cnt = remainRooms.get(RoomType.STANDARD);
                    }
                    case DELUXE -> {
                        cnt = remainRooms.get(RoomType.DELUXE);
                    }
                    case SUITE -> {
                        cnt = remainRooms.get(RoomType.SUITE);
                    }
                }

                if (cnt == 0) {
                    System.out.println("해당 객실은 예약 마감되었습니다");
                    System.out.println("다른 객실을 선택해주세요!");
                    selectRoomSize(date, dayOfweek);
                } else {
                    Room[] current;
                    if (isWeekday(dayOfweek)) {
                        current = weekdayRoomList;
                    } else {
                        current = weekendRoomList;
                    }

                    System.out.println(date + "에 " + current[select - 1].size + " Room을 예약 하시겠습니까??");
                    System.out.println("가격은 " + current[select - 1].price + "원 입니다!");

                    System.out.println(currentLoginedCustomer.getName() + "님 잔액 :  " + currentLoginedCustomer.cash + "원");
                    System.out.println("결제 하시겠습니까??");
                    System.out.println("1. 결제    2.취소");
                    confirmPayment(date, room_size, dayOfweek);
                }
            } else if (select == 0) {
                timeSleep("메인 메뉴");
                mainMenu();
            } else {
                printBadInput("숫자");
                selectRoomSize(date, dayOfweek);
            }

        } catch (InputMismatchException e) {
            sc = new Scanner(System.in);
            selectRoomSize(date, dayOfweek);
        }
    }

    private void payAndReserve(String date, int room_size, DayOfWeek dayOfWeek) {
        Map<RoomType, Integer> remainRooms = remainRoomByDate.get(date);
        Room[] current;

        if (isWeekday(dayOfWeek)) {
            current = weekdayRoomList;
        } else {
            current = weekendRoomList;
        }

        if (currentLoginedCustomer.cash >= current[room_size].price) {
            currentLoginedCustomer.cash -= current[room_size].price;//캐쉬 차감

            UUID reserved_id = UUID.randomUUID(); //UUid 생성
            Reservation reservation = new Reservation(reserved_id, current[room_size], currentLoginedCustomer, date);

            reservations.put(reserved_id, reservation);//예약 Map에 추가
            currentLoginedCustomer.addReservedId(reserved_id);//로그인된 손님에게 예약번호 저장

            //해당 크기 방 개수 1 감소
            switch (room_size) {
                case STANDARD -> {
                    remainRooms.put(RoomType.STANDARD, remainRooms.get(RoomType.STANDARD) - 1);
                }
                case DELUXE -> {
                    remainRooms.put(RoomType.DELUXE, remainRooms.get(RoomType.DELUXE) - 1);
                }
                case SUITE -> {
                    remainRooms.put(RoomType.SUITE, remainRooms.get(RoomType.SUITE) - 1);
                }
            }

            System.out.println("예약번호 : " + reserved_id);
            System.out.println(date + " 체크인");
            Date checkin = new Date();

            try {
                checkin = StringToDate2(date);//String -> Date
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(checkin);
            cal.add(Calendar.DATE, 1);//다음 날
            cal.add(Calendar.HOUR, -4);//4시간 전
            String checkout = dateFormat.format(cal.getTime());

            System.out.println(checkout + " 체크아웃");
            System.out.println(current[room_size].size + " Room 예약 완료!!!");

            timeSleep("메인 메뉴");
            mainMenu();
        } else {
            System.out.println("잔액이 부족합니다!");
            timeSleep("객실 선택");
            showRemainRooms(date, dayOfWeek);
        }
    }

    //결제 확인
    private void confirmPayment(String date, int room_size, DayOfWeek dayOfWeek) {
        try {
            int select = sc.nextInt();
            switch (select) {
                //결제
                case 1:
                    payAndReserve(date, room_size, dayOfWeek);
                    break;
                //취소
                case 2:
                    timeSleep("객실 선택");
                    showRemainRooms(date, dayOfWeek);
                default:
                    printBadInput("숫자");
                    confirmPayment(date, room_size, dayOfWeek);
            }
        } catch (InputMismatchException e) {
            printBadInput("숫자");
            sc = new Scanner(System.in);
            confirmPayment(date, room_size, dayOfWeek);
        }

    }

    //2.나의 예약내역 조회
    private void checkMyReservation() {
        List<UUID> my_reservation = currentLoginedCustomer.getReservationList(); //예약순으로 저장
        if (my_reservation.isEmpty()) {
            System.out.println("현재 예약된 정보가 없습니다.");
            timeSleep("메인 메뉴");
            mainMenu();
        }

        System.out.println("[" + currentLoginedCustomer.getName() + "님의 예약 목록 ]");
        int idx = 1;
        for (UUID uuid : my_reservation) {
            Reservation reservation = reservations.get(uuid);
            System.out.print(idx++ + ". ");
            reservation.PrintReservationInfo();
        }

        System.out.println("예약 취소를 진행하시겠습니까?");
        System.out.println("1.네    2. 아니요");
        selectCancel("메인 메뉴");
    }

    private void confirmCancel(String next) {
        UUID uuid;
        while (true) {
            System.out.println("취소할 예약번호를 입력하세요:");
            sc = new Scanner(System.in);
            String resevedId = sc.nextLine();

            try {
                uuid = UUID.fromString(resevedId);
            } catch (IllegalArgumentException e) {
                System.out.println("예약 번호 형식이 맞지 않습니다");
                continue;
            }

            if (reservations.containsKey(uuid)) {
                if (next.equals("메인 메뉴")) {
                    if (reservations.get(uuid).getCustomer() != currentLoginedCustomer) {// 주소값 비교, 비교하려는 객체가 동일한 객체인지를 판별
                        System.out.println("본인의 예약 정보가 아니라 삭제 불가합니다!");
                        continue;
                    }
                }

                reservations.get(uuid).PrintReservationInfo();

                System.out.println("해당 예약 취소 하시겠습니까?");
                System.out.println("1.네   2.아니요");
                break;
            } else {
                System.out.println("해당 번호가 없습니다!");
            }
        }

        int choice = sc.nextInt();
        if (choice == 1) {
            cancel(uuid);
            System.out.println("예약 취소와 환불 진행되었습니다!");

            if (next.equals("시작 메뉴")) {
                timeSleep("시작 메뉴");
                loginMenu();
            } else if (next.equals("메인 메뉴")) {
                timeSleep("메인 메뉴");
                mainMenu();
            }

        } else if (choice == 2) {
            if (next.equals("시작 메뉴")) {
                timeSleep("시작 메뉴");
                loginMenu();
            } else if (next.equals("메인 메뉴")) {
                timeSleep("메인 메뉴");
                mainMenu();
            }
        }


    }

    private void selectCancel(String next) {
        try {
            int select = sc.nextInt();
            if (select == 1) {
                confirmCancel(next);
            } else if (select == 2) {
                if (next.equals("시작 메뉴")) {
                    timeSleep("시작 메뉴");
                    loginMenu();
                } else if (next.equals("메인 메뉴")) {
                    timeSleep("메인 메뉴");
                    mainMenu();
                }
            } else {
                printBadInput("숫자");
                selectCancel(next);
            }
        } catch (InputMismatchException e) {
            printBadInput("숫자");
            sc = new Scanner(System.in);
            selectCancel(next);
        }
    }

    //3. 캐시 충전
    private void chargeCash() {
        int plus_cash = 0;
        System.out.println("얼마를 충전하시겠습니까?");
        try {
            plus_cash = sc.nextInt();
        } catch (InputMismatchException e) {
            printBadInput("숫자");
            chargeCash();
        }
        currentLoginedCustomer.chargeCash(plus_cash);
        System.out.println(plus_cash + "원을 충전하여 현재 잔액은 " + currentLoginedCustomer.cash + "원 입니다!");
        timeSleep("메인 메뉴");
        mainMenu();
    }


    //4.회원정보 변경
    private void changeCustomerInfo() {
        System.out.println("무엇을 변경 하시겠습니까??");
        System.out.println("1.이름");
        System.out.println("2.전화번호");
        System.out.println();
        System.out.println("0.메인 메뉴");

        int select = sc.nextInt();

        try {
            switch (select) {
                case 0:
                    timeSleep("메인 메뉴");
                    mainMenu();
                    break;
                case 1:
                    changeName();
                    break;
                case 2:
                    changePhoneNumber();
                    break;
                default:
                    printBadInput("숫자");
                    changeCustomerInfo();
            }
        } catch (InputMismatchException e) {
            printBadInput("숫자");
            sc = new Scanner(System.in);
            changeCustomerInfo();
        }
    }

    //이름 변경
    private void changeName() {
        String current = currentLoginedCustomer.getName();
        sc = new Scanner(System.in);
        System.out.println("현재 이름은 " + current + "입니다!");
        do {
            System.out.println("새로운 이름을 입력해 주세요!");
            String change_name = sc.nextLine();

            if (current.equals(change_name)) {
                System.out.println("현재 이름과 동일합니다");
                continue;
            }
            currentLoginedCustomer.setName(change_name);
            break;
        } while (true);
        System.out.println("이름이 변경 되었습니다!");

        timeSleep("회원 정보 변경");
        changeCustomerInfo();
    }

    //번호 변경
    private void changePhoneNumber() {
        sc = new Scanner(System.in);
        String current = currentLoginedCustomer.getPhone_number();
        System.out.println("현재 전화 번호는 " + current + " 입니다!");

        String change_phone;
        while (true) {
            System.out.println("새로운 전화 번호을 입력해 주세요!(-포함)");
            change_phone = sc.nextLine();
            String regEx = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";

            if (Pattern.matches(regEx, change_phone) && change_phone.length() == 13) {
                if (current.equals(change_phone)) {
                    System.out.println("현재 번호와 동일합니다!");
                    continue;
                }

                if (isExistingPhoneNumber(change_phone)) {
                    System.out.println("동일한 번호의 사용자가 존재합니다!");
                    continue;
                }

                currentLoginedCustomer.setPhone_number(change_phone);
                System.out.println("전화번호가 변경 되었습니다!");
                break;
            } else {
                System.out.println("맞는 형식의 전화번호가 아닙니다");
            }
        }
        timeSleep("회원 정보 변경");
        changeCustomerInfo();

    }


    private void cancel(UUID uuid) {
        Reservation reservation = reservations.get(uuid);// 예약정보

        String size = reservation.getRoom().size;
        int room_price = reservation.getRoom().price;
        String resevedDate = reservation.getDate();

        reservation.getCustomer().chargeCash(room_price);//환불
        reservation.getCustomer().removeReseved_id(uuid);//고객의 예약번호 리스트에서도 삭제
        reservations.remove(uuid);//전체 맵에서도 삭제(키값으로 삭제)

        Map<RoomType, Integer> remainRooms = remainRoomByDate.get(resevedDate);//잔여객실 수 증가

        switch (size) {
            case "Standard" -> remainRooms.put(RoomType.STANDARD, remainRooms.get(RoomType.STANDARD) + 1);
            case "Deluxe" -> remainRooms.put(RoomType.DELUXE, remainRooms.get(RoomType.DELUXE) + 1);
            case "Suite" -> remainRooms.put(RoomType.SUITE, remainRooms.get(RoomType.SUITE) + 1);
        }

    }

    //로그아웃
    private void logout() {
        System.out.println("로그아웃 하시겠습니까??");
        System.out.println("1. 네       2. 아니요");

        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    System.out.println("로그아웃 합니다");
                    currentLoginedCustomer = null;
                    loginMenu();
                case 2:
                    timeSleep("메인 메뉴");
                    mainMenu();
                default:
                    printBadInput("숫자");
                    logout();
            }
        } catch (InputMismatchException e) {
            printBadInput("숫자");
            sc = new Scanner(System.in);
            logout();
        }

    }

    private void timeSleep(String menu) {
        System.out.println("3초 후 " + menu + "로 이동합니다!");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printBadInput(String type) {
        System.out.println("잘못 입력하셨습니다 " + type + "를 다시 입력해주세요");
    }

}
