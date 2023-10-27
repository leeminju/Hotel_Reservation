# Hotel_Reservation
<시작메뉴>
현재 시간 표시
1.고객 정보 등록
- 이름 입력 받기(동명이인 허용 하기 때문에 예외처리 안함)
- 전화번호 입력 받기(전화번호 형식(01X-XXXX-XXXX)아니면 재입력/동일한 번호 존재하면 재입력)
- 소지금 입력(숫자 아니면 재입력)

- 고객 리스트에 추가

2.로그인
- 이름 입력 받고
- 전화번호 입력 받고(전화번호 형식(01X-XXXX-XXXX)아니면 재입력)
- 고객정보에서 같은 이름과 전화번호 고객 찾아서 현재 로그인된 고객으로 설정
- 메인메뉴 진입

3.전체 예약조회
- 관리자 비밀 번호 입력(일치하지 않으면 모두 잘못된 접근이라고 뜸)
- 전체 예약목록(**숙박 시간순 정렬**)
- 취소)할지 말지 1.네 2.아니오->시작 메뉴
- 네 했으면 예약번호 입력 받고 취소
예약번호가 UUID 형식에 맞지 않으면 형식오류 재입력
형식은 맞지만 없는 예약번호면 없는 예약번호라고 하고 재입력
**(관리자는 어떤 예약도 취소 할 수 있다)**

- 예약번호 입력 후 취소하겠습니까?(네/아니요)

- 네-예약취소 환불->시작 메뉴

(해당날짜&크기->남아있는방수 +1, 고객에게 환불, 고객 & 전체 예약내역에서 삭제)
-> 객실 예약 시 방 개수복원된 것 확인, 고객 소지금 환불확인
고객 & 전체 예약내역에서 삭제된 것 확인

- 아니요 -> 시작메뉴


4.종료->System.exit(0);

<메인 메뉴>
현재 시간 표시
1.객실 예약

- 날짜입력(숫자 8자리(아니면 형식에 맞지않다고 출력후 재입력), 지난날짜&한달뒤은 예약불가!,당일은 3시(체크인)전 까지만 예약가능)
 모든 방이 품절 시 해당날짜 예약불가->날짜선택과 메인메뉴중 선택

- 객실 내역 조회(크기 가격 남은객실)
 (**주말과 주중 가격 다르게 표시**
- 번호로 객실 선택
남은 객실 0개 인 객실 선택시 해당 객실 예약 불가

- 결제 여부 화인(결제/취소)
취소 시 객실 선택으로 이동

- 결제
**주중/주말 가격인지 체크**
현재 로그인된 고객이 방가격 결제할수 있는지 소지금과 비교 

- 가능하면)
해당날짜&크기->남아있는방수 -1, 고객돈 - 객실 가격(캐쉬 차감), 전체예약내역과 본인예약내역에 추가
UUID(예약번호),체크인시간,체크아웃(다음날 11시) 시간 출력후 메인메뉴로 진입

- 불가능하면)잔액 부족->객실 선택


2.고객의 예약 목록 조회(**예약 순서순 정렬**)
- 현재 로그인된 고객의 예약목록 출력

- 취소)할지 말지 1.네 2.아니오
네 했으면 예약번호 입력 받고 취소
예약번호가 UUID 형식에 맞지 않으면 형식오류 재입력
형식은 맞지만 없는 예약번호면 없는 예약번호라고 하고 재입력
**현재 로그인된 고객의 예약번호가 아니면 본인의 예약정보가 아니라고 하고 재입력**

- 본인 예약번호 입력 후 취소하겠습니까?(네/아니요)

- 네-예약취소 환불->메인 메뉴
(해당날짜&크기->남아있는방수 +1, 고객에게 환불, 고객 & 전체 예약내역에서 삭제)
-> 객실 예약 시 방 개수복원된 것 확인, 고객 소지금 환불확인
고객 & 전체 예약내역에서 삭제된 것 확인
- 아니요 -> 메인메뉴

3.캐시 충전
- 추가금액 입력 받기
현재 로그인된 고객의 price += 추가 금액

4.회원정보 변경
- 선택 메뉴출력
1.이름-> 현재와 동일한 이름X, 기존고객 이름 가능(동명이인허용) 
새로운 이름입력하고 변경된 후->선택메뉴로 돌아감
2.전화번호->현재와 동일한 번호X, 형식맞는 번호, 기존고객의 번호x
새로운 번호 입력하고 변경된 후->선택 메뉴
0.메인 메뉴

(메인,전체 예약내역,고객의 예약내역 에서의 고객정보가 변경되었음)

5.로그아웃
- 현재 로그인된 고객 Null로 설정 후 시작메뉴로 진입
