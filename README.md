# IntuitiveCare - TESTE DE NIVELAMENTO

# 1 - TESTE DE WEB SCRAPING
  Este serviço foi desenvolvido em Java 17 juntamente com o Spring. Para executar este teste, é necessário ter o ChromeDriver. Existe uma pasta chamada drive que contém o driver referente à versão 134.0.6998.165 do Google Chrome. Caso a versão seja 
    diferente, baixe o driver correspondente no site oficial do Google: ChromeDriver Downloads e coloque dentro da para src/drive com o seguinte nome chromedriver.
  Antes de executar o serviço, é necessário realizar algumas configurações, como alterar o local de download dos arquivos. Para isso, acesse o arquivo application.properties, localize a parte referente ao directory e 
   altere para o caminho desejado.
  Na linha 32 dentro da service, há um trecho de código responsável por clicar no botão de aceitação de cookies da página do Gov. Caso os cookies estejam desativados por padrão no navegador, comente ou remova esse trecho de código.
  O serviço pode ser executado pelo Postman, utilizando a seguinte rota: http://localhost:1080/api/webScraping/downloadAndCompressFiles metodo do tipo POST, ou caso deseje pode acessar a documentação(swegger) na seguinte 
    rota no navegador: http://localhost:1080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config, este serviço irá abrir o navegador e forma automática baixar os arquivos necessários, em seguida, irá compactar os 
    arquivos em .zip chamando Anexos.zip. 

# 2 - TESTE DE TRANSFORMAÇÃO DE DADOS
   Este serviço foi desenvolvido em Java 17 juntamente com o Spring. O metodo pode ser acessado pelo Postman na seguinte rota: http://localhost:1080/api/transformData/processPdfAndExportToCSV. metodo do tipo POST, ou caso deseje pode acessar 
    a documentação(swegger) na seguinte rota no navegador: http://localhost:1080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config.
   Para executar este serviço, é obrigatório que o serviço de WEB SCRAPING tenha sido executado previamente. Este serviço de transformação de dados buscará um documento específico dentro do arquivo .zip gerado anteriormente.
    Após a extração do documento, ele realizará a extração dos dados da tabela e gerará um arquivo .csv contendo os dados extraídos. Este arquivo .csv será compactado em .ZIP e salvo no diretório especificado anteriormente com o nome de Teste_Joao_Victor.zip

# 3 - TESTE DE BANCO DE DADOS
  Para esta etapa, foi gerado um arquivo .sql utilizando MYSQL dentro da pasta "desafio 3". Para executá-lo, será necessário criar um banco de dados MYSQL e rodar o script no mesmo. Além disso, é importante colocar os arquivos solicitados(Relatorio_cadop.csv e demonstrações contabeis) no desafio dentro da pasta de arquivos do MySQL.
  O próprio arquivo .sql contém comentários que explicam passo a passo o que deve ser seguido durante a execução.
  links: https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/ , https://dadosabertos.ans.gov.br/FTP/PDA/operadoras_de_plano_de_saude_ativas/

# 4 - TESTE DE API
  Para seguir com essa etapa, primeiro você precisa possui o python instalado, uma vez instalado entre na pasta backend e execute o seguinte comando: phyton app.py ou utilize o arquivo executavel que se enconta na pasta. Depois, no front-end, é necessário ter o Node.js 20.17 instalado. Com isso, basta rodar os comandos: npm install, npm run serve.
  Feito isso, a quarta etapa estará funcionando. O front-end vai se comunicar com o back-end por meio do Relatorio_cadop.csv que se encontra dentro da pasta desafio_4/backend, e exibira uma lista paginada de operadoras. No topo da tabela, haverá um campo de pesquisa onde você poderá buscar qualquer informação presente nas colunas do arquivo.

