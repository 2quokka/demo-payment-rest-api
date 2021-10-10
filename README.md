# 결제시스템
결제요청을 받아 카드사와 통신하는 결제 API 시스템입니다.
카드결제, 전체취소, 데이터조회, 부분취소 기능이 있으며 멀티스레드 환경을 고려하였습니다.

## 개발환경
JAVA, SpringBoot, JPA, Junit4, Maven

## 테이블 설계

### PAYMENT_INFO (결제정보 테이블)
| COLUMN_NAME      | TYPE        | COMMENTS                                      |
|------------------|-------------|-----------------------------------------------|
| PAYMENT_ID       | VARCHAR(20)(PK) | 결제식별번호(unique id, 20자리)  |
| ENC_CARD_INFO    | VARCHAR(300)| 암호화된 카드정보                    |
| INSTALLMENTS     | VARCHAR(2)  | 할부개월수: 0(일시불), 1 ~ 12                 |
| AMOUNT           | NUMBER      | 결제금액(100원 이상, 10억원 이하, 숫자)       |
| VAT              | NUMBER      | 부가가치세                                    |
| FINAL_AMOUNT     | NUMBER      | 최종결제금액                                  |
| FINAL_VAT        | NUMBER      | 최종부가가치세                                |
| STATUS           | VARCHAR(30)  | 상태                                 |
| ORGIN_PAYMENT_ID| VARCHAR(20)  | 원 결제식별번호                                 |
| APPROVAL_TIME    | DATE        | 결제승인일시                                  |
| CANCEL_TIME    | DATE        | 결제취소일시                                  |
| DATA_CREATE_TIME | DATE        | 데이터생성일시                                |
| DATA_MODIFY_TIME | DATE        | 데이터변경일시                                |  

### CARD_COMPANY_DATA (카드사 전달 데이터 테이블)
| COLUMN_NAME      | TYPE         | COMMENTS       |
|------------------|--------------|----------------|
| SEQ_NUM          | NUMBER(PK)      | 시퀀스         |
| DATA             | VARCHAR(450) | 카드사데이터   |
| DATA_CREATE_TIME | DATE         | 데이터생성일시 |
| DATA_MODIFY_TIME | DATE         | 데이터변경일시 |

## API Interface

  ### 1. 결제
    Method : POST
    URI : /api/payment
    Content-Type : application/json;charset=UTF-8
  
1)Request
  
  | 항목명       | 항목설명   | 필수 | JSON 타입                    |
|--------------|------------|------|-------------------------------|
| cardNum      | 카드번호   |   Y  | 숫자              |
| expiryDate   | 유효기간   |   Y  | 숫자              |
| cvcNum       | CVC        |   Y  | 숫자                    |
| installments | 할부개월수 |   Y  | 숫자        |
| amount       | 결제금액   |   Y  | 숫자 |
| vat          | 부가가치세 |   N  | 숫자 |
  
2)Response
| 항목명    | 항목설명 | 필수 | JSON 타입     |
|-----------|----------|------|----------------|
| paymentId | 관리번호 |   Y  | 문자열  |
| data      | 데이터   |   Y  | 문자열 |

  ### 2. 결제취소
    Method : POST
    URI : /cancel-payment
    Content-Type : application/json;charset=UTF-8
    
1)Request

  | 항목명       | 항목설명     | 필수 | JSON 타입                   |
|--------------|--------------|------|-------------------------------|
| paymentId    | 관리번호     |   Y  | 문자열                 |
| cancelAmount | 결제취소금액 |   Y  | 숫자 |
| vat          | 부가가치세   |   N  | 숫자 |

2)Response
| 항목명    | 항목설명 | 필수 | JSON 타입     |
|-----------|----------|------|----------------|
| paymentId | 관리번호 |   Y  | 문자열  |
| data      | 데이터   |   Y  | 문자열 |

  ### 3. 데이터 조회
    Method : GET
    URI : /payment-info
    Content-Type : application/json;charset=UTF-8
    
1)Request

  | 항목명       | 항목설명     | 필수 | JSON 타입                    |
|--------------|--------------|------|-------------------------------|
| paymentId    | 관리번호     |   Y  |   문자열                 |

2)Response

  | 항목명       | 항목설명   | 필수 | JSON 타입                    |
|--------------|------------|------|-------------------------------|
| paymentId    | 관리번호     |   Y  | 문자열                 |
| cardInfo      | 카드정보   |   Y  |               |
| - cardNum      | 카드번호   |   Y  | 문자열             |
| - expiryDate   | 유효기간   |   Y  | 문자열              |
| - cvc       | CVC        |   Y  | 문자열                    |
| state        | 결제취소구분  |   Y  | 문자열                    |
| amount       | 금액   |   Y  | 숫자 |
| vat          | 부가가치세 |   Y  | 숫자 |
| finalAmount    | 최종금액 |   N  | 숫자 |
| finalVat          | 최종부가가치세 |   N  | 숫자 |
  
  ### 4. 부분취소
    2. 결제취소에 기능 추가 API Interface 상동

  ### 5. 응답에러코드
    요청 파라미터  @Valid 활용
    Exception 발생에 대한 에러코드 정의

| STATUS           | ERROR      | MESSAGE                                           |
|------------------|------------|---------------------------------------------------|
| 404(Not Found)   | error-0000 | "해당 결제 건이 존재하지 않습니다."               |
| 400(Bad Request) | error-0001 | "취소 건 입니다."                                 |
| 400(Bad Request) | error-0002 | "이미 취소된 건 입니다."                          |
| 400(Bad Request) | error-0003 | "취소금액이 결제금액을 초과합니다."               |
| 400(Bad Request) | error-0004 | "취소 부가가치세가 결제 부가가치세를 초과합니다." |
| 400(Bad Request) | error-0005 | "남은 부가가치세가 남은 금액보다 큽니다."         |

## 문제해결 및 전략
### 분석 & 설계
 기존에는 결제건과 취소건이 일대다 관계를 갖기에 취소테이블에 관리번호를 외래키로 각각의 테이블을 설계하였으나, 취소결제건도 관리번호를 생성하기 때문에 결제/취소를 하나의 결제정보 테이블에 저장하도록 구성하였습니다.  
결제/취소는 상태필드로 구분하고 부분취소를 고려하여 최종금액, 최종부가가치세 필드를 추가하였습니다.

### 1. 결제요청
    1) 부가가치세 계산
    2) 암호화
    3) 결제정보 저장
    4) 카드사 전송 (테이블 데이터 저장)
    5) 응답
    
  #### * 관리번호(PaymentId) 채번
    PaymentInfo의 PaymentId는 20자리 문자열이기때문에 
    기본적으로 GenratedValue에서 생성해주는 숫자는 사용할 수 없어 Custom Generator(IdGenerator)를 구현하였습니다. 
    ex)paymentId : "T_000000000000000004"
    
  #### * 암호화
    암호화는 SEED 블록암호화 알고리즘을 활용하였습니다. 
    카드정보(카드번호, 유효기간, CVC)는 암호화됩니다.
    - 관련소스 : KISA_SEED_CBC
               SeedCrypto
    
  #### * 테스트
    [ PaymentControllerTest ]
    1) 정상처리
    2) 입력필드가 안 들어온 경우 
    3) 입력 값이 잘못 들어온 경우
    
### 2. 결제취소 (부분취소)
    1) 대상 결제정보 조회
    2) 부가가치세, 최종결제금액, 최종부가가치세 계산
    3) 원 결제정보 최종결제금액, 최종부가가치세 변경
    4) 취소 결제정보 저장
    5) 카드사 전송 (테이블 데이터 저장)
    6) 응답
    
  #### * 테스트
    [ PaymentControllerTest ]
    1) 정상처리
    2) 입력필드가 안 들어온 경우 
    3) 입력 값이 잘못 들어온 경우
    
  #### * 복호화
    카드정보(카드번호, 유효기간, CVC)는 암호화된 정보를 복호화하여 CardInfo로 객체화 합니다.
    - 관련소스 : KISA_SEED_CBC
               SeedCrypto
               CardInfo
               
### 3. 데이터 조회
    1) 대상 결제정보 조회
    2) 복호화
    3) 마스킹
    4) 응답
    
  #### * 테스트
    [ PaymentControllerTest ]
    1) 정상처리
    2) 입력필드가 안 들어온 경우 
    3) 입력 값이 잘못 들어온 경우
  
### 4. 이외
  #### * 카드사 전달용 문자열 생성
  
  #### * 멀티스레드 환경 대비
   멀티스레드 환경에 대비하기 위해서는 트랜잭션의 동시성을 제어해야합니다.  
   동시성문제는 두개 이상의 스레드가 하나의 데이터에 읽고 쓰는 경우에 발생 할 수 있습니다.  
   JPA에서는 Entity 단위로 Isolation Level을 설정하여 동시성을 제어 할 수 있습니다.  
   본 API에서는 멀티스레드에 대비하기 위해 아래와 같은 작업을 진행하였습니다. 
   
    1) Spring에서 지원하는 @Transaction 기능으로 Service의 각 기능 메소드를 트랜잭션으로 설정
    2) PaymentInfoRepository에 @Lock(LockModeType.PESSIMISTIC_WRITE) 설정으로  다른 세션에서 읽거나 쓰지 못하게 함
    
   <테스트>  MultiThreadTest  
   A. 하나의 카드번호로 동시에 결제 할 경우 테스트  
   B. 결제 한 건에 대한 전체취소를 동시에 할 경우 테스트  
        MultiThreadTest.cancelPayment_multiThread_Check  
   C. 결제 한 건에 대한 부분취소를 동시에 할 경우 테스트  
        MultiThreadTest.cancelPayment_multiThread_Check
    
## 빌드 및 실행방법
MAVEN BUILD 후 생성된 jar 파일 실행
java -jar demo-payment-rest-api-0.0.1-SNAPSHOT.jar
