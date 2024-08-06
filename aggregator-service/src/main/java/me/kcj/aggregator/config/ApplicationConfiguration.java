package me.kcj.aggregator.config;


import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;

import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class ApplicationConfiguration {

    @Bean
    public GrpcChannelConfigurer channelConfigurer(){
        return (channelBuilder, name)-> {
            log.info("channel builder {}", name);
            channelBuilder.executor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }

    @Bean
    public ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter(){
        return new ProtobufJsonFormatHttpMessageConverter(
                JsonFormat.parser().ignoringUnknownFields(),
                JsonFormat.printer().omittingInsignificantWhitespace()
                        .includingDefaultValueFields()
        );
    }
}
