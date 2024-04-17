import torch
import numpy as np
import pandas as pd
from transformers import AutoModel, BertTokenizerFast
import torch.nn as nn
import threading
from queue import Queue

# Define a function to preprocess the new string
def preprocess_text(text):
    tokenizer = BertTokenizerFast.from_pretrained('bert-base-uncased')
    tokens = tokenizer.encode_plus(text, max_length=25, padding='max_length', truncation=True, return_tensors='pt')
    return tokens['input_ids'], tokens['attention_mask']

# Define your BERT model architecture
class BERT_Arch(nn.Module):
    def __init__(self, bert):
        super(BERT_Arch, self).__init__()
        self.bert = bert
        self.dropout = nn.Dropout(0.1)
        self.relu = nn.ReLU()
        self.fc1 = nn.Linear(768, 512)
        self.fc2 = nn.Linear(512, 2)
        self.softmax = nn.LogSoftmax(dim=1)

    def forward(self, sent_id, mask):
        _, cls_hs = self.bert(sent_id, attention_mask=mask, return_dict=False)
        x = self.fc1(cls_hs)
        x = self.relu(x)
        x = self.dropout(x)
        x = self.fc2(x)
        x = self.softmax(x)
        return x

# Load the BERT model
bert = AutoModel.from_pretrained('bert-base-uncased')

# Create an instance of the BERT model architecture
model = BERT_Arch(bert)

# Load the trained weights
path = 'saved_weights.pt'
model.load_state_dict(torch.load(path))
model.eval()  # Set the model to evaluation mode

# Define a function to classify a string as spam or ham
def classify_string(text):
    # Preprocess the text
    input_ids, attention_mask = preprocess_text(text)
    
    # Make predictions
    with torch.no_grad():
        preds = model(input_ids, attention_mask)
    
    # Convert predictions to numpy array
    preds = preds.detach().cpu().numpy()
    
    # Get the predicted label
    predicted_label = np.argmax(preds, axis=1)[0]
    
    # Map the predicted label to 'ham' or 'spam'
    label_mapping = {0: 'ham', 1: 'spam'}
    predicted_class = label_mapping[predicted_label]
    
    return predicted_class

# Define a function to classify rows from the CSV file
def classify_rows(start_index, end_index, results_queue):
    for index in range(start_index, end_index):
        row = data.iloc[index]
        text_to_classify = row['v2']
        classification = classify_string(text_to_classify)
        results_queue.put((index, classification))

# Read the CSV file
data = pd.read_csv("spam.csv", encoding='latin', usecols=['v1', 'v2'])

# Prompt the user to specify rows from the CSV file
requested_slice = input("What rows do you want from the csv file (excluding the first row v1, v2)? Enter as two integers separated by a comma, e.g., '1,2', '2,1000'. The highest index is 5372:\n")
start_index, end_index = map(int, requested_slice.split(","))

# Define the number of threads
num_threads = 8  # You can adjust this based on your system's capabilities

# Calculate the number of rows per thread
rows_per_thread = (end_index - start_index) // num_threads

# Create a queue to store classification results
results_queue = Queue()

# Create and start threads
threads = []
for i in range(num_threads):
    thread_start = start_index + i * rows_per_thread
    thread_end = thread_start + rows_per_thread
    if i == num_threads - 1:
        thread_end = end_index
    thread = threading.Thread(target=classify_rows, args=(thread_start, thread_end, results_queue))
    threads.append(thread)
    thread.start()

# Wait for all threads to complete
for thread in threads:
    thread.join()

# Collect and sort results based on index
results = []
correct_predictions = 0
total_predictions = 0
while not results_queue.empty():
    results.append(results_queue.get())

# Sort results based on index
results.sort(key=lambda x: x[0])

# Print results
for index, classification in results:
    original_classification = data.iloc[index]['v1']
    content = data.iloc[index]['v2']
    print(f"Row {index}: Original Class - {original_classification}, Predicted Class - {classification}, Content - {content}")
    if original_classification == classification:
        correct_predictions += 1
    total_predictions += 1

# Calculate accuracy score
accuracy = (correct_predictions / total_predictions) * 100
print(f"\nAccuracy: {accuracy:.2f}% (Correct predictions: {correct_predictions}, Total predictions: {total_predictions})")