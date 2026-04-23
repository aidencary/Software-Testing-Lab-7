This is a demonstration project used for CSCI 4325 Software Testing at the University of Central Arkansas.
The purpose of this demonstration is to provide an environment for creating end-to-end tests using Selenium in a container environment.

The project requires Docker or Podman to run (I use Podman): https://podman-desktop.io/

Be sure that the docker-compose functionality is installed along with the container service.

The backend is written using Maven, SpringBoot along with an H2 database for simplicity.  It is exposed on 8080 by default.
The frontend makes use of React and Typescript, along with a Vite webserver.
The end2end-tests uses Maven and Selenium and is set up to only run in a testing profile.  

To run the front and backend, simply from the project directory, type: podman compose up -d
To run the Selenium tests, type: podman compose --profile testing up end2end-tests

Notes on Unit Test Frameworks:
JUnit and Mockito are used on the Backend Unit Tests, with jacoco for code coverage.  Current code coverage will generate a report in the testing step, but fail the build on the verify step.  If you need to adjust the settings in the POM file, go to the plugins sections and change the configuration of the jacoco plugin.

Vitest is used rather than Jest for unit tests on the frontend, because the syntax is nearly the same as jest, and it reputedly works better witha  Vite server.
vitest includes a code coverage tool (v8) that is currently configured to run on the frontend tests.


