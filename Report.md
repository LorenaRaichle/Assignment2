Assignment 1
------------

# Team Members: Joey & Lorena

# GitHub link to your (forked) repository
https://github.com/LorenaRaichle/Assignment2.git


# Task 1

1. Indicate the time necessary for the SimpleCrawler to work.

Ans: duration simple crawler: 153608 (miliseconds)



# Task 2

1. Is the flipped index smaller or larger than the initial index? What does this depend on?

Ans:the flipped index csv file is smaller than the initial index since in the initial index there are duplicates.
In the flipped index file, each keyword with corresponding urls where the keyword can be found are registered without any duplicates.

# Task 3

1. Explain your design choices for the API design.

Ans:
- for the main page ("/"), search ("/search") and lucky ("/lucky") endpoints we decided to use the GET method to retrieve information from the server without adding any changes. To retrieve search results and render the found urls, we consider the get method as an appropriate choice.
- in the ("admin/crawler") endpoint we would define a POST method for crawling the environment. Since an update of the index / flippedIndexFiles on the server is necessary when crawling, the POST method is appropriate.
-  in the ("admin/delete") endpoint a DELETE Method to delete a url from the index is appropriate.
- in the ("admin/update") endpoint we would use a PUT method to upate already existing url information in the index. 
- in the ("admin/regenerate") endpoint we would define a PATCH method to regenerate the flippedIndexFile because only partial updates are made to a already existing resource.  
# Task 4

1.  Indicate the time necessary for the MultithreadCrawler to work.
    / to do
    Ans:

3. Indicate the ratio of the time for the SimpleCrawler divided by the time for the MultithreadedCrawler to get the increase in speed.
   / to do
   Ans:


