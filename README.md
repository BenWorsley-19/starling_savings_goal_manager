# Starling Tech Test

Solution to the Starling Tech Test

## Prerequisites

- JDK 17.+
- Set up environment variables for the Starling API
  - `STARLING_AUTH_TOKEN`

## Notes for review

Throughout the code I have added notes where I wanted to explain something marked with NOTE_FOR_REVIEWER. I wouldn't 
usually put this level of comments in the code and it does make the code look a bit messy but I wanted to explain my
thought process.

Some things I would have liked to have done if I had more time:
- Build out a REST API
- Consider error handling further
- Consider design around api client. Class is currently doing a fair bit -  is it best to split more of it out
- Add to unit test details of the request such as what headers are added
- Add to unit test details of the request such as what headers are added
- Bulk out integration tests
- Provide mock servers for integration tests
- Implement refresh of OAuth
- Investigate how to handle when there are insufficient funds in the account

