package com.payment.api;

import com.payment.api.common.GenDataForForwarding;
import com.payment.api.payment.dto.PaymentDataMapper;
import com.payment.api.payment.dto.PaymentInfoDTO;
import com.payment.api.payment.dto.PaymentInfoMapper;
import com.payment.api.payment.entity.PaymentInfo;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.ui.Model;

@EnableJpaAuditing
@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        if (modelMapper.getTypeMap(PaymentInfoDTO.class, PaymentInfo.class) == null) {
            modelMapper.addMappings(new PaymentInfoMapper());
        }

        //결제정보->카드사전달데이터 중복 맵핑 추가 방지.
        if (modelMapper.getTypeMap(PaymentInfo.class, GenDataForForwarding.class) == null) {
            modelMapper.addMappings(new PaymentDataMapper());
        }

        return modelMapper;
    }

}