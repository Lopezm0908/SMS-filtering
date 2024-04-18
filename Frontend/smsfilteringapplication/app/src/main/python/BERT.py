import json
import requests

# API endpoint URL
url = 'http://127.0.0.1:5000/api'



# Set the headers to specify JSON content type
headers = {'Content-Type': 'application/json'}



def BertApiRequest(message):
    # JSON payload to send
    payload = {
        "text": message
    }

    # Convert payload to JSON string
    json_payload = json.dumps(payload)
    # Send POST request
    response = requests.post(url, data=json_payload, headers=headers)

    # Check if the request was successful (HTTP status code 200)
    if response.status_code == 200:
        # Print response data
        receivedJSON = response.text
        thirdQuoteIndex = receivedJSON.find('"', receivedJSON.find('"', receivedJSON.find('"') + 1) + 1)
        fourthQuoteIndex = receivedJSON.find('"', thirdQuoteIndex + 1)
        determination = receivedJSON[thirdQuoteIndex+1:fourthQuoteIndex]
        if determination == "ham":
            return False
        elif determination == "spam":
            return True
    else:
        # Print error message
        print(f"Error: {response.status_code} - {response.text}")


def returnInt():
    return 7

