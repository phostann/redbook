package cc.phos;

import cc.phos.entity.UserEntity;
import cc.phos.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
public class UserApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test() {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();

        List<UserEntity> userEntityEntities = userMapper.selectList(wrapper);
        userEntityEntities.forEach(System.out::println);
    }

    @Test
    public void testObjectMapper() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OffsetDateTime parse = OffsetDateTime.parse("2021-09-30T15:30:00+01:00");
        System.out.println(parse.toString());
//        System.out.println(objectMapper.writeValueAsString(parse));

    }
}
