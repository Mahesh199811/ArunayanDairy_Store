# Arunayan Dairy Store

Frontend-only React storefront built with React 18, Vite, and Tailwind CSS.

## Requirements

- Node.js 18 or later
- npm

## Run locally

```bash
cd frontend
npm install
npm run dev
```

Vite prints the local development URL in the terminal.

## Build for hosting

```bash
cd frontend
npm run build
```

Vite writes the production site to `frontend/dist/`. Upload the contents of that directory to the existing S3 bucket served by CloudFront.

The current storefront is a standalone UI. Product data and cart state are local to the frontend; no backend API is connected.
