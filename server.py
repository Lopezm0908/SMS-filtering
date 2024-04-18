from flask import Flask, jsonify
from BertTesting import classify_string

app = Flask(__name__)

@app.route('/api', methods=['GET'])
def api():
    # Example input string
    text = "XXXMobileMovieClub: To use your credit, click the WAP link in the next txt message or click here>> http://wap/. xxxmobilemovieclub.com?n=QJKGIGHJJGCBL"

    # log the text
    print(text)

    # Pass example input string to the classify_string function
    predicted_class = classify_string(text)

    # log the predicted class
    print(predicted_class)

    # Return the predicted class as a JSON response
    data = { "predicted_class": predicted_class }

    return jsonify(data)

if __name__ == '__main__':
    app.run(debug=True)