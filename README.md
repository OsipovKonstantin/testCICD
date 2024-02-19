# testCI/CD
Этот репозиторий создан в целях тестирования разных настроек CI/CD в GitHub Actions:
- запуск проверок кода в github при push и pullrequest в определенные ветки (main, dev)
- проверка сборки микросервисного проекта (docker compose)
- проверка стиля кода (checkstyle)
- проверка unit, integration тестов
- проверка postman тестов

## В качестве основы для изучения темы взяты 3 источника
- видео с простой настройкой CI/CD в github actions https://www.youtube.com/watch?v=o98qKQdNnms
- yml файл с проверкой стилей и запуском тестов (не postman) https://github.com/software-sales-and-installations/softplat-back/actions/runs/7331159512/workflow
- yml файл с проверкой в том числе postman тестов https://github.com/comparison-of-banking-products/cobp-backend/actions/runs/7913888561/workflow
  
#
- https://github.com/insurance-product-comparisons/backend/actions/runs/7885577496/workflow
- https://github.com/insurance-product-comparisons/backend/actions/runs/7887676522/workflow
- https://github.com/insurance-product-comparisons/backend/blob/main/.github/workflows/tests.yml
  
#
- https://github.com/comparison-of-banking-products/cobp-backend/blob/main/.github/workflows/build-and-push-image-manually.yml
- https://github.com/comparison-of-banking-products/cobp-backend/blob/main/.github/workflows/build-and-test-dev-on-pull-request.yml
- https://github.com/comparison-of-banking-products/cobp-backend/blob/main/.github/workflows/scp-and-deploy-compose-manually.yml
  
#
- https://github.com/store-electronics-and-household/store-backend/actions/runs/7319771485/workflow
- https://github.com/store-electronics-and-household/store-backend/actions/runs/7319771503/workflow
