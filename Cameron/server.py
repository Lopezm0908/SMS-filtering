from flask import Flask, jsonify, request
from BertTesting import classify_string

app = Flask(__name__)
@app.route('/')
def hello_world():
    return 'Hello from Flask!'

@app.route('/api', methods=['POST'])
def api():
    text = request.json.get('text', '')
    print(text)
    predicted_class = classify_string(text)
    print(predicted_class)
    data = { "predicted_class": predicted_class }
    return jsonify(data)

if __name__ == '__main__':
    # app.run(host='0.0.0.0')
    app.run(port=8080)