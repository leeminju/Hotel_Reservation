import java.util.*;

public class Hotel {
    //호텔은 모든 예약 목록을 조회 할 수 있다.
    //고객 목록 저장
    Scanner sc = new Scanner(System.in);
    Map<String, Integer[]> remainRoomByDate = new LinkedHashMap<>();//
    List<Customer> customers = new ArrayList<>();//고객 목록
    Map<UUID, Reservation> reservationMap = new HashMap<>();//전체 예약 목록

    final int ROOM_KIND = 3;
    final int managerPassword = 1234; //호텔 전체 예약 목록 보려면 입력해야 하는 비밀번호

    Room[] roomlist = {
            new Room("Standard", 100000),
            new Room("Deluxe", 200000),
            new Room("Suits", 500000)
    };
    Customer current_logined_customer;

    void start() {
        LoginMenu();
    }

    void LoginMenu() {
        System.out.println("피땀 눈물 Hotel에 오신것을 환영합니다!");
        System.out.println("1.고객 정보 등록");
        System.out.println("2.로그인");

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


    void Login() {
        System.out.println("이름을 입력하세요:");
        sc.nextLine();
        String name = sc.nextLine();

        System.out.println("전화번호를 입력하세요(11자):");
        String phone = sc.nextLine();

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

    int findCustomer(String name, String phone) {
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (customer.name.equals(name) && customer.phone_number.equals(phone)) {
                return i;
            }
        }
        return -1;
    }

    void Signin() {

        System.out.println("이름을 입력하세요:");
        sc.nextLine();//이걸 넣어야 오류 안생김! 왜지??
        String name = sc.nextLine();

        System.out.println("전화번호를 입력하세요(11자):");//하이픈 넣는 함수 구현 필요!
        String phone_number = sc.nextLine();


        //기존 비교해서 존재하면? 이미 등록된 회원입니다!
        int find = findCustomer(name, phone_number);
        if (find != -1) {
            System.out.println("이미 등록된 회원입니다!");
            LoginMenu();
        } else {
            System.out.println("소지금을 입력하세요:");
            int cash = sc.nextInt();

            customers.add(new Customer(name, phone_number, cash));
            System.out.println("회원 등록이 완료 되었습니다!");
            LoginMenu();
        }


    }

    void MainMenu() {
        System.out.println("피땀 눈물 Hotel에 오신것을 환영합니다!");
        System.out.println(current_logined_customer.name + "님이 로그인 중입니다!    잔액 :" + current_logined_customer.cash);

        System.out.println("1. 객실 예약");
        System.out.println("2. " + current_logined_customer.name + "님의 예약 목록 조회");//로그인 된 사람의 예약 목록
        System.out.println("3. 전체 예약 조회");
        System.out.println("4. 로그아웃");//원래 메뉴로 돌아가기!


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
                    Logout();
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

    private void CheckMyReservation() {
    }

    private void CheckTotalReservation() {
        System.out.println("관리자 비밀번호를 입력하세요.");
        int insertPassword = sc.nextInt(); //직원이 입력하는 비밀번호
        // if문 활용해서 관리자 비밀번호 일치하는지 여부 확인
        if (insertPassword == managerPassword) {
            if (reservationMap.isEmpty()) {
                System.out.println("현재 예약된 객실이 존재하지 않습니다."); //예약 없을 경우
                MainMenu();
            } else {
                //호텔 전체 예약 목록 띄우기
                for (UUID uuid : reservationMap.keySet()) {
                    Reservation reservation = reservationMap.get(uuid);
                    System.out.print("객실 종류: " + reservation.room);
                    System.out.print("| 숙박 날짜: " + reservation.date);
                    System.out.print("| 예약 고객명: " + reservation.customer_name);
                    System.out.print("| 고객 전화번호: " + reservation.customer_phone);
                    System.out.println();
                }
            }
        } else {
            System.out.println("잘못된 접근입니다.");
            MainMenu();
        }
    }

    void ReservedRoom() {
        //날짜 입력 받기
        System.out.println("숙박 날짜를 입력해주세요: ");
        sc.nextLine();//이걸 넣어야 오류 안생김! 왜지??
        String date = sc.nextLine();


        if (!remainRoomByDate.containsKey(date)) {
            //날짜 없으면(예약된 게 없다)
            Integer[] arr = {4, 4, 4};// 각 크기 당 10개방 배치
            remainRoomByDate.put(date, arr);//
        }

        //객실정보 가져오기
        Integer[] select_date_rooms = remainRoomByDate.get(date);

        boolean soldout = true;//모든 객실이 완판인지 확인
        for (int i = 0; i < ROOM_KIND; i++) {
            if (select_date_rooms[i] > 0) {
                soldout = false;
                break;
            }
        }

        if (soldout) {
            System.out.println("해당 날짜에 남은 객실이 없습니다!!");
            System.out.println("1.날짜 선택      2. 메인 메뉴");
        }


        ShowRemainRooms(date);
    }

    private void ShowRemainRooms(String date) {
        Integer[] select_date_rooms = remainRoomByDate.get(date);
        System.out.println(date + " 객실 현황 입니다");

        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + "." + roomlist[i].size + "(" + select_date_rooms[i] + "개 남음)          가격:" + roomlist[i].price + "원");
        }
        System.out.println("0. 메인 메뉴");

        SelectRoomSize(date);
    }


    private void SelectRoomSize(String date) {
        Integer[] select_date_rooms = remainRoomByDate.get(date);
        try {
            int select = sc.nextInt();
            if (1 <= select && select <= 3) {
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
                System.out.println("3초 후 메인으로 이동합니다!");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                MainMenu();
            } else {
                System.out.println("잘못 입력 하셨습니다! 다시 입력해주세요!");
                SelectRoomSize(date);
            }

        } catch (InputMismatchException e) {
            sc = new Scanner(System.in);
            SelectRoomSize(date);
        }
    }

    private void Confirmpayment(String date, int room_size) {
        Integer[] select_date_rooms = remainRoomByDate.get(date);
        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    if (current_logined_customer.cash >= roomlist[room_size].price) {
                        current_logined_customer.cash -= roomlist[room_size].price;
                        UUID reserved_id = UUID.randomUUID(); //UUid 생성
                        Reservation reservation = new Reservation(reserved_id, roomlist[room_size], current_logined_customer.name, current_logined_customer.phone_number, date);
                        reservationMap.put(reserved_id, reservation);//예약 Map에 추가
                        current_logined_customer.addReservedId(reserved_id);//로그인된 손님에게 예약번호 저장

                        select_date_rooms[room_size] -= 1;//방
                        remainRoomByDate.put(date, select_date_rooms);
                        System.out.println(date + " 체크인 오후 3시 체크아웃 오전 11시 입니다.");
                        System.out.println("예약 완료!!!");

                        System.out.println("3초 후 메인 메뉴으로 이동합니다");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        MainMenu();
                    } else {
                        System.out.println("잔액이 부족합니다!");
                        System.out.println("객실 선택으로 이동합니다");
                        ShowRemainRooms(date);
                    }
                    break;
                case 2:
                    System.out.println("객실 선택으로 이동합니다");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ShowRemainRooms(date);
                default:
                    System.out.println("잘못 입력하셨습니다 다시 입력해주세요");
                    Confirmpayment(date, room_size);
            }
        } catch (InputMismatchException e) {
            System.out.println("잘못 입력하셨습니다 다시 입력해주세요");
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
                    System.out.println("메인 메뉴로 돌아갑니다");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MainMenu();
                default:
                    System.out.println("잘못 입력하셨습니다 다시 입력해주세요");
                    Logout();
            }
        } catch (InputMismatchException e) {
            System.out.println("잘못 입력하셨습니다 다시 입력해주세요");
            sc = new Scanner(System.in);
            Logout();
        }

    }

}
