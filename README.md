# Covid 19 API

This project aims at providing a REST API for some covid statistics from the french gouvernment open data platform (<https://www.data.gouv.fr/en/pages/donnees-coronavirus>)

More information and API documentation can be found here: <https://cov19api.herokuapp.com>

## Frontend

A REACT frontend is available here: <https://cov19ui.herokuapp.com> ([https://github.com/mvoland/cov19ui](GitHub project))

## Development

This project is using the spring boot framework, and a postgresql database.

```bash
docker run --rm --name pg-docker -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=dev -d -p 5432:5432 postgres
```

