FROM python:3
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . /app
WORKDIR /app
EXPOSE 1081
CMD ["python", "app.py"]
