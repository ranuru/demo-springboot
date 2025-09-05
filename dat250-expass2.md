# Report DAT250 Assignment 2
### The Task
The main task of the project was to create a functional polling app for use as a web application. In this I have succeeded, but not without a few headaches and problems along the way. I will go through each of the steps of the task, highlighting what problems and challenges I faced along the way.
### Step 1
I had already created a git project so this step was irrelevant.
### Step 2
This step was quite straight forward, and I implemented the domain classes as simple Java beans (User, Vote, Poll, VoteOption) with minimal attributes and only getters and setters. I used the lombok plugin to add getters and setters for all the attributes in the classes. I also created the PollManager, which stores User, Vote and Poll in three separate HashMaps, with a **Long id** as a key.
### Step 3 & 4
I implemented the test scenarios and the methods for handling the tests in parallel (e.g. I created the http request for creating a new user after creating the method in VoteController).
The main challenge I had here was how to implement the methods and how to use the SpringBoot library for this, and how to use the annotations. I managed to be able to eventually solve all the test scenarios and was satisfied with the results I was given when I ran the test requests in Bruno.
### Step 5
The real headache was implementing the tests inside the project so that I didn't have to manually run them in the Bruno client. The problems here was how I had implemented the methods originally, and as they were all working fine in Bruno I was confused as to why they didn't work correctly in Java. The main problem was that my methods for creating entities didn't return anything (void methods), and the tests relied on the objects being stored as return values.

