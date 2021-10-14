# Setup (Download from github)

If you downloaded this repository be aware that the maven wrapper tool is not nicely packaged inside the repo.
You can download it following this guide: https://www.baeldung.com/maven-wrapper
or you can unzip a zipped version I have in this repo: e.g. unzip mvn.zip.

Also github download will not have the right permissions to run the spring boot application, therefore you may need to chmod -R 775 *.

Alternatively you can 

# How to run

Start the webserver, from your terminal:
```
./mvnw spring-boot:run
```

Start the webserver (with test data), from your terminal:
```
 ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev   
```

Then from your browser:
```
http://localhost:8080/notebooks
```

and start browsing your notebooks. or use curl for editing your notebooks (example below.)

# Test Requests (Notebooks)

Get all notebooks:
```
curl http://localhost:8080/notebooks
```

Create a new notebook:
```
curl -X POST http://localhost:8080/notebooks -H 'Content-Type: application/json' -d '{"name":"Nicola´s notebook"}'
```

Get a notebook:
```
curl http://localhost:8080/notebooks/1
```

Get a notebook and filter by tag:
```
curl http://localhost:8080/notebooks/1?tag=tag1
```

Delete a notebooks
```
curl -X DELETE http://localhost:8080/notebooks/1
```

# Test Requests (Notes)

Create a new note:
```
curl -X POST http://localhost:8080/notebooks/1/notes -H 'Content-Type: application/json' -d '{"title":"Nicola´s note title", "body":"Nicola´s note body", "tags":["tag1","tag2"]}'
```

Get a note:
```
curl http://localhost:8080/notebooks/1/notes/2
```

Update a note:
```
curl -X PUT http://localhost:8080/notebooks/1/notes/2 -H 'Content-Type: application/json' -d '{"title":"Nicola´s updated note title", "body":"Nicola´s updated note body", "tags":["tag1","tag2","tag3"]}'
```

Delete a note
```
curl -X DELETE http://localhost:8080/notebooks/1/notes/2
```
