# FINT Personnel REST Service Adapter

This adapter enables personnel systems to supply data using REST endpoints to the FINT platform, without having to implement the FINT provider SSE API.

The tool adds two POST endpoints for supplying full and partial additions to the data.

## Endpoints

## Data model for endpoints

## Adapter configuration
| Key | Description | Example |
|-----|-------------|---------|
| fint.adapter.organizations | List of orgIds the adapter handles. | rogfk.no, vaf.no, ofk.no |
| fint.adapter.endpoints.providers.`<id>` | Base URI of provider for `<id>` | https://beta.felleskomponent.no/administrasjon/personal/provider |
| fint.personnel.repository | Location of repository on file system. | |

- **[SSE Configuration](https://github.com/FINTlibs/fint-sse#sse-configuration)**
- **[OAuth Configuration](https://github.com/FINTlibs/fint-sse#oauth-configuration)** 
