import java.util.*;
import java.util.regex.Pattern;

public class Hotel {
    //호텔은 모든 예약 목록을 조회 할 수 있다.
    //고객 목록 저장
    Scanner sc = new Scanner(System.in);
    Map<String, Integer[]> remainRoomByDate = new LinkedHashMap<>();//
    List<Customer> customers = new ArrayList<>();//고객 목록
    Map<UUID, Reservation> reservationMap = new HashMap<>();//전체 예약 목록

    Room[] roomlist = {
            new Room("Standard", 100000),
            new Room("Deluxe", 200000),
            new Room("Suits", 500000)
    };

    static final int ROOM_KIND = 3;

    final String managerPassword = "1234";
    static final String HOTEL_NAME = "P땀눈물";


    Customer current_logined_customer;

    void start() {
        DataSetting();
        LoginMenu();
    }


    void DataSetting() {
        customers.add(new Customer("이민주", "010-1111-1111", 250000));//임의로 고객 넣기

        UUID uuid = UUID.randomUUID();
        reservationMap.put(uuid, new Reservation(uuid, roomlist[1], "이민주", "010-1111-1111", "2023-10-25"));
        remainRoomByDate.put("2023-10-25", new Integer[]{0, 2, 3});
    }


    void LoginMenu() {
        System.out.println(HOTEL_NAME + " Hotel에 오신것을 환영합니다!");
        System.out.println("1.고객 정보 등록");
        System.out.println("2.로그인");
        System.out.println("3.종료");

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
            if (customer.name.equals(name) && customer.phone_number.equals(phone)) {
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
        //phone 하이픈 넣은번호로 변경해주는 코드 작성!

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


        //기존 비교해서 존재하면? 이미 등록된 회원입니다!
        int find = findCustomer(name, phone);
        if (find != -1) {
            System.out.println("이미 등록된 회원입니다!");
            LoginMenu();
        } else {
            System.out.println("소지금을 입력하세요:");
            int cash = sc.nextInt();

            customers.add(new Customer(name, phone, cash));
            System.out.println("회원 등록이 완료 되었습니다!");
            LoginMenu();
        }


    }

    void MainMenu() {
        System.out.println(HOTEL_NAME + " Hotel MENU 입니다!");
        System.out.println("[" + current_logined_customer.name + "님이 로그인 중입니다!    잔액 :" + current_logined_customer.cash + "]");

        System.out.println("1. 객실 예약");
        System.out.println("2. " + current_logined_customer.name + "님의 예약 목록 조회");//로그인 된 사람의 예약 목록
        System.out.println("3. 전체 예약 조회");
        System.out.println("4. 캐시 충전");
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
                    CheckTotalReservation();
                    break;
                case 4:
                    ChargeCash();
                    break;
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

    }

    private void CheckTotalReservation() {


        System.out.println("관리자 비밀번호를 입력하세요.");
        String insertPassword = sc.nextLine(); //직원이 입력하는 비밀번호
        // if문 활용해서 관리자 비밀번호 일치하는지 여부 확인
        if (insertPassword.equals(managerPassword)) {
            if (reservationMap.isEmpty()) {
                System.out.println("현재 예약된 객실이 존재하지 않습니다."); //예약 없을 경우
                MainMenu();
            } else {
                //호텔 전체 예약 목록 띄우기
                for (UUID uuid : reservationMap.keySet()) {
                    Reservation reservation = reservationMap.get(uuid);
                    System.out.print("객실 종류: " + reservation.room.size);
                    System.out.print("| 숙박 날짜: " + reservation.date);
                    System.out.print("| 예약 고객명: " + reservation.customer_name);
                    System.out.print("| 고객 전화번호: " + reservation.customer_phone);
                    System.out.println();

                }
            }
        } else {
            System.out.println("잘못된 접근입니다.");
            CheckTotalReservation();
        }

    }

    void ReservedRoom() {
        //날짜 입력 받기
        System.out.println("숙박 날짜를 입력해주세요: ");
        sc.nextLine();//입력버퍼 비워주는 역할!
        String date = sc.nextLine();


        if (!remainRoomByDate.containsKey(date)) {

            //날짜 없으면(예약된 게 없다)
            Integer[] arr = {4, 4, 4};// 각 크기 당 10개방 배치

            remainRoomByDate.put(date, arr);//
        }

        //객실정보 가져오기
        Integer[] select_date_rooms = remainRoomByDate.get(date);


        if (isSoldOut(select_date_rooms)) {
            System.out.println("해당 날짜에 남은 객실이 없습니다!!");
            System.out.println("1.날짜 입력      2. 메인 메뉴");
            SelectReturn();
            return;
        }


        ShowRemainRooms(date);
    }

    void SelectReturn() {
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
                    SelectReturn();
            }
        } catch (InputMismatchException e) {
            PrintBadInput("숫자");
            sc = new Scanner(System.in);
            SelectReturn();
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

    private void ShowRemainRooms(String date) {
        Integer[] select_date_rooms = remainRoomByDate.get(date);
        System.out.println(date + " 객실 현황 입니다");

        for (int i = 0; i < ROOM_KIND; i++) {
            System.out.println((i + 1) + "." + roomlist[i].size + "(" + select_date_rooms[i] + "개 남음)          가격:" + roomlist[i].price + "원");
        }
        System.out.println("0. 메인 메뉴");

        SelectRoomSize(date);
    }


    private void SelectRoomSize(String date) {
        Integer[] select_date_rooms = remainRoomByDate.get(date);
        try {
            int select = sc.nextInt();
            if (1 <= select && select <= ROOM_KIND) {
                int room_size = select - 1;// 실제 배열은 0-2까지라서 -1 해준다!

                if (select_date_rooms[room_size] == 0) {
                    System.out.println("해당 객실은 예약 마감되었습니다");
                    System.out.println("다른 객실을 선택해주세요!");
                    SelectRoomSize(date);
                } else {
                    System.out.println(date + "에 " + roomlist[select - 1].size + " Room을 예약 하시겠습니까??");
                    System.out.println("가격은 " + roomlist[select - 1].price + "원 입니다!");

                    System.out.println(current_logined_customer.name + "님 잔액 :  " + current_logined_customer.cash + "원");

                    System.out.println("결제 하시겠습니까??");
                    System.out.println("1. 결제    2.취소");
                    Confirmpayment(date, room_size);
                }
            } else if (select == 0) {
                TimeSleep("메인 메뉴");
                MainMenu();
            } else {
                PrintBadInput("숫자");
                SelectRoomSize(date);
            }

        } catch (InputMismatchException e) {
            sc = new Scanner(System.in);
            SelectRoomSize(date);
        }
    }

    //결제 확인
    private void Confirmpayment(String date, int room_size) {
        Integer[] select_date_rooms = remainRoomByDate.get(date);
        try {
            int select = sc.nextInt();
            switch (select) {
                //결제
                case 1:
                    if (current_logined_customer.cash >= roomlist[room_size].price) {
                        current_logined_customer.cash -= roomlist[room_size].price;//캐쉬 차감

                        UUID reserved_id = UUID.randomUUID(); //UUid 생성
                        Reservation reservation = new Reservation(reserved_id, roomlist[room_size], current_logined_customer.name, current_logined_customer.phone_number, date);
                        reservationMap.put(reserved_id, reservation);//예약 Map에 추가
                        current_logined_customer.addReservedId(reserved_id);//로그인된 손님에게 예약번호 저장

                        select_date_rooms[room_size] -= 1;//해당 크기 방 개수 1 감소
                        remainRoomByDate.put(date, select_date_rooms);
                        System.out.println(date + " 체크인 오후 3시 체크아웃 오전 11시 입니다.");
                        System.out.println("예약 완료!!!");

                        TimeSleep("메인 메뉴");
                        MainMenu();
                    } else {
                        System.out.println("잔액이 부족합니다!");
                        TimeSleep("객실 선택");
                        ShowRemainRooms(date);
                    }
                    break;
                //취소
                case 2:
                    TimeSleep("객실 선택");
                    ShowRemainRooms(date);
                default:
                    PrintBadInput("숫자");
                    Confirmpayment(date, room_size);
            }
        } catch (InputMismatchException e) {
            PrintBadInput("숫자");
            sc = new Scanner(System.in);
            Confirmpayment(date, room_size);
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