from openai import OpenAI
from dotenv import load_dotenv
import os
load_dotenv()


key = os.getenv("API_KEY")

client = OpenAI(
  api_key=key
)

completion = client.chat.completions.create(
  model="gpt-3.5-turbo",
  max_tokens=10,
  messages=[
    {"role": "user", "content": "Is this text message potentially a scam message (reply in either a yes or a no)? Hey Dave, how's it going?"}
  ]
)

print(completion.choices[0].message)
