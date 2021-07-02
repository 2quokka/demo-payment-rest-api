# 결제시스템
결제요청을 받아 카드사와 통신하는 API 인터페이스를 제공하는 결제시스템.

* 요청 플로우
고객 -> 결제시스템 -> 카드사

## 개발환경

## 테이블 설계

### PAYMENT_INFO (결제정보 테이블)
| COLUMN_NAME      | TYPE        | COMMENTS                                      |
|------------------|-------------|-----------------------------------------------|
| PAYMENT_ID       | VARCHAR(20) | 결제식별번호(unique id, 20자리) pk 채번필요함 |
| CARD_NUM         | VARCHAR(16) | 카드번호(10 ~ 16자리 숫자)                    |
| EXPIRY_DATE      | VARCHAR(4)  | 유효기간(4자리 숫자, mmyy)                    |
| CVC_NUM          | VARCHAR(3)  | cvc(3자리 숫자)                               |
| INSTALLMENTS     | NUMBER      | 할부개월수: 0(일시불), 1 ~ 12                 |
| AMOUNT           | NUMBER      | 결제금액(100원 이상, 10억원 이하, 숫자)       |
| VAT              | NUMBER      | 부가가치세                                    |
| FINAL_AMOUNT     | NUMBER      | 최종결제금액                                  |
| FINAL_VAT        | NUMBER      | 최종부가가치세                                |
| STATUS           | VARCHAR(2)  | 상태코드 (00:승인, 01:전체취소 02:부분취소)   |
| APPROVAL_DATE    | DATE        | 결제승인일시                                  |
| DATA_CREATE_TIME | DATE        | 데이터생성일시                                |
| DATA_MODIFY_TIME | DATE        | 데이터변경일시                                |  


### PAYMENT_CANCEL (결제취소 테이블)
| COLUMN_NAME      | TYPE        | COMMENTS                               |
|------------------|-------------|----------------------------------------|
| PAYMENT_ID       | VARCHAR(20) | 결제식별번호(pk1, 20자리)              |
| SEQ_NUM          | NUMBER      | 시퀀스번호(pk2 10자리) 관리번호별 생성 |
| CANCEL_AMOUNT    | NUMBER      | 결제취소금액                           |
| CANCEL_VAT       | NUMBER      | 결제취소부가가치세                     |
| CANCEL_DATE      | DATE        | 결제취소일시                           |
| DATA_CREATE_TIME | DATE        | 데이터생성일시                         |
| DATA_MODIFY_TIME | DATE        | 데이터변경일시                         |

### FORWARD_DATA (카드사 전달 데이터 테이블)
| COLUMN_NAME      | TYPE         | COMMENTS       |
|------------------|--------------|----------------|
| SEQ_NUM          | NUMBER       | 시퀀스         |
| DATA             | VARCHAR(450) | 카드사데이터   |
| DATA_CREATE_TIME | DATE         | 데이터생성일시 |
| DATA_MODIFY_TIME | DATE         | 데이터변경일시 |

## 문제 해결 전략

  ### 1. 결제 API
  #### * API interface
  ### 2. 결제취소 API
  #### * API interface
  ### 3. 데이터 조회 API
  #### * API interface
  ### 4. API 요청 실패시 응답
  #### * API interface
  ### 5. (선택 문제) 부분취소 API
  
## 빌드 및 실행방법
