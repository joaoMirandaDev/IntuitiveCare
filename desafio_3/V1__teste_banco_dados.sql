/* RODE AS SQL DE UMA A UMA PARA EVITAR POSSIVEIS ERROS, CADA SQL TEM O LIMITADOR DE ; */

/* 
    Criação da tabela "operadoras", que armazenará informações sobre operadoras
*/

CREATE TABLE operadoras (
    id BIGINT NOT NULL AUTO_INCREMENT,
    registro_ans TEXT NOT NULL,
    cnpj VARCHAR(14) NOT NULL,
    razao_social VARCHAR(255) NOT NULL,
    nome_fantasia VARCHAR(255),
    modalidade VARCHAR(100) NOT NULL,
    logradouro VARCHAR(255) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(255),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    uf CHAR(2) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    ddd VARCHAR(3),
    telefone VARCHAR(20),
    fax VARCHAR(20),
    endereco_eletronico VARCHAR(255),
    representante VARCHAR(255),
    cargo_representante VARCHAR(100) NOT NULL,
    regiao_comercializacao VARCHAR(100),
    data_registro_ans DATE,
    PRIMARY KEY (id)
);

/* Para importar o csv, coloque o arquivo .csv dentro da pasta desejada e altere a rota no comando abaixo */

LOAD DATA INFILE '/var/lib/mysql-files/Relatorio_cadop.csv'
INTO TABLE operadoras
FIELDS TERMINATED BY ';'     
ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 ROWS 
(
    registro_ans,
    cnpj,
    razao_social,
    nome_fantasia,
    modalidade,
    logradouro,
    numero,
    complemento,
    bairro,
    cidade,
    uf,
    cep,
    ddd,
    telefone,
    fax,
    endereco_eletronico,
    representante,
    cargo_representante,
    regiao_comercializacao,
    data_registro_ans
);

/* 
    Criação da tabela "dados_contabeis", que armazenará dados contábeis das operadoras,
    como saldo inicial e final de contas.
*/

CREATE TABLE dados_contabeis (
    id BIGINT NOT NULL AUTO_INCREMENT,
    data VARCHAR(15) NOT NULL,
    reg_ans TEXT NOT NULL,
    cd_conta_contabil VARCHAR(20) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    vl_saldo_inicial VARCHAR(255) NOT NULL,
    vl_saldo_final VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

/* Para importar o arquivo .csv, coloque-o na pasta desejada e altere a rota no comando abaixo. Será necessário alterar a rota 8 vezes, pois essa é a
 quantidade de arquivos que precisam ser importados. Após importar cada arquivo, atualize a rota no comando para importar o próximo. */

LOAD DATA INFILE '/var/lib/mysql-files/2023/1T2023.csv'
INTO TABLE dados_contabeis
FIELDS TERMINATED BY ';'     
ENCLOSED BY '"' 
LINES TERMINATED BY '\n' 
IGNORE 1 ROWS 
(
    data,
    reg_ans,
    cd_conta_contabil,
    descricao,
    vl_saldo_inicial,
    vl_saldo_final
);

/* RODAR ESTE TRECHO SOMENTE APÓS RODAR AS TABELAS MENCIONADAS ACIMA */


/* 
    Atualização da coluna "data" na tabela "dados_contabeis" para o formato DATE,
    convertendo a string para um formato de data.
*/

UPDATE dados_contabeis
SET data = STR_TO_DATE(data, '%d/%m/%Y')
WHERE data LIKE '%/%/%';

ALTER TABLE dados_contabeis MODIFY COLUMN data DATE NOT NULL;

/* 
    Atualização dos valores de vl_saldo_inicial e vl_saldo_final, substituindo as vírgulas
    por pontos para garantir que os valores sejam reconhecidos corretamente como números decimais.
*/

UPDATE dados_contabeis
SET 
    vl_saldo_inicial = REPLACE(vl_saldo_inicial, ',', '.'),
    vl_saldo_final = REPLACE(vl_saldo_final, ',', '.')
WHERE 
    vl_saldo_inicial LIKE '%,%' OR vl_saldo_final LIKE '%,%';

/* 
    Alteração das colunas vl_saldo_inicial e vl_saldo_final para o tipo DECIMAL(15, 2),
    garantindo que os valores sejam armazenados como números decimais com duas casas decimais.
*/
   
ALTER TABLE dados_contabeis
MODIFY COLUMN vl_saldo_inicial DECIMAL(15, 2),
MODIFY COLUMN vl_saldo_final DECIMAL(15, 2);

/* 
    Criação de índices nas colunas "vl_saldo_inicial" e "vl_saldo_final" para melhorar o desempenho
    de consultas deses campos.
*/

CREATE INDEX idx_saldo_inicial ON dados_contabeis(vl_saldo_inicial);
CREATE INDEX idx_saldo_final ON dados_contabeis(vl_saldo_final);

/* Consulta os registros com as maiores despesas com base na descrição no ultimo trimeste com limite de 10 */

SELECT o.registro_ans, o.razao_social, o.cnpj, dc.descricao,
       sum(dc.vl_saldo_inicial) as valor_inicial, 
       sum(dc.vl_saldo_final) as valor_final
FROM dados_contabeis dc
INNER JOIN operadoras o ON o.registro_ans = dc.reg_ans
WHERE dc.descricao LIKE '%EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS  DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR%'
AND dc.data BETWEEN '2024-10-01' AND '2024-12-31'
GROUP BY o.registro_ans, o.razao_social, o.cnpj, dc.descricao  ORDER BY valor_final DESC LIMIT 10;

/* Consulta os registros com as maiores despesas com base no ultimo ano com limite de 10 */

SELECT o.registro_ans, o.razao_social, o.cnpj, dc.descricao,
       sum(dc.vl_saldo_inicial) as valor_inicial, 
       sum(dc.vl_saldo_final) as valor_final
FROM dados_contabeis dc
INNER JOIN operadoras o ON o.registro_ans = dc.reg_ans
WHERE dc.descricao LIKE '%EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS  DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR%'
AND dc.data BETWEEN '2024-01-01' AND '2024-12-31'
GROUP BY o.registro_ans, o.razao_social, o.cnpj, dc.descricao ORDER BY valor_final DESC LIMIT 10;  




