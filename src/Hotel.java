import java.util.*;

public class Hotel {
    //호텔은 모든 예약 목록을 조회 할 수 있다.
    //고객 목록 저장
    Scanner sc = new Scanner(System.in);
    Map<String, Integer[]> unreservedRooms = new LinkedHashMap<>();//
    List<Customer> customers = new ArrayList<>();//고객

    Customer current_logined_custormer;

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

        int find = -1;
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            if (customer.name.equals(name) && customer.phone_number.equals(phone)) {
                find = i;
                break;
            }
        }

        if (find != -1) {
            System.out.println("로그인 완료!");
            current_logined_custormer = customers.get(find);
            MainMenu();
        } else {
            System.out.println("회원정보가 없습니다");
            Login();
        }
    }

    void Signin() {

        System.out.println("이름을 입력하세요:");
        sc.nextLine();//이걸 넣어야 오류 안생김! 왜지??
        String name = sc.nextLine();

        System.out.println("전화번호를 입력하세요(11자):");//하이픈 넣는 함수 구현 필요!
        String phone_number = sc.nextLine();

        System.out.println("소지금을 입력하세요:");
        int cash = sc.nextInt();

        //기존 비교해서 존재하면? 이미 등록된 회원입니다!

        customers.add(new Customer(name, phone_number, cash));
        System.out.println("회원 등록이 완료 되었습니다!");
        LoginMenu();
    }

    void MainMenu() {
        System.out.println("MJ의 branch");
        System.out.println("피땀 눈물 Hotel에 오신것을 환영합니다!");
        System.out.println(current_logined_custormer.name + "님이 로그인 중입니다!    잔액 :" + current_logined_custormer.cash);

        System.out.println("1.객실 예약");
        System.out.println("2.예약 목록 조회");
        System.out.println("3.로그아웃");//원래 메뉴로 돌아가기!

        SelectMainMenu();
    }

    private void SelectMainMenu() {
        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    ReservateRoom();
                    break;
                case 2:
                    CheckReservation();
                    break;
                case 3:
                    Logout();
                    break;
                default:
                    System.out.println("잘못 입력하셨습니다 다시 입력해주세요");
                    SelectLoginMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("잘못 입력하셨습니다 다시 입력해주세요");
            sc = new Scanner(System.in);
            SelectLoginMenu();
        }
    }


    void ReservateRoom() {


        //날짜 입력 년 월 일 -> 2023-10-24

        String date ="2023-10-24";

        if (!unreservedRooms.containsKey(date)) {
            //날짜 없으면
            Integer[] arr = {10, 10, 10};
            unreservedRooms.put(date, arr);
        }

        //객실정보 가져오기
        Integer[] rooms = unreservedRooms.get(date);
        System.out.println("1.Standard " + rooms[0]);
        System.out.println("2.Deluxe " + rooms[1]);
        System.out.println("3.Suits " + rooms[2]);


    }

    private void CheckReservation() {
    }

    private void Logout() {
        System.out.println("로그아웃 하시겠습니까??");
        System.out.println("1. 네       2. 아니요");
        try {
            int select = sc.nextInt();
            switch (select) {
                case 1:
                    System.out.println("로그아웃 합니다");
                    current_logined_custormer = null;
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
