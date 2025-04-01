import json
import unicodedata
from flask import Flask, request, jsonify
from flask_cors import CORS
import pandas as pd
import os

# Define o diretório de trabalho
os.chdir(os.path.dirname(os.path.abspath(__file__)))

# Função principal
app = Flask(__name__)
CORS(app)

# Carregar o DataFrame globalmente
df = pd.read_csv('Relatorio_cadop.csv', delimiter=';', encoding='utf-8')

# Função para remover acentos
def remove_accent(text: str) -> str:
    return ''.join(
        c for c in unicodedata.normalize('NFD', text)
        if unicodedata.category(c) != 'Mn'
    )

# Função para realizar a busca no DataFrame
def search_operadoras(df: pd.DataFrame, query: str) -> pd.DataFrame:
    try:
        query = remove_accent(query.lower())  # Remove acento e converte para minúsculas
        # Aplica a busca em todas as colunas
        result = df[df.apply(lambda row: row.astype(str).apply(remove_accent).str.contains(query, case=False, na=False).any(), axis=1)]
        return result
    except Exception as e:
        print(f"Erro ao buscar na coluna {query}: {e}")
        return pd.DataFrame()

# Rota para busca, com paginação fixa de 10 resultados por página
@app.route('/search', methods=['GET'])
def search():
    try:
        # Pegando os parâmetros de consulta
        query = request.args.get('query', '')
        page = int(request.args.get('page', 1))  # Página atual (default é 1)
        per_page = 15  # Número fixo de resultados por página
        
        # Buscar os resultados no DataFrame
        result = search_operadoras(df, query)
        
        # Paginação
        total_results = len(result)  # Total de resultados
        total_pages = (total_results // per_page) + (1 if total_results % per_page > 0 else 0)  # Total de páginas
        
        # Calcular os índices de início e fim
        start_idx = (page - 1) * per_page
        end_idx = start_idx + per_page
        
        # Subconjunto dos resultados baseado nos índices calculados
        paginated_result = result.iloc[start_idx:end_idx]

        # Preparar a resposta com os dados paginados
        return jsonify({
            "total_results": total_results,
            "total_pages": total_pages,
            "current_page": page,
            "per_page": per_page,
            "results": json.loads(paginated_result.to_json(orient='records'))
        }), 200
    
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, port=1081)
