# Arunayan Dairy Store Frontend

A responsive dairy-store storefront built with React 18, Vite, and Tailwind CSS.

## Backend architecture

The planned backend uses Spring Boot 3.x and is split into three microservices:

- **user-service** (`8081`) — authentication, JWT, RBAC, and organization management
- **product-service** (`8082`) — products, inventory, and stock tracking
- **order-service** (`8083`) — orders, billing simulation, and order history

The frontend is currently a standalone UI and is not yet connected to these services. Configure the service base URLs and API integration when the backend endpoints are available. See the [project README](../README.md) for the repository layout.

## Requirements

- Node.js 18 or later
- npm

## Getting started

From this directory, install dependencies and start the development server:

```bash
npm install
npm run dev
```

Vite prints the local URL in the terminal after the server starts.

## Available scripts

| Command | Description |
| --- | --- |
| `npm run dev` | Start the Vite development server |
| `npm run build` | Create a production build in `dist/` |
| `npm run preview` | Preview the production build locally |

## Features

- Responsive storefront landing page and product catalog
- Product category filters and search
- Shopping bag with item quantity controls and subtotal
- Newsletter signup form
- Tailwind CSS styling with Vite's React plugin

Product photography and the display fonts are loaded from external image and font services, so an internet connection is needed to show them.
