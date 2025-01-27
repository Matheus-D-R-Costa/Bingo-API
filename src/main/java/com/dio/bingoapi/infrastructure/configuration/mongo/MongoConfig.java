package com.dio.bingoapi.infrastructure.configuration.mongo;

import com.dio.bingoapi.infrastructure.configuration.mongo.converter.DateToOffsetDateTimeConverter;
import com.dio.bingoapi.infrastructure.configuration.mongo.converter.OffsetDateTimeToDateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        var converters = List.of(new OffsetDateTimeToDateConverter(), new DateToOffsetDateTimeConverter());
        return new MongoCustomConversions(converters);
    }

}
