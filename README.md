# Programming-Test-Cloudonix

This is a simple Vert.x REST service that performs basic text analysis on a list of words. It exposes an HTTP REST interface that responds to POST requests on the URL /analyze.

Requirements
Java 17 or higher
Maven
Getting Started
Clone the repository
Run mvn clean package to build the project
Start the server by running java -jar target/vertx-rest-service-1.0.0-fat.jar
Send a POST request to http://localhost:8080/analyze with a JSON object that contains a "text" field with the string to analyze, for example:
json
Copy code
{
    "text": "Hello world"
}
Functionality
The server will compare the content of the "text" field in the JSON request to the list of words previously provided through the same API, and return a JSON response containing an object with two fields:
The field "value" will contain the word closest to the word provided in the request in terms of total character value, where character values are listed as a=1, b=2 and so on.
The field "lexical" will contain the word closest to the word provided in the request in terms of lexical closeness - i.e. that word that sorts lexically closest to the provided request.
The server will store any word submitted in a local file, so it can be compared against future requests, even across server restarts.
If no words are found to match against (as in the first request), the server will return null for both response fields.
Performance
The service is designed to perform well when handling at least 100,000 words. The responsiveness of the service should not degrade too much when the data set increases significantly.

Implementation Details
The server is implemented using Vert.x and uses the Vert.x promises/Future API for all IO operations.
The words are stored in a local file using the Vert.x FileSystem API
