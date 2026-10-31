# Arunayan Dairy Store Frontend

A responsive React 18 storefront built with Vite and Tailwind CSS.

## Requirements

- Node.js 18 or later
- npm

## Run locally

```bash
npm install
npm run dev
```

Vite prints the local development URL in the terminal.

## Build for hosting

```bash
npm run build
```

Vite writes the production site to `dist/`. Upload the contents of `dist/` to the S3 bucket served by CloudFront.

The current storefront uses local product data and cart state; it is not connected to a backend API.
