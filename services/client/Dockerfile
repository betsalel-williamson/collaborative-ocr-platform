FROM node:20-alpine AS build
WORKDIR /app

RUN corepack enable

COPY package.json tsconfig.json tsconfig.node.json vite.config.ts vitest.config.ts vitest.setup.ts index.html ./
COPY src ./src

RUN pnpm install && pnpm build

FROM node:20-alpine AS runtime
WORKDIR /app

RUN corepack enable

COPY package.json ./
COPY --from=build /app/node_modules ./node_modules
COPY --from=build /app/dist ./dist

EXPOSE 4173
CMD ["pnpm", "preview", "--host", "0.0.0.0", "--port", "4173"]

