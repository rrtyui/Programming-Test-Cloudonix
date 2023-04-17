# Programming-Test-Cloudonix

1-The program exposes an HTTP REST interface that responds to POST requests on the URL /analyze.
2-When an HTTP client POSTs a JSON object with the string property "text", the server compares the content of the provided "text" field to the list of words previously provided through the same API, and returns a JSON response containing an object with two fields:
  a. The field "value" contains the word closest to the word provided in the request in terms of total character value, where character values are listed as a=1, b=2 and       so on.
  b. The field "lexical" contains the word closest to the word provided in the request in terms of lexical closeness - i.e. that word that sorts lexically closest to the      provided request.
The server stores any word submitted in a local file, so it can be compared against future requests, even across server restarts.
If no words are found to match against (as in the first request), the server returns null for both response fields.
