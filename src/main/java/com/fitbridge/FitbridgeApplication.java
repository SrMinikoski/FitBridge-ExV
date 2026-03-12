package com.fitbridge;



import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import com.fitbridge.model.*;
import com.fitbridge.repository.*;


import java.util.List;


@SpringBootApplication
public class FitbridgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(FitbridgeApplication.class, args);
    }
}