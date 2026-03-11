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


    // Seed simple data
    @Bean
    CommandLineRunner initData(ExercicioRepository exRepo, InstrutorRepository instrRepo, TreinoRepository treinoRepo, AlunoRepository alunoRepo) {
        return args -> {
            Exercicio e1 = new Exercicio("Supino reto","Peito",3,10);
            Exercicio e2 = new Exercicio("Agachamento","Pernas",4,12);
            List<Exercicio> savedEx = exRepo.saveAll(List.of(e1,e2));
            // reassign managed instances
            e1 = savedEx.get(0);
            e2 = savedEx.get(1);

            Instrutor instr = new Instrutor("Marcos","M",35, "12345","marcos@fit.com","senha123");
            instrRepo.save(instr);

            Treino t1 = new Treino("Treino A","Peito/Tríceps","Treino para hipertrofia");
            t1.setInstrutor(instr);
            t1.getExercicios().add(e1);
            treinoRepo.save(t1);


            Aluno a = new Aluno("Julia","F",28,1.65f,60.0f,"Perda de gordura","julia@ex.com","senha");
            alunoRepo.save(a);
        };
    }
}