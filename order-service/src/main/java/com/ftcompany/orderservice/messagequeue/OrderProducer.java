package com.ftcompany.orderservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftcompany.orderservice.OrderDto;
import com.ftcompany.orderservice.messagequeue.dto.Field;
import com.ftcompany.orderservice.messagequeue.dto.KafkaOrderDto;
import com.ftcompany.orderservice.messagequeue.dto.Payload;
import com.ftcompany.orderservice.messagequeue.dto.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate kafkaTemplate;
    private final ModelMapper modelMapper;

    List<Field> fields = Arrays.asList(new Field("string", true, "order_id"),
            new Field("string", true, "account_id"),
            new Field("string", true, "product_id"),
            new Field("int32", true, "qty"),
            new Field("int32", true, "unit_price"),
            new Field("int32", true, "total_price"));
    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(true)
            .name("order_sheet")
            .build();

    public OrderDto send(String topic, OrderDto orderDto){
        Payload payload = modelMapper.map(orderDto,Payload.class);
        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema,payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString ="";
        try {
            jsonInString = mapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send(topic,jsonInString);
        log.info("kafka producer 잘보내짐 : " + kafkaOrderDto);

        return orderDto;
    }
}
