import torch

# Check if your GPU is capable of CUDA.
# If not, download saved_weights.pt from discord and use it to run BertTesting instead.

torch.cuda.is_available()
print(torch.cuda.is_available())