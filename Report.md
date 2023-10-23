Assignment 1
------------

# Team Members: Joey Run-Qi Lau & Lorena Raichle

# GitHub link to your (forked) repository
https://github.com/LorenaRaichle/Assignment2.git


# Task 1

1. Indicate the time necessary for the SimpleCrawler to work.

Ans: 153608 (miliseconds)

# Task 2

1. Is the flipped index smaller or larger than the initial index? What does this depend on?

Ans: The flipped index csv file is smaller than the initial index since in the initial index there are duplicates.
In the flipped index file, each keyword with corresponding urls where the keyword can be found are registered without any duplicates.

# Task 3

1. Explain your design choices for the API design.

Ans:
- For the main page ("/"), search ("/search") and lucky ("/lucky") endpoints, we decided to use the GET method to retrieve information from the server without adding any changes. To retrieve search results and render the found urls, we consider the get method as an appropriate choice.
- In the ("admin/crawler") endpoint, we would define a POST method for crawling the environment. Since an update of the index / flippedIndexFiles on the server is necessary when crawling, the POST method is appropriate.
-  In the ("admin/delete") endpoint, a DELETE Method to delete a url from the index is appropriate.
- In the ("admin/update") endpoint, we would use a PUT method to update an existing url information in the index. 
- In the ("admin/regenerate") endpoint, we would define a PATCH method to regenerate the flippedIndexFile because only partial updates are made to an existing resource. 

# Task 4

1.  Indicate the time necessary for the MultithreadCrawler to work.

Ans: 6045 (miliseconds)

2. Indicate the ratio of the time for the SimpleCrawler divided by the time for the MultithreadedCrawler to get the increase in speed.

Ans: 25.4 