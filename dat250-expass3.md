# Report DAT250 Experiment 3

# Issues
- No technical problems
- Biggest challenges were:
  - combining the frontend and the backend. Issues in how I designed the backend in the last assignment reappeared
    - in particular: how votes were handled. I had to go back to the backend and change how votes were handled to accommodate for the frontend, which I would assume is not optimal
  - Understanding how to use svelte. I have no experience in javascript or any other frontend language, so this was very new for me.

# Link to code
https://github.com/ranuru/demo-springboot/tree/main/experiment3/src

# Unsolved issues
- No immediate problems, but I don't believe the way I set up the frontend is really optimal for the use case. Currently, you have to create a user, remember the ID that pops up after the user is created to create polls as that user. In addition, there is no handling for which users have voted for which option, and no restriction in how many votes a single user has. E.g. this application currently relies on an honor system instead of a strict ruleset restricting users from breaking the established rules. 