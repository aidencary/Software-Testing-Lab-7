# StudentRegDemo

This is a demonstration project used for CSCI 4325 Software Testing at the University of Central Arkansas. The purpose of this demonstration is to provide an environment for creating end-to-end tests using Selenium in a container environment.

The project requires Docker or Podman to run (I use Podman): <https://podman-desktop.io/>

Be sure that the docker-compose functionality is installed along with the container service.

- The **backend** is written using Maven + Spring Boot along with an H2 database for simplicity. Exposed on `:8080` by default.
- The **frontend** uses React + TypeScript along with a Vite webserver. Exposed on `:5173`.
- The **end2end-tests** module uses Maven + Selenium and is set up to only run in a testing profile.

## Running the stack

```bash
# Front + backend
podman compose up -d

# End-to-end tests
podman compose --profile testing up end2end-tests
```

(Swap `podman` for `docker` if that's what you have.)

## Unit test frameworks

**Backend:** JUnit + Mockito, with JaCoCo for code coverage. The current configuration generates a coverage report during `mvn test` but fails the build during `mvn verify` if branch coverage drops below 80%. To adjust, edit the `jacoco-maven-plugin` block in `backend/pom.xml`.

**Frontend:** Vitest (rather than Jest) for unit tests, because the syntax is nearly identical to Jest and it integrates better with the Vite dev server. Vitest's built-in v8 coverage tool is configured in `vite.config.ts`.

---

## CI Pipeline (Lab 7)

Three GitHub Actions workflow files under [.github/workflows/](.github/workflows/), one per cadence tier. Each YAML file is heavily commented; the tables below summarise them.

| File | Workflow name | Triggers |
|---|---|---|
| [unit-tests.yml](.github/workflows/unit-tests.yml) | Unit Tests | every push (any branch) + every pull_request |
| [api-tests.yml](.github/workflows/api-tests.yml) | API Tests | pull_request to `main` + push to `main` + workflow_dispatch |
| [e2e-tests.yml](.github/workflows/e2e-tests.yml) | E2E Tests | pull_request to `main` + workflow_dispatch |

### Cadence justification

Cadences trade thoroughness against developer frustration. Cheap, fast tests run more often; expensive, flakier ones only run when the cost is justified.

| Level | Trigger | Approx. runtime | Why this cadence |
|---|---|---|---|
| **Unit (backend + frontend)** | every push, every PR | 1-2 min | Tests are cheap and catch the most common regressions. Running on every push gives devs a feedback loop that's faster than opening a PR; running again on the PR gates the merge. |
| **Coverage threshold (JaCoCo 80% branch)** | piggybacks on Unit workflow | +0 min | The `verify` phase that runs the unit tests also runs the JaCoCo gate — zero extra cost to enforce a merge-blocking coverage floor. |
| **API (Postman / Newman)** | PR to `main` + push to `main` + manual | 2-3 min | Booting Spring Boot costs ~45 s per run. Running it on every feature-branch commit is wasteful, but skipping it lets API contract drift reach `main`. PR-to-main catches that drift; push-to-main catches anything that slipped past review. |
| **E2E (Selenium inside Docker)** | PR to `main` + manual | 3-5 min | A full Docker build + headless Chrome run is the most expensive and flakiest tier. We avoid nightly cron (noise without a reviewer to act on it) and skip `on: push` (would burn CI minutes and train devs to ignore red checks). PR-to-main gates merges; the `workflow_dispatch` button covers on-demand runs when someone needs to double-check a branch. |

### Reading test results (the display)

Raw CLI logs are the fallback, not the default. Every tier surfaces results somewhere more direct:

- **GitHub Checks on PR and commit.** Every workflow publishes its JUnit XML via [`EnricoMi/publish-unit-test-result-action@v2`](https://github.com/EnricoMi/publish-unit-test-result-action). The PR page and each commit show a "Test Results" check with pass / fail counts. Expand to see which tests failed and their failure messages — no log-scrolling.
- **JaCoCo coverage comment on PR.** The backend unit job uses [`madrapps/jacoco-report@v1.7.2`](https://github.com/Madrapps/jacoco-report) to post a comment on each PR with overall coverage and changed-files coverage. The comment goes red if either drops below 80%.
- **Newman HTML dashboard as artifact.** The API workflow writes a `newman-reporter-htmlextra` report to `api-tests/results/report.html` and uploads it as the `newman-html-report` artifact. Download, open, read.
- **Selenium screenshots as artifact.** The E2E workflow uploads `end2end-tests/screenshots/` as the `selenium-screenshots` artifact. On failure, the test already captures a screenshot of the browser state at the point of failure — reviewers can see the broken UI without reproducing locally.
- **Coverage HTML reports as artifacts.** Both `jacoco-report` (backend) and `frontend-coverage` (frontend) artifacts ship the full HTML coverage drilldowns for deeper review.

### Running the CI steps locally

| Layer | Command |
|---|---|
| Backend unit + coverage gate | `cd backend && mvn verify` |
| Frontend unit + coverage | `cd frontend && npm run coverage` |
| API tests (Postman / Newman) | See [api-tests/README.md](api-tests/README.md) |
| End-to-end (Selenium + Docker) | `docker compose --profile testing up --build --abort-on-container-exit --exit-code-from end2end-tests` |

## Project layout

```
backend/                  Spring Boot 4.0.3 / Java 25 API (JPA + H2)
frontend/                 React 19 + Vite + TypeScript UI
end2end-tests/            Selenium 4 + JUnit 5 tests in their own Docker image
api-tests/                Postman collection (run via Newman in CI)
.github/workflows/        GitHub Actions pipeline (3 files, 1 per cadence)
docker-compose.yml        Orchestrates backend + frontend (+ e2e via profile)
```
