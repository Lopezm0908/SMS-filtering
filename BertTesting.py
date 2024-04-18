import torch
import numpy as np
import pandas as pd
from transformers import AutoModel, BertTokenizerFast
import torch.nn as nn

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
model.load_state_dict(torch.load(path, map_location=torch.device('cpu')))
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

# Test the function with a hardcoded string
# text_to_classify = "You did great on the spelling bee!"
# classification = classify_string(text_to_classify)
# print("Predicted Class:", classification)