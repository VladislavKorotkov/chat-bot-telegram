# Telegram chatbot
> This bot is designed to parse user messages and save the received data in Google Tables

## General Information
- This software product allows you to automate the routine process of entering information about the arrival and expenditure of the company
- The goal is to reduce the time spent by employees

## Technologies Used
- Java
- Spring Boot
- Google API
- Docker

## Features
- Parsing a message like: +/- <cost> <description> <order>
- Saving to Google Tables
- Integration with 1С (not implemented)


## Quick Start
```
# clone repository
git clone https://github.com/VladislavKorotkov/chat-bot-telegram.git
# Add your credentials for google api and telegram bot api
# start the server
mvn spring-boot:run
```
