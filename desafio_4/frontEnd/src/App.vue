<template lang="pug">
div
  div(style="margin-bottom: 0.5rem; display: flex; justify-content: center;")
    el-text(class="mx-1" style="font-size: 38px;") Busca de Operadoras
  el-card(style="padding: 0; margin-top: 1rem;")
    div(style="justify-content: end; position: relative; display: flex;")
      el-input(
        v-model="query"
        style="width: 300px; margin-bottom: 0.5rem;"
        placeholder="Digite um termo para buscar"
        clearable
        @input="searchOperadoras"
      )
    div
      el-table(:data="operadoras" border style="width: 100%  ")
        el-table-column(v-for="col in columns" :prop="col.prop" :label="col.label" :width='col.width')
      el-pagination(style="margin-top: 20px; display: flex; justify-content: flex-end;"
        background layout="prev, pager, next" 
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
      columns: [
        { prop: "Registro_ANS", label: "Registro ANS", width: "150px"},
        { prop: "CNPJ", label: "CNPJ Social", width: "140"},
        { prop: "Razao_Social", label: "Razão Social", width: "440px"},
        { prop: "Modalidade", label: "Modalidade" },
        { prop: "Telefone", label: "Telefone"},
        { prop: "Cidade", label: "Cidade"},
        { prop: "Representante", label: "Representante"},
      ],
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
        const response = await axios.get(`http://localhost:1081/search?query=${this.query.trim()}&page=${this.page}`);
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
</style>
