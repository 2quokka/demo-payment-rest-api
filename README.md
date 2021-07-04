# 결제시스템
결제요청을 받아 카드사와 통신하는 API 인터페이스를 제공하는 결제시스템.

* 요청 플로우
고객 -> 결제시스템 -> 카드사

## 개발환경

## 테이블 설계

### PAYMENT_INFO (결제정보 테이블)
| COLUMN_NAME      | TYPE        | COMMENTS                                      |
|------------------|-------------|-----------------------------------------------|
| PAYMENT_ID       | VARCHAR(20)(PK) | 결제식별번호(unique id, 20자리) pk 채번필요함 |
| CARD_NUM         | VARCHAR(16) | 카드번호(10 ~ 16자리 숫자)                    |
| EXPIRY_DATE      | VARCHAR(4)  | 유효기간(4자리 숫자, mmyy)                    |
| CVC_NUM          | VARCHAR(3)  | cvc(3자리 숫자)                               |
| INSTALLMENTS     | NUMBER      | 할부개월수: 0(일시불), 1 ~ 12                 |
| AMOUNT           | NUMBER      | 결제금액(100원 이상, 10억원 이하, 숫자)       |
| VAT              | NUMBER      | 부가가치세                                    |
| FINAL_AMOUNT     | NUMBER      | 최종결제금액                                  |
| FINAL_VAT        | NUMBER      | 최종부가가치세                                |
| STATUS           | VARCHAR(2)  | 상태코드 (00:승인, 01:전체취소 02:부분취소)   |
| APPROVAL_TIME    | DATE        | 결제승인일시                                  |
| DATA_CREATE_TIME | DATE        | 데이터생성일시                                |
| DATA_MODIFY_TIME | DATE        | 데이터변경일시                                |  


### PAYMENT_CANCEL (결제취소 테이블)
| COLUMN_NAME      | TYPE        | COMMENTS                               |
|------------------|-------------|----------------------------------------|
| PAYMENT_ID       | VARCHAR(20)(PK) | 결제식별번호(20자리)              |
| SEQ_NUM          | NUMBER(PK)      | 시퀀스번호(10자리) 관리번호별 생성 |
| CANCEL_AMOUNT    | NUMBER      | 결제취소금액                           |
| CANCEL_VAT       | NUMBER      | 결제취소부가가치세                     |
| CANCEL_TIME      | DATE        | 결제취소일시                           |
| DATA_CREATE_TIME | DATE        | 데이터생성일시                         |
| DATA_MODIFY_TIME | DATE        | 데이터변경일시                         |

### FORWARD_DATA (카드사 전달 데이터 테이블)
| COLUMN_NAME      | TYPE         | COMMENTS       |
|------------------|--------------|----------------|
| SEQ_NUM          | NUMBER(PK)      | 시퀀스         |
| DATA             | VARCHAR(450) | 카드사데이터   |
| DATA_CREATE_TIME | DATE         | 데이터생성일시 |
| DATA_MODIFY_TIME | DATE         | 데이터변경일시 |

## API Interface *json type추가

  ### 1. 결제
    Method : POST
    URI : /api/payment
    Content-Type : application/json;charset=UTF-8
  
1)Request
  
  | 항목명       | 항목설명   | 필수 | 타입(길이)                    |
|--------------|------------|------|-------------------------------|
| cardNum      | 카드번호   |   Y  | 10 ~ 16자리 숫자              |
| expiryDate   | 유효기간   |   Y  | 4자리 숫자, mmyy              |
| cvcNum       | CVC        |   Y  | 3자리 숫자                    |
| installments | 할부개월수 |   Y  | 0(일시불), 1 ~ 12 숫자        |
| amount       | 결제금액   |   Y  | 100원 이상, 10억원 이하, 숫자 |
| vat          | 부가가치세 |   N  | 100원 이상, 10억원 이하, 숫자 |
  
2)Response
| 항목명    | 항목설명 | 필수 | 타입(길이)     |
|-----------|----------|------|----------------|
| paymentId | 관리번호 |   Y  | 20자리 문자열  |
| data      | 데이터   |   Y  | 450자리 문자열 |

  ### 2. 결제취소
    Method : POST
    URI : /api/cancelPay
    Content-Type : application/json;charset=UTF-8
    
1)Request

  | 항목명       | 항목설명     | 필수 | 타입(길이)                    |
|--------------|--------------|------|-------------------------------|
| paymentId    | 관리번호     |   Y  | 20자리 문자열                 |
| cancelAmount | 결제취소금액 |   Y  | 100원 이상, 10억원 이하, 숫자 |
| vat          | 부가가치세   |   N  | 100원 이상, 10억원 이하, 숫자 |

2)Response
| 항목명    | 항목설명 | 필수 | 타입(길이)     |
|-----------|----------|------|----------------|
| paymentId | 관리번호 |   Y  | 20자리 문자열  |
| data      | 데이터   |   Y  | 450자리 문자열 |


  ### 3. 데이터 조회
    Method : GET
    URI : /api/getPayInfo
    Content-Type : application/json;charset=UTF-8
    
1)Request

  | 항목명       | 항목설명     | 필수 | 타입(길이)                    |
|--------------|--------------|------|-------------------------------|
| paymentId    | 관리번호     |   Y  | 20자리 문자열                 |

2)Response

  | 항목명       | 항목설명   | 필수 | 타입(길이)                    |
|--------------|------------|------|-------------------------------|
| paymentId    | 관리번호     |   Y  | 20자리 문자열                 |
| cardNum      | 카드번호   |   Y  | 10 ~ 16자리 숫자              |
| expiryDate   | 유효기간   |   Y  | 4자리 숫자, mmyy              |
| cvcNum       | CVC        |   Y  | 3자리 숫자                    |
| type        | 결제취소구분  |   Y  | 문자열                    |
| amount       | 금액   |   Y  | 100원 이상, 10억원 이하, 숫자 |
| vat          | 부가가치세 |   Y  | 100원 이상, 10억원 이하, 숫자 |
  
  ### 4. 부분취소
    Method : POST
    URI : /api/partCancelPay
    Content-Type : application/json;charset=UTF-8
    
1)Request

  | 항목명       | 항목설명     | 필수 | 타입(길이)                    |
|--------------|--------------|------|-------------------------------|
| paymentId    | 관리번호     |   Y  | 20자리 문자열                 |
| cancelAmount | 결제취소금액 |   Y  | 100원 이상, 10억원 이하, 숫자 |
| vat          | 부가가치세   |   N  | 100원 이상, 10억원 이하, 숫자 |

2)Response
| 항목명    | 항목설명 | 필수 | 타입(길이)     |
|-----------|----------|------|----------------|
| paymentId | 관리번호 |   Y  | 20자리 문자열  |
| data      | 데이터   |   Y  | 450자리 문자열 |

  ### 5. 응답에러코드
## 문제해결 및 전략

### 1. 결제요청
  #### * 관리번호 채번
  #### * 테스트
### 2. 결제취소
  #### * 테스트
### 3. 데이터 조회
  #### * 테스트
### 4. 부분취소
  #### * 테스트
### 5. 이외
  #### * 카드사 전달용 문자열 생성
  #### * 

Request(json) -> TB 각 필드타입 변경을 위해 Mapper Class Converter 사용

## 빌드 및 실행방법
