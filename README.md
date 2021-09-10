# hh-vacancy-parser
Service for searching and parsing vacancies from the HH.ru

HTML + Java + Jsoup

Service accepts a request to search for vacancies through the console. 
Creates the first search link, follows it, parses the number of pages with vacancies and puts the result in the number of available pages. 
If there is no data on the number of pages, it checks whether vacancies were found at all, if they are found, then sets the number of available pages to 1.  
Next, the service sequentially parses each page with vacancies, removes information from the previous search from the HTML file and then displays information (vacancy name (with a link to the vacancy on HH.ru) , address, company name, salary) into an result HTML file (vacancies.html).

![java](https://user-images.githubusercontent.com/90202470/132846100-ffcedd03-26cd-4122-a8c2-0569820b2c0d.jpg)
