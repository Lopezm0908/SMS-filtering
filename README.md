# Inbox Guardian

## Description 
	Inbox Guardian is an SMS spam filter meant to be deployed in addition to any existing SMS application allowing users 
	the ability to enjoy all the features of their favorite SMS application along with the added protection against spam 
	and fishing messages. Inbox Guardian employs the use of a logistic regression model, the BERT language model, and 
	other holistic methods such as keyword recognition to flag spam messages.
	
## Why?
	Spam is a pervasive threat to our productivity and safety when using mobile devices, we at Inbox Guardian find the 
	current integrated spam filters in most messaging applications to be inadequate to block the constant barrage of 
	ever evolving spam messages. This is why we designed the Inbox Guardian, a robust and comprehensive protective measure 
	using the latest artificial intelligence to block spam with a tested 90% accuracy rate.

## Quick Start

### Setup the inbox Guardian API 
	1. Clone repository [Link](https://github.com/CameronJ2/InboxGuardian-API)
	2. Install requirements listed in the requirements text folder contained in the main directory using the command 
		pip install -r requirements.txt
	3. Run the server.py file using the command 
		python server.py
### Run the project in Android Studio
	1. Open the application under smsfilteringapplication 
	2. Run the mainactivity.kt file
### Ensure app is default SMS app 
	1. Open settings 
	2. Navigate to apps
	3. Navigate to Default Apps 
	4. Navigate to SMS app 
	5. Select smsfilteringapplication from the list of apps given 
## Usage 

**Available Features:**
1. **Whitelist a Number** - Exempt a number from analysis 
2. **Blacklist a Number** - Block all incoming texts and calls from a specific number 
3. **Keyword Blocking** - Block messages containing specific keywords 
4. **Message Reporting** - Report a message as spam, this blacklists the number and deletes the message.
5. **Evaluation Mailbox** - If a message is blocked by keyword analysis, BERT analysis, or logistic regression analysis, the message is sent here. The user can then decide whether to flag the message as spam and block the number or send the message through to the inbox.
5. **BERT Analysis** - Using the BERT language model we can accurately detect whether or not a message is spam or not spam. 
6. **Logistic Regression Analysis** - Using a machine learning model trained on a large dataset of spam messages we are able to accurately detect 	whether or not a message is spam or not spam.
7. **Settings Menu** - Opt in and out of different filters and features to make the application better suit an individual users needs
## Examples 

### Adding a number to the whitelist
	1. Use the gui to navigate to the whitelist menu
	2. Press the add number button
	3. Enter the number of your choice
	4. Hit the confirm button
	5. Your number is now in the whitelist 
### Adding a number to the blacklist
	1. Use the gui to navigate to the blacklist menu
	2. Press the add number button
	3. Enter the number of your choice
	4. Hit the confirm button
	5. Your number is now in the blacklist
### Adding a keyword to filter out
	1. Use the gui to navigate to the keyword menu
	2. Press the add keyword button
	3. Enter the keyword of your choice
	4. Hit the confirm button
	5. Your keyword is now a flag for spam messages
### Reporting a message as spam 
	1. Use the gui to navigate to the message reporting screen
	2. Select the message you would like to report by pressing on the message itself 
	3. In the pop up menu just press confirm 
	4. The message is now deleted and the number is blocked.
### Evaluating a message 
	1. Use the gui to navigate to the evaluation mailbox screen
	2. Select the message you would like to evaluate by pressing on the message itself
	3. In the pop up menu select either flag or approve 
	4. If you approve the message it will be sent to the inbox
	5. If you flag the message the message is deleted and the number that sent it is added to the blacklist
### Edit Settings 
	1. Use the gui to navigate to the settings menu 
	2. Use the checkboxes to opt in and out of specific features or filters 
## Demo

https://github.com/Lopezm0908/SMS-filtering/assets/158241209/9370c61f-fa78-4955-aba4-418992dd5b7c


