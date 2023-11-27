# Blockchain Based Election with Security

A **Blockchain** app to help with **Elections** and **Voting** that's **Secure**.

## Authors

- [@Ricardo Saúde](https://www.github.com/pzmq)
- [@Pedro Martinho](https://www.github.com/a23299)

## Registration and Login
When u open the app you get presented with a login panel, if you have a login you should have a private key, in a file that can open and then just type your password and you are in.

If you don't have a login, you will to register your information in the app. We only ask for a username, and a password that you need to write twice to confirm it is the right one.

## Voting

After you get through the login you get to the voting panel and you can procced to vote.
At this moment in time app is still in development and it is in a rough state for consumer use, so we show everyting that's happening.

In the Voting section you can Vote on who you want and please. After that you can see that your vote as gone through in the ledger section.

On the Votes section you can see the various votes that occurred and in the blockchain section yu can see the information that we send to the blockchain.

## TODO:
### Voting
- Only if a Election has started can users access this menu.
- Only users who did not vote yet can see this GUI.
- After "Voting" a message saying who voted for who as well as a kick is needed.
- Missing Dropdown for candidates

### Administrative Menu
- Create system where there is a user Administrator that can grant access to the Administrative Menu to certain people based on their keys.
- Redesign interface. (Right now is a copy of the old "ElectionGui")
- If Election hasn't started be able to start one.
- Be able to check how many votes in total have been cast, how many per candidate and how many blocks were produced.
- Be able to check the Blocks and the Merkle Trees in each Block (Not sure if needed).
- Must be able to run several Elections at the same time.
- Each Election must have:
    - Name
    - Par of Keys (Private and public) 
    - Start and End Date (Limit user visibility)

## Project Updates 

### Election Info
- Added panel to reuse when adding new election to administrative Menu.

### Administrative Menu
- Added a new administrative menu for enhanced project management and administration.
- This menu provides additional functionality and options for project administrators. (Not yet finished just a Mockup)

### Login System Modification
- Implemented changes to the login system to enhance user experience.

### Changes to Voting (formerly ElectionGui)
- Renamed the component responsible for handling voting functionality to better reflect its purpose.
- Enhanced the user interface and functionality related to the voting process. (Only for voting now)

### Variable Name Changes
- Refactored variable names for improved clarity and consistency throughout the codebase.
- Ensured that variable names are more descriptive and aligned with best practices.

### Backup Creation
- Previous data has been backed up before changing with the "_Backup" ending.

