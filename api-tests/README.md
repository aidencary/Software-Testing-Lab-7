# API Tests

Postman collection run by Newman against a live StudentRegDemo backend. CI invokes this from [.github/workflows/api-tests.yml](../.github/workflows/api-tests.yml); to run locally:

```bash
# 1. Boot the backend
cd backend && mvn spring-boot:run

# 2. In a second terminal, run the collection
npm install -g newman newman-reporter-htmlextra
newman run api-tests/student-reg.postman_collection.json -r cli,htmlextra \
  --reporter-htmlextra-export api-tests/results/report.html
```

Open `api-tests/results/report.html` for the visual dashboard.
