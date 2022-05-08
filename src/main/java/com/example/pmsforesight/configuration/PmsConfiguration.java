package com.example.pmsforesight.configuration;

import com.example.pmsforesight.service.Validator;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PmsConfiguration {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        return modelMapper;
    }

    @Bean
    public DataBaseConstructor fillDb() {
        return new DataBaseConstructor();
    }

    @Bean
    public Validator getValidator(){
        return new Validator();
    }


}
