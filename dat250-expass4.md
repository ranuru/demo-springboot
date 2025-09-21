# Report DAT250 Experiment 4

# Technical problems
Had some problems with JPA not including PersistenceConfiguration because it was an older version. 
Fixed it by specifying the version to be 3.2.0 (which I see was added to the guide)

# Code
https://github.com/ranuru/demo-springboot/tree/main/src/main/java/no/hvl/dat250/domain

# Database tables 
"an explanation of how you inspected the database tables and what tables were created. For the latter, you may provide screenshots."
I am not quite sure what this means, but I tried seeing what database actually was created using the database tool in IntelliJ, but I couldn't see anything. My general strategy to complete this task was to look at the specific queries in the tests and implement the correct method names and use the annotations in a way that made sense so that the tests eventually passed.