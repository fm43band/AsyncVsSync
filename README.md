# Project to compare sync and async execution

This project is based on Typelevel stack.

Execution flow:

1. make a Http request to a public API to fetch some data.
2. parse the result and transform in a typed collection.
3. save sync/async in a PostgreSQL database.

A server will expose the persisted data for a web client.