package com.payment.api.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CancelPaymentId implements Serializable {

    private String paymentId;
    private Long seqNum;

    @Override
    public boolean equals(Object obj){
        if(this == obj) {
            return true;
        }

        if(obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        CancelPaymentId cancelPaymentId = (CancelPaymentId)obj;

        if(this.paymentId.equals(cancelPaymentId.paymentId) && this.seqNum.equals(cancelPaymentId.seqNum) ) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(paymentId, seqNum);
    }
}
