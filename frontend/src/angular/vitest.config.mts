/// <reference types="vitest" />
import { resolve } from "node:path";
import angular from "@analogjs/vite-plugin-angular";
import { defineConfig } from "vitest/config";

export default defineConfig({
  plugins: [
    angular({
      tsconfig: resolve(import.meta.dirname, "tsconfig.vitest.json"),
    }),
  ],
  test: {
    globals: true,
    environment: "jsdom",
    setupFiles: ["src/test-setup.ts"],
    include: ["src/**/*.spec.ts"],
    reporters: ["default", "junit"],
    outputFile: {
      junit: "reports/vitest-junit.xml",
    },
    coverage: {
      provider: "v8",
      reporter: ["text", "html", "lcovonly"],
      reportsDirectory: "./coverage-vitest",
      include: ["src/**/*.ts"],
      exclude: ["src/**/*.spec.ts", "src/test-setup.ts", "src/test.ts", "src/main.ts", "src/polyfills.ts"],
    },
    pool: "forks",
  },
});
