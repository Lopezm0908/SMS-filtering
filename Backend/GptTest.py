from openai import OpenAI
from dotenv import load_dotenv
import os
load_dotenv()


key = os.getenv("API_KEY")

client = OpenAI(
  api_key=key
)




""" completion = client.completions.create(
  model="davinci-002",
  messages=[
    {"role": "user", "content": "Where was the world series in 2020 played?"}
  ]
) """


completion = client.chat.completions.create(
  model="gpt-3.5-turbo",
  messages=[
    {"role": "user", "content": "Is this text message potentially a scam message? Hey Dave, how's it going?"}
  ]
)

print(completion.choices[0].message)
