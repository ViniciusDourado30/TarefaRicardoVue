package br.com.tarefas.api.config;

import br.com.tarefas.api.model.Tarefa;
import br.com.tarefas.api.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TarefaRepository tarefaRepository;

    @Autowired
    public DataInitializer(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        tarefaRepository.deleteAll();

        Tarefa t1 = new Tarefa();
        t1.setTitulo("Configurar o backend Spring Boot com MVC");
        t1.setConcluida(true);

        Tarefa t2 = new Tarefa();
        t2.setTitulo("Criar a entidade Tarefa e o Repository");
        t2.setConcluida(true);

        Tarefa t3 = new Tarefa();
        t3.setTitulo("Desenvolver a camada de Serviço e o Controller");
        t3.setConcluida(false);

        tarefaRepository.saveAll(Arrays.asList(t1, t2, t3));

        System.out.println("✅ Base de dados inicializada com tarefas de exemplo.");
    }
}
