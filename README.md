## Development Notes

### TLS for Redirection Endpoint

The redirection endpoint used during OAuth2 flows requires TLS.

A self-signed SSL certificate and Certificate Authority (CA) are included under `src/main/resources` for development purposes.

You may replace these with your own certificates if needed. If using Selenium or automated browsers, ensure that the browser trusts the included CA.
