# Report DAT250 Experiment 6

I ran RabbitMQ using a docker container, and implemented several classes to fulfill this experiment, specifically RabbitConfig, RabbitListenerConfig, VoteEvent and VoteEventListener. 

I found the task description a bit vague but I think I have fulfilled the requirements of the task, and observed that when I posted a vote using Bruno the exchange I created registered an increase in message rates, so the message was consumed by VoteEventListener. 

The code can be found in the repository where this report is, under java/no/hvl/dat250