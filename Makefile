.PHONY: help build test format format-check clean run

help: ## Show this help message
	@echo Available commands:
	@echo   build         - Compile the project (no tests)
	@echo   test          - Run tests
	@echo   format        - Auto-format code with Spotless
	@echo   format-check  - Check code formatting without modifying files
	@echo   clean         - Remove build artifacts
	@echo   run           - Run the config server on port 8888

build: ## Compile the project (no tests)
	mvn --batch-mode --no-transfer-progress compile

test: ## Run tests
	mvn --batch-mode --no-transfer-progress verify

format: ## Auto-format code with Spotless
	mvn --batch-mode --no-transfer-progress spotless:apply

format-check: ## Check code formatting without modifying files
	mvn --batch-mode --no-transfer-progress spotless:check

clean: ## Remove build artifacts
	mvn --batch-mode --no-transfer-progress clean

run: ## Run the config server on port 8888
	mvn --batch-mode --no-transfer-progress spring-boot:run
