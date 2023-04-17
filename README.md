# Programming-Test-Cloudonix
# Vert.x REST Service - Word Analyzer

This is a simple Vert.x REST service that compiles a list of words and performs simple text analysis on them. It exposes an HTTP REST interface that responds to POST requests on the URL `/analyze`. When an HTTP client POSTs a JSON object with the string property `"text"`, the server compares the content of the provided `"text"` field to the list of words previously provided through the same API, and returns a JSON response containing an object with two fields:

- The field `"value"` contains the word closest to the word provided in the request in terms of total character value, where character values are listed as a=1, b=2, and so on.
- The field `"lexical"` contains the word closest to the word provided in the request in terms of lexical closeness - i.e. that word that sorts lexically closest to the provided request.

The server stores any word submitted in a local file, so it can be compared against future requests, even across server restarts. If no words are found to match against (as in the first request), the server returns null for both response fields.

## Requirements

- The program is written in Java and uses the Vert.x promises/Future API for all IO operations.
- The program can handle at least 100,000 words and should perform well even when the data set increases significantly.

## Implementation

The project is implemented using Java and Vert.x, and requires Maven to build. The project structure is as follows:

├── pom.xml
└── src
└── main
└── java
└── test
└── project1
└── Server.java


- `pom.xml` contains the Maven build configuration and lists the Vert.x dependency.
- `src/main/java/test/project1/Server.java` contains the implementation of the Vert.x HTTP server and request router.

By default, the server listens on port `8080`.


