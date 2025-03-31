<template lang="pug">
div
  div(style="margin-bottom: 0.5rem; display: flex; justify-content: center;")
    el-text(class="mx-1" style="font-size: 38px;") Busca de Operadoras
  el-input(
    v-model="query"
    style="width: 240px; margin-bottom: 0.5rem;"
    placeholder="Digite um termo para buscar"
    clearable
    @input="searchOperadoras"
  )
  
  div
    el-text(class="mx-1" style="font-size: 36px;") Resultados:
    el-table(:data="operadoras" border style="width: 100%  ")
      el-table-column(prop="Registro_ANS" label="Registro ANS")
      el-table-column(prop="CNPJ" label="CNPJ" width="180")
      el-table-column(prop="Razao_Social" label="Razão Social")
      el-table-column(prop="Telefone" label="Telefone")
        template(#default="{ row }")
          span {{ '(' +row.DDD + ') ' + row.Telefone }}
      el-table-column(label="Cidade" )
        template(#default="{ row }")
          span {{ row.UF + '/' + row.Cidade }}
    el-pagination(style="margin-top: 20px; display: flex; justify-content: flex-end;"
      background layout="prev, pager, next" :total="1000" 
      @change="handlePage"
      :page-count="totalPage"
      :default-current-page="1"
      :page="page"
      )
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
      page: 1,
      totalResult: 0,
      pageSize: 10, 
      totalPage: 0,
      query: '', 
      operadoras: [] 
    };
  },
  created() {
    this.searchOperadoras(); 
  },
  methods: {
    handlePage(page) {
      this.page = page;
      this.searchOperadoras()
    },
    async searchOperadoras() {
      try {
        const response = await axios.get(`http://localhost:1081/search?query=${this.query}&page=${this.page}`);
        console.log('Resposta recebida:', response.data);
        this.totalPage = response.data.total_pages;
        this.page = response.data.current_page;
        this.totalResult = response.data.total_results;
        this.operadoras = response.data.results;
      } catch (error) {
        console.error("Erro ao buscar operadoras:", error);
        this.operadoras = [];
      }
    }
  }
};
</script>

<style scoped>
#app {
  font-family: Arial, sans-serif;
  padding: 20px;
}

input {
  padding: 10px;
  font-size: 16px;
  margin-bottom: 20px;
  width: 300px;
}

h2 {
  margin-top: 20px;
}
</style>
