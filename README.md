# FINT Personnel REST Service Adapter

This adapter enables personnel systems to supply data using REST endpoints to the FINT platform, without having to implement the FINT provider SSE API.

The tool adds two POST endpoints for supplying full and partial additions to the data.

## Endpoints

- `POST /input/{datatype}`

Used for full payload update.  Existing items will be removed first.

- `PATCH /input/{datatype}`

Used for incremental update.  Existing items will be retained.

- `DELETE /input/{datatype}`

Used for data deletion.  Items matching the payload will be removed.

## Datatype

Data type is one of:

- `person`
- `personalressurs`
- `arbeidsforhold`
- `organisasjonselement`

## Data model for payload

```json
{
  "timestamp": "<iso-8601-datetime>",
  "corrId": "<uuid>",
  "orgId": "assetId",
  "data": [
    {},
    {}
  ]
}
```

[JSON Schema for payload](src/main/resources/json/input.json)

## Adapter configuration
| Key | Description | Example |
|-----|-------------|---------|
| fint.adapter.organizations | List of orgIds the adapter handles. | rogfk.no, vaf.no, ofk.no |
| fint.adapter.endpoints.providers.`<id>` | Base URI of provider for `<id>` | https://beta.felleskomponent.no/administrasjon/personal/provider |
| fint.personnel.repository | Location of repository on file system. | |

- **[SSE Configuration](https://github.com/FINTlibs/fint-sse#sse-configuration)**
- **[OAuth Configuration](https://github.com/FINTlibs/fint-sse#oauth-configuration)** 
