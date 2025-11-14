package br.com.tarefas.api.controller; // Pacote corrigido

import br.com.tarefas.api.model.Tarefa;
import br.com.tarefas.api.service.TarefaService; // Importa o serviço
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tarefas") // Caminho corrigido para não duplicar /api
public class TarefaController {

    @Autowired
    private TarefaService tarefaService; // Injeta o Serviço, não o Repositório

    @GetMapping
    public ResponseEntity<List<Tarefa>> listar() {
        List<Tarefa> tarefas = tarefaService.listarTodas();
        return ResponseEntity.ok(tarefas);
    }

    @PostMapping
    public ResponseEntity<Tarefa> criar(@RequestBody Tarefa tarefa) {
        Tarefa novaTarefa = tarefaService.criarTarefa(tarefa);
        return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Tarefa> atualizarStatus(@PathVariable Long id, @RequestBody java.util.Map<String, Boolean> status) {
        Tarefa tarefaAtualizada = tarefaService.atualizarStatus(id, status.get("concluida"));
        return ResponseEntity.ok(tarefaAtualizada);
    }

    @PatchMapping("/{id}/titulo")
    public ResponseEntity<Tarefa> atualizarTitulo(@PathVariable Long id, @RequestBody java.util.Map<String, String> payload) {
        Tarefa tarefaAtualizada = tarefaService.atualizarTitulo(id, payload.get("titulo"));
        return ResponseEntity.ok(tarefaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }
}