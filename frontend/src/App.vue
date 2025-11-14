<script setup>
import { ref, onMounted, nextTick } from "vue";
import tarefaService from "./services/tarefaService";

const tarefas = ref([]);
const novaTarefaTitulo = ref("");
const erro = ref(null);
const tarefaEditandoId = ref(null);
const tituloEditando = ref("");

async function carregarTarefas() {
  try {
    erro.value = null;
    const response = await tarefaService.getTarefas();
    tarefas.value = response.data.sort(
      (a, b) => a.concluida - b.concluida || a.id - b.id
    );
  } catch (e) {
    erro.value = "Falha ao carregar tarefas.";
    console.error(e);
  }
}

async function adicionarTarefa() {
  if (!novaTarefaTitulo.value.trim()) return;
  const novaTarefa = { titulo: novaTarefaTitulo.value, concluida: false };
  try {
    erro.value = null;
    const response = await tarefaService.addTarefa(novaTarefa);
    tarefas.value.push(response.data);
    novaTarefaTitulo.value = "";
    await carregarTarefas();
  } catch (e) {
    erro.value = "Falha ao adicionar tarefa.";
    console.error(e);
  }
}

async function removerTarefa(id) {
  try {
    erro.value = null;
    await tarefaService.deleteTarefa(id);
    tarefas.value = tarefas.value.filter((t) => t.id !== id);
  } catch (e) {
    erro.value = "Falha ao remover tarefa.";
    console.error(e);
  }
}

async function alternarStatus(tarefa) {
  try {
    erro.value = null;
    const novoStatus = !tarefa.concluida;
    const response = await tarefaService.updateStatusTarefa(
      tarefa.id,
      novoStatus
    );
    tarefa.concluida = response.data.concluida;
    await carregarTarefas();
  } catch (e) {
    erro.value = "Falha ao atualizar status.";
    console.error(e);
  }
}

function ativarEdicao(tarefa) {
  tarefaEditandoId.value = tarefa.id;
  tituloEditando.value = tarefa.titulo;
  nextTick(() => {
    const el = document.querySelector(`#input-edit-${tarefa.id}`);
    if (el) el.focus();
  });
}

async function salvarEdicao(tarefa) {
  if (
    !tituloEditando.value.trim() ||
    tituloEditando.value.trim() === tarefa.titulo
  ) {
    cancelarEdicao();
    return;
  }
  try {
    erro.value = null;
    const response = await tarefaService.updateTituloTarefa(
      tarefa.id,
      tituloEditando.value
    );
    tarefa.titulo = response.data.titulo;
    cancelarEdicao();
  } catch (e) {
    erro.value = "Falha ao salvar edição.";
    console.error(e);
  }
}

function cancelarEdicao() {
  tarefaEditandoId.value = null;
  tituloEditando.value = "";
}

onMounted(carregarTarefas);
</script>

<template>
  <div class="container">
    <header>
      <h1>Lista de Tarefas</h1>
      <p>Aplicação Full-Stack com Vue e Spring Boot</p>
    </header>
    <main>
      <div class="form-container">
        <input
          v-model="novaTarefaTitulo"
          @keyup.enter="adicionarTarefa"
          placeholder="O que precisa ser feito?"
        />
        <button @click="adicionarTarefa">+</button>
      </div>

      <p v-if="erro" class="erro">{{ erro }}</p>

      <ul class="tarefa-lista">
        <li
          v-for="tarefa in tarefas"
          :key="tarefa.id"
          :class="{ concluida: tarefa.concluida }"
        >
          <div class="info-tarefa">
            <input
              type="checkbox"
              :checked="tarefa.concluida"
              @change="alternarStatus(tarefa)"
            />
            <span v-if="tarefaEditandoId !== tarefa.id" @dblclick="ativarEdicao(tarefa)">
              {{ tarefa.titulo }}
            </span>
            <input
              v-else
              :id="`input-edit-${tarefa.id}`"
              type="text"
              v-model="tituloEditando"
              @blur="salvarEdicao(tarefa)"
              @keyup.enter="salvarEdicao(tarefa)"
              @keyup.esc="cancelarEdicao"
              class="input-edicao"
            />
          </div>
          <div class="acoes">
            <button @click="removerTarefa(tarefa.id)" class="btn-remover">×</button>
          </div>
        </li>
      </ul>
    </main>
  </div>
</template>

<style>
:root {
  --cor-fundo: #1a1a1a;
  --cor-surface: #242424;
  --cor-primaria: #646cff;
  --cor-texto: rgba(255, 255, 255, 0.87);
  --cor-borda: #424242;
  --cor-perigo: #ff6b6b;
}
body {
  font-family: "Inter", system-ui, Avenir, Helvetica, Arial, sans-serif;
  background-color: var(--cor-fundo);
  color: var(--cor-texto);
  margin: 0;
  display: flex;
  justify-content: center;
  padding-top: 40px;
}
.container {
  width: 100%;
  max-width: 680px;
  margin: 0 auto;
  padding: 0 20px;
}
.form-container {
  display: flex;
  margin-bottom: 20px;
  gap: 10px;
}
.form-container input {
  flex-grow: 1;
  padding: 12px 15px;
  font-size: 1em;
  background-color: var(--cor-surface);
  border: 1px solid var(--cor-borda);
  border-radius: 8px;
  color: var(--cor-texto);
}
.form-container button {
  width: 48px;
  height: 48px;
  font-size: 2em;
  border-radius: 8px;
  background-color: var(--cor-primaria);
  color: white;
  cursor: pointer;
}
.erro { color: var(--cor-perigo); text-align:center; margin-bottom:20px }
.tarefa-lista { list-style:none; padding:0; margin:0 }
.tarefa-lista li { display:flex; justify-content:space-between; align-items:center; padding:15px; background:var(--cor-surface); border-bottom:1px solid var(--cor-borda) }
.input-edicao { font-size:1em; padding:5px; background:var(--cor-fundo); border:1px solid var(--cor-primaria); color:var(--cor-texto); border-radius:4px }
.btn-remover { background:none; border:none; color:#a0a0a0; font-size:1.5em; cursor:pointer; }
</style>
